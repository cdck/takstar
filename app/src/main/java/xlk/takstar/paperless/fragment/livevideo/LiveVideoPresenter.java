package xlk.takstar.paperless.fragment.livevideo;

import com.google.protobuf.InvalidProtocolBufferException;
import com.mogujie.tt.protobuf.InterfaceDevice;
import com.mogujie.tt.protobuf.InterfaceMacro;
import com.mogujie.tt.protobuf.InterfaceMember;
import com.mogujie.tt.protobuf.InterfaceStop;
import com.mogujie.tt.protobuf.InterfaceVideo;

import java.util.ArrayList;
import java.util.List;

import xlk.takstar.paperless.base.BasePresenter;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.model.EventMessage;
import xlk.takstar.paperless.model.EventType;
import xlk.takstar.paperless.model.GlobalValue;
import xlk.takstar.paperless.model.bean.DevMember;
import xlk.takstar.paperless.model.bean.VideoDev;
import xlk.takstar.paperless.util.LogUtil;

import static xlk.takstar.paperless.model.Constant.RESOURCE_ID_1;
import static xlk.takstar.paperless.model.Constant.RESOURCE_ID_2;
import static xlk.takstar.paperless.model.Constant.RESOURCE_ID_3;
import static xlk.takstar.paperless.model.Constant.RESOURCE_ID_4;

/**
 * @author Created by xlk on 2020/12/2.
 * @desc
 */
public class LiveVideoPresenter extends BasePresenter<LiveVideoContract.View> implements LiveVideoContract.Presenter{

    private List<InterfaceVideo.pbui_Item_MeetVideoDetailInfo> videoDetailInfos = new ArrayList<>();
    private List<InterfaceDevice.pbui_Item_DeviceDetailInfo> deviceDetailInfos = new ArrayList<>();
    private List<VideoDev> videoDevs = new ArrayList<>();
    public List<InterfaceMember.pbui_Item_MemberDetailInfo> memberDetailInfos = new ArrayList<>();
    public List<InterfaceDevice.pbui_Item_DeviceDetailInfo> onLineProjectors = new ArrayList<>();
    public List<DevMember> onLineMember = new ArrayList<>();

    public LiveVideoPresenter(LiveVideoContract.View view) {
        super(view);
    }

    @Override
    protected void busEvent(EventMessage msg) throws InvalidProtocolBufferException {
        switch (msg.getType()) {
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETVIDEO_VALUE://会议视频变更通知
                queryMeetVedio();
                break;
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_DEVICEINFO_VALUE://设备寄存器变更通知
                byte[] datas = (byte[]) msg.getObjects()[0];
                int datalen = (int) msg.getObjects()[1];
                InterfaceDevice.pbui_Type_MeetDeviceBaseInfo baseInfo = InterfaceDevice.pbui_Type_MeetDeviceBaseInfo.parseFrom(datas);
                int deviceid = baseInfo.getDeviceid();
                int attribid = baseInfo.getAttribid();
                LogUtil.d(TAG, "BusEvent -->" + "设备寄存器变更通知 deviceid= " + deviceid + ", attribid= " + attribid + ", datalen= " + datalen);
                queryDeviceInfo();
                break;
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEMBER_VALUE://参会人员变更通知
                LogUtil.d(TAG, "BusEvent -->" + "参会人员变更通知");
                queryMember();
                break;
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_DEVICEMEETSTATUS_VALUE://界面状态变更通知
                LogUtil.d(TAG, "BusEvent -->" + "界面状态变更通知");
                queryDeviceInfo();
                break;
            case EventType.BUS_VIDEO_DECODE://后台播放数据 DECODE
                Object[] objs = msg.getObjects();
                mView.updateDecode(objs);
                break;
            case EventType.BUS_YUV_DISPLAY://后台播放数据 YUV
                Object[] objs1 = msg.getObjects();
                mView.updateYuv(objs1);
                break;
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_STOPPLAY_VALUE://停止资源通知
                byte[] o1 = (byte[]) msg.getObjects()[0];
                if (msg.getMethod() == InterfaceMacro.Pb_Method.Pb_METHOD_MEET_INTERFACE_CLOSE_VALUE) {
                    //停止资源通知
                    InterfaceStop.pbui_Type_MeetStopResWork stopResWork = InterfaceStop.pbui_Type_MeetStopResWork.parseFrom(o1);
                    List<Integer> resList = stopResWork.getResList();
                    for (int resid : resList) {
                        LogUtil.i(TAG, "BusEvent -->" + "停止资源通知 resid: " + resid);
                        mView.stopResWork(resid);
                    }
                } else if (msg.getMethod() == InterfaceMacro.Pb_Method.Pb_METHOD_MEET_INTERFACE_NOTIFY_VALUE) {
                    //停止播放通知
                    InterfaceStop.pbui_Type_MeetStopPlay stopPlay = InterfaceStop.pbui_Type_MeetStopPlay.parseFrom(o1);
                    int resid = stopPlay.getRes();
                    int createdeviceid = stopPlay.getCreatedeviceid();
                    LogUtil.i(TAG, "BusEvent -->" + "停止播放通知 resid= " + resid + ", createdeviceid= " + createdeviceid);
                    mView.stopResWork(resid);
                }
                break;
        }
    }

    public void queryMeetVedio() {
        InterfaceVideo.pbui_Type_MeetVideoDetailInfo object = jni.queryMeetVideo();
        if (object == null) {
            videoDevs.clear();
            mView.updateRv(videoDevs);
            return;
        }
        videoDetailInfos.clear();
        videoDetailInfos.addAll(object.getItemList());
        videoDevs.clear();
        for (int i = 0; i < videoDetailInfos.size(); i++) {
            InterfaceVideo.pbui_Item_MeetVideoDetailInfo videoDetailInfo = videoDetailInfos.get(i);
            int deviceid = videoDetailInfo.getDeviceid();
            for (int j = 0; j < deviceDetailInfos.size(); j++) {
                InterfaceDevice.pbui_Item_DeviceDetailInfo detailInfo = deviceDetailInfos.get(j);
                if (detailInfo.getDevcieid() == deviceid) {
                    videoDevs.add(new VideoDev(videoDetailInfo, detailInfo));
                }
            }
        }
        mView.updateRv(videoDevs);
    }

    public void queryDeviceInfo() {
            InterfaceDevice.pbui_Type_DeviceDetailInfo deviceDetailInfo = jni.queryDeviceInfo();
            if (deviceDetailInfo == null) {
                return;
            }
            deviceDetailInfos.clear();
            deviceDetailInfos.addAll(deviceDetailInfo.getPdevList());
            queryMeetVedio();
            queryMember();
    }

    public void initVideoRes(int pvWidth, int pvHeight) {
        jni.initVideoRes(RESOURCE_ID_1, pvWidth / 2, pvHeight / 2);
        jni.initVideoRes(RESOURCE_ID_2, pvWidth / 2, pvHeight / 2);
        jni.initVideoRes(RESOURCE_ID_3, pvWidth / 2, pvHeight / 2);
        jni.initVideoRes(RESOURCE_ID_4, pvWidth / 2, pvHeight / 2);
    }

    public void releaseVideoRes() {
        jni.releaseVideoRes(RESOURCE_ID_1);
        jni.releaseVideoRes(RESOURCE_ID_2);
        jni.releaseVideoRes(RESOURCE_ID_3);
        jni.releaseVideoRes(RESOURCE_ID_4);
    }

    public void stopResource(int resId) {
        List<Integer> ids = new ArrayList<>();
        List<Integer> res = new ArrayList<>();
        res.add(resId);
        ids.add(GlobalValue.localDeviceId);
        jni.stopResourceOperate(res, ids);
    }

    public void watch(VideoDev videoDev, int resId) {
        InterfaceVideo.pbui_Item_MeetVideoDetailInfo videoDetailInfo = videoDev.getVideoDetailInfo();
        int deviceid = videoDetailInfo.getDeviceid();
        int subid = videoDetailInfo.getSubid();
        List<Integer> res = new ArrayList<>();
        res.add(resId);
        List<Integer> ids = new ArrayList<>();
        ids.add(GlobalValue.localDeviceId);
        jni.streamPlay(deviceid, subid, 0, res, ids);
    }

    public void queryMember() {
            InterfaceMember.pbui_Type_MemberDetailInfo attendPeople = jni.queryMember();
            if (attendPeople == null) {
                return;
            }
            memberDetailInfos.clear();
            memberDetailInfos.addAll(attendPeople.getItemList());
            onLineProjectors.clear();
            onLineMember.clear();
            for (int i = 0; i < deviceDetailInfos.size(); i++) {
                InterfaceDevice.pbui_Item_DeviceDetailInfo dev = deviceDetailInfos.get(i);
                int devcieid = dev.getDevcieid();
                int memberid = dev.getMemberid();
                int netstate = dev.getNetstate();
                int facestate = dev.getFacestate();
                if (devcieid == GlobalValue.localDeviceId) {
                    continue;
                }
                if (netstate == 1) {//在线
                    if (Constant.isThisDevType(InterfaceMacro.Pb_DeviceIDType.Pb_DeviceIDType_MeetProjective_VALUE, devcieid)) {//在线的投影机
                        onLineProjectors.add(dev);
                    } else {
                        if (facestate == 1) {
                            for (int j = 0; j < memberDetailInfos.size(); j++) {
                                InterfaceMember.pbui_Item_MemberDetailInfo member = memberDetailInfos.get(j);
                                if (member.getPersonid() == memberid) {
                                    onLineMember.add(new DevMember(dev, member));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            mView.notifyOnLineAdapter();
    }
}
