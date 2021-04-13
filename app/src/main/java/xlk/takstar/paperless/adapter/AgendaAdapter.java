package xlk.takstar.paperless.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.mogujie.tt.protobuf.InterfaceAgenda;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import xlk.takstar.paperless.R;
import xlk.takstar.paperless.util.DateUtil;

/**
 * @author Created by xlk on 2021/4/13.
 * @desc 时间轴式议程列表
 */
public class AgendaAdapter extends BaseQuickAdapter<InterfaceAgenda.pbui_ItemAgendaTimeInfo, BaseViewHolder> {
    private int selectedId = -1;

    public AgendaAdapter(@Nullable List<InterfaceAgenda.pbui_ItemAgendaTimeInfo> data) {
        super(R.layout.item_time_agenda, data);
    }

    public void choose(int id) {
        selectedId = id;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, InterfaceAgenda.pbui_ItemAgendaTimeInfo item) {
        String time = DateUtil.getHHss(item.getStartutctime()) + " - " + DateUtil.getHHss(item.getEndutctime());
        View view = holder.getView(R.id.item_root_view);
        view.setBackgroundColor(selectedId == item.getAgendaid()
                ? getContext().getColor(R.color.selected_item_bg)
                : getContext().getColor(R.color.transparent));
        holder.setText(R.id.tv_time, time)
                .setText(R.id.tv_content, item.getDesctext().toStringUtf8());
    }
}
