package xlk.takstar.paperless.adapter;

import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.mogujie.tt.protobuf.InterfaceMember;

import java.util.List;

import androidx.annotation.Nullable;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.util.LogUtil;

/**
 * @author Created by xlk on 2020/11/30.
 * @desc 主界面绑定参会人
 */
public class MemberAdapter extends BaseQuickAdapter<InterfaceMember.pbui_Item_MemberDetailInfo, BaseViewHolder> {
    private final String TAG = "MemberAdapter-->";
    private int selectedId;

    public MemberAdapter(int layoutResId, @Nullable List<InterfaceMember.pbui_Item_MemberDetailInfo> data) {
        super(layoutResId, data);
    }

    public void setSelectedId(int id) {
        selectedId = id;
        LogUtil.i(TAG, "setSelectedId =" + selectedId);
        notifyDataSetChanged();
    }
    public int getSelectedMemberId(){
        for (int i = 0; i < getData().size(); i++) {
            if(selectedId==getData().get(i).getPersonid()){
                return selectedId;
            }
        }
        return 0;
    }

    @Override
    protected void convert(BaseViewHolder helper, InterfaceMember.pbui_Item_MemberDetailInfo item) {
        helper.setText(R.id.item_single_btn, item.getName().toStringUtf8());
        Button item_btn = helper.getView(R.id.item_single_btn);
        item_btn.setSelected(selectedId == item.getPersonid());
    }
}
