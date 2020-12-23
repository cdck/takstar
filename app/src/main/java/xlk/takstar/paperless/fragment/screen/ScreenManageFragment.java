package xlk.takstar.paperless.fragment.screen;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mogujie.tt.protobuf.InterfaceMacro;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.adapter.DevMemberAdapter;
import xlk.takstar.paperless.adapter.ProjectionAdapter;
import xlk.takstar.paperless.base.BaseFragment;
import xlk.takstar.paperless.model.Constant;

import static xlk.takstar.paperless.model.Constant.RESOURCE_ID_0;

/**
 * @author Created by xlk on 2020/12/10.
 * @desc
 */
public class ScreenManageFragment extends BaseFragment<ScreenManagePresenter> implements ScreenManageContract.View, View.OnClickListener {
    private RecyclerView rv_screen_source;
    private Button btn_preview;
    private CheckBox cb_projection;
    private CheckBox cb_member;
    private RecyclerView rv_projection;
    private RecyclerView rv_member;
    private CheckBox cb_mandatory;
    private Button btn_launch_screen;
    private Button btn_stop_screen;
    private DevMemberAdapter sourceAdapter, targetAdapter;
    private ProjectionAdapter projectionAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_screen_manage;
    }

    @Override
    protected void initView(View inflate) {
        rv_screen_source = inflate.findViewById(R.id.rv_screen_source);
        btn_preview = inflate.findViewById(R.id.btn_preview);
        cb_projection = inflate.findViewById(R.id.cb_projection);
        cb_member = inflate.findViewById(R.id.cb_member);
        rv_projection = inflate.findViewById(R.id.rv_projection);
        rv_member = inflate.findViewById(R.id.rv_member);
        cb_mandatory = inflate.findViewById(R.id.cb_mandatory);
        btn_launch_screen = inflate.findViewById(R.id.btn_launch_screen);
        btn_stop_screen = inflate.findViewById(R.id.btn_stop_screen);
        btn_preview.setOnClickListener(this);
        cb_projection.setOnClickListener(this);
        cb_member.setOnClickListener(this);
        btn_launch_screen.setOnClickListener(this);
        btn_stop_screen.setOnClickListener(this);
    }

    @Override
    protected ScreenManagePresenter initPresenter() {
        return new ScreenManagePresenter(this);
    }

    @Override
    protected void onShow() {
        presenter.queryData();
    }

    @Override
    protected void initial() {
        presenter.queryData();
    }

    @Override
    public void updateRecyclerView() {
        if (sourceAdapter == null) {
            sourceAdapter = new DevMemberAdapter(presenter.sourceMembers);
            rv_screen_source.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
            rv_screen_source.setAdapter(sourceAdapter);
            sourceAdapter.setSingle(true);
            sourceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    sourceAdapter.setCheck(presenter.sourceMembers.get(position).getMemberDetailInfo().getPersonid());
                }
            });
        } else {
            sourceAdapter.notifyDataSetChanged();
        }
        if (targetAdapter == null) {
            targetAdapter = new DevMemberAdapter(presenter.targetMembers);
            rv_member.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
            rv_member.setAdapter(targetAdapter);
            targetAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    targetAdapter.setCheck(presenter.targetMembers.get(position).getMemberDetailInfo().getPersonid());
                    cb_member.setChecked(targetAdapter.isCheckAll());
                }
            });
        } else {
            targetAdapter.notifyDataSetChanged();
        }
        if (projectionAdapter == null) {
            projectionAdapter = new ProjectionAdapter(presenter.onLineProjectors);
            rv_projection.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            rv_projection.setAdapter(projectionAdapter);
            projectionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    projectionAdapter.setCheck(presenter.onLineProjectors.get(position).getDevcieid());
                    cb_projection.setChecked(projectionAdapter.isCheckedAll());
                }
            });
        } else {
            projectionAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_preview: {
                List<Integer> checks = sourceAdapter.getCheckDeviceIds();
                if (checks.isEmpty()) {
                    ToastUtils.showShort(R.string.please_choose_member);
                    return;
                }
                jni.playTargetScreen(checks.get(0));
                break;
            }
            case R.id.cb_projection: {
                boolean checked = cb_projection.isChecked();
                cb_projection.setChecked(checked);
                projectionAdapter.setCheckAll(checked);
                break;
            }
            case R.id.cb_member: {
                boolean checked = cb_member.isChecked();
                cb_member.setChecked(checked);
                targetAdapter.setCheckAll(checked);
                break;
            }
            case R.id.btn_launch_screen: {
                List<Integer> checks = sourceAdapter.getCheckDeviceIds();
                if (checks.isEmpty()) {
                    ToastUtils.showShort(R.string.please_choose_screen_source);
                    return;
                }
                List<Integer> targets = targetAdapter.getCheckDeviceIds();
                List<Integer> projects = projectionAdapter.getCheckDeviceIds();
                targets.addAll(projects);
                if (targets.isEmpty()) {
                    ToastUtils.showShort(R.string.please_choose_target);
                    return;
                }
                int triggeruserval = cb_mandatory.isChecked()
                        ? InterfaceMacro.Pb_TriggerUsedef.Pb_EXCEC_USERDEF_FLAG_NOCREATEWINOPER_VALUE
                        : InterfaceMacro.Pb_TriggerUsedef.Pb_EXCEC_USERDEF_FLAG_ZERO_VALUE;
                List<Integer> temps = new ArrayList<>();
                temps.add(RESOURCE_ID_0);
                jni.streamPlay(checks.get(0), Constant.SCREEN_SUB_ID, triggeruserval, temps, targets);
                break;
            }
            case R.id.btn_stop_screen: {
                List<Integer> targetIds = targetAdapter.getCheckDeviceIds();
                targetIds.addAll(projectionAdapter.getCheckDeviceIds());
                if (targetIds.isEmpty()) {
                    ToastUtils.showShort(R.string.please_choose_target);
                    return;
                }
                jni.stopResourceOperate(RESOURCE_ID_0, targetIds);
                break;
            }
            default:
                break;
        }
    }
}
