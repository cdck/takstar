package xlk.takstar.paperless.fragment.score;

import com.google.protobuf.InvalidProtocolBufferException;
import com.mogujie.tt.protobuf.InterfaceFilescorevote;
import com.mogujie.tt.protobuf.InterfaceMacro;

import java.util.ArrayList;
import java.util.List;

import xlk.takstar.paperless.base.BasePresenter;
import xlk.takstar.paperless.model.EventMessage;

/**
 * @author Created by xlk on 2021/2/23.
 * @desc 评分管理
 */
class ScoreManagePresenter extends BasePresenter<ScoreManageContract.View> implements ScoreManageContract.Presenter {

    public List<InterfaceFilescorevote.pbui_Type_Item_UserDefineFileScore> scoreList = new ArrayList<>();

    public ScoreManagePresenter(ScoreManageContract.View view) {
        super(view);
    }

    @Override
    public void queryScore() {
        InterfaceFilescorevote.pbui_Type_UserDefineFileScore object = jni.queryFileScore();
        scoreList.clear();
        if (object != null) {
            scoreList.addAll(object.getItemList());
        }
        mView.updateScoreRv();
    }

    @Override
    protected void busEvent(EventMessage msg) throws InvalidProtocolBufferException {
        switch (msg.getType()) {
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_FILESCOREVOTE_VALUE: {
                if (msg.getMethod() == InterfaceMacro.Pb_Method.Pb_METHOD_MEET_INTERFACE_NOTIFY_VALUE) {
                    queryScore();
                }
                break;
            }
            default:
                break;
        }
    }
}
