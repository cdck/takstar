package xlk.takstar.paperless.adapter;

import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.model.bean.DevMember;

/**
 * @author Created by xlk on 2020/12/7.
 * @desc
 */
public class DevMemberAdapter extends BaseQuickAdapter<DevMember, BaseViewHolder> {
    private List<Integer> checks = new ArrayList<>();
    private boolean isSingle = false;

    public DevMemberAdapter(int layoutResId, @Nullable List<DevMember> data) {
        super(layoutResId, data);
    }

    public DevMemberAdapter(@Nullable List<DevMember> data) {
        super(R.layout.item_dev_member, data);
    }

    public boolean isSingle() {
        return isSingle;
    }

    public void setSingle(boolean single) {
        isSingle = single;
    }

    public void setCheck(int memberId) {
        if (isSingle) {
            checks.clear();
            checks.add(memberId);
        } else {
            if (checks.contains(memberId)) {
                checks.remove(checks.indexOf(memberId));
            } else {
                checks.add(memberId);
            }
        }
        notifyDataSetChanged();
    }

    public List<Integer> getCheckMemberIds() {
        List<Integer> temps = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            DevMember devMember = mData.get(i);
            if (checks.contains(devMember.getMemberDetailInfo().getPersonid())) {
                temps.add(devMember.getMemberDetailInfo().getPersonid());
            }
        }
        return temps;
    }

    public List<Integer> getCheckDeviceIds() {
        List<Integer> temps = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            DevMember devMember = mData.get(i);
            if (checks.contains(devMember.getMemberDetailInfo().getPersonid())) {
                temps.add(devMember.getDeviceDetailInfo().getDevcieid());
            }
        }
        return temps;
    }

    public boolean isCheckAll() {
        return checks.size() == mData.size() && mData.size() > 0;
    }

    public void setCheckAll(boolean all) {
        checks.clear();
        if (all) {
            for (int i = 0; i < mData.size(); i++) {
                DevMember devMember = mData.get(i);
                int personid = devMember.getMemberDetailInfo().getPersonid();
                checks.add(personid);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder helper, DevMember item) {
        Button item_btn = helper.getView(R.id.item_single_btn);
        item_btn.setSelected(checks.contains(item.getMemberDetailInfo().getPersonid()));
        helper.setText(R.id.item_single_btn, item.getMemberDetailInfo().getName().toStringUtf8());
    }
}
