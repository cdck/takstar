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
    interface View extends IBaseView {

        /**
         * @param itemList           会议排位信息
         * @param isShow             是否展示图标
         * @param allMemberCount     所有会议终端的数量
         * @param checkedMemberCount 已经签到的参会人数量
         */
        void updateView(List<InterfaceRoom.pbui_Item_MeetRoomDevSeatDetailInfo> itemList, boolean isShow, int allMemberCount, int checkedMemberCount);

        void updateBg(String filepath);
    }

    interface Presenter extends IBasePresenter {

        void queryInterFaceConfiguration();

        void placeDeviceRankingInfo();
    }
}
