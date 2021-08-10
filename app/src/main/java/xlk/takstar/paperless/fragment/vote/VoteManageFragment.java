package xlk.takstar.paperless.fragment.vote;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.UriUtils;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.protobuf.ByteString;
import com.mogujie.tt.protobuf.InterfaceMacro;
import com.mogujie.tt.protobuf.InterfaceVote;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.adapter.MemberDetailAdapter;
import xlk.takstar.paperless.adapter.SubmitMemberAdapter;
import xlk.takstar.paperless.adapter.VoteAdapter;
import xlk.takstar.paperless.base.BaseFragment;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.model.EventMessage;
import xlk.takstar.paperless.model.EventType;
import xlk.takstar.paperless.model.bean.ExportSubmitMember;
import xlk.takstar.paperless.ui.MyPercentFormatter;
import xlk.takstar.paperless.ui.RvItemDecoration;
import xlk.takstar.paperless.util.DateUtil;
import xlk.takstar.paperless.util.JxlUtil;
import xlk.takstar.paperless.util.LogUtil;
import xlk.takstar.paperless.util.MaxLengthFilter;
import xlk.takstar.paperless.util.PopUtil;
import xlk.takstar.paperless.util.ToastUtil;

import static xlk.takstar.paperless.util.ConvertUtil.s2b;

/**
 * @author Created by xlk on 2020/12/8.
 * @desc
 */
public class VoteManageFragment extends BaseFragment<VoteManagePresenter> implements VoteManageContract.View, View.OnClickListener {
    /**
     * =true 投票页面, =false 选举页面
     */
    public boolean isVote;
    private boolean isManage;
    private RecyclerView rv_vote;
    private VoteAdapter voteAdapter;
    private MemberDetailAdapter memberDetailAdapter;
    private int REQUEST_CODE_VOTE = 1;
    private PopupWindow voteConfigPop, memberPop, detailPop, chartPop, modifyElectionsPop, modifyVotePop, createVotePop;
    private EditText edt_save_address;
    private String currentExportVoteContent;
    private Button btn_launch, btn_stop, btn_create, btn_modify, btn_delete, btn_view_details, btn_view_chart, btn_export_data, btn_import_data;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_vote_manage;
    }

    @Override
    protected void initView(View inflate) {
        rv_vote = inflate.findViewById(R.id.rv_vote);
        btn_launch = inflate.findViewById(R.id.btn_launch);
        btn_launch.setOnClickListener(this);
        btn_stop = inflate.findViewById(R.id.btn_stop);
        btn_stop.setOnClickListener(this);
        btn_create = inflate.findViewById(R.id.btn_create);
        btn_create.setOnClickListener(this);
        btn_modify = inflate.findViewById(R.id.btn_modify);
        btn_modify.setOnClickListener(this);
        btn_delete = inflate.findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(this);
        btn_view_details = inflate.findViewById(R.id.btn_view_details);
        btn_view_details.setOnClickListener(this);
        btn_view_chart = inflate.findViewById(R.id.btn_view_chart);
        btn_view_chart.setOnClickListener(this);
        btn_export_data = inflate.findViewById(R.id.btn_export_data);
        btn_export_data.setOnClickListener(this);
        btn_import_data = inflate.findViewById(R.id.btn_import_data);
        btn_import_data.setOnClickListener(this);
    }

    @Override
    protected VoteManagePresenter initPresenter() {
        return new VoteManagePresenter(this);
    }

    @Override
    protected void onShow() {
        initial();
    }

    @Override
    protected void initial() {
        isVote = getArguments().getBoolean("isVote");
        isManage = getArguments().getBoolean("isManage");
        presenter.setIsVote(isVote);
        btn_launch.setVisibility(isManage ? View.VISIBLE : View.GONE);
        btn_stop.setVisibility(isManage ? View.VISIBLE : View.GONE);
        btn_create.setVisibility(isManage ? View.VISIBLE : View.GONE);
        btn_modify.setVisibility(isManage ? View.VISIBLE : View.GONE);
        btn_delete.setVisibility(isManage ? View.VISIBLE : View.GONE);
        btn_export_data.setVisibility(isManage ? View.VISIBLE : View.GONE);
        btn_import_data.setVisibility(isManage ? View.VISIBLE : View.GONE);
        presenter.queryVote();
        presenter.queryMember();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_launch: {
                InterfaceVote.pbui_Item_MeetVoteDetailInfo vote = voteAdapter.getSelect();
                if (vote == null) {
                    ToastUtils.showShort(R.string.please_choose_vote);
                    return;
                }
                if (vote.getVotestate() != InterfaceMacro.Pb_MeetVoteStatus.Pb_vote_notvote_VALUE) {
                    ToastUtils.showShort(isVote ? R.string.start_vote_tip : R.string.start_election_tip);
                    return;
                }
                presenter.queryMember();
                launchVote(vote);
                break;
            }
            case R.id.btn_stop: {
                InterfaceVote.pbui_Item_MeetVoteDetailInfo vote = voteAdapter.getSelect();
                if (vote == null) {
                    ToastUtils.showShort(R.string.please_choose_vote);
                    return;
                }
                if (vote.getVotestate() != InterfaceMacro.Pb_MeetVoteStatus.Pb_vote_voteing_VALUE) {
                    ToastUtils.showShort(isVote ? R.string.stop_vote_tip : R.string.stop_election_tip);
                    return;
                }
                jni.stopVote(vote.getVoteid());
                break;
            }
            case R.id.btn_create: {
                createVote();
                break;
            }
            case R.id.btn_modify: {
                InterfaceVote.pbui_Item_MeetVoteDetailInfo vote = voteAdapter.getSelect();
                if (vote == null) {
                    ToastUtils.showShort(R.string.please_choose_vote);
                    return;
                }
                if (vote.getVotestate() != InterfaceMacro.Pb_MeetVoteStatus.Pb_vote_notvote_VALUE) {
                    ToastUtils.showShort(isVote ? R.string.start_vote_tip : R.string.start_election_tip);
                    return;
                }
                if (isVote) {
                    modifyVote(vote);
                } else {
                    modifyElections(vote);
                }
                break;
            }
            case R.id.btn_delete: {
                InterfaceVote.pbui_Item_MeetVoteDetailInfo vote = voteAdapter.getSelect();
                if (vote == null) {
                    ToastUtils.showShort(R.string.please_choose_vote);
                    return;
                }
                jni.deleteVote(vote.getVoteid());
                break;
            }
            case R.id.btn_view_details: {
                InterfaceVote.pbui_Item_MeetVoteDetailInfo vote = voteAdapter.getSelect();
                if (vote == null) {
                    ToastUtils.showShort(R.string.please_choose_vote);
                    return;
                }
                if (vote.getMode() == InterfaceMacro.Pb_MeetVoteMode.Pb_VOTEMODE_signed_VALUE) {
                    if (vote.getVotestate() != InterfaceMacro.Pb_MeetVoteStatus.Pb_vote_notvote_VALUE) {
                        presenter.querySubmittedVoters(vote, true);
                    } else {
                        ToastUtils.showShort(isVote ? R.string.view_vote_tip : R.string.view_election_tip);
                    }
                } else {
                    ToastUtils.showShort(isVote ? R.string.please_choose_snotation_vote : R.string.please_choose_snotation_election);
                }
                break;
            }
            case R.id.btn_view_chart: {
                InterfaceVote.pbui_Item_MeetVoteDetailInfo vote = voteAdapter.getSelect();
                if (vote == null) {
                    ToastUtils.showShort(R.string.please_choose_vote);
                    return;
                }
                if (vote.getVotestate() != InterfaceMacro.Pb_MeetVoteStatus.Pb_vote_notvote_VALUE) {
                    presenter.querySubmittedVoters(vote, false);
                } else {
                    ToastUtils.showShort(isVote ? R.string.view_vote_tip : R.string.view_election_tip);
                }
                break;
            }
            case R.id.btn_export_data: {
                if (presenter.votes.isEmpty()) {
                    ToastUtils.showShort(R.string.no_vote_data);
                    break;
                }
                InterfaceVote.pbui_Item_MeetVoteDetailInfo select = voteAdapter.getSelect();
                if (select == null) {
                    ToastUtils.showShort(R.string.please_choose_vote_first);
                    return;
                }
                showExportPop(0, select);
                break;
            }
            case R.id.btn_import_data: {
                chooseLocalFile(REQUEST_CODE_VOTE);
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void updateExportDirPath(String dirPath) {
        if (edt_save_address != null) {
            edt_save_address.setText(dirPath);
            edt_save_address.setSelection(dirPath.length());
        }
    }

    /**
     * @param exportType =0投票信息，=1投票提交人详情
     * @param vote       要导出的投票
     */
    private void showExportPop(int exportType, InterfaceVote.pbui_Item_MeetVoteDetailInfo vote) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.pop_export_config, null);
        View meet_fl = getActivity().findViewById(R.id.meet_fl);
        View meet_left_ll = getActivity().findViewById(R.id.meet_left_ll);
        int width = meet_fl.getWidth();
        int height = meet_fl.getHeight();
        int width1 = meet_left_ll.getWidth();
        PopupWindow pop = PopUtil.createPopupWindow(inflate, width * 2 / 3, height * 2 / 3, rv_vote, Gravity.CENTER, width1 / 2, 0);
        EditText edt_file_name = inflate.findViewById(R.id.edt_file_name);
        edt_file_name.setText(vote.getContent().toStringUtf8());
        edt_save_address = inflate.findViewById(R.id.edt_save_address);
        edt_save_address.setKeyListener(null);
        edt_save_address.setText(Constant.export_dir);
        edt_save_address.setSelection(Constant.export_dir.length());
        inflate.findViewById(R.id.btn_choose_dir).setOnClickListener(v -> {
            String currentDirPath = edt_save_address.getText().toString().trim();
            if (currentDirPath.isEmpty()) {
                currentDirPath = Constant.root_dir;
            }
            EventBus.getDefault().post(new EventMessage.Builder().type(EventType.CHOOSE_DIR_PATH).objects(Constant.CHOOSE_DIR_TYPE_PAGE_VOTE_MANAGE, currentDirPath).build());
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
            if (exportType == 0) {
                List<InterfaceVote.pbui_Item_MeetVoteDetailInfo> votes = new ArrayList<>();
                votes.add(vote);
                JxlUtil.exportVoteInfo(addr, fileName, isVote ? getString(R.string.vote_fileName) : getString(R.string.elections_filename), votes);
            } else {
                String[] strings = presenter.queryYd(vote);
                String createTime = DateUtil.nowDate();
                ExportSubmitMember exportSubmitMember = new ExportSubmitMember(vote.getContent().toStringUtf8(), createTime, strings[0], strings[1], strings[2], strings[3], presenter.submitMembers);
                JxlUtil.exportSubmitMember(addr, fileName, exportSubmitMember);
            }
            pop.dismiss();
        });
    }

    private void createVote() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.pop_create_vote, null, false);
        View meet_fl = getActivity().findViewById(R.id.meet_fl);
        View meet_left_ll = getActivity().findViewById(R.id.meet_left_ll);
        int width = meet_fl.getWidth();
        int height = meet_fl.getHeight();
        int width1 = meet_left_ll.getWidth();
        int height1 = meet_left_ll.getHeight();
        LogUtils.i(TAG, "createVote 宽高=" + width + "," + height + ",功能菜单宽高=" + width1 + "," + height1);
        createVotePop = PopUtil.createPopupWindow(inflate, width * 2 / 3, height * 2 / 3, rv_vote, Gravity.CENTER, width1 / 2, 0);
        createVotePop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        CreateVoteViewHolder holder = new CreateVoteViewHolder(inflate);
        createHolderEvent(holder);
    }

    private void createHolderEvent(CreateVoteViewHolder holder) {
        holder.tvOperationTitle.setText(isVote ? getString(R.string.create_vote) : getString(R.string.create_election));
        holder.tvTitle.setText(isVote ? getString(R.string.vote_title_) : getString(R.string.elections_title_));
        holder.tvType.setText(isVote ? getString(R.string.vote_type) : getString(R.string.election_type));
        if (isVote) {
            holder.optionAEdt.setText(getString(R.string.favour));
            holder.optionBEdt.setText(getString(R.string.against));
            holder.optionCEdt.setText(getString(R.string.abstain));
        }
//        holder.optionAEdt.setFilters(new InputFilter[]{new MaxLengthFilter(20)});
//        holder.optionBEdt.setFilters(new InputFilter[]{new MaxLengthFilter(20)});
//        holder.optionCEdt.setFilters(new InputFilter[]{new MaxLengthFilter(20)});
//        holder.optionDEdt.setFilters(new InputFilter[]{new MaxLengthFilter(20)});
//        holder.optionEEdt.setFilters(new InputFilter[]{new MaxLengthFilter(20)});
        if (!isVote) {
            holder.optionDLl.setVisibility(View.VISIBLE);
            holder.optionELl.setVisibility(View.VISIBLE);
            //当前是创建选举
            holder.spVoteType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    holder.optionALl.setVisibility(View.VISIBLE);
                    holder.optionBLl.setVisibility(View.VISIBLE);
                    holder.optionCLl.setVisibility(View.VISIBLE);
                    holder.optionDLl.setVisibility(View.VISIBLE);
                    holder.optionELl.setVisibility(View.VISIBLE);
                    if (position == 5) {
                        //3选2
                        holder.optionDLl.setVisibility(View.GONE);
                        holder.optionELl.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else {
            holder.tvType.setVisibility(View.GONE);
            holder.spVoteType.setVisibility(View.GONE);
        }
        holder.btnEnsure.setOnClickListener(v -> {
            String title = holder.edtTitle.getText().toString().trim();
            String aStr = holder.optionAEdt.getText().toString().trim();
            String bStr = holder.optionBEdt.getText().toString().trim();
            String cStr = holder.optionCEdt.getText().toString().trim();
            String dStr = holder.optionDEdt.getText().toString().trim();
            String eStr = holder.optionEEdt.getText().toString().trim();
            if (title.isEmpty()) {
                ToastUtils.showShort(R.string.please_enter_title);
                return;
            }
            if (aStr.isEmpty() || bStr.isEmpty() || cStr.isEmpty()) {
                ToastUtils.showShort(R.string.please_enter_option_content);
                return;
            }
            int voteType = Constant.getSpinnerVoteType(holder.spVoteType.getSelectedItemPosition());
            if (voteType == InterfaceMacro.Pb_MeetVote_SelType.Pb_VOTE_TYPE_4_5_VALUE || voteType == InterfaceMacro.Pb_MeetVote_SelType.Pb_VOTE_TYPE_3_5_VALUE
                    || voteType == InterfaceMacro.Pb_MeetVote_SelType.Pb_VOTE_TYPE_2_5_VALUE) {
                if (dStr.isEmpty() || eStr.isEmpty()) {
                    ToastUtils.showShort(R.string.please_enter_option_content);
                    return;
                }
            }
            List<ByteString> all = new ArrayList<>();
            all.add(s2b(aStr));
            all.add(s2b(bStr));
            all.add(s2b(cStr));
            if (!isVote) {
                if (!dStr.isEmpty()) all.add(s2b(dStr));
                if (!eStr.isEmpty()) all.add(s2b(eStr));
            }
            InterfaceVote.pbui_Item_MeetOnVotingDetailInfo.Builder builder = InterfaceVote.pbui_Item_MeetOnVotingDetailInfo.newBuilder();
            builder.setContent(s2b(title));
            builder.setMaintype(isVote ? InterfaceMacro.Pb_MeetVoteType.Pb_VOTE_MAINTYPE_vote_VALUE : InterfaceMacro.Pb_MeetVoteType.Pb_VOTE_MAINTYPE_election_VALUE);
            builder.setMode(InterfaceMacro.Pb_MeetVoteMode.Pb_VOTEMODE_signed_VALUE);
            builder.setType(isVote ? InterfaceMacro.Pb_MeetVote_SelType.Pb_VOTE_TYPE_SINGLE_VALUE : voteType);
            builder.setSelectcount(all.size());
            builder.setTimeouts(0);
            builder.addAllText(all);
            jni.createVote(builder.build());
            createVotePop.dismiss();
        });
        holder.ivClose.setOnClickListener(v -> createVotePop.dismiss());
        holder.btnCancel.setOnClickListener(v -> createVotePop.dismiss());
    }

    private void modifyVote(InterfaceVote.pbui_Item_MeetVoteDetailInfo vote) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.pop_modify_vote, null, false);
        View meet_fl = getActivity().findViewById(R.id.meet_fl);
        View meet_left_ll = getActivity().findViewById(R.id.meet_left_ll);
        int width = meet_fl.getWidth();
        int height = meet_fl.getHeight();
        int width1 = meet_left_ll.getWidth();
        int height1 = meet_left_ll.getHeight();
        LogUtils.i(TAG, "modifyVote 宽高=" + width + "," + height + ",功能菜单宽高=" + width1 + "," + height1);
        modifyVotePop = PopUtil.createPopupWindow(inflate, width * 2 / 3, height * 2 / 3, rv_vote, Gravity.CENTER, width1 / 2, 0);
        VoteViewHolder viewHolder = new VoteViewHolder(inflate);
        voteHolderEvent(viewHolder, vote);
    }

    private void voteHolderEvent(VoteViewHolder holder, InterfaceVote.pbui_Item_MeetVoteDetailInfo vote) {
        holder.edt_vote_title.setText(vote.getContent().toStringUtf8());
        holder.sp_notation.setSelection(vote.getMode() == 0 ? 1 : 0);
        holder.option_a_edt.setFilters(new InputFilter[]{new MaxLengthFilter(20)});
        holder.option_b_edt.setFilters(new InputFilter[]{new MaxLengthFilter(20)});
        holder.option_c_edt.setFilters(new InputFilter[]{new MaxLengthFilter(20)});

        List<InterfaceVote.pbui_SubItem_VoteItemInfo> itemList = vote.getItemList();
        for (int i = 0; i < itemList.size(); i++) {
            String answer = itemList.get(i).getText().toStringUtf8();
            if (i == 0) {
                holder.option_a_edt.setText(answer);
            } else if (i == 1) {
                holder.option_b_edt.setText(answer);
            } else if (i == 2) {
                holder.option_c_edt.setText(answer);
            }
        }
        holder.btn_cancel.setOnClickListener(v -> modifyVotePop.dismiss());
        holder.iv_close.setOnClickListener(v -> modifyVotePop.dismiss());
        holder.btn_ensure.setOnClickListener(v -> {
            String title = holder.edt_vote_title.getText().toString().trim();
            if (TextUtils.isEmpty(title)) {
                ToastUtils.showShort(R.string.title_can_not_empty);
                return;
            }
            String a = holder.option_a_edt.getText().toString().trim();
            String b = holder.option_b_edt.getText().toString().trim();
            String c = holder.option_c_edt.getText().toString().trim();
            if (TextUtils.isEmpty(a) || TextUtils.isEmpty(b) || TextUtils.isEmpty(c)) {
                ToastUtils.showShort(R.string.answer_can_not_empty);
                return;
            }
            int mode = holder.sp_notation.getSelectedItemPosition() == 0
                    ? InterfaceMacro.Pb_MeetVoteMode.Pb_VOTEMODE_signed_VALUE
                    : InterfaceMacro.Pb_MeetVoteMode.Pb_VOTEMODE_agonymous_VALUE;

            List<ByteString> all = new ArrayList<>();
            all.add(s2b(a));
            all.add(s2b(b));
            all.add(s2b(c));
            InterfaceVote.pbui_Item_MeetOnVotingDetailInfo.Builder builder = InterfaceVote.pbui_Item_MeetOnVotingDetailInfo.newBuilder();
            builder.setVoteid(vote.getVoteid())
                    .setContent(s2b(title))
                    .setMaintype(vote.getMaintype())
                    .setType(vote.getType())
                    .setMode(mode)
                    .setTimeouts(vote.getTimeouts())
                    .setSelectcount(all.size())
                    .addAllText(all);
            jni.modifyVote(builder.build());
            modifyVotePop.dismiss();
        });
    }

    private void modifyElections(InterfaceVote.pbui_Item_MeetVoteDetailInfo vote) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.pop_modify_elections, null, false);
        View meet_fl = getActivity().findViewById(R.id.meet_fl);
        View meet_left_ll = getActivity().findViewById(R.id.meet_left_ll);
        int width = meet_fl.getWidth();
        int height = meet_fl.getHeight();
        int width1 = meet_left_ll.getWidth();
        int height1 = meet_left_ll.getHeight();
        LogUtils.i(TAG, "showDetailsPop 宽高=" + width + "," + height + ",功能菜单宽高=" + width1 + "," + height1);
        modifyElectionsPop = PopUtil.createPopupWindow(inflate, width * 2 / 3, height * 2 / 3, rv_vote, Gravity.CENTER, width1 / 2, 0);
        ElectionsViewHolder viewHolder = new ElectionsViewHolder(inflate);
        holderEvent(viewHolder, vote);
    }

    private void holderEvent(ElectionsViewHolder holder, InterfaceVote.pbui_Item_MeetVoteDetailInfo vote) {
        holder.iv_close.setOnClickListener(v -> modifyElectionsPop.dismiss());
        holder.btn_cancel.setOnClickListener(v -> modifyElectionsPop.dismiss());
        String text = vote.getContent().toStringUtf8();
        holder.edt_elections_title.setText(text);
        holder.edt_elections_title.setSelection(text.length());
        holder.sp_vote_type.setSelection(vote.getType());
        holder.sp_vote_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //指定了3选2模式则隐藏掉后两项
                if (position == 5) {
                    holder.option_a_ll.setVisibility(View.VISIBLE);
                    holder.option_b_ll.setVisibility(View.VISIBLE);
                    holder.option_c_ll.setVisibility(View.VISIBLE);
                    holder.option_d_ll.setVisibility(View.GONE);
                    holder.option_e_ll.setVisibility(View.GONE);
                } else {
                    holder.option_a_ll.setVisibility(View.VISIBLE);
                    holder.option_b_ll.setVisibility(View.VISIBLE);
                    holder.option_c_ll.setVisibility(View.VISIBLE);
                    holder.option_d_ll.setVisibility(View.VISIBLE);
                    holder.option_e_ll.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        holder.sp_notation.setSelection(vote.getMode() == 0 ? 1 : 0);
//        int timeouts = vote.getTimeouts();
//        int index = 0;
//        if (timeouts >= 1800) {
//            index = 7;
//        } else if (timeouts >= 900) {
//            index = 6;
//        } else if (timeouts >= 300) {
//            index = 5;
//        } else if (timeouts >= 120) {
//            index = 4;
//        } else if (timeouts >= 60) {
//            index = 3;
//        } else if (timeouts >= 30) {
//            index = 2;
//        } else if (timeouts >= 10) {
//            index = 1;
//        }
//        holder.sp_count_down.setSelection(index);
        holder.option_a_edt.setFilters(new InputFilter[]{new MaxLengthFilter(20)});
        holder.option_b_edt.setFilters(new InputFilter[]{new MaxLengthFilter(20)});
        holder.option_c_edt.setFilters(new InputFilter[]{new MaxLengthFilter(20)});
        holder.option_d_edt.setFilters(new InputFilter[]{new MaxLengthFilter(20)});
        holder.option_e_edt.setFilters(new InputFilter[]{new MaxLengthFilter(20)});

        List<InterfaceVote.pbui_SubItem_VoteItemInfo> itemList = vote.getItemList();
        holder.option_a_ll.setVisibility(View.GONE);
        holder.option_b_ll.setVisibility(View.GONE);
        holder.option_c_ll.setVisibility(View.GONE);
        holder.option_d_ll.setVisibility(View.GONE);
        holder.option_e_ll.setVisibility(View.GONE);
        for (int i = 0; i < itemList.size(); i++) {
            InterfaceVote.pbui_SubItem_VoteItemInfo item = itemList.get(i);
            String option = item.getText().toStringUtf8();
            if (i == 0) {
                holder.option_a_ll.setVisibility(View.VISIBLE);
                holder.option_a_edt.setText(option);
            } else if (i == 1) {
                holder.option_b_ll.setVisibility(View.VISIBLE);
                holder.option_b_edt.setText(option);
            } else if (i == 2) {
                holder.option_c_ll.setVisibility(View.VISIBLE);
                holder.option_c_edt.setText(option);
            } else if (i == 3) {
                holder.option_d_ll.setVisibility(View.VISIBLE);
                holder.option_d_edt.setText(option);
            } else if (i == 4) {
                holder.option_e_ll.setVisibility(View.VISIBLE);
                holder.option_e_edt.setText(option);
            }
        }
        holder.btn_ensure.setOnClickListener(v -> {
            String title = holder.edt_elections_title.getText().toString().trim();
            if (TextUtils.isEmpty(title)) {
                ToastUtils.showShort(R.string.title_can_not_empty);
                return;
            }
            String a = holder.option_a_edt.getText().toString().trim();
            String b = holder.option_b_edt.getText().toString().trim();
            String c = holder.option_c_edt.getText().toString().trim();
            String d = holder.option_d_edt.getText().toString().trim();
            String e = holder.option_e_edt.getText().toString().trim();
            int type = holder.sp_vote_type.getSelectedItemPosition();
            int mode = holder.sp_notation.getSelectedItemPosition() == 0
                    ? InterfaceMacro.Pb_MeetVoteMode.Pb_VOTEMODE_signed_VALUE
                    : InterfaceMacro.Pb_MeetVoteMode.Pb_VOTEMODE_agonymous_VALUE;

            List<ByteString> all = new ArrayList<>();
            //前3个答案必须要填
            if (TextUtils.isEmpty(a) || TextUtils.isEmpty(b) || TextUtils.isEmpty(c)) {
                ToastUtils.showShort(getString(R.string.answer_can_not_empty));
                return;
            }
            all.add(s2b(a));
            all.add(s2b(b));
            all.add(s2b(c));
            //判断是否是需要5个答案
            if (type == InterfaceMacro.Pb_MeetVote_SelType.Pb_VOTE_TYPE_4_5_VALUE
                    || type == InterfaceMacro.Pb_MeetVote_SelType.Pb_VOTE_TYPE_3_5_VALUE
                    || type == InterfaceMacro.Pb_MeetVote_SelType.Pb_VOTE_TYPE_2_5_VALUE) {
                if (TextUtils.isEmpty(d) || TextUtils.isEmpty(e)) {
                    ToastUtils.showShort(getString(R.string.answer_can_not_empty));
                    return;
                }
                all.add(s2b(d));
                all.add(s2b(e));
            } else if (type == InterfaceMacro.Pb_MeetVote_SelType.Pb_VOTE_TYPE_SINGLE_VALUE) {
                if (!TextUtils.isEmpty(d)) {
                    all.add(s2b(d));
                }
                if (!TextUtils.isEmpty(e)) {
                    all.add(s2b(e));
                }
            }
            InterfaceVote.pbui_Item_MeetOnVotingDetailInfo.Builder builder = InterfaceVote.pbui_Item_MeetOnVotingDetailInfo.newBuilder();
            builder.setVoteid(vote.getVoteid())
                    .setContent(s2b(title))
                    .setMaintype(vote.getMaintype())
                    .setType(type)
                    .setMode(mode)
                    .setTimeouts(vote.getTimeouts())
                    .setSelectcount(all.size())
                    .addAllText(all);
            jni.modifyVote(builder.build());
            modifyElectionsPop.dismiss();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_VOTE) {
            Uri uri = data.getData();
            File file = UriUtils.uri2File(uri);
            if (file != null) {
                if (file.getName().endsWith(".xls")) {
                    List<InterfaceVote.pbui_Item_MeetOnVotingDetailInfo> info = JxlUtil.readVoteXls(file.getAbsolutePath(),
                            isVote ? InterfaceMacro.Pb_MeetVoteType.Pb_VOTE_MAINTYPE_vote_VALUE
                                    : InterfaceMacro.Pb_MeetVoteType.Pb_VOTE_MAINTYPE_election_VALUE);
                    if (!info.isEmpty()) {
                        for (int i = 0; i < info.size(); i++) {
                            jni.createVote(info.get(i));
                        }
                        ToastUtils.showShort(R.string.import_successful);
                    } else {
                        ToastUtils.showLong(R.string.import_err_content_format_is_incorrect);
                    }
                } else {
                    ToastUtils.showShort(R.string.please_choose_xls_file);
                }
            }
        }
    }

    @Override
    public void showDetailsPop(InterfaceVote.pbui_Item_MeetVoteDetailInfo vote) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.pop_vote_detail, null, false);
        View meet_fl = getActivity().findViewById(R.id.meet_fl);
        View meet_left_ll = getActivity().findViewById(R.id.meet_left_ll);
        int width = meet_fl.getWidth();
        int height = meet_fl.getHeight();
        int width1 = meet_left_ll.getWidth();
        LogUtils.i(TAG, "showDetailsPop 倒计时=" + vote.getTimeouts());
        detailPop = PopUtil.createPopupWindow(inflate, width * 2 / 3, height * 2 / 3, rv_vote, Gravity.CENTER, width1 / 2, 0);

        TextView tv_title = inflate.findViewById(R.id.tv_title);

        String content = vote.getContent().toStringUtf8();
        int type = vote.getType();
        String voteType = Constant.getVoteType(getContext(), type);
        String mode = vote.getMode() == InterfaceMacro.Pb_MeetVoteMode.Pb_VOTEMODE_agonymous_VALUE
                ? getString(R.string.anonymous) : getString(R.string.notation);
        content += "（" + voteType + "、" + mode + "）";
        tv_title.setText(content);

        RecyclerView rv_member = inflate.findViewById(R.id.rv_member);
        SubmitMemberAdapter submitMemberAdapter = new SubmitMemberAdapter(R.layout.item_vote_submit_member, presenter.submitMembers);
        rv_member.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_member.addItemDecoration(new RvItemDecoration(getContext()));
        rv_member.setAdapter(submitMemberAdapter);

        inflate.findViewById(R.id.btn_export_result).setOnClickListener(v -> {
            currentExportVoteContent = vote.getContent().toStringUtf8();
            detailPop.dismiss();
            showExportPop(1, vote);
        });
        inflate.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            detailPop.dismiss();
        });
        inflate.findViewById(R.id.iv_close).setOnClickListener(v -> {
            detailPop.dismiss();
        });
    }

    @Override
    public void showChartPop(InterfaceVote.pbui_Item_MeetVoteDetailInfo vote) {
        List<InterfaceVote.pbui_SubItem_VoteItemInfo> itemList = vote.getItemList();
        int totalVotes = getTotalVotes(itemList);
        if (totalVotes == 0) {
            ToastUtils.showShort(R.string.current_vote_no_data);
            return;
        }
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.pop_vote_chart, null, false);
        View meet_fl = getActivity().findViewById(R.id.meet_fl);
        View meet_left_ll = getActivity().findViewById(R.id.meet_left_ll);
        int width = meet_fl.getWidth();
        int height = meet_fl.getHeight();
        int width1 = meet_left_ll.getWidth();
        int height1 = meet_left_ll.getHeight();
        LogUtils.i(TAG, "showDetailsPop 宽高=" + width + "," + height + ",功能菜单宽高=" + width1 + "," + height1);
        chartPop = PopUtil.createPopupWindow(inflate, width * 2 / 3, height * 2 / 3, rv_vote, Gravity.CENTER, width1 / 2, 0);

//        chartPop = PopUtil.createBigPop(inflate, rv_vote);

        PieChart chart = inflate.findViewById(R.id.pie_chart);
        TextView tv_title = inflate.findViewById(R.id.tv_title);
        String content = vote.getContent().toStringUtf8();
        int type = vote.getType();
        String voteType = Constant.getVoteType(getContext(), type);
        String mode = vote.getMode() == InterfaceMacro.Pb_MeetVoteMode.Pb_VOTEMODE_agonymous_VALUE
                ? getString(R.string.anonymous) : getString(R.string.notation);
        content += "（" + voteType + "、" + mode + "）";
        tv_title.setText(content);
        int index = getSingleAnswerIndex(vote);
        if (index == -1) {
            configChart(vote, chart);
        } else {
            singleAnswerConfig(vote, chart, index);
        }
        inflate.findViewById(R.id.iv_close).setOnClickListener(v -> chartPop.dismiss());
    }

    private int getSingleAnswerIndex(InterfaceVote.pbui_Item_MeetVoteDetailInfo vote) {
        int count = 0;
        int index = -1;
        List<InterfaceVote.pbui_SubItem_VoteItemInfo> itemList = vote.getItemList();
        for (int i = 0; i < itemList.size(); i++) {
            InterfaceVote.pbui_SubItem_VoteItemInfo item = itemList.get(i);
            int selcnt = item.getSelcnt();
            if (count != 0 && selcnt != 0) {
                return -1;
            }
            if (count == 0) {
                count = selcnt;
                index = i;
            }
        }
        return index;
    }

    private void singleAnswerConfig(InterfaceVote.pbui_Item_MeetVoteDetailInfo vote, PieChart chart, int index) {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        List<InterfaceVote.pbui_SubItem_VoteItemInfo> itemList = vote.getItemList();
        for (int i = 0; i < itemList.size(); i++) {
            InterfaceVote.pbui_SubItem_VoteItemInfo item = itemList.get(i);
            String answer = item.getText().toStringUtf8();
            int selcnt = item.getSelcnt();
            PieEntry pieEntry = new PieEntry((float) 0, answer);
            pieEntries.add(pieEntry);
            if (i == 0) {
                colors.add(getResources().getColor(R.color.option_a));
            } else if (i == 1) {
                colors.add(getResources().getColor(R.color.option_b));
            } else if (i == 2) {
                colors.add(getResources().getColor(R.color.option_c));
            } else if (i == 3) {
                colors.add(getResources().getColor(R.color.option_d));
            } else if (i == 4) {
                colors.add(getResources().getColor(R.color.option_e));
            }
        }

        chart.setLogEnabled(true);
        //如果启用此功能，则图表内的值将以百分比而不是原始值绘制。 提供为 提供的值 ValueFormatter然后，以百分比形式 to格式
        chart.setUsePercentValues(true);
        //将此设置为true，以将入口标签绘制到饼片中（由PieEntry类的getLabel()方法提供）
        chart.setDrawEntryLabels(false);
        //设置额外的偏移量（在图表视图周围）附加到自动计算的偏移量上
        chart.setExtraOffsets(5, 5, 100, 5);
        //减速摩擦系数，以[0 ; 1]为区间，数值越大表示速度越慢，如设为0，则会立即停止，1为无效值，会自动转换为0.999f。1为无效值，将自动转换为0.999f
        chart.setDragDecelerationFrictionCoef(0.95f);

        //设置中间的文本
        chart.setCenterText("100.0 %");
        //设置中心区域的文本颜色
        chart.setCenterTextColor(Color.WHITE);
        //设置中心文本字体大小，单位dp
        chart.setCenterTextSize(14f);
        //将此设置为 "true"，以使饼中心为空
        chart.setDrawHoleEnabled(true);
        //设置画在饼图中心的孔的颜色。(上一行设置为true生效)
        /* **** **  这里设置成当前圆的背景颜色，这样就算设置了文本用户也看不到，达到只有中间一个100%的效果  ** **** */
        chart.setHoleColor(colors.get(index));

        //设置透明圆的颜色
        chart.setTransparentCircleColor(Color.WHITE);
        //设置透明圆的透明度
        chart.setTransparentCircleAlpha(110);
        //设置饼图中心孔的半径，以最大半径的百分比为单位（max=整个饼图的半径），默认为50%
        chart.setHoleRadius(100f);
        //设置画在饼图中孔洞旁边的透明圆的半径，以最大半径的百分比为单位(max=整个图表的半径)，默认55%->表示比中心孔洞默认大5%
        chart.setTransparentCircleRadius(61f);
        //将此设置为 "true"，以绘制显示在饼图中心的文字
        chart.setDrawCenterText(true);
        //设置雷达图的旋转偏移量，单位为度。默认270f
        chart.setRotationAngle(0);
        //设置为 "true"，可以通过触摸来实现图表的旋转/旋转。设置为false则禁用。默认值：true
        chart.setRotationEnabled(false);
        //将此设置为false，以防止通过点击手势突出显示数值。值仍然可以通过拖动或编程方式高亮显示。默认值：true
        chart.setHighlightPerTapEnabled(false);

        //返回图表的Legend对象。本方法可以用来获取图例的实例，以便自定义自动生成的图例
        Legend l = chart.getLegend();
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        //在水平轴上设置图例条目之间的空间，单位为像素，内部转换为dp
        l.setXEntrySpace(7f);
        //在垂直轴上设置图例条目之间的空间，单位为像素，内部转换为dp
        l.setYEntrySpace(5f);
        l.setWordWrapEnabled(true);
        //设置此轴上标签的Y轴偏移量。对于图例来说，偏移量越大，意味着图例作为一个整体将被放置在离顶部越远的地方
        l.setYOffset(0f);

        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setValueTextSize(14f);
        dataSet.setColors(colors);
        //设置百分比内容的文本颜色
        /* **** **  设置成当前圆的颜色，达到隐藏的效果  ** **** */
        dataSet.setValueTextColor(colors.get(index));

        PieData pieData = new PieData(dataSet);
        MyPercentFormatter f = new MyPercentFormatter(chart);
        pieData.setValueFormatter(f);
        pieData.setDrawValues(true);
        pieData.setValueTextSize(14f);
        chart.setData(pieData);

        Description description = new Description();
        description.setText("");
        chart.setDescription(description);

        chart.invalidate();
    }


    private void configChart(InterfaceVote.pbui_Item_MeetVoteDetailInfo vote, PieChart chart) {
        chart.setLogEnabled(true);
        //如果启用此功能，则图表内的值将以百分比而不是原始值绘制。 提供为 提供的值 ValueFormatter然后，以百分比形式 to格式
        chart.setUsePercentValues(true);
        //将此设置为true，以将入口标签绘制到饼片中（由PieEntry类的getLabel()方法提供）
        chart.setDrawEntryLabels(false);
        //设置额外的偏移量（在图表视图周围）附加到自动计算的偏移量上
        chart.setExtraOffsets(5, 5, 100, 5);
        //减速摩擦系数，以[0 ; 1]为区间，数值越大表示速度越慢，如设为0，则会立即停止，1为无效值，会自动转换为0.999f。1为无效值，将自动转换为0.999f
        chart.setDragDecelerationFrictionCoef(0.95f);
//        chart.setCenterText("中间的文本内容");
        //将此设置为 "true"，以使饼中心为空
        chart.setDrawHoleEnabled(true);
        //设置画在饼图中心的孔的颜色。(上一行设置为true生效)
        chart.setHoleColor(Color.WHITE);
        //设置透明圆的颜色
        chart.setTransparentCircleColor(Color.WHITE);
        //设置透明圆的透明度
        chart.setTransparentCircleAlpha(110);
        //设置饼图中心孔的半径，以最大半径的百分比为单位（max=整个饼图的半径），默认为50%
        chart.setHoleRadius(0f);
        //设置画在饼图中孔洞旁边的透明圆的半径，以最大半径的百分比为单位(max=整个图表的半径)，默认55%->表示比中心孔洞默认大5%
        chart.setTransparentCircleRadius(0f);
        //将此设置为 "true"，以绘制显示在饼图中心的文字
        chart.setDrawCenterText(false);
        //设置雷达图的旋转偏移量，单位为度。默认270f
        chart.setRotationAngle(0);
        //设置为 "true"，可以通过触摸来实现图表的旋转/旋转。设置为false则禁用。默认值：true
        chart.setRotationEnabled(true);
        //将此设置为false，以防止通过点击手势突出显示数值。值仍然可以通过拖动或编程方式高亮显示。默认值：true
        chart.setHighlightPerTapEnabled(false);

        //返回图表的Legend对象。本方法可以用来获取图例的实例，以便自定义自动生成的图例
        Legend l = chart.getLegend();
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        //在水平轴上设置图例条目之间的空间，单位为像素，内部转换为dp
        l.setXEntrySpace(7f);
        //在垂直轴上设置图例条目之间的空间，单位为像素，内部转换为dp
        l.setYEntrySpace(5f);
        l.setWordWrapEnabled(true);
        //设置此轴上标签的Y轴偏移量。对于图例来说，偏移量越大，意味着图例作为一个整体将被放置在离顶部越远的地方
        l.setYOffset(0f);

        // entry label styling
        //设置输入标签的颜色
        chart.setEntryLabelColor(Color.WHITE);
        //设置用于绘制条目标签的自定义字体
//        chart.setEntryLabelTypeface(tfRegular);
        //设置以dp为单位的条目标签的大小。默认值：13dp
        chart.setEntryLabelTextSize(14f);

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        List<InterfaceVote.pbui_SubItem_VoteItemInfo> itemList = vote.getItemList();
        for (int i = 0; i < itemList.size(); i++) {
            InterfaceVote.pbui_SubItem_VoteItemInfo item = itemList.get(i);
            String s = item.getText().toStringUtf8();
            int selcnt = item.getSelcnt();
            PieEntry pieEntry = new PieEntry((float) selcnt, s);
            pieEntries.add(pieEntry);
            if (i == 0) {
                colors.add(getResources().getColor(R.color.option_a));
            } else if (i == 1) {
                colors.add(getResources().getColor(R.color.option_b));
            } else if (i == 2) {
                colors.add(getResources().getColor(R.color.option_c));
            } else if (i == 3) {
                colors.add(getResources().getColor(R.color.option_d));
            } else if (i == 4) {
                colors.add(getResources().getColor(R.color.option_e));
            }
        }
        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setValueTextSize(14f);
        dataSet.setColors(colors);

//        chart.setUsePercentValues(true);

        PieData pieData = new PieData(dataSet);
        MyPercentFormatter f = new MyPercentFormatter(chart);
        pieData.setValueFormatter(f);
        pieData.setDrawValues(true);
        pieData.setValueTextSize(14f);
        chart.setData(pieData);

        Description description = new Description();
        description.setText("");
        chart.setDescription(description);

        chart.invalidate();
    }

    /**
     * 获取当前投票的总票数
     *
     * @return 总票数
     */
    private int getTotalVotes(List<InterfaceVote.pbui_SubItem_VoteItemInfo> vote) {
        int count = 0;
        for (int i = 0; i < vote.size(); i++) {
            InterfaceVote.pbui_SubItem_VoteItemInfo info = vote.get(i);
            count += info.getSelcnt();
        }
        LogUtil.e(TAG, "getTotalVotes :  当前投票票数总数 --> " + count);
        return count;
    }

    private void launchVote(InterfaceVote.pbui_Item_MeetVoteDetailInfo vote) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.pop_launch_vote, null, false);
        View meet_fl = getActivity().findViewById(R.id.meet_fl);
        View meet_left_ll = getActivity().findViewById(R.id.meet_left_ll);
        int width = meet_fl.getWidth();
        int height = meet_fl.getHeight();
        int width1 = meet_left_ll.getWidth();
        int height1 = meet_left_ll.getHeight();
        LogUtils.i(TAG, "showDetailsPop 宽高=" + width + "," + height + ",功能菜单宽高=" + width1 + "," + height1);
        voteConfigPop = PopUtil.createPopupWindow(inflate, width * 2 / 3, height * 2 / 3, rv_vote, Gravity.CENTER, width1 / 2, 0);
        TextView pop_title = inflate.findViewById(R.id.pop_title);
        pop_title.setText(isVote ? getString(R.string.launch_vote) : getString(R.string.launch_elections));

        TextView tv_title = inflate.findViewById(R.id.tv_title);
        EditText edt_remarks = inflate.findViewById(R.id.edt_remarks);
        RadioButton rb_ten = inflate.findViewById(R.id.rb_ten);
        RadioButton rb_thirty = inflate.findViewById(R.id.rb_thirty);
        RadioButton rb_notation = inflate.findViewById(R.id.rb_notation);
        RadioButton rb_anonymity = inflate.findViewById(R.id.rb_anonymity);

        boolean checked = vote.getTimeouts() < (30 * 60);
        rb_ten.setChecked(checked);
        rb_thirty.setChecked(!checked);

        boolean checked1 = vote.getMode() == InterfaceMacro.Pb_MeetVoteMode.Pb_VOTEMODE_signed_VALUE;
        rb_notation.setChecked(checked1);
        rb_anonymity.setChecked(!checked1);

        tv_title.setText(vote.getContent().toStringUtf8());
        inflate.findViewById(R.id.btn_cancel).setOnClickListener(v -> voteConfigPop.dismiss());
        inflate.findViewById(R.id.iv_close).setOnClickListener(v -> voteConfigPop.dismiss());
        inflate.findViewById(R.id.btn_ensure).setOnClickListener(v -> {
            int time;
            if (rb_ten.isChecked()) {
                time = 10 * 60;
            } else {
                time = 30 * 60;
            }
            int mode;
            if (rb_notation.isChecked()) {
                mode = InterfaceMacro.Pb_MeetVoteMode.Pb_VOTEMODE_signed_VALUE;
            } else {
                mode = InterfaceMacro.Pb_MeetVoteMode.Pb_VOTEMODE_agonymous_VALUE;
            }
            List<ByteString> all = new ArrayList<>();
            List<InterfaceVote.pbui_SubItem_VoteItemInfo> itemList = vote.getItemList();
            for (int i = 0; i < itemList.size(); i++) {
                InterfaceVote.pbui_SubItem_VoteItemInfo item = itemList.get(i);
                all.add(item.getText());
            }
            InterfaceVote.pbui_Item_MeetOnVotingDetailInfo.Builder builder = InterfaceVote.pbui_Item_MeetOnVotingDetailInfo.newBuilder();
            builder.setVoteid(vote.getVoteid())
                    .setContent(vote.getContent())
                    .setMaintype(vote.getMaintype())
                    .setType(vote.getType())
                    .setMode(mode)
                    .setTimeouts(time)
                    .setSelectcount(all.size())
                    .addAllText(all);
            jni.modifyVote(builder.build());
            showMember(vote.getVoteid(), time);
            voteConfigPop.dismiss();
        });
    }

    private void showMember(int voteId, int time) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.pop_launch_vote_member, null, false);
        View meet_fl = getActivity().findViewById(R.id.meet_fl);
        View meet_left_ll = getActivity().findViewById(R.id.meet_left_ll);
        int width = meet_fl.getWidth();
        int height = meet_fl.getHeight();
        int width1 = meet_left_ll.getWidth();
        int height1 = meet_left_ll.getHeight();
        LogUtils.i(TAG, "showDetailsPop 宽高=" + width + "," + height + ",功能菜单宽高=" + width1 + "," + height1);
        memberPop = PopUtil.createPopupWindow(inflate, width * 2 / 3, height * 2 / 3, rv_vote, Gravity.CENTER, width1 / 2, 0);
        RecyclerView pop_vote_rv = inflate.findViewById(R.id.pop_vote_rv);
        CheckBox pop_vote_all = inflate.findViewById(R.id.pop_vote_all);
        memberDetailAdapter = new MemberDetailAdapter(R.layout.item_member_detail, presenter.memberDetails);
        pop_vote_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        pop_vote_rv.setAdapter(memberDetailAdapter);
        memberDetailAdapter.setOnItemClickListener((adapter, view, position) -> {
            memberDetailAdapter.setSelectedId(presenter.memberDetails.get(position).getMemberid());
            pop_vote_all.setChecked(memberDetailAdapter.isCheckedAll());
        });
        pop_vote_all.setOnClickListener(v -> {
            boolean checked = pop_vote_all.isChecked();
            pop_vote_all.setChecked(checked);
            memberDetailAdapter.setCheckedAll(checked);
        });
        inflate.findViewById(R.id.pop_vote_determine).setOnClickListener(v -> {
            List<Integer> memberIds = memberDetailAdapter.getSelectedIds();
            if (memberIds.isEmpty()) {
                ToastUtils.showShort(R.string.please_choose_member);
                return;
            }
            InterfaceVote.pbui_Item_MeetVoteDetailInfo select = voteAdapter.getSelect();
            if (select != null && select.getVotestate() == InterfaceMacro.Pb_MeetVoteStatus.Pb_vote_notvote_VALUE) {
                jni.launchVote(memberIds, voteId, time);
                showSuccessfulToast();
            } else {
                ToastUtils.showShort(R.string.tip_vote_status_changed);
            }
            memberPop.dismiss();
        });
        inflate.findViewById(R.id.pop_vote_cancel).setOnClickListener(v -> {
            memberPop.dismiss();
        });
    }

    @Override
    public void updateMembers() {
        if (memberPop != null && memberPop.isShowing()) {
            memberDetailAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void updateVote(List<InterfaceVote.pbui_Item_MeetVoteDetailInfo> votes) {
        if (voteAdapter == null) {
            voteAdapter = new VoteAdapter(votes);
            rv_vote.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_vote.addItemDecoration(new RvItemDecoration(getContext()));
            rv_vote.setAdapter(voteAdapter);
            voteAdapter.setOnItemClickListener((adapter, view, position) -> voteAdapter.setSelectedId(votes.get(position).getVoteid()));
        } else {
            voteAdapter.notifyDataSetChanged();
        }
    }

    public static class ElectionsViewHolder {
        public View rootView;
        public TextView pop_title;
        public ImageView iv_close;
        public EditText edt_elections_title;
        public Spinner sp_vote_type;
        public Spinner sp_count_down;
        public Spinner sp_notation;
        public TextView option_a_edt;
        public LinearLayout option_a_ll;
        public TextView option_b_edt;
        public LinearLayout option_b_ll;
        public TextView option_c_edt;
        public LinearLayout option_c_ll;
        public TextView option_d_edt;
        public LinearLayout option_d_ll;
        public TextView option_e_edt;
        public LinearLayout option_e_ll;
        public Button btn_cancel;
        public Button btn_ensure;

        public ElectionsViewHolder(View rootView) {
            this.rootView = rootView;
            this.pop_title = (TextView) rootView.findViewById(R.id.pop_title);
            this.iv_close = (ImageView) rootView.findViewById(R.id.iv_close);
            this.edt_elections_title = (EditText) rootView.findViewById(R.id.edt_elections_title);
            this.sp_vote_type = (Spinner) rootView.findViewById(R.id.sp_vote_type);
            this.sp_count_down = (Spinner) rootView.findViewById(R.id.sp_count_down);
            this.sp_notation = (Spinner) rootView.findViewById(R.id.sp_notation);
            this.option_a_edt = (TextView) rootView.findViewById(R.id.option_a_edt);
            this.option_a_ll = (LinearLayout) rootView.findViewById(R.id.option_a_ll);
            this.option_b_edt = (TextView) rootView.findViewById(R.id.option_b_edt);
            this.option_b_ll = (LinearLayout) rootView.findViewById(R.id.option_b_ll);
            this.option_c_edt = (TextView) rootView.findViewById(R.id.option_c_edt);
            this.option_c_ll = (LinearLayout) rootView.findViewById(R.id.option_c_ll);
            this.option_d_edt = (TextView) rootView.findViewById(R.id.option_d_edt);
            this.option_d_ll = (LinearLayout) rootView.findViewById(R.id.option_d_ll);
            this.option_e_edt = (TextView) rootView.findViewById(R.id.option_e_edt);
            this.option_e_ll = (LinearLayout) rootView.findViewById(R.id.option_e_ll);
            this.btn_cancel = (Button) rootView.findViewById(R.id.btn_cancel);
            this.btn_ensure = (Button) rootView.findViewById(R.id.btn_ensure);
        }

    }

    public static class CreateVoteViewHolder {
        public View rootView;
        public TextView tvOperationTitle;
        public ImageView ivClose;
        public TextView tvTitle;
        public EditText edtTitle;
        public TextView tvType;
        public Spinner spVoteType;
        public LinearLayout optionALl;
        public EditText optionAEdt;
        public LinearLayout optionBLl;
        public EditText optionBEdt;
        public LinearLayout optionCLl;
        public EditText optionCEdt;
        public LinearLayout optionDLl;
        public EditText optionDEdt;
        public LinearLayout optionELl;
        public EditText optionEEdt;
        public Button btnCancel;
        public Button btnEnsure;

        public CreateVoteViewHolder(View rootView) {
            this.rootView = rootView;
            tvOperationTitle = (TextView) rootView.findViewById(R.id.tv_operation_title);
            ivClose = (ImageView) rootView.findViewById(R.id.iv_close);
            tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
            edtTitle = (EditText) rootView.findViewById(R.id.edt_title);
            tvType = (TextView) rootView.findViewById(R.id.tv_type);
            spVoteType = (Spinner) rootView.findViewById(R.id.sp_vote_type);
            optionALl = (LinearLayout) rootView.findViewById(R.id.option_a_ll);
            optionAEdt = (EditText) rootView.findViewById(R.id.option_a_edt);
            optionBLl = (LinearLayout) rootView.findViewById(R.id.option_b_ll);
            optionBEdt = (EditText) rootView.findViewById(R.id.option_b_edt);
            optionCLl = (LinearLayout) rootView.findViewById(R.id.option_c_ll);
            optionCEdt = (EditText) rootView.findViewById(R.id.option_c_edt);
            optionDLl = (LinearLayout) rootView.findViewById(R.id.option_d_ll);
            optionDEdt = (EditText) rootView.findViewById(R.id.option_d_edt);
            optionELl = (LinearLayout) rootView.findViewById(R.id.option_e_ll);
            optionEEdt = (EditText) rootView.findViewById(R.id.option_e_edt);
            btnCancel = (Button) rootView.findViewById(R.id.btn_cancel);
            btnEnsure = (Button) rootView.findViewById(R.id.btn_ensure);
        }
    }

    public static class VoteViewHolder {
        public View rootView;
        public ImageView iv_close;
        public EditText edt_vote_title;
        public Spinner sp_notation;
        public EditText option_a_edt;
        public EditText option_b_edt;
        public EditText option_c_edt;
        public Button btn_cancel;
        public Button btn_ensure;

        public VoteViewHolder(View rootView) {
            this.rootView = rootView;
            this.iv_close = (ImageView) rootView.findViewById(R.id.iv_close);
            this.edt_vote_title = (EditText) rootView.findViewById(R.id.edt_vote_title);
            this.sp_notation = (Spinner) rootView.findViewById(R.id.sp_notation);
            this.option_a_edt = (EditText) rootView.findViewById(R.id.option_a_edt);
            this.option_b_edt = (EditText) rootView.findViewById(R.id.option_b_edt);
            this.option_c_edt = (EditText) rootView.findViewById(R.id.option_c_edt);
            this.btn_cancel = (Button) rootView.findViewById(R.id.btn_cancel);
            this.btn_ensure = (Button) rootView.findViewById(R.id.btn_ensure);
        }

    }
}
