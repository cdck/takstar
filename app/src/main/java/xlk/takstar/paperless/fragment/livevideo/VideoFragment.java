package xlk.takstar.paperless.fragment.livevideo;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.mogujie.tt.protobuf.InterfaceMacro;
import com.mogujie.tt.protobuf.InterfaceVideo;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.adapter.MeetLiveVideoAdapter;
import xlk.takstar.paperless.adapter.WmProjectorAdapter;
import xlk.takstar.paperless.adapter.WmScreenMemberAdapter;
import xlk.takstar.paperless.adapter.WmScreenProjectorAdapter;
import xlk.takstar.paperless.base.BaseFragment;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.model.GlobalValue;
import xlk.takstar.paperless.model.bean.VideoDev;
import xlk.takstar.paperless.service.fab.CustomBaseViewHolder;
import xlk.takstar.paperless.ui.video.WlOnGlSurfaceViewOncreateListener;
import xlk.takstar.paperless.ui.video.single.PlayView;
import xlk.takstar.paperless.util.LogUtil;
import xlk.takstar.paperless.util.PopUtil;

import static xlk.takstar.paperless.model.Constant.RESOURCE_ID_0;
import static xlk.takstar.paperless.model.Constant.RESOURCE_ID_1;
import static xlk.takstar.paperless.model.Constant.RESOURCE_ID_2;
import static xlk.takstar.paperless.model.Constant.RESOURCE_ID_3;
import static xlk.takstar.paperless.model.Constant.RESOURCE_ID_4;
import static xlk.takstar.paperless.model.Constant.permission_code_projection;
import static xlk.takstar.paperless.model.Constant.permission_code_screen;

/**
 * @author Created by xlk on 2021/7/26.
 * @desc
 */
public class VideoFragment extends BaseFragment<LiveVideoPresenter> implements LiveVideoContract.View, View.OnClickListener {

    private ConstraintLayout constraint_layout;
    private LinearLayout a, b, c, d;
    private PlayView a_view, b_view, c_view, d_view;
    private LinearLayout right_view;
    private int width, height;
    private boolean isFull;
    private LinearLayout ll_screen, ll_pro;
    private RecyclerView f_l_v_rv;
    private CheckBox cb_enable_recording;
    private Button f_l_v_watch, f_l_v_stop, f_l_v_stop_pro, f_l_v_start_pro, f_l_v_stop_screen, f_l_v_start_screen;
    List<Integer> ids = new ArrayList<>();
    private WmScreenMemberAdapter memberAdapter;
    private WmProjectorAdapter projectorAdapter;
    private WmScreenProjectorAdapter wmScreenProjectorAdapter;
    /**
     * 当前选中的资源id
     */
    private int currentResId = -1;
    private MeetLiveVideoAdapter adapter;
    private PopupWindow proPop;
    private PopupWindow screenPop;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_live_video_test;
    }

    @Override
    protected void initView(View inflate) {
        ll_screen = (LinearLayout) inflate.findViewById(R.id.ll_screen);
        ll_pro = (LinearLayout) inflate.findViewById(R.id.ll_pro);
        f_l_v_rv = (RecyclerView) inflate.findViewById(R.id.f_l_v_rv);
        f_l_v_watch = (Button) inflate.findViewById(R.id.f_l_v_watch);
        f_l_v_stop = (Button) inflate.findViewById(R.id.f_l_v_stop);
        f_l_v_watch.setOnClickListener(this);
        f_l_v_stop.setOnClickListener(this);
        cb_enable_recording = (CheckBox) inflate.findViewById(R.id.cb_enable_recording);
        cb_enable_recording.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.operating_tips)
                        .setMessage(R.string.enable_recording_tip)
                        .setPositiveButton(R.string.define, (dialog, which) -> {
                            jni.modifyContextProperties(InterfaceMacro.Pb_ContextPropertyID.Pb_MEETCONTEXT_PROPERTY_STREAMSAVE_VALUE, 1);
                            cb_enable_recording.setChecked(true);
                            dialog.dismiss();
                        })
                        .setNegativeButton(R.string.cancel, (dialog, which) -> {
                            cb_enable_recording.setChecked(false);
                            dialog.dismiss();
                        })
                        .create().show();
            } else {
                jni.modifyContextProperties(InterfaceMacro.Pb_ContextPropertyID.Pb_MEETCONTEXT_PROPERTY_STREAMSAVE_VALUE, 0);
            }
        });
        f_l_v_stop_pro = (Button) inflate.findViewById(R.id.f_l_v_stop_pro);
        f_l_v_stop_pro.setOnClickListener(this);
        f_l_v_start_pro = (Button) inflate.findViewById(R.id.f_l_v_start_pro);
        f_l_v_start_pro.setOnClickListener(this);
        f_l_v_stop_screen = (Button) inflate.findViewById(R.id.f_l_v_stop_screen);
        f_l_v_stop_screen.setOnClickListener(this);
        f_l_v_start_screen = (Button) inflate.findViewById(R.id.f_l_v_start_screen);
        f_l_v_start_screen.setOnClickListener(this);

        right_view = inflate.findViewById(R.id.right_view);
        constraint_layout = inflate.findViewById(R.id.constraint_layout);

        a = inflate.findViewById(R.id.a);
        a_view = inflate.findViewById(R.id.a_view);
        ImageView a_play = inflate.findViewById(R.id.a_play);
        ImageView a_stop = inflate.findViewById(R.id.a_stop);
        ImageView a_pause = inflate.findViewById(R.id.a_pause);
        ImageView a_full = inflate.findViewById(R.id.a_full);
        a_play.setOnClickListener(this);
        a_stop.setOnClickListener(this);
        a_pause.setOnClickListener(this);
        a_full.setOnClickListener(this);

        b = inflate.findViewById(R.id.b);
        b_view = inflate.findViewById(R.id.b_view);
        ImageView b_play = inflate.findViewById(R.id.b_play);
        ImageView b_stop = inflate.findViewById(R.id.b_stop);
        ImageView b_pause = inflate.findViewById(R.id.b_pause);
        ImageView b_full = inflate.findViewById(R.id.b_full);
        b_play.setOnClickListener(this);
        b_stop.setOnClickListener(this);
        b_pause.setOnClickListener(this);
        b_full.setOnClickListener(this);

        c = inflate.findViewById(R.id.c);
        c_view = inflate.findViewById(R.id.c_view);
        ImageView c_play = inflate.findViewById(R.id.c_play);
        ImageView c_stop = inflate.findViewById(R.id.c_stop);
        ImageView c_pause = inflate.findViewById(R.id.c_pause);
        ImageView c_full = inflate.findViewById(R.id.c_full);
        c_play.setOnClickListener(this);
        c_stop.setOnClickListener(this);
        c_pause.setOnClickListener(this);
        c_full.setOnClickListener(this);

        d = inflate.findViewById(R.id.d);
        d_view = inflate.findViewById(R.id.d_view);
        ImageView d_play = inflate.findViewById(R.id.d_play);
        ImageView d_stop = inflate.findViewById(R.id.d_stop);
        ImageView d_pause = inflate.findViewById(R.id.d_pause);
        ImageView d_full = inflate.findViewById(R.id.d_full);
        d_play.setOnClickListener(this);
        d_stop.setOnClickListener(this);
        d_pause.setOnClickListener(this);
        d_full.setOnClickListener(this);
        a.setOnClickListener(v -> setSelected(0));
        b.setOnClickListener(v -> setSelected(1));
        c.setOnClickListener(v -> setSelected(2));
        d.setOnClickListener(v -> setSelected(3));
    }

    @Override
    protected LiveVideoPresenter initPresenter() {
        return new LiveVideoPresenter(this);
    }

    @Override
    protected void initial() {
        ids.add(RESOURCE_ID_1);
        ids.add(RESOURCE_ID_2);
        ids.add(RESOURCE_ID_3);
        ids.add(RESOURCE_ID_4);
        initPlayView();
        memberAdapter = new WmScreenMemberAdapter(R.layout.item_wm_screen, presenter.onLineMember);
        projectorAdapter = new WmProjectorAdapter(R.layout.item_wm_pro, presenter.onLineProjectors);
        wmScreenProjectorAdapter = new WmScreenProjectorAdapter(presenter.onLineProjectors);
        right_view.post(() -> {
            width = right_view.getWidth();
            height = right_view.getHeight();
            onShow();
        });
    }

    private void initPlayView() {
        a_view.setResId(RESOURCE_ID_1);
        a_view.setOnGlSurfaceViewOncreateListener(new WlOnGlSurfaceViewOncreateListener() {
            @Override
            public void onGlSurfaceViewOncreate(Surface surface) {
                LogUtil.e(TAG, "createPlayView onGlSurfaceViewOncreate :   --> ");
                a_view.setSurface(surface);
            }

            @Override
            public void onCutVideoImg(Bitmap bitmap) {
                LogUtil.e(TAG, "createPlayView onCutVideoImg :   --> ");
            }
        });
        b_view.setResId(RESOURCE_ID_2);
        b_view.setOnGlSurfaceViewOncreateListener(new WlOnGlSurfaceViewOncreateListener() {
            @Override
            public void onGlSurfaceViewOncreate(Surface surface) {
                LogUtil.e(TAG, "createPlayView onGlSurfaceViewOncreate :   --> ");
                b_view.setSurface(surface);
            }

            @Override
            public void onCutVideoImg(Bitmap bitmap) {
                LogUtil.e(TAG, "createPlayView onCutVideoImg :   --> ");
            }
        });
        c_view.setResId(RESOURCE_ID_3);
        c_view.setOnGlSurfaceViewOncreateListener(new WlOnGlSurfaceViewOncreateListener() {
            @Override
            public void onGlSurfaceViewOncreate(Surface surface) {
                LogUtil.e(TAG, "createPlayView onGlSurfaceViewOncreate :   --> ");
                c_view.setSurface(surface);
            }

            @Override
            public void onCutVideoImg(Bitmap bitmap) {
                LogUtil.e(TAG, "createPlayView onCutVideoImg :   --> ");
            }
        });
        d_view.setResId(RESOURCE_ID_4);
        d_view.setOnGlSurfaceViewOncreateListener(new WlOnGlSurfaceViewOncreateListener() {
            @Override
            public void onGlSurfaceViewOncreate(Surface surface) {
                LogUtil.e(TAG, "createPlayView onGlSurfaceViewOncreate :   --> ");
                d_view.setSurface(surface);
            }

            @Override
            public void onCutVideoImg(Bitmap bitmap) {
                LogUtil.e(TAG, "createPlayView onCutVideoImg :   --> ");
            }
        });
    }

    @Override
    protected void onShow() {
        boolean isManage = getArguments().getBoolean("isManage");
        ll_pro.setVisibility(isManage ? View.VISIBLE : View.GONE);
        ll_screen.setVisibility(isManage ? View.VISIBLE : View.GONE);
        presenter.register();
        presenter.initVideoRes(width, height);
        presenter.queryDeviceInfo();
    }

    @Override
    protected void onHide() {
        for (Integer id : ids) {
            presenter.stopResource(id);
        }
        presenter.releaseVideoRes();
        presenter.unregister();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onHide();
        presenter.onDestroy();
    }

    private void setSelected(int index) {
        if (currentResId == index + 1) {
            currentResId = -1;
        } else {
            currentResId = index + 1;
        }
        LogUtils.e("setSelected currentResId=" + currentResId);
        a.setSelected(currentResId == RESOURCE_ID_1);
        b.setSelected(currentResId == RESOURCE_ID_2);
        c.setSelected(currentResId == RESOURCE_ID_3);
        d.setSelected(currentResId == RESOURCE_ID_4);
    }

    private PlayView getViewByResId(int resId) {
        switch (resId) {
            case Constant.RESOURCE_ID_1:
                return a_view;
            case Constant.RESOURCE_ID_2:
                return b_view;
            case Constant.RESOURCE_ID_3:
                return c_view;
            case Constant.RESOURCE_ID_4:
                return d_view;
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.f_l_v_watch: {
                if (adapter != null) {
                    VideoDev videoDev = adapter.getSelected();
                    if (videoDev != null) {
                        if (currentResId != -1) {
                            presenter.stopResource(currentResId);
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    presenter.watch(videoDev, currentResId);
                                }
                            }, 500);
                        } else {
                            ToastUtils.showShort(R.string.please_choose_view);
                        }
                    } else {
                        ToastUtils.showShort(R.string.please_choose_video_show);
                    }
                }
                break;
            }
            case R.id.f_l_v_stop: {
                if (currentResId != -1) {
                    presenter.stopResource(currentResId);
                } else {
                    ToastUtils.showShort(R.string.please_choose_stop_view);
                }
                break;
            }
            case R.id.f_l_v_stop_pro: {
                if (adapter != null && adapter.getSelected() != null) {
                    if (Constant.hasPermission(permission_code_projection)) {
                        showProPop(false, adapter.getSelected());
                    } else {
                        ToastUtils.showShort(R.string.err_NoPermission);
                    }
                }
                break;
            }
            case R.id.f_l_v_start_pro: {
                if (adapter != null && adapter.getSelected() != null) {
                    if (Constant.hasPermission(permission_code_projection)) {
                        showProPop(true, adapter.getSelected());
                    } else {
                        ToastUtils.showShort(R.string.err_NoPermission);
                    }
                }
                break;
            }
            case R.id.f_l_v_stop_screen: {
                if (adapter != null && adapter.getSelected() != null) {
                    if (Constant.hasPermission(permission_code_screen)) {
                        showScreenPop(false, adapter.getSelected());
                    } else {
                        ToastUtils.showShort(R.string.err_NoPermission);
                    }
                }
                break;
            }
            case R.id.f_l_v_start_screen: {
                if (adapter != null && adapter.getSelected() != null) {
                    if (Constant.hasPermission(permission_code_screen)) {
                        showScreenPop(true, adapter.getSelected());
                    } else {
                        ToastUtils.showShort(R.string.err_NoPermission);
                    }
                }
                break;
            }
            case R.id.a_play: {
                if (adapter != null) {
                    VideoDev videoDev = adapter.getSelected();
                    if (videoDev != null) {
                        presenter.stopResource(RESOURCE_ID_1);
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                presenter.watch(videoDev, RESOURCE_ID_1);
                            }
                        }, 500);
                    } else {
                        ToastUtils.showShort(R.string.please_choose_video_show);
                    }
                }
//                jni.setPlayRecover(RESOURCE_ID_1, GlobalValue.localDeviceId);
                break;
            }
            case R.id.a_stop: {
                presenter.stopResource(RESOURCE_ID_1);
                break;
            }
            case R.id.a_pause: {
                jni.setPlayPause(RESOURCE_ID_1, GlobalValue.localDeviceId);
                break;
            }
            case R.id.a_full: {
                fullA();
                break;
            }
            case R.id.b_play: {
                if (adapter != null) {
                    VideoDev videoDev = adapter.getSelected();
                    if (videoDev != null) {
                        presenter.stopResource(RESOURCE_ID_2);
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                presenter.watch(videoDev, RESOURCE_ID_2);
                            }
                        }, 500);
                    } else {
                        ToastUtils.showShort(R.string.please_choose_video_show);
                    }
                }
//                jni.setPlayRecover(RESOURCE_ID_2, GlobalValue.localDeviceId);
                break;
            }
            case R.id.b_stop: {
                presenter.stopResource(RESOURCE_ID_2);
                break;
            }
            case R.id.b_pause: {
                jni.setPlayPause(RESOURCE_ID_2, GlobalValue.localDeviceId);
                break;
            }
            case R.id.b_full: {
                fullB();
                break;
            }
            case R.id.c_play: {
                if (adapter != null) {
                    VideoDev videoDev = adapter.getSelected();
                    if (videoDev != null) {
                        presenter.stopResource(RESOURCE_ID_3);
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                presenter.watch(videoDev, RESOURCE_ID_3);
                            }
                        }, 500);
                    } else {
                        ToastUtils.showShort(R.string.please_choose_video_show);
                    }
                }
//                jni.setPlayRecover(RESOURCE_ID_3, GlobalValue.localDeviceId);
                break;
            }
            case R.id.c_stop: {
                presenter.stopResource(RESOURCE_ID_3);
                break;
            }
            case R.id.c_pause: {
                jni.setPlayPause(RESOURCE_ID_3, GlobalValue.localDeviceId);
                break;
            }
            case R.id.c_full: {
                fullC();
                break;
            }
            case R.id.d_play: {
                if (adapter != null) {
                    VideoDev videoDev = adapter.getSelected();
                    if (videoDev != null) {
                        presenter.stopResource(RESOURCE_ID_4);
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                presenter.watch(videoDev, RESOURCE_ID_4);
                            }
                        }, 500);
                    } else {
                        ToastUtils.showShort(R.string.please_choose_video_show);
                    }
                }
//                jni.setPlayRecover(RESOURCE_ID_4, GlobalValue.localDeviceId);
                break;
            }
            case R.id.d_stop: {
                presenter.stopResource(RESOURCE_ID_4);
                break;
            }
            case R.id.d_pause: {
                jni.setPlayPause(RESOURCE_ID_4, GlobalValue.localDeviceId);
                break;
            }
            case R.id.d_full: {
                fullD();
                break;
            }
        }
    }

    private void showProPop(boolean isStart, VideoDev videoDev) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.wm_pro_view, null);
        proPop = PopUtil.createHalfPop(inflate, f_l_v_stop_pro);
        CustomBaseViewHolder.ProViewHolder holder = new CustomBaseViewHolder.ProViewHolder(inflate);
        proHolderEvent(holder, isStart, videoDev);
    }

    private void proHolderEvent(CustomBaseViewHolder.ProViewHolder holder, boolean isStart, VideoDev videoDev) {
        holder.mandatory_ll.setVisibility(isStart ? View.VISIBLE : View.GONE);
        holder.output_type_ll.setVisibility(isStart ? View.VISIBLE : View.GONE);
        holder.iv_dividing_line.setVisibility(isStart ? View.VISIBLE : View.GONE);
        holder.dividing_line.setVisibility(isStart ? View.GONE : View.VISIBLE);
        holder.wm_pro_title.setText(isStart ? getContext().getString(R.string.launch_pro) : getContext().getString(R.string.stop_pro));
        holder.wm_pro_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        holder.wm_pro_rv.setAdapter(projectorAdapter);
        projectorAdapter.setOnItemClickListener((adapter, view, position) -> {
            projectorAdapter.choose(presenter.onLineProjectors.get(position).getDevcieid());
            holder.wm_pro_all.setChecked(projectorAdapter.isChooseAll());
        });
        holder.wm_pro_all.setOnClickListener(v -> {
            boolean checked = holder.wm_pro_all.isChecked();
            holder.wm_pro_all.setChecked(checked);
            projectorAdapter.setChooseAll(checked);
        });
        holder.wm_pro_full.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                holder.wm_pro_flow1.setChecked(false);
                holder.wm_pro_flow2.setChecked(false);
                holder.wm_pro_flow3.setChecked(false);
                holder.wm_pro_flow4.setChecked(false);
            }
        });
        CompoundButton.OnCheckedChangeListener lll = (buttonView, isChecked) -> {
            if (isChecked) {
                holder.wm_pro_full.setChecked(false);
            }
        };
        holder.wm_pro_flow1.setOnCheckedChangeListener(lll);
        holder.wm_pro_flow2.setOnCheckedChangeListener(lll);
        holder.wm_pro_flow3.setOnCheckedChangeListener(lll);
        holder.wm_pro_flow4.setOnCheckedChangeListener(lll);
        holder.btn_cancel.setOnClickListener(v -> proPop.dismiss());
        holder.iv_close.setOnClickListener(v -> proPop.dismiss());
        holder.btn_ensure.setOnClickListener(v -> {
            List<Integer> ids = projectorAdapter.getChooseIds();
            if (ids.isEmpty()) {
                ToastUtils.showShort(getContext().getString(R.string.please_choose_projector_first));
                return;
            }
            boolean checked = holder.wm_pro_full.isChecked();
            List<Integer> res = new ArrayList<>();
            if (checked) {
                res.add(RESOURCE_ID_0);
            } else {
                if (holder.wm_pro_flow1.isChecked()) res.add(RESOURCE_ID_1);
                if (holder.wm_pro_flow2.isChecked()) res.add(RESOURCE_ID_2);
                if (holder.wm_pro_flow3.isChecked()) res.add(RESOURCE_ID_3);
                if (holder.wm_pro_flow4.isChecked()) res.add(RESOURCE_ID_4);
            }
            if (res.isEmpty()) {
                ToastUtils.showShort(R.string.please_choose_res_first);
                return;
            }
            int deviceid = videoDev.getVideoDetailInfo().getDeviceid();
            int subid = videoDev.getVideoDetailInfo().getSubid();
            if (isStart) {//发起投影
                boolean isMandatory = holder.wm_pro_mandatory.isChecked();
                int triggeruserval = isMandatory ? InterfaceMacro.Pb_TriggerUsedef.Pb_EXCEC_USERDEF_FLAG_NOCREATEWINOPER_VALUE
                        : InterfaceMacro.Pb_TriggerUsedef.Pb_EXCEC_USERDEF_FLAG_ZERO_VALUE;
                jni.streamPlay(deviceid, subid, triggeruserval, res, ids);
            } else {//结束投影
                jni.stopResourceOperate(res, ids);
            }
            proPop.dismiss();
        });
        holder.wm_pro_all.performClick();
    }

    private void showScreenPop(boolean isStart, VideoDev videoDev) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.wm_screen_view, null);
        screenPop = PopUtil.createHalfPop(inflate, f_l_v_stop_pro);
        CustomBaseViewHolder.ScreenViewHolder holder = new CustomBaseViewHolder.ScreenViewHolder(inflate);
        holderEvent(holder, isStart, videoDev);
    }

    private void holderEvent(CustomBaseViewHolder.ScreenViewHolder holder, boolean isStart, VideoDev videoDev) {
        if (isStart) {
            holder.mandatory_ll.setVisibility(View.VISIBLE);
            holder.iv_dividing_line.setVisibility(View.VISIBLE);
            holder.dividing_line.setVisibility(View.GONE);
            holder.wm_screen_title.setText(getString(R.string.launch_screen));
        } else {
            holder.mandatory_ll.setVisibility(View.GONE);
            holder.iv_dividing_line.setVisibility(View.GONE);
            holder.dividing_line.setVisibility(View.VISIBLE);
            holder.wm_screen_title.setText(getString(R.string.stop_screen));
        }
        holder.wm_screen_rv_attendee.setLayoutManager(new LinearLayoutManager(getContext()));
        holder.wm_screen_rv_attendee.setAdapter(memberAdapter);
        memberAdapter.setOnItemClickListener((adapter, view, position) -> {
            memberAdapter.choose(presenter.onLineMember.get(position).getDeviceDetailInfo().getDevcieid());
            holder.wm_screen_cb_attendee.setChecked(memberAdapter.isChooseAll());
        });
        holder.wm_screen_cb_attendee.setOnClickListener(v -> {
            boolean checked = holder.wm_screen_cb_attendee.isChecked();
            holder.wm_screen_cb_attendee.setChecked(checked);
            memberAdapter.setChooseAll(checked);
        });
        holder.wm_screen_rv_projector.setLayoutManager(new LinearLayoutManager(getContext()));
        holder.wm_screen_rv_projector.setAdapter(wmScreenProjectorAdapter);
        wmScreenProjectorAdapter.setOnItemClickListener((adapter, view, position) -> {
            wmScreenProjectorAdapter.choose(presenter.onLineProjectors.get(position).getDevcieid());
            holder.wm_screen_cb_projector.setChecked(wmScreenProjectorAdapter.isChooseAll());
        });
        holder.wm_screen_cb_projector.setOnClickListener(v -> {
            boolean checked = holder.wm_screen_cb_projector.isChecked();
            holder.wm_screen_cb_projector.setChecked(checked);
            wmScreenProjectorAdapter.setChooseAll(checked);
        });
        holder.btn_cancel.setOnClickListener(v -> screenPop.dismiss());
        holder.iv_close.setOnClickListener(v -> screenPop.dismiss());
        //发起/结束同屏
        holder.btn_ensure.setOnClickListener(v -> {
            List<Integer> ids = memberAdapter.getSelectedDeviceIds();
            ids.addAll(wmScreenProjectorAdapter.getChooseIds());
            if (ids.isEmpty()) {
                ToastUtils.showShort(R.string.err_target_NotNull);
            } else {
                List<Integer> temps = new ArrayList<>();
                temps.add(RESOURCE_ID_0);
                int deviceid = videoDev.getVideoDetailInfo().getDeviceid();
                int subid = videoDev.getVideoDetailInfo().getSubid();
                if (isStart) {//发起同屏
                    int triggeruserval = 0;
                    if (holder.wm_screen_mandatory.isChecked()) {//是否强制同屏
                        triggeruserval = InterfaceMacro.Pb_TriggerUsedef.Pb_EXCEC_USERDEF_FLAG_NOCREATEWINOPER_VALUE;
                    }
                    jni.streamPlay(deviceid, subid, triggeruserval, temps, ids);
                } else {//结束同屏
                    jni.stopResourceOperate(temps, ids);
                }
                screenPop.dismiss();
            }
        });
        //默认全部选中
        holder.wm_screen_cb_attendee.performClick();
        holder.wm_screen_cb_projector.performClick();
    }

    private void fullD() {
        ConstraintSet set = new ConstraintSet();
        set.clone(constraint_layout);
        if (isFull) {
            isFull = false;
            set.constrainWidth(R.id.a, width / 2);
            set.constrainHeight(R.id.a, height / 2);
            set.setHorizontalBias(R.id.a, 0);
            set.setVerticalBias(R.id.a, 0);

            set.constrainWidth(R.id.b, width / 2);
            set.constrainHeight(R.id.b, height / 2);
            set.setHorizontalBias(R.id.a, 0.5f);
            set.setVerticalBias(R.id.a, 0);

            set.constrainWidth(R.id.c, width / 2);
            set.constrainHeight(R.id.c, height / 2);
            set.setHorizontalBias(R.id.c, 0);
            set.setVerticalBias(R.id.c, 0.5f);

            set.constrainWidth(R.id.d, width / 2);
            set.constrainHeight(R.id.d, height / 2);
            set.setHorizontalBias(R.id.d, 0.5f);
            set.setVerticalBias(R.id.d, 0.5f);
        } else {
            isFull = true;
            set.constrainWidth(R.id.a, 1);
            set.constrainHeight(R.id.a, 1);
            set.setHorizontalBias(R.id.a, 0);
            set.setVerticalBias(R.id.a, 0);

            set.constrainWidth(R.id.b, 1);
            set.constrainHeight(R.id.b, 1);
            set.setHorizontalBias(R.id.b, 0);
            set.setVerticalBias(R.id.b, 0);

            set.constrainWidth(R.id.c, 1);
            set.constrainHeight(R.id.c, 1);
            set.setHorizontalBias(R.id.c, 0);
            set.setVerticalBias(R.id.c, 0);

            set.constrainWidth(R.id.d, width);
            set.constrainHeight(R.id.d, height);
            set.setHorizontalBias(R.id.d, 0);
            set.setVerticalBias(R.id.d, 0);
        }
        set.applyTo(constraint_layout);
    }

    private void fullC() {
        ConstraintSet set = new ConstraintSet();
        set.clone(constraint_layout);
        if (isFull) {
            isFull = false;
            set.constrainWidth(R.id.a, width / 2);
            set.constrainHeight(R.id.a, height / 2);
            set.setHorizontalBias(R.id.a, 0);
            set.setVerticalBias(R.id.a, 0);

            set.constrainWidth(R.id.b, width / 2);
            set.constrainHeight(R.id.b, height / 2);
            set.setHorizontalBias(R.id.a, 0.5f);
            set.setVerticalBias(R.id.a, 0);

            set.constrainWidth(R.id.c, width / 2);
            set.constrainHeight(R.id.c, height / 2);
            set.setHorizontalBias(R.id.c, 0);
            set.setVerticalBias(R.id.c, 0.5f);

            set.constrainWidth(R.id.d, width / 2);
            set.constrainHeight(R.id.d, height / 2);
            set.setHorizontalBias(R.id.d, 0.5f);
            set.setVerticalBias(R.id.d, 0.5f);
        } else {
            isFull = true;
            set.constrainWidth(R.id.a, 1);
            set.constrainHeight(R.id.a, 1);
            set.setHorizontalBias(R.id.a, 0);
            set.setVerticalBias(R.id.a, 0);

            set.constrainWidth(R.id.b, 1);
            set.constrainHeight(R.id.b, 1);
            set.setHorizontalBias(R.id.b, 0);
            set.setVerticalBias(R.id.b, 0);

            set.constrainWidth(R.id.c, width);
            set.constrainHeight(R.id.c, height);
            set.setHorizontalBias(R.id.c, 0);
            set.setVerticalBias(R.id.c, 0);

            set.constrainWidth(R.id.d, 1);
            set.constrainHeight(R.id.d, 1);
            set.setHorizontalBias(R.id.d, 0);
            set.setVerticalBias(R.id.d, 0);
        }
        set.applyTo(constraint_layout);
    }

    private void fullB() {
        ConstraintSet set = new ConstraintSet();
        set.clone(constraint_layout);
        if (isFull) {
            isFull = false;
            set.constrainWidth(R.id.a, width / 2);
            set.constrainHeight(R.id.a, height / 2);
            set.setHorizontalBias(R.id.a, 0);
            set.setVerticalBias(R.id.a, 0);

            set.constrainWidth(R.id.b, width / 2);
            set.constrainHeight(R.id.b, height / 2);
            set.setHorizontalBias(R.id.a, 0.5f);
            set.setVerticalBias(R.id.a, 0);

            set.constrainWidth(R.id.c, width / 2);
            set.constrainHeight(R.id.c, height / 2);
            set.setHorizontalBias(R.id.c, 0);
            set.setVerticalBias(R.id.c, 0.5f);

            set.constrainWidth(R.id.d, width / 2);
            set.constrainHeight(R.id.d, height / 2);
            set.setHorizontalBias(R.id.d, 0.5f);
            set.setVerticalBias(R.id.d, 0.5f);
        } else {
            isFull = true;
            set.constrainWidth(R.id.a, 1);
            set.constrainHeight(R.id.a, 1);
            set.setHorizontalBias(R.id.a, 0);
            set.setVerticalBias(R.id.a, 0);

            set.constrainWidth(R.id.b, width);
            set.constrainHeight(R.id.b, height);
            set.setHorizontalBias(R.id.b, 0);
            set.setVerticalBias(R.id.b, 0);

            set.constrainWidth(R.id.c, 1);
            set.constrainHeight(R.id.c, 1);
            set.setHorizontalBias(R.id.c, 0);
            set.setVerticalBias(R.id.c, 0);

            set.constrainWidth(R.id.d, 1);
            set.constrainHeight(R.id.d, 1);
            set.setHorizontalBias(R.id.d, 0);
            set.setVerticalBias(R.id.d, 0);

        }
        set.applyTo(constraint_layout);
    }

    private void fullA() {
        ConstraintSet set = new ConstraintSet();
        set.clone(constraint_layout);
        if (isFull) {
            isFull = false;
            set.constrainWidth(R.id.a, width / 2);
            set.constrainHeight(R.id.a, height / 2);
            set.setHorizontalBias(R.id.a, 0);
            set.setVerticalBias(R.id.a, 0);

            set.constrainWidth(R.id.b, width / 2);
            set.constrainHeight(R.id.b, height / 2);
            set.setHorizontalBias(R.id.a, 0.5f);
            set.setVerticalBias(R.id.a, 0);

            set.constrainWidth(R.id.c, width / 2);
            set.constrainHeight(R.id.c, height / 2);
            set.setHorizontalBias(R.id.c, 0);
            set.setVerticalBias(R.id.c, 0.5f);

            set.constrainWidth(R.id.d, width / 2);
            set.constrainHeight(R.id.d, height / 2);
            set.setHorizontalBias(R.id.d, 0.5f);
            set.setVerticalBias(R.id.d, 0.5f);
        } else {
            isFull = true;
            set.constrainWidth(R.id.a, width);
            set.constrainHeight(R.id.a, height);
            set.setHorizontalBias(R.id.a, 0);
            set.setVerticalBias(R.id.a, 0);

            set.constrainWidth(R.id.b, 1);
            set.constrainHeight(R.id.b, 1);
            set.setHorizontalBias(R.id.b, 0);
            set.setVerticalBias(R.id.b, 0);

            set.constrainWidth(R.id.c, 1);
            set.constrainHeight(R.id.c, 1);
            set.setHorizontalBias(R.id.c, 0);
            set.setVerticalBias(R.id.c, 0);

            set.constrainWidth(R.id.d, 1);
            set.constrainHeight(R.id.d, 1);
            set.setHorizontalBias(R.id.d, 0);
            set.setVerticalBias(R.id.d, 0);
        }
        set.applyTo(constraint_layout);
    }

    @Override
    public void updateRv(List<VideoDev> videoDevs) {
        if (adapter == null) {
            adapter = new MeetLiveVideoAdapter(R.layout.item_meet_video, videoDevs);
            f_l_v_rv.setLayoutManager(new LinearLayoutManager(getContext()));
            f_l_v_rv.setAdapter(adapter);
            adapter.setOnItemClickListener((ap, view, position) -> {
                if (videoDevs.get(position).getDeviceDetailInfo().getNetstate() == 1) {
                    InterfaceVideo.pbui_Item_MeetVideoDetailInfo videoDetailInfo = videoDevs.get(position).getVideoDetailInfo();
                    LogUtil.d(TAG, "onItemClick --> Subid= " + videoDetailInfo.getSubid());
                    adapter.setSelected(videoDetailInfo.getDeviceid(), videoDetailInfo.getId());
                }
            });
        } else {
            adapter.notifyDataSetChanged();
            adapter.notifySelect();
        }
    }

    @Override
    public void updateDecode(Object[] videoDecode) {
        int resid = (int) videoDecode[1];
        PlayView playView = getViewByResId(resid);
        if (playView == null) return;
        int isKeyframe = (int) videoDecode[0];
        int codecid = (int) videoDecode[2];
        int w = (int) videoDecode[3];
        int h = (int) videoDecode[4];
        byte[] packet = (byte[]) videoDecode[5];
        long pts = (long) videoDecode[6];
        byte[] codecdata = (byte[]) videoDecode[7];
        String mimeType = Constant.getMimeType(codecid);
        //设置播放类型为decode
        playView.setCodecType(1);
        //收到通知后，可能surface还是空
        if (playView.getSurface() != null) {
            if (packet != null) {
                playView.setLastPushTime(System.currentTimeMillis());
                playView.initCodec(mimeType, w, h, codecdata);
            }
            playView.mediaCodecDecode(packet, pts, isKeyframe);
            playView.startTimeThread();
        }
    }

    @Override
    public void updateYuv(Object[] yuvdisplay) {
        int resid = (int) yuvdisplay[0];
        PlayView playView = getViewByResId(resid);
        if (playView == null) return;
        int w = (int) yuvdisplay[1];
        int h = (int) yuvdisplay[2];
        byte[] y = (byte[]) yuvdisplay[3];
        byte[] u = (byte[]) yuvdisplay[4];
        byte[] v = (byte[]) yuvdisplay[5];
        playView.stopTimeThread();
        playView.setCodecType(0);
        playView.setFrameData(w, h, y, u, v);
    }

    @Override
    public void stopResWork(int resid) {
        LogUtil.d(TAG, "stopResWork -->" + "停止播放：" + resid);
        PlayView playView = getViewByResId(resid);
        if (playView != null) {
            playView.stopTimeThread();
            playView.setCodecType(2);
        }
    }

    @Override
    public void notifyOnLineAdapter() {
        if (memberAdapter != null) {
            memberAdapter.notifyDataSetChanged();
            memberAdapter.notifyChecks();
        }
        if (projectorAdapter != null) {
            projectorAdapter.notifyDataSetChanged();
            projectorAdapter.notifyChecks();
        }
        if (wmScreenProjectorAdapter != null) {
            wmScreenProjectorAdapter.notifyDataSetChanged();
            wmScreenProjectorAdapter.notifyChecks();
        }
    }
}
