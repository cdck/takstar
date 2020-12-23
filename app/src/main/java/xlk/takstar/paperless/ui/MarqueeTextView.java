package xlk.takstar.paperless.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

/**
 * Created by xlk on 2019/8/9.
 * 用于实现需要多个TextView实现跑马灯效果，获取不到焦点
 */
public class MarqueeTextView extends androidx.appcompat.widget.AppCompatTextView {
    public MarqueeTextView(Context context) {
        this(context, null);
    }

    public MarqueeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setLines(1);
        setMaxLines(1);
        setSingleLine(true);
        //设置无限循环
        setMarqueeRepeatLimit(-1);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
