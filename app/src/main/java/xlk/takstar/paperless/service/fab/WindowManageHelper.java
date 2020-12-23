package xlk.takstar.paperless.service.fab;

import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import xlk.takstar.paperless.R;
import xlk.takstar.paperless.model.GlobalValue;

/**
 * @author Created by xlk on 2020/12/11.
 * @desc
 */
public class WindowManageHelper {
    private final WindowManager wm;
    private WindowManager.LayoutParams defaultParams;
    private boolean hoverButtonIsShowing, menuViewIsShowing, serviceViewIsShowing, screenViewIsShowing,
            joinViewIsShowing, proViewIsShowing, voteViewIsShowing, voteEnsureViewIsShowing;

    public WindowManageHelper(WindowManager wm) {
        this.wm = wm;
        initParams();
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
        defaultParams = new WindowManager.LayoutParams();
        setParamsType(defaultParams);
        defaultParams.format = PixelFormat.RGBA_8888;
        defaultParams.gravity = Gravity.CENTER;
        defaultParams.width = GlobalValue.half_width;
        defaultParams.height = GlobalValue.half_height;
        defaultParams.windowAnimations = R.style.pop_animation_t_b;
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

    private void setIsShowing(View removeView, View addView) {
        String removeTag = (String) removeView.getTag();
        String addTag = (String) addView.getTag();
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
        }
    }
}
