package xlk.takstar.paperless.fragment.screen;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.mogujie.tt.protobuf.InterfaceMacro;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.adapter.WmScreenMemberAdapter;
import xlk.takstar.paperless.adapter.WmScreenProjectorAdapter;
import xlk.takstar.paperless.base.BaseFragment;
import xlk.takstar.paperless.model.Constant;

import static xlk.takstar.paperless.model.Constant.RESOURCE_ID_0;

/**
 * @author Created by xlk on 2020/12/10.
 * @desc
 */
public class ScreenManageFragment extends BaseFragment<ScreenManagePresenter> implements ScreenManageContract.View, View.OnClickListener {
    private RecyclerView rv_screen_source, rv_projection, rv_member;
    private CheckBox cb_source, cb_projection, cb_member;
    private Button btn_preview;
    private CheckBox cb_mandatory;
    private Button btn_launch;
    private Button btn_stop;
    private WmScreenMemberAdapter sourceMemberAdapter,targetAdapter;
    private WmScreenProjectorAdapter projectorAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_screen_manage;
    }

    @Override
    protected void initView(View inflate) {
        cb_source = inflate.findViewById(R.id.cb_source);
        cb_projection = inflate.findViewById(R.id.cb_projection);
        cb_member = inflate.findViewById(R.id.cb_member);

        rv_screen_source = inflate.findViewById(R.id.rv_screen_source);
        rv_projection = inflate.findViewById(R.id.rv_projection);
        rv_member = inflate.findViewById(R.id.rv_member);

        cb_mandatory = inflate.findViewById(R.id.cb_mandatory);

        btn_preview = inflate.findViewById(R.id.btn_preview);
        btn_launch = inflate.findViewById(R.id.btn_launch);
        btn_stop = inflate.findViewById(R.id.btn_stop);

        btn_preview.setOnClickListener(this);
        btn_launch.setOnClickListener(this);
        btn_stop.setOnClickListener(this);

        cb_source.setOnClickListener(this);
        cb_projection.setOnClickListener(this);
        cb_member.setOnClickListener(this);
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
        if (sourceMemberAdapter == null) {
            sourceMemberAdapter = new WmScreenMemberAdapter(R.layout.item_wm_screen, presenter.sourceMembers);
            rv_screen_source.setLayoutManager(new LinearLayoutManager(getContext()));
            sourceMemberAdapter.setSingleCheck(true);
            rv_screen_source.setAdapter(sourceMemberAdapter);
            sourceMemberAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    sourceMemberAdapter.choose(presenter.sourceMembers.get(position).getDeviceDetailInfo().getDevcieid());
//                    cb_source.setChecked(sourceMemberAdapter.isChooseAll());
                }
            });
        } else {
            sourceMemberAdapter.notifyChecks();
        }

        if (projectorAdapter == null) {
            projectorAdapter = new WmScreenProjectorAdapter( presenter.onLineProjectors);
            rv_projection.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_projection.setAdapter(projectorAdapter);
            projectorAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    projectorAdapter.choose(presenter.onLineProjectors.get(position).getDevcieid());
                    cb_projection.setChecked(projectorAdapter.isChooseAll());
                }
            });
        } else {
            projectorAdapter.notifyChecks();
        }

        if (targetAdapter == null) {
            targetAdapter = new WmScreenMemberAdapter(R.layout.item_wm_screen,presenter.targetMembers);
            rv_member.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_member.setAdapter(targetAdapter);
            targetAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    targetAdapter.choose(presenter.targetMembers.get(position).getDeviceDetailInfo().getDevcieid());
                    cb_member.setChecked(targetAdapter.isChooseAll());
                }
            });
        } else {
            targetAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_preview: {
                List<Integer> checks = sourceMemberAdapter.getSelectedDeviceIds();
                if (checks.isEmpty()) {
                    ToastUtils.showShort(R.string.please_choose_member);
                    return;
                }
                jni.playTargetScreen(checks.get(0));
                break;
            }
            case R.id.cb_source: {
//                boolean checked = cb_source.isChecked();
//                cb_source.setChecked(checked);
//                sourceMemberAdapter.setChooseAll(checked);
                break;
            }
            case R.id.cb_projection: {
                boolean checked = cb_projection.isChecked();
                cb_projection.setChecked(checked);
                projectorAdapter.setChooseAll(checked);
                break;
            }
            case R.id.cb_member: {
                boolean checked = cb_member.isChecked();
                cb_member.setChecked(checked);
                targetAdapter.setChooseAll(checked);
                break;
            }
            case R.id.btn_launch: {
                List<Integer> checks = sourceMemberAdapter.getSelectedDeviceIds();
                if (checks.isEmpty()) {
                    ToastUtils.showShort(R.string.please_choose_screen_source);
                    return;
                }
                List<Integer> targets = targetAdapter.getSelectedDeviceIds();
                List<Integer> projects = projectorAdapter.getChooseIds();
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
            case R.id.btn_stop: {
                List<Integer> targetIds = targetAdapter.getSelectedDeviceIds();
                targetIds.addAll(projectorAdapter.getChooseIds());
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
