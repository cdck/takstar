package xlk.takstar.paperless.fragment.agenda;

import xlk.takstar.paperless.base.IBaseView;
import xlk.takstar.paperless.base.IBasePresenter;

/**
 * @author Created by xlk on 2020/12/1.
 * @desc
 */
public interface AgendaContract {
    interface View extends IBaseView {
        void initDefault();

        void updateAgendaTv(String content);

        void displayFile(String path);

        void showTimeAgenda();

        void updateFileList();
    }

    interface Presenter extends IBasePresenter {
        void queryAgenda();
        void queryFileByDir(int dirId);
    }
}
