package xlk.takstar.paperless.fragment.annotate;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mogujie.tt.protobuf.InterfaceBase;
import com.mogujie.tt.protobuf.InterfaceDevice;
import com.mogujie.tt.protobuf.InterfaceFile;
import com.mogujie.tt.protobuf.InterfaceMacro;
import com.mogujie.tt.protobuf.InterfaceMember;
import com.mogujie.tt.protobuf.InterfaceRoom;

import java.util.ArrayList;
import java.util.List;

import xlk.takstar.paperless.App;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.base.BasePresenter;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.model.EventMessage;
import xlk.takstar.paperless.model.EventType;
import xlk.takstar.paperless.model.GlobalValue;
import xlk.takstar.paperless.model.bean.SeatMember;
import xlk.takstar.paperless.util.LogUtil;

/**
 * @author Created by xlk on 2020/12/1.
 * @desc
 */
public class AnnotatePresenter extends BasePresenter<AnnotateContract.View> implements AnnotateContract.Presenter {

    public List<InterfaceMember.pbui_Item_MemberDetailInfo> members = new ArrayList<>();
    public List<SeatMember> seatMembers = new ArrayList<>();
    private List<InterfaceFile.pbui_Item_MeetDirFileDetailInfo> annotateFiles = new ArrayList<>();
    private List<Integer> saveConsentDevices = new ArrayList<>();

    public AnnotatePresenter(AnnotateContract.View view) {
        super(view);
    }

    @Override
    protected void busEvent(EventMessage msg) throws InvalidProtocolBufferException {
        switch (msg.getType()) {
            //会议排位变更通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETSEAT_VALUE: {
                queryMeetRanking();
                break;
            }
            //参会人员变更通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEMBER_VALUE: {
                queryMember();
                break;
            }
            //会议目录文件变更通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETDIRECTORYFILE_VALUE: {
                if (msg.getMethod() == InterfaceMacro.Pb_Method.Pb_METHOD_MEET_INTERFACE_NOTIFY.getNumber()) {
                    byte[] o = (byte[]) msg.getObjects()[0];
                    InterfaceBase.pbui_MeetNotifyMsgForDouble info = InterfaceBase.pbui_MeetNotifyMsgForDouble.parseFrom(o);
                    if (info.getId() == Constant.ANNOTATION_FILE_DIRECTORY_ID) {
                        queryFile();
                    }
                }
                break;
            }
            //会议资料下载完成
            case EventType.BUS_MATERIAL_FILE: {
                LogUtil.d(TAG, "BusEvent -->" + "会议资料下载完成");
                queryFile();
                break;
            }
            //设备交互
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_DEVICEOPER_VALUE: {
                if (msg.getMethod() == InterfaceMacro.Pb_Method.Pb_METHOD_MEET_INTERFACE_RESPONSEPRIVELIGE.getNumber()) {
                    //收到参会人员权限请求回复
                    byte[] o = (byte[]) msg.getObjects()[0];
                    InterfaceDevice.pbui_Type_MeetRequestPrivilegeResponse object = InterfaceDevice.pbui_Type_MeetRequestPrivilegeResponse.parseFrom(o);
                    // returncode 1=同意,0=不同意
                    int returncode = object.getReturncode();
                    // 发起请求的设备ID
                    int deviceid = object.getDeviceid();
                    // 发起请求的人员ID
                    int memberid = object.getMemberid();
                    LogUtil.i(TAG, "busEvent 收到参会人员权限请求回复 returncode=" + returncode + ",deviceid=" + deviceid + ",memberid=" + memberid);
                    //查看批注文件权限有了
                    if (returncode == 1) {
                        if (!saveConsentDevices.contains(deviceid)) {
                            saveConsentDevices.add(deviceid);
                        }
                        for (int i = 0; i < members.size(); i++) {
                            if (members.get(i).getPersonid() == memberid) {
                                String name = members.get(i).getName().toStringUtf8();
                                ToastUtils.showShort(App.appContext.getString(R.string.agreed_postilview, name));
                                break;
                            }
                        }
                        queryFile();
                    } else {
                        if (saveConsentDevices.contains(deviceid)) {
                            saveConsentDevices.remove(deviceid);
                        }
                        for (int i = 0; i < members.size(); i++) {
                            if (members.get(i).getPersonid() == memberid) {
                                String name = members.get(i).getName().toStringUtf8();
                                ToastUtils.showShort(App.appContext.getString(R.string.reject_postilview, name));
                                break;
                            }
                        }
                    }
                }
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void queryMember() {
        InterfaceMember.pbui_Type_MemberDetailInfo info = jni.queryMember();
        members.clear();
        if (info != null) {
            members.addAll(info.getItemList());
        }
        queryMeetRanking();
    }

    @Override
    public void queryMeetRanking() {
        InterfaceRoom.pbui_Type_MeetSeatDetailInfo info = jni.queryMeetRanking();
        seatMembers.clear();
        if (info != null) {
            List<InterfaceRoom.pbui_Item_MeetSeatDetailInfo> itemList = info.getItemList();
            for (int i = 0; i < itemList.size(); i++) {
                InterfaceRoom.pbui_Item_MeetSeatDetailInfo item = itemList.get(i);
                for (int j = 0; j < members.size(); j++) {
                    InterfaceMember.pbui_Item_MemberDetailInfo member = members.get(j);
                    if (item.getNameId() == member.getPersonid()) {
                        seatMembers.add(new SeatMember(member, item));
                    }
                }
            }
        }
        mView.updateMemberRv(seatMembers);
    }

    @Override
    public void queryFile() {
        InterfaceFile.pbui_Type_MeetDirFileDetailInfo info = jni.queryMeetDirFile(Constant.ANNOTATION_FILE_DIRECTORY_ID);
        annotateFiles.clear();
        if (info != null) {
            annotateFiles.addAll(info.getItemList());
        }
        mView.updateFiles(annotateFiles);
    }

    public boolean hasPermission(int devId) {
        if (devId == GlobalValue.localDeviceId) {
            return true;
        }
        return saveConsentDevices.contains(devId);
    }

    @Override
    public void downloadFile(InterfaceFile.pbui_Item_MeetDirFileDetailInfo itemFile) {
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
