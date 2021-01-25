package xlk.takstar.paperless.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.mogujie.tt.protobuf.InterfaceBullet;

import java.util.List;

import androidx.annotation.Nullable;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.util.DateUtil;

/**
 * @author Created by xlk on 2020/12/11.
 * @desc
 */
public class BulletAdapter extends BaseQuickAdapter<InterfaceBullet.pbui_Item_BulletDetailInfo, BaseViewHolder> {
    int selectedId;

    public BulletAdapter(int layoutResId, @Nullable List<InterfaceBullet.pbui_Item_BulletDetailInfo> data) {
        super(layoutResId, data);
    }

    public void setSelectedId(int id) {
        selectedId = id;
        notifyDataSetChanged();
    }

    public InterfaceBullet.pbui_Item_BulletDetailInfo getSelectedBullet() {
        for (int i = 0; i < getData().size(); i++) {
            if (selectedId == getData().get(i).getBulletid()) {
                return getData().get(i);
            }
        }
        return null;
    }

    @Override
    protected void convert(BaseViewHolder helper, InterfaceBullet.pbui_Item_BulletDetailInfo item) {
        helper.setText(R.id.tv_title, item.getTitle().toStringUtf8())
                .setText(R.id.tv_content, item.getContent().toStringUtf8());
//                .setText(R.id.tv_time, item.getTimeouts() == 0 ? "" : DateUtil.convertTime(item.getTimeouts() * 1000));
        View bullet_root = helper.getView(R.id.bullet_root);
        bullet_root.setSelected(selectedId == item.getBulletid());
    }
}
