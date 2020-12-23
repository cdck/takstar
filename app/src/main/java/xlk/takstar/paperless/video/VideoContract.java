package xlk.takstar.paperless.video;

import android.view.Surface;

import java.util.List;

import xlk.takstar.paperless.base.IBasePresenter;
import xlk.takstar.paperless.base.IBaseView;

/**
 * @author Created by xlk on 2020/12/10.
 * @desc
 */
public interface VideoContract {
    interface View extends IBaseView {
        void close();

        void setCanNotExit();

        void updateProgressUi(int per, String currentTime, String totalTime);

        void setCodecType(int type);

        void updateYuv(int w, int h, byte[] y, byte[] u, byte[] v);

        void updateTopTitle(String title);

        void updateAnimator(int status);

        void updateRecyclerView();
    }

    interface Presenter extends IBasePresenter {
        void queryMember();

        void queryDevice();

        void stopPlay();

        void releaseMediaRes();

        void releasePlay();

        void playOrPause();

        void cutVideoImg();

        void setPlayPlace(int progress);

        String queryDevName(int deivceid);

        void setSurface(Surface surface);

        void launchScreen(List<Integer> deviceIds, int value);

        void setDeviceId(int deviceId, int subId, int mediaId);
    }
}
