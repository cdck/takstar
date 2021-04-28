package xlk.takstar.paperless.admin.activity;

import com.blankj.utilcode.util.LogUtils;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mogujie.tt.protobuf.InterfaceBase;
import com.mogujie.tt.protobuf.InterfaceDevice;
import com.mogujie.tt.protobuf.InterfaceMacro;

import xlk.takstar.paperless.base.BasePresenter;
import xlk.takstar.paperless.model.EventMessage;
import xlk.takstar.paperless.model.GlobalValue;
import xlk.takstar.paperless.util.DateUtil;

import static xlk.takstar.paperless.model.Constant.RESOURCE_ID_0;

/**
 * @author Created by xlk on 2021/4/20.
 * @desc
 */
public class AdminPresenter extends BasePresenter<AdminContract.View> implements AdminContract.Presenter {

    public AdminPresenter(AdminContract.View view) {
        super(view);
        //  修改本机界面状态
        jni.modifyContextProperties(InterfaceMacro.Pb_ContextPropertyID.Pb_MEETCONTEXT_PROPERTY_ROLE_VALUE,
                InterfaceMacro.Pb_MeetFaceStatus.Pb_MemState_AdminFace_VALUE);
        //强制缓存会议信息
        jni.cacheData(InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETINFO_VALUE, 1, 1);
        // 缓存会议排位
        jni.cacheData(InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETSEAT_VALUE);
        //缓存会议桌牌
        jni.cacheData(InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETTABLECARD_VALUE);
        // 缓存会议室
        jni.cacheData(InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_ROOM_VALUE);
        // 缓存会场设备
        jni.cacheData(InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_ROOMDEVICE_VALUE);
        //缓存会议目录
        jni.cacheData(InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETDIRECTORY_VALUE);
        //会议目录文件
        jni.cacheData(InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETDIRECTORYFILE_VALUE);
        //缓存会议评分
        jni.cacheData(InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_FILESCOREVOTESIGN_VALUE);
        // 缓存参会人信息
        jni.cacheData(InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEMBER_VALUE);
        //缓存参会人权限
        jni.cacheData(InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEMBERPERMISSION_VALUE);
        //缓存投票信息
        jni.cacheData(InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETVOTEINFO_VALUE);
        //人员签到
        jni.cacheData(InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETSIGN_VALUE);
        //公告信息
        jni.cacheData(InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETBULLET_VALUE);
        //会议视频
        jni.cacheData(InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETVIDEO_VALUE);
        //会议管理员
        jni.cacheData(InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_ADMIN_VALUE);
        //会议管理员控制的会场
        jni.cacheData(InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MANAGEROOM_VALUE);
        //缓存会议目录权限
        jni.cacheData(InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETDIRECTORYRIGHT_VALUE);
        //缓存任务
        jni.cacheData(InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETTASK_VALUE);
        initVideoRes();
    }

    private void initVideoRes() {
        jni.initVideoRes(RESOURCE_ID_0, GlobalValue.screen_width, GlobalValue.screen_height);
    }

    @Override
    public void onDestroy() {
        releaseVideoRes();
        super.onDestroy();
    }

    void releaseVideoRes() {
        jni.releaseVideoRes(RESOURCE_ID_0);
    }


    @Override
    protected void busEvent(EventMessage msg) throws InvalidProtocolBufferException {
        switch (msg.getType()){
            //时间回调
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_TIME_VALUE: {
                Object[] objs = msg.getObjects();
                byte[] data = (byte[]) objs[0];
                InterfaceBase.pbui_Time pbui_time = InterfaceBase.pbui_Time.parseFrom(data);
                //微秒 转换成毫秒 除以 1000
                String[] adminTime = DateUtil.convertAdminTime(pbui_time.getUsec() / 1000);
                mView.updateTime(adminTime);
                break;
            }
            //设备寄存器变更通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_DEVICEINFO_VALUE: {
                LogUtils.i(TAG, "BusEvent 设备寄存器变更通知");
                mView.updateOnlineStatus();
                break;
            }
            default:break;
        }
    }
}
