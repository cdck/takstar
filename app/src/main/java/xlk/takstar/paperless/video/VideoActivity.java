package xlk.takstar.paperless.video;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.mogujie.tt.protobuf.InterfaceMacro;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.adapter.DevMemberAdapter;
import xlk.takstar.paperless.adapter.ProjectionAdapter;
import xlk.takstar.paperless.base.BaseActivity;
import xlk.takstar.paperless.meet.MeetingActivity;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.model.EventMessage;
import xlk.takstar.paperless.model.EventType;
import xlk.takstar.paperless.model.GlobalValue;
import xlk.takstar.paperless.service.fab.FabService;
import xlk.takstar.paperless.ui.video.MyGLSurfaceView;
import xlk.takstar.paperless.ui.video.WlOnGlSurfaceViewOncreateListener;
import xlk.takstar.paperless.util.LogUtil;
import xlk.takstar.paperless.util.PopUtil;

import static xlk.takstar.paperless.fragment.draw.DrawFragment.isDrawing;
import static xlk.takstar.paperless.model.GlobalValue.haveNewPlayInform;
import static xlk.takstar.paperless.model.GlobalValue.isMandatoryPlaying;
import static xlk.takstar.paperless.model.GlobalValue.isVideoPlaying;

public class VideoActivity extends BaseActivity<VideoPresenter> implements VideoContract.View, WlOnGlSurfaceViewOncreateListener {

    private MyGLSurfaceView video_view;
    private ImageView opticalDisk;
    private ImageView plectrum;
    private ImageView two;
    private RelativeLayout play_mp3_view;
    private TextView video_top_title;
    private ConstraintLayout video_root_layout;

    private PopupWindow popView;
    private TextView pop_video_time, pop_video_current_time;
    private SeekBar seekBar;
    private LinearLayout pop_video_schedule;
    private int playAction;
    private int mStatus = -1;
    private int lastPer;
    private String lastSec;
    private String lastTotal;
    private ObjectAnimator plectrumAnimator;
    private ObjectAnimator opticalDiskAnimator;
    private PopupWindow screenPop;
    private DevMemberAdapter devMemberAdapter;
    private ProjectionAdapter projectionAdapter;


    class HomeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
                String reason = intent.getStringExtra("reason");
                if (reason != null) {
                    if (reason.equals("homekey")) {
                        LogUtils.e(TAG, "onReceive 用户点击了home键");
                        finish();
                    } else if (reason.equals("recentapps")) {
                        LogUtils.e(TAG, "onReceive 用户点击了多任务键");
                    }
                }
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video;
    }

    @Override
    protected VideoPresenter initPresenter() {
        return new VideoPresenter(this);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
        presenter.queryMember();
        showVideoOrMusicUI(getIntent());
        //创建广播
        HomeReceiver homeReceiver = new HomeReceiver();
        //动态注册广播
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        //启动广播
        registerReceiver(homeReceiver, intentFilter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showVideoOrMusicUI(intent);
    }

    private void showVideoOrMusicUI(Intent intent) {
        GlobalValue.isVideoPlaying = true;
        if (isMandatoryPlaying) {
            setCanNotExit();
        }
        playAction = intent.getIntExtra(Constant.EXTRA_VIDEO_ACTION, -1);
        int subtype = intent.getIntExtra(Constant.EXTRA_VIDEO_SUBTYPE, -1);
        int deviceId = intent.getIntExtra(Constant.EXTRA_VIDEO_DEVICE_ID, -1);
        int subId = intent.getIntExtra(Constant.EXTRA_VIDEO_SUB_ID, -1);
        int mediaId = intent.getIntExtra(Constant.EXTRA_VIDEO_MEDIA_ID, -1);
        presenter.setDeviceId(deviceId, subId, mediaId);
        if (subtype == Constant.MEDIA_FILE_TYPE_MP3) {
            //如果当前播放的是mp3文件，则只显示MP3控件
            play_mp3_view.setVisibility(View.VISIBLE);
            video_view.setVisibility(View.GONE);
            presenter.releasePlay();
        } else {
            play_mp3_view.setVisibility(View.GONE);
            video_view.setVisibility(View.VISIBLE);
            video_view.setOnGlSurfaceViewOncreateListener(this);
            if (playAction == InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_STREAMPLAY_VALUE) {
                String devName = presenter.queryDevName(deviceId);
                LogUtil.i(TAG, "showVideoOrMusicUI devName=" + devName);
                video_top_title.setText(devName);
            }
        }
        if (popView != null && popView.isShowing()) {
            pop_video_schedule.setVisibility(playAction == InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_STREAMPLAY_VALUE
                    ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void setCanNotExit() {
        if (popView != null && popView.isShowing()) {
            popView.dismiss();
        }
        video_root_layout.setClickable(false);
    }

    @Override
    public void updateTopTitle(String title) {
        video_top_title.setText(title);
    }

    @Override
    public void updateProgressUi(int per, String currentTime, String totalTime) {
        if (seekBar != null && pop_video_time != null && pop_video_current_time != null) {
            lastPer = per;
            lastSec = currentTime;
            lastTotal = totalTime;
            seekBar.setProgress(per);
            pop_video_current_time.setText(currentTime);
            pop_video_time.setText(totalTime);
        }
    }

    @Override
    public void setCodecType(int type) {
        video_view.setCodecType(type);
    }

    @Override
    public void updateYuv(int w, int h, byte[] y, byte[] u, byte[] v) {
        setCodecType(0);
        video_view.setFrameData(w, h, y, u, v);
    }

    @Override
    public void updateAnimator(int status) {
        if (mStatus == status) return;
        mStatus = status;
        LogUtil.i(TAG, "updateAnimator 新的状态：" + mStatus);
        //0=播放中，1=暂停，2=停止,3=恢复
        switch (mStatus) {
            case 0:
                startAnimator();
                break;
            case 1:
                stopAnimator();
                break;
        }
    }

    @Override
    public void updateRecyclerView() {
        if (screenPop != null && screenPop.isShowing()) {
            devMemberAdapter.notifyDataSetChanged();
            projectionAdapter.notifyDataSetChanged();
        }
    }

    private void plectrum(float from, float to, long duration) {
        plectrumAnimator = ObjectAnimator.ofFloat(plectrum, "rotation", from, to);
        plectrum.setPivotX(1);
        plectrum.setPivotY(1);
        plectrumAnimator.setDuration(duration);
        plectrumAnimator.start();
    }

    private void startAnimator() {
        LogUtil.i(TAG, "startAnimator ");
        plectrum(0f, 30f, 500);
        opticalDiskAnimator = ObjectAnimator.ofFloat(opticalDisk, "rotation", 0f, 360f);
        opticalDiskAnimator.setDuration(3000);
        opticalDiskAnimator.setRepeatCount(ValueAnimator.INFINITE);
        opticalDiskAnimator.setRepeatMode(ValueAnimator.RESTART);
        opticalDiskAnimator.setInterpolator(new LinearInterpolator());
        opticalDiskAnimator.start();
    }

    private void stopAnimator() {
        LogUtil.i(TAG, "stopAnimator ");
        if (opticalDiskAnimator != null) {
            opticalDiskAnimator.cancel();
            opticalDiskAnimator = null;
        }
        plectrum(30f, 0f, 500L);
    }

    private void initView() {
        video_root_layout = (ConstraintLayout) findViewById(R.id.video_root_layout);
        play_mp3_view = (RelativeLayout) findViewById(R.id.play_mp3_view);
        opticalDisk = (ImageView) findViewById(R.id.opticalDisk);
        plectrum = (ImageView) findViewById(R.id.plectrum);
        video_top_title = (TextView) findViewById(R.id.video_top_title);
        video_view = (MyGLSurfaceView) findViewById(R.id.video_view);
        video_root_layout.setOnClickListener(v -> {
            if (popView != null && popView.isShowing()) {
                video_top_title.setVisibility(View.GONE);
                popView.dismiss();
                return;
            }
            video_top_title.setVisibility(View.VISIBLE);
            createPop();
        });
    }

    private void createPop() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.pop_video_bottom, null);
        popView = new PopupWindow(inflate, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popView.setBackgroundDrawable(new BitmapDrawable());
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        popView.setTouchable(true);
        // true:设置触摸外面时消失
        popView.setOutsideTouchable(true);
        popView.setFocusable(true);
        popView.setAnimationStyle(R.style.pop_animation_t_b);
        popView.showAtLocation(video_root_layout, Gravity.BOTTOM, 0, 0);
        pop_video_current_time = inflate.findViewById(R.id.pop_video_current_time);
        pop_video_time = inflate.findViewById(R.id.pop_video_time);
        seekBar = inflate.findViewById(R.id.pop_video_seekBar);
        pop_video_schedule = inflate.findViewById(R.id.pop_video_schedule);
        pop_video_schedule.setVisibility(playAction == InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_STREAMPLAY_VALUE ? View.GONE : View.VISIBLE);

        /**  手动设置隐藏PopupWindow时保存的信息  */
        seekBar.setProgress(lastPer);
        pop_video_current_time.setText(lastSec);
        pop_video_time.setText(lastTotal);

        inflate.findViewById(R.id.pop_video_play).setOnClickListener(v -> {
            presenter.playOrPause();
        });
        inflate.findViewById(R.id.pop_video_stop).setOnClickListener(v -> {
            presenter.stopPlay();
            popView.dismiss();
            finish();
        });
        inflate.findViewById(R.id.pop_video_screen_shot).setOnClickListener(v -> {
            presenter.cutVideoImg();
            video_view.cutVideoImg();
        });
        inflate.findViewById(R.id.pop_video_launch_screen).setOnClickListener(v -> {
            showScreenPop(true);
        });
        inflate.findViewById(R.id.pop_video_stop_screen).setOnClickListener(v -> {
            showScreenPop(false);
//            presenter.stopPlay();
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                presenter.setPlayPlace(seekBar.getProgress());
            }
        });
        popView.setOnDismissListener(() -> video_top_title.setVisibility(View.GONE));
    }

    private void showScreenPop(boolean isLaunch) {
        View inflate = LayoutInflater.from(this).inflate(R.layout.pop_screen, null, false);
        screenPop = PopUtil.createHalfPop(inflate, video_root_layout);
        CheckBox cb_mandatory = inflate.findViewById(R.id.cb_mandatory);
        CheckBox cb_member = inflate.findViewById(R.id.cb_member);
        RecyclerView rv_member = inflate.findViewById(R.id.rv_member);
        CheckBox cb_projection = inflate.findViewById(R.id.cb_projection);
        RecyclerView rv_projection = inflate.findViewById(R.id.rv_projection);
        devMemberAdapter = new DevMemberAdapter(presenter.onLineMember);
        rv_member.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        rv_member.setAdapter(devMemberAdapter);
        devMemberAdapter.setOnItemClickListener((adapter, view, position) -> {
            devMemberAdapter.setCheck(presenter.onLineMember.get(position).getMemberDetailInfo().getPersonid());
            cb_member.setChecked(devMemberAdapter.isCheckAll());
        });
        inflate.findViewById(R.id.cb_member).setOnClickListener(v -> {
            boolean checked = cb_member.isChecked();
            cb_member.setChecked(checked);
            devMemberAdapter.setCheckAll(checked);
        });

        projectionAdapter = new ProjectionAdapter(presenter.onLineProjectors);
        rv_projection.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rv_projection.setAdapter(projectionAdapter);
        projectionAdapter.setOnItemClickListener((adapter, view, position) -> {
            projectionAdapter.setCheck(presenter.onLineProjectors.get(position).getDevcieid());
            cb_projection.setChecked(projectionAdapter.isCheckedAll());
        });
        inflate.findViewById(R.id.cb_projection).setOnClickListener(v -> {
            boolean checked = cb_projection.isChecked();
            cb_projection.setChecked(checked);
            projectionAdapter.setCheckAll(checked);
        });

        inflate.findViewById(R.id.btn_definite).setOnClickListener(v -> {
            List<Integer> deviceIds = devMemberAdapter.getCheckDeviceIds();
            deviceIds.addAll(projectionAdapter.getCheckDeviceIds());
            if (deviceIds.isEmpty()) {
                ToastUtils.showShort(R.string.please_choose_target);
                return;
            }
            if (isLaunch) {
                int value = cb_mandatory.isChecked() ? InterfaceMacro.Pb_TriggerUsedef.Pb_EXCEC_USERDEF_FLAG_NOCREATEWINOPER_VALUE : 0;
                presenter.launchScreen(deviceIds, value);
            } else {
                presenter.stopPlay();
            }
            screenPop.dismiss();
        });
        inflate.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            screenPop.dismiss();
        });
        //默认全选
        cb_member.performClick();
        cb_projection.performClick();
    }

    @Override
    public void close() {
        //500毫秒之后再判断是否退出
        haveNewPlayInform = false;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                LogUtil.i(TAG, "close :   --->>> haveNewPlayInform= " + haveNewPlayInform);
                if (!haveNewPlayInform) {
                    finish();
                } else {
                    timer.cancel();
                    timer.purge();
                }
            }
        }, 500);
    }

    @Override
    public void onBackPressed() {
        if (!isMandatoryPlaying) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(popView!=null && popView.isShowing()){
            popView.dismiss();
        }
        isVideoPlaying = false;
        isMandatoryPlaying = false;
        presenter.stopPlay();
        presenter.releaseMediaRes();
        presenter.releasePlay();
    }

    @Override
    public void onGlSurfaceViewOncreate(Surface surface) {
        presenter.setSurface(surface);
    }

    @Override
    public void onCutVideoImg(Bitmap bitmap) {
        LogUtil.i(TAG, "onCutVideoImg -->" + "截图成功");
        if (bitmap != null) {
            FabService.screenShotBitmap = bitmap;
//            if (isDrawing) {
//                EventBus.getDefault().post(new EventMessage.Builder().type(EventType.BUS_SCREEN_SHOT).build());
//            } else {
                finish();
                LogUtil.i(TAG, "onCutVideoImg 跳转到画板界面");
                Intent intent = new Intent(VideoActivity.this, MeetingActivity.class);
                intent.putExtra("draw", "draw");
                startActivity(intent);
//            }
        }
    }
}
