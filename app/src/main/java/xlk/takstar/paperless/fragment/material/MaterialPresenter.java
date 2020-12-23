package xlk.takstar.paperless.fragment.material;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mogujie.tt.protobuf.InterfaceFile;
import com.mogujie.tt.protobuf.InterfaceMacro;

import java.util.ArrayList;
import java.util.List;

import xlk.takstar.paperless.R;
import xlk.takstar.paperless.base.BasePresenter;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.model.EventMessage;
import xlk.takstar.paperless.model.EventType;
import xlk.takstar.paperless.model.GlobalValue;
import xlk.takstar.paperless.util.LogUtil;

/**
 * @author Created by xlk on 2020/12/1.
 * @desc
 */
public class MaterialPresenter extends BasePresenter<MaterialContract.View> implements MaterialContract.Presenter {

    public List<InterfaceFile.pbui_Item_MeetDirDetailInfo> meetDirs = new ArrayList<>();
    private List<InterfaceFile.pbui_Item_MeetDirFileDetailInfo> meetFiles = new ArrayList<>();

    public MaterialPresenter(MaterialContract.View view) {
        super(view);
    }

    @Override
    protected void busEvent(EventMessage msg) throws InvalidProtocolBufferException {
        switch (msg.getType()) {
            //会议目录
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETDIRECTORY_VALUE:
                queryDir();
                break;
            //会议目录文件
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETDIRECTORYFILE_VALUE:
                LogUtil.d(TAG, "BusEvent -->" + "会议目录文件变更通知");
                queryDir();
                break;
            //会议资料下载完成
            case EventType.BUS_MATERIAL_FILE:
                LogUtil.d(TAG, "BusEvent -->" + "会议资料下载完成");
                queryDir();
                break;
            default:
                break;
        }
    }

    @Override
    public void queryDir() {
        InterfaceFile.pbui_Type_MeetDirDetailInfo info = jni.queryMeetDir();
        meetDirs.clear();
        if (info != null) {
            List<InterfaceFile.pbui_Item_MeetDirDetailInfo> itemList = info.getItemList();
            for (int i = 0; i < itemList.size(); i++) {
                InterfaceFile.pbui_Item_MeetDirDetailInfo item = itemList.get(i);
                if (item.getId() != Constant.ANNOTATION_FILE_DIRECTORY_ID) {
                    meetDirs.add(item);
                }
            }
        }
        mView.updateDir();
    }

    @Override
    public void cleanFile() {
        meetFiles.clear();
        mView.updateFile(meetFiles);
    }

    @Override
    public void queryFileByDir(int dirId) {
        InterfaceFile.pbui_Type_MeetDirFileDetailInfo info = jni.queryMeetDirFile(dirId);
        meetFiles.clear();
        if (info != null) {
            meetFiles.addAll(info.getItemList());
        }
        mView.updateFile(meetFiles);
    }

    @Override
    public void downloadFiles(InterfaceFile.pbui_Item_MeetDirFileDetailInfo itemFile) {
        if (FileUtils.createOrExistsDir(Constant.download_dir)) {
            String fileName = itemFile.getName().toStringUtf8();
            if (FileUtils.isFileExists(Constant.download_dir + fileName)) {
                if (GlobalValue.downloadingFiles.contains(itemFile.getMediaid())) {
                    ToastUtils.showShort(R.string.file_downloading);
                } else {
                    ToastUtils.showShort(R.string.file_already_exists);
                }
            } else {
                jni.creationFileDownload(Constant.download_dir + fileName,
                        itemFile.getMediaid(), 1, 0, Constant.DOWNLOAD_MATERIAL_FILE);
            }
        }
    }

}
