package xlk.takstar.paperless.fragment.web;

import com.google.protobuf.InvalidProtocolBufferException;
import com.mogujie.tt.protobuf.InterfaceBase;
import com.mogujie.tt.protobuf.InterfaceMacro;

import java.util.ArrayList;
import java.util.List;

import xlk.takstar.paperless.base.BasePresenter;
import xlk.takstar.paperless.model.EventMessage;

/**
 * @author Created by xlk on 2020/12/2.
 * @desc
 */
public class WebPresenter extends BasePresenter<WebContract.View> implements WebContract.Presenter {

    private List<InterfaceBase.pbui_Item_UrlDetailInfo> webUrls = new ArrayList<>();

    public WebPresenter(WebContract.View view) {
        super(view);
    }

    @Override
    protected void busEvent(EventMessage msg) throws InvalidProtocolBufferException {
        switch (msg.getType()) {
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_DEFAULTURL_VALUE:
                queryWebUrl();
                break;
            default:
                break;
        }
    }

    @Override
    public void queryWebUrl() {
        InterfaceBase.pbui_meetUrl pbui_meetUrl = jni.queryWebUrl();
        webUrls.clear();
        if (pbui_meetUrl != null) {
            webUrls.addAll(pbui_meetUrl.getItemList());
        }
        mView.updateWebUrl(webUrls);
    }
}
