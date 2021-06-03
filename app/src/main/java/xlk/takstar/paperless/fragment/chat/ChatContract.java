package xlk.takstar.paperless.fragment.chat;

import java.util.List;

import xlk.takstar.paperless.base.IBasePresenter;
import xlk.takstar.paperless.base.IBaseView;
import xlk.takstar.paperless.model.bean.ChatDeviceMember;
import xlk.takstar.paperless.model.bean.MyChatMessage;

/**
 * @author Created by xlk on 2020/12/2.
 * @desc
 */
public interface ChatContract {
    interface View extends IBaseView{

        void updateMeetingName(String meetingName);

        void updateMessageRv(List<MyChatMessage> messages);

        void updateMemberList();
    }
    interface Presenter extends IBasePresenter{

        void queryDeviceMeetInfo();

        void queryMember();

    }
}
