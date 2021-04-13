package xlk.takstar.paperless.adapter;

import android.view.View;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.mogujie.tt.protobuf.InterfaceDevice;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import xlk.takstar.paperless.R;

/**
 * @author Created by xlk on 2021/1/11.
 * @desc 悬浮框中在线投影机
 */
public class WmScreenProjectorAdapter extends BaseQuickAdapter<InterfaceDevice.pbui_Item_DeviceDetailInfo, BaseViewHolder> {
    List<Integer> ids = new ArrayList<>();

    public WmScreenProjectorAdapter( @Nullable List<InterfaceDevice.pbui_Item_DeviceDetailInfo> data) {
        super(R.layout.item_wm_screen, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, InterfaceDevice.pbui_Item_DeviceDetailInfo item) {
        int layoutPosition = helper.getLayoutPosition();
        CheckBox cb = helper.getView(R.id.cb_name);
        View item_root_view = helper.getView(R.id.item_root_view);
        item_root_view.setBackgroundColor(isOdd(layoutPosition)
                ? getContext().getColor(R.color.table_bg_color) : getContext().getColor(R.color.table_bg_color1));
        helper.setText(R.id.cb_name, item.getDevname().toStringUtf8());
        cb.setChecked(ids.contains(item.getDevcieid()));
    }

    /**
     * 判断是否为奇数
     *
     * @return =true奇数，=false偶数
     */
    private boolean isOdd(int number) {
        if ((number & 1) != 0) {
            return true;
        }
        return false;
    }

    public List<Integer> getChooseIds() {
        return ids;
    }


    public void choose(int devId) {
        if (ids.contains(devId)) {
            ids.remove(ids.indexOf(devId));
        } else {
            ids.add(devId);
        }
        notifyDataSetChanged();
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

    public void clearChoose() {
        ids.clear();
    }
}
