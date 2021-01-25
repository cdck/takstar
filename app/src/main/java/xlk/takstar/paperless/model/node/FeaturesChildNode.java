package xlk.takstar.paperless.model.node;

import com.chad.library.adapter.base.entity.node.BaseNode;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Created by xlk on 2021/1/21.
 * @desc
 */
public class FeaturesChildNode extends BaseNode {
    private Object[] objects;

    public Object[] getObjects() {
        return objects;
    }

    public FeaturesChildNode(Object... objects) {
        this.objects = objects;
    }

    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return null;
    }
}
