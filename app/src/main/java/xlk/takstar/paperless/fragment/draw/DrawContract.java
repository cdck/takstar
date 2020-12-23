package xlk.takstar.paperless.fragment.draw;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Path;
import java.util.List;

import xlk.takstar.paperless.base.IBasePresenter;
import xlk.takstar.paperless.base.IBaseView;
import xlk.takstar.paperless.model.bean.DevMember;
import xlk.takstar.paperless.ui.ArtBoard;

/**
 * @author Created by xlk on 2020/12/7.
 * @desc
 */
public interface DrawContract {
    interface View extends IBaseView{

        void updateMembers(List<DevMember> devMembers);

        void drawZoomBmp(Bitmap bs2bmp);

        void updateShareStatus();

        void setCanvasSize(int maxX, int maxY);

        void drawPath(Path newPath, Paint paint);

        void invalidate();

        void funDraw(Paint newPaint, int height, int i, int x, int y, String ptext);

        void drawText(String ptext, float lx, float ly, Paint paint);

        void initCanvas();

        void drawAgain(List<ArtBoard.DrawPath> localPathList);
    }

    interface Presenter extends IBasePresenter{

        void queryMember();

        void queryDevice();
    }
}
