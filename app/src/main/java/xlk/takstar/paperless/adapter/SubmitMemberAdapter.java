package xlk.takstar.paperless.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.model.bean.SubmitMember;

/**
 * @author Created by xlk on 2020/12/9.
 * @desc
 */
public class SubmitMemberAdapter extends BaseQuickAdapter<SubmitMember, BaseViewHolder> {
    public SubmitMemberAdapter(int layoutResId, @Nullable List<SubmitMember> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SubmitMember item) {
        helper.setText(R.id.item_view_1, String.valueOf(helper.getLayoutPosition() + 1))
                .setText(R.id.item_view_2, item.getMemberInfo().getMembername().toStringUtf8())
                .setText(R.id.item_view_3, item.getAnswer());
    }
}
