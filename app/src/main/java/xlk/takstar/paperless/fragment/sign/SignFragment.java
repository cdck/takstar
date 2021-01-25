package xlk.takstar.paperless.fragment.sign;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mogujie.tt.protobuf.InterfaceRoom;

import java.util.List;

import xlk.takstar.paperless.R;
import xlk.takstar.paperless.base.BaseFragment;
import xlk.takstar.paperless.ui.CustomAbsoluteLayout;
import xlk.takstar.paperless.util.LogUtil;

/**
 * @author Created by xlk on 2020/12/2.
 * @desc
 */
public class SignFragment extends BaseFragment<SignPresenter> implements SignContract.View {
    private CustomAbsoluteLayout seat_absolute;
    private LinearLayout seat_root_ll;
    private TextView tv_yd;
    private TextView tv_yqd;
    private TextView tv_wqd;
    private int width, height;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sign;
    }

    @Override
    protected void initView(View inflate) {
        seat_absolute = inflate.findViewById(R.id.seat_absolute);
        seat_root_ll = inflate.findViewById(R.id.seat_root_ll);
        tv_yd = inflate.findViewById(R.id.tv_yd);
        tv_yqd = inflate.findViewById(R.id.tv_yqd);
        tv_wqd = inflate.findViewById(R.id.tv_wqd);
    }

    @Override
    protected SignPresenter initPresenter() {
        return new SignPresenter(this);
    }

    @Override
    protected void onShow() {
        presenter.queryInterFaceConfiguration();
    }

    @Override
    protected void initial() {
        seat_root_ll.post(() -> {
            seat_absolute.setScreen(seat_root_ll.getWidth(), seat_root_ll.getHeight());
            presenter.queryInterFaceConfiguration();
        });
    }

    @Override
    public void updateView(List<InterfaceRoom.pbui_Item_MeetRoomDevSeatDetailInfo> itemList, boolean isShow,int allMemberCount, int checkedMemberCount) {
        if (getActivity() == null) {
            return;
        }
        getActivity().runOnUiThread(() -> {
            LogUtil.e(TAG, "updateView  -->");
            tv_yqd.setText(getString(R.string.yqd_, String.valueOf(checkedMemberCount)));
            tv_yd.setText(getString(R.string.yd_, String.valueOf(allMemberCount)));
            tv_wqd.setText(getString(R.string.wqd_, String.valueOf(allMemberCount - checkedMemberCount)));
            seat_absolute.removeAllViews();
            for (InterfaceRoom.pbui_Item_MeetRoomDevSeatDetailInfo info : itemList) {
                LogUtil.d(TAG, "updateView -->左上角坐标：（" + info.getX() + "," + info.getY() + "）, 设备= " + info.getDevname().toStringUtf8());
                addSeat(info, isShow);
            }
        });
    }

    private void addSeat(InterfaceRoom.pbui_Item_MeetRoomDevSeatDetailInfo item, boolean isShow) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.item_seat, null);
//        RelativeLayout.LayoutParams ivParams = new RelativeLayout.LayoutParams(
//                30, 30);
////                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        RelativeLayout.LayoutParams seatLinearParams = new RelativeLayout.LayoutParams(120, 40);
        ImageView item_seat_iv = inflate.findViewById(R.id.item_seat_iv);
        ImageView item_seat_close = inflate.findViewById(R.id.item_seat_close);
        LinearLayout item_seat_ll = inflate.findViewById(R.id.item_seat_ll);
        TextView item_seat_device = inflate.findViewById(R.id.item_seat_device);
        TextView item_seat_member = inflate.findViewById(R.id.item_seat_member);
        boolean isChecked = item.getIssignin() == 1;
        item_seat_iv.setSelected(isChecked);
        item_seat_close.setSelected(isChecked);

//        switch (item.getDirection()) {
//            //上
//            case 0:
//                item_seat_iv.setImageResource(R.drawable.icon_seat_bottom);
//                ivParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//                ivParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//                seatLinearParams.addRule(RelativeLayout.BELOW, item_seat_iv.getId());
//                seatLinearParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//                break;
//            //下
//            case 1:
//                item_seat_iv.setImageResource(R.drawable.icon_seat_top);
//                seatLinearParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//                seatLinearParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//                ivParams.addRule(RelativeLayout.BELOW, item_seat_ll.getId());
//                ivParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//                break;
//            //左
//            case 2:
//                item_seat_iv.setImageResource(R.drawable.icon_seat_right);
//                ivParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//                ivParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//                seatLinearParams.addRule(RelativeLayout.BELOW, item_seat_iv.getId());
//                seatLinearParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//                break;
//            //右
//            case 3:
//                item_seat_iv.setImageResource(R.drawable.icon_seat_left);
//                ivParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//                ivParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//                seatLinearParams.addRule(RelativeLayout.BELOW, item_seat_iv.getId());
//                seatLinearParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//                break;
//            default:
//                break;
//        }
//        item_seat_iv.setVisibility(isShow ? View.VISIBLE : View.GONE);

        String devName = item.getDevname().toStringUtf8();
        if (!TextUtils.isEmpty(devName)) {
            item_seat_device.setText(devName);
        } else {
            item_seat_device.setVisibility(View.GONE);
        }

        String memberName = item.getMembername().toStringUtf8();
        if (!TextUtils.isEmpty(memberName)) {
            item_seat_member.setText(memberName);
//            item_seat_member.setTextColor(isChecked
//                    ? App.appContext.getColor(R.color.signed_text_color)
//                    : App.appContext.getColor(R.color.unsigned_text_color)
//            );
        } else {
            item_seat_member.setVisibility(View.GONE);
        }

//        item_seat_iv.setLayoutParams(ivParams);
//        item_seat_ll.setLayoutParams(seatLinearParams);
        //左上角x坐标
        float x1 = item.getX();
        //左上角y坐标
        float y1 = item.getY();
        if (x1 > 1) {
            x1 = 1;
        } else if (x1 < 0) {
            x1 = 0;
        }
        if (y1 > 1) {
            y1 = 1;
        } else if (y1 < 0) {
            y1 = 0;
        }
        int x = (int) (x1 * width);
        int y = (int) (y1 * height);

        AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,
                120, 70,
                x, y);
        inflate.setLayoutParams(params);
        seat_absolute.addView(inflate);
    }

    @Override
    public void updateSignin(int yqd, int yd) {
//        if (getActivity() == null) {
//            return;
//        }
//        getActivity().runOnUiThread(() -> {
//            tv_yqd.setText(getString(R.string.yqd_, String.valueOf(yqd)));
//            tv_yd.setText(getString(R.string.yd_, String.valueOf(yd)));
//            tv_wqd.setText(getString(R.string.wqd_, String.valueOf(yd - yqd)));
//        });
    }

    @Override
    public void updateBg(String filepath) {
        if (getActivity() == null) {
            return;
        }
        getActivity().runOnUiThread(() -> {
            Drawable drawable = Drawable.createFromPath(filepath);
            seat_absolute.setBackground(drawable);
            Bitmap bitmap = BitmapFactory.decodeFile(filepath);
            if (bitmap != null) {
                width = bitmap.getWidth();
                height = bitmap.getHeight();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
                seat_absolute.setLayoutParams(params);
                LogUtil.e(TAG, "updateBg 图片宽高 -->" + width + ", " + height);
                presenter.placeDeviceRankingInfo();
                bitmap.recycle();
            }
        });
    }
}
