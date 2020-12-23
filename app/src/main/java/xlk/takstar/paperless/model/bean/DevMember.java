package xlk.takstar.paperless.model.bean;

import com.mogujie.tt.protobuf.InterfaceDevice;
import com.mogujie.tt.protobuf.InterfaceMember;

/**
 * @author Created by xlk on 2020/12/7.
 * @desc
 */
public class DevMember {
    InterfaceDevice.pbui_Item_DeviceDetailInfo deviceDetailInfo;
    InterfaceMember.pbui_Item_MemberDetailInfo memberDetailInfo;

    public DevMember(InterfaceDevice.pbui_Item_DeviceDetailInfo deviceDetailInfo, InterfaceMember.pbui_Item_MemberDetailInfo memberDetailInfo) {
        this.deviceDetailInfo = deviceDetailInfo;
        this.memberDetailInfo = memberDetailInfo;
    }

    public InterfaceDevice.pbui_Item_DeviceDetailInfo getDeviceDetailInfo() {
        return deviceDetailInfo;
    }

    public void setDeviceDetailInfo(InterfaceDevice.pbui_Item_DeviceDetailInfo deviceDetailInfo) {
        this.deviceDetailInfo = deviceDetailInfo;
    }

    public InterfaceMember.pbui_Item_MemberDetailInfo getMemberDetailInfo() {
        return memberDetailInfo;
    }

    public void setMemberDetailInfo(InterfaceMember.pbui_Item_MemberDetailInfo memberDetailInfo) {
        this.memberDetailInfo = memberDetailInfo;
    }
}
