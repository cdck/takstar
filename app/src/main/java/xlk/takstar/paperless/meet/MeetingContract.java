package xlk.takstar.paperless.meet;

import com.mogujie.tt.protobuf.InterfaceDevice;

import java.util.List;

import xlk.takstar.paperless.base.IBaseView;
import xlk.takstar.paperless.base.IBasePresenter;
import xlk.takstar.paperless.model.bean.DevMember;

/**
 * @author Created by xlk on 2020/11/30.
 * @desc
 */
public interface MeetingContract {
    interface View extends IBaseView {

        /**
         * 更新在线状态
         */
        void updateOnline(String string);

        void updateMeetingName(String meetingName);

        void updateMemberName(String memberName);

        void updateMemberRole(String role);

        void jump2main();

        void collapseOtherFeature();

        void updateTime(String[] gtmDate);

        void updateMeetingFeatures();

        void showFragment(int code);

        void showPushView(List<DevMember> onlineMembers, List<InterfaceDevice.pbui_Item_DeviceDetailInfo> onLineProjectors, int mediaId);

        void exportNoteFile();

        void exitDraw();

        void updateMeetingBadgeNumber(int count);

        void setFirstDirId(int dirId);
    }

    interface Presenter extends IBasePresenter {

        void initial();

        void initVideoRes();

        void releaseVideoRes();

        void queryIsOnline();

        void queryDeviceMeetInfo();

        void queryLocalRole();

        void queryMeetingFeature();
    }
}
