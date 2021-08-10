package xlk.takstar.paperless.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.protobuf.InvalidProtocolBufferException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import xlk.takstar.paperless.App;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.adapter.LocalFileAdapter;
import xlk.takstar.paperless.meet.MeetingActivity;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.model.EventMessage;
import xlk.takstar.paperless.model.EventType;
import xlk.takstar.paperless.model.JniHelper;
import xlk.takstar.paperless.ui.RvItemDecoration;
import xlk.takstar.paperless.util.FileUtil;
import xlk.takstar.paperless.util.IniUtil;
import xlk.takstar.paperless.util.LogUtil;
import xlk.takstar.paperless.util.PopUtil;
import xlk.takstar.paperless.util.ToastUtil;

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

    private List<File> currentFiles = new ArrayList<>();
    private LocalFileAdapter localFileAdapter;

    private FileFilter dirFilter = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return pathname.isDirectory() && !pathname.getName().startsWith(".");
        }
    };
    private EditText edt_save_address;

    /**
     * 展示选择目录的弹框
     *
     * @param defaultFileName 文件名
     * @param defaultDirPath  目录路径
     */
    public void showExportPop(String defaultFileName, String defaultDirPath, String noteContent) {
        View inflate = LayoutInflater.from(this).inflate(R.layout.pop_export_config, null);
        PopupWindow pop = PopUtil.createHalfPop(inflate, getWindow().getDecorView());
        EditText edt_file_name = inflate.findViewById(R.id.edt_file_name);
        TextView tv_suffix = inflate.findViewById(R.id.tv_suffix);
        edt_save_address = inflate.findViewById(R.id.edt_save_address);
        edt_file_name.setText(defaultFileName);
        tv_suffix.setText(".txt");
        edt_save_address.setText(defaultDirPath);
        edt_save_address.setKeyListener(null);
        edt_save_address.setSelection(defaultDirPath.length());
        inflate.findViewById(R.id.btn_choose_dir).setOnClickListener(v -> {
            String currentDirPath = edt_save_address.getText().toString().trim();
            if (currentDirPath.isEmpty()) {
                currentDirPath = Constant.root_dir;
            }
            showChooseDirPop(Constant.CHOOSE_DIR_TYPE_SAVE_NOTE, currentDirPath);
        });
        inflate.findViewById(R.id.iv_close).setOnClickListener(v -> pop.dismiss());
        inflate.findViewById(R.id.btn_cancel).setOnClickListener(v -> pop.dismiss());
        inflate.findViewById(R.id.btn_define).setOnClickListener(v -> {
            String fileName = edt_file_name.getText().toString().trim();
            String addr = edt_save_address.getText().toString().trim();
            if (fileName.isEmpty() || addr.isEmpty()) {
                ToastUtil.showShort(R.string.please_enter_file_name_and_addr);
                return;
            }
            App.threadPool.execute(() -> {
                FileUtils.createOrExistsFile(addr + "/" + fileName + ".txt");
                if (FileUtil.writeFileFromString(addr + "/" + fileName + ".txt", noteContent)) {
                    EventBus.getDefault().post(new EventMessage.Builder().type(EventType.BUS_TOAST_MESSAGE).objects(getString(R.string.save_meet_note_, addr + "/" + fileName + ".txt")).build());
                } else {
                    LogUtils.e("保存会议笔记失败");
                }
            });
            pop.dismiss();
        });
    }

    public void showChooseDirPop(int dirType, String rootDir) {
        currentFiles.clear();
        currentFiles.addAll(FileUtils.listFilesInDirWithFilter(rootDir, dirFilter));
        View inflate = LayoutInflater.from(this).inflate(R.layout.pop_local_file, null);
        PopupWindow dirPop;
        if (this instanceof MeetingActivity
                && dirType != Constant.CHOOSE_DIR_TYPE_SAVE_NOTE) {
            View ll_content = findViewById(R.id.meet_fl);
            View rv_navigation = findViewById(R.id.meet_left_ll);
            int width = ll_content.getWidth();
            int height = ll_content.getHeight();
            int width1 = rv_navigation.getWidth();
            dirPop = PopUtil.createPopupWindow(inflate, width / 2, height / 2, getWindow().getDecorView(), Gravity.CENTER, width1 / 2, 0);
        } else {
            dirPop = PopUtil.createHalfPop(inflate, getWindow().getDecorView());
        }
        EditText edt_current_dir = inflate.findViewById(R.id.edt_current_dir);
        edt_current_dir.setKeyListener(null);
        edt_current_dir.setText(rootDir);
        edt_current_dir.setSelection(rootDir.length());
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
                ToastUtil.showShort(R.string.current_dir_root);
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
        inflate.findViewById(R.id.btn_define).setOnClickListener(v -> {
            String dirPath = edt_current_dir.getText().toString();
            if (dirType == Constant.CHOOSE_DIR_TYPE_SAVE_NOTE) {
                edt_save_address.setText(dirPath);
                edt_save_address.setSelection(dirPath.length());
            } else {
                EventBus.getDefault().post(new EventMessage.Builder().type(EventType.RESULT_DIR_PATH).objects(dirType, dirPath).build());
            }
            dirPop.dismiss();
        });
        inflate.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            dirPop.dismiss();
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        app = (App) getApplication();
        presenter = initPresenter();
        init(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        LogUtil.i(TAG, "onNewIntent " + this);
        super.onNewIntent(intent);
    }

    protected abstract int getLayoutId();

    protected abstract T initPresenter();

    protected abstract void init(Bundle savedInstanceState);

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMessage(EventMessage msg) throws InvalidProtocolBufferException {
        switch (msg.getType()) {
            case EventType.CHOOSE_DIR_PATH: {
                Object[] objects = msg.getObjects();
                int intValue = (int) objects[0];
                String stringValue = (String) objects[1];
                if (intValue == Constant.CHOOSE_DIR_TYPE_SAVE_NOTE) {
                    showExportPop("会议笔记", Constant.export_dir, stringValue);
                } else {
                    showChooseDirPop(intValue, stringValue);
                }
                break;
            }
            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
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
