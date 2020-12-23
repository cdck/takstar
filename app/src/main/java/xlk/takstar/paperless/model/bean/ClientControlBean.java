package xlk.takstar.paperless.model.bean;

import com.mogujie.tt.protobuf.InterfaceDevice;
import com.mogujie.tt.protobuf.InterfaceRoom;

/**
 * @author Created by xlk on 2020/12/8.
 * @desc
 */
public class ClientControlBean {
    InterfaceDevice.pbui_Item_DeviceDetailInfo deviceInfo;
    InterfaceRoom.pbui_Item_MeetRoomDevSeatDetailInfo seatInfo;

    public ClientControlBean(InterfaceDevice.pbui_Item_DeviceDetailInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public ClientControlBean(InterfaceDevice.pbui_Item_DeviceDetailInfo deviceInfo, InterfaceRoom.pbui_Item_MeetRoomDevSeatDetailInfo seatInfo) {
        this.deviceInfo = deviceInfo;
        this.seatInfo = seatInfo;
    }

    public InterfaceDevice.pbui_Item_DeviceDetailInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(InterfaceDevice.pbui_Item_DeviceDetailInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public InterfaceRoom.pbui_Item_MeetRoomDevSeatDetailInfo getSeatInfo() {
        return seatInfo;
    }

    public void setSeatInfo(InterfaceRoom.pbui_Item_MeetRoomDevSeatDetailInfo seatInfo) {
        this.seatInfo = seatInfo;
    }
}
