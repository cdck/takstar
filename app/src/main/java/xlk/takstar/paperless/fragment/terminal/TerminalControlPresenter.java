package xlk.takstar.paperless.fragment.terminal;

import com.google.protobuf.InvalidProtocolBufferException;
import com.mogujie.tt.protobuf.InterfaceDevice;
import com.mogujie.tt.protobuf.InterfaceMacro;
import com.mogujie.tt.protobuf.InterfaceMember;
import com.mogujie.tt.protobuf.InterfaceRoom;

import java.util.ArrayList;
import java.util.List;

import xlk.takstar.paperless.base.BasePresenter;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.model.EventMessage;
import xlk.takstar.paperless.model.bean.ClientControlBean;
import xlk.takstar.paperless.model.bean.MemberRoleBean;
import xlk.takstar.paperless.util.LogUtil;

/**
 * @author Created by xlk on 2020/12/8.
 * @desc
 */
public class TerminalControlPresenter extends BasePresenter<TerminalControlContract.View> implements TerminalControlContract.Presenter {

    private List<InterfaceRoom.pbui_Item_MeetRoomDevSeatDetailInfo> seatInfos = new ArrayList<>();
    private List<ClientControlBean> clientControlBeans = new ArrayList<>();
    public List<MemberRoleBean> memberRoleBeans = new ArrayList<>();

    public TerminalControlPresenter(TerminalControlContract.View view) {
        super(view);
    }

    @Override
    protected void busEvent(EventMessage msg) throws InvalidProtocolBufferException {
        switch (msg.getType()) {
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_ROOMDEVICE_VALUE://会场设备信息变更通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_DEVICEMEETSTATUS_VALUE://界面状态变更通知
                queryRankInfo();
                break;
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_DEVICEINFO_VALUE://设备寄存器变更通知
                queryDevice();
                break;
            //参会人员变更通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEMBER_VALUE: {
                LogUtil.i(TAG, "BusEvent -->" + "参会人员变更通知");
                queryMember();
                break;
            }
            //会议排位变更通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETSEAT_VALUE: {
                LogUtil.i(TAG, "busEvent " + "会议排位变更通知");
                queryRankInfo();
                break;
            }
            default:
                break;
        }
    }

    public void queryMember() {
        InterfaceMember.pbui_Type_MemberDetailInfo info = jni.queryMember();
        memberRoleBeans.clear();
        if (info != null) {
            List<InterfaceMember.pbui_Item_MemberDetailInfo> itemList = info.getItemList();
            for (int i = 0; i < itemList.size(); i++) {
                InterfaceMember.pbui_Item_MemberDetailInfo item = itemList.get(i);
                memberRoleBeans.add(new MemberRoleBean(item));
            }
        }
        queryRankInfo();
    }

    public void queryRankInfo() {
        InterfaceRoom.pbui_Type_MeetRoomDevSeatDetailInfo info = jni.placeDeviceRankingInfo(queryCurrentRoomId());
        seatInfos.clear();
        if (info != null) {
            seatInfos.addAll(info.getItemList());
            for (int i = 0; i < memberRoleBeans.size(); i++) {
                MemberRoleBean bean = memberRoleBeans.get(i);
                for (int j = 0; j < seatInfos.size(); j++) {
                    InterfaceRoom.pbui_Item_MeetRoomDevSeatDetailInfo item = seatInfos.get(j);
                    if (item.getMemberid() == bean.getMember().getPersonid()) {
                        bean.setSeat(item);
                    }
                }
            }
        }
        mView.updateMemberRole(memberRoleBeans);
        queryDevice();
    }


    private void queryDevice() {
        InterfaceDevice.pbui_Type_DeviceDetailInfo info = jni.queryDeviceInfo();
        clientControlBeans.clear();
        if (info != null) {
            List<InterfaceDevice.pbui_Item_DeviceDetailInfo> pdevList = info.getPdevList();
            for (int i = 0; i < pdevList.size(); i++) {
                InterfaceDevice.pbui_Item_DeviceDetailInfo dev = pdevList.get(i);
                int devcieid = dev.getDevcieid();
                //是否是茶水设备
                boolean isTea = Constant.isThisDevType(InterfaceMacro.Pb_DeviceIDType.Pb_DeviceIDType_MeetService_VALUE, devcieid);
                //是否是会议数据库设备
                boolean isDatabase = Constant.isThisDevType(InterfaceMacro.Pb_DeviceIDType.Pb_DeviceIDType_MeetDBServer_VALUE, devcieid);
                if (isTea || isDatabase) {//公共设备
                    clientControlBeans.add(new ClientControlBean(dev));
                } else {
                    for (int j = 0; j < seatInfos.size(); j++) {
                        InterfaceRoom.pbui_Item_MeetRoomDevSeatDetailInfo seat = seatInfos.get(j);
                        if (seat.getDevid() == devcieid) {
                            clientControlBeans.add(new ClientControlBean(dev, seat));
                            break;
                        }
                    }
                }
            }
        }
        mView.updateClient(clientControlBeans);
    }
}
