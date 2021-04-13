package xlk.takstar.paperless.model.node;

import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseNodeAdapter;
import com.chad.library.adapter.base.entity.node.BaseNode;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Created by xlk on 2021/1/21.
 * @desc
 */

public class FeaturesNodeAdapter extends BaseNodeAdapter {
    public static final int NODE_TYPE_PARENT = 0;
    public static final int NODE_TYPE_CHILD = 1;
    public static final int NODE_TYPE_FOOT = 2;
    private final FeaturesParentProvider parentProvider;
    private final FeaturesChildProvider childProvider;
    private final FeaturesFootProvider footProvider;
    private NodeClickItem listener;


    public enum ClickType {
        FEATURE, DIRECTORY, FOOT_FEATURE;
    }

    public FeaturesNodeAdapter(@Nullable List<BaseNode> nodeList) {
        super(nodeList);
        parentProvider = new FeaturesParentProvider();
        addNodeProvider(parentProvider);
        childProvider = new FeaturesChildProvider();
        addNodeProvider(childProvider);
        footProvider = new FeaturesFootProvider();
        addFooterNodeProvider(footProvider);
    }

    public void expandOrCollapseOtherFeature(boolean expand) {
        for (int i = 0; i < getData().size(); i++) {
            BaseNode baseNode = getData().get(i);
            if (baseNode instanceof FeaturesFootNode) {
                FeaturesFootNode footNode = (FeaturesFootNode) baseNode;
                LogUtils.i("xlklog", "expandOrCollapseOtherFeature 展开=" + expand);
                footNode.setExpanded(expand);
                notifyDataSetChanged();
                return;
            }
        }
    }

    public void setDefaultSelected(int id) {
        parentProvider.setDefaultSelected(id);
    }

    public void clearFootSelectedStatus() {
        footProvider.clearSelected();
        notifyDataSetChanged();
    }

    public void clearParentSelectedStatus() {
        parentProvider.clearSelectedStatus();
        notifyDataSetChanged();
    }

    public void clearChildSelectedStatus() {
        childProvider.clearCurrentChildId();
        notifyDataSetChanged();
    }

    public void setSelectChildFeature(int id) {
        childProvider.setSelectedChildFeature(id);
        notifyDataSetChanged();
    }

    public void setCurrentChildId(int id){
        childProvider.setCurrentChildId(id);
        notifyDataSetChanged();
    }

    public int getCurrentChildId() {
        return childProvider.getCurrentChildId();
    }

    public void clickFeature(ClickType clickType, Object... obj) {
        if (listener != null) {
            listener.onClickItem(clickType, obj);
        }
    }


    public void setNodeClickItemListener(NodeClickItem listener) {
        this.listener = listener;
    }

    public interface NodeClickItem {
        void onClickItem(ClickType clickType, Object... obj);
    }

    @Override
    protected int getItemType(@NotNull List<? extends BaseNode> list, int position) {
        BaseNode baseNode = list.get(position);
        if (baseNode instanceof FeaturesParentNode) {
            return NODE_TYPE_PARENT;
        } else if (baseNode instanceof FeaturesChildNode) {
            return NODE_TYPE_CHILD;
        } else if (baseNode instanceof FeaturesFootNode) {
            return NODE_TYPE_FOOT;
        }
        return -1;
    }

}
