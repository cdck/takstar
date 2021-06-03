package xlk.takstar.paperless.model.bean;

import com.mogujie.tt.protobuf.InterfaceDevice;
import com.mogujie.tt.protobuf.InterfaceMember;
import com.mogujie.tt.protobuf.InterfaceRoom;

/**
 * @author Created by xlk on 2020/12/3.
 * @desc
 */
public class ChatDeviceMember {
    InterfaceMember.pbui_Item_MeetMemberDetailInfo memberDetail;
    InterfaceRoom.pbui_Item_MeetSeatDetailInfo seat;
    /**
     * 未读消息数量
     */
    int count;
    /**
     * 最后一次查看该参会人发送的消息，用于记录未读消息
     */
    long lastCheckTime;

    public ChatDeviceMember(InterfaceMember.pbui_Item_MeetMemberDetailInfo memberDetail, InterfaceRoom.pbui_Item_MeetSeatDetailInfo seat) {
        this.memberDetail = memberDetail;
        this.seat = seat;
    }

    public int getMemberId() {
        return memberDetail.getMemberid();
    }

    public String getMemberName() {
        return memberDetail.getMembername().toStringUtf8();
    }

    public int getRole(){
        return seat.getRole();
    }

    public int getDeviceId() {
        return memberDetail.getDevid();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getLastCheckTime() {
        return lastCheckTime;
    }

    public void setLastCheckTime(long lastCheckTime) {
        this.lastCheckTime = lastCheckTime;
    }
}
