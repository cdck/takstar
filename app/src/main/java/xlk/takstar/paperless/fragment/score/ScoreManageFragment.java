package xlk.takstar.paperless.fragment.score;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

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
public class ScoreManageFragment extends BaseFragment<ScoreManagePresenter> implements ScoreManageContract.View, View.OnClickListener {

    private RecyclerView rvScore, rvMemberScore;
    private ScoreAdapter scoreAdapter;
    private TextView tvStatus;
    private EditText edtDesc;
    private EditText edtFile;
    private TextView tvRegister;
    private TextView tvShouldAttend;
    private TextView tvReviewedAttend;
    private EditText tvA;
    private TextView tvTotalScore;
    private EditText tvB;
    private TextView tvAverageScore;
    private EditText tvC;
    private EditText tvD;
    private EditText edtMemberScore;
    private ScoreMemberAdapter scoreMemberAdapter;
    private int currentSelectedVoteId;
    private LinearLayout ll_manage_score;
    private Button btn_launch_score, btn_stop_score;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_score_manage;
    }

    @Override
    protected void initView(View inflate) {
        rvScore = inflate.findViewById(R.id.rv_score);
        tvStatus = (TextView) inflate.findViewById(R.id.tv_status);
        edtDesc = (EditText) inflate.findViewById(R.id.edt_desc);
        edtFile = (EditText) inflate.findViewById(R.id.edt_file);
        tvRegister = (TextView) inflate.findViewById(R.id.tv_register);
        tvShouldAttend = (TextView) inflate.findViewById(R.id.tv_should_attend);
        tvReviewedAttend = (TextView) inflate.findViewById(R.id.tv_reviewed_attend);
        tvA = (EditText) inflate.findViewById(R.id.tv_a);
        tvTotalScore = (TextView) inflate.findViewById(R.id.tv_total_score);
        tvB = (EditText) inflate.findViewById(R.id.tv_b);
        tvAverageScore = (TextView) inflate.findViewById(R.id.tv_average_score);
        tvC = (EditText) inflate.findViewById(R.id.tv_c);
        tvD = (EditText) inflate.findViewById(R.id.tv_d);
        rvMemberScore = inflate.findViewById(R.id.rv_member_score);
        edtMemberScore = inflate.findViewById(R.id.edt_member_score);
        btn_launch_score = inflate.findViewById(R.id.btn_launch_score);
        btn_stop_score = inflate.findViewById(R.id.btn_stop_score);
        ll_manage_score = inflate.findViewById(R.id.ll_manage_score);
        btn_launch_score.setOnClickListener(this);
        btn_stop_score.setOnClickListener(this);
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
        ll_manage_score.setVisibility(isScoreManage ? View.VISIBLE : View.INVISIBLE);
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
        if (scoreMemberAdapter == null) {
            scoreMemberAdapter = new ScoreMemberAdapter(R.layout.item_score_member, presenter.scoreMembers);
            rvMemberScore.setLayoutManager(new LinearLayoutManager(getContext()));
//            rvMemberScore.addItemDecoration(new RvItemDecoration(getContext()));
            rvMemberScore.setAdapter(scoreMemberAdapter);
            scoreMemberAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    scoreMemberAdapter.setSelectedId(presenter.scoreMembers.get(position).getScore().getMemberid());
                    String text = presenter.scoreMembers.get(position).getScore().getContent().toStringUtf8();
                    edtMemberScore.setText(getString(R.string.opinion_, text));
                }
            });
        } else {
            scoreMemberAdapter.notifyDataSetChanged();
        }
    }

    private void setScoreDetails(InterfaceFilescorevote.pbui_Type_Item_UserDefineFileScore item) {
        if (item == null) {
            tvStatus.setText("");
            edtDesc.setText("");
            edtFile.setText("");
            tvShouldAttend.setText("");
            tvReviewedAttend.setText("");
            tvA.setText("");
            tvB.setText("");
            tvC.setText("");
            tvD.setText("");
            tvAverageScore.setText("");
            tvTotalScore.setText("");
            edtMemberScore.setText(getString(R.string.opinion_, ""));
            presenter.querySubmittedVoters(-1);
            return;
        }
        tvStatus.setText(Constant.getVoteState(getContext(), item.getVotestate()));
        edtDesc.setText(item.getContent().toStringUtf8());
        edtFile.setText(jni.queryFileNameByMediaId(item.getFileid()));
        tvRegister.setText(item.getMode() == InterfaceMacro.Pb_MeetVoteMode.Pb_VOTEMODE_signed_VALUE
                ? getContext().getString(R.string.yes)
                : getContext().getString(R.string.no));
        tvShouldAttend.setText(String.valueOf(item.getShouldmembernum()));
        tvReviewedAttend.setText(String.valueOf(item.getRealmembernum()));
        tvA.setText(getScore(item, 0));
        tvB.setText(getScore(item, 1));
        tvC.setText(getScore(item, 2));
        tvD.setText(getScore(item, 3));
        double total = getTotal(item);
        if (item.getRealmembernum() == 0) {
            tvAverageScore.setText(String.valueOf(0));
        } else {
            double average = ArithUtil.div(total, item.getSelectcount() * item.getRealmembernum(), 1);
            tvAverageScore.setText(String.valueOf(average));
        }
        tvTotalScore.setText(String.valueOf(total));
        if (scoreMemberAdapter != null && currentSelectedVoteId != item.getVoteid()) {
            scoreMemberAdapter.setSelectedId(-1);
            edtMemberScore.setText(getString(R.string.opinion_, ""));
        }
        presenter.querySubmittedVoters(item.getVoteid());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_launch_score: {
                if (scoreAdapter != null) {
                    InterfaceFilescorevote.pbui_Type_Item_UserDefineFileScore selectedScore = scoreAdapter.getSelectedScore();
                    if (selectedScore != null) {
                        if (presenter.noVoteingScore()) {
                            if (selectedScore.getVotestate() == InterfaceMacro.Pb_MeetVoteStatus.Pb_vote_notvote_VALUE) {
                                showChooseOnLineMemberDialog(selectedScore, InterfaceMacro.Pb_VoteStartFlag.Pb_MEET_VOTING_FLAG_ZERO_VALUE);
                            } else if(selectedScore.getVotestate() == InterfaceMacro.Pb_MeetVoteStatus.Pb_vote_endvote_VALUE){
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
                break;
            }
            case R.id.btn_stop_score: {
                if (scoreAdapter != null) {
                    InterfaceFilescorevote.pbui_Type_Item_UserDefineFileScore selectedScore = scoreAdapter.getSelectedScore();
                    if (selectedScore != null) {
                        if (selectedScore.getVotestate() == InterfaceMacro.Pb_MeetVoteStatus.Pb_vote_voteing_VALUE) {
                            jni.stopScore(selectedScore.getVoteid());
                        } else {
                            ToastUtils.showShort(R.string.please_choose_voteing_score);
                        }
                    } else {
                        ToastUtils.showShort(R.string.please_choose_score);
                    }
                }
                break;
            }
            default:
                break;
        }
    }

    private void showChooseOnLineMemberDialog(InterfaceFilescorevote.pbui_Type_Item_UserDefineFileScore item, int voteflag) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.pop_online_member, null, false);
        PopupWindow memberPop = PopUtil.createHalfPop(inflate, rvMemberScore);
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
