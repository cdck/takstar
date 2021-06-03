package xlk.takstar.paperless.fragment.chat;

import com.google.protobuf.InvalidProtocolBufferException;
import com.mogujie.tt.protobuf.InterfaceBase;
import com.mogujie.tt.protobuf.InterfaceDevice;
import com.mogujie.tt.protobuf.InterfaceIM;
import com.mogujie.tt.protobuf.InterfaceMacro;
import com.mogujie.tt.protobuf.InterfaceMember;
import com.mogujie.tt.protobuf.InterfaceRoom;

import java.util.ArrayList;
import java.util.List;

import xlk.takstar.paperless.base.BasePresenter;
import xlk.takstar.paperless.model.EventMessage;
import xlk.takstar.paperless.model.GlobalValue;
import xlk.takstar.paperless.model.bean.ChatDeviceMember;
import xlk.takstar.paperless.model.bean.MyChatMessage;
import xlk.takstar.paperless.util.LogUtil;

import static xlk.takstar.paperless.meet.MeetingActivity.chatIsShowing;
import static xlk.takstar.paperless.meet.MeetingPresenter.imMessages;
import static xlk.takstar.paperless.model.GlobalValue.localDeviceId;

/**
 * @author Created by xlk on 2020/12/2.
 * @desc
 */
public class ChatPresenter extends BasePresenter<ChatContract.View> implements ChatContract.Presenter {

    public List<ChatDeviceMember> deviceMembers = new ArrayList<>();
    private List<InterfaceMember.pbui_Item_MeetMemberDetailInfo> memberDetails = new ArrayList<>();
    /**
     * 当前正在交流的参会人
     */
    private int currentMemberId;
    List<MyChatMessage> currentMessages = new ArrayList<>();

    public ChatPresenter(ChatContract.View view) {
        super(view);
    }

    public void setCurrentMemberId(int id) {
        currentMemberId = id;
    }

    @Override
    protected void busEvent(EventMessage msg) throws InvalidProtocolBufferException {
        switch (msg.getType()) {
            //会议交流
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETIM_VALUE: {
                if (chatIsShowing) {
                    byte[] o = (byte[]) msg.getObjects()[0];
                    InterfaceIM.pbui_Type_MeetIM meetIM = InterfaceIM.pbui_Type_MeetIM.parseFrom(o);
                    LogUtil.i(TAG, "busEvent 收到会议交流信息=" + meetIM.getMembername().toStringUtf8() + "," + meetIM.getMsg().toStringUtf8());
                    //文本类消息
                    if (meetIM.getMsgtype() == InterfaceMacro.Pb_MeetIMMSG_TYPE.Pb_MEETIM_CHAT_Message_VALUE) {
                        int memberid = meetIM.getMemberid();
                        MyChatMessage newImMsg = new MyChatMessage(0, meetIM.getMembername().toStringUtf8(), memberid, meetIM.getUtcsecond(), meetIM.getMsg().toStringUtf8());
                        addImMessage(memberid, newImMsg);
                        if (memberid != currentMemberId) {
                            updateMember(memberid, meetIM.getUtcsecond());
                        }
                    }
                }
                break;
            }
            //设备会议信息变更通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_DEVICEFACESHOW_VALUE: {
                LogUtil.i(TAG, "BusEvent -->" + "设备会议信息变更通知");
                queryDeviceMeetInfo();
                break;
            }
            //参会人变更通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEMBER_VALUE: {
                LogUtil.i(TAG, "busEvent 参会人变更通知");
                queryMember();
                break;
            }
            //界面状态变更通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_DEVICEMEETSTATUS_VALUE: {
                LogUtil.i(TAG, "busEvent 界面状态变更通知");
                int o = (int) msg.getObjects()[1];
                if (o > 0) {
                    queryMember();
                }
                break;
            }
            //设备寄存器变更通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_DEVICEINFO_VALUE: {
                int o1 = (int) msg.getObjects()[1];
                if (msg.getMethod() == InterfaceMacro.Pb_Method.Pb_METHOD_MEET_INTERFACE_NOTIFY_VALUE
                        && o1 > 0) {
                    LogUtil.i(TAG, "busEvent 设备寄存器变更通知");
                    queryMember();
                }
                break;
            }
            //会议排位变更通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETSEAT_VALUE: {
                byte[] datas = (byte[]) msg.getObjects()[0];
                InterfaceBase.pbui_MeetNotifyMsg inform = InterfaceBase.pbui_MeetNotifyMsg.parseFrom(datas);
                LogUtil.d(TAG, "BusEvent -->" + "会议排位变更通知 id=" + inform.getId() + ",operMethod=" + inform.getOpermethod());
                queryMeetRanking();
                break;
            }
            default:
                break;
        }
    }

    public void addImMessage(int memberid, MyChatMessage newImMsg) {
        LogUtil.i(TAG, "addImMessage memberId=" + memberid);
        List<MyChatMessage> myChatMessages;
        if (imMessages.containsKey(memberid)) {
            myChatMessages = imMessages.get(memberid);
            LogUtil.i(TAG, "addImMessage 获取消息数据 " + myChatMessages.size());
        } else {
            myChatMessages = new ArrayList<>();
            LogUtil.i(TAG, "addImMessage 新建消息数据 " + myChatMessages.size());
        }
        myChatMessages.add(newImMsg);
        imMessages.put(memberid, myChatMessages);
        updateRvMessage();
    }

    public void updateRvMessage() {
        currentMessages.clear();
        if (imMessages.containsKey(currentMemberId)) {
            List<MyChatMessage> items = imMessages.get(currentMemberId);
            currentMessages.addAll(items);
        }
        LogUtil.i(TAG, "updateRvMessage 消息个数=" + currentMessages.size());
        mView.updateMessageRv(currentMessages);
    }

    public void refreshUnreadCount() {
        for (int i = 0; i < deviceMembers.size(); i++) {
            ChatDeviceMember item = deviceMembers.get(i);
            long lastCheckTime = item.getLastCheckTime();
            int personid = item.getMemberId();
            int newCount = 0;
            if (imMessages.containsKey(personid)) {
                List<MyChatMessage> chatMessages = imMessages.get(personid);
                for (int j = 0; j < chatMessages.size(); j++) {
                    MyChatMessage info = chatMessages.get(j);
                    if (info.getUtcSecond() > lastCheckTime) {
                        newCount++;
                    }
                }
            }
            if (currentMemberId == personid) {
                newCount = 0;
            }
            item.setCount(newCount);
        }
        mView.updateMemberList();
    }

    public void updateMember(int memberid, long utcsecond) {
        for (int i = 0; i < deviceMembers.size(); i++) {
            ChatDeviceMember item = deviceMembers.get(i);
            if (item.getMemberId() == memberid) {
                long lastCheckTime = item.getLastCheckTime();
                if (utcsecond > lastCheckTime) {
                    if (memberid != currentMemberId) {
                        item.setCount(item.getCount() + 1);
                    } else {
                        item.setCount(0);
                    }
                }
            }
        }
        mView.updateMemberList();
    }

    @Override
    public void queryDeviceMeetInfo() {
        InterfaceDevice.pbui_Type_DeviceFaceShowDetail info = jni.queryDeviceMeetInfo();
        if (info != null) {
            String meetingName = info.getMeetingname().toStringUtf8();
            mView.updateMeetingName(meetingName);
        }
    }

    @Override
    public void queryMember() {
        InterfaceMember.pbui_Type_MeetMemberDetailInfo memberDetailInfo = jni.queryMemberDetailed();
        memberDetails.clear();
        if (memberDetailInfo != null) {
            List<InterfaceMember.pbui_Item_MeetMemberDetailInfo> itemList = memberDetailInfo.getItemList();
            for (int i = 0; i < itemList.size(); i++) {
                InterfaceMember.pbui_Item_MeetMemberDetailInfo item = itemList.get(i);
                if (item.getMemberdetailflag() == InterfaceMember.Pb_MemberDetailFlag.Pb_MEMBERDETAIL_FLAG_ONLINE_VALUE
                        && item.getFacestatus() == InterfaceMacro.Pb_MeetFaceStatus.Pb_MemState_MemFace_VALUE
                        && item.getDevid() != GlobalValue.localDeviceId) {
                    memberDetails.add(item);
                }
            }
        }
        queryMeetRanking();
    }

    private void queryMeetRanking() {
        InterfaceRoom.pbui_Type_MeetSeatDetailInfo info = jni.queryMeetRanking();
        List<ChatDeviceMember> temps = new ArrayList<>();
        temps.addAll(deviceMembers);
        deviceMembers.clear();
        if (info != null) {
            List<InterfaceRoom.pbui_Item_MeetSeatDetailInfo> itemList = info.getItemList();
            for (int i = 0; i < itemList.size(); i++) {
                InterfaceRoom.pbui_Item_MeetSeatDetailInfo item = itemList.get(i);
                for (int j = 0; j < memberDetails.size(); j++) {
                    if (item.getNameId() == memberDetails.get(j).getMemberid()) {
                        deviceMembers.add(new ChatDeviceMember(memberDetails.get(j), item));
                    }
                }
            }
        }
        for (int i = 0; i < temps.size(); i++) {
            ChatDeviceMember temp = temps.get(i);
            for (int j = 0; j < deviceMembers.size(); j++) {
                ChatDeviceMember item = deviceMembers.get(j);
                if (temp.getMemberId() == item.getMemberId()) {
                    item.setLastCheckTime(temp.getLastCheckTime());
                    break;
                }
            }
        }
        mView.updateMemberList();
    }
}
