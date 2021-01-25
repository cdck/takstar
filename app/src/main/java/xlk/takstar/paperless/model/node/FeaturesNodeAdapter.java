package xlk.takstar.paperless.model.node;

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

    public FeaturesNodeAdapter(@Nullable List<BaseNode> nodeList) {
        super(nodeList);
        parentProvider = new FeaturesParentProvider();
        addNodeProvider(parentProvider);
        childProvider = new FeaturesChildProvider();
        addNodeProvider(childProvider);
        footProvider = new FeaturesFootProvider();
        addFooterNodeProvider(footProvider);
    }

    public void closeFoot() {
        for (int i = 0; i < getData().size(); i++) {
            BaseNode baseNode = getData().get(i);
            if (baseNode instanceof FeaturesFootNode) {
                FeaturesFootNode footNode = (FeaturesFootNode) baseNode;
                footNode.setExpanded(false);
                notifyDataSetChanged();
                return;
            }
        }
    }

    public void setDefaultSelected(int id) {
        parentProvider.setDefaultSelected(id);
    }

    public void clearParentSelectedStatus() {
        parentProvider.clearSelectedStatus();
        notifyDataSetChanged();
    }

    public void clearChildSelectedStatus() {
        childProvider.clearSelectedStatus();
        notifyDataSetChanged();
    }

    public int getSelectedDirId() {
        return childProvider.getSelectedDirId();
    }

    public void clickFeature(Object... obj) {
        if (listener != null) {
            listener.onClickItem(obj);
        }
    }

    public void setNodeClickItemListener(NodeClickItem listener) {
        this.listener = listener;
    }

    public interface NodeClickItem {
        void onClickItem(Object... obj);
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
