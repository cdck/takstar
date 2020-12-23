package xlk.takstar.paperless.model.bean;

import com.mogujie.tt.protobuf.InterfaceMember;
import com.mogujie.tt.protobuf.InterfaceRoom;

/**
 * @author Created by xlk on 2020/12/8.
 * @desc
 */
public class MemberRoleBean {

    InterfaceMember.pbui_Item_MemberDetailInfo member;
    InterfaceRoom.pbui_Item_MeetRoomDevSeatDetailInfo seat;

    public MemberRoleBean(InterfaceMember.pbui_Item_MemberDetailInfo member) {
        this.member = member;
    }

    public InterfaceMember.pbui_Item_MemberDetailInfo getMember() {
        return member;
    }

    public void setMember(InterfaceMember.pbui_Item_MemberDetailInfo member) {
        this.member = member;
    }

    public InterfaceRoom.pbui_Item_MeetRoomDevSeatDetailInfo getSeat() {
        return seat;
    }

    public void setSeat(InterfaceRoom.pbui_Item_MeetRoomDevSeatDetailInfo seat) {
        this.seat = seat;
    }
}
