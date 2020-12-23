package xlk.takstar.paperless.adapter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.model.bean.ChatDeviceMember;

/**
 * @author Created by xlk on 2020/12/3.
 * @desc
 */
public class ChatMemberAdapter extends BaseQuickAdapter<ChatDeviceMember, BaseViewHolder> {
    private int selectedId = -1;

    public void setSelected(int id) {
        selectedId = id;
        notifyDataSetChanged();
    }

    public int getSelectedId() {
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getMemberDetailInfo().getPersonid() == selectedId) {
                return selectedId;
            }
        }
        return -1;
    }

    public ChatMemberAdapter(int layoutResId, @Nullable List<ChatDeviceMember> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatDeviceMember item) {
        helper.setText(R.id.item_tv_name, item.getMemberDetailInfo().getName().toStringUtf8())
                .setText(R.id.item_tv_job, item.getMemberDetailInfo().getJob().toStringUtf8());
        int count = item.getCount();
        TextView item_tv_count = helper.getView(R.id.item_tv_count);
        LinearLayout item_chat_root = helper.getView(R.id.item_chat_root);
        item_tv_count.setText(String.valueOf(count));
        item_tv_count.setVisibility(count > 0 ? View.VISIBLE : View.INVISIBLE);
        item_chat_root.setSelected(selectedId == item.getMemberDetailInfo().getPersonid());

    }
}
