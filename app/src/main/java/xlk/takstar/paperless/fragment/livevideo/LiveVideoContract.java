package xlk.takstar.paperless.fragment.livevideo;

import java.util.List;

import xlk.takstar.paperless.base.IBasePresenter;
import xlk.takstar.paperless.base.IBaseView;
import xlk.takstar.paperless.model.bean.VideoDev;

/**
 * @author Created by xlk on 2020/12/2.
 * @desc
 */
public interface LiveVideoContract {
    interface View extends IBaseView{

        void updateRv(List<VideoDev> videoDevs);

        void updateDecode(Object[] objs);

        void updateYuv(Object[] objs1);

        void stopResWork(int resid);

        void notifyOnLineAdapter();
    }
    interface Presenter extends IBasePresenter{

    }
}
