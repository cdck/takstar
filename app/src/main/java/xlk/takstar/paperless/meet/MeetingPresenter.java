package xlk.takstar.paperless.meet;

import android.content.Context;
import android.content.DialogInterface;

import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mogujie.tt.protobuf.InterfaceBase;
import com.mogujie.tt.protobuf.InterfaceDevice;
import com.mogujie.tt.protobuf.InterfaceFile;
import com.mogujie.tt.protobuf.InterfaceIM;
import com.mogujie.tt.protobuf.InterfaceMacro;
import com.mogujie.tt.protobuf.InterfaceMeetfunction;
import com.mogujie.tt.protobuf.InterfaceMember;
import com.mogujie.tt.protobuf.InterfaceWhiteboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cc.shinichi.library.ImagePreview;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.base.BasePresenter;
import xlk.takstar.paperless.fragment.draw.DrawFragment;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.model.EventMessage;
import xlk.takstar.paperless.model.EventType;
import xlk.takstar.paperless.model.GlobalValue;
import xlk.takstar.paperless.model.bean.DevMember;
import xlk.takstar.paperless.model.bean.MyChatMessage;
import xlk.takstar.paperless.model.node.FeaturesChildNode;
import xlk.takstar.paperless.model.node.FeaturesFootNode;
import xlk.takstar.paperless.model.node.FeaturesParentNode;
import xlk.takstar.paperless.ui.ArtBoard;
import xlk.takstar.paperless.util.DateUtil;
import xlk.takstar.paperless.util.DialogUtil;
import xlk.takstar.paperless.util.LogUtil;

import static xlk.takstar.paperless.App.appContext;
import static xlk.takstar.paperless.fragment.draw.DrawFragment.disposePicOpermemberid;
import static xlk.takstar.paperless.fragment.draw.DrawFragment.disposePicSrcmemid;
import static xlk.takstar.paperless.fragment.draw.DrawFragment.disposePicSrcwbidd;
import static xlk.takstar.paperless.fragment.draw.DrawFragment.isDrawing;
import static xlk.takstar.paperless.fragment.draw.DrawFragment.savePicData;
import static xlk.takstar.paperless.fragment.draw.DrawFragment.tempPicData;
import static xlk.takstar.paperless.meet.MeetingActivity.chatIsShowing;
import static xlk.takstar.paperless.model.Constant.RESOURCE_ID_0;
import static xlk.takstar.paperless.model.Constant.RESOURCE_ID_10;
import static xlk.takstar.paperless.model.Constant.RESOURCE_ID_11;

/**
 * @author Created by xlk on 2020/11/30.
 * @desc
 */
public class MeetingPresenter extends BasePresenter<MeetingContract.View> implements MeetingContract.Presenter {

    private final Context context;
    public List<BaseNode> features = new ArrayList<>();
    List<String> picPath = new ArrayList<>();
    public static HashMap<Integer, List<MyChatMessage>> imMessages = new HashMap<>();
    private FeaturesParentNode materialNode;

    public MeetingPresenter(MeetingContract.View view, Context context) {
        super(view);
        this.context = context;
    }

    @Override
    public void initial() {
        //  修改本机界面状态
        jni.modifyContextProperties(InterfaceMacro.Pb_ContextPropertyID.Pb_MEETCONTEXT_PROPERTY_ROLE_VALUE,
                InterfaceMacro.Pb_MeetFaceStatus.Pb_MemState_MemFace_VALUE);
        //缓存会议目录
        jni.cacheData(InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETDIRECTORY.getNumber());
        //会议目录文件
        jni.cacheData(InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETDIRECTORYFILE.getNumber());
        //缓存会议评分
        jni.cacheData(InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_FILESCOREVOTESIGN.getNumber());
        // 缓存会场设备
        jni.cacheData(InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_ROOMDEVICE.getNumber());
        //缓存会场设备
        jni.cacheData(InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_ROOMDEVICE.getNumber());
        // 缓存会议排位
        jni.cacheData(InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETSEAT.getNumber());
        // 缓存参会人信息
        jni.cacheData(InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEMBER.getNumber());
        //缓存参会人权限
        jni.cacheData(InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEMBERPERMISSION.getNumber());
        //缓存投票信息
        jni.cacheData(InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETVOTEINFO.getNumber());
        //人员签到
        jni.cacheData(InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETSIGN.getNumber());
        //公告信息
        jni.cacheData(InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETBULLET.getNumber());
        //会议视频
        jni.cacheData(InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETVIDEO.getNumber());
        initVideoRes();
        queryIsOnline();
        queryDeviceMeetInfo();
        queryMeetingFeature();
        queryPermission();
    }

    private void queryPermission() {
        InterfaceMember.pbui_Type_MemberPermission info = jni.queryAttendPeoplePermissions();
        if (info != null) {
            GlobalValue.allPermissions = info.getItemList();
            for (int i = 0; i < GlobalValue.allPermissions.size(); i++) {
                InterfaceMember.pbui_Item_MemberPermission item = GlobalValue.allPermissions.get(i);
                if (item.getMemberid() == GlobalValue.localMemberId) {
                    GlobalValue.localPermission = item.getPermission();
                }
            }
        }
    }

    @Override
    public void initVideoRes() {
        jni.initVideoRes(RESOURCE_ID_0, GlobalValue.screen_width, GlobalValue.screen_height);
        jni.initVideoRes(RESOURCE_ID_10, GlobalValue.screen_width, GlobalValue.screen_height);
        jni.initVideoRes(RESOURCE_ID_11, GlobalValue.screen_width, GlobalValue.screen_height);
    }

    @Override
    public void releaseVideoRes() {
        jni.releaseVideoRes(RESOURCE_ID_0);
        jni.releaseVideoRes(RESOURCE_ID_10);
        jni.releaseVideoRes(RESOURCE_ID_11);
    }

    //查询本机是否在线
    @Override
    public void queryIsOnline() {
        byte[] bytes = jni.queryDevicePropertiesById(InterfaceMacro.Pb_MeetDevicePropertyID.Pb_MEETDEVICE_PROPERTY_NETSTATUS_VALUE, GlobalValue.localDeviceId);
        if (bytes == null) {
            mView.updateOnline(appContext.getResources().getString(R.string.offline));
            return;
        }
        try {
            InterfaceDevice.pbui_DeviceInt32uProperty pbui_deviceInt32uProperty = InterfaceDevice.pbui_DeviceInt32uProperty.parseFrom(bytes);
            int propertyval = pbui_deviceInt32uProperty.getPropertyval();
            mView.updateOnline((propertyval == 1) ? appContext.getResources().getString(R.string.online) : appContext.getResources().getString(R.string.offline));
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void queryDeviceMeetInfo() {
        InterfaceDevice.pbui_Type_DeviceFaceShowDetail deviceMeetInfo = jni.queryDeviceMeetInfo();
        if (deviceMeetInfo != null) {
            GlobalValue.localMemberId = deviceMeetInfo.getMemberid();
            GlobalValue.localMemberName = deviceMeetInfo.getMembername().toStringUtf8();
            mView.updateMeetingName(deviceMeetInfo.getMeetingname().toStringUtf8());
            mView.updateMemberName(deviceMeetInfo.getMembername().toStringUtf8());
        }
        queryLocalRole();
    }

    @Override
    public void queryLocalRole() {
        InterfaceBase.pbui_CommonInt32uProperty property = jni.queryMeetRankingProperty(InterfaceMacro.Pb_MeetSeatPropertyID.Pb_MEETSEAT_PROPERTY_ROLEBYMEMBERID.getNumber());
        if (property != null) {
            GlobalValue.localRole = property.getPropertyval();
            if (GlobalValue.localRole == InterfaceMacro.Pb_MeetMemberRole.Pb_role_member_compere.getNumber()
                    || GlobalValue.localRole == InterfaceMacro.Pb_MeetMemberRole.Pb_role_member_secretary.getNumber()
                    || GlobalValue.localRole == InterfaceMacro.Pb_MeetMemberRole.Pb_role_admin.getNumber()) {
                GlobalValue.hasAllPermission = true;
                if (GlobalValue.localRole == InterfaceMacro.Pb_MeetMemberRole.Pb_role_member_compere.getNumber()) {
                    mView.updateMemberRole(appContext.getString(R.string.role_hosts_));
                } else if (GlobalValue.localRole == InterfaceMacro.Pb_MeetMemberRole.Pb_role_member_compere.getNumber()) {
                    mView.updateMemberRole(appContext.getString(R.string.role_secretary_));
                } else {
                    mView.updateMemberRole(appContext.getString(R.string.role_manager_));
                }
            } else {
                GlobalValue.hasAllPermission = false;
                mView.collapseOtherFeature();
                mView.updateMemberRole(appContext.getString(R.string.role_member_));
            }
        }
    }

    @Override
    public void queryMeetingFeature() {
        InterfaceMeetfunction.pbui_Type_MeetFunConfigDetailInfo funConfigDetailInfo = jni.queryMeetFunction();
        //获取之前的其它功能模块是否展开
        boolean isExpanded = false;
        for (int i = 0; i < features.size(); i++) {
            BaseNode baseNode = features.get(i);
            if (baseNode instanceof FeaturesFootNode) {
                FeaturesFootNode node = (FeaturesFootNode) baseNode;
                isExpanded = node.isExpanded();
                break;
            }
        }
        features.clear();
        materialNode = null;
        if (funConfigDetailInfo != null) {
            List<InterfaceMeetfunction.pbui_Item_MeetFunConfigDetailInfo> itemList = funConfigDetailInfo.getItemList();
            for (int i = 0; i < itemList.size(); i++) {
                InterfaceMeetfunction.pbui_Item_MeetFunConfigDetailInfo item = itemList.get(i);
                int funcode = item.getFuncode();
                LogUtils.i(TAG, "queryMeetingFunction 功能码=" + funcode);
                /*if (funcode != InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_SHAREDFILE_VALUE
                        && funcode != InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_VOTERESULT_VALUE
                        && funcode != InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_DOCUMENT_VALUE
                )*/
                if (funcode == InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_AGENDA_BULLETIN_VALUE
                        || funcode == InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_MATERIAL_VALUE
                        || funcode == InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_POSTIL_VALUE
                        || funcode == InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_MESSAGE_VALUE
                        || funcode == InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_VIDEOSTREAM_VALUE
                        || funcode == InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_WHITEBOARD_VALUE
                        || funcode == InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_WEBBROWSER_VALUE
                        || funcode == InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_SIGNINRESULT_VALUE
                ) {
                    FeaturesParentNode parentNode = new FeaturesParentNode(funcode);
                    features.add(parentNode);
                    if (funcode == InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_MATERIAL_VALUE) {
                        materialNode = parentNode;
                        queryDir(materialNode);
                    }
                }
            }
        }
        features.add(new FeaturesFootNode(isExpanded));
        mView.updateMeetingFeatures();
    }

    @Override
    public boolean hasThisFeature(int id) {
        for (int i = 0; i < features.size(); i++) {

        }
        return false;
    }

    public void queryDir(FeaturesParentNode parentNode) {
        InterfaceFile.pbui_Type_MeetDirDetailInfo info = jni.queryMeetDir();
        if (parentNode != null) {
            List<BaseNode> childs = new ArrayList<>();
            if (info != null) {
                List<InterfaceFile.pbui_Item_MeetDirDetailInfo> itemList = info.getItemList();
                for (int i = 0; i < itemList.size(); i++) {
                    InterfaceFile.pbui_Item_MeetDirDetailInfo item = itemList.get(i);
                    if (item.getId() != Constant.ANNOTATION_FILE_DIRECTORY_ID) {
                        childs.add(new FeaturesChildNode(item.getId(), item.getName().toStringUtf8()));
                    }
                }
            }
            LogUtils.i(TAG, "目录个数：" + childs.size());
            parentNode.setChildNode(childs);
            mView.updateMeetingFeatures();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseVideoRes();
    }

    @Override
    protected void busEvent(EventMessage msg) throws InvalidProtocolBufferException {
        switch (msg.getType()) {
            //时间回调
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_TIME_VALUE: {
                Object[] objs = msg.getObjects();
                byte[] data = (byte[]) objs[0];
                InterfaceBase.pbui_Time pbui_time = InterfaceBase.pbui_Time.parseFrom(data);
                //微秒 转换成毫秒 除以 1000
                String[] gtmDate = DateUtil.getGTMDate(pbui_time.getUsec() / 1000);
                mView.updateTime(gtmDate);
                break;
            }
            //设备会议信息变更通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_DEVICEFACESHOW_VALUE: {
                LogUtil.i(TAG, "BusEvent -->" + "设备会议信息变更通知");
                queryDeviceMeetInfo();
                break;
            }
            //会场设备信息变更通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_ROOMDEVICE_VALUE: {
                byte[] o = (byte[]) msg.getObjects()[0];
                InterfaceBase.pbui_MeetNotifyMsgForDouble msgForDouble = InterfaceBase.pbui_MeetNotifyMsgForDouble.parseFrom(o);
                int id = msgForDouble.getId();
                int subid = msgForDouble.getSubid();
                int opermethod = msgForDouble.getOpermethod();
                if (opermethod == 4 && subid == GlobalValue.localDeviceId) {
                    LogUtil.d(TAG, "BusEvent -->" + "会场设备信息变更通知 退到主界面 id=" + id + ", subid= " + subid);
                    mView.jump2main();
                }
                break;
            }
            //会议功能变更通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_FUNCONFIG_VALUE: {
                LogUtil.d(TAG, "BusEvent -->" + "会议功能变更通知");
                queryMeetingFeature();
                break;
            }
            //会议排位变更通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETSEAT_VALUE: {
                LogUtil.d(TAG, "BusEvent -->" + "会议排位变更通知");
                queryLocalRole();
                break;
            }
            //参会人员权限变更通知    pbui_MeetNotifyMsg
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEMBERPERMISSION_VALUE: {
                LogUtil.i(TAG, "busEvent 参会人员权限变更通知");
                queryPermission();
                break;
            }
            //打开预览图片文件
            case EventType.BUS_PREVIEW_IMAGE: {
                String filepath = (String) msg.getObjects()[0];
                LogUtil.i(TAG, "BusEvent 将要打开的图片路径：" + filepath);
                int index = 0;
                if (!picPath.contains(filepath)) {
                    picPath.add(filepath);
                    index = picPath.size() - 1;
                } else {
                    for (int i = 0; i < picPath.size(); i++) {
                        if (picPath.get(i).equals(filepath)) {
                            index = i;
                        }
                    }
                }
                previewImage(index);
                break;
            }
            //会议交流信息
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETIM_VALUE: {
                if (!chatIsShowing) {
                    byte[] o = (byte[]) msg.getObjects()[0];
                    InterfaceIM.pbui_Type_MeetIM meetIM = InterfaceIM.pbui_Type_MeetIM.parseFrom(o);
                    LogUtil.i(TAG, "busEvent 收到会议交流信息 参会人id=" + meetIM.getMemberid() + ",名称=" + meetIM.getMembername().toStringUtf8() + ",内容=" + meetIM.getMsg().toStringUtf8());
                    //文本类消息
                    if (meetIM.getMsgtype() == InterfaceMacro.Pb_MeetIMMSG_TYPE.Pb_MEETIM_CHAT_Message_VALUE) {
                        int badgeNumber = MeetingActivity.mBadge.getBadgeNumber();
                        MeetingActivity.mBadge.setBadgeNumber(++badgeNumber);
                        int memberid = meetIM.getMemberid();
                        MyChatMessage newImMsg = new MyChatMessage(0, meetIM.getMembername().toStringUtf8(), memberid, meetIM.getUtcsecond(), meetIM.getMsg().toStringUtf8());
                        List<MyChatMessage> myChatMessages;
                        if (imMessages.containsKey(memberid)) {
                            myChatMessages = imMessages.get(memberid);
                            LogUtil.i(TAG, "busEvent 获取消息数据 " + myChatMessages.size());
                        } else {
                            myChatMessages = new ArrayList<>();
                            LogUtil.i(TAG, "busEvent 新建消息数据");
                        }
                        myChatMessages.add(newImMsg);
                        imMessages.put(memberid, myChatMessages);
                    }
                }
                break;
            }
            //收到打开白板通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_WHITEBOARD_VALUE: {
                if (msg.getMethod() == InterfaceMacro.Pb_Method.Pb_METHOD_MEET_INTERFACE_ASK_VALUE) {
//                    if (!isDrawing) {
                    openArtBoardInform(msg);
//                    }
                }
                break;
            }
            //推动文件
            case EventType.BUS_PUSH_FILE: {
                int mediaId = (int) msg.getObjects()[0];
                pushFile(mediaId);
                break;
            }
            //导入笔记
            case EventType.BUS_CHOOSE_NOTE_FILE: {
                mView.exportNoteFile();
                break;
            }
            //会议目录
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETDIRECTORY_VALUE: {
                if (materialNode != null) {
                    queryDir(materialNode);
                }
                break;
            }
            //添加新的未读消息提示抖动动画
            case EventType.BUS_UNREAD_MESSAGE_TIP: {
                // TODO: 2021/2/23 添加新的未读消息提示抖动动画
                break;
            }
            //退出电子白板
            case EventType.BUS_EXIT_DRAW: {
                mView.exitDraw();
                break;
            }
            default:
                break;
        }
    }

    List<InterfaceDevice.pbui_Item_DeviceDetailInfo> deviceDetailInfos = new ArrayList<>();
    List<DevMember> onlineMembers = new ArrayList<>();
    List<InterfaceDevice.pbui_Item_DeviceDetailInfo> onLineProjectors = new ArrayList<>();

    public void pushFile(int mediaId) {
        InterfaceDevice.pbui_Type_DeviceDetailInfo deviceDetailInfo = jni.queryDeviceInfo();
        if (deviceDetailInfo == null) {
            return;
        }
        List<InterfaceDevice.pbui_Item_DeviceDetailInfo> pdevList = deviceDetailInfo.getPdevList();
        deviceDetailInfos.clear();
        deviceDetailInfos.addAll(pdevList);
        onlineMembers.clear();
        onLineProjectors.clear();
        InterfaceMember.pbui_Type_MemberDetailInfo attendPeople = jni.queryMember();
        List<InterfaceMember.pbui_Item_MemberDetailInfo> itemList = attendPeople.getItemList();
        for (int i = 0; i < deviceDetailInfos.size(); i++) {
            InterfaceDevice.pbui_Item_DeviceDetailInfo detailInfo = deviceDetailInfos.get(i);
            int devcieid = detailInfo.getDevcieid();
            int netstate = detailInfo.getNetstate();
            int memberid = detailInfo.getMemberid();
            int facestate = detailInfo.getFacestate();
            if (Constant.isThisDevType(InterfaceMacro.Pb_DeviceIDType.Pb_DeviceIDType_MeetProjective_VALUE, devcieid)
                    && netstate == 1) {
                onLineProjectors.add(detailInfo);
            }
            if (netstate == 1 && facestate == 1 && devcieid != GlobalValue.localDeviceId) {
                for (int j = 0; j < itemList.size(); j++) {
                    InterfaceMember.pbui_Item_MemberDetailInfo memberDetailInfo = itemList.get(j);
                    if (memberDetailInfo.getPersonid() == memberid) {
                        onlineMembers.add(new DevMember(detailInfo, memberDetailInfo));
                    }
                }
            }
        }
        mView.showPushView(onlineMembers, onLineProjectors, mediaId);
    }

    private void openArtBoardInform(EventMessage msg) throws InvalidProtocolBufferException {
        LogUtil.i(TAG, "openArtBoardInform 收到打开白板通知");
        byte[] o = (byte[]) msg.getObjects()[0];
        InterfaceWhiteboard.pbui_Type_MeetStartWhiteBoard object = InterfaceWhiteboard.pbui_Type_MeetStartWhiteBoard.parseFrom(o);
        int operflag = object.getOperflag();//指定操作标志 参见Pb_MeetPostilOperType
        String medianame = object.getMedianame().toStringUtf8();//白板操作描述
        disposePicOpermemberid = object.getOpermemberid();//当前该命令的人员ID
        disposePicSrcmemid = object.getSrcmemid();//发起人的人员ID 白板标识使用
        disposePicSrcwbidd = object.getSrcwbid();//发起人的白板标识 取微秒级的时间作标识 白板标识使用
        if (operflag == InterfaceMacro.Pb_MeetPostilOperType.Pb_MEETPOTIL_FLAG_FORCEOPEN.getNumber()) {
            LogUtil.i(TAG, "openArtBoardInform: 强制打开白板  直接强制同意加入..");
            jni.agreeJoin(GlobalValue.localMemberId, disposePicSrcmemid, disposePicSrcwbidd);
            DrawFragment.isSharing = true;//如果同意加入就设置已经在共享中
            DrawFragment.mSrcmemid = disposePicSrcmemid;//设置发起的人员ID
            DrawFragment.mSrcwbid = disposePicSrcwbidd;//设置白板标识
            mView.showFragment(InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_WHITEBOARD_VALUE);
        } else if (operflag == InterfaceMacro.Pb_MeetPostilOperType.Pb_MEETPOTIL_FLAG_REQUESTOPEN.getNumber()) {
            LogUtil.i(TAG, "openArtBoardInform: 询问打开白板..");
            whetherOpen(disposePicSrcmemid, disposePicSrcwbidd, medianame, disposePicOpermemberid);
        }
    }

    private void whetherOpen(int srcmemid, long srcwbidd, String medianame, int opermemberid) {
        DialogUtil.createDialog(context, context.getString(R.string.title_whether_agree_join, medianame),
                context.getString(R.string.agree), context.getString(R.string.reject), new DialogUtil.onDialogClickListener() {
                    @Override
                    public void positive(DialogInterface dialog) {
                        //同意加入
                        jni.agreeJoin(GlobalValue.localMemberId, srcmemid, srcwbidd);
                        DrawFragment.isSharing = true;//如果同意加入就设置已经在共享中
                        DrawFragment.mSrcmemid = srcmemid;//设置发起的人员ID
                        DrawFragment.mSrcwbid = srcwbidd;
                        if (tempPicData != null) {
                            savePicData = tempPicData;
                            /** **** **  作为接收者保存  ** **** **/
                            ArtBoard.DrawPath drawPath = new ArtBoard.DrawPath();
                            drawPath.operid = GlobalValue.operid;
                            drawPath.srcwbid = srcwbidd;
                            drawPath.srcmemid = srcmemid;
                            drawPath.opermemberid = opermemberid;
                            drawPath.picdata = savePicData;
                            GlobalValue.operid = 0;
                            tempPicData = null;
                            //将路径保存到共享中绘画信息
                            DrawFragment.pathList.add(drawPath);
                        }
                        mView.showFragment(InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_WHITEBOARD_VALUE);
                        //自己不是发起人的时候,每次收到绘画通知都要判断是不是同一个发起人和白板标识
                        //并且集合中没有这一号人,将其添加进集合中
                        if (!DrawFragment.togetherIDs.contains(opermemberid)) {
                            DrawFragment.togetherIDs.add(opermemberid);
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void negative(DialogInterface dialog) {
                        jni.rejectJoin(GlobalValue.localMemberId, srcmemid, srcwbidd);
                        dialog.dismiss();
                    }

                    @Override
                    public void dismiss(DialogInterface dialog) {

                    }
                });
    }

    private void previewImage(int index) {
        if (picPath.isEmpty()) {
            return;
        }
        ImagePreview.getInstance()
                .setContext(context)
                .setImageList(picPath)//设置图片地址集合
                .setIndex(index)//设置开始的索引
                .setShowDownButton(false)//设置是否显示下载按钮
                .setShowCloseButton(false)//设置是否显示关闭按钮
                .setEnableDragClose(true)//设置是否开启下拉图片退出
                .setEnableUpDragClose(true)//设置是否开启上拉图片退出
                .setEnableClickClose(true)//设置是否开启点击图片退出
                .setShowErrorToast(true)
                .start();
    }
}
