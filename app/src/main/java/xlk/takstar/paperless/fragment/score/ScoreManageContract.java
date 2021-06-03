package xlk.takstar.paperless.fragment.score;

import xlk.takstar.paperless.base.IBasePresenter;
import xlk.takstar.paperless.base.IBaseView;

/**
 * @author Created by xlk on 2021/2/23.
 * @desc
 */
interface ScoreManageContract {
    interface View extends IBaseView {

        void updateScoreRv();

        void updateScoreSubmitMemberList();

        void addFile2List(String filePath, int mediaId);
    }

    interface Presenter extends IBasePresenter {
        void queryScore();

        void queryMemberDetailed();

        void queryScoreSubmittedScore(int voteId);
    }
}
