package xlk.takstar.paperless.fragment.screen;

import com.google.protobuf.InvalidProtocolBufferException;
import com.mogujie.tt.protobuf.InterfaceDevice;
import com.mogujie.tt.protobuf.InterfaceMacro;
import com.mogujie.tt.protobuf.InterfaceMember;

import java.util.ArrayList;
import java.util.List;

import xlk.takstar.paperless.base.BasePresenter;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.model.EventMessage;
import xlk.takstar.paperless.model.bean.DevMember;
import xlk.takstar.paperless.util.LogUtil;

/**
 * @author Created by xlk on 2020/12/10.
 * @desc
 */
public class ScreenManagePresenter extends BasePresenter<ScreenManageContract.View> implements ScreenManageContract.Presenter {

    private List<InterfaceMember.pbui_Item_MemberDetailInfo> members = new ArrayList<>();
    public List<InterfaceDevice.pbui_Item_DeviceDetailInfo> onLineProjectors = new ArrayList<>();
    public List<DevMember> sourceMembers = new ArrayList<>();
    public List<DevMember> targetMembers = new ArrayList<>();

    public ScreenManagePresenter(ScreenManageContract.View view) {
        super(view);
    }

    @Override
    protected void busEvent(EventMessage msg) throws InvalidProtocolBufferException {
        switch (msg.getType()) {
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_DEVICEINFO_VALUE://设备寄存器变更通知
                LogUtil.d(TAG, "BusEvent -->" + "设备寄存器变更通知 ");
                queryDevice();
                break;
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEMBER_VALUE://参会人员变更通知
                LogUtil.d(TAG, "BusEvent -->" + "参会人员变更通知");
                queryData();
                break;
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_DEVICEMEETSTATUS_VALUE://界面状态变更通知
                LogUtil.d(TAG, "BusEvent -->" + "界面状态变更通知");
                queryDevice();
                break;
        }
    }

    @Override
    public void queryData() {
        InterfaceMember.pbui_Type_MemberDetailInfo info = jni.queryMember();
        members.clear();
        if (info != null) {
            members.addAll(info.getItemList());
        }
        queryDevice();
    }

    private void queryDevice() {
        InterfaceDevice.pbui_Type_DeviceDetailInfo info = jni.queryDeviceInfo();
        if (info != null) {
            List<InterfaceDevice.pbui_Item_DeviceDetailInfo> pdevList = info.getPdevList();
            onLineProjectors.clear();
            targetMembers.clear();
            sourceMembers.clear();
            for (int i = 0; i < pdevList.size(); i++) {
                InterfaceDevice.pbui_Item_DeviceDetailInfo dev = pdevList.get(i);
                int netstate = dev.getNetstate();
                int devcieid = dev.getDevcieid();
                int facestate = dev.getFacestate();
                if (netstate == 1) {
                    if (Constant.isThisDevType(InterfaceMacro.Pb_DeviceIDType.Pb_DeviceIDType_MeetProjective_VALUE, devcieid)) {//在线的投影机
                        onLineProjectors.add(dev);
                    } else {
//                        if (facestate == InterfaceMacro.Pb_MeetFaceStatus.Pb_MemState_MemFace_VALUE) {
                        for (int j = 0; j < members.size(); j++) {
                            InterfaceMember.pbui_Item_MemberDetailInfo member = members.get(j);
                            if (member.getPersonid() == dev.getMemberid()) {
                                DevMember devMember = new DevMember(dev, member);
                                targetMembers.add(devMember);
                                sourceMembers.add(devMember);
                                break;
                            }
                        }
//                        }
                    }
                }
            }
        }
        mView.updateRecyclerView();
    }
}
