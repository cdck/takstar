package xlk.takstar.paperless.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mogujie.tt.protobuf.InterfaceDevice;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import xlk.takstar.paperless.R;

/**
 * @author xlk
 * @date 2020/3/14
 * @desc 推送文件投影机列表adapter
 */
public class PopPushProjectionAdapter extends BaseQuickAdapter<InterfaceDevice.pbui_Item_DeviceDetailInfo,BaseViewHolder> {
    private List<Integer> devIds = new ArrayList<>();
    public PopPushProjectionAdapter(int layoutResId, @Nullable List<InterfaceDevice.pbui_Item_DeviceDetailInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, InterfaceDevice.pbui_Item_DeviceDetailInfo item) {
        helper.setText(R.id.item_single_btn, item.getDevname().toStringUtf8());
        helper.getView(R.id.item_single_btn).setSelected(devIds.contains(item.getDevcieid()));
    }
    public List<Integer> getDevIds(){
        return devIds;
    }

    public void notifyChecks() {
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            if (devIds.contains(mData.get(i).getDevcieid())) {
                ids.add(mData.get(i).getDevcieid());
            }
        }
        devIds = ids;
        notifyDataSetChanged();
    }

    public void choose(int devId) {
        if (devIds.contains(devId)) {
            devIds.remove(devIds.indexOf(devId));
        } else {
            devIds.add(devId);
        }
        notifyDataSetChanged();
    }

    public boolean isChooseAll() {
        return mData.size() == devIds.size();
    }

    public void setChooseAll(boolean isAll) {
        devIds.clear();
        if (isAll) {
            for (int i = 0; i < mData.size(); i++) {
                devIds.add(mData.get(i).getDevcieid());
            }
        }
        notifyDataSetChanged();
    }
}
