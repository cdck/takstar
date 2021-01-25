package xlk.takstar.paperless.adapter;

import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.mogujie.tt.protobuf.InterfaceDevice;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import xlk.takstar.paperless.R;

/**
 * @author xlk
 * @date 2020/3/27
 * @desc悬浮框中在线投影机
 */
public class WmProjectorAdapter extends BaseQuickAdapter<InterfaceDevice.pbui_Item_DeviceDetailInfo, BaseViewHolder> {
    List<Integer> ids = new ArrayList<>();

    public WmProjectorAdapter(int layoutResId, @Nullable List<InterfaceDevice.pbui_Item_DeviceDetailInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, InterfaceDevice.pbui_Item_DeviceDetailInfo item) {
        helper.setText(R.id.item_tv_number, String.valueOf(helper.getLayoutPosition() + 1))
                .setText(R.id.item_tv_name, item.getDevname().toStringUtf8());
        CheckBox cb = helper.getView(R.id.item_cb);
        cb.setChecked(ids.contains(item.getDevcieid()));
    }

    public List<Integer> getChooseIds() {
        return ids;
    }

    public void notifyChecks() {
        List<Integer> temp = new ArrayList<>();
        for (int i = 0; i < getData().size(); i++) {
            if (ids.contains(getData().get(i).getDevcieid())) {
                temp.add(getData().get(i).getDevcieid());
            }
        }
        ids = temp;
        notifyDataSetChanged();
    }

    public void choose(int devId) {
        if (ids.contains(devId)) {
            ids.remove(ids.indexOf(devId));
        } else {
            ids.add(devId);
        }
        notifyDataSetChanged();
    }

    public boolean isChooseAll() {
        return getData().size() == ids.size();
    }

    public void setChooseAll(boolean isAll) {
        ids.clear();
        if (isAll) {
            for (int i = 0; i < getData().size(); i++) {
                ids.add(getData().get(i).getDevcieid());
            }
        }
        notifyDataSetChanged();
    }
}
