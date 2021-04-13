package xlk.takstar.paperless.fragment.agenda;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.mogujie.tt.protobuf.InterfaceAgenda;
import com.mogujie.tt.protobuf.InterfaceFile;
import com.tencent.smtt.sdk.TbsDownloader;
import com.tencent.smtt.sdk.TbsReaderView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.adapter.AgendaAdapter;
import xlk.takstar.paperless.adapter.AgendaFileAdapter;
import xlk.takstar.paperless.base.BaseFragment;
import xlk.takstar.paperless.model.GlobalValue;
import xlk.takstar.paperless.model.JniHelper;
import xlk.takstar.paperless.ui.RvItemDecoration;
import xlk.takstar.paperless.util.FileUtil;
import xlk.takstar.paperless.util.LogUtil;

import static xlk.takstar.paperless.App.appContext;
import static xlk.takstar.paperless.model.Constant.RESOURCE_ID_0;
import static xlk.takstar.paperless.model.GlobalValue.initX5Finished;

/**
 * @author Created by xlk on 2020/12/1.
 * @desc
 */
public class AgendaFragment extends BaseFragment<AgendaPresenter> implements AgendaContract.View, TbsReaderView.ReaderCallback {
    /**
     * =true 加载的是系统内核（默认），=false 加载的是X5内核
     */
    public static boolean isNeedRestart;
    private LinearLayout agenda_root;
    private ProgressBar progress_bar;
    private ScrollView agenda_sv;
    private TextView agenda_tv;
    private LinearLayout ll_time_agenda;
    private RecyclerView rv_agenda, rv_file;
    private TbsReaderView tbsReaderView;
    private AgendaAdapter agendaAdapter;
    private AgendaFileAdapter agendaFileAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_agenda;
    }

    @Override
    protected void initView(View inflate) {
        agenda_root = inflate.findViewById(R.id.agenda_root);
        progress_bar = inflate.findViewById(R.id.progress_bar);
        agenda_sv = inflate.findViewById(R.id.agenda_sv);
        agenda_tv = inflate.findViewById(R.id.agenda_tv);
        ll_time_agenda = inflate.findViewById(R.id.ll_time_agenda);
        rv_agenda = inflate.findViewById(R.id.rv_agenda);
        rv_file = inflate.findViewById(R.id.rv_file);
    }

    @Override
    protected AgendaPresenter initPresenter() {
        return new AgendaPresenter(this);
    }

    @Override
    protected void initial() {
        presenter.queryAgenda();
    }

    @Override
    protected void onShow() {
        presenter.queryAgenda();
    }

    @Override
    public void initDefault() {
        ll_time_agenda.setVisibility(View.GONE);
        progress_bar.setVisibility(View.GONE);
        agenda_sv.setVisibility(View.GONE);
        agenda_tv.setText("");
        if (tbsReaderView != null) {
            agenda_root.removeView(tbsReaderView);
            tbsReaderView.onStop();
            tbsReaderView = null;
        }
    }

    @Override
    public void updateAgendaTv(String content) {
        //也有可能下载X5内核完成，但是议程变成文本类
        progress_bar.setVisibility(View.GONE);
        ll_time_agenda.setVisibility(View.GONE);
        agenda_sv.setVisibility(View.VISIBLE);
        agenda_tv.setText(content);
    }

    @Override
    public void displayFile(String path) {
        agenda_sv.setVisibility(View.GONE);
        ll_time_agenda.setVisibility(View.GONE);
        if (initX5Finished) {
            //加载完成
            if (isNeedRestart) {
                //加载的是系统内核
                progress_bar.setVisibility(View.GONE);
                agenda_sv.setVisibility(View.VISIBLE);
                agenda_tv.setText(getString(R.string.init_x5_failure));
                return;
            }
        } else {
            //没有加载完成
            progress_bar.setVisibility(View.VISIBLE);
            TbsDownloader.startDownload(appContext);
            return;
        }
        /* **** **  加载完成，并且加载的是X5内核  ** **** */
        progress_bar.setVisibility(View.GONE);
        String tempPath = Environment.getExternalStorageDirectory().getPath();
        tbsReaderView = new TbsReaderView(Objects.requireNonNull(getContext()), this);
        agenda_root.addView(tbsReaderView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        Bundle bundle = new Bundle();
        bundle.putString("filePath", path);
        bundle.putString("tempPath", tempPath);//bsReaderTemp
        String suffix = path.substring(path.lastIndexOf(".") + 1);
        LogUtil.i(TAG, "displayFile 打开文件 -->" + path + "， 后缀： " + suffix + ", tempPath= " + tempPath);
        try {
            boolean result = tbsReaderView.preOpen(suffix, false);
            LogUtil.e(TAG, "displayFile :  result --> " + result);
            if (result) {
                tbsReaderView.openFile(bundle);
            } else {
                ToastUtils.showShort(R.string.not_supported);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {

    }

    @Override
    public void showTimeAgenda() {
        ll_time_agenda.setVisibility(View.VISIBLE);
        if (agendaAdapter == null) {
            agendaAdapter = new AgendaAdapter(presenter.agendaTimeInfos);
            rv_agenda.addItemDecoration(new RvItemDecoration(getContext()));
            rv_agenda.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_agenda.setAdapter(agendaAdapter);
            agendaAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    InterfaceAgenda.pbui_ItemAgendaTimeInfo item = presenter.agendaTimeInfos.get(position);
                    agendaAdapter.choose(item.getAgendaid());
                    presenter.queryFileByDir(item.getDirid());
                }
            });
        } else {
            agendaAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void updateFileList() {
        if (agendaFileAdapter == null) {
            agendaFileAdapter = new AgendaFileAdapter(presenter.files);
            rv_file.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_file.addItemDecoration(new RvItemDecoration(getContext()));
            rv_file.setAdapter(agendaFileAdapter);
            agendaFileAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    InterfaceFile.pbui_Item_MeetDirFileDetailInfo item = presenter.files.get(position);
                    String fileName = item.getName().toStringUtf8();
                    if (FileUtil.isVideo(fileName)) {
                        List<Integer> devIds = new ArrayList<>();
                        devIds.add(GlobalValue.localDeviceId);
                        JniHelper.getInstance().mediaPlayOperate(item.getMediaid(), devIds, 0, RESOURCE_ID_0, 0, 0);
                    } else {
                        FileUtil.openFile(getContext(), fileName, item.getMediaid());
                    }
                }
            });
        } else {
            agendaFileAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //不停止掉，下次进入是打开文件会卡在加载中状态
        if (tbsReaderView != null) {
            tbsReaderView.onStop();
            tbsReaderView = null;
        }
    }
}
