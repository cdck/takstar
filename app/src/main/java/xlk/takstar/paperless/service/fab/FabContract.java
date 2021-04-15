package xlk.takstar.paperless.service.fab;

import com.mogujie.tt.protobuf.InterfaceBullet;
import com.mogujie.tt.protobuf.InterfaceDevice;
import com.mogujie.tt.protobuf.InterfaceFilescorevote;
import com.mogujie.tt.protobuf.InterfaceVote;

import xlk.takstar.paperless.base.IBasePresenter;
import xlk.takstar.paperless.base.IBaseView;

/**
 * @author Created by xlk on 2020/12/11.
 * @desc
 */
public interface FabContract {
    interface View extends IBaseView{
        void notifyOnLineAdapter();

        void notifyJoinAdapter();

        void closeVoteView();

        void showView(int inviteflag, int operdeviceid);

        void applyPermissionsInform(InterfaceDevice.pbui_Type_MeetRequestPrivilegeNotify info);

        void showVoteView(InterfaceVote.pbui_Item_MeetOnVotingDetailInfo item);

        void showOpenCamera(int inviteflag, int operdeviceid);

        void showBulletWindow(InterfaceBullet.pbui_Item_BulletDetailInfo bulletid);

        void closeBulletWindow();

        void showVoteWindow(InterfaceVote.pbui_Item_MeetOnVotingDetailInfo item);

        /**
         * 展示笔记视图
         * @param content 笔记内容
         */
        void showNoteView(String content);

        /**
         * 展示提交评分视图
         * @param info
         */
        void showScoreView(InterfaceFilescorevote.pbui_Type_StartUserDefineFileScoreNotify info);

        void closeScoreView(int id);
    }
    interface Presenter extends IBasePresenter{}
}
