package xlk.takstar.paperless.model;

import android.content.Context;
import android.os.Environment;

import com.mogujie.tt.protobuf.InterfaceMacro;

import xlk.takstar.paperless.R;

import static xlk.takstar.paperless.model.GlobalValue.hasAllPermission;
import static xlk.takstar.paperless.model.GlobalValue.localPermission;
import static xlk.takstar.paperless.model.GlobalValue.screen_width;

/**
 * @author Created by xlk on 2020/11/26.
 * @desc
 */
public class Constant {
    public static final String root_dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/TakstarPaperless/";
    public static final String logcat_dir = root_dir + "myLog";
    public static final String file_dir = root_dir + "文件/";
    public static final String download_dir = file_dir + "下载文件/";
    public static final String export_dir = file_dir + "导出文件/";

    /**
     * 各个界面的背景图和logo图标
     */
    public static final String MAIN_BG = "main_bg";
    public static final String MAIN_LOGO = "ic_logo_main";
    public static final String MEET_BG = "meet_bg";
    public static final String MEET_LOGO = "meet_logo";
    public static final String BULLETIN_BG = "bulletin_bg";
    public static final String BULLETIN_LOGO = "bulletin_logo";
    public static final String ROOM_BG = "room_bg";

    //固定的会议目录ID
    /**
     * 共享文件目录ID
     */
    public static final int SHARED_FILE_DIRECTORY_ID = 1;
    /**
     * 批注文件目录ID
     */
    public static final int ANNOTATION_FILE_DIRECTORY_ID = 2;

    //上传文件标识
    public static final String UPLOAD_CHOOSE_FILE = "upload_choose_file";
    public static final String UPLOAD_DRAW_PIC = "upload_draw_pic";
    public static final String UPLOAD_WPS_FILE = "upload_wps_file";
    /**
     * 上传背景图片时的标识
     */
    public static final String UPLOAD_BACKGROUND_IMAGE = "upload_background_image";
    /**
     * 上传桌牌背景图片时的标识
     */
    public static final String UPLOAD_TABLE_CARD_BACKGROUND_IMAGE = "upload_table_card_background_image";
    /**
     * 上传会议发布文件
     */
    public static final String UPLOAD_PUBLISH_FILE = "upload_publish_file";
    /**
     * 上传升级文件
     */
    public static final String UPLOAD_UPGRADE_FILE = "upload_upgrade_file";

    //下载文件标识
    public static final String DOWNLOAD_AGENDA_FILE = "download_agenda_file";
    /**
     * 下载会议资料文件
     */
    public static final String DOWNLOAD_MATERIAL_FILE = "download_material_file";
    /**
     * 下载完成需要打开的文件
     */
    public static final String DOWNLOAD_SHOULD_OPEN_FILE = "download_should_open_file";

    public static final int RESOURCE_ID_0 = 0;
    public static final int RESOURCE_ID_1 = 1;
    public static final int RESOURCE_ID_2 = 2;
    public static final int RESOURCE_ID_3 = 3;
    public static final int RESOURCE_ID_4 = 4;
    public static final int RESOURCE_ID_10 = 10;
    public static final int RESOURCE_ID_11 = 11;

    public static final int SCREEN_SUB_ID = 2;
    public static final int CAMERA_SUB_ID = 3;


    //自定义其它功能的功能码

    public static final int FUN_CODE = 200000;
    public static final int FUN_CODE_TERMINAL = FUN_CODE + 1;
    public static final int FUN_CODE_VOTE = FUN_CODE + 2;
    public static final int FUN_CODE_ELECTION = FUN_CODE + 3;
    public static final int FUN_CODE_VIDEO = FUN_CODE + 4;
    public static final int FUN_CODE_SCREEN = FUN_CODE + 5;
    public static final int FUN_CODE_BULLETIN = FUN_CODE + 6;

    /**
     * 发起播放的类型
     */
    public static final String EXTRA_VIDEO_ACTION = "extra_video_action";
    /**
     * 发起播放的设备ID
     */
    public static final String EXTRA_VIDEO_DEVICE_ID = "extra_video_device_id";
    /**
     * 发起播放的设备id流通道
     */
    public static final String EXTRA_VIDEO_SUB_ID = "extra_video_sub_id";
    /**
     * 发起播放的文件类型
     */
    public static final String EXTRA_VIDEO_SUBTYPE = "extra_video_subtype";
    /**
     * 播放的媒体id
     */
    public static final String EXTRA_VIDEO_MEDIA_ID = "extra_video_media_id";
    /**
     * 设备对讲
     */
    public static final String EXTRA_INVITE_FLAG = "extra_invite_flag";
    public static final String EXTRA_OPERATING_DEVICE_ID = "extra_operating_device_id";
    /**
     * 传入摄像头前置/后置
     */
    public static final String EXTRA_CAMERA_TYPE = "extra_camera_type";


    /**
     * 投票时提交，用于签到参与投票
     */
    public static final int PB_VOTE_SELFLAG_CHECKIN = 0x80000000;

    //文件类别

    //  大类
    /**
     * 音频
     */
    public static final int MEDIA_FILE_TYPE_AUDIO = 0x00000000;
    /**
     * 视频
     */
    public static final int MEDIA_FILE_TYPE_VIDEO = 0x20000000;
    /**
     * 录制
     */
    public static final int MEDIA_FILE_TYPE_RECORD = 0x40000000;
    /**
     * 图片
     */
    public static final int MEDIA_FILE_TYPE_PICTURE = 0x60000000;
    /**
     * 升级
     */
    public static final int MEDIA_FILE_TYPE_UPDATE = 0xe0000000;
    /**
     * 临时文件
     */
    public static final int MEDIA_FILE_TYPE_TEMP = 0x80000000;
    /**
     * 其它文件
     */
    public static final int MEDIA_FILE_TYPE_OTHER = 0xa0000000;
    public static final int MAIN_TYPE_BITMASK = 0xe0000000;

    //  小类
    /**
     * PCM文件
     */
    public static final int MEDIA_FILE_TYPE_PCM = 0x01000000;
    /**
     * MP3文件
     */
    public static final int MEDIA_FILE_TYPE_MP3 = 0x02000000;
    /**
     * WAV文件
     */
    public static final int MEDIA_FILE_TYPE_ADPCM = 0x03000000;
    /**
     * FLAC文件
     */
    public static final int MEDIA_FILE_TYPE_FLAC = 0x04000000;
    /**
     * MP4文件
     */
    public static final int MEDIA_FILE_TYPE_MP4 = 0x07000000;
    /**
     * MKV文件
     */
    public static final int MEDIA_FILE_TYPE_MKV = 0x08000000;
    /**
     * RMVB文件
     */
    public static final int MEDIA_FILE_TYPE_RMVB = 0x09000000;
    /**
     * RM文件
     */
    public static final int MEDIA_FILE_TYPE_RM = 0x0a000000;
    /**
     * AVI文件
     */
    public static final int MEDIA_FILE_TYPE_AVI = 0x0b000000;
    /**
     * bmp文件
     */
    public static final int MEDIA_FILE_TYPE_BMP = 0x0c000000;
    /**
     * jpeg文件
     */
    public static final int MEDIA_FILE_TYPE_JPEG = 0x0d000000;
    /**
     * png文件
     */
    public static final int MEDIA_FILE_TYPE_PNG = 0x0e000000;
    /**
     * 其它文件
     */
    public static final int MEDIA_FILE_TYPE_OTHER_SUB = 0x10000000;

    public static final int SUB_TYPE_BITMASK = 0x1f000000;


    /* **** **  权限码  ** **** */
    /**
     * 同屏权限
     */
    public static final int permission_code_screen = 1;
    /**
     * 投影权限
     */
    public static final int permission_code_projection = 2;
    /**
     * 上传权限
     */
    public static final int permission_code_upload = 4;
    /**
     * 下载权限
     */
    public static final int permission_code_download = 8;
    /**
     * 投票权限
     */
    public static final int permission_code_vote = 16;

    /**
     * 发送广播时的action和extra
     */
    public static final String ACTION_START_SCREEN_RECORD = "action_start_screen_record";
    public static final String ACTION_STOP_SCREEN_RECORD = "action_stop_screen_record";
    /**
     * 退出应用时发送广播通知停止掉屏幕录制
     */
    public static final String ACTION_STOP_SCREEN_RECORD_WHEN_EXIT_APP = "action_stop_screen_record_when_exit_app";
    /**
     * 要采集的类型，值为2或3，屏幕或摄像头
     */
    public static final String EXTRA_CAPTURE_TYPE = "extra_capture_type";

    /**
     * 判断本机是否拥有某权限
     */
    public static boolean hasPermission(int code) {
        if (hasAllPermission) {
            return true;
        }
        return (localPermission & code) == code;
    }

    /**
     * 判断权限码是否有某一权限
     *
     * @param permission 权限
     * @param code       某一权限的权限码
     */
    public static boolean isHasPermission(int permission, int code) {
        return (permission & code) == code;
    }

    /**
     * 判断设备是否是当前{@param type}类型
     *
     * @param type  类型
     * @param devId 设备ID
     * @see InterfaceMacro.Pb_DeviceIDType
     */
    public static boolean isThisDevType(int type, int devId) {
        return (devId & Constant.DEVICE_MEET_ID_MASK) == type;
    }

    public static final int DEVICE_MEET_ID_MASK = 0xfffc0000;


    /**
     * 获取该设备的类型名称
     *
     * @return 返回空表示是未识别的设备（服务器设备）
     */
    public static String getDeviceTypeName(Context context, int deviceId) {
        if (isThisDevType(InterfaceMacro.Pb_DeviceIDType.Pb_DeviceIDType_MeetDBServer_VALUE, deviceId)) {
            //会议数据库设备
            return context.getString(R.string.database_dev);
        } else if (isThisDevType(InterfaceMacro.Pb_DeviceIDType.Pb_DeviceIDType_MeetService_VALUE, deviceId)) {
            //会议茶水设备
            return context.getString(R.string.tea_dev);
        } else if (isThisDevType(InterfaceMacro.Pb_DeviceIDType.Pb_DeviceIDType_MeetProjective_VALUE, deviceId)) {
            //会议投影设备
            return context.getString(R.string.pro_dev);
        } else if (isThisDevType(InterfaceMacro.Pb_DeviceIDType.Pb_DeviceIDType_MeetCapture_VALUE, deviceId)) {
            //会议流采集设备
            return context.getString(R.string.capture_dev);
        } else if (isThisDevType(InterfaceMacro.Pb_DeviceIDType.Pb_DeviceIDType_MeetClient_VALUE, deviceId)) {
            //会议终端设备
            return context.getString(R.string.client_dev);
        } else if (isThisDevType(InterfaceMacro.Pb_DeviceIDType.Pb_DeviceIDType_MeetVideoClient_VALUE, deviceId)) {
            //会议视屏对讲客户端
            return context.getString(R.string.video_chat_dev);
        } else if (isThisDevType(InterfaceMacro.Pb_DeviceIDType.Pb_DeviceIDType_MeetPublish_VALUE, deviceId)) {
            //会议发布
            return context.getString(R.string.release_dev);
        } else if (isThisDevType(InterfaceMacro.Pb_DeviceIDType.Pb_DEVICE_MEET_PHPCLIENT_VALUE, deviceId)) {
            //PHP中转数据设备
        } else if (isThisDevType(InterfaceMacro.Pb_DeviceIDType.Pb_DeviceIDType_MeetShare_VALUE, deviceId)) {
            //会议一键同屏设备
            return context.getString(R.string.screen_dev);
        }
        return "";
    }

    /**
     * 获取操作界面名称
     */
    public static String getInterfaceStateName(Context context, int state) {
        if (state == InterfaceMacro.Pb_MeetFaceStatus.Pb_MemState_MainFace_VALUE) {
            return context.getString(R.string.face_main);
        } else if (state == InterfaceMacro.Pb_MeetFaceStatus.Pb_MemState_MemFace_VALUE) {
            return context.getString(R.string.face_meet);
        } else if (state == InterfaceMacro.Pb_MeetFaceStatus.Pb_MemState_AdminFace_VALUE) {
            return context.getString(R.string.face_back);
        } else if (state == InterfaceMacro.Pb_MeetFaceStatus.Pb_MemState_VoteFace_VALUE) {
            return context.getString(R.string.face_vote);
        }
        return "";
    }

    /**
     * 获取参会人身份名称
     *
     * @param context 上下文
     * @param role    身份代码  InterfaceMacro.Pb_MeetMemberRole
     * @return 秘书、管理员...
     */
    public static String getMemberRoleName(Context context, int role) {
        switch (role) {
            case InterfaceMacro.Pb_MeetMemberRole.Pb_role_member_normal_VALUE:
                return context.getString(R.string.member_role_ordinary);
            case InterfaceMacro.Pb_MeetMemberRole.Pb_role_member_compere_VALUE:
                return context.getString(R.string.member_role_host);
            case InterfaceMacro.Pb_MeetMemberRole.Pb_role_member_secretary_VALUE:
                return context.getString(R.string.member_role_secretary);
            case InterfaceMacro.Pb_MeetMemberRole.Pb_role_admin_VALUE:
                return context.getString(R.string.member_role_admin);
            default:
                return context.getString(R.string.none);
        }
    }

    public static String getVoteType(Context context, int type) {
        switch (type) {
            case InterfaceMacro.Pb_MeetVote_SelType.Pb_VOTE_TYPE_4_5_VALUE:
                return context.getString(R.string.vote_type_4_5);
            case InterfaceMacro.Pb_MeetVote_SelType.Pb_VOTE_TYPE_3_5_VALUE:
                return context.getString(R.string.vote_type_3_5);
            case InterfaceMacro.Pb_MeetVote_SelType.Pb_VOTE_TYPE_2_5_VALUE:
                return context.getString(R.string.vote_type_2_5);
            case InterfaceMacro.Pb_MeetVote_SelType.Pb_VOTE_TYPE_2_3_VALUE:
                return context.getString(R.string.vote_type_2_3);
            default:
                return context.getString(R.string.vote_type_single);
        }
    }

    public static String getVoteState(Context context, int votestate) {
        if (votestate == InterfaceMacro.Pb_MeetVoteStatus.Pb_vote_notvote_VALUE) {
            return context.getString(R.string.vote_state_not_initiated);
        } else if (votestate == InterfaceMacro.Pb_MeetVoteStatus.Pb_vote_voteing_VALUE) {
            return context.getString(R.string.vote_state_ongoing);
        } else {
            return context.getString(R.string.vote_state_has_ended);
        }
    }


    //编码类型
    /**
     * VP8 video (i.e. video in .webm)
     */
    public static final String MIME_VIDEO_VP8 = "video/x-vnd.on2.vp8";
    /**
     * VP9 video (i.e. video in .webm)
     */
    public static final String MIME_VIDEO_VP9 = "video/x-vnd.on2.vp9";
    /**
     *  H.263 video
     */
    public static final String MIME_VIDEO_3GPP = "video/3gpp";
    /**
     * SCREEN_HEIGHT.264/AVC video
     */
    public static final String MIME_VIDEO_AVC = "video/avc";

    /**
     * SCREEN_HEIGHT.265/HEVC video
     */
    public static final String MIME_VIDEO_HEVC = "video/hevc";
    /**
     * MPEG4 video
     */
    public static final String MIME_VIDEO_MPEG4 = "video/mp4v-es";

    /**
     * 获取指定的MimeType
     *
     * @param codecId 后台回调ID
     * @return MimeType
     */
    public static String getMimeType(int codecId) {
        switch (codecId) {
            case 12:
            case 13:
                return MIME_VIDEO_MPEG4;
            case 139:
            case 140:
                return MIME_VIDEO_VP8;
            case 167:
            case 168:
                return MIME_VIDEO_VP9;
            case 173:
            case 174:
                return MIME_VIDEO_HEVC;
            default:
                return MIME_VIDEO_AVC;
        }
    }

}
