package xlk.takstar.paperless.adapter;

import android.view.View;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.model.bean.DevMember;

/**
 * @author xlk
 * @date 2020/3/26
 * @desc 悬浮框中在线参会人
 */
public class WmScreenMemberAdapter extends BaseQuickAdapter<DevMember, BaseViewHolder> {
    List<Integer> devicesIds = new ArrayList<>();
    private boolean isSingle = false;

    public void setSingleCheck(boolean b) {
        isSingle = b;
    }

    public WmScreenMemberAdapter(int layoutResId, @Nullable List<DevMember> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DevMember item) {
        int layoutPosition = helper.getLayoutPosition();
        CheckBox cb = helper.getView(R.id.cb_name);
        View item_root_view = helper.getView(R.id.item_root_view);
        item_root_view.setBackgroundColor(isOdd(layoutPosition)
                ? getContext().getColor(R.color.table_bg_color) : getContext().getColor(R.color.table_bg_color1));
        helper.setText(R.id.item_tv_name, item.getMemberDetailInfo().getName().toStringUtf8());
        cb.setChecked(devicesIds.contains(item.getDeviceDetailInfo().getDevcieid()));
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

    /**
     * 作为单选adapter时才使用
     *
     * @return
     */
    public DevMember getChoose() {
        for (int i = 0; i < getData().size(); i++) {
            DevMember devMember = getData().get(i);
            if (devicesIds.contains(devMember.getDeviceDetailInfo().getDevcieid())) {
                return devMember;
            }
        }
        return null;
    }

    public void notifyChecks() {
        List<Integer> temp = new ArrayList<>();
        for (int i = 0; i < getData().size(); i++) {
            if (devicesIds.contains(getData().get(i).getDeviceDetailInfo().getDevcieid())) {
                temp.add(getData().get(i).getDeviceDetailInfo().getDevcieid());
            }
        }
        devicesIds = temp;
        notifyDataSetChanged();
    }

    public List<Integer> getSelectedDeviceIds() {
        return devicesIds;
    }

    public List<Integer> getSelectedMemberIds() {
        List<Integer> memberIds = new ArrayList<>();
        for (int i = 0; i < getData().size(); i++) {
            DevMember devMember = getData().get(i);
            if (devicesIds.contains(devMember.getDeviceDetailInfo().getDevcieid())) {
                memberIds.add(devMember.getMemberDetailInfo().getPersonid());
            }
        }
        return memberIds;
    }

    public void choose(int devId) {
        if (isSingle) {
            devicesIds.clear();
            devicesIds.add(devId);
        } else {
            if (devicesIds.contains(devId)) {
                devicesIds.remove(devicesIds.indexOf(devId));
            } else {
                devicesIds.add(devId);
            }
        }
        notifyDataSetChanged();
    }

    public boolean isChooseAll() {
        return getData().size() == devicesIds.size();
    }

    public void setChooseAll(boolean isAll) {
        devicesIds.clear();
        if (isAll) {
            for (int i = 0; i < getData().size(); i++) {
                devicesIds.add(getData().get(i).getDeviceDetailInfo().getDevcieid());
            }
        }
        notifyDataSetChanged();
    }
}
