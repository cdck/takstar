package xlk.takstar.paperless.model.bean;

import com.mogujie.tt.protobuf.InterfaceMember;
import com.mogujie.tt.protobuf.InterfaceVote;

/**
 * @author Created by xlk on 2020/12/9.
 * @desc
 */
public class SubmitMember {

    InterfaceMember.pbui_Item_MeetMemberDetailInfo memberInfo;
    InterfaceVote.pbui_Item_MeetVoteSignInDetailInfo voteInfo;
    String answer;

    public SubmitMember(InterfaceMember.pbui_Item_MeetMemberDetailInfo memberInfo, InterfaceVote.pbui_Item_MeetVoteSignInDetailInfo voteInfo, String answer) {
        this.memberInfo = memberInfo;
        this.voteInfo = voteInfo;
        this.answer = answer;
    }

    public InterfaceMember.pbui_Item_MeetMemberDetailInfo getMemberInfo() {
        return memberInfo;
    }

    public void setMemberInfo(InterfaceMember.pbui_Item_MeetMemberDetailInfo memberInfo) {
        this.memberInfo = memberInfo;
    }

    public InterfaceVote.pbui_Item_MeetVoteSignInDetailInfo getVoteInfo() {
        return voteInfo;
    }

    public void setVoteInfo(InterfaceVote.pbui_Item_MeetVoteSignInDetailInfo voteInfo) {
        this.voteInfo = voteInfo;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
