package xlk.takstar.paperless.fragment.agenda;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mogujie.tt.protobuf.InterfaceAgenda;
import com.mogujie.tt.protobuf.InterfaceBase;
import com.mogujie.tt.protobuf.InterfaceMacro;

import java.io.File;

import xlk.takstar.paperless.R;
import xlk.takstar.paperless.base.BasePresenter;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.model.EventMessage;
import xlk.takstar.paperless.model.EventType;
import xlk.takstar.paperless.model.GlobalValue;
import xlk.takstar.paperless.util.LogUtil;

/**
 * @author Created by xlk on 2020/12/1.
 * @desc
 */
public class AgendaPresenter extends BasePresenter<AgendaContract.View> implements AgendaContract.Presenter {
    public AgendaPresenter(AgendaContract.View view) {
        super(view);
    }

    @Override
    protected void busEvent(EventMessage msg) throws InvalidProtocolBufferException {
        switch (msg.getType()) {
            //议程文件下载完成
            case EventType.BUS_AGENDA_FILE:
                String path = (String) msg.getObjects()[0];
                mView.displayFile(path);
                break;
            case EventType.BUS_X5_INSTALL://腾讯X5内核加载完成
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETAGENDA_VALUE://议程变更通知
                queryAgenda();
                break;
            default:
                break;
        }
    }

    @Override
    public void queryAgenda() {
        mView.initDefault();
        InterfaceAgenda.pbui_meetAgenda meetAgenda = jni.queryAgenda();
        if (meetAgenda != null) {
            int agendatype = meetAgenda.getAgendatype();
            if (agendatype == InterfaceMacro.Pb_AgendaType.Pb_MEET_AGENDA_TYPE_TEXT_VALUE) {
                String s = meetAgenda.getText().toStringUtf8();
                mView.updateAgendaTv(s);
            } else if (agendatype == InterfaceMacro.Pb_AgendaType.Pb_MEET_AGENDA_TYPE_FILE_VALUE) {
                int mediaid = meetAgenda.getMediaid();
                byte[] bytes = jni.queryFileProperty(InterfaceMacro.Pb_MeetFilePropertyID.Pb_MEETFILE_PROPERTY_NAME.getNumber(), mediaid);
                InterfaceBase.pbui_CommonTextProperty textProperty = null;
                try {
                    textProperty = InterfaceBase.pbui_CommonTextProperty.parseFrom(bytes);
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                String fileName = textProperty.getPropertyval().toStringUtf8();
                LogUtil.i(TAG, "fun_queryAgenda 获取到文件议程 -->" + mediaid + ", 文件名：" + fileName);
                FileUtils.createOrExistsDir(Constant.download_dir);
                File file = new File(Constant.download_dir + fileName);
                if (file.exists()) {
                    if(GlobalValue.downloadingFiles.contains(mediaid)){
                        ToastUtils.showShort(R.string.file_downloading);
                    }else {
                        mView.displayFile(file.getAbsolutePath());
                    }
                }else {
                    jni.creationFileDownload(Constant.download_dir + fileName, mediaid, 1, 0, Constant.DOWNLOAD_AGENDA_FILE);
                }
            } else if (agendatype == InterfaceMacro.Pb_AgendaType.Pb_MEET_AGENDA_TYPE_TIME_VALUE) {

            }
        }
    }
}
