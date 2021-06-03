package xlk.takstar.paperless.fragment.vote;

import com.mogujie.tt.protobuf.InterfaceVote;

import java.util.List;

import xlk.takstar.paperless.base.IBasePresenter;
import xlk.takstar.paperless.base.IBaseView;

/**
 * @author Created by xlk on 2020/12/8.
 * @desc
 */
public interface VoteManageContract {
    interface View extends IBaseView{
        void updateVote(List<InterfaceVote.pbui_Item_MeetVoteDetailInfo> votes);

        void updateMembers();

        void showDetailsPop(InterfaceVote.pbui_Item_MeetVoteDetailInfo vote);

        void showChartPop(InterfaceVote.pbui_Item_MeetVoteDetailInfo vote);

        void updateExportDirPath(String dirPath);
    }
    interface Presenter extends IBasePresenter{

        void queryVote();
        void queryMember();

        void querySubmittedVoters(InterfaceVote.pbui_Item_MeetVoteDetailInfo vote, boolean details);
    }
}
