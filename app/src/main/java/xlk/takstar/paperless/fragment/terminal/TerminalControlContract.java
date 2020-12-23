package xlk.takstar.paperless.fragment.terminal;

import java.util.List;

import xlk.takstar.paperless.base.IBasePresenter;
import xlk.takstar.paperless.base.IBaseView;
import xlk.takstar.paperless.model.bean.ClientControlBean;
import xlk.takstar.paperless.model.bean.MemberRoleBean;

/**
 * @author Created by xlk on 2020/12/8.
 * @desc
 */
public interface TerminalControlContract {
    interface View extends IBaseView{

        void updateClient(List<ClientControlBean> clientControlBeans);

        void updateMemberRole(List<MemberRoleBean> memberRoleBeans);
    }
    interface Presenter extends IBasePresenter{

    }
}
