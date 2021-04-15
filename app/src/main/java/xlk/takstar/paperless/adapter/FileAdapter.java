package xlk.takstar.paperless.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.blankj.utilcode.util.FileUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.mogujie.tt.protobuf.InterfaceFile;

import java.util.List;

import androidx.annotation.Nullable;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.util.FileUtil;

/**
 * @author Created by xlk on 2020/12/1.
 * @desc
 */
public class FileAdapter extends BaseQuickAdapter<InterfaceFile.pbui_Item_MeetDirFileDetailInfo, BaseViewHolder> {
    public int selectedId = -1;

    public FileAdapter(int layoutResId, @Nullable List<InterfaceFile.pbui_Item_MeetDirFileDetailInfo> data) {
        super(layoutResId, data);
    }

    public void setSelectedId(int id) {
        selectedId = id;
        notifyDataSetChanged();
    }

    public int getSelectedId() {
        for (int i = 0; i < getData().size(); i++) {
            if (getData().get(i).getMediaid() == selectedId) {
                return selectedId;
            }
        }
        return -1;
    }

    @Override
    protected void convert(BaseViewHolder helper, InterfaceFile.pbui_Item_MeetDirFileDetailInfo item) {
        boolean isSelected = selectedId == item.getMediaid();
        View root_view = helper.getView(R.id.root_view);
        root_view.setSelected(isSelected);
        String fileName = item.getName().toStringUtf8();
        Button item_btn_download = helper.getView(R.id.item_btn_download);
        ImageView item_iv_icon = helper.getView(R.id.item_iv_icon);
        helper.setText(R.id.item_tv_name, fileName);
        if (FileUtil.isDoc(fileName)) {
            item_iv_icon.setImageResource(R.drawable.ic_word);
        } else if (FileUtil.isPPT(fileName)) {
            item_iv_icon.setImageResource(R.drawable.ic_ppt);
        } else if (FileUtil.isXLS(fileName)) {
            item_iv_icon.setImageResource(R.drawable.ic_wps);
        } else if (FileUtil.isPicture(fileName)) {
            item_iv_icon.setImageResource(R.drawable.ic_picture);
        } else if (FileUtil.isVideo(fileName)) {
            item_iv_icon.setImageResource(R.drawable.ic_video);
        } else {
            item_iv_icon.setImageResource(R.drawable.ic_other);
        }
        boolean fileExists = FileUtils.isFileExists(Constant.download_dir + fileName);
        item_btn_download.setEnabled(!fileExists);
    }
}
