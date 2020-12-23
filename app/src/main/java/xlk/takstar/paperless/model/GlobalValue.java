package xlk.takstar.paperless.model;

import com.mogujie.tt.protobuf.InterfaceMember;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by xlk on 2020/11/27.
 * @desc
 */
public class GlobalValue {

    public static int screen_width, screen_height, half_width, half_height;
    public static int camera_width,camera_height;
    /**
     * 是否初始化完成
     */
    public static boolean initializationIsOver;

    public static int localDeviceId = 0;
    public static int localMemberId = 0;
    public static String localMemberName;
    /**
     * 存放正在下载中的媒体ID，下载退出后进行删除
     */
    public static List<Integer> downloadingFiles = new ArrayList<>();

    /**
     * 本机参会人角色
     */
    public static int localRole;

    /**
     * 是否加载完成（不等于成功加载X5，也可能加载的是系统内核）
     */
    public static boolean initX5Finished = false;
    /**
     * 是否正在播放
     */
    public static boolean isVideoPlaying;
    /**
     * 是否正在被强制性播放中
     */
    public static boolean isMandatoryPlaying;
    /**
     * 是否有新的播放
     */
    public static boolean haveNewPlayInform;
    /**
     * 本机是否拥有全部权限
     */
    public static boolean hasAllPermission;
    /**
     * 存放所有参会人的权限
     */
    public static List<InterfaceMember.pbui_Item_MemberPermission> allPermissions;
    /**
     * 本机的权限
     */
    public static int localPermission;
    /**
     * 画板的操作ID
     */
    public static int operid;
}
