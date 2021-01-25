package xlk.takstar.paperless.fragment.chat;

import com.google.protobuf.InvalidProtocolBufferException;
import com.mogujie.tt.protobuf.InterfaceDevice;
import com.mogujie.tt.protobuf.InterfaceIM;
import com.mogujie.tt.protobuf.InterfaceMacro;
import com.mogujie.tt.protobuf.InterfaceMember;

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

/**
 * @author Created by xlk on 2020/12/2.
 * @desc
 */
public class ChatPresenter extends BasePresenter<ChatContract.View> implements ChatContract.Presenter {

    private List<InterfaceMember.pbui_Item_MemberDetailInfo> members = new ArrayList<>();
    private List<ChatDeviceMember> deviceMembers = new ArrayList<>();
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
            int personid = item.getMemberDetailInfo().getPersonid();
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
        mView.updateDeviceMember(deviceMembers);
    }


    public void updateMember(int memberid, long utcsecond) {
        for (int i = 0; i < deviceMembers.size(); i++) {
            ChatDeviceMember item = deviceMembers.get(i);
            if (item.getMemberDetailInfo().getPersonid() == memberid) {
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
        mView.updateDeviceMember(deviceMembers);
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
        InterfaceMember.pbui_Type_MemberDetailInfo info = jni.queryMember();
        members.clear();
        if (info != null) {
            members.addAll(info.getItemList());
        }
        queryDevice();
    }

    @Override
    public void queryDevice() {
        InterfaceDevice.pbui_Type_DeviceDetailInfo info = jni.queryDeviceInfo();
        List<ChatDeviceMember> temps = new ArrayList<>();
        temps.addAll(deviceMembers);
        deviceMembers.clear();
        if (info != null) {
            List<InterfaceDevice.pbui_Item_DeviceDetailInfo> pdevList = info.getPdevList();
            for (int i = 0; i < pdevList.size(); i++) {
                InterfaceDevice.pbui_Item_DeviceDetailInfo dev = pdevList.get(i);
                int devcieid = dev.getDevcieid();
                int memberid = dev.getMemberid();
                int netstate = dev.getNetstate();
                int facestate = dev.getFacestate();
                if (facestate == 1 && netstate == 1 && devcieid != GlobalValue.localDeviceId) {
                    for (int j = 0; j < members.size(); j++) {
                        InterfaceMember.pbui_Item_MemberDetailInfo member = members.get(j);
                        if (member.getPersonid() == memberid) {
                            deviceMembers.add(new ChatDeviceMember(dev, member));
                        }
                    }
                }
            }
        }
        for (int i = 0; i < temps.size(); i++) {
            ChatDeviceMember temp = temps.get(i);
            for (int j = 0; j < deviceMembers.size(); j++) {
                ChatDeviceMember item = deviceMembers.get(j);
                if (temp.getMemberDetailInfo().getPersonid() == item.getMemberDetailInfo().getPersonid()) {
                    item.setLastCheckTime(temp.getLastCheckTime());
                    break;
                }
            }
        }
        mView.updateDeviceMember(deviceMembers);
    }

}
