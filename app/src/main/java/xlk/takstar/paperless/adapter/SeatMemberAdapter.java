package xlk.takstar.paperless.adapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.model.bean.SeatMember;

/**
 * @author Created by xlk on 2020/12/2.
 * @desc
 */
public class SeatMemberAdapter extends BaseQuickAdapter<SeatMember, BaseViewHolder> {
    private int selectedId = -1;

    public SeatMemberAdapter(int layoutResId, @Nullable List<SeatMember> data) {
        super(layoutResId, data);
    }

    public void setSelectedId(int id) {
        selectedId = id;
        notifyDataSetChanged();
    }

    public int getSelectedId() {
        for (int i = 0; i < getData().size(); i++) {
            if (getData().get(i).getSeatDetailInfo().getSeatid() == selectedId) {
                return selectedId;
            }
        }
        return -1;
    }

    @Override
    protected void convert(BaseViewHolder helper, SeatMember item) {
        helper.setText(R.id.item_tv_name, item.getMemberDetailInfo().getName().toStringUtf8());
        int devid = item.getSeatDetailInfo().getSeatid();
        boolean iss = selectedId == devid;
        helper.getView(R.id.item_seat_member_ll).setSelected(iss);
        helper.getView(R.id.item_iv_icon).setSelected(iss);
        helper.getView(R.id.item_iv_icon).setSelected(iss);
    }
}
