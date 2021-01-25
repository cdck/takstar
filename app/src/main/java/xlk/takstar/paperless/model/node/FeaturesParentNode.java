package xlk.takstar.paperless.model.node;

import com.chad.library.adapter.base.entity.node.BaseExpandNode;
import com.chad.library.adapter.base.entity.node.BaseNode;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Created by xlk on 2021/1/21.
 * @desc
 */
public class FeaturesParentNode extends BaseExpandNode {
    private final int featureId;
    private List<BaseNode> childNode;

    public FeaturesParentNode(int feature_id) {
        this.featureId = feature_id;
    }

    public int getFeatureId() {
        return featureId;
    }

    public void setChildNode(List<BaseNode> childNode) {
        this.childNode = childNode;
    }

    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return childNode;
    }
}
