package xlk.takstar.paperless.fragment.terminal;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Spinner;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.mogujie.tt.protobuf.InterfaceMacro;
import com.mogujie.tt.protobuf.InterfaceMember;
import com.mogujie.tt.protobuf.InterfaceRoom;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.adapter.MemberRoleAdapter;
import xlk.takstar.paperless.adapter.TerminalControlAdapter;
import xlk.takstar.paperless.base.BaseFragment;
import xlk.takstar.paperless.model.bean.ClientControlBean;
import xlk.takstar.paperless.model.bean.MemberRoleBean;
import xlk.takstar.paperless.ui.RvItemDecoration;
import xlk.takstar.paperless.util.PopUtil;

/**
 * @author Created by xlk on 2020/12/8.
 * @desc
 */
public class TerminalControlFragment extends BaseFragment<TerminalControlPresenter> implements TerminalControlContract.View, View.OnClickListener {
    private Button btn_rise;
    private Button btn_fall;
    private Button btn_stop;
    private Button btn_restart_app;
    private Button btn_restart_client;
    private Button btn_shutdown_client;
    private Button btn_assisted_sign_in;
    private Button btn_set_role;
    private Button btn_wake_up;
    private RecyclerView rv_client;
    private TerminalControlAdapter clientControlAdapter;
    private PopupWindow rolePop;
    private MemberRoleAdapter roleAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_terminal_control;
    }

    @Override
    protected void initView(View inflate) {
        btn_rise = inflate.findViewById(R.id.btn_rise);
        btn_fall = inflate.findViewById(R.id.btn_fall);
        btn_stop = inflate.findViewById(R.id.btn_stop);
        btn_restart_app = inflate.findViewById(R.id.btn_restart_app);
        btn_restart_client = inflate.findViewById(R.id.btn_restart_client);
        btn_shutdown_client = inflate.findViewById(R.id.btn_shutdown_client);
        btn_assisted_sign_in = inflate.findViewById(R.id.btn_assisted_sign_in);
        btn_set_role = inflate.findViewById(R.id.btn_set_role);
        btn_wake_up = inflate.findViewById(R.id.btn_wake_up);
        rv_client = inflate.findViewById(R.id.rv_client);
        btn_rise.setOnClickListener(this);
        btn_fall.setOnClickListener(this);
        btn_stop.setOnClickListener(this);
        btn_restart_app.setOnClickListener(this);
        btn_restart_client.setOnClickListener(this);
        btn_shutdown_client.setOnClickListener(this);
        btn_assisted_sign_in.setOnClickListener(this);
        btn_set_role.setOnClickListener(this);
        btn_wake_up.setOnClickListener(this);
    }

    @Override
    protected TerminalControlPresenter initPresenter() {
        return new TerminalControlPresenter(this);
    }

    @Override
    protected void initial() {
        presenter.queryRankInfo();
    }

    @Override
    protected void onHide() {
        if (rolePop != null && rolePop.isShowing()) {
            rolePop.dismiss();
        }
    }

    @Override
    protected void onShow() {
        presenter.queryRankInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //上升
            case R.id.btn_rise: {
                List<Integer> devIds = clientControlAdapter.getChecks();
                if (devIds.isEmpty()) {
                    ToastUtils.showShort(R.string.please_choose_device);
                    return;
                }
                int value = InterfaceMacro.Pb_LiftFlag.Pb_LIFT_FLAG_MACHICE_VALUE | InterfaceMacro.Pb_LiftFlag.Pb_LIFT_FLAG_MIC_VALUE;
                jni.executeTerminalControl(InterfaceMacro.Pb_DeviceControlFlag.Pb_DEVICECONTORL_LIFTUP_VALUE, value, 0, devIds);
                break;
            }
            //下降
            case R.id.btn_fall: {
                List<Integer> devIds = clientControlAdapter.getChecks();
                if (devIds.isEmpty()) {
                    ToastUtils.showShort(R.string.please_choose_device);
                    return;
                }
                int value = InterfaceMacro.Pb_LiftFlag.Pb_LIFT_FLAG_MACHICE_VALUE | InterfaceMacro.Pb_LiftFlag.Pb_LIFT_FLAG_MIC_VALUE;
                jni.executeTerminalControl(InterfaceMacro.Pb_DeviceControlFlag.Pb_DEVICECONTORL_LIFTDOWN_VALUE, value, 0, devIds);
                break;
            }
            //停止
            case R.id.btn_stop: {
                List<Integer> devIds = clientControlAdapter.getChecks();
                if (devIds.isEmpty()) {
                    ToastUtils.showShort(R.string.please_choose_device);
                    return;
                }
                int value = InterfaceMacro.Pb_LiftFlag.Pb_LIFT_FLAG_MACHICE_VALUE | InterfaceMacro.Pb_LiftFlag.Pb_LIFT_FLAG_MIC_VALUE;
                jni.executeTerminalControl(InterfaceMacro.Pb_DeviceControlFlag.Pb_DEVICECONTORL_LIFTSTOP_VALUE, value, 0, devIds);
                break;
            }
            //软件重启
            case R.id.btn_restart_app: {
                List<Integer> devIds = clientControlAdapter.getChecks();
                if (devIds.isEmpty()) {
                    ToastUtils.showShort(R.string.please_choose_device);
                    return;
                }
                jni.executeTerminalControl(InterfaceMacro.Pb_DeviceControlFlag.Pb_DEVICECONTORL_PROGRAMRESTART_VALUE, 0, 0, devIds);
                break;
            }
            //终端重启
            case R.id.btn_restart_client: {
                List<Integer> devIds = clientControlAdapter.getChecks();
                if (devIds.isEmpty()) {
                    ToastUtils.showShort(R.string.please_choose_device);
                    return;
                }
                jni.executeTerminalControl(InterfaceMacro.Pb_DeviceControlFlag.Pb_DEVICECONTORL_REBOOT_VALUE, 0, 0, devIds);
                break;
            }
            //终端关机
            case R.id.btn_shutdown_client: {
                List<Integer> devIds = clientControlAdapter.getChecks();
                if (devIds.isEmpty()) {
                    ToastUtils.showShort(R.string.please_choose_device);
                    return;
                }
                jni.executeTerminalControl(InterfaceMacro.Pb_DeviceControlFlag.Pb_DEVICECONTORL_SHUTDOWN_VALUE, 0, 0, devIds);
                break;
            }
            //辅助签到
            case R.id.btn_assisted_sign_in: {
                List<Integer> devIds = clientControlAdapter.getChecks();
                if (devIds.isEmpty()) {
                    ToastUtils.showShort(R.string.please_choose_device);
                    return;
                }
                jni.assistedSignIn(devIds);
                break;
            }
            //角色设定
            case R.id.btn_set_role: {
                presenter.queryMember();
                showRolePop();
                break;
            }
            //网络唤醒
            case R.id.btn_wake_up: {
                List<Integer> devIds = clientControlAdapter.getChecks();
                if (devIds.isEmpty()) {
                    ToastUtils.showShort(R.string.please_choose_device);
                    return;
                }
                jni.wakeOnLan(devIds);
                break;
            }
            default:
                break;
        }
    }

    private void showRolePop() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.pop_member_role, null, false);
        View meet_fl = getActivity().findViewById(R.id.meet_fl);
        View meet_left_ll = getActivity().findViewById(R.id.meet_left_ll);
        int width = meet_fl.getWidth();
        int height = meet_fl.getHeight();
        int width1 = meet_left_ll.getWidth();
        int height1 = meet_left_ll.getHeight();
        rolePop = PopUtil.createPopupWindow(inflate, width * 2 / 3, height * 2 / 3, btn_set_role, Gravity.CENTER, width1 / 2, 0);
//        rolePop = PopUtil.createHalfPop(inflate, btn_set_role);
        RecyclerView rv_member_role = inflate.findViewById(R.id.rv_member_role);
        Spinner sp_role = inflate.findViewById(R.id.sp_role);
        roleAdapter = new MemberRoleAdapter(R.layout.item_member_role, presenter.memberRoleBeans);
        rv_member_role.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_member_role.setAdapter(roleAdapter);
        roleAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                MemberRoleBean memberRoleBean = presenter.memberRoleBeans.get(position);
                InterfaceMember.pbui_Item_MemberDetailInfo member = memberRoleBean.getMember();
                roleAdapter.setSelectedId(member.getPersonid());
                InterfaceRoom.pbui_Item_MeetRoomDevSeatDetailInfo seat = memberRoleBean.getSeat();
                if (seat != null) {
                    switch (seat.getRole()) {
                        case InterfaceMacro.Pb_MeetMemberRole.Pb_role_member_normal_VALUE:
                            sp_role.setSelection(1);
                            break;
                        case InterfaceMacro.Pb_MeetMemberRole.Pb_role_member_compere_VALUE:
                            sp_role.setSelection(2);
                            break;
                        case InterfaceMacro.Pb_MeetMemberRole.Pb_role_member_secretary_VALUE:
                            sp_role.setSelection(3);
                            break;
                        case InterfaceMacro.Pb_MeetMemberRole.Pb_role_admin_VALUE:
                            sp_role.setSelection(4);
                            break;
                        default:
                            sp_role.setSelection(0);
                            break;
                    }
                } else {
                    sp_role.setSelection(0);
                }
            }
        });
        inflate.findViewById(R.id.btn_definite).setOnClickListener(v -> {
            MemberRoleBean selected = roleAdapter.getSelected();
            if (selected == null) {
                ToastUtils.showShort(R.string.please_choose_member);
                return;
            }
            InterfaceRoom.pbui_Item_MeetRoomDevSeatDetailInfo seat = selected.getSeat();
            if (seat == null) {
                ToastUtils.showShort(R.string.please_choose_binded_member);
                return;
            }
            int personid = selected.getMember().getPersonid();
            int devid = seat.getDevid();
            int role;
            int position = sp_role.getSelectedItemPosition();
            switch (position) {
                case 1:
                    role = InterfaceMacro.Pb_MeetMemberRole.Pb_role_member_normal_VALUE;
                    break;
                case 2:
                    role = InterfaceMacro.Pb_MeetMemberRole.Pb_role_member_compere_VALUE;
                    break;
                case 3:
                    role = InterfaceMacro.Pb_MeetMemberRole.Pb_role_member_secretary_VALUE;
                    break;
                case 4:
                    role = InterfaceMacro.Pb_MeetMemberRole.Pb_role_admin_VALUE;
                    break;
                default:
                    role = InterfaceMacro.Pb_MeetMemberRole.Pb_role_member_nouser_VALUE;
                    break;
            }
            jni.modifyMeetRanking(personid, role, devid);
        });
        inflate.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            rolePop.dismiss();
        });
    }

    @Override
    public void updateMemberRole(List<MemberRoleBean> memberRoleBeans) {
        if (rolePop != null && rolePop.isShowing()) {
            roleAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void updateClient(List<ClientControlBean> clientControlBeans) {
        if (clientControlAdapter == null) {
            clientControlAdapter = new TerminalControlAdapter(R.layout.item_terminal_control, clientControlBeans);
//            rv_client.addItemDecoration(new RvItemDecoration(getContext()));
            rv_client.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_client.setAdapter(clientControlAdapter);
            clientControlAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    clientControlAdapter.setCheck(clientControlBeans.get(position).getDeviceInfo().getDevcieid());
                }
            });
        } else {
            clientControlAdapter.notifyDataSetChanged();
        }
    }
}
