package xlk.takstar.paperless.fragment.score;

import com.blankj.utilcode.util.LogUtils;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mogujie.tt.protobuf.InterfaceBase;
import com.mogujie.tt.protobuf.InterfaceDevice;
import com.mogujie.tt.protobuf.InterfaceFilescorevote;
import com.mogujie.tt.protobuf.InterfaceMacro;
import com.mogujie.tt.protobuf.InterfaceMember;
import com.mogujie.tt.protobuf.InterfaceVote;

import java.util.ArrayList;
import java.util.List;

import xlk.takstar.paperless.base.BasePresenter;
import xlk.takstar.paperless.model.EventMessage;
import xlk.takstar.paperless.model.bean.DevMember;
import xlk.takstar.paperless.model.bean.ScoreMember;

/**
 * @author Created by xlk on 2021/2/23.
 * @desc 评分管理
 */
class ScoreManagePresenter extends BasePresenter<ScoreManageContract.View> implements ScoreManageContract.Presenter {

    public List<InterfaceFilescorevote.pbui_Type_Item_UserDefineFileScore> scoreList = new ArrayList<>();
    private List<InterfaceMember.pbui_Item_MemberDetailInfo> memberInfos = new ArrayList<>();
    public List<DevMember> onlineMembers = new ArrayList<>();
    public List<ScoreMember> scoreMembers = new ArrayList<>();

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
                queryMember();
                break;
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_FILESCOREVOTESIGN_VALUE://会议评分
                byte[] data = (byte[]) msg.getObjects()[0];
                InterfaceBase.pbui_MeetNotifyMsg pbui_meetNotifyMsg = InterfaceBase.pbui_MeetNotifyMsg.parseFrom(data);
                int id = pbui_meetNotifyMsg.getId();
                int opermethod = pbui_meetNotifyMsg.getOpermethod();
                LogUtils.d(TAG, "BusEvent -->" + "会议评分变更通知 id= " + id + ", opermethod= " + opermethod);
                if (opermethod == 1) {
                    querySubmittedVoters(id);
                }
                break;
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
    public void querySubmittedVoters(int voteid) {
        InterfaceFilescorevote.pbui_Type_UserDefineFileScoreMemberStatistic object = jni.queryScoreSubmittedScore(voteid);
        scoreMembers.clear();
        if (object != null) {
            List<InterfaceFilescorevote.pbui_Type_Item_FileScoreMemberStatistic> itemList = object.getItemList();
            for (int i = 0; i < itemList.size(); i++) {
                InterfaceFilescorevote.pbui_Type_Item_FileScoreMemberStatistic item = itemList.get(i);
                for (int j = 0; j < memberInfos.size(); j++) {
                    InterfaceMember.pbui_Item_MemberDetailInfo member = memberInfos.get(j);
                    if (member.getPersonid() == item.getMemberid()) {
                        scoreMembers.add(new ScoreMember(member, item));
                        break;
                    }
                }
            }
        }
        mView.updateScoreMemberRv();
    }

    @Override
    public void queryMember() {
        InterfaceMember.pbui_Type_MemberDetailInfo pbui_type_memberDetailInfo = jni.queryMember();
        memberInfos.clear();
        if (pbui_type_memberDetailInfo != null) {
            memberInfos.addAll(pbui_type_memberDetailInfo.getItemList());
        }
        queryDevice();
    }

    @Override
    public void queryDevice() {
        InterfaceDevice.pbui_Type_DeviceDetailInfo pbui_type_deviceDetailInfo = jni.queryDeviceInfo();
        onlineMembers.clear();
        if (pbui_type_deviceDetailInfo != null) {
            List<InterfaceDevice.pbui_Item_DeviceDetailInfo> pdevList = pbui_type_deviceDetailInfo.getPdevList();
            for (int i = 0; i < pdevList.size(); i++) {
                InterfaceDevice.pbui_Item_DeviceDetailInfo dev = pdevList.get(i);
                int memberid = dev.getMemberid();
                int netstate = dev.getNetstate();
                int facestate = dev.getFacestate();
                if (netstate == 1 && facestate == 1) {
                    for (int j = 0; j < memberInfos.size(); j++) {
                        InterfaceMember.pbui_Item_MemberDetailInfo member = memberInfos.get(j);
                        if (memberid == member.getPersonid()) {
                            onlineMembers.add(new DevMember(dev, member));
                            break;
                        }
                    }
                }
            }
            mView.updateOnlineMemberRv();
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
}
