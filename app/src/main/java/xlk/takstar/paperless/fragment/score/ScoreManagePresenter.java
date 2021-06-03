package xlk.takstar.paperless.fragment.score;

import com.blankj.utilcode.util.LogUtils;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mogujie.tt.protobuf.InterfaceBase;
import com.mogujie.tt.protobuf.InterfaceFilescorevote;
import com.mogujie.tt.protobuf.InterfaceMacro;
import com.mogujie.tt.protobuf.InterfaceMember;

import java.util.ArrayList;
import java.util.List;

import xlk.takstar.paperless.base.BasePresenter;
import xlk.takstar.paperless.model.EventMessage;
import xlk.takstar.paperless.model.EventType;
import xlk.takstar.paperless.model.bean.DevMember;
import xlk.takstar.paperless.model.bean.ScoreMember;

/**
 * @author Created by xlk on 2021/2/23.
 * @desc 评分管理
 */
class ScoreManagePresenter extends BasePresenter<ScoreManageContract.View> implements ScoreManageContract.Presenter {

    public List<InterfaceFilescorevote.pbui_Type_Item_UserDefineFileScore> scoreList = new ArrayList<>();
    private List<InterfaceMember.pbui_Item_MeetMemberDetailInfo> members = new ArrayList<>();
    public List<DevMember> onlineMembers = new ArrayList<>();
    public List<ScoreMember> scoreMembers = new ArrayList<>();
    private int currentVoteId;

    public ScoreManagePresenter(ScoreManageContract.View view) {
        super(view);
    }

    @Override
    protected void busEvent(EventMessage msg) throws InvalidProtocolBufferException {
        switch (msg.getType()) {
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_FILESCOREVOTE_VALUE: {
                if (msg.getMethod() == InterfaceMacro.Pb_Method.Pb_METHOD_MEET_INTERFACE_NOTIFY_VALUE) {
                    queryScore();
                }
                break;
            }
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEMBER_VALUE://参会人员变更通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETSEAT_VALUE://会议排位变更通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_DEVICEMEETSTATUS_VALUE://界面状态变更通知
                queryMemberDetailed();
                break;
            //投票提交会议评分相关变更
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_FILESCOREVOTESIGN_VALUE: {
                byte[] data = (byte[]) msg.getObjects()[0];
                InterfaceBase.pbui_MeetNotifyMsg pbui_meetNotifyMsg = InterfaceBase.pbui_MeetNotifyMsg.parseFrom(data);
                int id = pbui_meetNotifyMsg.getId();
                int opermethod = pbui_meetNotifyMsg.getOpermethod();
                LogUtils.d(TAG, "BusEvent -->" + "会议评分变更通知 id= " + id + ", opermethod= " + opermethod);
                if (opermethod == 1 && id == currentVoteId) {
                    queryScoreSubmittedScore(id);
                }
                break;
            }
            case EventType.BUS_UPLOAD_SCORE_FILE_FINISH: {
                String filePath = (String) msg.getObjects()[0];
                int mediaId = (int) msg.getObjects()[1];
                mView.addFile2List(filePath,mediaId);
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void queryScore() {
        InterfaceFilescorevote.pbui_Type_UserDefineFileScore object = jni.queryFileScore();
        scoreList.clear();
        if (object != null) {
            scoreList.addAll(object.getItemList());
        }
        mView.updateScoreRv();
    }

    @Override
    public void queryMemberDetailed() {
        InterfaceMember.pbui_Type_MeetMemberDetailInfo info = jni.queryMemberDetailed();
        members.clear();
        if (info != null) {
            members.addAll(info.getItemList());
        }
    }

    //判断是否没有正在进行的评分
    public boolean noVoteingScore() {
        for (int i = 0; i < scoreList.size(); i++) {
            InterfaceFilescorevote.pbui_Type_Item_UserDefineFileScore info = scoreList.get(i);
            if (info.getVotestate() == InterfaceMacro.Pb_MeetVoteStatus.Pb_vote_voteing_VALUE) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void queryScoreSubmittedScore(int voteid) {
        currentVoteId = voteid;
        InterfaceFilescorevote.pbui_Type_UserDefineFileScoreMemberStatistic info = jni.queryScoreSubmittedScore(voteid);
        scoreMembers.clear();
        if (info != null) {
            List<InterfaceFilescorevote.pbui_Type_Item_FileScoreMemberStatistic> itemList = info.getItemList();
            for (int i = 0; i < itemList.size(); i++) {
                InterfaceFilescorevote.pbui_Type_Item_FileScoreMemberStatistic score = itemList.get(i);
                int memberid = score.getMemberid();
                for (int j = 0; j < members.size(); j++) {
                    if (members.get(j).getMemberid() == memberid) {
                        scoreMembers.add(new ScoreMember(score, members.get(j)));
                    }
                }
            }
        }
        mView.updateScoreSubmitMemberList();
    }
}
