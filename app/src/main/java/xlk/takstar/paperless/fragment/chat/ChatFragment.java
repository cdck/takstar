package xlk.takstar.paperless.fragment.chat;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.mogujie.tt.protobuf.InterfaceMacro;
import com.mogujie.tt.protobuf.InterfaceMember;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.adapter.ChatMemberAdapter;
import xlk.takstar.paperless.adapter.MulitpleItemAdapter;
import xlk.takstar.paperless.base.BaseFragment;
import xlk.takstar.paperless.chatonline.ChatVideoActivity;
import xlk.takstar.paperless.meet.MeetingActivity;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.model.EventMessage;
import xlk.takstar.paperless.model.EventType;
import xlk.takstar.paperless.model.GlobalValue;
import xlk.takstar.paperless.model.bean.ChatDeviceMember;
import xlk.takstar.paperless.model.bean.MyChatMessage;


/**
 * @author Created by xlk on 2020/12/2.
 * @desc
 */
public class ChatFragment extends BaseFragment<ChatPresenter> implements ChatContract.View, View.OnClickListener {
    private TextView tv_meeting_name;
    private RecyclerView rv_member;
    private TextView tv_member_name;
    private RecyclerView rv_message;
    private ImageView iv_file;
    private ImageView iv_cut;
    private ImageView iv_camera;
    private EditText edt_message;
    private Button btn_send;
    private ChatMemberAdapter chatMemberAdapter;
    private MulitpleItemAdapter chatAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat;
    }

    @Override
    protected void initView(View inflate) {
        tv_meeting_name = inflate.findViewById(R.id.tv_meeting_name);
        rv_member = inflate.findViewById(R.id.rv_member);
        tv_member_name = inflate.findViewById(R.id.tv_member_name);
        rv_message = inflate.findViewById(R.id.rv_message);
        iv_file = inflate.findViewById(R.id.iv_file);
        iv_cut = inflate.findViewById(R.id.iv_cut);
        iv_camera = inflate.findViewById(R.id.iv_camera);
        edt_message = inflate.findViewById(R.id.edt_message);
        btn_send = inflate.findViewById(R.id.btn_send);
        iv_file.setOnClickListener(this);
        iv_cut.setOnClickListener(this);
        iv_camera.setOnClickListener(this);
        btn_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_file:
                break;
            case R.id.iv_cut:
                break;
            case R.id.iv_camera:
                startActivity(new Intent(getContext(), ChatVideoActivity.class));
                break;
            case R.id.btn_send: {
                int selectedId = chatMemberAdapter.getSelectedId();
                if (selectedId == -1) {
                    ToastUtils.showShort(R.string.please_choose_member);
                    return;
                }
                String trim = edt_message.getText().toString().trim();
                if (trim.isEmpty()) {
                    ToastUtils.showShort(R.string.please_enter_content);
                    return;
                }
                List<Integer> ids = new ArrayList<>();
                ids.add(selectedId);
                jni.sendChatMessage(trim, InterfaceMacro.Pb_MeetIMMSG_TYPE.Pb_MEETIM_CHAT_Message.getNumber(), ids);
                edt_message.setText("");
                presenter.addImMessage(selectedId, new MyChatMessage(1, "", GlobalValue.localMemberId
                        , System.currentTimeMillis() / 1000, trim));
                break;
            }
            default:
                break;
        }
    }

    @Override
    protected ChatPresenter initPresenter() {
        return new ChatPresenter(this);
    }

    @Override
    protected void initial() {
        MeetingActivity.chatIsShowing = true;
        presenter.queryDeviceMeetInfo();
        presenter.queryMember();
        EventBus.getDefault().post(new EventMessage.Builder().type(EventType.BUS_UPDATE_BADGE_NUMBER).objects(0).build());
        presenter.refreshUnreadCount();
        presenter.updateRvMessage();
    }

    @Override
    protected void onShow() {
        initial();
    }

    @Override
    protected void onHide() {
        MeetingActivity.chatIsShowing = false;
    }

    @Override
    public void updateMeetingName(String meetingName) {
        tv_meeting_name.setText(meetingName);
    }

    @Override
    public void updateMemberList() {
        if (chatMemberAdapter == null) {
            chatMemberAdapter = new ChatMemberAdapter(R.layout.item_chat_member, presenter.deviceMembers);
            rv_member.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_member.setAdapter(chatMemberAdapter);
            chatMemberAdapter.setOnItemClickListener((adapter, view, position) -> {
                ChatDeviceMember item = presenter.deviceMembers.get(position);
                item.setCount(0);
                item.setLastCheckTime(System.currentTimeMillis() / 1000);
                chatMemberAdapter.setSelected(item.getMemberId());
                String memberRoleName = Constant.getMemberRoleName(getContext(), item.getRole());
                String memberName = item.getMemberName();
                tv_member_name.setText(memberRoleName.isEmpty() ? memberName : memberRoleName + ":" + memberName);
                presenter.setCurrentMemberId(item.getMemberId());
                presenter.updateRvMessage();
            });
        } else {
            chatMemberAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void updateMessageRv(List<MyChatMessage> messages) {
        if (chatAdapter == null) {
            chatAdapter = new MulitpleItemAdapter(getContext(), messages);
            rv_message.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_message.setAdapter(chatAdapter);
        } else {
            chatAdapter.notifyDataSetChanged();
        }
        if (messages.size() > 0) {
            rv_message.smoothScrollToPosition(messages.size() - 1);
        }
    }

}
