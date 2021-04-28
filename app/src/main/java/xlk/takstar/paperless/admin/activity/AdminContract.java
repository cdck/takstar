package xlk.takstar.paperless.admin.activity;

import xlk.takstar.paperless.base.IBasePresenter;
import xlk.takstar.paperless.base.IBaseView;

/**
 * @author Created by xlk on 2021/4/20.
 * @desc
 */
interface AdminContract {
    interface View extends IBaseView{

        void updateOnlineStatus();

        void updateTime(String[] adminTime);
    }
    interface Presenter extends IBasePresenter{
    }
}
