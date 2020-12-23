package xlk.takstar.paperless.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mogujie.tt.protobuf.InterfaceMacro;
import com.mogujie.tt.protobuf.InterfaceVote;

import java.util.List;

import androidx.annotation.Nullable;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.model.Constant;

/**
 * @author Created by xlk on 2020/12/8.
 * @desc
 */
public class VoteAdapter extends BaseQuickAdapter<InterfaceVote.pbui_Item_MeetVoteDetailInfo, BaseViewHolder> {
    int selectedId;

    public VoteAdapter(int layoutResId, @Nullable List<InterfaceVote.pbui_Item_MeetVoteDetailInfo> data) {
        super(layoutResId, data);
    }

    public void setSelectedId(int id) {
        selectedId = id;
        notifyDataSetChanged();
    }

    public InterfaceVote.pbui_Item_MeetVoteDetailInfo getSelect() {
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getVoteid() == selectedId) {
                return mData.get(i);
            }
        }
        return null;
    }

    @Override
    protected void convert(BaseViewHolder helper, InterfaceVote.pbui_Item_MeetVoteDetailInfo item) {
        LinearLayout item_vote_root = helper.getView(R.id.item_vote_root);
        item_vote_root.setSelected(selectedId == item.getVoteid());
        String content = item.getContent().toStringUtf8();
        int type = item.getType();
        String voteType = Constant.getVoteType(mContext, type);
        String mode = item.getMode() == InterfaceMacro.Pb_MeetVoteMode.Pb_VOTEMODE_agonymous_VALUE
                ? mContext.getString(R.string.anonymous) : mContext.getString(R.string.notation);
        content += "（" + voteType + "、" + mode + "）";
        LinearLayout item_options_a = helper.getView(R.id.item_options_a);
        LinearLayout item_options_b = helper.getView(R.id.item_options_b);
        LinearLayout item_options_c = helper.getView(R.id.item_options_c);
        LinearLayout item_options_d = helper.getView(R.id.item_options_d);
        LinearLayout item_options_e = helper.getView(R.id.item_options_e);
        item_options_a.setVisibility(View.GONE);
        item_options_b.setVisibility(View.GONE);
        item_options_c.setVisibility(View.GONE);
        item_options_d.setVisibility(View.GONE);
        item_options_e.setVisibility(View.GONE);
        List<InterfaceVote.pbui_SubItem_VoteItemInfo> itemList = item.getItemList();
        for (int i = 0; i < itemList.size(); i++) {
            InterfaceVote.pbui_SubItem_VoteItemInfo option = itemList.get(i);
            String text = option.getText().toStringUtf8();
            int selcnt = option.getSelcnt();
            String string = text + "：" + selcnt + "票";
            if (i == 0) {
                helper.setText(R.id.tv_a_content, string);
                item_options_a.setVisibility(View.VISIBLE);
            } else if (i == 1) {
                helper.setText(R.id.tv_b_content, string);
                item_options_b.setVisibility(View.VISIBLE);
            } else if (i == 2) {
                helper.setText(R.id.tv_c_content, string);
                item_options_c.setVisibility(View.VISIBLE);
            } else if (i == 3) {
                helper.setText(R.id.tv_d_content, string);
                item_options_d.setVisibility(View.VISIBLE);
            } else if (i == 4) {
                helper.setText(R.id.tv_e_content, string);
                item_options_e.setVisibility(View.VISIBLE);
            }
        }
        helper.setText(R.id.item_tv_title, content);
        TextView item_btn_status = helper.getView(R.id.item_btn_status);
        int votestate = item.getVotestate();
        boolean enabled = votestate != InterfaceMacro.Pb_MeetVoteStatus.Pb_vote_endvote_VALUE;
        boolean selected = votestate == InterfaceMacro.Pb_MeetVoteStatus.Pb_vote_notvote_VALUE;
        item_btn_status.setEnabled(enabled);
        item_btn_status.setSelected(selected);
        item_btn_status.setText(Constant.getVoteState(mContext, votestate));
    }

}
