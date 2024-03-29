package xlk.takstar.paperless.fragment.bullet;

import com.blankj.utilcode.util.LogUtils;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mogujie.tt.protobuf.InterfaceBullet;
import com.mogujie.tt.protobuf.InterfaceMacro;

import java.util.ArrayList;
import java.util.List;

import xlk.takstar.paperless.base.BasePresenter;
import xlk.takstar.paperless.model.EventMessage;
import xlk.takstar.paperless.util.LogUtil;

/**
 * @author Created by xlk on 2020/12/11.
 * @desc
 */
public class BulletPresenter extends BasePresenter<BulletContract.View> implements BulletContract.Presenter {

    private List<InterfaceBullet.pbui_Item_BulletDetailInfo> bullets = new ArrayList<>();

    public BulletPresenter(BulletContract.View view) {
        super(view);
    }

    @Override
    protected void busEvent(EventMessage msg) throws InvalidProtocolBufferException {
        switch (msg.getType()) {
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEETBULLET_VALUE:
                LogUtil.d(TAG, "BusEvent -->" + "公告变更通知");
                queryBullet();
                break;
            default:
                break;
        }
    }

    @Override
    public void queryBullet() {
        InterfaceBullet.pbui_BulletDetailInfo info = jni.queryBullet();
        bullets.clear();
        if (info != null) {
            bullets.addAll(info.getItemList());
            for (int i = 0; i < bullets.size(); i++) {
                InterfaceBullet.pbui_Item_BulletDetailInfo item = bullets.get(i);
                String title = item.getTitle().toStringUtf8();
                String content = item.getContent().toStringUtf8();
                int type = item.getType();
                int starttime = item.getStarttime();
                int timeouts = item.getTimeouts();
                LogUtils.e(TAG, "公告title：" + title + ",内容=" + content + "\ntype=" + type + ",starttime=" + starttime + ",timeouts=" + timeouts);
            }
        }
        mView.updateBullet(bullets);
    }
}
