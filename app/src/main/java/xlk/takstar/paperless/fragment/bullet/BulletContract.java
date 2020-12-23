package xlk.takstar.paperless.fragment.bullet;

import com.mogujie.tt.protobuf.InterfaceBullet;

import java.util.List;

import xlk.takstar.paperless.base.IBasePresenter;
import xlk.takstar.paperless.base.IBaseView;

/**
 * @author Created by xlk on 2020/12/11.
 * @desc
 */
public interface BulletContract {
    interface View extends IBaseView {
        void updateBullet(List<InterfaceBullet.pbui_Item_BulletDetailInfo> bullets);
    }

    interface Presenter extends IBasePresenter {

        void queryBullet();
    }
}
