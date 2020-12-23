package xlk.takstar.paperless.fragment.screen;

import xlk.takstar.paperless.base.IBasePresenter;
import xlk.takstar.paperless.base.IBaseView;

/**
 * @author Created by xlk on 2020/12/10.
 * @desc
 */
public interface ScreenManageContract {
    interface View extends IBaseView{

        void updateRecyclerView();
    }
    interface Presenter extends IBasePresenter{

        void queryData();
    }
}
