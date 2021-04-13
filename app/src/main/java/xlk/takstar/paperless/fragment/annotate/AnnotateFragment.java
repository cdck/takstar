package xlk.takstar.paperless.fragment.annotate;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.mogujie.tt.protobuf.InterfaceFile;
import com.mogujie.tt.protobuf.InterfaceMacro;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.adapter.DownloadFileAdapter;
import xlk.takstar.paperless.adapter.FileAdapter;
import xlk.takstar.paperless.adapter.SeatMemberAdapter;
import xlk.takstar.paperless.base.BaseFragment;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.model.EventMessage;
import xlk.takstar.paperless.model.EventType;
import xlk.takstar.paperless.model.GlobalValue;
import xlk.takstar.paperless.model.JniHelper;
import xlk.takstar.paperless.model.bean.SeatMember;
import xlk.takstar.paperless.ui.RvItemDecoration;
import xlk.takstar.paperless.util.FileUtil;
import xlk.takstar.paperless.util.LogUtil;
import xlk.takstar.paperless.util.PopUtil;

import static xlk.takstar.paperless.model.Constant.RESOURCE_ID_0;

/**
 * @author Created by xlk on 2020/12/1.
 * @desc
 */
public class AnnotateFragment extends BaseFragment<AnnotatePresenter> implements AnnotateContract.View, View.OnClickListener {
    private RecyclerView rv_member;
    private Button btn_document;
    private Button btn_picture;
    private Button btn_video;
    private Button btn_other;
    private Button btn_push;
    private Button btn_export;
    private RecyclerView rv_file;
    private SeatMemberAdapter seatMemberAdapter;
    private int currentDeviceId, currentMemberId;
    /**
     * =0显示全部，=1文档资料，=2图片文件，=3视频文件，=4其它文件
     */
    private int currentFileType = 0;
    private FileAdapter fileAdapter;
    List<InterfaceFile.pbui_Item_MeetDirFileDetailInfo> allFiles = new ArrayList<>();
    List<InterfaceFile.pbui_Item_MeetDirFileDetailInfo> currentFiles = new ArrayList<>();
    List<InterfaceFile.pbui_Item_MeetDirFileDetailInfo> downloadFiles = new ArrayList<>();
    private DownloadFileAdapter downloadFileAdapter;
    private PopupWindow downloadPop;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_annotate;
    }

    @Override
    protected AnnotatePresenter initPresenter() {
        return new AnnotatePresenter(this);
    }

    @Override
    protected void initial() {
        presenter.queryFile();
        presenter.queryMember();
    }

    @Override
    protected void onShow() {
        initial();
    }

    @Override
    protected void onHide() {
        dismissPop(downloadPop);
    }

    @Override
    protected void initView(View inflate) {
        rv_member = inflate.findViewById(R.id.rv_member);
        btn_document = inflate.findViewById(R.id.btn_document);
        btn_picture = inflate.findViewById(R.id.btn_picture);
        btn_video = inflate.findViewById(R.id.btn_video);
        btn_other = inflate.findViewById(R.id.btn_other);
        btn_push = inflate.findViewById(R.id.btn_push);
        btn_export = inflate.findViewById(R.id.btn_export);
        rv_file = inflate.findViewById(R.id.rv_file);
        btn_document.setOnClickListener(this);
        btn_picture.setOnClickListener(this);
        btn_video.setOnClickListener(this);
        btn_other.setOnClickListener(this);
        btn_push.setOnClickListener(this);
        btn_export.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_document:
                currentFileType = currentFileType == 1 ? 0 : 1;
                showFile();
                break;
            case R.id.btn_picture:
                currentFileType = currentFileType == 2 ? 0 : 2;
                showFile();
                break;
            case R.id.btn_video:
                currentFileType = currentFileType == 3 ? 0 : 3;
                showFile();
                break;
            case R.id.btn_other:
                currentFileType = currentFileType == 4 ? 0 : 4;
                showFile();
                break;
            case R.id.btn_push: {
                if (fileAdapter == null) return;
                int mediaId = fileAdapter.getSelectedId();
                if (mediaId != 0) {
                    EventBus.getDefault().post(new EventMessage.Builder().type(EventType.BUS_PUSH_FILE).objects(mediaId).build());
                } else {
                    ToastUtils.showShort(R.string.please_choose_file_first);
                }
                break;
            }
            case R.id.btn_export:
                if (Constant.hasPermission(Constant.permission_code_download)) {
                    showDownloadPop();
                } else {
                    ToastUtils.showShort(R.string.you_have_no_permission);
                }
                break;
            default:
                break;
        }
    }

    private void showDownloadPop() {
        if (downloadFiles.isEmpty()) {
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
                presenter.downloadFile(selected.get(i));
            }
            downloadPop.dismiss();
        });
    }

    @Override
    public void updateMemberRv(List<SeatMember> seatMembers) {
        if (seatMemberAdapter == null) {
            seatMemberAdapter = new SeatMemberAdapter(R.layout.item_seat_member, seatMembers);
            rv_member.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_member.setAdapter(seatMemberAdapter);
            seatMemberAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    currentDeviceId = seatMembers.get(position).getSeatDetailInfo().getSeatid();
                    currentMemberId = seatMembers.get(position).getMemberDetailInfo().getPersonid();
                    seatMemberAdapter.setSelectedId(currentDeviceId);
                    if (presenter.hasPermission(currentDeviceId)) {
                        showFile();
                    } else {
                        cleanFile();
                        jni.applyPermission(currentDeviceId,
                                InterfaceMacro.Pb_MemberPermissionPropertyID.Pb_memperm_postilview_VALUE);
                    }
                }
            });
        } else {
            seatMemberAdapter.notifyDataSetChanged();
        }
    }

    private void showFile() {
        if (currentMemberId == 0) {
            return;
        }
        currentFiles.clear();
        downloadFiles.clear();
        if (presenter.hasPermission(currentDeviceId)) {
            for (int i = 0; i < allFiles.size(); i++) {
                InterfaceFile.pbui_Item_MeetDirFileDetailInfo item = allFiles.get(i);
                int uploaderid = item.getUploaderid();
                if (uploaderid == currentMemberId) {
                    downloadFiles.add(item);
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
                    } else {
                        currentFiles.add(item);
                    }
                }
            }
            if (fileAdapter == null) {
                fileAdapter = new FileAdapter(R.layout.item_file, currentFiles);
                rv_file.setLayoutManager(new LinearLayoutManager(getContext()));
                rv_file.addItemDecoration(new RvItemDecoration(getContext()));
                rv_file.setAdapter(fileAdapter);
                fileAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                        fileAdapter.setSelectedId(currentFiles.get(position).getMediaid());
                    }
                });
                fileAdapter.setOnItemChildClickListener((adapter, view, position) -> {
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
                });
            } else {
                fileAdapter.notifyDataSetChanged();
            }
        } else {
            cleanFile();
        }
        if (downloadFileAdapter == null) {
            downloadFileAdapter = new DownloadFileAdapter(R.layout.item_download_file, downloadFiles);
            downloadFileAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    InterfaceFile.pbui_Item_MeetDirFileDetailInfo item = downloadFiles.get(position);
                    int mediaid = item.getMediaid();
                    if (FileUtils.isFileExists(Constant.download_dir + item.getName().toStringUtf8())) {
                        ToastUtils.showShort(getString(R.string._file_already_exists, item.getName().toStringUtf8()));
                    } else {
                        downloadFileAdapter.choose(mediaid);
                    }
                }
            });
        } else {
            downloadFileAdapter.notifyDataSetChanged();
        }
    }

    private void cleanFile() {
        if (fileAdapter != null) {
            currentFiles.clear();
            fileAdapter.notifyDataSetChanged();
        }
        if (downloadFileAdapter != null) {
            downloadFiles.clear();
            downloadFileAdapter.clearSelected();
            downloadFileAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void updateFiles(List<InterfaceFile.pbui_Item_MeetDirFileDetailInfo> annotateFiles) {
        allFiles.clear();
        allFiles.addAll(annotateFiles);
        if (seatMemberAdapter != null && currentMemberId != 0) {
            showFile();
        }
//        downloadFiles.clear();
//        downloadFiles.addAll(allFiles);
//        if (downloadFileAdapter == null) {
//            downloadFileAdapter = new DownloadFileAdapter(R.layout.item_download_file, downloadFiles);
//            downloadFileAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                    InterfaceFile.pbui_Item_MeetDirFileDetailInfo item = downloadFiles.get(position);
//                    int mediaid = item.getMediaid();
//                    if (FileUtils.isFileExists(Constant.root_file + item.getName().toStringUtf8())) {
//                        ToastUtils.showShort(getString(R.string._file_already_exists, item.getName().toStringUtf8()));
//                    } else {
//                        downloadFileAdapter.setSelected(mediaid);
//                    }
//                }
//            });
//        } else {
//            downloadFileAdapter.notifyDataSetChanged();
//        }
    }
}
