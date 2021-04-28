package xlk.takstar.paperless.admin.activity.node;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.provider.BaseNodeProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

import xlk.takstar.paperless.R;
import xlk.takstar.paperless.model.Constant;

/**
 * @author Created by xlk on 2021/4/20.
 * @desc
 */
public class AdminParentProvider extends BaseNodeProvider {
    @Override
    public int getItemViewType() {
        return AdminNodeAdapter.NODE_TYPE_PARENT;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_admin_parent;
    }

    @Override
    public void convert(@NotNull BaseViewHolder baseViewHolder, BaseNode node) {
        AdminParentNode parentNode = (AdminParentNode) node;
        TextView item_view_1 = baseViewHolder.getView(R.id.item_view_1);
        int id = parentNode.getId();
        switch (id) {
            //系统设置
            case Constant.admin_system_settings: {
                item_view_1.setText(R.string.system_settings);
                break;
            }
            //会议预约
            case Constant.admin_meeting_reservation: {
                item_view_1.setText(R.string.meeting_reservation);
                break;
            }
            //会前设置
            case Constant.admin_before_meeting: {
                item_view_1.setText(R.string.before_meeting);
                break;
            }
            //会中管理
            case Constant.admin_current_meeting: {
                item_view_1.setText(R.string.current_meeting);
                break;
            }
            //会后查看
            case Constant.admin_after_meeting: {
                item_view_1.setText(R.string.after_meeting);
                break;
            }
            default:
                break;
        }
    }
}
