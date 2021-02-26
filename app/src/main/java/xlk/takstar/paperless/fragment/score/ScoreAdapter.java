package xlk.takstar.paperless.fragment.score;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.mogujie.tt.protobuf.InterfaceFilescorevote;
import com.mogujie.tt.protobuf.InterfaceMacro;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;

import xlk.takstar.paperless.R;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.model.JniHelper;

/**
 * @author Created by xlk on 2021/2/23.
 * @desc 文件评分adapter
 */
class ScoreAdapter extends BaseQuickAdapter<InterfaceFilescorevote.pbui_Type_Item_UserDefineFileScore, BaseViewHolder> {
    private int selectedId = -1;

    public ScoreAdapter(int layoutResId, @Nullable List<InterfaceFilescorevote.pbui_Type_Item_UserDefineFileScore> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, InterfaceFilescorevote.pbui_Type_Item_UserDefineFileScore item) {
        double total = getTotal(item);
        BigDecimal b1 = new BigDecimal(Double.toString(total));
        BigDecimal b2 = new BigDecimal(Double.toString(item.getSelectcount()));
        //默认保留两位会有错误，这里设置保留小数点后4位
        double average = b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
        helper.setText(R.id.item_view_1, item.getContent().toStringUtf8())
                .setText(R.id.item_view_2, JniHelper.getInstance().queryFileNameByMediaId(item.getFileid()))
                .setText(R.id.item_view_3, Constant.getVoteState(getContext(), item.getVotestate()))
                .setText(R.id.item_view_4, item.getShouldmembernum() + "|" + item.getRealmembernum())
                .setText(R.id.item_view_5, item.getMode() == InterfaceMacro.Pb_MeetVoteMode.Pb_VOTEMODE_agonymous_VALUE
                        ? getContext().getString(R.string.yes)
                        : getContext().getString(R.string.no)
                )
                .setText(R.id.item_view_6, getScore(item, 0))
                .setText(R.id.item_view_7, getScore(item, 1))
                .setText(R.id.item_view_8, getScore(item, 2))
                .setText(R.id.item_view_9, getScore(item, 3))
                .setText(R.id.item_view_10, String.valueOf(total))
                .setText(R.id.item_view_11, String.valueOf(average));

        boolean isSelected = selectedId == item.getVoteid();
        int textColor = isSelected ? getContext().getColor(R.color.white) : getContext().getColor(R.color.black);
        helper.setTextColor(R.id.item_view_1, textColor)
                .setTextColor(R.id.item_view_2, textColor)
                .setTextColor(R.id.item_view_3, textColor)
                .setTextColor(R.id.item_view_4, textColor)
                .setTextColor(R.id.item_view_5, textColor)
                .setTextColor(R.id.item_view_6, textColor)
                .setTextColor(R.id.item_view_7, textColor)
                .setTextColor(R.id.item_view_8, textColor)
                .setTextColor(R.id.item_view_9, textColor)
                .setTextColor(R.id.item_view_10, textColor)
                .setTextColor(R.id.item_view_11, textColor)
        ;

        int backgroundColor = isSelected ? getContext().getColor(R.color.blue) : getContext().getColor(R.color.white);
        helper.setBackgroundColor(R.id.item_view_1, backgroundColor)
                .setBackgroundColor(R.id.item_view_2, backgroundColor)
                .setBackgroundColor(R.id.item_view_3, backgroundColor)
                .setBackgroundColor(R.id.item_view_4, backgroundColor)
                .setBackgroundColor(R.id.item_view_5, backgroundColor)
                .setBackgroundColor(R.id.item_view_6, backgroundColor)
                .setBackgroundColor(R.id.item_view_7, backgroundColor)
                .setBackgroundColor(R.id.item_view_8, backgroundColor)
                .setBackgroundColor(R.id.item_view_9, backgroundColor)
                .setBackgroundColor(R.id.item_view_10, backgroundColor)
                .setBackgroundColor(R.id.item_view_11, backgroundColor);
    }

    private String getScore(InterfaceFilescorevote.pbui_Type_Item_UserDefineFileScore item, int index) {
        int selectcount = item.getSelectcount();
        if (selectcount < (index + 1)) {//要获取的文本大于有效选项个数
            return "";
        }
        return item.getVoteText(index).toStringUtf8();
    }

    private double getTotal(InterfaceFilescorevote.pbui_Type_Item_UserDefineFileScore item) {
        double total = 0;
        List<Integer> itemsumscoreList = item.getItemsumscoreList();
        for (int i : itemsumscoreList) {
            total += i;
        }
        return total;
    }
}
