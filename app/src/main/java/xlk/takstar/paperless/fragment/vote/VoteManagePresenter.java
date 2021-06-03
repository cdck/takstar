package xlk.takstar.paperless.fragment.vote;

import com.google.protobuf.InvalidProtocolBufferException;
import com.mogujie.tt.protobuf.InterfaceMacro;
import com.mogujie.tt.protobuf.InterfaceMember;
import com.mogujie.tt.protobuf.InterfaceVote;

import java.util.ArrayList;
import java.util.List;

import xlk.takstar.paperless.base.BasePresenter;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.model.EventMessage;
import xlk.takstar.paperless.model.EventType;
import xlk.takstar.paperless.model.bean.SubmitMember;
import xlk.takstar.paperless.util.LogUtil;

import static xlk.takstar.paperless.fragment.vote.VoteManageFragment.IS_VOTE_PAGE;

/**
 * @author Created by xlk on 2020/12/8.
 * @desc
 */
public class VoteManagePresenter extends BasePresenter<VoteManageContract.View> implements VoteManageContract.Presenter {
    public List<InterfaceVote.pbui_Item_MeetVoteDetailInfo> votes = new ArrayList<>();
    public List<InterfaceMember.pbui_Item_MeetMemberDetailInfo> memberDetails = new ArrayList<>();
    public List<SubmitMember> submitMembers = new ArrayList<>();

    public VoteManagePresenter(VoteManageContract.View view) {
        super(view);
    }

    @Override
    protected void busEvent(EventMessage msg) throws InvalidProtocolBufferException {
        switch (msg.getType()) {
            //选择会议目录返回的结果
            case EventType.RESULT_DIR_PATH: {
                Object[] objects = msg.getObjects();
                int dirType = (int) objects[0];
                String dirPath = (String) objects[1];
                if (dirType == Constant.CHOOSE_DIR_TYPE_EXPORT_VOTE
                        || dirType == Constant.CHOOSE_DIR_TYPE_EXPORT_VOTE_SUBMIT) {
                    mView.updateExportDirPath(dirPath);
                }
                break;
            }
            //投票变更通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETVOTEINFO_VALUE:
                LogUtil.i(TAG, "busEvent 投票变更通知");
                queryVote();
                break;
            //会议排位变更通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETSEAT_VALUE:
                LogUtil.d(TAG, "BusEvent -->" + "会议排位变更通知");
                queryMember();
                break;
            //参会人员变更通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEMBER_VALUE:
                LogUtil.d(TAG, "BusEvent -->" + "参会人员变更通知");
                queryMember();
                break;
            //参会人员权限变更通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEMBERPERMISSION_VALUE:
                LogUtil.d(TAG, "BusEvent -->" + "参会人员权限变更通知");
                queryMember();
                break;
            //界面状态变更通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_DEVICEMEETSTATUS_VALUE:
                LogUtil.d(TAG, "BusEvent -->" + "界面状态变更通知");
                queryMember();
                break;
            default:
                break;
        }
    }

    @Override
    public void queryVote() {
        InterfaceVote.pbui_Type_MeetVoteDetailInfo info = jni.queryVote();
        votes.clear();
        if (info != null) {
            List<InterfaceVote.pbui_Item_MeetVoteDetailInfo> itemList = info.getItemList();
            for (int i = 0; i < itemList.size(); i++) {
                InterfaceVote.pbui_Item_MeetVoteDetailInfo item = itemList.get(i);
                int maintype = item.getMaintype();
                if (IS_VOTE_PAGE) {
                    if (maintype == InterfaceMacro.Pb_MeetVoteType.Pb_VOTE_MAINTYPE_vote_VALUE) {
                        votes.add(item);
                    }
                } else if (maintype == InterfaceMacro.Pb_MeetVoteType.Pb_VOTE_MAINTYPE_election_VALUE) {
                    votes.add(item);
                }
            }
        }
        mView.updateVote(votes);
    }

    @Override
    public void queryMember() {
        InterfaceMember.pbui_Type_MeetMemberDetailInfo info = jni.queryMemberDetailed();
        memberDetails.clear();
        if (info != null) {
            memberDetails.addAll(info.getItemList());
        }
        mView.updateMembers();
    }

    @Override
    public void querySubmittedVoters(InterfaceVote.pbui_Item_MeetVoteDetailInfo vote, boolean isDetails) {
        InterfaceVote.pbui_Type_MeetVoteSignInDetailInfo info = jni.querySubmittedVoters(vote.getVoteid());
        submitMembers.clear();
        if (info != null) {
            List<InterfaceVote.pbui_SubItem_VoteItemInfo> optionInfo = vote.getItemList();
            List<InterfaceVote.pbui_Item_MeetVoteSignInDetailInfo> submittedMembers = info.getItemList();
            for (int i = 0; i < submittedMembers.size(); i++) {
                InterfaceVote.pbui_Item_MeetVoteSignInDetailInfo item = submittedMembers.get(i);
                InterfaceMember.pbui_Item_MeetMemberDetailInfo memberInfo = null;
                String chooseText = "";
                for (int j = 0; j < memberDetails.size(); j++) {
                    if (memberDetails.get(j).getMemberid() == item.getId()) {
                        memberInfo = memberDetails.get(j);
                        break;
                    }
                }
                if (memberInfo == null) {
                    LogUtil.d(TAG, "querySubmittedVoters -->" + "没有找打提交人名字");
                    break;
                }
                int selcnt = item.getSelcnt();
                //int变量的二进制表示的字符串
                String string = Integer.toBinaryString(selcnt);
                //查找字符串中为1的索引位置
                int length = string.length();
                int selectedItem = 0;
                for (int j = 0; j < length; j++) {
                    char c = string.charAt(j);
                    //将 char 装换成int型整数
                    int a = c - '0';
                    if (a == 1) {
                        //索引从0开始
                        selectedItem = length - j - 1;
                        for (int k = 0; k < optionInfo.size(); k++) {
                            if (k == selectedItem) {
                                InterfaceVote.pbui_SubItem_VoteItemInfo voteOptionsInfo = optionInfo.get(k);
                                String text = voteOptionsInfo.getText().toStringUtf8();
                                if (chooseText.length() == 0) {
                                    chooseText = text;
                                } else {
                                    chooseText += " | " + text;
                                }
                            }
                        }
                    }
                }
                submitMembers.add(new SubmitMember(memberInfo, item, chooseText));
            }
            if (isDetails) {
                mView.showDetailsPop(vote);
            } else {
                mView.showChartPop(vote);
            }
        }
    }
}
