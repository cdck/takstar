package xlk.takstar.paperless.fragment.score;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.UriUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.protobuf.ByteString;
import com.mogujie.tt.protobuf.InterfaceFilescorevote;
import com.mogujie.tt.protobuf.InterfaceMacro;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.adapter.ScoreAdapter;
import xlk.takstar.paperless.adapter.ScoreSubmitMemberAdapter;
import xlk.takstar.paperless.adapter.UploadFileAdapter;
import xlk.takstar.paperless.adapter.WmScreenMemberAdapter;
import xlk.takstar.paperless.base.BaseFragment;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.model.EventMessage;
import xlk.takstar.paperless.model.EventType;
import xlk.takstar.paperless.model.bean.PdfRateInfo;
import xlk.takstar.paperless.model.bean.ScoreFileBean;
import xlk.takstar.paperless.ui.RvItemDecoration;
import xlk.takstar.paperless.util.DialogUtil;
import xlk.takstar.paperless.util.FileUtil;
import xlk.takstar.paperless.util.PdfUtil;
import xlk.takstar.paperless.util.PopUtil;
import xlk.takstar.paperless.util.ToastUtil;

import static xlk.takstar.paperless.util.ConvertUtil.s2b;

/**
 * @author Created by xlk on 2021/2/23.
 * @desc
 */
public class ScoreManageFragment extends BaseFragment<ScoreManagePresenter> implements ScoreManageContract.View {

    private RecyclerView rvScore;
    private ScoreAdapter scoreAdapter;
    final int REQUEST_CODE_IMPORT_SCORE_FILE = 1;
    List<ScoreFileBean> uploadFiles = new ArrayList<>();
    private UploadFileAdapter uploadFileAdapter;
    private ScoreSubmitMemberAdapter scoreSubmitMemberAdapter;
    private boolean isManage;
    private Button btn_launch, btn_stop, btn_add;
    private EditText edt_save_address;
    private WmScreenMemberAdapter memberAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_score_manage;
    }

    @Override
    protected void initView(View inflate) {
        rvScore = inflate.findViewById(R.id.rv_score);
        //发起
        btn_launch = inflate.findViewById(R.id.btn_launch);
        btn_launch.setOnClickListener(v -> {
            if (scoreAdapter != null) {
                InterfaceFilescorevote.pbui_Type_Item_UserDefineFileScore selectedScore = scoreAdapter.getSelectedScore();
                if (selectedScore != null) {
                    if (presenter.noVoteingScore()) {
                        if (selectedScore.getVotestate() == InterfaceMacro.Pb_MeetVoteStatus.Pb_vote_notvote_VALUE) {
                            showChooseOnLineMemberDialog(selectedScore, InterfaceMacro.Pb_VoteStartFlag.Pb_MEET_VOTING_FLAG_ZERO_VALUE);
                        } else if (selectedScore.getVotestate() == InterfaceMacro.Pb_MeetVoteStatus.Pb_vote_endvote_VALUE) {
                            DialogUtil.createDialog(getContext(), getString(R.string.score_tip),
                                    getString(R.string.ensure), getString(R.string.cancel), new DialogUtil.onDialogClickListener() {
                                        @Override
                                        public void positive(DialogInterface dialog) {
                                            dialog.dismiss();
                                            showChooseOnLineMemberDialog(selectedScore, InterfaceMacro.Pb_VoteStartFlag.Pb_MEET_VOTING_FLAG_REVOTE_VALUE);
                                        }

                                        @Override
                                        public void negative(DialogInterface dialog) {
                                            dialog.dismiss();
                                        }

                                        @Override
                                        public void dismiss(DialogInterface dialog) {

                                        }
                                    });
                        }
                    } else {
                        ToastUtils.showShort(R.string.please_stop_voteing_first);
                    }
                } else {
                    ToastUtils.showShort(R.string.please_choose_score);
                }
            }
        });
        //停止
        btn_stop = inflate.findViewById(R.id.btn_stop);
        btn_stop.setOnClickListener(v -> {
            if (scoreAdapter != null) {
                InterfaceFilescorevote.pbui_Type_Item_UserDefineFileScore selectedScore = scoreAdapter.getSelectedScore();
                if (selectedScore != null) {
                    if (selectedScore.getVotestate() == InterfaceMacro.Pb_MeetVoteStatus.Pb_vote_voteing_VALUE) {
                        jni.stopScore(selectedScore.getVoteid());
                    } else {
                        ToastUtils.showShort(R.string.stop_score_tip);
                    }
                } else {
                    ToastUtils.showShort(R.string.please_choose_score);
                }
            }
        });
        //查看
        inflate.findViewById(R.id.btn_view).setOnClickListener(v -> {
            InterfaceFilescorevote.pbui_Type_Item_UserDefineFileScore item = scoreAdapter.getSelectedScore();
            if (item == null) {
                ToastUtils.showShort(R.string.please_choose_score);
                return;
            }
            if (item.getVotestate() == InterfaceMacro.Pb_MeetVoteStatus.Pb_vote_notvote_VALUE) {
                ToastUtils.showShort(R.string.stop_score_tip);
                return;
            }
            presenter.queryScoreSubmittedScore(item.getVoteid());
            viewFileScore(item);
        });
        //添加
        btn_add = inflate.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(v -> {
            showAddPop();
        });
    }

    @Override
    public void updateScoreSubmitMemberList() {
        if (scoreSubmitMemberAdapter == null) {
            scoreSubmitMemberAdapter = new ScoreSubmitMemberAdapter(presenter.scoreMembers);
        } else {
            scoreSubmitMemberAdapter.notifyDataSetChanged();
        }
    }

    private void viewFileScore(InterfaceFilescorevote.pbui_Type_Item_UserDefineFileScore item) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.pop_file_score, null, false);
        View meet_fl = getActivity().findViewById(R.id.meet_fl);
        View meet_left_ll = getActivity().findViewById(R.id.meet_left_ll);
        int width = meet_fl.getWidth();
        int height = meet_fl.getHeight();
        int width1 = meet_left_ll.getWidth();
        PopupWindow pop = PopUtil.createPopupWindow(inflate, width * 2 / 3, height * 3 / 4, rvScore, Gravity.CENTER, width1 / 2, 0);

        TextView tv_content = inflate.findViewById(R.id.tv_content);
        tv_content.setText(item.getContent().toStringUtf8());
        TextView tv_file_name = inflate.findViewById(R.id.tv_file_name);
        TextView tv_view_file = inflate.findViewById(R.id.tv_view_file);
        String fileName = jni.queryFileNameByMediaId(item.getFileid());
        tv_file_name.setText(fileName);
        tv_view_file.setVisibility(fileName.isEmpty() ? View.GONE : View.VISIBLE);
        tv_view_file.setOnClickListener(v -> {
            String filePath = Constant.file_dir + fileName;
            boolean fileExists = FileUtils.isFileExists(filePath);
            if (fileExists) {
                File file = new File(filePath);
                FileUtil.openFile(getContext(), file);
            } else {
                jni.downloadFile(filePath, item.getFileid(), 1, 0, Constant.DOWNLOAD_SHOULD_OPEN_FILE);
            }
        });

        RecyclerView rv_rate_file = inflate.findViewById(R.id.rv_rate_file);
        TextView tv_notation = inflate.findViewById(R.id.tv_notation);
        tv_notation.setText(item.getMode() == 1 ? getString(R.string.yes) : getString(R.string.no));
        TextView tv_yprs = inflate.findViewById(R.id.tv_yprs);
        tv_yprs.setText(String.valueOf(item.getShouldmembernum()));
        TextView tv_yiprs = inflate.findViewById(R.id.tv_yiprs);
        tv_yiprs.setText(String.valueOf(item.getRealmembernum()));
        TextView tv_a_score = inflate.findViewById(R.id.tv_a_score);
        TextView tv_b_score = inflate.findViewById(R.id.tv_b_score);
        TextView tv_c_score = inflate.findViewById(R.id.tv_c_score);
        TextView tv_d_score = inflate.findViewById(R.id.tv_d_score);
        List<Integer> itemsumscoreList = item.getItemsumscoreList();
        int total = 0;
        for (int i = 0; i < itemsumscoreList.size(); i++) {
            String text = String.valueOf(itemsumscoreList.get(i));
            if (i == 0) tv_a_score.setText(text);
            if (i == 1) tv_b_score.setText(text);
            if (i == 2) tv_c_score.setText(text);
            if (i == 3) tv_d_score.setText(text);
            total += itemsumscoreList.get(i);
        }
        TextView tv_total_score = inflate.findViewById(R.id.tv_total_score);
        tv_total_score.setText(String.valueOf(total));
        TextView tv_average_score = inflate.findViewById(R.id.tv_average_score);
        if (item.getRealmembernum() > 0) {
            double div = Constant.div(total, item.getRealmembernum(), 2);
            tv_average_score.setText(String.valueOf(div));
        }
        RecyclerView rv_member = inflate.findViewById(R.id.rv_member);
        if (scoreSubmitMemberAdapter == null) {
            scoreSubmitMemberAdapter = new ScoreSubmitMemberAdapter(presenter.scoreMembers);
        }
        rv_member.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_member.addItemDecoration(new RvItemDecoration(getContext()));
        rv_member.setAdapter(scoreSubmitMemberAdapter);
        inflate.findViewById(R.id.iv_close).setOnClickListener(v -> pop.dismiss());
        inflate.findViewById(R.id.btn_cancel).setOnClickListener(v -> pop.dismiss());
        inflate.findViewById(R.id.btn_export).setOnClickListener(v -> {
            if (presenter.scoreMembers.isEmpty()) {
                ToastUtil.showShort(R.string.no_data_to_export);
                return;
            }
            showExportFilePop(new PdfRateInfo(fileName, item, presenter.scoreMembers));
            pop.dismiss();
        });
    }

    @Override
    public void updateExportDirPath(String dirPath) {
        if (edt_save_address != null) {
            edt_save_address.setText(dirPath);
            edt_save_address.setSelection(dirPath.length());
        }
    }

    private void showExportFilePop(PdfRateInfo pdfRateInfo) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.pop_export_config, null);
        View ll_content = getActivity().findViewById(R.id.meet_fl);
        View rv_navigation = getActivity().findViewById(R.id.meet_left_ll);
        int width = ll_content.getWidth();
        int height = ll_content.getHeight();
        int width1 = rv_navigation.getWidth();
        PopupWindow pop = PopUtil.createPopupWindow(inflate, width * 2 / 3, height * 2 / 3, rvScore, Gravity.CENTER, width1 / 2, 0);
        EditText edt_file_name = inflate.findViewById(R.id.edt_file_name);
        TextView tv_suffix = inflate.findViewById(R.id.tv_suffix);
        tv_suffix.setText(".pdf");
        edt_save_address = inflate.findViewById(R.id.edt_save_address);
        edt_save_address.setKeyListener(null);
        edt_save_address.setText(Constant.export_dir);
        edt_save_address.setSelection(Constant.export_dir.length());
        inflate.findViewById(R.id.btn_choose_dir).setOnClickListener(v -> {
            String currentDirPath = edt_save_address.getText().toString().trim();
            if (currentDirPath.isEmpty()) {
                currentDirPath = Constant.root_dir;
            }
            EventBus.getDefault().post(new EventMessage.Builder().type(EventType.CHOOSE_DIR_PATH).objects(Constant.CHOOSE_DIR_TYPE_EXPORT_SOCRE_RESULT, currentDirPath).build());
        });
        inflate.findViewById(R.id.iv_close).setOnClickListener(v -> pop.dismiss());
        inflate.findViewById(R.id.btn_cancel).setOnClickListener(v -> pop.dismiss());
        inflate.findViewById(R.id.btn_define).setOnClickListener(v -> {
            String fileName = edt_file_name.getText().toString().trim();
            String addr = edt_save_address.getText().toString().trim();
            if (fileName.isEmpty() || addr.isEmpty()) {
                ToastUtil.showShort(R.string.please_enter_file_name_and_addr);
                return;
            }
            PdfUtil.exportScore(addr, fileName, pdfRateInfo);
            pop.dismiss();
        });
    }

    private void showAddPop() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.pop_add_rate, null, false);
        View meet_fl = getActivity().findViewById(R.id.meet_fl);
        View meet_left_ll = getActivity().findViewById(R.id.meet_left_ll);
        int width = meet_fl.getWidth();
        int height = meet_fl.getHeight();
        int width1 = meet_left_ll.getWidth();
        PopupWindow pop = PopUtil.createPopupWindow(inflate, width * 2 / 3, height * 2 / 3, rvScore, Gravity.CENTER, width1 / 2, 0);
        EditText edt_rate_description = inflate.findViewById(R.id.edt_rate_description);
        CheckBox cb_use_file_name = inflate.findViewById(R.id.cb_use_file_name);
        Spinner sp_notation = inflate.findViewById(R.id.sp_notation);
        EditText edt_score_a = inflate.findViewById(R.id.edt_score_a);
        EditText edt_score_b = inflate.findViewById(R.id.edt_score_b);
        EditText edt_score_c = inflate.findViewById(R.id.edt_score_c);
        EditText edt_score_d = inflate.findViewById(R.id.edt_score_d);
        RecyclerView rv_file = inflate.findViewById(R.id.rv_file);
        rv_file.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_file.addItemDecoration(new RvItemDecoration(getContext()));
        rv_file.setAdapter(uploadFileAdapter);
        uploadFileAdapter.setOnItemClickListener((adapter, view, position) -> uploadFileAdapter.choose(uploadFiles.get(position).getFilePath()));
        inflate.findViewById(R.id.btn_upload).setOnClickListener(v -> chooseLocalFile(REQUEST_CODE_IMPORT_SCORE_FILE));
        inflate.findViewById(R.id.iv_close).setOnClickListener(v -> pop.dismiss());
        inflate.findViewById(R.id.btn_cancel).setOnClickListener(v -> pop.dismiss());
        inflate.findViewById(R.id.btn_define).setOnClickListener(v -> {
            String content = edt_rate_description.getText().toString();
            int modeIndex = sp_notation.getSelectedItemPosition() == 0 ? 1 : 0;
            String a = edt_score_a.getText().toString();
            String b = edt_score_b.getText().toString();
            String c = edt_score_c.getText().toString();
            String d = edt_score_d.getText().toString();
            if (a.isEmpty() || b.isEmpty() || c.isEmpty()) {
                ToastUtil.showShort(R.string.first_three_options_must_be_filled_in);
                return;
            }
            if (uploadFiles.isEmpty()) {
                ToastUtil.showShort(R.string.please_add_score_file_first);
                return;
            }
            List<ByteString> answers = new ArrayList<>();
            answers.add(s2b(a));
            answers.add(s2b(b));
            answers.add(s2b(c));
            if (!d.isEmpty()) answers.add(s2b(d));
            List<InterfaceFilescorevote.pbui_Type_Item_UserDefineFileScore> addScores = new ArrayList<>();
            if (uploadFiles.size() > 1) {
                for (ScoreFileBean bean : uploadFiles) {
                    InterfaceFilescorevote.pbui_Type_Item_UserDefineFileScore build = InterfaceFilescorevote.pbui_Type_Item_UserDefineFileScore.newBuilder()
                            .setContent(s2b(bean.getFilePath()))
                            .setFileid(bean.getMediaId())
                            .setMode(modeIndex)
                            .setSelectcount(answers.size())
                            .setTimeouts(300)
                            .addAllVoteText(answers).build();
                    addScores.add(build);
                }
            } else {
                ScoreFileBean bean = uploadFiles.get(0);
                if (cb_use_file_name.isChecked()) {
                    content = bean.getFilePath();
                } else {
                    if (content.isEmpty()) {
                        ToastUtil.showShort(R.string.please_enter_score_content_first);
                        return;
                    }
                }
                InterfaceFilescorevote.pbui_Type_Item_UserDefineFileScore build = InterfaceFilescorevote.pbui_Type_Item_UserDefineFileScore.newBuilder()
                        .setContent(s2b(content))
                        .setFileid(bean.getMediaId())
                        .setMode(modeIndex)
                        .setSelectcount(answers.size())
                        .setTimeouts(300)
                        .addAllVoteText(answers).build();
                addScores.add(build);
            }
            jni.addScore(0, addScores);
            pop.dismiss();
        });
        inflate.findViewById(R.id.btn_delete).setOnClickListener(v -> {
            List<ScoreFileBean> selectedFiles = uploadFileAdapter.getSelectedFiles();
            if (selectedFiles.isEmpty()) {
                ToastUtil.showShort(R.string.please_choose_file_first);
                return;
            }
            Iterator<ScoreFileBean> iterator = uploadFiles.iterator();
            while (iterator.hasNext()) {
                ScoreFileBean next = iterator.next();
                if (selectedFiles.contains(next)) {
                    iterator.remove();
                }
            }
            uploadFileAdapter.notifyDataSetChanged();
        });
        pop.setOnDismissListener(() -> uploadFiles.clear());
    }

    @Override
    protected ScoreManagePresenter initPresenter() {
        return new ScoreManagePresenter(this);
    }

    @Override
    protected void onShow() {
        initial();
    }

    @Override
    protected void initial() {
        isManage = getArguments().getBoolean("isManage");
        btn_launch.setVisibility(isManage ? View.VISIBLE : View.GONE);
        btn_stop.setVisibility(isManage ? View.VISIBLE : View.GONE);
        btn_add.setVisibility(isManage ? View.VISIBLE : View.GONE);

        presenter.queryMember();
        presenter.queryMemberDetailed();
        presenter.queryScore();
        uploadFileAdapter = new UploadFileAdapter(uploadFiles);
    }

    @Override
    public void updateScoreRv() {
        if (scoreAdapter == null) {
            scoreAdapter = new ScoreAdapter(R.layout.item_score, presenter.scoreList);
            rvScore.setLayoutManager(new LinearLayoutManager(getContext()));
            rvScore.setAdapter(scoreAdapter);
            scoreAdapter.setOnItemClickListener((adapter, view, position) -> {
                InterfaceFilescorevote.pbui_Type_Item_UserDefineFileScore item = presenter.scoreList.get(position);
                scoreAdapter.setSelectedId(item.getVoteid());
            });
        } else {
            scoreAdapter.notifyDataSetChanged();
            scoreAdapter.notifySelectedScore();
        }
    }

    @Override
    public void updateOnLineMemberList() {
        if (memberAdapter != null) {
            memberAdapter.notifyDataSetChanged();
        }
    }

    private void showChooseOnLineMemberDialog(InterfaceFilescorevote.pbui_Type_Item_UserDefineFileScore item, int voteflag) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.pop_online_member, null, false);
        View meet_fl = getActivity().findViewById(R.id.meet_fl);
        View meet_left_ll = getActivity().findViewById(R.id.meet_left_ll);
        int width = meet_fl.getWidth();
        int height = meet_fl.getHeight();
        int width1 = meet_left_ll.getWidth();
        PopupWindow memberPop = PopUtil.createPopupWindow(inflate, width * 2 / 3, height * 2 / 3, rvScore, Gravity.CENTER, width1 / 2, 0);
        CheckBox cb_all = inflate.findViewById(R.id.cb_all);
        RecyclerView rv_member = inflate.findViewById(R.id.rv_member);
        memberAdapter = new WmScreenMemberAdapter(R.layout.item_wm_screen, presenter.onlineMembers);
        rv_member.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_member.setAdapter(memberAdapter);
        memberAdapter.setOnItemClickListener((adapter, view, position) -> {
            memberAdapter.choose(presenter.onlineMembers.get(position).getDeviceDetailInfo().getDevcieid());
            cb_all.setChecked(memberAdapter.isChooseAll());
        });
        cb_all.setOnClickListener(v -> {
            boolean checked = cb_all.isChecked();
            cb_all.setChecked(checked);
            memberAdapter.setChooseAll(checked);
        });
        inflate.findViewById(R.id.btn_start_score).setOnClickListener(v -> {
            List<Integer> memberIds = memberAdapter.getSelectedMemberIds();
            if (memberIds.isEmpty()) {
                ToastUtils.showShort(R.string.please_choose_member);
            } else {
                jni.startScore(item.getVoteid(), voteflag, 0, memberIds);
                memberPop.dismiss();
            }
        });
        inflate.findViewById(R.id.iv_close).setOnClickListener(v -> memberPop.dismiss());
        inflate.findViewById(R.id.btn_cancel).setOnClickListener(v -> memberPop.dismiss());
    }

    @Override
    public void addFile2List(String filePath, int mediaId) {
        for (int i = 0; i < uploadFiles.size(); i++) {
            ScoreFileBean bean = uploadFiles.get(i);
            if (bean.getFilePath().equals(filePath) && bean.getMediaId() == mediaId) {
                ToastUtil.showShort(R.string.file_already_exists);
                return;
            }
        }
        uploadFiles.add(new ScoreFileBean(mediaId, filePath));
        uploadFileAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            File file = UriUtils.uri2File(uri);
            if (file != null && file.exists()) {
                if (requestCode == REQUEST_CODE_IMPORT_SCORE_FILE) {
                    jni.uploadFile(InterfaceMacro.Pb_Upload_Flag.Pb_MEET_UPLOADFLAG_ZERO_VALUE, 0,
                            InterfaceMacro.Pb_MeetFileAttrib.Pb_MEETFILE_ATTRIB_ZERO_VALUE,
                            file.getName(), file.getAbsolutePath(), 0, Constant.UPLOAD_SCORE_FILE);
                }
            }
        }
    }
}
