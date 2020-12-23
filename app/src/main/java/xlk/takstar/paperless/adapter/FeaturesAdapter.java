package xlk.takstar.paperless.adapter;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mogujie.tt.protobuf.InterfaceMacro;
import com.mogujie.tt.protobuf.InterfaceMeetfunction;

import java.util.List;

import androidx.annotation.Nullable;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.util.LogUtil;

/**
 * @author Created by xlk on 2020/11/30.
 * @desc 会议功能
 */
public class FeaturesAdapter extends BaseQuickAdapter<InterfaceMeetfunction.pbui_Item_MeetFunConfigDetailInfo, BaseViewHolder> {
    private int selectedId;

    public FeaturesAdapter(int layoutResId, @Nullable List<InterfaceMeetfunction.pbui_Item_MeetFunConfigDetailInfo> data) {
        super(layoutResId, data);
    }

    public void setSelected(int id) {
        selectedId = id;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder helper, InterfaceMeetfunction.pbui_Item_MeetFunConfigDetailInfo item) {
        LinearLayout item_feature_root = helper.getView(R.id.item_feature_root);
        ImageView item_feature_iv = helper.getView(R.id.item_feature_iv);
        TextView item_feature_tv = helper.getView(R.id.item_feature_tv);
        int funcode = item.getFuncode();
        switch (funcode) {
            //会议议程
            case InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_AGENDA_BULLETIN_VALUE: {
                item_feature_iv.setImageResource(R.drawable.feature_agenda_icon);
                item_feature_tv.setText(mContext.getString(R.string.meeting_agenda));
                break;
            }
            //会议资料
            case InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_MATERIAL_VALUE: {
                item_feature_iv.setImageResource(R.drawable.feature_materials_icon);
                item_feature_tv.setText(mContext.getString(R.string.meeting_materials));
                break;
            }
            //批注查看
            case InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_POSTIL_VALUE: {
                item_feature_iv.setImageResource(R.drawable.feature_annotate_icon);
                item_feature_tv.setText(mContext.getString(R.string.meeting_annotate));
                break;
            }
            //互动交流
            case InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_MESSAGE_VALUE: {
                item_feature_iv.setImageResource(R.drawable.feature_chat_icon);
                item_feature_tv.setText(mContext.getString(R.string.meeting_chat));
                break;
            }
            //视频直播
            case InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_VIDEOSTREAM_VALUE: {
                item_feature_iv.setImageResource(R.drawable.feature_video_icon);
                item_feature_tv.setText(mContext.getString(R.string.meeting_video));
                break;
            }
            //电子白板
            case InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_WHITEBOARD_VALUE: {
                item_feature_iv.setImageResource(R.drawable.feature_draw_icon);
                item_feature_tv.setText(mContext.getString(R.string.meeting_draw));
                break;
            }
            //网页浏览
            case InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_WEBBROWSER_VALUE: {
                item_feature_iv.setImageResource(R.drawable.feature_web_icon);
                item_feature_tv.setText(mContext.getString(R.string.meeting_web));
                break;
            }
            //签到信息
            case InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_SIGNINRESULT_VALUE: {
                item_feature_iv.setImageResource(R.drawable.feature_signin_icon);
                item_feature_tv.setText(mContext.getString(R.string.meeting_signin));
                break;
            }
            default:
                break;
        }
        item_feature_root.setSelected(selectedId == item.getFuncode());
        item_feature_iv.setSelected(selectedId == item.getFuncode());
        item_feature_tv.setSelected(selectedId == item.getFuncode());
    }
}
