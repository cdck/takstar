package xlk.takstar.paperless.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.protobuf.ByteString;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;
import com.mogujie.tt.protobuf.InterfaceBase;
import com.mogujie.tt.protobuf.InterfaceDevice;
import com.mogujie.tt.protobuf.InterfaceMacro;
import com.mogujie.tt.protobuf.InterfaceMember;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import xlk.takstar.paperless.App;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.adapter.LocalFileAdapter;
import xlk.takstar.paperless.adapter.MemberAdapter;
import xlk.takstar.paperless.admin.activity.AdminActivity;
import xlk.takstar.paperless.base.BaseActivity;
import xlk.takstar.paperless.meet.MeetingActivity;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.model.GlobalValue;
import xlk.takstar.paperless.ui.ArtBoard;
import xlk.takstar.paperless.ui.RvItemDecoration;
import xlk.takstar.paperless.util.AppUtil;
import xlk.takstar.paperless.util.ConvertUtil;
import xlk.takstar.paperless.util.LogUtil;
import xlk.takstar.paperless.util.PopUtil;

import static xlk.takstar.paperless.App.lbm;
import static xlk.takstar.paperless.model.GlobalValue.camera_height;
import static xlk.takstar.paperless.model.GlobalValue.camera_width;
import static xlk.takstar.paperless.util.ConvertUtil.s2b;

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View, View.OnClickListener {


    private TextView main_tv_meeting_name;
    private TextView main_tv_member_name;
    private TextView main_tv_device_name;
    private TextView main_tv_position;
    private TextView main_tv_unit;
    private LinearLayout main_ll_date;
    private Button main_btn_enter_meeting;
    private ImageView main_iv_logo;
    private ImageView main_iv_close;
    private ImageView main_iv_min;
    private TextView main_tv_date;
    private TextView main_tv_time;
    private ConstraintLayout main_root;
    private int mSignInType;
    private PopupWindow enterPwdPop;
    private TextView tv_handwritten;
    private TextView tv_password;
    private ArtBoard drawing_board;
    private EditText edt_pwd;
    private PopupWindow memberPop;
    private RecyclerView rv_member;
    private MemberAdapter memberAdapter;
    private PopupWindow createPop;

    private final int REQUEST_CODE_READ_FRAME_BUFFER = 1;
    private TextView main_tv_remarks, main_tv_status, main_tv_role, main_tv_version;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected MainPresenter initPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
        setVersion();
//        try {
//            initCameraSize();
//        } catch (Exception e) {
//            LogUtils.e(TAG, "查找摄像机像素失败=" + e);
//            e.printStackTrace();
//        }
        applyReadFrameBufferPermission();
    }

    private void setVersion() {
        if (ini.loadFile()) {
            PackageManager pm = getPackageManager();
            try {
                PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
//                main_tv_version.setText(getString(R.string.app_version_, packageInfo.versionName));
                String hardver = "";
                String softver = "";
                if (packageInfo.versionName.contains(".")) {
                    hardver = packageInfo.versionName.substring(0, packageInfo.versionName.indexOf("."));
                    softver = packageInfo.versionName.substring(packageInfo.versionName.indexOf(".") + 1, packageInfo.versionName.length());
                }
                ini.put("selfinfo", "hardver", hardver);
                ini.put("selfinfo", "softver", softver);
                ini.store();
                LogUtils.i(TAG, "已将版本信息写入ini文件中");
            } catch (PackageManager.NameNotFoundException e) {
                LogUtils.e(TAG, e);
                e.printStackTrace();
            }
        }
    }

    private void initCameraSize() throws Exception {
        int type = 1;
        LogUtil.d(TAG, "initCameraSize :   --> ");
        //获取摄像机的个数 一般是前/后置两个
        int numberOfCameras = Camera.getNumberOfCameras();
        if (numberOfCameras < 2) {
            LogUtil.d(TAG, "initCameraSize: 该设备没有两个摄像头");
            //如果没有2个则说明只有后置像头
            type = 0;
        }
        ArrayList<Integer> supportW = new ArrayList<>();
        ArrayList<Integer> supportH = new ArrayList<>();
        int largestW = 0, largestH = 0;
        Camera c = Camera.open(type);
        Camera.Parameters param = null;
        if (c != null) {
            param = c.getParameters();
        }
        if (param == null) {
            return;
        }
        for (int i = 0; i < param.getSupportedPreviewSizes().size(); i++) {
            int w = param.getSupportedPreviewSizes().get(i).width, h = param.getSupportedPreviewSizes().get(i).height;
            LogUtil.d(TAG, "initCameraSize: w=" + w + " h=" + h);
            supportW.add(w);
            supportH.add(h);
        }
        for (int i = 0; i < supportH.size(); i++) {
            try {
                largestW = supportW.get(i);
                largestH = supportH.get(i);
//                LogUtil.d(TAG, "initCameraSize :   --> largestW= " + largestW + " , largestH=" + largestH);
                MediaFormat mediaFormat = MediaFormat.createVideoFormat("video/avc", largestW, largestH);
                if (MediaCodec.createEncoderByType("video/avc").getCodecInfo().getCapabilitiesForType("video/avc").isFormatSupported(mediaFormat)) {
                    if (camera_width * camera_height <= 640 * 480) {
                        camera_width = largestW;
                        camera_height = largestH;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (c != null) {
                    c.setPreviewCallback(null);
                    c.stopPreview();
                    c.release();
                    c = null;
                }
            }
        }
        LogUtil.d(TAG, "initCameraSize  摄像机像素：" + camera_width + " X " + camera_height);
    }

    /**
     * 平台初始化
     */
    private void platformInitiation() {
        presenter.initialization(AppUtil.getUniqueId(this));
    }

    /**
     * 申请屏幕录制权限
     */
    private void applyReadFrameBufferPermission() {
        App.mMediaProjectionManager = (MediaProjectionManager) getApplication().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        if (App.mResult == 0 || App.mIntent == null) {
            startActivityForResult(App.mMediaProjectionManager.createScreenCaptureIntent(), REQUEST_CODE_READ_FRAME_BUFFER);
        } else {
            platformInitiation();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_READ_FRAME_BUFFER) {
            if (resultCode == Activity.RESULT_OK) {
                App.mResult = resultCode;
                App.mIntent = data;
                App.mMediaProjection = App.mMediaProjectionManager.getMediaProjection(resultCode, data);
                platformInitiation();
            } else {
                applyReadFrameBufferPermission();
            }
        }
    }

    private long lastClickTime = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastClickTime < 1500) {
            exitApp();
        } else {
            lastClickTime = System.currentTimeMillis();
            ToastUtils.showShort(R.string.click_again_exit);
        }
    }

    private void exitApp() {
        Intent intent = new Intent();
        intent.setAction(Constant.ACTION_STOP_SCREEN_RECORD_WHEN_EXIT_APP);
        lbm.sendBroadcast(intent);
//        finish();
//        AppUtils.exitApp();
    }

    @Override
    public void updateTime(String[] date) {
        main_tv_time.setText(date[0]);
        main_tv_date.setText(date[1]);
    }

    @Override
    public void updateSeatName(String name) {
        main_tv_device_name.setText(name);
    }

    @Override
    public void showUpgradeDialog(String content, InterfaceBase.pbui_Type_MeetUpdateNotify info) {

    }

    @Override
    public void jump2meet() {
        presenter.onDestroy();
        finish();
        startActivity(new Intent(MainActivity.this, MeetingActivity.class));
    }

    @Override
    public void readySignIn() {
        if (!XXPermissions.hasPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW)) {
            applyAlertWindowPermission();
        } else {
            switch (mSignInType) {
                case InterfaceMacro.Pb_MeetSignType.Pb_signin_direct_VALUE:
                    jni.sendSign(0, mSignInType, "", s2b(""));
                    break;
                //个人密码签到和会议密码签到
                case InterfaceMacro.Pb_MeetSignType.Pb_signin_psw_VALUE:
                case InterfaceMacro.Pb_MeetSignType.Pb_signin_onepsw_VALUE:
                    enterPwdSign(2);
                    break;
                //拍照(手写)签到
                case InterfaceMacro.Pb_MeetSignType.Pb_signin_photo_VALUE:
                    enterPwdSign(1);
                    break;
                //会议密码+拍照手写和个人密码+拍照手写
                case InterfaceMacro.Pb_MeetSignType.Pb_signin_onepsw_photo_VALUE:
                case InterfaceMacro.Pb_MeetSignType.Pb_signin_psw_photo_VALUE:
                    enterPwdSign(3);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * @param type =1手写签到，=2密码签到，=3手写+密码
     */
    private void enterPwdSign(final int type) {
        View inflate = LayoutInflater.from(this).inflate(R.layout.pop_sign_in, null, false);
        enterPwdPop = PopUtil.createHalfPop(inflate, main_btn_enter_meeting);
        tv_handwritten = inflate.findViewById(R.id.tv_handwritten);
        tv_password = inflate.findViewById(R.id.tv_password);
        drawing_board = inflate.findViewById(R.id.drawing_board);
        edt_pwd = inflate.findViewById(R.id.edt_pwd);
        RelativeLayout pwd_rl = inflate.findViewById(R.id.pwd_rl);
        tv_handwritten.setSelected(true);
        tv_password.setSelected(false);
        pwd_rl.setVisibility(View.GONE);
        tv_handwritten.setOnClickListener(v -> {
            tv_handwritten.setSelected(true);
            tv_password.setSelected(false);
            pwd_rl.setVisibility(View.GONE);
        });
        tv_password.setOnClickListener(v -> {
            tv_handwritten.setSelected(false);
            tv_password.setSelected(true);
            pwd_rl.setVisibility(View.VISIBLE);
        });
        inflate.findViewById(R.id.btn_undo).setOnClickListener(v -> {
            drawing_board.revoke();
        });
        inflate.findViewById(R.id.btn_clean).setOnClickListener(v -> {
            drawing_board.clear();
        });
        inflate.findViewById(R.id.btn_definite).setOnClickListener(v -> {
            ByteString bytestring = s2b("");
            String pwd = "";
            if ((type & 1) == 1) {
                if (drawing_board.isNotEmpty()) {
                    Bitmap canvasBmp = drawing_board.getCanvasBmp();
                    bytestring = ConvertUtil.bmp2bs(canvasBmp);
                    canvasBmp.recycle();
                    drawing_board.clear();
                } else {
                    ToastUtils.showShort(R.string.please_signing_first);
                    return;
                }
            }
            if ((type & 2) == 2) {
                pwd = edt_pwd.getText().toString().trim();
                if (pwd.isEmpty()) {
                    ToastUtils.showShort(R.string.please_enter_password_first);
                    return;
                }
            }
            jni.sendSign(0, mSignInType, pwd, bytestring);
            enterPwdPop.dismiss();
        });
        inflate.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            enterPwdPop.dismiss();
        });
        enterPwdPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                drawing_board.clear();
            }
        });
    }

    /**
     * 申请悬浮窗权限
     */
    private void applyAlertWindowPermission() {
        XXPermissions.with(this).constantRequest()
                .permission(Manifest.permission.SYSTEM_ALERT_WINDOW)
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean all) {
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                    }
                });
    }

    @Override
    public void updateBackground(Drawable drawable) {
        main_root.setBackground(drawable);
    }

    @Override
    public void updateLogo(Drawable drawable) {
//        main_iv_logo.setImageDrawable(drawable);
    }

    @Override
    public void updateMemberRole(String string) {
//        main_tv_role.setVisibility(string.isEmpty() ? View.GONE : View.VISIBLE);
//        main_tv_role.setText(getString(R.string.member_role_, string));
    }

    @Override
    public void updateRemarks(String s) {
//        main_tv_remarks.setVisibility(s.isEmpty() ? View.GONE : View.VISIBLE);
//        main_tv_remarks.setText(s);
    }

    @Override
    public void updateMeetingState(int state) {
//        LogUtil.i(TAG, "updateMeetingState state=" + state);
//        main_tv_status.setVisibility(state == -1 ? View.GONE : View.VISIBLE);
//        //会议状态，0为未开始会议，1为已开始会议，2为已结束会议，其它表示未加入会议无状态
//        switch (state) {
//            case 0:
//                main_tv_status.setText(getString(R.string.state_meet_not));
//                break;
//            case 1:
//                main_tv_status.setText(getString(R.string.state_meet_start));
//                break;
//            case 2:
//                main_tv_status.setText(getString(R.string.state_meet_end));
//                break;
//            default:
//                main_tv_status.setText("");
//                break;
//        }
    }

    @Override
    public void updateMeetInfo(InterfaceDevice.pbui_Type_DeviceFaceShowDetail info) {
        mSignInType = 0;
        GlobalValue.localMemberId = 0;
        if (info != null) {
            mSignInType = info.getSigninType();
            GlobalValue.localMemberId = info.getMemberid();
            GlobalValue.localMemberName = info.getMembername().toStringUtf8();
            String meetName = info.getMeetingname().toStringUtf8();
            String memberName = info.getMembername().toStringUtf8();
            String company = info.getCompany().toStringUtf8();
            String job = info.getJob().toStringUtf8();
            main_tv_meeting_name.setText(meetName);
            main_tv_member_name.setText(!memberName.isEmpty() ? getString(R.string.member_name_) + memberName : "");
            main_tv_position.setText(!company.isEmpty() ? getString(R.string.member_position_) + company : "");
            main_tv_unit.setText(!job.isEmpty() ? getString(R.string.member_unit_) + job : "");
        } else {
            main_tv_meeting_name.setText("");
            main_tv_member_name.setText("");
            main_tv_position.setText("");
            main_tv_unit.setText("");
        }
    }

    @Override
    public void updateUnBindMember() {
        if (memberPop != null && memberPop.isShowing()) {
            memberAdapter.notifyDataSetChanged();
        }
    }

    private void showUnBindMembers() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.pop_unbind_member, null, false);
        memberPop = PopUtil.createBigPop(inflate, main_btn_enter_meeting);
        rv_member = inflate.findViewById(R.id.rv_member);
        memberAdapter = new MemberAdapter(R.layout.item_single_button, presenter.unbindMembers);
        rv_member.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        rv_member.setAdapter(memberAdapter);
        memberAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                LogUtil.i(TAG, "onItemClick " + position);
                memberAdapter.setSelectedId(presenter.unbindMembers.get(position).getPersonid());
            }
        });
        inflate.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            memberPop.dismiss();
        });
        inflate.findViewById(R.id.btn_definite).setOnClickListener(v -> {
            int memberId = memberAdapter.getSelectedMemberId();
            if (memberId != 0) {
                jni.modifyMeetRanking(memberId, 0, GlobalValue.localDeviceId);
                memberPop.dismiss();
            } else {
                ToastUtils.showShort(R.string.please_choose_member);
            }
        });
        inflate.findViewById(R.id.btn_create).setOnClickListener(v -> {
            memberPop.dismiss();
            showCreateMember();
        });
    }

    private void showCreateMember() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.pop_create_member, null, false);
        createPop = PopUtil.createBigPop(inflate, main_btn_enter_meeting);
        EditText edt_company = inflate.findViewById(R.id.edt_company);
        EditText edt_name = inflate.findViewById(R.id.edt_name);
        EditText edt_position = inflate.findViewById(R.id.edt_position);
        EditText edt_phone = inflate.findViewById(R.id.edt_phone);
        EditText edt_email = inflate.findViewById(R.id.edt_email);
        EditText edt_password = inflate.findViewById(R.id.edt_password);
        EditText edt_note = inflate.findViewById(R.id.edt_note);
        inflate.findViewById(R.id.btn_definite).setOnClickListener(v -> {
            String name = edt_name.getText().toString().trim();
            if (name.isEmpty()) {
                ToastUtils.showShort(R.string.please_enter_name);
                return;
            }
            String company = edt_company.getText().toString().trim();
            String position = edt_position.getText().toString().trim();
            String phone = edt_phone.getText().toString().trim();
            String email = edt_email.getText().toString().trim();
            String password = edt_password.getText().toString().trim();
            String note = edt_note.getText().toString().trim();
            if (!email.isEmpty() && !RegexUtils.isEmail(email)) {
                ToastUtils.showShort(R.string.email_format_error);
                return;
            }
            if (!phone.isEmpty() && !RegexUtils.isMobileSimple(phone)) {
                ToastUtils.showShort(R.string.phone_format_error);
                return;
            }
            InterfaceMember.pbui_Item_MemberDetailInfo build = InterfaceMember.pbui_Item_MemberDetailInfo.newBuilder()
                    .setCompany(s2b(company))
                    .setName(s2b(name))
                    .setJob(s2b(position))
                    .setEmail(s2b(email))
                    .setPhone(s2b(phone))
                    .setPassword(s2b(password))
                    .setComment(s2b(note)).build();
            jni.addAttendPeople(build);
            createPop.dismiss();
        });
        inflate.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            createPop.dismiss();
        });
        createPop.setOnDismissListener(this::showUnBindMembers);
    }

    private void initView() {
        main_root = (ConstraintLayout) findViewById(R.id.main_root);
        main_tv_meeting_name = (TextView) findViewById(R.id.main_tv_meeting_name);
        main_tv_member_name = (TextView) findViewById(R.id.main_tv_member_name);
        main_tv_device_name = (TextView) findViewById(R.id.main_tv_device_name);
        main_tv_position = (TextView) findViewById(R.id.main_tv_position);
        main_tv_unit = (TextView) findViewById(R.id.main_tv_unit);
        main_tv_version = (TextView) findViewById(R.id.main_tv_version);

        main_tv_remarks = (TextView) findViewById(R.id.main_tv_remarks);
        main_tv_status = (TextView) findViewById(R.id.main_tv_status);
        main_tv_role = (TextView) findViewById(R.id.main_tv_role);

        main_ll_date = (LinearLayout) findViewById(R.id.main_ll_date);
        main_btn_enter_meeting = (Button) findViewById(R.id.main_btn_enter_meeting);
        main_iv_logo = (ImageView) findViewById(R.id.main_iv_logo);
        main_iv_close = (ImageView) findViewById(R.id.main_iv_close);
        main_iv_min = (ImageView) findViewById(R.id.main_iv_min);
        main_tv_date = (TextView) findViewById(R.id.main_tv_date);
        main_tv_time = (TextView) findViewById(R.id.main_tv_time);

        main_iv_min.setOnClickListener(this);
        main_iv_close.setOnClickListener(this);
        main_btn_enter_meeting.setOnClickListener(this);
        findViewById(R.id.tv_custom_set).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_btn_enter_meeting: {
                if (!main_tv_meeting_name.getText().toString().trim().isEmpty()) {
                    if (!main_tv_member_name.getText().toString().trim().isEmpty()) {
                        readySignIn();
                    } else {
                        ToastUtils.showShort(R.string.please_bind_the_member_first);
                        presenter.queryMember();
                        showUnBindMembers();
                    }
                } else {
                    ToastUtils.showShort(R.string.please_join_the_meeting_first);
                }
                break;
            }
            case R.id.tv_custom_set: {
//                startActivity(new Intent(MainActivity.this, AdminActivity.class));
                if (ini.loadFile()) {
                    showConfigPop();
                }
                break;
            }
            case R.id.main_iv_min: {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                break;
            }
            case R.id.main_iv_close: {
                exitApp();
                break;
            }
            default:
                break;
        }
    }

    private void showConfigPop() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.pop_config, null, false);
        PopupWindow configPop = PopUtil.createBigPop(inflate, main_btn_enter_meeting);
        EditText edt_ip = inflate.findViewById(R.id.edt_ip);
        EditText edt_port = inflate.findViewById(R.id.edt_port);
        EditText edt_bitrate = inflate.findViewById(R.id.edt_bitrate);
        EditText edt_cache_location = inflate.findViewById(R.id.edt_cache_location);
        EditText edt_cache_size = inflate.findViewById(R.id.edt_cache_size);
        CheckBox cb_encode_filter = inflate.findViewById(R.id.cb_encode_filter);
        CheckBox cb_open_microphone = inflate.findViewById(R.id.cb_open_microphone);
        CheckBox cb_disable_multicast = inflate.findViewById(R.id.cb_disable_multicast);
        CheckBox cb_tcp_mode = inflate.findViewById(R.id.cb_tcp_mode);

        edt_cache_location.setKeyListener(null);

        edt_cache_location.setText(ini.get("Buffer Dir", "mediadir"));
        edt_cache_size.setText(ini.get("Buffer Dir", "mediadirsize"));


        edt_ip.setText(ini.get("areaaddr", "area0ip"));
        edt_port.setText(ini.get("areaaddr", "area0port"));
        edt_bitrate.setText(ini.get("debug", "maxBitRate"));

        //编码过滤
        cb_encode_filter.setChecked(ini.get("nosdl", "disablebsf").equals("0"));
        //打开麦克风 将音频附加到视频通道中
        cb_open_microphone.setChecked(ini.get("debug", "videoaudio").equals("1"));
        //禁用组播 等于1表示禁用组播
        cb_disable_multicast.setChecked(ini.get("debug", "disablemulticast").equals("1"));
        //等于1表示使用TCP模式
        cb_tcp_mode.setChecked(ini.get("selfinfo", "streamprotol").equals("1"));

        inflate.findViewById(R.id.iv_close).setOnClickListener(v -> configPop.dismiss());
        inflate.findViewById(R.id.btn_cancel).setOnClickListener(v -> configPop.dismiss());
        inflate.findViewById(R.id.iv_more).setOnClickListener(v -> {
            showDirPop(edt_cache_location);
        });
        inflate.findViewById(R.id.btn_ensure).setOnClickListener(v -> {
            String ip = edt_ip.getText().toString().trim();
            String port = edt_port.getText().toString().trim();
            String bitrate = edt_bitrate.getText().toString().trim();
            String mediadir = edt_cache_location.getText().toString().trim();
            String mediadirsize = edt_cache_size.getText().toString().trim();
            if (ip.isEmpty() || port.isEmpty() || bitrate.isEmpty()) {
                ToastUtils.showShort(R.string.tip_content_empty);
                return;
            }
            if (!RegexUtils.isIP(ip)) {
                ToastUtils.showShort(R.string.ip_format_err);
                return;
            }
            int i = Integer.parseInt(bitrate);
            if (i < 500 || i > 10000) {
                ToastUtils.showShort(R.string.tip_bitrate_scope);
                return;
            }
            ini.put("areaaddr", "area0ip", ip);
            ini.put("areaaddr", "area0port", port);
            ini.put("Buffer Dir", "mediadir", mediadir);
            ini.put("Buffer Dir", "mediadirsize", mediadirsize);
            ini.put("debug", "maxBitRate", bitrate);
            ini.put("nosdl", "disablebsf", cb_encode_filter.isChecked() ? "0" : "1");
            ini.put("debug", "videoaudio", cb_open_microphone.isChecked() ? "1" : "0");
            ini.put("debug", "disablemulticast", cb_disable_multicast.isChecked() ? "1" : "0");
            ini.put("selfinfo", "streamprotol", cb_tcp_mode.isChecked() ? "1" : "0");
            ini.store();
            AppUtils.relaunchApp(true);
            configPop.dismiss();
        });
    }

    private FileFilter dirFilter = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return pathname.isDirectory() && !pathname.getName().startsWith(".");
        }
    };

    private List<File> currentFiles = new ArrayList<>();
    private LocalFileAdapter localFileAdapter;

    private void showDirPop(EditText edt) {
        String rootDir = edt.getText().toString();
        currentFiles.clear();
        currentFiles.addAll(FileUtils.listFilesInDirWithFilter(rootDir, dirFilter));
        View inflate = LayoutInflater.from(this).inflate(R.layout.pop_local_file, null);
        PopupWindow dirPop = PopUtil.createHalfPop(inflate, main_btn_enter_meeting);
        EditText edt_current_dir = inflate.findViewById(R.id.edt_current_dir);
        edt_current_dir.setKeyListener(null);
        edt_current_dir.setText(rootDir);

        RecyclerView rv_current_file = inflate.findViewById(R.id.rv_current_file);
        localFileAdapter = new LocalFileAdapter(R.layout.item_local_file, currentFiles);
        rv_current_file.setLayoutManager(new LinearLayoutManager(this));
        rv_current_file.addItemDecoration(new RvItemDecoration(this));
        rv_current_file.setAdapter(localFileAdapter);
        localFileAdapter.setOnItemClickListener((adapter, view, position) -> {
            File file = currentFiles.get(position);
            edt_current_dir.setText(file.getAbsolutePath());
            edt_current_dir.setSelection(edt_current_dir.getText().toString().length());
            List<File> files = FileUtils.listFilesInDirWithFilter(file, dirFilter);
            currentFiles.clear();
            currentFiles.addAll(files);
            localFileAdapter.notifyDataSetChanged();
        });
        inflate.findViewById(R.id.iv_back).setOnClickListener(v -> {
            String dirPath = edt_current_dir.getText().toString().trim();
            if (dirPath.equals(Environment.getExternalStorageDirectory().getAbsolutePath())) {
                ToastUtils.showShort(R.string.current_dir_root);
                return;
            }
            File file = new File(dirPath);
            File parentFile = file.getParentFile();
            edt_current_dir.setText(parentFile.getAbsolutePath());
            LogUtil.i(TAG, "showChooseDir 上一级的目录=" + parentFile.getAbsolutePath());
            List<File> files = FileUtils.listFilesInDirWithFilter(parentFile, dirFilter);
            currentFiles.clear();
            currentFiles.addAll(files);
            localFileAdapter.notifyDataSetChanged();
        });
        inflate.findViewById(R.id.btn_ensure).setOnClickListener(v -> {
            String text = edt_current_dir.getText().toString();
            edt.setText(text);
            edt.setSelection(text.length());
            dirPop.dismiss();
        });
        inflate.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            dirPop.dismiss();
        });
    }
}
