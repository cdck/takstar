package xlk.takstar.paperless.ui;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import xlk.takstar.paperless.ui.video.MyWlGlRender;
import xlk.takstar.paperless.ui.video.WlOnRenderRefreshListener;
import xlk.takstar.paperless.util.LogUtil;

/**
 * @author Created by xlk on 2021/4/15.
 * @desc
 */
public class LivePage extends ViewGroup {
    private MyWlGlRender glRender;
    private int widthMeasureSpec;
    private int heightMeasureSpec;

    public LivePage(Context context) {
        this(context, null);
    }

    public LivePage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.widthMeasureSpec = widthMeasureSpec;
        this.heightMeasureSpec = heightMeasureSpec;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        measureChild(getWidth(), getHeight());
    }

    private void measureChild(int parentWidth, int parentHeight) {
        View videoView1 = getChildAt(0);
        View videoView2 = getChildAt(1);
        View videoView3 = getChildAt(2);
        View videoView4 = getChildAt(3);
        LayoutParams params = new LayoutParams(parentWidth, parentHeight);
        LayoutParams params2_1 = new LayoutParams(parentWidth / 2, parentHeight / 2);
        switch (getChildCount()) {
            case 1:
                videoView1.setLayoutParams(params);
                break;
            case 4:
                videoView1.setLayoutParams(params2_1);
                videoView2.setLayoutParams(params2_1);
                videoView3.setLayoutParams(params2_1);
                videoView4.setLayoutParams(params2_1);
                break;
            default:
        }
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
