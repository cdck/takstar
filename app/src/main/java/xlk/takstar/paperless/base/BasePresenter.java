package xlk.takstar.paperless.base;

import com.google.protobuf.InvalidProtocolBufferException;
import com.mogujie.tt.protobuf.InterfaceContext;
import com.mogujie.tt.protobuf.InterfaceMacro;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import xlk.takstar.paperless.model.EventMessage;
import xlk.takstar.paperless.model.JniHelper;
import xlk.takstar.paperless.util.LogUtil;

/**
 * @author Created by xlk on 2020/11/28.
 * @desc
 */
public abstract class BasePresenter<T extends IBaseView>{
    protected String TAG = this.getClass().getSimpleName() + "-->";
    protected JniHelper jni = JniHelper.getInstance();
    public T mView;

    public BasePresenter(T view) {
        mView = view;
        register();
    }

    public void register() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    public void unregister() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void onDestroy() {
        unregister();
    }

    /**
     * 获取当前会议的会议id
     *
     * @return 会议id
     */
    protected int queryCurrentMeetId() {
        InterfaceContext.pbui_MeetContextInfo info = jni.queryContextProperty(
                InterfaceMacro.Pb_ContextPropertyID.Pb_MEETCONTEXT_PROPERTY_CURMEETINGID_VALUE);
        int propertyval = info.getPropertyval();
        LogUtil.i(TAG, "queryCurrentMeetId 当前会议的会议id=" + propertyval);
        return propertyval;
    }


    /**
     * 获取当前会议的会场id
     *
     * @return 会场id
     */
    protected int queryCurrentRoomId() {
        InterfaceContext.pbui_MeetContextInfo info = jni.queryContextProperty(
                InterfaceMacro.Pb_ContextPropertyID.Pb_MEETCONTEXT_PROPERTY_CURROOMID_VALUE);
        int propertyval = info.getPropertyval();
        LogUtil.i(TAG, "queryCurrentRoomId 当前会议的会场id=" + propertyval);
        return propertyval;
    }

    /**
     * EventBus发送的消息交给子类去处理
     *
     * @param msg 消息数据
     * @throws InvalidProtocolBufferException byte数组转指定结构体时的异常，避免子类中一直try catch
     */
    protected abstract void busEvent(EventMessage msg) throws InvalidProtocolBufferException;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMessage(EventMessage msg) throws InvalidProtocolBufferException {
        busEvent(msg);
    }

}
