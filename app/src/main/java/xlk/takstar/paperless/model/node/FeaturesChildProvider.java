package xlk.takstar.paperless.model.node;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseNodeAdapter;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.provider.BaseNodeProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

import xlk.takstar.paperless.R;
import xlk.takstar.paperless.model.Constant;

/**
 * @author Created by xlk on 2021/1/21.
 * @desc
 */
public class FeaturesChildProvider extends BaseNodeProvider {
    private int selectedDirId = -1;

    @Override
    public int getItemViewType() {
        return FeaturesNodeAdapter.NODE_TYPE_CHILD;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_feature_child_layout;
    }

    @Override
    public void convert(@NotNull BaseViewHolder baseViewHolder, BaseNode baseNode) {
        LinearLayout item_feature_root = baseViewHolder.getView(R.id.item_feature_root);
        ImageView item_feature_iv = baseViewHolder.getView(R.id.item_feature_iv);
        item_feature_iv.setVisibility(View.VISIBLE);
        TextView item_feature_tv = baseViewHolder.getView(R.id.item_feature_tv);
        FeaturesChildNode childNode = (FeaturesChildNode) baseNode;
        Object[] objects = childNode.getObjects();
        int dirId = (int) objects[0];
        if (dirId > Constant.FUN_CODE) {
            switch (dirId) {
                //终端控制
                case Constant.FUN_CODE_TERMINAL:
                    item_feature_tv.setText(getContext().getResources().getString(R.string.terminal_control));
                    item_feature_iv.setImageResource(R.drawable.feature_terminal_icon);
                    break;
                case Constant.FUN_CODE_VOTE:
                    item_feature_tv.setText(getContext().getResources().getString(R.string.vote_manage));
                    item_feature_iv.setImageResource(R.drawable.feature_vote_manage_icon);
                    break;
                case Constant.FUN_CODE_ELECTION:
                    item_feature_tv.setText(getContext().getResources().getString(R.string.election_manage));
                    item_feature_iv.setImageResource(R.drawable.feature_election_manage_icon);
                    break;
                case Constant.FUN_CODE_VIDEO:
                    item_feature_tv.setText(getContext().getResources().getString(R.string.video_control));
                    item_feature_iv.setImageResource(R.drawable.feature_video_control_icon);
                    break;
                case Constant.FUN_CODE_SCREEN:
                    item_feature_tv.setText(getContext().getResources().getString(R.string.screen_manage));
                    item_feature_iv.setImageResource(R.drawable.feature_screen_manage_icon);
                    break;
                case Constant.FUN_CODE_BULLETIN:
                    item_feature_tv.setText(getContext().getResources().getString(R.string.bulletin));
                    item_feature_iv.setImageResource(R.drawable.feature_bulletin_icon);
                    break;
            }
        } else {
            String dirName = (String) objects[1];
            item_feature_tv.setText(dirName);
            item_feature_iv.setVisibility(View.INVISIBLE);
        }
        item_feature_root.setSelected(selectedDirId == dirId);
        item_feature_iv.setSelected(selectedDirId == dirId);
        item_feature_tv.setSelected(selectedDirId == dirId);
    }

    @Override
    public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {
        FeaturesChildNode childNode = (FeaturesChildNode) data;
        selectedDirId = (int) childNode.getObjects()[0];
        FeaturesNodeAdapter adapter = (FeaturesNodeAdapter) getAdapter();
        adapter.clearParentSelectedStatus();
        if (selectedDirId > Constant.FUN_CODE) {
            adapter.clickFeature(selectedDirId);
        } else {
            adapter.clickFeature(selectedDirId, adapter.findParentNode(childNode));
        }
    }

    public void clearSelectedStatus() {
        selectedDirId = -1;
    }

    public int getSelectedDirId() {
        return selectedDirId;
    }
}
