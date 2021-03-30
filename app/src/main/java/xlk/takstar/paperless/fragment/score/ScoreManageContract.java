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

        void updateOnlineMemberRv();

        void updateScoreMemberRv();
    }

    interface Presenter extends IBasePresenter {
        void queryScore();

        void queryMember();

        void queryDevice();

        void querySubmittedVoters(int voteId);
    }
}
