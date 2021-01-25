package xlk.takstar.paperless.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.mogujie.tt.protobuf.InterfaceMember;
import com.mogujie.tt.protobuf.InterfaceRoom;

import java.util.List;

import androidx.annotation.Nullable;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.model.bean.MemberRoleBean;

/**
 * @author Created by xlk on 2020/12/8.
 * @desc
 */
public class MemberRoleAdapter extends BaseQuickAdapter<MemberRoleBean, BaseViewHolder> {
    private int selectedId;

    public MemberRoleAdapter(int layoutResId, @Nullable List<MemberRoleBean> data) {
        super(layoutResId, data);
    }

    public void setSelectedId(int id) {
        selectedId = id;
        notifyDataSetChanged();
    }

    public MemberRoleBean getSelected() {
        for (int i = 0; i < getData().size(); i++) {
            int personid = getData().get(i).getMember().getPersonid();
            if (selectedId == personid) {
                return getData().get(i);
            }
        }
        return null;
    }

    @Override
    protected void convert(BaseViewHolder helper, MemberRoleBean item) {
        InterfaceRoom.pbui_Item_MeetRoomDevSeatDetailInfo seat = item.getSeat();
        InterfaceMember.pbui_Item_MemberDetailInfo member = item.getMember();
        helper.setText(R.id.item_view_1, String.valueOf(helper.getLayoutPosition() + 1))
                .setText(R.id.item_view_2, member.getName().toStringUtf8())
                .setText(R.id.item_view_3, seat != null ? seat.getDevname().toStringUtf8() : "")
                .setText(R.id.item_view_4, seat != null ? Constant.getMemberRoleName(getContext(), seat.getRole()) : "");
        boolean isSelected = selectedId == member.getPersonid();
        int textColor = isSelected ? getContext().getColor(R.color.white) : getContext().getColor(R.color.normal_text_color);
        helper.setTextColor(R.id.item_view_1, textColor)
                .setTextColor(R.id.item_view_2, textColor)
                .setTextColor(R.id.item_view_3, textColor)
                .setTextColor(R.id.item_view_4, textColor);

        int backgroundColor = isSelected ? getContext().getColor(R.color.selected_item_bg) : getContext().getColor(R.color.white);
        helper.setBackgroundColor(R.id.item_view_1, backgroundColor)
                .setBackgroundColor(R.id.item_view_2, backgroundColor)
                .setBackgroundColor(R.id.item_view_3, backgroundColor)
                .setBackgroundColor(R.id.item_view_4, backgroundColor);
    }
}
