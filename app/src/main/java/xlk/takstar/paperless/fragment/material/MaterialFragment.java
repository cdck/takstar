package xlk.takstar.paperless.fragment.material;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.UriUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mogujie.tt.protobuf.InterfaceFile;
import com.mogujie.tt.protobuf.InterfaceMacro;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.adapter.DirAdapter;
import xlk.takstar.paperless.adapter.DownloadFileAdapter;
import xlk.takstar.paperless.adapter.FileAdapter;
import xlk.takstar.paperless.base.BaseFragment;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.model.EventMessage;
import xlk.takstar.paperless.model.EventType;
import xlk.takstar.paperless.model.GlobalValue;
import xlk.takstar.paperless.model.JniHelper;
import xlk.takstar.paperless.ui.RvItemDecoration;
import xlk.takstar.paperless.util.FileUtil;
import xlk.takstar.paperless.util.LogUtil;
import xlk.takstar.paperless.util.PopUtil;

import static xlk.takstar.paperless.model.Constant.RESOURCE_ID_0;

/**
 * @author Created by xlk on 2020/12/1.
 * @desc
 */
public class MaterialFragment extends BaseFragment<MaterialPresenter> implements MaterialContract.View, View.OnClickListener {
    private RecyclerView rv_dir;
    private Button btn_upload;
    private Button btn_export;
    private Button btn_document;
    private Button btn_picture;
    private Button btn_video;
    private Button btn_other;
    private Button btn_push;
    private RecyclerView rv_file;
    private DirAdapter dirAdapter;
    private int currentDirId = -1;
    private FileAdapter fileAdapter;
    /**
     * =0显示全部，=1文档资料，=2图片文件，=3视频文件，=4其它文件
     */
    private int currentFileType = 0;
    List<InterfaceFile.pbui_Item_MeetDirFileDetailInfo> currentFiles = new ArrayList<>();
    private final int REQUEST_CODE_UPLOAD = 1;
    private PopupWindow downloadPop;
    private DownloadFileAdapter downloadFileAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_material;
    }

    @Override
    protected void initView(View inflate) {
        rv_dir = inflate.findViewById(R.id.rv_dir);
        btn_upload = inflate.findViewById(R.id.btn_upload);
        btn_export = inflate.findViewById(R.id.btn_export);
        btn_document = inflate.findViewById(R.id.btn_document);
        btn_picture = inflate.findViewById(R.id.btn_picture);
        btn_video = inflate.findViewById(R.id.btn_video);
        btn_other = inflate.findViewById(R.id.btn_other);
        btn_push = inflate.findViewById(R.id.btn_push);
        rv_file = inflate.findViewById(R.id.rv_file);
        btn_upload.setOnClickListener(this);
        btn_export.setOnClickListener(this);
        btn_document.setOnClickListener(this);
        btn_picture.setOnClickListener(this);
        btn_video.setOnClickListener(this);
        btn_other.setOnClickListener(this);
        btn_push.setOnClickListener(this);
    }

    @Override
    protected MaterialPresenter initPresenter() {
        return new MaterialPresenter(this);
    }

    @Override
    protected void initial() {
        presenter.queryDir();
    }

    @Override
    protected void onShow() {
        presenter.queryDir();
    }

    @Override
    protected void onHide() {
        dismissPop(downloadPop);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_UPLOAD) {
            Uri uri = data.getData();
            File file = UriUtils.uri2File(uri);
            if (file != null) {
                uploadFileDialog(file);
            }
        }
    }

    private void uploadFileDialog(File file) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.modify_file_name);
        EditText editText = new EditText(getContext());
        editText.setText(file.getName());
        builder.setView(editText);
        builder.setPositiveButton(R.string.upload, (dialog, which) -> {
            String fileName = editText.getText().toString().trim();
            if (fileName.isEmpty()) {
                ToastUtils.showShort(R.string.please_enter_file_name);
                return;
            }
            jni.uploadFile(InterfaceMacro.Pb_Upload_Flag.Pb_MEET_UPLOADFLAG_ONLYENDCALLBACK_VALUE,
                    currentDirId, 0, fileName, file.getParentFile().getAbsolutePath() + "/" + fileName, 0, Constant.UPLOAD_CHOOSE_FILE);
            dialog.dismiss();
        });
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_upload:
                if (Constant.hasPermission(Constant.permission_code_upload)) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");//无类型限制
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(intent, REQUEST_CODE_UPLOAD);
                } else {
                    ToastUtils.showShort(R.string.you_have_no_permission);
                }
                break;
            case R.id.btn_export:
                if (Constant.hasPermission(Constant.permission_code_download)) {
                    showDownloadPop();
                } else {
                    ToastUtils.showShort(R.string.you_have_no_permission);
                }
                break;
            case R.id.btn_document:
                currentFileType = currentFileType == 1 ? 0 : 1;
                presenter.queryDir();
                break;
            case R.id.btn_picture:
                currentFileType = currentFileType == 2 ? 0 : 2;
                presenter.queryDir();
                break;
            case R.id.btn_video:
                currentFileType = currentFileType == 3 ? 0 : 3;
                presenter.queryDir();
                break;
            case R.id.btn_other:
                currentFileType = currentFileType == 4 ? 0 : 4;
                presenter.queryDir();
                break;
            case R.id.btn_push: {
                int mediaId = fileAdapter.getSelectedId();
                if (mediaId != 0) {
                    EventBus.getDefault().post(new EventMessage.Builder().type(EventType.BUS_PUSH_FILE).objects(mediaId).build());
                } else {
                    ToastUtils.showShort(R.string.please_choose_file_first);
                }
                break;
            }
            default:
                break;
        }
    }

    private void showDownloadPop() {
        if (downloadFileAdapter == null) {
            ToastUtils.showShort(R.string.no_file_to_download);
            return;
        }
        downloadFileAdapter.clearSelected();
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.pop_download_file, null, false);
        downloadPop = PopUtil.createBigPop(inflate, btn_export);
        RecyclerView pop_rv_file = inflate.findViewById(R.id.pop_rv_file);
        pop_rv_file.setLayoutManager(new LinearLayoutManager(getContext()));
        pop_rv_file.setAdapter(downloadFileAdapter);
        inflate.findViewById(R.id.pop_iv_close).setOnClickListener(v -> downloadPop.dismiss());
        inflate.findViewById(R.id.pop_btn_cancel).setOnClickListener(v -> downloadPop.dismiss());
        inflate.findViewById(R.id.pop_btn_download).setOnClickListener(v -> {
            List<InterfaceFile.pbui_Item_MeetDirFileDetailInfo> selected = downloadFileAdapter.getSelected();
            for (int i = 0; i < selected.size(); i++) {
                presenter.downloadFiles(selected.get(i));
            }
            downloadPop.dismiss();
        });
    }

    @Override
    public void updateDir() {
        if (dirAdapter == null) {
            dirAdapter = new DirAdapter(R.layout.item_dir, presenter.meetDirs);
            rv_dir.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_dir.setAdapter(dirAdapter);
            dirAdapter.setOnItemClickListener((adapter, view, position) -> {
                currentFileType = 0;
                currentDirId = presenter.meetDirs.get(position).getId();
                dirAdapter.setChoose(currentDirId);
                presenter.queryFileByDir(currentDirId);
            });
        } else {
            dirAdapter.notifyDataSetChanged();
        }
        if (!presenter.meetDirs.isEmpty()) {
            int dirId = presenter.meetDirs.get(0).getId();
            if (currentDirId == -1) {
                presenter.queryFileByDir(dirId);
                currentDirId = dirId;
                dirAdapter.setChoose(currentDirId);
            } else {
                boolean have = false;
                for (int i = 0; i < presenter.meetDirs.size(); i++) {
                    InterfaceFile.pbui_Item_MeetDirDetailInfo info = presenter.meetDirs.get(i);
                    if (info.getId() == currentDirId) {
                        have = true;
                        break;
                    }
                }
                if (!have) {
                    currentDirId = dirId;
                }
                presenter.queryFileByDir(currentDirId);
                dirAdapter.setChoose(currentDirId);
            }
        } else {
            presenter.cleanFile();
        }
    }

    @Override
    public void updateFile(List<InterfaceFile.pbui_Item_MeetDirFileDetailInfo> meetFiles) {
        currentFiles.clear();
        if (currentFileType == 0) {
            currentFiles.addAll(meetFiles);
        } else {
            for (int i = 0; i < meetFiles.size(); i++) {
                InterfaceFile.pbui_Item_MeetDirFileDetailInfo item = meetFiles.get(i);
                String fileName = item.getName().toStringUtf8();
                if (currentFileType == 1) {
                    if (FileUtil.isDocument(fileName)) {
                        currentFiles.add(item);
                    }
                } else if (currentFileType == 2) {
                    if (FileUtil.isPicture(fileName)) {
                        currentFiles.add(item);
                    }
                } else if (currentFileType == 3) {
                    if (FileUtil.isVideo(fileName)) {
                        currentFiles.add(item);
                    }
                } else if (currentFileType == 4) {
                    if (FileUtil.isOther(fileName)) {
                        currentFiles.add(item);
                    }
                }
            }
        }
        if (fileAdapter == null) {
            fileAdapter = new FileAdapter(R.layout.item_file, currentFiles);
            rv_file.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_file.addItemDecoration(new RvItemDecoration(getContext()));
            rv_file.setAdapter(fileAdapter);
            fileAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    fileAdapter.setSelectedId(currentFiles.get(position).getMediaid());
                }
            });
            fileAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    InterfaceFile.pbui_Item_MeetDirFileDetailInfo item = currentFiles.get(position);
                    String fileName = item.getName().toStringUtf8();
                    int mediaid = item.getMediaid();
                    if (view.getId() == R.id.item_btn_open) {
                        if (FileUtil.isVideo(fileName)) {
                            List<Integer> devIds = new ArrayList<>();
                            devIds.add(GlobalValue.localDeviceId);
                            JniHelper.getInstance().mediaPlayOperate(mediaid, devIds, 0, RESOURCE_ID_0, 0, 0);
                        } else {
                            FileUtil.openFile(getContext(), fileName, mediaid);
                        }
                    } else {
                        LogUtil.i(TAG, "onItemChildClick fileName=" + fileName);
                        JniHelper.getInstance().creationFileDownload(Constant.download_dir + fileName,
                                mediaid, 1, 0, Constant.DOWNLOAD_MATERIAL_FILE);
                    }
                }
            });
        } else {
            fileAdapter.notifyDataSetChanged();
        }
        if (downloadFileAdapter == null) {
            downloadFileAdapter = new DownloadFileAdapter(R.layout.item_download_file, meetFiles);
            downloadFileAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    InterfaceFile.pbui_Item_MeetDirFileDetailInfo item = meetFiles.get(position);
                    int mediaid = item.getMediaid();
                    if (FileUtils.isFileExists(Constant.download_dir + item.getName().toStringUtf8())) {
                        ToastUtils.showShort(getString(R.string._file_already_exists, item.getName().toStringUtf8()));
                    } else {
                        downloadFileAdapter.setSelected(mediaid);
                    }
                }
            });
        } else {
            downloadFileAdapter.notifyDataSetChanged();
        }
    }
}
