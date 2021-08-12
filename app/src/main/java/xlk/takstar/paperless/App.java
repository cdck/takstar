package xlk.takstar.paperless;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.blankj.utilcode.util.LogUtils;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsDownloader;
import com.tencent.smtt.sdk.TbsListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cat.ereza.customactivityoncrash.config.CaocConfig;
import xlk.takstar.paperless.fragment.agenda.AgendaFragment;
import xlk.takstar.paperless.launch.LaunchActivity;
import xlk.takstar.paperless.main.MainActivity;
import xlk.takstar.paperless.meet.MeetingActivity;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.model.EventType;
import xlk.takstar.paperless.model.EventMessage;
import xlk.takstar.paperless.model.GlobalValue;
import xlk.takstar.paperless.service.BackService;
import xlk.takstar.paperless.service.fab.FabService;
import xlk.takstar.paperless.service.ScreenRecorder;
import xlk.takstar.paperless.util.LogUtil;
import xlk.takstar.paperless.util.MyRejectedExecutionHandler;
import xlk.takstar.paperless.util.NamingThreadFactory;

/**
 * @author Created by xlk on 2020/11/26.
 * @desc
 */
public class App extends Application {
    public static LocalBroadcastManager lbm;
    public static MediaProjectionManager mMediaProjectionManager;
    public static MediaProjection mMediaProjection;
    public static int mResult;
    public static Intent mIntent;

    static {
        System.loadLibrary("avcodec-57");
        System.loadLibrary("avdevice-57");
        System.loadLibrary("avfilter-6");
        System.loadLibrary("avformat-57");
        System.loadLibrary("avutil-55");
        System.loadLibrary("postproc-54");
        System.loadLibrary("swresample-2");
        System.loadLibrary("swscale-4");
        System.loadLibrary("SDL2");
        System.loadLibrary("main");
        System.loadLibrary("NetClient");
        System.loadLibrary("Codec");
        System.loadLibrary("ExecProc");
        System.loadLibrary("Device-OpenSles");
        System.loadLibrary("meetcoreAnd");
        System.loadLibrary("PBmeetcoreAnd");
        System.loadLibrary("meetAnd");
        System.loadLibrary("native-lib");
        System.loadLibrary("z");
    }

    public static final boolean read2file = true;
    private final String TAG = "App-->";
    public static boolean isDebug = true;
    public static Context appContext;
    public static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            1,
            Runtime.getRuntime().availableProcessors() + 1,
            10L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(100),
            new NamingThreadFactory("paperless-threadPool-"),
            new MyRejectedExecutionHandler()
    );
    public static List<Activity> activities = new ArrayList<>();
    public static Activity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("程序创建的时候执行");
        appContext = this;
//        CrashHandler.getInstance().init(this);
        LogUtils.Config config = LogUtils.getConfig();
        config.setLog2FileSwitch(true);
        config.setDir(Constant.logcat_dir);
        config.setSaveDays(7);
        initScreenParam();
        lbm = LocalBroadcastManager.getInstance(getApplicationContext());
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ACTION_START_SCREEN_RECORD);
        filter.addAction(Constant.ACTION_STOP_SCREEN_RECORD);
        filter.addAction(Constant.ACTION_STOP_SCREEN_RECORD_WHEN_EXIT_APP);
        lbm.registerReceiver(receiver, filter);
        //创建广播
        HomeReceiver homeReceiver = new HomeReceiver();
        //动态注册广播
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        //启动广播
        registerReceiver(homeReceiver, intentFilter);

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                activities.add(activity);
                LogUtil.d("activityLife", "onActivityCreated " + activity + ",Activity数量=" + activities.size() + logAxt());
                if (activity.getClass().getName().equals(MainActivity.class.getName())) {
                    //进入到MainActivity证明已经获取到权限了
                    loadX5();
                    openBackService(true);
                }
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                LogUtil.i("activityLife", "onActivityStarted " + activity);
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                LogUtil.i("activityLife", "onActivityResumed " + activity);
                currentActivity = activity;
                openBackService(true);
                if (activity.getClass().getName().equals(MeetingActivity.class.getName())) {
                    openFabService(true);
                }
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
                LogUtil.i("activityLife", "onActivityPaused " + activity);
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                LogUtil.i("activityLife", "onActivityStopped " + activity);
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
                LogUtil.i("activityLife", "onActivitySaveInstanceState " + activity);
            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                activities.remove(activity);
                LogUtil.e("activityLife", "onActivityDestroyed " + activity + ",Activity数量=" + activities.size() + logAxt());
                if (activity.getClass().getName().equals(MeetingActivity.class.getName())) {
                    openFabService(false);
                }
                if (activities.isEmpty()) {
                    openBackService(false);
                    System.exit(0);
                }
            }
        });


        //防止项目崩溃，崩溃后打开错误界面
        CaocConfig.Builder.create()
                //程序在后台时，发生崩溃的三种处理方式
                //BackgroundMode.BACKGROUND_MODE_SHOW_CUSTOM: //当应用程序处于后台时崩溃，也会启动错误页面，
                //BackgroundMode.BACKGROUND_MODE_CRASH:      //当应用程序处于后台崩溃时显示默认系统错误（一个系统提示的错误对话框），
                //BackgroundMode.BACKGROUND_MODE_SILENT:     //当应用程序处于后台时崩溃，默默地关闭程序！
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
                .enabled(true)//是否启用CustomActivityOnCrash崩溃拦截机制 必须启用！不然集成这个库干啥？？？
                .showErrorDetails(true) //是否必须显示包含错误详细信息的按钮 default: true
                .showRestartButton(true) //是否必须显示“重新启动应用程序”按钮或“关闭应用程序”按钮default: true
                .logErrorOnRestart(true) //是否必须重新堆栈堆栈跟踪 default: true
                .trackActivities(true) //是否必须跟踪用户访问的活动及其生命周期调用 default: false
                .minTimeBetweenCrashesMs(2000) //应用程序崩溃之间必须经过的时间 default: 3000
                .restartActivity(LaunchActivity.class) // 重启的activity
                .errorActivity(ErrorActivity.class) //发生错误跳转的activity
                .eventListener(new ErrActEventListener())
                .apply();
    }

    private static class ErrActEventListener implements CustomActivityOnCrash.EventListener {

        @Override
        public void onLaunchErrorActivity() {
            LogUtils.e("onLaunchErrorActivity");
        }

        @Override
        public void onRestartAppFromErrorActivity() {
            LogUtils.e("onRestartAppFromErrorActivity");
        }

        @Override
        public void onCloseAppFromErrorActivity() {
            LogUtils.e("onCloseAppFromErrorActivity");
        }
    }

    private String logAxt() {
        String ret = "打印所有的Activity:\n";
        for (Activity activity : activities) {
            String a = activity.getCallingPackage() + "  #  " + activity + "\n";
            ret += a;
        }
        return ret;
    }

    private void loadX5() {
        // 在调用TBS初始化、创建WebView之前进行如下配置
//        HashMap map = new HashMap();
//        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
//        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
//        QbSdk.initTbsSettings(map);

        boolean canLoadX5 = QbSdk.canLoadX5(appContext);
        LogUtils.i(TAG, "x5内核  是否可以加载X5内核 -->" + canLoadX5);
        if (canLoadX5) {
            initX5();
        } else {
            QbSdk.setDownloadWithoutWifi(true);
            QbSdk.setTbsListener(new TbsListener() {
                @Override
                public void onDownloadFinish(int i) {
                    LogUtils.i(TAG, "x5内核 onDownloadFinish -->下载X5内核：" + i);
                }

                @Override
                public void onInstallFinish(int i) {
                    LogUtils.i(TAG, "x5内核 onInstallFinish -->安装X5内核：" + i);
                    if (i == TbsListener.ErrorCode.INSTALL_SUCCESS_AND_RELEASE_LOCK) {
                        initX5();
                    }
                }

                @Override
                public void onDownloadProgress(int i) {
                    LogUtils.i(TAG, "x5内核 onDownloadProgress -->下载X5内核：" + i);
                }
            });
            App.threadPool.execute(() -> {
                //判断是否要自行下载内核
//                boolean needDownload = TbsDownloader.needDownload(mContext, TbsDownloader.DOWNLOAD_OVERSEA_TBS);
//                LogUtil.i(TAG, "loadX5 是否需要自行下载X5内核" + needDownload);
//                if (needDownload) {
//                    // 根据实际的网络情况下，选择是否下载或是其他操作
//                    // 例如: 只有在wifi状态下，自动下载，否则弹框提示
//                    // 启动下载
//                    TbsDownloader.startDownload(mContext);
//                }
                LogUtils.i(TAG, "loadX5 开始下载X5内核");
                TbsDownloader.startDownload(appContext);
            });
        }
    }

    public void initX5() {
        LogUtil.i(TAG, "initX5 ");
        //目前线上sdk存在部分情况下initX5Enviroment方法没有回调，您可以不用等待该方法回调直接使用x5内核。
//        QbSdk.initX5Environment(appContext, cb);
        //如果您需要得知内核初始化状态，可以使用QbSdk.preinit接口代替
        QbSdk.preInit(appContext, cb);
    }

    public QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
        @Override
        public void onCoreInitFinished() {
            //x5内核初始化完成回调接口，此接口回调并表示已经加载起来了x5，有可能特殊情况下x5内核加载失败，切换到系统内核。
            LogUtils.i(TAG, "x5内核 onCoreInitFinished-->");
        }

        @Override
        public void onViewInitFinished(boolean b) {
            GlobalValue.initX5Finished = true;
            //ToastUtil.showToast(usedX5 ? R.string.tencent_x5_load_successfully : R.string.tencent_x5_load_failed);
            //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
            LogUtils.i(TAG, "x5内核 onViewInitFinished: 加载X5内核是否成功: " + b);
            AgendaFragment.isNeedRestart = !b;
            EventBus.getDefault().post(new EventMessage.Builder().type(EventType.BUS_X5_INSTALL).build());
        }
    };

    private void initScreenParam() {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager window = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        window.getDefaultDisplay().getMetrics(metric);
        GlobalValue.screen_width = metric.widthPixels;
        GlobalValue.screen_height = metric.heightPixels;
        GlobalValue.half_width = metric.widthPixels / 2;
        GlobalValue.half_height = metric.heightPixels / 2;
        width = metric.widthPixels;
        height = metric.heightPixels;
        dpi = metric.densityDpi;
        LogUtil.e(TAG, "initScreenParam :  dpi --> " + dpi);
        if (dpi > 320) {
            dpi = 320;
        }
        LogUtils.i(TAG, "initScreenParam 屏幕大小=" + GlobalValue.screen_width + "," + GlobalValue.screen_height);
    }

    private Intent fabService;
    public static boolean fabServiceIsOpen;

    private void openFabService(boolean open) {
        if (open && !fabServiceIsOpen) {
            if (fabService == null) {
                fabService = new Intent(this, FabService.class);
            }
            startService(fabService);
            LogUtil.d(TAG, "openFabService --> 打开悬浮窗服务");
        } else if (!open && fabServiceIsOpen) {
            if (fabService != null) {
                stopService(fabService);
                LogUtil.d(TAG, "openFabService --> 关闭悬浮窗服务");
            } else {
                LogUtil.d(TAG, "openFabService --> fabService为空，不需要关闭");
            }
        }
    }

    private Intent backService;
    public static boolean backServiceIsOpen;

    private void openBackService(boolean open) {
        if (open && !backServiceIsOpen) {
            if (backService == null) {
                backService = new Intent(this, BackService.class);
            }
            startService(backService);
            LogUtil.d(TAG, "openBackService --> 打开后台服务");
        } else if (!open && backServiceIsOpen) {
            if (backService != null) {
                stopService(backService);
                LogUtil.d(TAG, "openBackService --> 关闭后台服务");
            } else {
                LogUtil.d(TAG, "openBackService --> backService为空，不需要关闭");
            }
        }
    }

    public static int width, height, dpi, maxBitRate = 1000 * 1000;
    private ScreenRecorder recorder;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            int type = intent.getIntExtra(Constant.EXTRA_CAPTURE_TYPE, 0);
            LogUtil.e(TAG, "onReceive :   --> type= " + type + " , action = " + action);
            if (action.equals(Constant.ACTION_START_SCREEN_RECORD)) {
                LogUtil.e(TAG, "screen_shot --> ");
                screenRecording();
            } else if (action.equals(Constant.ACTION_STOP_SCREEN_RECORD)) {
                LogUtil.e(TAG, "stop_screen_shot --> ");
                if (stopRecord()) {
                    LogUtil.i(TAG, "stopStreamInform: 屏幕录制已停止..");
                } else {
                    LogUtil.e(TAG, "stopStreamInform: 屏幕录制停止失败 ");
                }
            } else if (action.equals(Constant.ACTION_STOP_SCREEN_RECORD_WHEN_EXIT_APP)) {
                LogUtil.i(TAG, "onReceive 退出APP时停止屏幕录制----");
                stopRecord();
                killAllActivity();
            }
        }
    };

    private void killAllActivity() {
        for (Activity activity : activities) {
            activity.finish();
        }
    }

    class HomeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
                String reason = intent.getStringExtra("reason");
                if (reason != null) {
                    if (reason.equals("homekey")) {
                        LogUtils.e(TAG, "onReceive 用户点击了home键");
//                        AppUtils.exitApp();
//                        openFabService(false);
                    } else if (reason.equals("recentapps")) {
                        LogUtils.e(TAG, "onReceive 用户点击了多任务键");
                    }
                }
            }
        }
    }

    private boolean stopRecord() {
        if (recorder != null) {
            recorder.quit();
            recorder = null;
            return true;
        } else {
            return false;
        }
    }

    private void screenRecording() {
        if (stopRecord()) {
            LogUtil.i(TAG, "startScreenRecord: 屏幕录制已停止");
        } else {
            if (mMediaProjection == null) {
                return;
            }
            if (recorder != null) {
                recorder.quit();
            }
            if (recorder == null) {
                recorder = new ScreenRecorder(width, height, maxBitRate, dpi, mMediaProjection, Constant.file_dir + "屏幕录制.mp4");
            }
            recorder.start();//启动录屏线程
            LogUtil.i(TAG, "startScreenRecord: 开启屏幕录制");
        }
    }
}
