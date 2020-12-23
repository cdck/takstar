package xlk.takstar.paperless.fragment.web;

import com.mogujie.tt.protobuf.InterfaceBase;

import java.util.List;

import xlk.takstar.paperless.base.IBasePresenter;
import xlk.takstar.paperless.base.IBaseView;

/**
 * @author Created by xlk on 2020/12/2.
 * @desc
 */
public interface WebContract {
    interface View extends IBaseView {

        void updateWebUrl(List<InterfaceBase.pbui_Item_UrlDetailInfo> webUrls);
    }

    interface Presenter extends IBasePresenter {
        void queryWebUrl();
    }
}
