package xlk.takstar.paperless.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.AbsoluteLayout;

import com.blankj.utilcode.util.LogUtils;

/**
 * @author by xlk
 * @date 2020/6/12 18:03
 * @desc 可以上下左右自由拖拽的AbsoluteLayout
 */
public class CustomAbsoluteLayout extends AbsoluteLayout {
    private final String TAG = "CustomAbsoluteLayout-->";
    private int width = 1300, height = 760;//底图宽高
    private int viewWidth, viewHeight;//显示区域的宽高
    private float downX, downY;//拖动时按下
    private int left, top, right, bottom;

    /**
     * 设置显示区域的宽高
     */
    public void setScreen(int viewWidth, int viewHeight) {
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
    }

    public CustomAbsoluteLayout(Context context) {
        this(context, null);
    }

    public CustomAbsoluteLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomAbsoluteLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CustomAbsoluteLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 动态设置setLayoutParams后会执行
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
        this.width = width;
        this.height = height;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        left = l;
        top = t;
        right = r;
        bottom = b;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //是否可以左右拖动
        boolean canDragX = viewWidth < width;
        //是否可以上下拖动
        boolean canDragY = viewHeight < height;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                float moveX = event.getX();
                float moveY = event.getY();
                float dx = moveX - downX;
                float dy = moveY - downY;
                dragParentView(canDragX, canDragY, dx, dy);
                break;
            }
            case MotionEvent.ACTION_UP: {
                Log.i(TAG, "move up  显示宽高：" + viewWidth + "," + viewHeight
                        + ",底图宽高：" + width + "," + height
                        + ",canDragX：" + canDragX + ",canDragY：" + canDragY);
                break;
            }
            default:
                break;
        }
        return true;
    }

    private void dragParentView(boolean canMoveX, boolean canMoveY, float dx, float dy) {
        if (canMoveX) {
            //左
            if (left == 0) {
                //当前左边已经封顶了
                if (dx < 0 && right >= viewWidth) {
                    //还往左边移动
                    left = (int) (left + dx);
                } else {
                    left = 0;
                }
            } else if (left < 0) {
                //当前已经超过最左边
                if (right >= viewWidth) {
                    if (dx < 0) {
                        this.left = (int) (left + dx);
                    } else if (dx > 0) {
                        this.left = (int) (left + dx);
                    }
                }
            } else {
                this.left = 0;
            }
            //右
            right = width + this.left;
            int i1 = viewWidth - right;
            if (i1 > 0) {
                right = viewWidth;
                this.left += i1;
            }
        } else {
            //不可以拖动X轴
            left = 0;
            right = width;
        }
        if (canMoveY) {
            //上
            if (top == 0) {
                if (dy < 0 && bottom > viewHeight) {
                    top = (int) (top + dy);
                } else {
                    top = 0;
                }
            } else if (top < 0) {
                if (dy < 0 && bottom > viewHeight) {
                    top = (int) (top + dy);
                } else if (dy > 0) {
                    top = (int) (top + dy);
                }
            } else {
                top = 0;
            }
            //下
            bottom = height + top;
            int i = viewHeight - bottom;
            if (i > 0) {
                bottom = viewHeight;
                top += i;
            }
        } else {
            //不可以拖动Y轴
            top = 0;
            bottom = height;
        }
        this.layout(left, top, right, bottom);
    }
}
