package xlk.takstar.paperless.fragment.sign;

import com.mogujie.tt.protobuf.InterfaceRoom;

import java.util.List;

import xlk.takstar.paperless.base.IBasePresenter;
import xlk.takstar.paperless.base.IBaseView;

/**
 * @author Created by xlk on 2020/12/2.
 * @desc
 */
public interface SignContract {
    interface View extends IBaseView{

        void updateView(List<InterfaceRoom.pbui_Item_MeetRoomDevSeatDetailInfo> itemList, boolean isShow,int allMemberCount, int checkedMemberCount);

        void updateSignin(int yqd, int size);

        void updateBg(String filepath);
    }
    interface Presenter extends IBasePresenter{

        void queryInterFaceConfiguration();
        void placeDeviceRankingInfo();
    }
}
