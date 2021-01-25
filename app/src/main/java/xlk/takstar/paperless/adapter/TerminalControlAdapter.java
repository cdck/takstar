package xlk.takstar.paperless.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.mogujie.tt.protobuf.InterfaceDevice;
import com.mogujie.tt.protobuf.InterfaceRoom;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.model.bean.ClientControlBean;

/**
 * @author Created by xlk on 2020/12/8.
 * @desc
 */
public class TerminalControlAdapter extends BaseQuickAdapter<ClientControlBean, BaseViewHolder> {
    private List<Integer> checks = new ArrayList<>();

    public TerminalControlAdapter(int layoutResId, @Nullable List<ClientControlBean> data) {
        super(layoutResId, data);
    }

    public void setCheck(int id) {
        if (checks.contains(id)) {
            checks.remove(checks.indexOf(id));
        } else {
            checks.add(id);
        }
        notifyDataSetChanged();
    }

    public List<Integer> getChecks() {
        List<Integer> temps = new ArrayList<>();
        for (int i = 0; i < getData().size(); i++) {
            int devcieid = getData().get(i).getDeviceInfo().getDevcieid();
            if (checks.contains(devcieid)) {
                temps.add(devcieid);
            }
        }
        return temps;
    }


    @Override
    protected void convert(BaseViewHolder helper, ClientControlBean item) {
        InterfaceDevice.pbui_Item_DeviceDetailInfo deviceInfo = item.getDeviceInfo();
        InterfaceRoom.pbui_Item_MeetRoomDevSeatDetailInfo seatInfo = item.getSeatInfo();
        boolean isOnline = deviceInfo.getNetstate() == 1;
        helper.setText(R.id.item_view_1, String.valueOf(helper.getLayoutPosition() + 1))
                .setText(R.id.item_view_2, deviceInfo.getDevname().toStringUtf8())
                .setText(R.id.item_view_3, Constant.getDeviceTypeName(getContext(), deviceInfo.getDevcieid()))
                .setText(R.id.item_view_4, String.valueOf(deviceInfo.getDevcieid()))
                .setText(R.id.item_view_5, isOnline ? getContext().getString(R.string.online) : getContext().getString(R.string.offline))
                .setText(R.id.item_view_6, seatInfo != null ? seatInfo.getMembername().toStringUtf8() : "")
                .setText(R.id.item_view_7, Constant.getInterfaceStateName(getContext(), deviceInfo.getFacestate()));
        boolean isSelected = checks.contains(deviceInfo.getDevcieid());
        int textColor = isSelected ? getContext().getColor(R.color.white) : getContext().getColor(R.color.normal_text_color);
        helper.setTextColor(R.id.item_view_1, textColor)
                .setTextColor(R.id.item_view_2, textColor)
                .setTextColor(R.id.item_view_3, textColor)
                .setTextColor(R.id.item_view_4, textColor)
                .setTextColor(R.id.item_view_5, textColor)
                .setTextColor(R.id.item_view_6, textColor)
                .setTextColor(R.id.item_view_7, textColor);

        int backgroundColor = isSelected ? getContext().getColor(R.color.selected_item_bg) : getContext().getColor(R.color.white);
        helper.setBackgroundColor(R.id.item_view_1, backgroundColor)
                .setBackgroundColor(R.id.item_view_2, backgroundColor)
                .setBackgroundColor(R.id.item_view_3, backgroundColor)
                .setBackgroundColor(R.id.item_view_4, backgroundColor)
                .setBackgroundColor(R.id.item_view_5, backgroundColor)
                .setBackgroundColor(R.id.item_view_6, backgroundColor)
                .setBackgroundColor(R.id.item_view_7, backgroundColor);
    }
}
