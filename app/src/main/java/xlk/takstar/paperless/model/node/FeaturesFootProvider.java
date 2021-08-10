package xlk.takstar.paperless.model.node;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseNodeAdapter;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.provider.BaseNodeProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.mogujie.tt.protobuf.InterfaceMacro;

import org.jetbrains.annotations.NotNull;

import xlk.takstar.paperless.R;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.model.GlobalValue;


/**
 * @author Created by xlk on 2021/1/21.
 * @desc 其它功能
 */
class FeaturesFootProvider extends BaseNodeProvider {
    private boolean isSelected = false;

    @Override
    public int getItemViewType() {
        return FeaturesNodeAdapter.NODE_TYPE_FOOT;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_feature_foot_layout;
    }

    public void clearSelected() {
        isSelected = false;
    }

    @Override
    public void convert(@NotNull BaseViewHolder baseViewHolder, BaseNode baseNode) {
        LinearLayout item_feature_root = baseViewHolder.getView(R.id.item_feature_root);
        ImageView item_feature_iv = baseViewHolder.getView(R.id.item_feature_iv);
        TextView item_feature_tv = baseViewHolder.getView(R.id.item_feature_tv);
        item_feature_root.setSelected(isSelected);
        item_feature_iv.setSelected(isSelected);
        item_feature_tv.setSelected(isSelected);
    }

    @Override
    public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {
        //其它功能
        if (GlobalValue.localRole == InterfaceMacro.Pb_MeetMemberRole.Pb_role_member_compere.getNumber()
                || GlobalValue.localRole == InterfaceMacro.Pb_MeetMemberRole.Pb_role_member_secretary.getNumber()
                || GlobalValue.localRole == InterfaceMacro.Pb_MeetMemberRole.Pb_role_admin.getNumber()) {

        } else {
            ToastUtils.showShort(R.string.you_have_no_permission);
            return;
        }
//        isSelected = true;
        // 这里使用payload进行增量刷新（避免整个item刷新导致的闪烁，不自然）
        FeaturesNodeAdapter adapter = (FeaturesNodeAdapter) getAdapter();
        FeaturesFootNode node = (FeaturesFootNode) data;
        boolean expanded = node.isExpanded();
        if (adapter != null) {
            adapter.expandOrCollapse(position);
//            adapter.clearParentSelectedStatus();
//            adapter.clearChildSelectedStatus();
            adapter.clickFeature(FeaturesNodeAdapter.ClickType.FOOT_FEATURE, !expanded);
        }
    }
}
