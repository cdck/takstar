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
 * @desc
 */
class FeaturesParentProvider extends BaseNodeProvider {
    private final String TAG = "FeaturesParentProvider-->";
    private int selectedId = -1;

    @Override
    public int getItemViewType() {
        return FeaturesNodeAdapter.NODE_TYPE_PARENT;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_feature;
    }

    public void clearSelectedStatus() {
        selectedId = -1;
    }

    public void setDefaultSelected(int id) {
        selectedId = id;
        getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {
        FeaturesParentNode parentNode = (FeaturesParentNode) data;
        int featureId = parentNode.getFeatureId();
        FeaturesNodeAdapter adapter = (FeaturesNodeAdapter) getAdapter();
        adapter.clickFeature(featureId);
        if (featureId == InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_MATERIAL_VALUE) {
            if (!parentNode.isExpanded()) {
                int selectedChildID = adapter.getSelectedDirId();
                if (selectedChildID != -1 && selectedChildID < Constant.FUN_CODE) {
                    //之前已经选中了某个目录
                    // TODO: 2021/1/22 有可能这个目录已经不存在了
                    clearSelectedStatus();
                }
            } else {
                selectedId = featureId;
            }
            adapter.expandOrCollapse(position);
            adapter.notifyDataSetChanged();
        } else {
            selectedId = featureId;
            adapter.clearChildSelectedStatus();
        }
    }

    @Override
    public void convert(@NotNull BaseViewHolder baseViewHolder, BaseNode baseNode) {
        LinearLayout item_feature_root = baseViewHolder.getView(R.id.item_feature_root);
        ImageView item_feature_iv = baseViewHolder.getView(R.id.item_feature_iv);
        TextView item_feature_tv = baseViewHolder.getView(R.id.item_feature_tv);
        FeaturesParentNode parentNode = (FeaturesParentNode) baseNode;
        int funcode = parentNode.getFeatureId();
        switch (funcode) {
            //会议议程
            case InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_AGENDA_BULLETIN_VALUE: {
                item_feature_iv.setImageResource(R.drawable.feature_agenda_icon);
                item_feature_tv.setText(getContext().getString(R.string.meeting_agenda));
                break;
            }
            //会议资料
            case InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_MATERIAL_VALUE: {
                item_feature_iv.setImageResource(R.drawable.feature_materials_icon);
                item_feature_tv.setText(getContext().getString(R.string.meeting_materials));
                break;
            }
            //批注查看
            case InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_POSTIL_VALUE: {
                item_feature_iv.setImageResource(R.drawable.feature_annotate_icon);
                item_feature_tv.setText(getContext().getString(R.string.meeting_annotate));
                break;
            }
            //互动交流
            case InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_MESSAGE_VALUE: {
                item_feature_iv.setImageResource(R.drawable.feature_chat_icon);
                item_feature_tv.setText(getContext().getString(R.string.meeting_chat));
                break;
            }
            //视频直播
            case InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_VIDEOSTREAM_VALUE: {
                item_feature_iv.setImageResource(R.drawable.feature_video_icon);
                item_feature_tv.setText(getContext().getString(R.string.meeting_video));
                break;
            }
            //电子白板
            case InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_WHITEBOARD_VALUE: {
                item_feature_iv.setImageResource(R.drawable.feature_draw_icon);
                item_feature_tv.setText(getContext().getString(R.string.meeting_draw));
                break;
            }
            //网页浏览
            case InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_WEBBROWSER_VALUE: {
                item_feature_iv.setImageResource(R.drawable.feature_web_icon);
                item_feature_tv.setText(getContext().getString(R.string.meeting_web));
                break;
            }
            //签到信息
            case InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_SIGNINRESULT_VALUE: {
                item_feature_iv.setImageResource(R.drawable.feature_signin_icon);
                item_feature_tv.setText(getContext().getString(R.string.meeting_signin));
                break;
            }
            //会议评分
            case 31: {
                item_feature_iv.setImageResource(R.drawable.feature_signin_icon);
                item_feature_tv.setText(getContext().getString(R.string.meeting_score));
                break;
            }
            default:
                break;
        }
        LogUtils.d(TAG, "当前选中的功能id=" + selectedId + ",funcode=" + funcode);
        item_feature_root.setSelected(selectedId == funcode);
        item_feature_iv.setSelected(selectedId == funcode);
        item_feature_tv.setSelected(selectedId == funcode);
    }
}
