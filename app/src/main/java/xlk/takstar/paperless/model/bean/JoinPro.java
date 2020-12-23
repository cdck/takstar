package xlk.takstar.paperless.model.bean;

import com.mogujie.tt.protobuf.InterfaceDevice;

/**
 * @author xlk
 * @date 2020/3/28
 * @desc 可加入同屏的投影机
 */
public class JoinPro {
    InterfaceDevice.pbui_Item_DeviceResPlay resPlay;
    InterfaceDevice.pbui_Item_DeviceDetailInfo device;

    public JoinPro(InterfaceDevice.pbui_Item_DeviceResPlay resPlay, InterfaceDevice.pbui_Item_DeviceDetailInfo device) {
        this.resPlay = resPlay;
        this.device = device;
    }

    public InterfaceDevice.pbui_Item_DeviceResPlay getResPlay() {
        return resPlay;
    }

    public InterfaceDevice.pbui_Item_DeviceDetailInfo getDevice() {
        return device;
    }
}
