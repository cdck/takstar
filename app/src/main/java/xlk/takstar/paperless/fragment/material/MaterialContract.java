package xlk.takstar.paperless.fragment.material;

import com.mogujie.tt.protobuf.InterfaceDevice;
import com.mogujie.tt.protobuf.InterfaceFile;

import java.util.List;

import xlk.takstar.paperless.base.IBaseView;
import xlk.takstar.paperless.base.IBasePresenter;
import xlk.takstar.paperless.model.bean.DevMember;

/**
 * @author Created by xlk on 2020/12/1.
 * @desc
 */
public interface MaterialContract {
    interface View extends IBaseView {
        void updateDir();

        void updateFile(List<InterfaceFile.pbui_Item_MeetDirFileDetailInfo> meetFiles);

    }

    interface Presenter extends IBasePresenter {
        void queryDir();

        void queryFileByDir(int dirId);

        void cleanFile();

        void downloadFiles(InterfaceFile.pbui_Item_MeetDirFileDetailInfo itemFile);
    }
}
