package xlk.takstar.paperless.model.bean;

import com.mogujie.tt.protobuf.InterfaceFilescorevote;

import java.util.List;

/**
 * @author Created by xlk on 2021/7/5.
 * @desc 导出评分查看的结果为PDF文件
 */
public class PdfRateInfo {
    String fileName;
    InterfaceFilescorevote.pbui_Type_Item_UserDefineFileScore score;
    List<ScoreMember> scoreMembers;

    public PdfRateInfo(String fileName, InterfaceFilescorevote.pbui_Type_Item_UserDefineFileScore score, List<ScoreMember> scoreMembers) {
        this.fileName = fileName;
        this.score = score;
        this.scoreMembers = scoreMembers;
    }

    public String getFileName() {
        return fileName;
    }

    public InterfaceFilescorevote.pbui_Type_Item_UserDefineFileScore getScore() {
        return score;
    }

    public List<ScoreMember> getScoreMembers() {
        return scoreMembers;
    }
}
