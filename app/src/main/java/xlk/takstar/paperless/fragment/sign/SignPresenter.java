package xlk.takstar.paperless.fragment.sign;

import com.blankj.utilcode.util.FileUtils;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mogujie.tt.protobuf.InterfaceBase;
import com.mogujie.tt.protobuf.InterfaceFaceconfig;
import com.mogujie.tt.protobuf.InterfaceMacro;
import com.mogujie.tt.protobuf.InterfaceMember;
import com.mogujie.tt.protobuf.InterfaceRoom;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import xlk.takstar.paperless.base.BasePresenter;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.model.EventMessage;
import xlk.takstar.paperless.model.EventType;
import xlk.takstar.paperless.util.LogUtil;

/**
 * @author Created by xlk on 2020/12/2.
 * @desc
 */
public class SignPresenter extends BasePresenter<SignContract.View> implements SignContract.Presenter {
    private List<InterfaceMember.pbui_Item_MemberDetailInfo> memberDetailInfos = new ArrayList<>();
    private boolean isShow = true;
    private boolean isDownLoad;
    private Timer timer;
    private TimerTask task;

    public SignPresenter(SignContract.View view) {
        super(view);
    }

    @Override
    protected void busEvent(EventMessage msg) throws InvalidProtocolBufferException {
        switch (msg.getType()) {
            case EventType.BUS_ROOM_BG:
                String filepath = (String) msg.getObjects()[0];
                isDownLoad = true;
                mView.updateBg(filepath);
                break;
            //会场设备信息变更通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_ROOMDEVICE_VALUE:
                LogUtil.e(TAG, "BusEvent 会场设备信息变更通知 -->");
                executeLater();
                break;
            //会场信息变更通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_ROOM_VALUE:
                LogUtil.e(TAG, "BusEvent 会场信息变更通知 -->");
                queryMeetRoomBg();
                break;
            //签到变更
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETSIGN_VALUE:
                if (msg.getMethod() == InterfaceMacro.Pb_Method.Pb_METHOD_MEET_INTERFACE_NOTIFY_VALUE) {
                    placeDeviceRankingInfo();
                }
                break;
            //界面配置变更通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETFACECONFIG_VALUE:
                byte[] bytes = (byte[]) msg.getObjects()[0];
                InterfaceBase.pbui_MeetNotifyMsg pbui_meetNotifyMsg = InterfaceBase.pbui_MeetNotifyMsg.parseFrom(bytes);
                int id = pbui_meetNotifyMsg.getId();
                int opermethod = pbui_meetNotifyMsg.getOpermethod();
                LogUtil.i(TAG, "BusEvent 界面配置变更通知 id=" + id + ",opermethod=" + opermethod);
//                if (id == InterfaceMacro.Pb_MeetFaceID.Pb_MEET_FACE_SeatIcoShow_GEO_VALUE) {
//                    InterfaceFaceconfig.pbui_Type_FaceConfigInfo info = jni.queryInterFaceConfigurationById(id);
//                    if (info != null) {
//                        List<InterfaceFaceconfig.pbui_Item_FaceTextItemInfo> textList = info.getTextList();
//                        InterfaceFaceconfig.pbui_Item_FaceTextItemInfo item = textList.get(0);
//                        int flag = item.getFlag();
//                        boolean showFlag = (InterfaceMacro.Pb_MeetFaceFlag.Pb_MEET_FACEFLAG_SHOW_VALUE == (flag & InterfaceMacro.Pb_MeetFaceFlag.Pb_MEET_FACEFLAG_SHOW_VALUE));
//                    }
//                }
                queryInterFaceConfiguration();
                break;
            default:
                break;
        }
    }

    private void executeLater() {
        //解决短时间内收到很多通知，查询很多次的问题
        if (timer == null) {
            timer = new Timer();
            LogUtil.i(TAG, "创建timer");
            task = new TimerTask() {
                @Override
                public void run() {
                    placeDeviceRankingInfo();
                    task.cancel();
                    timer.cancel();
                    task = null;
                    timer = null;
                }
            };
            LogUtil.i(TAG, "500毫秒之后查询");
            timer.schedule(task, 500);
        }
    }

    public void queryMeetRoomBg() {
        try {
            int mediaId = jni.queryMeetRoomProperty(queryCurrentRoomId());
            if (mediaId != 0) {
                FileUtils.createOrExistsDir(Constant.download_dir);
                jni.downloadFile(Constant.download_dir + Constant.ROOM_BG + ".png", mediaId, 1, 0, Constant.ROOM_BG);
                return;
            }
            placeDeviceRankingInfo();
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    /**
     * 1.获取排位图表是否需要显示（小板凳子）
     */
    @Override
    public void queryInterFaceConfiguration() {
        InterfaceFaceconfig.pbui_Type_FaceConfigInfo info = jni.queryInterFaceConfiguration();
        if (info == null) {
            return;
        }
        List<InterfaceFaceconfig.pbui_Item_FaceTextItemInfo> textList = info.getTextList();
        for (int i = 0; i < textList.size(); i++) {
            InterfaceFaceconfig.pbui_Item_FaceTextItemInfo item = textList.get(i);
            LogUtil.i(TAG, "queryInterFaceConfiguration faceId=" + item.getFaceid());
            //判断排位图表是否显示
            if (item.getFaceid() == InterfaceMacro.Pb_MeetFaceID.Pb_MEET_FACE_SeatIcoShow_GEO_VALUE) {
                int flag = item.getFlag();
                boolean showFlag = (InterfaceMacro.Pb_MeetFaceFlag.Pb_MEET_FACEFLAG_SHOW_VALUE == (flag & InterfaceMacro.Pb_MeetFaceFlag.Pb_MEET_FACEFLAG_SHOW_VALUE));
                LogUtil.i(TAG, "queryInterFaceConfiguration 排位图标是否显示=" + showFlag + " ,当前是否显示=" + isShow + " ,是否已经下载过了底图=" + isDownLoad);
                if (isDownLoad) {
                    //底图已经下载过了，说明不是第一次
                    if (isShow == showFlag) {
                        //排位图标显示状态没改变
                        return;
                    } else {
                        //排位图标显示状态改变
                        isShow = showFlag;
                        placeDeviceRankingInfo();
                    }
                } else {
                    //第一次进入先下载底图
                    isShow = showFlag;
                    queryMeetRoomBg();
                }
                break;
            }
        }
    }

    @Override
    public void placeDeviceRankingInfo() {
        InterfaceRoom.pbui_Type_MeetRoomDevSeatDetailInfo meetRoomDevSeatDetailInfo = jni.placeDeviceRankingInfo(queryCurrentRoomId());
        if (meetRoomDevSeatDetailInfo == null) {
            return;
        }
        List<InterfaceRoom.pbui_Item_MeetRoomDevSeatDetailInfo> temps = new ArrayList<>();
        List<InterfaceRoom.pbui_Item_MeetRoomDevSeatDetailInfo> itemList = meetRoomDevSeatDetailInfo.getItemList();
        int checkedMemberCount = 0, allMemberCount = 0;
        for (int i = 0; i < itemList.size(); i++) {
            InterfaceRoom.pbui_Item_MeetRoomDevSeatDetailInfo item = itemList.get(i);
            if (Constant.isThisDevType(InterfaceMacro.Pb_DeviceIDType.Pb_DeviceIDType_MeetClient_VALUE, item.getDevid())) {
                allMemberCount++;
                temps.add(item);
                if (item.getIssignin() == 1) {
                    checkedMemberCount++;
                }
            }
        }
        mView.updateView(itemList, isShow, allMemberCount, checkedMemberCount);
    }
}
