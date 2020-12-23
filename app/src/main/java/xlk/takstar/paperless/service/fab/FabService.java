package xlk.takstar.paperless.service.fab;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.protobuf.ByteString;
import com.mogujie.tt.protobuf.InterfaceBullet;
import com.mogujie.tt.protobuf.InterfaceDevice;
import com.mogujie.tt.protobuf.InterfaceMacro;
import com.mogujie.tt.protobuf.InterfaceVote;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.adapter.WmCanJoinMemberAdapter;
import xlk.takstar.paperless.adapter.WmCanJoinProAdapter;
import xlk.takstar.paperless.adapter.WmProjectorAdapter;
import xlk.takstar.paperless.adapter.WmScreenMemberAdapter;
import xlk.takstar.paperless.chatonline.ChatVideoActivity;
import xlk.takstar.paperless.meet.MeetingActivity;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.model.EventMessage;
import xlk.takstar.paperless.model.EventType;
import xlk.takstar.paperless.model.GlobalValue;
import xlk.takstar.paperless.model.JniHelper;
import xlk.takstar.paperless.service.CameraActivity;
import xlk.takstar.paperless.ui.CircleMenuLayout;
import xlk.takstar.paperless.util.AppUtil;
import xlk.takstar.paperless.util.DateUtil;
import xlk.takstar.paperless.util.DialogUtil;
import xlk.takstar.paperless.util.FileUtil;
import xlk.takstar.paperless.util.LogUtil;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static xlk.takstar.paperless.MyApplication.mMediaProjection;
import static xlk.takstar.paperless.chatonline.ChatVideoActivity.isChatingOpened;
import static xlk.takstar.paperless.fragment.draw.DrawFragment.isDrawing;
import static xlk.takstar.paperless.model.Constant.RESOURCE_ID_0;
import static xlk.takstar.paperless.model.Constant.RESOURCE_ID_1;
import static xlk.takstar.paperless.model.Constant.RESOURCE_ID_2;
import static xlk.takstar.paperless.model.Constant.RESOURCE_ID_3;
import static xlk.takstar.paperless.model.Constant.RESOURCE_ID_4;
import static xlk.takstar.paperless.model.Constant.permission_code_projection;
import static xlk.takstar.paperless.model.Constant.permission_code_screen;

/**
 * @author Created by xlk on 2020/11/30.
 * @desc
 */
public class FabService extends Service implements FabContract.View {
    private final String TAG = "FabService-->";
    private JniHelper jni = JniHelper.getInstance();
    private Context cxt;
    public static Bitmap screenShotBitmap = null;
    private FabPresenter presenter;
    private WindowManager wm;
    private int windowWidth, windowHeight;
    private ImageReader mImageReader;
    private long downTime, upTime;
    private int mTouchStartX, mTouchStartY;


    private WmScreenMemberAdapter memberAdapter;
    private WmProjectorAdapter projectorAdapter;
    private WmCanJoinMemberAdapter joinMemberAdapter;
    private WmCanJoinProAdapter joinProAdapter;

    private WindowManager.LayoutParams mParams, defaultParams, fullParams, wrapParams;
    private ImageView hoverButton;
    private boolean hoverButtonIsShowing;
    private View menuView, serviceView, screenView, joinView, proView, voteView, voteEnsureView, noteView;
    private boolean menuViewIsShowing, serviceViewIsShowing, screenViewIsShowing,
            joinViewIsShowing, proViewIsShowing, voteViewIsShowing, voteEnsureViewIsShowing, noteViewIsShowing;
    private int mScreenDensity;
    private VirtualDisplay mVirtualDisplay;

    private int currentVoteId;
    private int currentChooseCount;
    private int voteTimeouts;
    private int maxChooseCount = 1;//当前投票最多可以选择答案的个数

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.i(TAG, "onCreate ");
        cxt = getApplicationContext();
        presenter = new FabPresenter(this, this);
        presenter.queryMember();
        memberAdapter = new WmScreenMemberAdapter(R.layout.item_single_button, presenter.onLineMember);
        projectorAdapter = new WmProjectorAdapter(R.layout.item_single_button, presenter.onLineProjectors);
        joinMemberAdapter = new WmCanJoinMemberAdapter(R.layout.item_single_button, presenter.canJoinMembers);
        joinProAdapter = new WmCanJoinProAdapter(R.layout.item_single_button, presenter.canJoinPros);

        hoverButton = new ImageView(this);
        hoverButton.setTag("hoverButton");
        hoverButton.setImageResource(R.drawable.ic_fab);
        hoverButton.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downTime = System.currentTimeMillis();
                    mTouchStartX = (int) event.getRawX();
                    mTouchStartY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int rawX = (int) event.getRawX();
                    int rawY = (int) event.getRawY();
                    int mx = rawX - mTouchStartX;
                    int my = rawY - mTouchStartY;
                    mParams.x += mx;
                    mParams.y += my;//相对于屏幕左上角的位置
                    wm.updateViewLayout(hoverButton, mParams);
                    mTouchStartX = rawX;
                    mTouchStartY = rawY;
                    break;
                case MotionEvent.ACTION_UP:
                    upTime = System.currentTimeMillis();
                    if (upTime - downTime > 150) {
//                        mParams.x = windowWidth - hoverButton.getWidth();
//                        mParams.y = mTouchStartY - hoverButton.getHeight();
//                        wm.updateViewLayout(hoverButton, mParams);
                    } else {
                        showMenuView();
                    }
                    break;
            }
            return true;
        });
        initial();
        initParams();
        hoverButtonIsShowing = true;
        wm.addView(hoverButton, mParams);
    }

    private void setParamsType(WindowManager.LayoutParams params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0新特性
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            params.type = WindowManager.LayoutParams.TYPE_PHONE;//总是出现在应用程序窗口之上
        } else {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;//总是出现在应用程序窗口之上
        }
    }

    private void initParams() {
        /** **** **  悬浮按钮  ** **** **/
        mParams = new WindowManager.LayoutParams();
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        setParamsType(mParams);
        mParams.format = PixelFormat.RGBA_8888;
        mParams.gravity = Gravity.START | Gravity.TOP;
        mParams.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        mParams.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        LogUtil.i(TAG, "initParams 屏幕宽高=" + windowWidth + "," + windowHeight
                + ",悬浮按钮宽高=" + hoverButton.getWidth() + "," + hoverButton.getHeight());
        mParams.x = windowWidth - hoverButton.getWidth();
        mParams.y = windowHeight;//使用windowHeight在首次拖动的时候才会有效
        mParams.windowAnimations = R.style.pop_animation_t_b;
        /** **** **  弹框  ** **** **/
        defaultParams = new WindowManager.LayoutParams();
        setParamsType(defaultParams);
        defaultParams.format = PixelFormat.RGBA_8888;
        defaultParams.gravity = Gravity.CENTER;
        defaultParams.width = GlobalValue.half_width;
        defaultParams.height = GlobalValue.half_height;
        defaultParams.windowAnimations = R.style.pop_animation_t_b;
        /** **** **  充满屏幕  ** **** **/
        fullParams = new WindowManager.LayoutParams();
        setParamsType(fullParams);
        fullParams.format = PixelFormat.RGBA_8888;
        fullParams.gravity = Gravity.CENTER;
        fullParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
        fullParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
        fullParams.windowAnimations = R.style.pop_animation_t_b;
        /** **** **  外部不可点击  ** **** **/
        wrapParams = new WindowManager.LayoutParams();
        setParamsType(wrapParams);
        wrapParams.format = PixelFormat.RGBA_8888;
        wrapParams.gravity = Gravity.CENTER;
        wrapParams.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        wrapParams.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        wrapParams.windowAnimations = R.style.pop_animation_t_b;

    }

    private void initial() {
        //获取 WindowManager
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowWidth = wm.getDefaultDisplay().getWidth();
        windowHeight = wm.getDefaultDisplay().getHeight();
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;
        mImageReader = ImageReader.newInstance(GlobalValue.screen_width, GlobalValue.screen_height, 0x1, 2);
    }

    private void showMenuView() {
        menuView = LayoutInflater.from(this).inflate(R.layout.fab_menu_view, null);
        menuView.setFocusable(true);
        menuView.setFocusableInTouchMode(true);
        menuView.setTag("menuView");
        CircleMenuLayout circle_menu_layout = menuView.findViewById(R.id.circle_menu_layout);
        String[] sts = new String[]{"结束投影", "截图批注", "结束同屏", "加入同屏",
                "发起同屏", "发起投影", "会议笔记", "呼叫服务"};
        int[] dras = new int[]{
                R.drawable.menu_stop_pro_status, R.drawable.menu_screenshot_status,
                R.drawable.menu_stop_screen_status, R.drawable.menu_join_screen_status,
                R.drawable.menu_start_screen_status, R.drawable.menu_start_pro_status,
                R.drawable.menu_meet_note_status, R.drawable.menu_call_service_status,
        };
        circle_menu_layout.setMenuItemIconsAndTexts(dras, sts);
        circle_menu_layout.setOnMenuItemClickListener(new CircleMenuLayout.OnMenuItemClickListener() {
            @Override
            public void itemClick(View view, int pos) {
                switch (pos) {
                    //结束投影
                    case 0: {
                        if (Constant.hasPermission(permission_code_projection)) {
                            showProView(2);
                        } else {
                            ToastUtils.showShort(R.string.err_NoPermission);
                        }
                        break;
                    }
                    //截图批注
                    case 1: {
                        screenshot();
                        break;
                    }
                    //结束同屏
                    case 2: {
                        if (Constant.hasPermission(permission_code_screen)) {
                            showScreenView(2);
                        } else {
                            ToastUtils.showShort(R.string.err_NoPermission);
                        }
                        break;
                    }
                    //加入同屏
                    case 3: {
                        presenter.queryCanJoin();
                        showJoinView();
                        break;
                    }
                    //发起同屏
                    case 4: {
                        if (Constant.hasPermission(permission_code_screen)) {
                            showScreenView(1);
                        } else {
                            ToastUtils.showShort(R.string.err_NoPermission);
                        }
                        break;
                    }
                    //发起投影
                    case 5: {
                        if (Constant.hasPermission(permission_code_projection)) {
                            showProView(1);
                        } else {
                            ToastUtils.showShort(R.string.err_NoPermission);
                        }
                        break;
                    }
                    //会议笔记
                    case 6: {
                        showNoteView(menuView, saveNoteContent);
                        break;
                    }
                    //呼叫服务
                    case 7: {
                        showServiceView();
                        break;
                    }
                    default:
                        break;
                }
            }

            @Override
            public void itemCenterClick(View view) {
                showPop(menuView, hoverButton, mParams);
            }
        });
//        CustomMenu custom_menu = menuView.findViewById(R.id.custom_menu);
//        custom_menu.setFocusable(true);
//        custom_menu.setFocusableInTouchMode(true);
//        custom_menu.setListener(index -> {
//            switch (index) {
//                //发起同屏
//                case 0: {
//                    if (Constant.hasPermission(permission_code_screen)) {
//                        showScreenView(1);
//                    } else {
//                        ToastUtils.showShort(R.string.err_NoPermission);
//                    }
//                    break;
//                }
//                //结束同屏
//                case 1: {
//                    if (Constant.hasPermission(permission_code_screen)) {
//                        showScreenView(2);
//                    } else {
//                        ToastUtils.showShort(R.string.err_NoPermission);
//                    }
//                    break;
//                }
//                //加入同屏
//                case 2: {
//                    presenter.queryCanJoin();
//                    showJoinView();
//                    break;
//                }
//                //发起投影
//                case 3: {
//                    if (Constant.hasPermission(permission_code_projection)) {
//                        showProView(1);
//                    } else {
//                        ToastUtils.showShort(R.string.err_NoPermission);
//                    }
//                    break;
//                }
//                //结束投影
//                case 4: {
//                    if (Constant.hasPermission(permission_code_projection)) {
//                        showProView(2);
//                    } else {
//                        ToastUtils.showShort(R.string.err_NoPermission);
//                    }
//                    break;
//                }
//                //呼叫服务
//                case 5: {
//                    showServiceView();
//                    break;
//                }
//                //截图批注
//                case 6: {
//                    screenshot();
//                    break;
//                }
//                //会议笔记
//                case 7: {
//
//                    break;
//                }
//                //返回
//                case 8: {
//                    showPop(menuView, hoverButton, mParams);
//                    break;
//                }
//                default:
//                    break;
//            }
//        });
        showPop(hoverButton, menuView, wrapParams);
    }

    @Override
    public void showNoteView(String content) {
        showNoteView(hoverButton, content);
    }

    private String saveNoteContent = "";

    //会议笔记视图
    private void showNoteView(View removeView, String content) {
        saveNoteContent = content;
        noteView = LayoutInflater.from(this).inflate(R.layout.fab_note_view, null);
        noteView.setTag("noteView");
        CustomBaseViewHolder.NoteViewHolder holder = new CustomBaseViewHolder.NoteViewHolder(noteView);
        noteViewHolderEvent(holder);
        showPop(removeView, noteView);
    }

    private void noteViewHolderEvent(CustomBaseViewHolder.NoteViewHolder holder) {
        holder.btn_back.setOnClickListener(v -> {
            saveNoteContent = holder.edt_note.getText().toString();
            showPop(noteView, hoverButton, mParams);
        });
        holder.iv_min.setOnClickListener(v -> {
            saveNoteContent = holder.edt_note.getText().toString();
            showPop(noteView, hoverButton, mParams);
        });
        holder.edt_note.setText(saveNoteContent);
        holder.edt_note.setSelection(saveNoteContent.length());
        holder.iv_close.setOnClickListener(v -> {
            saveNoteContent = "";
            showPop(noteView, hoverButton, mParams);
        });
        holder.btn_export_note.setOnClickListener(v -> {
            saveNoteContent = holder.edt_note.getText().toString();
            showPop(noteView, hoverButton, mParams);
            EventBus.getDefault().post(new EventMessage.Builder().type(EventType.BUS_CHOOSE_NOTE_FILE).build());
        });
        holder.btn_save_local.setOnClickListener(v -> {
            String content = holder.edt_note.getText().toString();
            String filePath = Constant.file_dir + "会议笔记.txt";
            File file = new File(filePath);
            FileUtils.createOrExistsFile(file);
            if (FileUtil.writeFileFromString(file, content)) {
                ToastUtils.showShort(R.string.save_successful);
            }
        });
    }

    private void screenshot() {
        if (menuViewIsShowing) {
            wm.removeView(menuView);
            menuViewIsShowing = false;
        }
        new Handler().postDelayed(() -> {
            startVirtual();
            new Handler().postDelayed(() -> {
                startScreen();
            }, 500);//延迟因为ImageReader需要准备时间
        }, 500);
    }

    private void startVirtual() {
        LogUtil.i(TAG, "startVirtual GlobalValue.screen_width=" + GlobalValue.screen_width +
                ", GlobalValue.screen_height=" + GlobalValue.screen_height +
                ", mScreenDensity=" + mScreenDensity);
        try {
            Surface surface = mImageReader.getSurface();
            mVirtualDisplay = mMediaProjection.createVirtualDisplay("screen-mirror",
                    GlobalValue.screen_width, GlobalValue.screen_height,
//                    windowWidth, windowHeight,
                    mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                    surface, null, null);
            LogUtil.d(TAG, "virtual displayed surface是否为null： " + (surface == null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startScreen() {
        Image image = mImageReader.acquireLatestImage();
        LogUtil.e(TAG, "startScreen :  image 为null --> " + (image == null));
        if (image == null) {
            hoverButtonIsShowing = true;
            wm.addView(hoverButton, mParams);
            return;
        }
        int width = image.getWidth();
        int height = image.getHeight();
        final Image.Plane[] planes = image.getPlanes();
        final ByteBuffer buffer = planes[0].getBuffer();
        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * width;
        Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(buffer);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
        image.close();
        LogUtil.d(TAG, "image data captured bitmap是否为空：" + (bitmap == null));
        //截图完毕，重新显示悬浮按钮
        hoverButtonIsShowing = true;
        wm.addView(hoverButton, mParams);
        if (bitmap != null) {
            screenShotBitmap = bitmap;
            if (isDrawing) {
                EventBus.getDefault().post(new EventMessage.Builder().type(EventType.BUS_SCREEN_SHOT).build());
            } else {
                Intent intent = new Intent(cxt, MeetingActivity.class);
                intent.putExtra("draw", "draw");
                startActivity(intent);
            }
        }
    }

    //服务视图
    private void showServiceView() {
        serviceView = LayoutInflater.from(cxt).inflate(R.layout.wm_service_view, null);
        serviceView.setTag("serviceView");
        CustomBaseViewHolder.ServiceViewHolder serviceViewHolder = new CustomBaseViewHolder.ServiceViewHolder(serviceView);
        serviceViewHolderEvent(serviceViewHolder);
        showPop(menuView, serviceView);
    }

    //服务视图事件
    private void serviceViewHolderEvent(CustomBaseViewHolder.ServiceViewHolder holder) {
        holder.wm_service_close.setOnClickListener(v -> showPop(serviceView, hoverButton, mParams));
        holder.wm_service_pager.setOnClickListener(v -> holder.wm_service_edt.setText(cxt.getResources().getString(R.string.service_pager)));
        holder.wm_service_pen.setOnClickListener(v -> holder.wm_service_edt.setText(cxt.getResources().getString(R.string.service_pen)));
        holder.wm_service_tea.setOnClickListener(v -> holder.wm_service_edt.setText(cxt.getResources().getString(R.string.service_tea)));
        holder.wm_service_calculate.setOnClickListener(v -> holder.wm_service_edt.setText(cxt.getResources().getString(R.string.service_calculate)));
        holder.wm_service_waiter.setOnClickListener(v -> holder.wm_service_edt.setText(cxt.getResources().getString(R.string.service_waiter)));
        holder.wm_service_clean.setOnClickListener(v -> holder.wm_service_edt.setText(cxt.getResources().getString(R.string.service_clean)));
        holder.wm_service_send.setOnClickListener(v -> {
            String msg = holder.wm_service_edt.getText().toString().trim();
            if (!msg.isEmpty()) {
                List<Integer> arr = new ArrayList<>();
                arr.add(0);//会议服务类请求则为 0
                jni.sendChatMessage(msg, InterfaceMacro.Pb_MeetIMMSG_TYPE.Pb_MEETIM_CHAT_Other_VALUE, arr);
            }
        });
    }

    //投影视图
    private void showProView(int type) {
        proView = LayoutInflater.from(cxt).inflate(R.layout.wm_pro_view, null);
        proView.setTag("proView");
        CustomBaseViewHolder.ProViewHolder proViewHolder = new CustomBaseViewHolder.ProViewHolder(proView);
        proViewHolderEvent(proViewHolder, type);
        showPop(menuView, proView);
    }

    private void proViewHolderEvent(CustomBaseViewHolder.ProViewHolder holder, int type) {
        holder.wm_pro_mandatory.setVisibility(type == 1 ? View.VISIBLE : View.INVISIBLE);
        holder.wm_pro_title.setText(type == 1 ? cxt.getString(R.string.launch_pro_title) : cxt.getString(R.string.stop_pro_title));
        holder.wm_pro_launch_pro.setText(type == 1 ? cxt.getString(R.string.launch_pro) : cxt.getString(R.string.stop_pro));
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
        holder.wm_pro_launch_pro.setOnClickListener(v -> {
            List<Integer> ids = projectorAdapter.getChooseIds();
            if (ids.isEmpty()) {
                ToastUtils.showShort(cxt.getString(R.string.please_choose_projector_first));
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
                ToastUtils.showShort(cxt.getString(R.string.please_choose_res_first));
                return;
            }
            if (type == 1) {//发起投影
                boolean isMandatory = holder.wm_pro_mandatory.isChecked();
                int triggeruserval = isMandatory ? InterfaceMacro.Pb_TriggerUsedef.Pb_EXCEC_USERDEF_FLAG_NOCREATEWINOPER_VALUE
                        : InterfaceMacro.Pb_TriggerUsedef.Pb_EXCEC_USERDEF_FLAG_ZERO_VALUE;
                jni.streamPlay(GlobalValue.localDeviceId, 2, triggeruserval, res, ids);
            } else {//结束投影
                jni.stopResourceOperate(RESOURCE_ID_0, ids);
            }
            showPop(proView, hoverButton, mParams);
        });
        holder.wm_pro_cancel.setOnClickListener(v -> showPop(proView, hoverButton, mParams));
        holder.wm_pro_all.performClick();
    }

    //加入同屏视图
    private void showJoinView() {
        joinView = LayoutInflater.from(cxt).inflate(R.layout.wm_screen_view, null);
        joinView.setTag("joinView");
        CustomBaseViewHolder.ScreenViewHolder joinViewHolder = new CustomBaseViewHolder.ScreenViewHolder(joinView);
        joinViewHolderEvent(joinViewHolder);
        showPop(menuView, joinView);
    }

    //加入同屏视图事件
    private void joinViewHolderEvent(CustomBaseViewHolder.ScreenViewHolder holder) {
        holder.wm_screen_mandatory.setVisibility(View.INVISIBLE);
        holder.wm_screen_cb_attendee.setVisibility(View.INVISIBLE);
        holder.wm_screen_cb_projector.setVisibility(View.INVISIBLE);
        holder.wm_screen_title.setText(cxt.getString(R.string.choose_join_screen));
        holder.wm_screen_launch.setText(cxt.getString(R.string.join_screen));
        holder.wm_screen_rv_attendee.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        holder.wm_screen_rv_attendee.setAdapter(joinMemberAdapter);
        joinMemberAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                joinMemberAdapter.choose(presenter.canJoinMembers.get(position).getDevceid());
            }
        });
        holder.wm_screen_rv_projector.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        holder.wm_screen_rv_projector.setAdapter(joinProAdapter);
        joinProAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                joinProAdapter.choose(presenter.canJoinPros.get(position).getResPlay().getDevceid());
            }
        });
        holder.wm_screen_cancel.setOnClickListener(v -> showPop(joinView, hoverButton, mParams));
        //加入同屏
        holder.wm_screen_launch.setOnClickListener(v -> {
            List<Integer> ids = new ArrayList<>();
            int chooseId = joinMemberAdapter.getChooseId();
            if (chooseId != -1) {
                ids.add(chooseId);
            }
            int chooseId1 = joinProAdapter.getChooseId();
            if (chooseId1 != -1) {
                ids.add(chooseId1);
            }
            if (ids.size() > 1) {
                ToastUtils.showShort(R.string.can_only_choose_one);
            } else if (ids.isEmpty()) {
                ToastUtils.showShort(R.string.err_target_NotNull);
            } else {
                List<Integer> res = new ArrayList<>();
                res.add(RESOURCE_ID_0);
                List<Integer> devs = new ArrayList<>();
                devs.add(GlobalValue.localDeviceId);
                jni.streamPlay(ids.get(0), 2, 0, res, devs);
                showPop(joinView, hoverButton, mParams);
            }
        });
    }

    //同屏视图 type =1发起同屏，=2结束同屏
    private void showScreenView(int type) {
        //同屏视图
        screenView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.wm_screen_view, null);
        screenView.setTag("screenView");
        CustomBaseViewHolder.ScreenViewHolder screenViewHolder = new CustomBaseViewHolder.ScreenViewHolder(screenView);
        screenViewHolderEvent(screenViewHolder, type);
        showPop(menuView, screenView);
    }

    private void screenViewHolderEvent(CustomBaseViewHolder.ScreenViewHolder holder, int type) {
        if (type == 1) {
            holder.wm_screen_mandatory.setVisibility(View.VISIBLE);
            holder.wm_screen_launch.setText(cxt.getString(R.string.launch_screen));
            holder.wm_screen_title.setText(cxt.getString(R.string.launch_screen_title));
        } else if (type == 2) {
            holder.wm_screen_mandatory.setVisibility(View.INVISIBLE);
            holder.wm_screen_launch.setText(cxt.getString(R.string.stop_screen));
            holder.wm_screen_title.setText(cxt.getString(R.string.stop_screen_title));
        }
        holder.wm_screen_cancel.setOnClickListener(v -> showPop(screenView, hoverButton, mParams));
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
        //发起/结束同屏
        holder.wm_screen_launch.setOnClickListener(v -> {
            List<Integer> ids = memberAdapter.getChooseIds();
            ids.addAll(projectorAdapter.getChooseIds());
            if (ids.isEmpty()) {
                ToastUtils.showShort(R.string.err_target_NotNull);
            } else {
                if (type == 1) {//发起同屏
                    int triggeruserval = 0;
                    if (holder.wm_screen_mandatory.isChecked()) {//是否强制同屏
                        triggeruserval = InterfaceMacro.Pb_TriggerUsedef.Pb_EXCEC_USERDEF_FLAG_NOCREATEWINOPER_VALUE;
                    }
                    List<Integer> temps = new ArrayList<>();
                    temps.add(RESOURCE_ID_0);
                    jni.streamPlay(GlobalValue.localDeviceId, 2, triggeruserval, temps, ids);
                } else {//结束同屏
                    jni.stopResourceOperate(RESOURCE_ID_0, ids);
                }
                showPop(screenView, hoverButton, mParams);
            }
        });
        //默认全部选中
        holder.wm_screen_cb_attendee.performClick();
        holder.wm_screen_cb_projector.performClick();
    }

    /**
     * 展示新的弹框
     *
     * @param removeView 正在展示的view
     * @param addView    需要替换的view
     * @param params     params配置
     */
    private void showPop(View removeView, View addView, WindowManager.LayoutParams params) {
        wm.removeView(removeView);
        wm.addView(addView, params);
        setIsShowing(removeView, addView);
    }

    private void showPop(View removeView, View addView) {
        wm.removeView(removeView);
        wm.addView(addView, defaultParams);
        setIsShowing(removeView, addView);
    }

    private void delAllView() {
        if (hoverButtonIsShowing) wm.removeView(hoverButton);
        if (menuViewIsShowing) wm.removeView(menuView);
        if (serviceViewIsShowing) wm.removeView(serviceView);
        if (screenViewIsShowing) wm.removeView(screenView);
        if (joinViewIsShowing) wm.removeView(joinView);
        if (proViewIsShowing) wm.removeView(proView);
        if (voteViewIsShowing) wm.removeView(voteView);
        if (voteEnsureViewIsShowing) wm.removeView(voteEnsureView);
        if (noteViewIsShowing) wm.removeView(noteView);
    }

    private void setIsShowing(View remove, View add) {
        String removeTag = (String) remove.getTag();
        String addTag = (String) add.getTag();
        switch (removeTag) {
            case "hoverButton":
                hoverButtonIsShowing = false;
                break;
            case "menuView":
                menuViewIsShowing = false;
                break;
            case "serviceView":
                serviceViewIsShowing = false;
                break;
            case "screenView":
                screenViewIsShowing = false;
                break;
            case "joinView":
                joinViewIsShowing = false;
                break;
            case "proView":
                proViewIsShowing = false;
                break;
            case "voteView":
                voteViewIsShowing = false;
                break;
            case "voteEnsureView":
                voteEnsureViewIsShowing = false;
                break;
            case "noteView":
                noteViewIsShowing = false;
                break;
        }
        switch (addTag) {
            case "hoverButton":
                hoverButtonIsShowing = true;
                break;
            case "menuView":
                menuViewIsShowing = true;
                break;
            case "serviceView":
                serviceViewIsShowing = true;
                break;
            case "screenView":
                screenViewIsShowing = true;
                break;
            case "joinView":
                joinViewIsShowing = true;
                break;
            case "proView":
                proViewIsShowing = true;
                break;
            case "voteView":
                voteViewIsShowing = true;
                break;
            case "voteEnsureView":
                voteEnsureViewIsShowing = true;
                break;
            case "noteView":
                noteViewIsShowing = true;
                break;
        }
    }

    @Override
    public void onDestroy() {
        LogUtil.i(TAG, "onDestroy ");
        try {
            delAllView();
        } catch (Exception e) {
            e.printStackTrace();
        }
        presenter.onDestroy();
        super.onDestroy();
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

    @Override
    public void notifyJoinAdapter() {
        if (joinMemberAdapter != null) {
            joinMemberAdapter.notifyDataSetChanged();
            joinMemberAdapter.notifyChecks();
        }
        if (joinProAdapter != null) {
            joinProAdapter.notifyDataSetChanged();
            joinProAdapter.notifyChecks();
        }
    }

    private AlertDialog bulletDialog;
    private TextView bullet_title, bullet_content;

    @Override
    public void showBulletWindow(InterfaceBullet.pbui_Item_BulletDetailInfo bullet) {
        if (bulletDialog != null && bulletDialog.isShowing()) {
            bullet_title.setText(bullet.getTitle().toStringUtf8());
            bullet_content.setText(bullet.getContent().toStringUtf8());
            return;
        }
        bulletDialog = DialogUtil.createDialog(cxt, R.layout.pop_receive_bullet, false);
        bullet_title = bulletDialog.findViewById(R.id.bullet_title);
        bullet_content = bulletDialog.findViewById(R.id.bullet_content);
        bullet_title.setText(bullet.getTitle().toStringUtf8());
        bullet_content.setText(bullet.getContent().toStringUtf8());
        bulletDialog.findViewById(R.id.btn_cancel).setVisibility(View.GONE);
        bulletDialog.findViewById(R.id.btn_ensure).setOnClickListener(v -> {
            bulletDialog.dismiss();
        });
        bulletDialog.findViewById(R.id.iv_close).setOnClickListener(v -> {
            bulletDialog.dismiss();
        });
    }

    @Override
    public void closeBulletWindow() {
        if (bulletDialog != null && bulletDialog.isShowing()) {
            bulletDialog.dismiss();
        }
    }

    @Override
    public void showView(final int inviteflag, final int operdeviceid) {
        //是否有询问
        boolean isAsk = (inviteflag & InterfaceDevice.Pb_DeviceInviteFlag.Pb_DEVICE_INVITECHAT_FLAG_ASK_VALUE)
                == InterfaceDevice.Pb_DeviceInviteFlag.Pb_DEVICE_INVITECHAT_FLAG_ASK_VALUE;
        if (!isAsk) {//没有询问，对方强制要求加入
            if (!isChatingOpened) {
                startActivity(new Intent(this, ChatVideoActivity.class)
                        .putExtra(Constant.EXTRA_INVITE_FLAG, inviteflag)
                        .putExtra(Constant.EXTRA_OPERATING_DEVICE_ID, operdeviceid)
                        .setFlags(FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
            } else {
                EventBus.getDefault().post(new EventMessage.Builder()
                        .type(EventType.BUS_CHAT_STATE)
                        .objects(inviteflag, operdeviceid)
                        .build());
            }
//            showOpenCamera(inviteflag, operdeviceid);
            //强制的
            int flag = inviteflag | InterfaceDevice.Pb_DeviceInviteFlag.Pb_DEVICE_INVITECHAT_FLAG_DEAL_VALUE;
            LogUtil.d(TAG, "强制的则直接同意：" + flag);
            jni.replyDeviceIntercom(operdeviceid, flag);
            return;
        }
        //有进行询问
        DialogUtil.createDialog(cxt, getString(R.string.deviceIntercom_Inform_title, presenter.getMemberNameByDevid(operdeviceid)),
                getString(R.string.agree), getString(R.string.reject), new DialogUtil.onDialogClickListener() {
                    @Override
                    public void positive(DialogInterface dialog) {
                        if (!isChatingOpened) {
                            startActivity(new Intent(cxt, ChatVideoActivity.class)
                                    .putExtra(Constant.EXTRA_INVITE_FLAG, inviteflag)
                                    .putExtra(Constant.EXTRA_OPERATING_DEVICE_ID, operdeviceid)
                                    .setFlags(FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                        } else {
                            EventBus.getDefault().post(new EventMessage.Builder().type(EventType.BUS_CHAT_STATE).objects(inviteflag, operdeviceid).build());
                        }
//                        showOpenCamera(inviteflag, operdeviceid);
                        int flag = inviteflag | InterfaceDevice.Pb_DeviceInviteFlag.Pb_DEVICE_INVITECHAT_FLAG_DEAL_VALUE;
                        LogUtil.d(TAG, "showView -->" + "同意：" + flag);
                        jni.replyDeviceIntercom(operdeviceid, flag);
                        dialog.dismiss();
                    }

                    @Override
                    public void negative(DialogInterface dialog) {
                        LogUtil.d(TAG, "showView -->" + "拒绝：" + inviteflag);
                        jni.replyDeviceIntercom(operdeviceid, inviteflag);
                        dialog.dismiss();
                    }

                    @Override
                    public void dismiss(DialogInterface dialog) {

                    }
                });
    }

    private View cameraView;

    //打开摄像头视图
    @Override
    public void showOpenCamera(int inviteflag, int operdeviceid) {
        cameraView = LayoutInflater.from(cxt).inflate(R.layout.wm_camera_choose, null);
        cameraView.findViewById(R.id.wm_camera_pre).setOnClickListener(v -> {
            if (AppUtil.checkCamera(cxt, 1)) {
                Intent intent = new Intent(cxt, CameraActivity.class);
                intent.putExtra(Constant.EXTRA_CAMERA_TYPE, 1);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                cxt.startActivity(intent);
                wm.removeView(cameraView);
                int flag = inviteflag | InterfaceDevice.Pb_DeviceInviteFlag.Pb_DEVICE_INVITECHAT_FLAG_DEAL_VALUE;
                LogUtil.d(TAG, "强制的则直接同意：" + flag);
                jni.replyDeviceIntercom(operdeviceid, flag);
            } else {
                ToastUtils.showShort(R.string.no_front_camera);
            }
        });
        cameraView.findViewById(R.id.wm_camera_back).setOnClickListener(v -> {
            if (AppUtil.checkCamera(cxt, 0)) {
                Intent intent = new Intent(cxt, CameraActivity.class);
                intent.putExtra(Constant.EXTRA_CAMERA_TYPE, 0);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                cxt.startActivity(intent);
                wm.removeView(cameraView);
                int flag = inviteflag | InterfaceDevice.Pb_DeviceInviteFlag.Pb_DEVICE_INVITECHAT_FLAG_DEAL_VALUE;
                LogUtil.d(TAG, "强制的则直接同意：" + flag);
                jni.replyDeviceIntercom(operdeviceid, flag);
            } else {
                ToastUtils.showShort(R.string.no_rear_camera);
            }
        });
        cameraView.findViewById(R.id.wm_camera_reject).setOnClickListener(v -> {
            wm.removeView(cameraView);
        });
        wm.addView(cameraView, wrapParams);
    }

    boolean dialogIsShowing = false;
    private LinkedList<InterfaceDevice.pbui_Type_MeetRequestPrivilegeNotify> permissionsRequests = new LinkedList<>();//存放收到的申请权限信息

    @Override
    public void applyPermissionsInform(InterfaceDevice.pbui_Type_MeetRequestPrivilegeNotify info) {
        if (dialogIsShowing) {
            permissionsRequests.addLast(info);
            return;
        }
        dialogIsShowing = true;
        int deviceid = info.getDeviceid();
        int memberid = info.getMemberid();
        int privilege = info.getPrivilege();
        DialogUtil.createDialog(cxt, cxt.getString(R.string.apply_permissions, presenter.getMemberNameById(memberid)),
                cxt.getString(R.string.agree), cxt.getString(R.string.reject), new DialogUtil.onDialogClickListener() {
                    @Override
                    public void positive(DialogInterface dialog) {
                        jni.revertAttendPermissionsRequest(deviceid, 1);
                        dialog.dismiss();
                    }

                    @Override
                    public void negative(DialogInterface dialog) {
                        jni.revertAttendPermissionsRequest(deviceid, 0);
                        dialog.dismiss();
                    }

                    @Override
                    public void dismiss(DialogInterface dialog) {
                        dialogIsShowing = false;
                        if (!permissionsRequests.isEmpty()) {
                            InterfaceDevice.pbui_Type_MeetRequestPrivilegeNotify item = permissionsRequests.removeFirst();
                            if (item != null) {
                                LogUtil.d(TAG, "dismiss -->" + "处理了一个还有下一个申请要处理");
                                applyPermissionsInform(item);
                            }
                        }
                    }
                });
    }

    @Override
    public void showVoteView(InterfaceVote.pbui_Item_MeetOnVotingDetailInfo info) {
//        if (voteViewIsShowing) {
//            LogUtil.e(TAG, "showVoteView -->" + "已经有投票正在展示中");
//            return;
//        }
//        currentVoteId = info.getVoteid();
//        voteView = LayoutInflater.from(cxt).inflate(R.layout.wm_vote_view, null);
//        voteView.setTag("voteView");
//        CustomBaseViewHolder.VoteViewHolder voteViewHolder = new CustomBaseViewHolder.VoteViewHolder(voteView);
//        voteViewHolderEvent(voteViewHolder, info);
//        voteViewIsShowing = true;
//        wm.addView(voteView, fullParams);
    }

    @Override
    public void closeVoteView() {
        if (voteDialog != null && voteDialog.isShowing()) {
            voteDialog.dismiss();
            currentVoteId = -1;
            currentChooseCount = 0;
        }
//        if (voteViewIsShowing) {
//            wm.removeView(voteView);
//            voteViewIsShowing = false;
//            currentVoteId = -1;
//            currentChooseCount = 0;
//        }
//        if (voteEnsureViewIsShowing) {
//            wm.removeView(voteEnsureView);
//            voteEnsureViewIsShowing = false;
//        }
    }

    public static class VoteViewHolder {
        public ImageView iv_close;
        public RelativeLayout top_layout;
        public TextView tv_title;
        public TextView tv_time;
        public Chronometer chronometer;
        public LinearLayout countdown_view;
        public CheckBox cb_a;
        public CheckBox cb_b;
        public CheckBox cb_c;
        public CheckBox cb_d;
        public CheckBox cb_e;
        public Button btn_ensure;

        public VoteViewHolder(AlertDialog rootView) {
            this.iv_close = (ImageView) rootView.findViewById(R.id.iv_close);
            this.top_layout = (RelativeLayout) rootView.findViewById(R.id.top_layout);
            this.tv_title = (TextView) rootView.findViewById(R.id.tv_title);
            this.tv_time = (TextView) rootView.findViewById(R.id.tv_time);
            this.chronometer = (Chronometer) rootView.findViewById(R.id.chronometer);
            this.countdown_view = (LinearLayout) rootView.findViewById(R.id.countdown_view);
            this.cb_a = (CheckBox) rootView.findViewById(R.id.cb_a);
            this.cb_b = (CheckBox) rootView.findViewById(R.id.cb_b);
            this.cb_c = (CheckBox) rootView.findViewById(R.id.cb_c);
            this.cb_d = (CheckBox) rootView.findViewById(R.id.cb_d);
            this.cb_e = (CheckBox) rootView.findViewById(R.id.cb_e);
            this.btn_ensure = (Button) rootView.findViewById(R.id.btn_ensure);
        }
    }


    private AlertDialog voteDialog;

    @Override
    public void showVoteWindow(InterfaceVote.pbui_Item_MeetOnVotingDetailInfo info) {
        if (voteDialog != null && voteDialog.isShowing()) {
            LogUtil.e(TAG, "voteViewHolderEvent --> 已经有投票正在展示");
            return;
        }
        currentVoteId = info.getVoteid();
        voteDialog = DialogUtil.createDialog(cxt, R.layout.pop_receive_vote, false);
        VoteViewHolder voteViewHolder = new VoteViewHolder(voteDialog);
        voteViewHolderEvent(voteViewHolder, info);
        voteDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                LogUtil.i(TAG, "onDismiss 投票窗口关闭");
            }
        });
    }

    //投票视图事件
    private void voteViewHolderEvent(VoteViewHolder holder, InterfaceVote.pbui_Item_MeetOnVotingDetailInfo info) {
        voteTimeouts = info.getTimeouts();
        String voteMode = getVoteMode(info);
        holder.tv_title.setText(info.getContent().toStringUtf8() + voteMode);
        int maintype = info.getMaintype();
        int selectItem = 0 | Constant.PB_VOTE_SELFLAG_CHECKIN;
        jni.submitVoteResult(1, currentVoteId, selectItem);
        LogUtil.d(TAG, "当前倒计时 -->" + voteTimeouts);
        boolean isVote = maintype == InterfaceMacro.Pb_MeetVoteType.Pb_VOTE_MAINTYPE_vote_VALUE;
        if (voteTimeouts <= 0) {
            holder.countdown_view.setVisibility(View.INVISIBLE);
        } else {
            holder.countdown_view.setVisibility(View.VISIBLE);
            holder.chronometer.setBase(SystemClock.elapsedRealtime());
            holder.chronometer.start();
            holder.chronometer.setOnChronometerTickListener(c -> {
                voteTimeouts--;
                if (voteTimeouts <= 0) {
                    if (isVote) {
                        //投票倒计时结束还没有进行选择，则默认弃权
                        jni.submitVoteResult(info.getSelectcount(), currentVoteId, 4);
                    }
                    c.stop();
                    closeVoteView();
                } else {
                    String countdown = DateUtil.countdown(voteTimeouts);
                    c.setText(countdown);
                }
            });
        }
        initCheckBox(holder, info);
        chooseEvent(holder.cb_a);
        chooseEvent(holder.cb_b);
        chooseEvent(holder.cb_c);
        chooseEvent(holder.cb_d);
        chooseEvent(holder.cb_e);
        holder.iv_close.setOnClickListener(v -> closeVoteView());
        holder.btn_ensure.setOnClickListener(v -> {
            int answer = 0;
            if (holder.cb_a.isChecked()) answer += 1;
            if (holder.cb_b.isChecked()) answer += 2;
            if (holder.cb_c.isChecked()) answer += 4;
            if (holder.cb_d.isChecked()) answer += 8;
            if (holder.cb_e.isChecked()) answer += 16;
            if (answer != 0) {
                jni.submitVoteResult(info.getSelectcount(), currentVoteId, answer);
                closeVoteView();
            } else {
                ToastUtils.showShort(R.string.please_choose_answer_first);
            }
        });
    }

    private void initCheckBox(VoteViewHolder holder, InterfaceVote.pbui_Item_MeetOnVotingDetailInfo info) {
        holder.cb_a.setVisibility(View.VISIBLE);
        holder.cb_b.setVisibility(View.VISIBLE);
        holder.cb_c.setVisibility(View.VISIBLE);
        holder.cb_d.setVisibility(View.VISIBLE);
        holder.cb_e.setVisibility(View.VISIBLE);
        int selectcount = info.getSelectcount();//有效选项
        switch (selectcount) {
            case 2:
                holder.cb_c.setVisibility(View.GONE);
                holder.cb_d.setVisibility(View.GONE);
                holder.cb_e.setVisibility(View.GONE);
                break;
            case 3:
                holder.cb_d.setVisibility(View.GONE);
                holder.cb_e.setVisibility(View.GONE);
                break;
            case 4:
                holder.cb_e.setVisibility(View.GONE);
                break;
        }
        List<ByteString> textList = info.getTextList();
        for (int i = 0; i < textList.size(); i++) {
            String s = textList.get(i).toStringUtf8();
            switch (i) {
                case 0:
                    holder.cb_a.setText(s);
                    break;
                case 1:
                    holder.cb_b.setText(s);
                    break;
                case 2:
                    holder.cb_c.setText(s);
                    break;
                case 3:
                    holder.cb_d.setText(s);
                    break;
                case 4:
                    holder.cb_e.setText(s);
                    break;
            }
        }
    }

    private void chooseEvent(CheckBox checkBox) {
        checkBox.setOnClickListener(v -> {
            boolean checked = checkBox.isChecked();
            if (checked) {//将要设置成选中
                if (currentChooseCount < maxChooseCount) {
                    currentChooseCount++;
                    checkBox.setChecked(true);
                } else {
                    checkBox.setChecked(false);
                    ToastUtils.showShort(cxt.getString(R.string.max_choose_, String.valueOf(maxChooseCount)));
                }
            } else {
                currentChooseCount--;
                checkBox.setChecked(false);
            }
        });
    }

    private String getVoteMode(InterfaceVote.pbui_Item_MeetOnVotingDetailInfo info) {
        String voteMode = "(";
        switch (info.getType()) {
            case InterfaceMacro.Pb_MeetVote_SelType.Pb_VOTE_TYPE_SINGLE_VALUE://单选
                voteMode += cxt.getString(R.string.vote_type_single) + "，";
                maxChooseCount = 1;
                break;
            case InterfaceMacro.Pb_MeetVote_SelType.Pb_VOTE_TYPE_4_5_VALUE://5选4
                voteMode += cxt.getString(R.string.vote_type_4_5) + "，";
                maxChooseCount = 4;
                break;
            case InterfaceMacro.Pb_MeetVote_SelType.Pb_VOTE_TYPE_3_5_VALUE:
                voteMode += cxt.getString(R.string.vote_type_3_5) + "，";
                maxChooseCount = 3;
                break;
            case InterfaceMacro.Pb_MeetVote_SelType.Pb_VOTE_TYPE_2_5_VALUE:
                voteMode += cxt.getString(R.string.vote_type_2_5) + "，";
                maxChooseCount = 2;
                break;
            case InterfaceMacro.Pb_MeetVote_SelType.Pb_VOTE_TYPE_2_3_VALUE:
                voteMode += cxt.getString(R.string.vote_type_2_3) + "，";
                maxChooseCount = 2;
                break;
            case InterfaceMacro.Pb_MeetVote_SelType.Pb_VOTE_TYPE_MANY_VALUE:
                voteMode += cxt.getString(R.string.vote_type_multi) + "，";
                maxChooseCount = info.getSelectcount() - 1;
                break;
        }
        if (info.getMode() == InterfaceMacro.Pb_MeetVoteMode.Pb_VOTEMODE_agonymous_VALUE) {//匿名
            voteMode += cxt.getString(R.string.anonymous);
        } else {
            voteMode += cxt.getString(R.string.notation);
        }
        voteMode += "）";
        return voteMode;
    }

    //是否提交投票视图
    private void showEnsureView(int answer, InterfaceVote.pbui_Item_MeetOnVotingDetailInfo vote) {
        voteEnsureView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.vote_ensure_view, null);
        voteEnsureView.setTag("voteEnsureView");
        CustomBaseViewHolder.SubmitViewHolder holder = new CustomBaseViewHolder.SubmitViewHolder(voteEnsureView);
        SubmitViewHolderEvent(holder, answer, vote);
        wm.addView(voteEnsureView, wrapParams);
        voteEnsureViewIsShowing = true;
    }

    //是否提交投票视图事件
    private void SubmitViewHolderEvent(CustomBaseViewHolder.SubmitViewHolder holder, int answer, InterfaceVote.pbui_Item_MeetOnVotingDetailInfo vote) {
        holder.vote_submit_ensure.setOnClickListener(v -> {
            jni.submitVoteResult(vote.getSelectcount(), currentVoteId, answer);
            wm.removeView(voteEnsureView);
            voteEnsureViewIsShowing = false;
            wm.removeView(voteView);
            voteViewIsShowing = false;
            currentVoteId = -1;
        });
        holder.vote_submit_cancel.setOnClickListener(v -> {
            wm.removeView(voteEnsureView);
            voteEnsureViewIsShowing = false;
        });
    }

}