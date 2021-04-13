package xlk.takstar.paperless.model.node;

import com.chad.library.adapter.base.entity.node.BaseExpandNode;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.entity.node.NodeFooterImp;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import xlk.takstar.paperless.model.Constant;

/**
 * @author Created by xlk on 2021/1/21.
 * @desc 脚布局 其它功能 默认不展开
 * 展开条件是：身份为管理员、秘书、主持人
 */
public class FeaturesFootNode extends BaseExpandNode {
    List<BaseNode> otherFeatures;
    private int featureId = Constant.FUN_CODE;

    public int getFeatureId() {
        return featureId;
    }

    public FeaturesFootNode(boolean expand) {
        setExpanded(expand);
        if (otherFeatures == null) {
            otherFeatures = new ArrayList<>();
            otherFeatures.add(new FeaturesChildNode(Constant.FUN_CODE_TERMINAL));
            otherFeatures.add(new FeaturesChildNode(Constant.FUN_CODE_VOTE));
            otherFeatures.add(new FeaturesChildNode(Constant.FUN_CODE_ELECTION));
            otherFeatures.add(new FeaturesChildNode(Constant.FUN_CODE_VIDEO));
            otherFeatures.add(new FeaturesChildNode(Constant.FUN_CODE_SCREEN));
            otherFeatures.add(new FeaturesChildNode(Constant.FUN_CODE_BULLETIN));
            otherFeatures.add(new FeaturesChildNode(Constant.FUN_CODE_SCORE));
        }
    }

    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return otherFeatures;
    }
}
