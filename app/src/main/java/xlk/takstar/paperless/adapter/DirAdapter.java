package xlk.takstar.paperless.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mogujie.tt.protobuf.InterfaceFile;

import java.util.List;

import androidx.annotation.Nullable;
import xlk.takstar.paperless.R;

/**
 * @author Created by xlk on 2020/12/1.
 * @desc 会议目录
 */
public class DirAdapter extends BaseQuickAdapter<InterfaceFile.pbui_Item_MeetDirDetailInfo, BaseViewHolder> {
    private int chooseId = -1;
    public DirAdapter(int layoutResId, @Nullable List<InterfaceFile.pbui_Item_MeetDirDetailInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, InterfaceFile.pbui_Item_MeetDirDetailInfo item) {
        helper.setText(R.id.item_meet_data_dir_name, item.getName().toStringUtf8());
        boolean selected = item.getId() == chooseId;
        helper.getView(R.id.item_meet_data_dir_iv).setSelected(selected);
        helper.getView(R.id.item_meet_data_dir_ll).setSelected(selected);
        TextView view = helper.getView(R.id.item_meet_data_dir_name);
        view.setTextColor(selected ? mContext.getResources().getColor(R.color.white) : mContext.getResources().getColor(R.color.normal_text_color));
    }
    public int getChooseId() {
        return chooseId;
    }

    public void setChoose(int dirId) {
        chooseId = dirId;
        notifyDataSetChanged();
    }
}
