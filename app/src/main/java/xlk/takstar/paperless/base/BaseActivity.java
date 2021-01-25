package xlk.takstar.paperless.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;

import com.blankj.utilcode.util.ToastUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import xlk.takstar.paperless.App;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.model.JniHelper;
import xlk.takstar.paperless.util.IniUtil;
import xlk.takstar.paperless.util.LogUtil;

/**
 * @author Created by xlk on 2020/11/28.
 * @desc
 */
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements IBaseView {
    protected String TAG = this.getClass().getSimpleName();
    protected T presenter;
    protected JniHelper jni = JniHelper.getInstance();
    protected IniUtil ini = IniUtil.getInstance();
    protected App app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        app = (App) getApplication();
        presenter = initPresenter();
        init(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        LogUtil.i(TAG, "onNewIntent " + this);
        super.onNewIntent(intent);
    }

    protected abstract int getLayoutId();

    protected abstract T initPresenter();

    protected abstract void init(Bundle savedInstanceState);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    /**
     * 打开选择本地文件
     *
     * @param requestCode 返回码
     */
    protected void chooseLocalFile(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, requestCode);
    }

    protected void showSuccessfulToast() {
        ToastUtils.make()
                .setLeftIcon(R.drawable.ic_successful)
                .setGravity(Gravity.CENTER, 0, 0)
                .setTextColor(0xFF3475D3)
                .setTextSize(18)
                .setBgResource(R.drawable.round_gray_bg)
                .setDurationIsLong(true)
                .show(R.string.launch_successful);
    }
}
