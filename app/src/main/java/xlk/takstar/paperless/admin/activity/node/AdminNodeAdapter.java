package xlk.takstar.paperless.admin.activity.node;

import com.chad.library.adapter.base.BaseNodeAdapter;
import com.chad.library.adapter.base.entity.node.BaseNode;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Created by xlk on 2021/4/20.
 * @desc
 */
public class AdminNodeAdapter extends BaseNodeAdapter {
    public static final int NODE_TYPE_PARENT = 0;
    public static final int NODE_TYPE_CHILD = 1;
    private AdminParentProvider parentProvider;
    private AdminChildProvider childProvider;

    public AdminNodeAdapter(@Nullable List<BaseNode> nodeList) {
        super(nodeList);
        this.parentProvider = new AdminParentProvider();
        this.childProvider = new AdminChildProvider();
    }

    @Override
    protected int getItemType(@NotNull List<? extends BaseNode> list, int position) {
        BaseNode baseNode = list.get(position);
        if (baseNode instanceof AdminParentNode) {
            return NODE_TYPE_PARENT;
        } else if (baseNode instanceof AdminChildNode) {
            return NODE_TYPE_CHILD;
        } else {
            return -1;
        }
    }
}
