package xlk.takstar.paperless.adapter;

import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.mogujie.tt.protobuf.InterfaceDevice;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import xlk.takstar.paperless.R;

/**
 * @author Created by xlk on 2020/12/10.
 * @desc
 */
public class ProjectionAdapter extends BaseQuickAdapter<InterfaceDevice.pbui_Item_DeviceDetailInfo, BaseViewHolder> {
    List<Integer> checks = new ArrayList<>();

    public ProjectionAdapter(int layoutResId, @Nullable List<InterfaceDevice.pbui_Item_DeviceDetailInfo> data) {
        super(layoutResId, data);
    }

    public ProjectionAdapter(@Nullable List<InterfaceDevice.pbui_Item_DeviceDetailInfo> data) {
        super(R.layout.item_dev_member, data);
    }

    public void setCheck(int deviceId) {
        if (checks.contains(deviceId)) {
            checks.remove(checks.indexOf(deviceId));
        } else {
            checks.add(deviceId);
        }
        notifyDataSetChanged();
    }

    public List<Integer> getCheckDeviceIds() {
        List<Integer> temps = new ArrayList<>();
        ;
        for (int i = 0; i < getData().size(); i++) {
            int devcieid = getData().get(i).getDevcieid();
            if (checks.contains(devcieid)) {
                temps.add(devcieid);
            }
        }
        return temps;
    }

    public void setCheckAll(boolean all) {
        checks.clear();
        if (all) {
            for (int i = 0; i < getData().size(); i++) {
                checks.add(getData().get(i).getDevcieid());
            }
        }
        notifyDataSetChanged();
    }

    public boolean isCheckedAll() {
        return checks.size() == getData().size();
    }

    @Override
    protected void convert(BaseViewHolder helper, InterfaceDevice.pbui_Item_DeviceDetailInfo item) {
        Button item_btn = helper.getView(R.id.item_single_btn);
        item_btn.setSelected(checks.contains(item.getDevcieid()));
        helper.setText(R.id.item_single_btn, item.getDevname().toStringUtf8());
    }
}
