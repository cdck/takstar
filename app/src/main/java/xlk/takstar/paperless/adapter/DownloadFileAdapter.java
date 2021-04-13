package xlk.takstar.paperless.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.mogujie.tt.protobuf.InterfaceFile;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import xlk.takstar.paperless.R;

/**
 * @author Created by xlk on 2020/12/1.
 * @desc
 */
public class DownloadFileAdapter extends BaseQuickAdapter<InterfaceFile.pbui_Item_MeetDirFileDetailInfo, BaseViewHolder> {
    List<Integer> checks = new ArrayList<>();

    public DownloadFileAdapter(int layoutResId, @Nullable List<InterfaceFile.pbui_Item_MeetDirFileDetailInfo> data) {
        super(layoutResId, data);
    }

    public void clearSelected() {
        checks.clear();
        notifyDataSetChanged();
    }

    public void choose(int mediaId) {
        if (checks.contains(mediaId)) {
            checks.remove(checks.indexOf(mediaId));
        } else {
            checks.add(mediaId);
        }
        notifyDataSetChanged();
    }

    public List<InterfaceFile.pbui_Item_MeetDirFileDetailInfo> getSelected() {
        List<InterfaceFile.pbui_Item_MeetDirFileDetailInfo> temps = new ArrayList<>();
        for (int i = 0; i < getData().size(); i++) {
            if (checks.contains(getData().get(i).getMediaid())) {
                temps.add(getData().get(i));
            }
        }
        return temps;
    }

    @Override
    protected void convert(BaseViewHolder helper, InterfaceFile.pbui_Item_MeetDirFileDetailInfo item) {
        helper.setText(R.id.item_view_1, String.valueOf(helper.getLayoutPosition() + 1))
                .setText(R.id.item_view_2, item.getName().toStringUtf8())
                .setText(R.id.item_view_3, String.valueOf(item.getMediaid()));
        boolean isSelected = checks.contains(item.getMediaid());
        int textColor = isSelected ? getContext().getColor(R.color.white) : getContext().getColor(R.color.normal_text_color);
        helper.setTextColor(R.id.item_view_1, textColor)
                .setTextColor(R.id.item_view_2, textColor)
                .setTextColor(R.id.item_view_3, textColor);

        int backgroundColor = isSelected ? getContext().getColor(R.color.blue) : getContext().getColor(R.color.white);
        helper.setBackgroundColor(R.id.item_view_1, backgroundColor)
                .setBackgroundColor(R.id.item_view_2, backgroundColor)
                .setBackgroundColor(R.id.item_view_3, backgroundColor);
    }

}
