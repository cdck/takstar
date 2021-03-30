package xlk.takstar.paperless.util;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * @author Created by xlk on 2021/2/27.
 * @desc 现在EditText输入数字范围
 */
public class RangeControlFilter implements InputFilter {
    private final int min, max;

    public RangeControlFilter(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String sourceText = source.toString();
        String destText = dest.toString();
        if (dstart == 0 && "0".equals(source)) {
            //如果输入是0 且位置在第一位，取消输入
            return "";
        }

        StringBuilder totalText = new StringBuilder();
        totalText.append(destText.substring(0, dstart))
                .append(sourceText)
                .append(destText.substring(dstart, destText.length()));


        try {
            if (Integer.parseInt(totalText.toString()) > max) {
                return "";
            } else if (Integer.parseInt(totalText.toString()) < min) {
                //如果输入是0，取消输入
                return "";
            }
        } catch (Exception e) {
            return "";
        }

        if ("".equals(source.toString())) {
            return "";
        }
        return "" + Integer.parseInt(source.toString());
//        原文链接：https://blog.csdn.net/oarsman/article/details/105971356
    }
}
