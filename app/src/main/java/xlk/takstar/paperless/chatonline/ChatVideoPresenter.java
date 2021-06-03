package xlk.takstar.paperless.chatonline;

import com.google.protobuf.InvalidProtocolBufferException;

import xlk.takstar.paperless.base.BasePresenter;
import xlk.takstar.paperless.model.EventMessage;

/**
 * @author Created by xlk on 2020/12/18.
 * @desc
 */
public class ChatVideoPresenter extends BasePresenter<ChatVideoContract.View> implements ChatVideoContract.Presenter {
    public ChatVideoPresenter(ChatVideoContract.View view) {
        super(view);
    }

    @Override
    protected void busEvent(EventMessage msg) throws InvalidProtocolBufferException {

    }

}
