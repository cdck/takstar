package xlk.takstar.paperless.adapter;

import android.widget.CheckBox;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mogujie.tt.protobuf.InterfaceMacro;
import com.mogujie.tt.protobuf.InterfaceMember;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.model.Constant;

/**
 * @author Created by xlk on 2020/12/9.
 * @desc
 */
public class MemberDetailAdapter extends BaseQuickAdapter<InterfaceMember.pbui_Item_MeetMemberDetailInfo, BaseViewHolder> {
    List<Integer> ids = new ArrayList<>();

    public MemberDetailAdapter(int layoutResId, @Nullable List<InterfaceMember.pbui_Item_MeetMemberDetailInfo> data) {
        super(layoutResId, data);
    }

    public boolean isCheckedAll() {
        List<InterfaceMember.pbui_Item_MeetMemberDetailInfo> canChooseVote = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            InterfaceMember.pbui_Item_MeetMemberDetailInfo item = mData.get(i);
            if (isCanChoose(item)) {
                canChooseVote.add(item);
            }
        }
        return canChooseVote.size() == ids.size() && !ids.isEmpty();
    }

    public void setCheckedAll(boolean all) {
        ids.clear();
        if (all) {
            for (int i = 0; i < mData.size(); i++) {
                InterfaceMember.pbui_Item_MeetMemberDetailInfo item = mData.get(i);
                if (isCanChoose(item)) {
                    ids.add(item.getMemberid());
                }
            }
        }
        notifyDataSetChanged();
    }

    public List<Integer> getSelectedIds() {
        return ids;
    }

    public void setSelectedId(int id) {
        if (ids.contains(id)) {
            ids.remove(ids.indexOf(id));
        } else {
            boolean isCan = false;
            for (int i = 0; i < mData.size(); i++) {
                InterfaceMember.pbui_Item_MeetMemberDetailInfo info = mData.get(i);
                if (id == info.getMemberid()) {
                    isCan = isCanChoose(info);
                    break;
                }
            }
            if (isCan) {
                ids.add(id);
            } else {
                ToastUtils.showShort(R.string.attendees_are_unable_to_vote);
            }
        }
        notifyDataSetChanged();
    }

    private boolean isCanChoose(InterfaceMember.pbui_Item_MeetMemberDetailInfo info) {
        boolean online = info.getMemberdetailflag() == InterfaceMember.Pb_MemberDetailFlag.Pb_MEMBERDETAIL_FLAG_ONLINE_VALUE;
        return info.getDevid() != 0 && online && Constant.isHasPermission(info.getPermission(), Constant.permission_code_vote);
    }

    @Override
    protected void convert(BaseViewHolder helper, InterfaceMember.pbui_Item_MeetMemberDetailInfo item) {
        boolean ishas = Constant.isHasPermission(item.getPermission(), Constant.permission_code_vote);
        boolean isonline = item.getMemberdetailflag() == InterfaceMember.Pb_MemberDetailFlag.Pb_MEMBERDETAIL_FLAG_ONLINE_VALUE;
        boolean isCan = false;
        int facestatus = item.getFacestatus();
        String state;
        if (item.getDevid() == 0) {
            state = mContext.getString(R.string.not_bind_dev);
        } else {
            if (isonline) {
                state = mContext.getString(R.string.online);
                if (facestatus != InterfaceMacro.Pb_MeetFaceStatus.Pb_MemState_MemFace_VALUE) {
                    state += " / " + mContext.getString(R.string.not_on_meet);
                }
                if (ishas) {//有权限
                    isCan = true;
                }
            } else {
                state = mContext.getString(R.string.offline);
            }
        }
        helper.setText(R.id.item_vote_member_number, String.valueOf(helper.getLayoutPosition() + 1))
                .setText(R.id.item_vote_member_name, item.getMembername().toStringUtf8())
                .setText(R.id.item_vote_member_seat, item.getDevname().toStringUtf8())
                .setText(R.id.item_vote_member_state, state)
                .setText(R.id.item_vote_member_permissions, ishas ? "√" : mContext.getResources().getString(R.string.no_permission));
        int textColor = isCan ? mContext.getResources().getColor(R.color.btn_normal_bg_color) : mContext.getResources().getColor(R.color.normal_text_color);
        helper.setTextColor(R.id.item_vote_member_number, textColor)
                .setTextColor(R.id.item_vote_member_name, textColor)
                .setTextColor(R.id.item_vote_member_seat, textColor)
                .setTextColor(R.id.item_vote_member_state, textColor)
                .setTextColor(R.id.item_vote_member_permissions, textColor);
        CheckBox cb = helper.getView(R.id.item_vote_member_number);
        cb.setChecked(ids.contains(item.getMemberid()));
    }
}
