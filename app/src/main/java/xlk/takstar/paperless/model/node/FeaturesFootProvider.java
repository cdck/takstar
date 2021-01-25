package xlk.takstar.paperless.model.node;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
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
    @Override
    public int getItemViewType() {
        return FeaturesNodeAdapter.NODE_TYPE_FOOT;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_feature_foot_layout;
    }

    @Override
    public void convert(@NotNull BaseViewHolder baseViewHolder, BaseNode baseNode) {
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
        // 这里使用payload进行增量刷新（避免整个item刷新导致的闪烁，不自然）
        if (getAdapter() != null) {
            getAdapter().expandOrCollapse(position);
        }
    }
}
