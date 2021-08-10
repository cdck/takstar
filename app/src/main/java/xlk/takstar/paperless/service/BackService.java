package xlk.takstar.paperless.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mogujie.tt.protobuf.InterfaceBase;
import com.mogujie.tt.protobuf.InterfaceDevice;
import com.mogujie.tt.protobuf.InterfaceDownload;
import com.mogujie.tt.protobuf.InterfaceMacro;
import com.mogujie.tt.protobuf.InterfacePlaymedia;
import com.mogujie.tt.protobuf.InterfaceStream;
import com.mogujie.tt.protobuf.InterfaceUpload;
import com.mogujie.tt.protobuf.InterfaceWhiteboard;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import androidx.annotation.Nullable;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.fragment.draw.DrawFragment;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.model.EventType;
import xlk.takstar.paperless.model.EventMessage;
import xlk.takstar.paperless.model.GlobalValue;
import xlk.takstar.paperless.model.JniHelper;
import xlk.takstar.paperless.model.WpsModel;
import xlk.takstar.paperless.util.FileUtil;
import xlk.takstar.paperless.util.LogUtil;
import xlk.takstar.paperless.util.ToastUtil;
import xlk.takstar.paperless.video.VideoActivity;

import static xlk.takstar.paperless.App.backServiceIsOpen;
import static xlk.takstar.paperless.fragment.draw.DrawFragment.disposePicOpermemberid;
import static xlk.takstar.paperless.fragment.draw.DrawFragment.disposePicSrcmemid;
import static xlk.takstar.paperless.fragment.draw.DrawFragment.disposePicSrcwbidd;
import static xlk.takstar.paperless.fragment.draw.DrawFragment.isSharing;
import static xlk.takstar.paperless.model.GlobalValue.isMandatoryPlaying;
import static xlk.takstar.paperless.model.GlobalValue.localDeviceId;

/**
 * @author Created by xlk on 2020/11/28.
 * @desc
 */
public class BackService extends Service {
    private final String TAG = "BackService-->";
    private WpsReceiver receiver;
    private JniHelper jni = JniHelper.getInstance();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.i(TAG, "onCreate ");
        backServiceIsOpen = true;
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.i(TAG, "onDestroy ");
        backServiceIsOpen = false;
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBusEvent(EventMessage msg) throws InvalidProtocolBufferException {
        switch (msg.getType()) {
            case EventType.BUS_TOAST_MESSAGE: {
                String message = (String) msg.getObjects()[0];
                LogUtils.i("弹出提示：" + message);
                ToastUtil.showLong(message);
                break;
            }
            case EventType.BUS_EXPORT_SUCCESSFUL: {
                String filePath = (String) msg.getObjects()[0];
                ToastUtil.showLong(getString(R.string.export_successful_, filePath));
                break;
            }
            //平台下载
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_DOWNLOAD_VALUE: {
                downloadInform(msg);
                break;
            }
            //上传进度通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_UPLOAD_VALUE: {
                uploadInform(msg);
                break;
            }
            //处理WPS广播监听
            case EventType.BUS_WPS_RECEIVER: {
                boolean isopen = (boolean) msg.getObjects()[0];
                if (isopen) {
                    registerWpsBroadCase();
                } else {
                    unregisterWpsBroadCase();
                }
                break;
            }
            //媒体播放通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEDIAPLAY_VALUE: {
                LogUtil.i(TAG, "onBusEvent 媒体播放通知");
                mediaPlayInform(msg);
                break;
            }
            //流播放通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_STREAMPLAY_VALUE: {
                LogUtil.i(TAG, "onBusEvent 流播放通知");
                streamPlayInform(msg);
                break;
            }
            //电子白板添加图片通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_WHITEBOARD_VALUE: {
                //添加图片通知
                if (msg.getMethod() == InterfaceMacro.Pb_Method.Pb_METHOD_MEET_INTERFACE_ADDPICTURE_VALUE) {
                    byte[] o1 = (byte[]) msg.getObjects()[0];
                    addPicInform(o1);
                }
                break;
            }
            //设备寄存器变更通知
//            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_DEVICEINFO_VALUE: {
//                byte[] bytes = (byte[]) msg.getObjects()[0];
//                InterfaceDevice.pbui_Type_MeetDeviceBaseInfo info = InterfaceDevice.pbui_Type_MeetDeviceBaseInfo.parseFrom(bytes);
//                int deviceid = info.getDeviceid();
//                int attribid = info.getAttribid();
//                LogUtil.i(TAG, "busEvent 设备寄存器变更通知 deviceid=" + deviceid + ",attribid=" + attribid);
//                if (deviceid != 0 && deviceid == localDeviceId) {
//                    queryDeviceFlag();
//                }
//                break;
//            }
            default:
                break;
        }
    }

    private void queryDeviceFlag() {
        byte[] bytes = jni.queryDevicePropertiesById(InterfaceMacro.Pb_MeetDevicePropertyID.Pb_MEETDEVICE_PROPERTY_DEVICEFLAG_VALUE, localDeviceId);
        if (bytes != null) {
            try {
                InterfaceDevice.pbui_DeviceInt32uProperty devFlag = InterfaceDevice.pbui_DeviceInt32uProperty.parseFrom(bytes);
                GlobalValue.localDeviceFlag = devFlag.getPropertyval();
                LogUtils.e("本机的设备属性，localDeviceFlag=" + GlobalValue.localDeviceFlag);
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
        }
    }

    private void addPicInform(byte[] datas) throws InvalidProtocolBufferException {
        InterfaceWhiteboard.pbui_Item_MeetWBPictureDetail object = InterfaceWhiteboard.pbui_Item_MeetWBPictureDetail.parseFrom(datas);
        int rPicSrcmemid = object.getSrcmemid();
        long rPicSrcwbid = object.getSrcwbid();
        ByteString rPicData = object.getPicdata();
        int opermemberid = object.getOpermemberid();
        GlobalValue.operid = object.getOperid();
        if (!isSharing) {
            if (disposePicOpermemberid == opermemberid && disposePicSrcmemid == rPicSrcmemid
                    && disposePicSrcwbidd == rPicSrcwbid) {
                DrawFragment.tempPicData = rPicData;
                disposePicOpermemberid = 0;
                disposePicSrcmemid = 0;
                disposePicSrcwbidd = 0;
            }
        } else {
            EventBus.getDefault().postSticky(new EventMessage.Builder().type(EventType.BUS_SHARE_PIC).objects(object).build());
        }
    }

    private void uploadInform(EventMessage msg) throws InvalidProtocolBufferException {
        byte[] datas = (byte[]) msg.getObjects()[0];
        InterfaceUpload.pbui_TypeUploadPosCb uploadPosCb = InterfaceUpload.pbui_TypeUploadPosCb.parseFrom(datas);
        String pathName = uploadPosCb.getPathname().toStringUtf8();
        String userStr = uploadPosCb.getUserstr().toStringUtf8();
        int status = uploadPosCb.getStatus();
        int mediaId = uploadPosCb.getMediaId();
        int per = uploadPosCb.getPer();
        byte[] bytes = jni.queryFileProperty(InterfaceMacro.Pb_MeetFilePropertyID.Pb_MEETFILE_PROPERTY_NAME.getNumber(), mediaId);
        InterfaceBase.pbui_CommonTextProperty pbui_commonTextProperty = InterfaceBase.pbui_CommonTextProperty.parseFrom(bytes);
        String fileName = pbui_commonTextProperty.getPropertyval().toStringUtf8();
        LogUtil.i(TAG, "uploadInform -->" + "上传进度：" + per + "\npathName= " + pathName);
         if (status == InterfaceMacro.Pb_Upload_State.Pb_UPLOADMEDIA_FLAG_HADEND_VALUE) {
            //结束上传
            if (userStr.equals(Constant.UPLOAD_DRAW_PIC)) {
                //从画板上传的图片
                FileUtils.delete(pathName);
            } else if (userStr.equals(Constant.UPLOAD_SCORE_FILE)) {
                EventBus.getDefault().post(new EventMessage.Builder().type(EventType.BUS_UPLOAD_SCORE_FILE_FINISH).objects(pathName, mediaId).build());
            }
            ToastUtils.showShort(getString(R.string.upload_completed, fileName));
            LogUtil.i(TAG, "uploadInform -->" + fileName + " 上传完毕");
        } else if (status == InterfaceMacro.Pb_Upload_State.Pb_UPLOADMEDIA_FLAG_NOSERVER_VALUE) {
            LogUtil.i(TAG, "uploadInform -->" + " 没找到可用的服务器");
        } else if (status == InterfaceMacro.Pb_Upload_State.Pb_UPLOADMEDIA_FLAG_ISBEING_VALUE) {
            LogUtil.i(TAG, "uploadInform -->" + pathName + " 已经存在");
            if (userStr.equals(Constant.UPLOAD_SCORE_FILE)) {
                EventBus.getDefault().post(new EventMessage.Builder().type(EventType.BUS_UPLOAD_SCORE_FILE_FINISH).objects(pathName, mediaId).build());
            }
        }
    }

    private void streamPlayInform(EventMessage msg) throws InvalidProtocolBufferException {
        byte[] datas = (byte[]) msg.getObjects()[0];
        InterfaceStream.pbui_Type_MeetStreamPlay meetStreamPlay = InterfaceStream.pbui_Type_MeetStreamPlay.parseFrom(datas);
        int res = meetStreamPlay.getRes();
        int createdeviceid = meetStreamPlay.getCreatedeviceid();
        LogUtil.i(TAG, "streamPlayInform -->" + "流播放通知 res =" + res);
        if (res != 0) {
            //只处理资源ID为0的播放资源
            return;
        }
        //触发器ID
        int triggerid = meetStreamPlay.getTriggerid();
        //流源设备ID 采集端的设备ID
        int deviceid = meetStreamPlay.getDeviceid();
        int subid = meetStreamPlay.getSubid();
        int triggeruserval = meetStreamPlay.getTriggeruserval();
        boolean isMandatory = triggeruserval == InterfaceMacro.Pb_TriggerUsedef.Pb_EXCEC_USERDEF_FLAG_NOCREATEWINOPER_VALUE;
        LogUtils.e("当前是否是强制性的 isMandatory=" + isMandatory);
        if (isMandatoryPlaying) {//当前正在被强制播放中
            if (isMandatory) {//收到新的强制性播放
                LogUtil.i(TAG, "streamPlayInform -->" + "当前属于强制性播放中，收到新的强制流播放播放");
            } else {//收到的不是强制性播放
                LogUtil.i(TAG, "streamPlayInform -->" + "当前属于强制性播放中，不处理非强制的流播放通知");
                return;
            }
        }
        if (createdeviceid != GlobalValue.localDeviceId) {
            //是否是强制性播放
            isMandatoryPlaying = isMandatory;
        }
        GlobalValue.haveNewPlayInform = true;
        startActivity(new Intent(this, VideoActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                .putExtra(Constant.EXTRA_VIDEO_ACTION, InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_STREAMPLAY_VALUE)
                .putExtra(Constant.EXTRA_VIDEO_DEVICE_ID, deviceid)
                .putExtra(Constant.EXTRA_VIDEO_SUB_ID, subid)
        );
    }

    private void mediaPlayInform(EventMessage msg) throws InvalidProtocolBufferException {
        byte[] datas = (byte[]) msg.getObjects()[0];
        InterfacePlaymedia.pbui_Type_MeetMediaPlay mediaPlay = InterfacePlaymedia.pbui_Type_MeetMediaPlay.parseFrom(datas);
        int res = mediaPlay.getRes();
        LogUtil.i(TAG, "mediaPlayInform -->" + "媒体播放通知 res= " + res);
        if (res != 0) {
            //只处理资源ID为0的播放资源
            return;
        }
        int mediaid = mediaPlay.getMediaid();
        int createdeviceid = mediaPlay.getCreatedeviceid();
        int triggerid = mediaPlay.getTriggerid();
        int triggeruserval = mediaPlay.getTriggeruserval();
        boolean isMandatory = triggeruserval == InterfaceMacro.Pb_TriggerUsedef.Pb_EXCEC_USERDEF_FLAG_NOCREATEWINOPER_VALUE;
        int type = mediaid & Constant.MAIN_TYPE_BITMASK;
        int subtype = mediaid & Constant.SUB_TYPE_BITMASK;
        if (type == Constant.MEDIA_FILE_TYPE_AUDIO || type == Constant.MEDIA_FILE_TYPE_VIDEO) {
            LogUtil.i(TAG, "mediaPlayInform -->" + "媒体播放通知：isVideoPlaying= " + GlobalValue.isVideoPlaying);
            if (createdeviceid != GlobalValue.localDeviceId) {
                isMandatoryPlaying = isMandatory;
            }
            GlobalValue.haveNewPlayInform = true;
            startActivity(new Intent(this, VideoActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    .putExtra(Constant.EXTRA_VIDEO_ACTION, InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEDIAPLAY_VALUE)
                    .putExtra(Constant.EXTRA_VIDEO_SUBTYPE, subtype)
                    .putExtra(Constant.EXTRA_VIDEO_MEDIA_ID, mediaid)
            );
        } else {
            LogUtil.i(TAG, "mediaPlayInform -->" + "媒体播放通知：下载文件后打开");
            //创建好下载目录
            FileUtils.createOrExistsDir(Constant.download_dir);
            /** **** **  查询该媒体ID的文件名  ** **** **/
            byte[] bytes = JniHelper.getInstance().queryFileProperty(InterfaceMacro.Pb_MeetFilePropertyID.Pb_MEETFILE_PROPERTY_NAME.getNumber(), mediaid);
            InterfaceBase.pbui_CommonTextProperty pbui_commonTextProperty = InterfaceBase.pbui_CommonTextProperty.parseFrom(bytes);
            String fielName = pbui_commonTextProperty.getPropertyval().toStringUtf8();
            String pathname = Constant.download_dir + fielName;
            File file = new File(pathname);
            if (file.exists()) {
                if (GlobalValue.downloadingFiles.contains(mediaid)) {
                    ToastUtils.showShort(R.string.file_downloading);
                } else {
                    FileUtil.openFile(this, file);
                }
                return;
            }
            JniHelper.getInstance().downloadFile(pathname, mediaid, 0, 0, Constant.DOWNLOAD_SHOULD_OPEN_FILE);
        }
    }

    private void registerWpsBroadCase() {
        if (receiver == null) {
            receiver = new WpsReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(WpsModel.Reciver.ACTION_SAVE);
            filter.addAction(WpsModel.Reciver.ACTION_CLOSE);
            filter.addAction(WpsModel.Reciver.ACTION_HOME);
            filter.addAction(WpsModel.Reciver.ACTION_BACK);
            registerReceiver(receiver, filter);
        }
    }

    private void unregisterWpsBroadCase() {
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    private void downloadInform(EventMessage msg) throws InvalidProtocolBufferException {
        byte[] data2 = (byte[]) msg.getObjects()[0];
        InterfaceDownload.pbui_Type_DownloadCb pbui_type_downloadCb = InterfaceDownload.pbui_Type_DownloadCb.parseFrom(data2);
        int mediaid = pbui_type_downloadCb.getMediaid();
        int progress = pbui_type_downloadCb.getProgress();
        int nstate = pbui_type_downloadCb.getNstate();
        int err = pbui_type_downloadCb.getErr();
        String filepath = pbui_type_downloadCb.getPathname().toStringUtf8();
        String userStr = pbui_type_downloadCb.getUserstr().toStringUtf8();
        String fileName = filepath.substring(filepath.lastIndexOf("/") + 1).toLowerCase();
        if (nstate == InterfaceMacro.Pb_Download_State.Pb_STATE_MEDIA_DOWNLOAD_WORKING_VALUE) {
            //主页背景
            if (!userStr.equals(Constant.MAIN_BG)
                    //主页logo
                    && !userStr.equals(Constant.MAIN_LOGO)
                    //子界面背景
                    && !userStr.equals(Constant.MEET_BG)
                    //公告背景
                    && !userStr.equals(Constant.BULLETIN_BG)
                    //公告logo
                    && !userStr.equals(Constant.BULLETIN_LOGO)
                    //会场底图
                    && !userStr.equals(Constant.ROOM_BG)
                    //下载议程文件
                    && !userStr.equals(Constant.DOWNLOAD_AGENDA_FILE)
            ) {
                ToastUtils.showShort(getString(R.string.file_downloaded_percent, fileName, progress + "%"));
            }
        } else if (nstate == InterfaceMacro.Pb_Download_State.Pb_STATE_MEDIA_DOWNLOAD_EXIT_VALUE) {
            //下载退出---不管成功与否,下载结束最后一次的状态都是这个
            if (GlobalValue.downloadingFiles.contains(mediaid)) {
                int index = GlobalValue.downloadingFiles.indexOf(mediaid);
                GlobalValue.downloadingFiles.remove(index);
            }
            File file = new File(filepath);
            if (file.exists()) {
                LogUtil.i(TAG, "BusEvent -->" + "下载完成：" + filepath);
                switch (userStr) {
                    case Constant.MAIN_BG:
                        EventBus.getDefault().post(new EventMessage.Builder().type(EventType.BUS_MAIN_BG).objects(filepath, mediaid).build());
                        break;
                    case Constant.MAIN_LOGO:
                        EventBus.getDefault().post(new EventMessage.Builder().type(EventType.BUS_MAIN_LOGO).objects(filepath, mediaid).build());
                        break;
                    case Constant.MEET_BG:
                        EventBus.getDefault().post(new EventMessage.Builder().type(EventType.BUS_MEET_BG).objects(filepath, mediaid).build());
                        break;
                    case Constant.MEET_LOGO:
                        EventBus.getDefault().post(new EventMessage.Builder().type(EventType.BUS_MEET_LOGO).objects(filepath, mediaid).build());
                        break;
                    case Constant.BULLETIN_BG:
                        EventBus.getDefault().post(new EventMessage.Builder().type(EventType.BUS_BULLETIN_BG).objects(filepath, mediaid).build());
                        break;
                    case Constant.BULLETIN_LOGO:
                        EventBus.getDefault().post(new EventMessage.Builder().type(EventType.BUS_BULLETIN_LOGO).objects(filepath, mediaid).build());
                        break;
                    case Constant.ROOM_BG:
                        EventBus.getDefault().post(new EventMessage.Builder().type(EventType.BUS_ROOM_BG).objects(filepath, mediaid).build());
                        break;
                    //下载的议程文件
                    case Constant.DOWNLOAD_AGENDA_FILE:
                        EventBus.getDefault().post(new EventMessage.Builder().type(EventType.BUS_AGENDA_FILE).objects(filepath, mediaid).build());
                        break;
                    case Constant.DOWNLOAD_SHOULD_OPEN_FILE:
                        EventBus.getDefault().post(new EventMessage.Builder().type(EventType.BUS_MATERIAL_FILE).objects(filepath, mediaid).build());
                        FileUtil.openFile(this, file);
                        break;
                    //会议资料文件下载完成
                    case Constant.DOWNLOAD_MATERIAL_FILE:
                        EventBus.getDefault().post(new EventMessage.Builder().type(EventType.BUS_MATERIAL_FILE).objects(filepath, mediaid).build());
                        break;
                    default:
                        break;
                }
            } else {
                LogUtil.i(TAG, "downloadInform 没有找到文件 filepath=" + filepath);
            }
        } else {
            LogUtil.i(TAG, "downloadInform 下载状态：" + nstate + ", 下载错误码：" + err + ", 文件名：" + fileName);
        }
    }
}
