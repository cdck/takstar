package xlk.takstar.paperless.ui.video.test;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * @author Created by xlk on 2021/1/25.
 * @desc
 */
class CustomPlaySubView extends LinearLayout {
    public CustomPlaySubView(Context context) {
        this(context, null);
    }

    public CustomPlaySubView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomPlaySubView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CustomPlaySubView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }



}
