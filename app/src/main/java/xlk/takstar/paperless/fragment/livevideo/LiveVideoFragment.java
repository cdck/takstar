package xlk.takstar.paperless.fragment.livevideo;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mogujie.tt.protobuf.InterfaceMacro;
import com.mogujie.tt.protobuf.InterfaceVideo;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.adapter.MeetLiveVideoAdapter;
import xlk.takstar.paperless.adapter.WmProjectorAdapter;
import xlk.takstar.paperless.adapter.WmScreenMemberAdapter;
import xlk.takstar.paperless.base.BaseFragment;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.model.bean.VideoDev;
import xlk.takstar.paperless.service.fab.CustomBaseViewHolder;
import xlk.takstar.paperless.ui.video.CustomVideoView;
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
 * @author Created by xlk on 2020/12/2.
 * @desc
 */
public class LiveVideoFragment extends BaseFragment<LiveVideoPresenter> implements LiveVideoContract.View, View.OnClickListener, CustomVideoView.ViewClickListener {
    public static boolean isVideoManage = false;
    private Button f_l_v_stop_pro;
    private Button f_l_v_start_pro;
    private Button f_l_v_stop_screen;
    private Button f_l_v_start_screen;
    private RecyclerView f_l_v_rv;
    private Button f_l_v_watch;
    private Button f_l_v_stop;
    private CustomVideoView f_l_v_v;
    List<Integer> ids = new ArrayList<>();
    private WmScreenMemberAdapter memberAdapter;
    private WmProjectorAdapter projectorAdapter;
    private int pvWidth,pvHeight;
    private MeetLiveVideoAdapter adapter;
    private PopupWindow proPop;
    private PopupWindow screenPop;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_live_video;
    }

    @Override
    protected void initView(View inflate) {
        f_l_v_rv = (RecyclerView) inflate.findViewById(R.id.f_l_v_rv);
        f_l_v_watch = (Button) inflate.findViewById(R.id.f_l_v_watch);
        f_l_v_stop = (Button) inflate.findViewById(R.id.f_l_v_stop);
        f_l_v_v = (CustomVideoView) inflate.findViewById(R.id.f_l_v_v);

        f_l_v_watch.setOnClickListener(this);
        f_l_v_stop.setOnClickListener(this);
        f_l_v_v.setViewClickListener(this);
        f_l_v_stop_pro = (Button) inflate.findViewById(R.id.f_l_v_stop_pro);
        f_l_v_stop_pro.setOnClickListener(this);
        f_l_v_start_pro = (Button) inflate.findViewById(R.id.f_l_v_start_pro);
        f_l_v_start_pro.setOnClickListener(this);
        f_l_v_stop_screen = (Button) inflate.findViewById(R.id.f_l_v_stop_screen);
        f_l_v_stop_screen.setOnClickListener(this);
        f_l_v_start_screen = (Button) inflate.findViewById(R.id.f_l_v_start_screen);
        f_l_v_start_screen.setOnClickListener(this);

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
        memberAdapter = new WmScreenMemberAdapter(R.layout.item_single_button, presenter.onLineMember);
        projectorAdapter = new WmProjectorAdapter(R.layout.item_single_button, presenter.onLineProjectors);
        f_l_v_v.post(() -> {
            pvWidth = f_l_v_v.getWidth();
            pvHeight = f_l_v_v.getHeight();
            start();
        });
    }

    @Override
    protected void onShow() {
        start();
    }

    @Override
    protected void onHide() {
        stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop();
        presenter.onDestroy();
    }

    private void start() {
        f_l_v_start_pro.setVisibility(isVideoManage ? View.VISIBLE : View.GONE);
        f_l_v_stop_pro.setVisibility(isVideoManage ? View.VISIBLE : View.GONE);
        f_l_v_start_screen.setVisibility(isVideoManage ? View.VISIBLE : View.GONE);
        f_l_v_stop_screen.setVisibility(isVideoManage ? View.VISIBLE : View.GONE);
        presenter.register();
        presenter.initVideoRes(pvWidth, pvHeight);
        f_l_v_v.createView(ids);
        presenter.queryDeviceInfo();
    }

    private void stop() {
        for (Integer id : ids) {
            presenter.stopResource(id);
        }
        f_l_v_v.clearAll();
        presenter.releaseVideoRes();
        presenter.unregister();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.f_l_v_watch:
                if (adapter != null) {
                    VideoDev videoDev = adapter.getSelected();
                    if (videoDev != null) {
                        int selectResId = f_l_v_v.getSelectResId();
                        if (selectResId != -1) {
                            presenter.stopResource(selectResId);
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    presenter.watch(videoDev, selectResId);
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
            case R.id.f_l_v_stop:
                int selectResId = f_l_v_v.getSelectResId();
                if (selectResId != -1) {
                    presenter.stopResource(selectResId);
                } else {
                    ToastUtils.showShort(R.string.please_choose_stop_view);
                }
                break;
            case R.id.f_l_v_stop_pro:
                if (adapter != null && adapter.getSelected() != null) {
                    if (Constant.hasPermission(permission_code_projection)) {
                        showProPop(false, adapter.getSelected());
                    } else {
                        ToastUtils.showShort(R.string.err_NoPermission);
                    }
                }
                break;
            case R.id.f_l_v_start_pro:
                if (adapter != null && adapter.getSelected() != null) {
                    if (Constant.hasPermission(permission_code_projection)) {
                        showProPop(true, adapter.getSelected());
                    } else {
                        ToastUtils.showShort(R.string.err_NoPermission);
                    }
                }
                break;
            case R.id.f_l_v_stop_screen:
                if (adapter != null && adapter.getSelected() != null) {
                    if (Constant.hasPermission(permission_code_screen)) {
                        showScreenPop(false, adapter.getSelected());
                    } else {
                        ToastUtils.showShort(R.string.err_NoPermission);
                    }
                }
                break;
            case R.id.f_l_v_start_screen:
                if (adapter != null && adapter.getSelected() != null) {
                    if (Constant.hasPermission(permission_code_screen)) {
                        showScreenPop(true, adapter.getSelected());
                    } else {
                        ToastUtils.showShort(R.string.err_NoPermission);
                    }
                }
                break;
            default:break;
        }
    }

    private void showProPop(boolean isStart, VideoDev videoDev) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.wm_pro_view, null);
        proPop = PopUtil.createHalfPop(inflate, f_l_v_stop_pro);
        CustomBaseViewHolder.ProViewHolder holder = new CustomBaseViewHolder.ProViewHolder(inflate);
        proHolderEvent(holder, isStart, videoDev);
    }

    private void proHolderEvent(CustomBaseViewHolder.ProViewHolder holder, boolean isStart, VideoDev videoDev) {
        holder.wm_pro_mandatory.setVisibility(isStart ? View.VISIBLE : View.INVISIBLE);
        holder.wm_pro_title.setText(isStart ? getContext().getString(R.string.launch_pro_title) : getContext().getString(R.string.stop_pro_title));
        holder.wm_pro_launch_pro.setText(isStart ? getContext().getString(R.string.launch_pro) : getContext().getString(R.string.stop_pro));
        holder.wm_pro_rv.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
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
        holder.wm_pro_cancel.setOnClickListener(v -> proPop.dismiss());
        holder.wm_pro_launch_pro.setOnClickListener(v -> {
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
    }

    private void showScreenPop(boolean isStart, VideoDev videoDev) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.wm_screen_view, null);
        screenPop = PopUtil.createHalfPop(inflate, f_l_v_stop_pro);
        CustomBaseViewHolder.ScreenViewHolder holder = new CustomBaseViewHolder.ScreenViewHolder(inflate);
        holderEvent(holder, isStart, videoDev);
    }

    private void holderEvent(CustomBaseViewHolder.ScreenViewHolder holder, boolean isStart, VideoDev videoDev) {
        holder.wm_screen_mandatory.setVisibility(isStart ? View.VISIBLE : View.INVISIBLE);
        if (isStart) {
            holder.wm_screen_launch.setText(getContext().getString(R.string.launch_screen));
            holder.wm_screen_title.setText(getContext().getString(R.string.launch_screen_title));
        } else {
            holder.wm_screen_launch.setText(getContext().getString(R.string.stop_screen));
            holder.wm_screen_title.setText(getContext().getString(R.string.stop_screen_title));
        }
        holder.wm_screen_rv_attendee.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
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
        holder.wm_screen_rv_projector.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        holder.wm_screen_rv_projector.setAdapter(projectorAdapter);
        projectorAdapter.setOnItemClickListener((adapter, view, position) -> {
            projectorAdapter.choose(presenter.onLineProjectors.get(position).getDevcieid());
            holder.wm_screen_cb_projector.setChecked(projectorAdapter.isChooseAll());
        });
        holder.wm_screen_cb_projector.setOnClickListener(v -> {
            boolean checked = holder.wm_screen_cb_projector.isChecked();
            holder.wm_screen_cb_projector.setChecked(checked);
            projectorAdapter.setChooseAll(checked);
        });
        holder.wm_screen_cancel.setOnClickListener(v -> screenPop.dismiss());
        //发起/结束同屏
        holder.wm_screen_launch.setOnClickListener(v -> {
            List<Integer> ids = memberAdapter.getChooseIds();
            ids.addAll(projectorAdapter.getChooseIds());
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
    }


    long oneTime, twoTime, threeTime, fourTime;

    @Override
    public void click(int res) {
        switch (res) {
            case RESOURCE_ID_1:
                f_l_v_v.setSelectResId(res);
                if (System.currentTimeMillis() - oneTime < 500) {
                    f_l_v_v.zoom(res);
                } else {
                    oneTime = System.currentTimeMillis();
                }
                break;
            case RESOURCE_ID_2:
                f_l_v_v.setSelectResId(res);
                if (System.currentTimeMillis() - twoTime < 500) {
                    f_l_v_v.zoom(res);
                } else {
                    twoTime = System.currentTimeMillis();
                }
                break;
            case RESOURCE_ID_3:
                f_l_v_v.setSelectResId(res);
                if (System.currentTimeMillis() - threeTime < 500) {
                    f_l_v_v.zoom(res);
                } else {
                    threeTime = System.currentTimeMillis();
                }
                break;
            case RESOURCE_ID_4:
                f_l_v_v.setSelectResId(res);
                if (System.currentTimeMillis() - fourTime < 500) {
                    f_l_v_v.zoom(res);
                } else {
                    fourTime = System.currentTimeMillis();
                }
                break;
        }
    }

    @Override
    public void updateRv(List<VideoDev> videoDevs) {
        if (adapter == null) {
            adapter = new MeetLiveVideoAdapter(R.layout.item_meet_video, videoDevs);
            f_l_v_rv.setLayoutManager(new LinearLayoutManager(getContext()));
            f_l_v_rv.setAdapter(adapter);
            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter ad, View view, int position) {
                    if (videoDevs.get(position).getDeviceDetailInfo().getNetstate() == 1) {
                        InterfaceVideo.pbui_Item_MeetVideoDetailInfo videoDetailInfo = videoDevs.get(position).getVideoDetailInfo();
                        LogUtil.d(TAG, "onItemClick --> Subid= " + videoDetailInfo.getSubid());
                        adapter.setSelected(videoDetailInfo.getDeviceid(), videoDetailInfo.getId());
                    }
                }
            });
        } else {
            adapter.notifyDataSetChanged();
            adapter.notifySelect();
        }
    }

    @Override
    public void stopResWork(int resid) {
        f_l_v_v.stopResWork(resid);
    }

    @Override
    public void updateYuv(Object[] objs) {
        f_l_v_v.setYuv(objs);
    }

    @Override
    public void updateDecode(Object[] objs) {
        f_l_v_v.setVideoDecode(objs);
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
    }
}
