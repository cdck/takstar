package xlk.takstar.paperless.service.fab;

import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mogujie.tt.protobuf.InterfaceBase;
import com.mogujie.tt.protobuf.InterfaceBullet;
import com.mogujie.tt.protobuf.InterfaceDevice;
import com.mogujie.tt.protobuf.InterfaceFilescorevote;
import com.mogujie.tt.protobuf.InterfaceMacro;
import com.mogujie.tt.protobuf.InterfaceMember;
import com.mogujie.tt.protobuf.InterfaceVote;

import java.util.ArrayList;
import java.util.List;

import xlk.takstar.paperless.R;
import xlk.takstar.paperless.base.BasePresenter;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.model.EventMessage;
import xlk.takstar.paperless.model.EventType;
import xlk.takstar.paperless.model.GlobalValue;
import xlk.takstar.paperless.model.bean.DevMember;
import xlk.takstar.paperless.model.bean.JoinPro;
import xlk.takstar.paperless.service.CameraActivity;
import xlk.takstar.paperless.util.AppUtil;
import xlk.takstar.paperless.util.LogUtil;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * @author Created by xlk on 2020/12/7.
 * @desc
 */
public class FabPresenter extends BasePresenter<FabContract.View> implements FabContract.Presenter {

    private final Context cxt;
    private List<InterfaceMember.pbui_Item_MemberDetailInfo> memberDetailInfos = new ArrayList<>();
    public List<InterfaceDevice.pbui_Item_DeviceDetailInfo> deviceDetailInfos = new ArrayList<>();
    public List<InterfaceDevice.pbui_Item_DeviceDetailInfo> onLineProjectors = new ArrayList<>();
    public List<DevMember> onLineMember = new ArrayList<>();
    public List<InterfaceDevice.pbui_Item_DeviceResPlay> canJoinMembers = new ArrayList<>();
    public List<JoinPro> canJoinPros = new ArrayList<>();

    public FabPresenter(Context cxt, FabContract.View view) {
        super(view);
        this.cxt = cxt;
    }

    @Override
    protected void busEvent(EventMessage msg) throws InvalidProtocolBufferException {
        switch (msg.getType()) {
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_DEVICEINFO_VALUE://设备寄存器变更通知
                LogUtil.i(TAG, "BusEvent -->" + "设备寄存器变更通知");
                queryDevice();
                break;
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEMBER_VALUE://参会人员变更通知
                LogUtil.i(TAG, "BusEvent -->" + "参会人员变更通知");
                queryMember();
                break;
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_DEVICEMEETSTATUS_VALUE://界面状态变更通知
                LogUtil.i(TAG, "BusEvent -->" + "界面状态变更通知");
                queryMember();
                break;
            //有新的投票发起通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETONVOTING_VALUE: {
                byte[] o = (byte[]) msg.getObjects()[0];
                InterfaceBase.pbui_MeetNotifyMsg pbui_meetNotifyMsg = InterfaceBase.pbui_MeetNotifyMsg.parseFrom(o);
                int opermethod = pbui_meetNotifyMsg.getOpermethod();
                int id = pbui_meetNotifyMsg.getId();
                LogUtil.i(TAG, "BusEvent -->" + "有新的投票发起通知 opermethod=" + opermethod + ",id=" + id);
                if (opermethod == InterfaceMacro.Pb_Method.Pb_METHOD_MEET_INTERFACE_START_VALUE) {
//                    queryInitiateVote(id);
                    queryVoteById(id);
                }
                break;
            }
            //投票变更通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETVOTEINFO_VALUE: {
                byte[] o1 = (byte[]) msg.getObjects()[0];
                InterfaceBase.pbui_MeetNotifyMsg pbui_meetNotifyMsg1 = InterfaceBase.pbui_MeetNotifyMsg.parseFrom(o1);
                int opermethod = pbui_meetNotifyMsg1.getOpermethod();
                int id = pbui_meetNotifyMsg1.getId();
                LogUtil.i(TAG, "busEvent 投票变更通知 opermethod=" + opermethod + ",id=" + id);
                if (opermethod == InterfaceMacro.Pb_Method.Pb_METHOD_MEET_INTERFACE_STOP_VALUE) {
                    LogUtil.i(TAG, "BusEvent -->" + "收到结束投票通知 投票ID= " + id);
                    mView.closeVoteView();
                }
                break;
            }
            case EventType.BUS_COLLECT_CAMERA_START:
                int type = (int) msg.getObjects()[0];
                LogUtil.i(TAG, "BusEvent -->" + "收到开始采集摄像头通知 type= " + type);
                if (AppUtil.checkCamera(cxt, 1)) {
                    ToastUtils.showShort(R.string.opening_camera);
                    Intent intent = new Intent(cxt, CameraActivity.class);
                    intent.putExtra(Constant.EXTRA_CAMERA_TYPE, 1);
                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    cxt.startActivity(intent);
                } else if (AppUtil.checkCamera(cxt, 0)) {
                    ToastUtils.showShort(R.string.opening_camera);
                    Intent intent = new Intent(cxt, CameraActivity.class);
                    intent.putExtra(Constant.EXTRA_CAMERA_TYPE, 0);
                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    cxt.startActivity(intent);
                }
//                else {
//                    ToastUtils.showShort(R.string.not_find_camera);
//                }
//                mView.showOpenCamera();
                break;
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_DEVICEOPER_VALUE://设备交互信息
                if (msg.getMethod() == InterfaceMacro.Pb_Method.Pb_METHOD_MEET_INTERFACE_REQUESTINVITE_VALUE) {
                    byte[] o2 = (byte[]) msg.getObjects()[0];
                    InterfaceDevice.pbui_Type_DeviceChat info = InterfaceDevice.pbui_Type_DeviceChat.parseFrom(o2);
                    int inviteflag = info.getInviteflag();
                    int operdeviceid = info.getOperdeviceid();
                    LogUtil.i(TAG, "BusEvent -->" + "收到设备对讲的通知 inviteflag = " + inviteflag + ", operdeviceid= " + operdeviceid);
                    mView.showView(inviteflag, operdeviceid);
                } else if (msg.getMethod() == InterfaceMacro.Pb_Method.Pb_METHOD_MEET_INTERFACE_REQUESTPRIVELIGE_VALUE) {
                    LogUtil.i(TAG, "BusEvent -->" + "收到参会人权限请求");
                    byte[] o2 = (byte[]) msg.getObjects()[0];
                    InterfaceDevice.pbui_Type_MeetRequestPrivilegeNotify info = InterfaceDevice.pbui_Type_MeetRequestPrivilegeNotify.parseFrom(o2);
                    mView.applyPermissionsInform(info);
                }
                break;
            //公告通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETBULLET_VALUE: {
                byte[] bulletin = (byte[]) msg.getObjects()[0];
                if (msg.getMethod() == InterfaceMacro.Pb_Method.Pb_METHOD_MEET_INTERFACE_PUBLIST_VALUE) {
                    LogUtil.i(TAG, "发布公告通知");
                    InterfaceBullet.pbui_BulletDetailInfo detailInfo = InterfaceBullet.pbui_BulletDetailInfo.parseFrom(bulletin);
                    List<InterfaceBullet.pbui_Item_BulletDetailInfo> itemList = detailInfo.getItemList();
                    if (!itemList.isEmpty()) {
                        InterfaceBullet.pbui_Item_BulletDetailInfo info = itemList.get(0);
                        mView.showBulletWindow(info);
                    }
                } else if (msg.getMethod() == InterfaceMacro.Pb_Method.Pb_METHOD_MEET_INTERFACE_STOP_VALUE) {
                    LogUtil.i(TAG, "停止公告通知");
                    mView.closeBulletWindow();
                }
                break;
            }
            //成功读取到txt文件中的笔记内容
            case EventType.BUS_EXPORT_NOTE_CONTENT: {
                String content = (String) msg.getObjects()[0];
                mView.showNoteView(content);
                break;
            }
            //收到文件评分通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_FILESCOREVOTE_VALUE: {
                if (msg.getMethod() == InterfaceMacro.Pb_Method.Pb_METHOD_MEET_INTERFACE_START_VALUE) {
                    //收到发起文件评分
                    LogUtils.i(TAG, "评分通知 发起文件评分 Pb_METHOD_MEET_INTERFACE_START_VALUE");
                    byte[] object = (byte[]) msg.getObjects()[0];
                    InterfaceFilescorevote.pbui_Type_StartUserDefineFileScoreNotify info
                            = InterfaceFilescorevote.pbui_Type_StartUserDefineFileScoreNotify.parseFrom(object);
                    if (info != null) {
                        mView.showScoreView(info);
                    }
                }
                /*else if (msg.getMethod() == InterfaceMacro.Pb_Method.Pb_METHOD_MEET_INTERFACE_STOP_VALUE) {
                    //收到停止文件评分
                    LogUtils.i(TAG, "评分通知 停止文件评分 Pb_METHOD_MEET_INTERFACE_START_VALUE");
                    mView.closeScoreView();
                }
                */else if (msg.getMethod() == InterfaceMacro.Pb_Method.Pb_METHOD_MEET_INTERFACE_NOTIFY_VALUE) {

                    byte[] data = (byte[]) msg.getObjects()[0];
                    InterfaceBase.pbui_MeetNotifyMsg pbui_meetNotifyMsg = InterfaceBase.pbui_MeetNotifyMsg.parseFrom(data);
                    int id = pbui_meetNotifyMsg.getId();
                    int opermethod = pbui_meetNotifyMsg.getOpermethod();
                    LogUtils.d(TAG, "评分通知 评分变更 id= " + id + ", opermethod= " + opermethod);
                    if (opermethod == 30) {
                        mView.closeScoreView();
                    }
                }
                break;
            }
            default:
                break;
        }
    }

    private void queryVoteById(int voteId) {
        InterfaceVote.pbui_Type_MeetOnVotingDetailInfo info = jni.queryVoteById(voteId);
        if (info != null) {
            InterfaceVote.pbui_Item_MeetOnVotingDetailInfo item = info.getItem(0);
            mView.showVoteWindow(item);
        }
    }

    public void queryMember() {
        InterfaceMember.pbui_Type_MemberDetailInfo attendPeople = jni.queryMember();
        memberDetailInfos.clear();
        if (attendPeople == null) {
            return;
        }
        memberDetailInfos.addAll(attendPeople.getItemList());
        queryDevice();
    }

    private void queryDevice() {
        InterfaceDevice.pbui_Type_DeviceDetailInfo deviceDetailInfo = jni.queryDeviceInfo();
        if (deviceDetailInfo == null) {
            return;
        }
        deviceDetailInfos.clear();
        deviceDetailInfos.addAll(deviceDetailInfo.getPdevList());
        onLineProjectors.clear();
        onLineMember.clear();
        for (int i = 0; i < deviceDetailInfos.size(); i++) {
            InterfaceDevice.pbui_Item_DeviceDetailInfo detailInfo = deviceDetailInfos.get(i);
            int devcieid = detailInfo.getDevcieid();
            int memberid = detailInfo.getMemberid();
            int netstate = detailInfo.getNetstate();
            int facestate = detailInfo.getFacestate();
            if (devcieid == GlobalValue.localDeviceId) {
                continue;
            }
            if (netstate == 1) {//在线
                if (Constant.isThisDevType(InterfaceMacro.Pb_DeviceIDType.Pb_DeviceIDType_MeetProjective_VALUE, devcieid)) {//在线的投影机
                    onLineProjectors.add(detailInfo);
                } else {//查找在线参会人
                    if (facestate == 1) {//确保在会议界面
                        for (int j = 0; j < memberDetailInfos.size(); j++) {
                            InterfaceMember.pbui_Item_MemberDetailInfo info = memberDetailInfos.get(j);
                            if (info.getPersonid() == memberid) {
                                onLineMember.add(new DevMember(detailInfo, info));
                                break;
                            }
                        }
                    }
                }
            }
        }
        mView.notifyOnLineAdapter();
    }

    public void queryCanJoin() {
        InterfaceDevice.pbui_Type_DeviceResPlay pbui_type_deviceResPlay = jni.queryCanJoin();
        canJoinPros.clear();
        canJoinMembers.clear();
        if (pbui_type_deviceResPlay == null) {
            mView.notifyJoinAdapter();
            return;
        }
        List<InterfaceDevice.pbui_Item_DeviceResPlay> pdevList = pbui_type_deviceResPlay.getPdevList();
        for (int i = 0; i < pdevList.size(); i++) {
            InterfaceDevice.pbui_Item_DeviceResPlay resPlay = pdevList.get(i);
            int devceid = resPlay.getDevceid();
            boolean isPro = Constant.isThisDevType(InterfaceMacro.Pb_DeviceIDType.Pb_DeviceIDType_MeetProjective_VALUE, devceid);
            if (isPro) {//投影机
                for (InterfaceDevice.pbui_Item_DeviceDetailInfo info : deviceDetailInfos) {
                    if (info.getDevcieid() == devceid) {
                        canJoinPros.add(new JoinPro(resPlay, info));
                    }
                }
            } else {
                canJoinMembers.add(resPlay);
            }
        }
        mView.notifyJoinAdapter();
    }

    public String getMemberNameByDevid(int devid) {
        for (int i = 0; i < onLineMember.size(); i++) {
            DevMember devMember = onLineMember.get(i);
            if (devMember.getDeviceDetailInfo().getDevcieid() == devid) {
                return devMember.getMemberDetailInfo().getName().toStringUtf8();
            }
        }
        return "";
    }

    public String getMemberNameById(int memberid) {
        for (int i = 0; i < memberDetailInfos.size(); i++) {
            InterfaceMember.pbui_Item_MemberDetailInfo memberDetailInfo = memberDetailInfos.get(i);
            if (memberDetailInfo.getPersonid() == memberid) {
                return memberDetailInfo.getName().toStringUtf8();
            }
        }
        return "";
    }
}
