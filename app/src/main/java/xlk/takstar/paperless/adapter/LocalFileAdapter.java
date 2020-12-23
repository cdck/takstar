package xlk.takstar.paperless.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.io.File;
import java.util.List;

import androidx.annotation.Nullable;
import xlk.takstar.paperless.R;

/**
 * @author Created by xlk on 2020/11/11.
 * @desc
 */
public class LocalFileAdapter extends BaseQuickAdapter<File, BaseViewHolder> {
    public LocalFileAdapter(int layoutResId, @Nullable List<File> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, File item) {
        helper.setText(R.id.tv_file_name,item.getName());
    }
}
