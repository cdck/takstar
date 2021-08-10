package xlk.takstar.paperless.model.node;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.provider.BaseNodeProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.mogujie.tt.protobuf.InterfaceMacro;

import org.jetbrains.annotations.NotNull;

import xlk.takstar.paperless.R;
import xlk.takstar.paperless.model.Constant;

/**
 * @author Created by xlk on 2021/1/21.
 * @desc
 */
class FeaturesParentProvider extends BaseNodeProvider {
    private final String TAG = "FeaturesParentProvider-->";
    private int currentParentId = -1;

    @Override
    public int getItemViewType() {
        return FeaturesNodeAdapter.NODE_TYPE_PARENT;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_feature;
    }

    //清除父类的点击状态
    public void clearSelectedStatus() {
        currentParentId = -1;
    }

    public void setDefaultSelected(int id) {
        currentParentId = id;
        getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {
        FeaturesParentNode parentNode = (FeaturesParentNode) data;
        int featureId = parentNode.getFeatureId();
        FeaturesNodeAdapter adapter = (FeaturesNodeAdapter) getAdapter();
        if (featureId == Constant.function_code_material) {
//            if (!parentNode.isExpanded()) {
//                int currentChildId = adapter.getCurrentChildId();
//                if (currentChildId != -1 && currentChildId < Constant.FUN_CODE) {
//                    //之前已经选中了某个目录
//                    //2021年3月1日14:36:24 只要点击了父类，目录的点击状态就清空掉
////                    adapter.clearChildSelectedStatus();
////                    adapter.clearFootSelectedStatus();
//                }
//            }
//            currentParentId = featureId;
//            adapter.clearFootSelectedStatus();
            adapter.expandOrCollapse(position);
            adapter.clickFeature(FeaturesNodeAdapter.ClickType.FEATURE, featureId);
//            adapter.notifyDataSetChanged();
        } else {
            currentParentId = featureId;
            adapter.clickFeature(FeaturesNodeAdapter.ClickType.FEATURE, featureId);
            adapter.clearChildSelectedStatus();
            adapter.clearFootSelectedStatus();
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
            case Constant.function_code_agenda: {
                item_feature_iv.setImageResource(R.drawable.feature_agenda_icon);
                item_feature_tv.setText(getContext().getString(R.string.function_meeting_agenda));
                break;
            }
            //会议资料
            case Constant.function_code_material: {
                item_feature_iv.setImageResource(R.drawable.feature_materials_icon);
                item_feature_tv.setText(getContext().getString(R.string.function_meeting_material));
                break;
            }
            //批注查看
            case Constant.function_code_annotation: {
                item_feature_iv.setImageResource(R.drawable.feature_annotate_icon);
                item_feature_tv.setText(getContext().getString(R.string.function_meeting_annotation));
                break;
            }
            //互动交流
            case Constant.function_code_chat: {
                item_feature_iv.setImageResource(R.drawable.feature_chat_icon);
                item_feature_tv.setText(getContext().getString(R.string.function_meeting_chat));
                break;
            }
            //视频直播
            case Constant.function_code_video: {
                item_feature_iv.setImageResource(R.drawable.feature_video_icon);
                item_feature_tv.setText(getContext().getString(R.string.function_meeting_live_video));
                break;
            }
            //电子白板
            case Constant.function_code_board: {
                item_feature_iv.setImageResource(R.drawable.feature_draw_icon);
                item_feature_tv.setText(getContext().getString(R.string.function_meeting_art_board));
                break;
            }
            //网页浏览
            case Constant.function_code_web: {
                item_feature_iv.setImageResource(R.drawable.feature_web_icon);
                item_feature_tv.setText(getContext().getString(R.string.function_meeting_web_browsing));
                break;
            }
            //签到信息
//            case Constant.function_code_sign: {
//                item_feature_iv.setImageResource(R.drawable.feature_signin_icon);
//                item_feature_tv.setText(getContext().getString(R.string.function_meeting_sign_in));
//                break;
//            }
            //评分查看
            case Constant.function_code_rate: {
                item_feature_iv.setImageResource(R.drawable.feature_score_manage_icon);
                item_feature_tv.setText(getContext().getString(R.string.function_rate_view));
                break;
            }
            //公告查看
            case Constant.function_code_bulletin: {
                item_feature_iv.setImageResource(R.drawable.feature_bulletin_icon);
                item_feature_tv.setText(getContext().getString(R.string.function_bulletin_view));
                break;
            }
            //投票查看
            case Constant.function_code_vote: {
                item_feature_iv.setImageResource(R.drawable.feature_vote_manage_icon);
                item_feature_tv.setText(getContext().getString(R.string.function_vote_view));
                break;
            }
            //选举查看
            case Constant.function_code_election: {
                item_feature_iv.setImageResource(R.drawable.feature_election_manage_icon);
                item_feature_tv.setText(getContext().getString(R.string.function_election_view));
                break;
            }
            default:
                break;
        }
        LogUtils.d(TAG, "当前选中的功能id=" + currentParentId + ",funcode=" + funcode);
        item_feature_root.setSelected(currentParentId == funcode);
        item_feature_iv.setSelected(currentParentId == funcode);
        item_feature_tv.setSelected(currentParentId == funcode);
    }
}
