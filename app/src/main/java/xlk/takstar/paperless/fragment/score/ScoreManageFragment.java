package xlk.takstar.paperless.fragment.score;

import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.mogujie.tt.protobuf.InterfaceFilescorevote;
import com.mogujie.tt.protobuf.InterfaceMacro;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.adapter.ScoreAdapter;
import xlk.takstar.paperless.adapter.ScoreMemberAdapter;
import xlk.takstar.paperless.adapter.WmScreenMemberAdapter;
import xlk.takstar.paperless.base.BaseFragment;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.util.ArithUtil;
import xlk.takstar.paperless.util.DialogUtil;
import xlk.takstar.paperless.util.PopUtil;

/**
 * @author Created by xlk on 2021/2/23.
 * @desc
 */
public class ScoreManageFragment extends BaseFragment<ScoreManagePresenter> implements ScoreManageContract.View {

    private RecyclerView rvScore;
    private ScoreAdapter scoreAdapter;
    private ScoreMemberAdapter scoreMemberAdapter;
    private int currentSelectedVoteId;
    private Button btnLaunch;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_score_manage;
    }

    @Override
    protected void initView(View inflate) {
        rvScore = inflate.findViewById(R.id.rv_score);
        btnLaunch = inflate.findViewById(R.id.btn_launch);
        inflate.findViewById(R.id.btn_stop).setOnClickListener(v -> {
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
        btnLaunch.setOnClickListener(v -> {
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
//                                ToastUtils.showShort(R.string.please_choose_notlaunch_score);
                        }
                    } else {
                        ToastUtils.showShort(R.string.please_stop_voteing_first);
                    }
                } else {
                    ToastUtils.showShort(R.string.please_choose_score);
                }
            }
        });
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
        boolean isScoreManage = getArguments().getBoolean("isScoreManage");
//        ll_manage_score.setVisibility(isScoreManage ? View.VISIBLE : View.INVISIBLE);
        presenter.queryMember();
        presenter.queryScore();
    }

    @Override
    public void updateOnlineMemberRv() {

    }

    @Override
    public void updateScoreRv() {
        if (scoreAdapter == null) {
            scoreAdapter = new ScoreAdapter(R.layout.item_score, presenter.scoreList);
            rvScore.setLayoutManager(new LinearLayoutManager(getContext()));
            rvScore.setAdapter(scoreAdapter);
            scoreAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    InterfaceFilescorevote.pbui_Type_Item_UserDefineFileScore item = presenter.scoreList.get(position);
                    scoreAdapter.setSelectedId(item.getVoteid());
                    setScoreDetails(item);
                    currentSelectedVoteId = item.getVoteid();
                }
            });
        } else {
            scoreAdapter.notifyDataSetChanged();
            scoreAdapter.notifySelectedScore();
            setScoreDetails(scoreAdapter.getSelectedScore());
        }
    }

    @Override
    public void updateScoreMemberRv() {
//        if (scoreMemberAdapter == null) {
//            scoreMemberAdapter = new ScoreMemberAdapter(R.layout.item_score_member, presenter.scoreMembers);
//            rvMemberScore.setLayoutManager(new LinearLayoutManager(getContext()));
////            rvMemberScore.addItemDecoration(new RvItemDecoration(getContext()));
//            rvMemberScore.setAdapter(scoreMemberAdapter);
//            scoreMemberAdapter.setOnItemClickListener(new OnItemClickListener() {
//                @Override
//                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
//                    scoreMemberAdapter.setSelectedId(presenter.scoreMembers.get(position).getScore().getMemberid());
////                    String text = presenter.scoreMembers.get(position).getScore().getContent().toStringUtf8();
////                    edtMemberScore.setText(getString(R.string.opinion_, text));
//                }
//            });
//        } else {
//            scoreMemberAdapter.notifyDataSetChanged();
//        }
    }

    private void setScoreDetails(InterfaceFilescorevote.pbui_Type_Item_UserDefineFileScore item) {
//        if (item == null) {
//            tvStatus.setText("");
//            edtDesc.setText("");
//            edtFile.setText("");
//            tvShouldAttend.setText("");
//            tvReviewedAttend.setText("");
//            tvA.setText("");
//            tvB.setText("");
//            tvC.setText("");
//            tvD.setText("");
//            tvAverageScore.setText("");
//            tvTotalScore.setText("");
//            edtMemberScore.setText(getString(R.string.opinion_, ""));
//            presenter.querySubmittedVoters(-1);
//            return;
//        }
//        tvStatus.setText(Constant.getVoteState(getContext(), item.getVotestate()));
//        edtDesc.setText(item.getContent().toStringUtf8());
//        edtFile.setText(jni.queryFileNameByMediaId(item.getFileid()));
//        tvRegister.setText(item.getMode() == InterfaceMacro.Pb_MeetVoteMode.Pb_VOTEMODE_signed_VALUE
//                ? getContext().getString(R.string.yes)
//                : getContext().getString(R.string.no));
//        tvShouldAttend.setText(String.valueOf(item.getShouldmembernum()));
//        tvReviewedAttend.setText(String.valueOf(item.getRealmembernum()));
//        tvA.setText(getScore(item, 0));
//        tvB.setText(getScore(item, 1));
//        tvC.setText(getScore(item, 2));
//        tvD.setText(getScore(item, 3));
//        double total = getTotal(item);
//        if (item.getRealmembernum() == 0) {
//            tvAverageScore.setText(String.valueOf(0));
//        } else {
//            double average = ArithUtil.div(total, item.getSelectcount() * item.getRealmembernum(), 1);
//            tvAverageScore.setText(String.valueOf(average));
//        }
//        tvTotalScore.setText(String.valueOf(total));
//        if (scoreMemberAdapter != null && currentSelectedVoteId != item.getVoteid()) {
//            scoreMemberAdapter.setSelectedId(-1);
//            edtMemberScore.setText(getString(R.string.opinion_, ""));
//        }
        if (item != null) {
            presenter.querySubmittedVoters(item.getVoteid());
        }
    }

    private String getScore(InterfaceFilescorevote.pbui_Type_Item_UserDefineFileScore item, int index) {
        int selectcount = item.getSelectcount();
        if (selectcount < (index + 1)) {//要获取的文本大于有效选项个数
            return "";
        }
        return item.getVoteText(index).toStringUtf8();
    }

    private double getTotal(InterfaceFilescorevote.pbui_Type_Item_UserDefineFileScore item) {
        double total = 0;
        List<Integer> itemsumscoreList = item.getItemsumscoreList();
        for (int i : itemsumscoreList) {
            total += i;
        }
        return total;
    }

    private void showChooseOnLineMemberDialog(InterfaceFilescorevote.pbui_Type_Item_UserDefineFileScore item, int voteflag) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.pop_online_member, null, false);
        View meet_fl = getActivity().findViewById(R.id.meet_fl);
        View meet_left_ll = getActivity().findViewById(R.id.meet_left_ll);
        int width = meet_fl.getWidth();
        int height = meet_fl.getHeight();
        int width1 = meet_left_ll.getWidth();
        int height1 = meet_left_ll.getHeight();
        PopupWindow memberPop = PopUtil.createPopupWindow(inflate, width * 2 / 3, height * 2 / 3, rvScore, Gravity.CENTER, width1 / 2, 0);
        CheckBox cb_all = inflate.findViewById(R.id.cb_all);
        RecyclerView rv_member = inflate.findViewById(R.id.rv_member);
        WmScreenMemberAdapter memberAdapter = new WmScreenMemberAdapter(R.layout.item_wm_screen, presenter.onlineMembers);
        rv_member.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_member.setAdapter(memberAdapter);
        memberAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                memberAdapter.choose(presenter.onlineMembers.get(position).getDeviceDetailInfo().getDevcieid());
                cb_all.setChecked(memberAdapter.isChooseAll());
            }
        });
        cb_all.setOnClickListener(v -> {
            boolean checked = cb_all.isChecked();
            cb_all.setChecked(checked);
            memberAdapter.setChooseAll(checked);
        });
        inflate.findViewById(R.id.btn_start_score).setOnClickListener(v -> {
            List<Integer> chooseIds = memberAdapter.getChooseIds();
            if (chooseIds.isEmpty()) {
                ToastUtils.showShort(R.string.please_choose_member);
            } else {
                jni.startScore(item.getVoteid(), voteflag, 0, chooseIds);
                memberPop.dismiss();
            }
        });
        inflate.findViewById(R.id.iv_close).setOnClickListener(v -> memberPop.dismiss());
        inflate.findViewById(R.id.btn_cancel).setOnClickListener(v -> memberPop.dismiss());
    }
}
