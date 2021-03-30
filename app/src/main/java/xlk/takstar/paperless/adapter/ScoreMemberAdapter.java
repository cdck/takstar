package xlk.takstar.paperless.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.mogujie.tt.protobuf.InterfaceFilescorevote;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import xlk.takstar.paperless.R;
import xlk.takstar.paperless.model.bean.ScoreMember;
import xlk.takstar.paperless.util.ArithUtil;

/**
 * @author Created by xlk on 2021/2/27.
 * @desc
 */
public class ScoreMemberAdapter extends BaseQuickAdapter<ScoreMember, BaseViewHolder> {
    private int selectedId = -1;

    public ScoreMemberAdapter(int layoutResId, @Nullable List<ScoreMember> data) {
        super(layoutResId, data);
    }

    public void setSelectedId(int id) {
        selectedId = id;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, ScoreMember item) {
        InterfaceFilescorevote.pbui_Type_Item_FileScoreMemberStatistic score = item.getScore();
        helper.setText(R.id.item_view_1, item.getMember().getName().toStringUtf8())
                .setText(R.id.item_view_2, String.valueOf(getFraction(score, 0)))
                .setText(R.id.item_view_3, String.valueOf(getFraction(score, 1)))
                .setText(R.id.item_view_4, String.valueOf(getFraction(score, 2)))
                .setText(R.id.item_view_5, String.valueOf(getFraction(score, 3)))
                .setText(R.id.item_view_6, String.valueOf(getFraction(score, -1)))
                .setText(R.id.item_view_7, String.valueOf(getFraction(score, -2)));
        boolean isSelected = selectedId == item.getMember().getPersonid();
        int textColor = isSelected ? getContext().getColor(R.color.white) : getContext().getColor(R.color.black);
        helper.setTextColor(R.id.item_view_1, textColor)
                .setTextColor(R.id.item_view_2, textColor)
                .setTextColor(R.id.item_view_3, textColor)
                .setTextColor(R.id.item_view_4, textColor)
                .setTextColor(R.id.item_view_5, textColor)
                .setTextColor(R.id.item_view_6, textColor)
                .setTextColor(R.id.item_view_7, textColor);

        int backgroundColor = isSelected ? getContext().getColor(R.color.blue) : getContext().getColor(R.color.white);
        helper.setBackgroundColor(R.id.item_view_1, backgroundColor)
                .setBackgroundColor(R.id.item_view_2, backgroundColor)
                .setBackgroundColor(R.id.item_view_3, backgroundColor)
                .setBackgroundColor(R.id.item_view_4, backgroundColor)
                .setBackgroundColor(R.id.item_view_5, backgroundColor)
                .setBackgroundColor(R.id.item_view_6, backgroundColor)
                .setBackgroundColor(R.id.item_view_7, backgroundColor);
    }

    /**
     * @param item 提交人信息
     * @param type 决定返回数据
     * @return type =-2平均分，=-1总分，=其它返回该选项的分数
     */
    public double getFraction(InterfaceFilescorevote.pbui_Type_Item_FileScoreMemberStatistic item, int type) {
        if (item.getState() == 1) {
            List<Integer> scoreList = item.getScoreList();
            int totalScore = 0;
            for (int i = 0; i < scoreList.size(); i++) {
                Integer integer = scoreList.get(i);
                if (type == i) {
                    return integer;
                }
                totalScore += integer;
            }
            if (type == -1) {
                return totalScore;
            }
            if (type == -2) {
                return ArithUtil.div(totalScore, scoreList.size(), 1);
            }
        }
        return 0;
    }
}
