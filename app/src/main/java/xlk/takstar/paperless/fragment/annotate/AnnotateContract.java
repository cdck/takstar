package xlk.takstar.paperless.fragment.annotate;

import com.mogujie.tt.protobuf.InterfaceFile;

import java.util.List;

import xlk.takstar.paperless.base.IBaseView;
import xlk.takstar.paperless.base.IBasePresenter;
import xlk.takstar.paperless.model.bean.SeatMember;

/**
 * @author Created by xlk on 2020/12/1.
 * @desc
 */
public interface AnnotateContract {
    interface View extends IBaseView {

        void updateMemberRv(List<SeatMember> seatMembers);

        void updateFiles(List<InterfaceFile.pbui_Item_MeetDirFileDetailInfo> annotateFiles);
    }

    interface Presenter extends IBasePresenter {
        void queryMember();

        void queryMeetRanking();

        void queryFile();

        void downloadFile(InterfaceFile.pbui_Item_MeetDirFileDetailInfo itemFile);
    }
}
