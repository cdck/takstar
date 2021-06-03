package xlk.takstar.paperless.model.bean;

import com.mogujie.tt.protobuf.InterfaceFilescorevote;
import com.mogujie.tt.protobuf.InterfaceMember;

/**
 * @author xlk
 * @date 2020/4/9
 * @desc
 */
public class ScoreMember {
    InterfaceFilescorevote.pbui_Type_Item_FileScoreMemberStatistic score;
    InterfaceMember.pbui_Item_MeetMemberDetailInfo member;

    public ScoreMember(InterfaceFilescorevote.pbui_Type_Item_FileScoreMemberStatistic score, InterfaceMember.pbui_Item_MeetMemberDetailInfo member) {
        this.score = score;
        this.member = member;
    }

    public InterfaceFilescorevote.pbui_Type_Item_FileScoreMemberStatistic getScore() {
        return score;
    }

    public InterfaceMember.pbui_Item_MeetMemberDetailInfo getMember() {
        return member;
    }
}
