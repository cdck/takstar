package xlk.takstar.paperless.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mogujie.tt.protobuf.InterfaceIM;


import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.model.bean.ChatMessageA;
import xlk.takstar.paperless.model.bean.MyChatMessage;
import xlk.takstar.paperless.util.DateUtil;

/**
 * Created by Administrator on 2017/11/13.
 * 会议聊天 消息列表
 */

public class MulitpleItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private List<MyChatMessage> data;


    public MulitpleItemAdapter(Context context, List<MyChatMessage> data) {
        this.data = data;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new LeftViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_chat_left, parent, false));
        } else {
            return new RightViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_chat_right, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //根据
        MyChatMessage message = data.get(position);
        if (holder instanceof LeftViewHolder) {
            String msg = message.getContent();
            long utcsecond = message.getUtcSecond();
            String time = DateUtil.getHHss(utcsecond);
            String name = message.getMemberName();
            String title = mContext.getResources().getString(R.string.time, name, time);
            ((LeftViewHolder) holder).i_m_c_l_message_title.setText(title);
            ((LeftViewHolder) holder).i_m_c_l_message_content.setText(msg);
        } else {
            long utcsecond = message.getUtcSecond();
            String time = DateUtil.getHHss(utcsecond);
            String title = mContext.getResources().getString(R.string.me_time, time);
            ((RightViewHolder) holder).i_m_c_r_message_title.setText(title);
            ((RightViewHolder) holder).i_m_c_r_message_content.setText(message.getContent());
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        MyChatMessage message = data.get(position);
        //=0为接收的消息，=1为发送的消息
        int type = message.getType();
        return type;
    }

    //接收 --->>> 其他人发送的消息
    class LeftViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView i_m_c_l_message_title;
        TextView i_m_c_l_message_content;

        public LeftViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            i_m_c_l_message_title = itemView.findViewById(R.id.i_m_c_l_message_title);
            i_m_c_l_message_content = itemView.findViewById(R.id.i_m_c_l_message_content);
        }
    }

    //发送 --->>> 第一人称发送的消息
    class RightViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView i_m_c_r_message_title;
        TextView i_m_c_r_message_content;

        public RightViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            i_m_c_r_message_title = itemView.findViewById(R.id.i_m_c_r_message_title);
            i_m_c_r_message_content = itemView.findViewById(R.id.i_m_c_r_message_content);
        }
    }
}
