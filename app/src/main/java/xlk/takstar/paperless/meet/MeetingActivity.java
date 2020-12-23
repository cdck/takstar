package xlk.takstar.paperless.meet;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.UriUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mogujie.tt.protobuf.InterfaceDevice;
import com.mogujie.tt.protobuf.InterfaceMacro;
import com.mogujie.tt.protobuf.InterfaceMeetfunction;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.adapter.FeaturesAdapter;
import xlk.takstar.paperless.adapter.PopPushMemberAdapter;
import xlk.takstar.paperless.adapter.PopPushProjectionAdapter;
import xlk.takstar.paperless.base.BaseActivity;
import xlk.takstar.paperless.fragment.agenda.AgendaFragment;
import xlk.takstar.paperless.fragment.annotate.AnnotateFragment;
import xlk.takstar.paperless.fragment.bullet.BulletFragment;
import xlk.takstar.paperless.fragment.chat.ChatFragment;
import xlk.takstar.paperless.fragment.draw.DrawFragment;
import xlk.takstar.paperless.fragment.livevideo.LiveVideoFragment;
import xlk.takstar.paperless.fragment.material.MaterialFragment;
import xlk.takstar.paperless.fragment.screen.ScreenManageFragment;
import xlk.takstar.paperless.fragment.sign.SignFragment;
import xlk.takstar.paperless.fragment.terminal.TerminalControlFragment;
import xlk.takstar.paperless.fragment.vote.VoteManageFragment;
import xlk.takstar.paperless.fragment.web.WebFragment;
import xlk.takstar.paperless.main.MainActivity;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.model.EventMessage;
import xlk.takstar.paperless.model.EventType;
import xlk.takstar.paperless.model.GlobalValue;
import xlk.takstar.paperless.model.bean.DevMember;
import xlk.takstar.paperless.util.LogUtil;
import xlk.takstar.paperless.util.PopUtil;

import static xlk.takstar.paperless.model.Constant.RESOURCE_ID_0;

public class MeetingActivity extends BaseActivity<MeetingPresenter> implements MeetingContract.View, View.OnClickListener {

    private LinearLayout meet_left_ll;
    private LinearLayout meet_top_ll;
    private ImageView meet_iv_logo;
    private RecyclerView meet_rv;
    private TextView meet_tv_time;
    private TextView meet_tv_date;
    private TextView meet_tv_meet_name;
    private TextView meet_tv_member_role;
    private TextView meet_tv_member_name;
    private ImageView meet_iv_close;
    private ImageView meet_iv_min;
    private TextView meet_top_line;
    private TextView meet_tv_online;
    private ImageView meet_iv_message;
    private ImageView meet_iv_news;
    private FrameLayout meet_fl;
    private LinearLayout other_feature_ll;
    private FeaturesAdapter featuresAdapter;
    private int saveFunCode = -1;
    public static boolean chatIsShowing = false;
    public static Badge mBadge;
    private AgendaFragment agendaFragment;
    private MaterialFragment materialFragment;
    private AnnotateFragment annotateFragment;
    private WebFragment webFragment;
    private SignFragment signFragment;
    private ChatFragment chatFragment;
    private DrawFragment drawFragment;
    private TerminalControlFragment clientControlFragment;
    private PopupWindow featurePop;
    private VoteManageFragment voteManageFragment;
    private ScreenManageFragment screenManageFragment;
    private BulletFragment bulletFragment;
    private LiveVideoFragment liveVideoFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_meeting;
    }

    @Override
    protected MeetingPresenter initPresenter() {
        return new MeetingPresenter(this, this);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
        presenter.initial();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String draw = intent.getStringExtra("draw");
        LogUtil.i(TAG, "onNewIntent draw=" + draw);
        if (draw != null && draw.equals("draw")) {
            showFragment(InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_WHITEBOARD_VALUE);
        }
    }

    @Override
    public void updateTime(String[] gtmDate) {
        meet_tv_time.setText(gtmDate[0]);
        meet_tv_date.setText(gtmDate[1]);
    }

    @Override
    public void updateOnline(String string) {
        meet_tv_online.setText(string);
    }

    @Override
    public void updateMeetingName(String meetingName) {
        meet_tv_meet_name.setText(meetingName);
    }

    @Override
    public void updateMemberName(String memberName) {
        meet_tv_member_name.setText(memberName);
    }

    @Override
    public void updateMemberRole(String role) {
        meet_tv_member_role.setText(role);
    }

    @Override
    public void closeOtherFeaturePage() {
        if (featurePop != null && featurePop.isShowing()) {
            featurePop.dismiss();
        }
        if (saveFunCode > Constant.FUN_CODE) {
            saveFunCode = -1;
            updateMeetingFeatures();
        }
    }

    @Override
    public void updateMeetingFeatures() {
        LogUtil.i(TAG, "updateMeetingFeatures meetingFeatures.size=" + presenter.meetingFeatures.size());
        if (featuresAdapter == null) {
            featuresAdapter = new FeaturesAdapter(R.layout.item_feature, presenter.meetingFeatures);
            meet_rv.setLayoutManager(new LinearLayoutManager(this));
            meet_rv.setAdapter(featuresAdapter);
            featuresAdapter.setOnItemClickListener((adapter, view, position) -> {
                int funcode = presenter.meetingFeatures.get(position).getFuncode();
                showFragment(funcode);
            });
        } else {
            featuresAdapter.notifyDataSetChanged();
        }
        if (saveFunCode == -1) {
            if (presenter.meetingFeatures.size() > 0) {
                InterfaceMeetfunction.pbui_Item_MeetFunConfigDetailInfo info = presenter.meetingFeatures.get(0);
                int funcode = info.getFuncode();
                showFragment(funcode);
            }
        }
    }

    @Override
    public void showFragment(int funcode) {
        LogUtil.i(TAG, "showFragment funcode=" + funcode);
        saveFunCode = funcode;
        if (funcode > Constant.FUN_CODE) {
            featuresAdapter.setSelected(-1);
        } else {
            featuresAdapter.setSelected(funcode);
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        hideFragment(ft);
        switch (funcode) {
            //会议议程
            case InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_AGENDA_BULLETIN_VALUE: {
                if (agendaFragment == null) {
                    agendaFragment = new AgendaFragment();
                    ft.add(R.id.meet_fl, agendaFragment);
                }
                ft.show(agendaFragment);
                break;
            }
            //会议资料
            case InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_MATERIAL_VALUE: {
                if (materialFragment == null) {
                    materialFragment = new MaterialFragment();
                    ft.add(R.id.meet_fl, materialFragment);
                }
                ft.show(materialFragment);
                break;
            }
            //批注查看
            case InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_POSTIL_VALUE: {
                if (annotateFragment == null) {
                    annotateFragment = new AnnotateFragment();
                    ft.add(R.id.meet_fl, annotateFragment);
                }
                ft.show(annotateFragment);
                break;
            }
            //互动交流
            case InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_MESSAGE_VALUE: {
                if (chatFragment == null) {
                    chatFragment = new ChatFragment();
                    ft.add(R.id.meet_fl, chatFragment);
                }
                ft.show(chatFragment);
                break;
            }
            //视频直播
            case InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_VIDEOSTREAM_VALUE: {
                LiveVideoFragment.isVideoManage = false;
                if (liveVideoFragment == null) {
                    liveVideoFragment = new LiveVideoFragment();
                    ft.add(R.id.meet_fl, liveVideoFragment);
                }
                ft.show(liveVideoFragment);
                break;
            }
            //电子白板
            case InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_WHITEBOARD_VALUE: {
                if (drawFragment == null) {
                    drawFragment = new DrawFragment();
                    ft.add(R.id.meet_fl, drawFragment);
                }
                ft.show(drawFragment);
                break;
            }
            //网页浏览
            case InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_WEBBROWSER_VALUE: {
                if (webFragment == null) {
                    webFragment = new WebFragment();
                    ft.add(R.id.meet_fl, webFragment);
                }
                ft.show(webFragment);
                break;
            }
            //签到信息
            case InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_SIGNINRESULT_VALUE: {
                if (signFragment == null) {
                    signFragment = new SignFragment();
                    ft.add(R.id.meet_fl, signFragment);
                }
                ft.show(signFragment);
                break;
            }
            //终端控制  其它功能
            case Constant.FUN_CODE_TERMINAL: {
                if (clientControlFragment == null) {
                    clientControlFragment = new TerminalControlFragment();
                    ft.add(R.id.meet_fl, clientControlFragment);
                }
                ft.show(clientControlFragment);
                break;
            }
            //投票管理
            case Constant.FUN_CODE_VOTE: {
                VoteManageFragment.IS_VOTE_PAGE = true;
                if (voteManageFragment == null) {
                    voteManageFragment = new VoteManageFragment();
                    ft.add(R.id.meet_fl, voteManageFragment);
                }
                ft.show(voteManageFragment);
                break;
            }
            //选举管理
            case Constant.FUN_CODE_ELECTION: {
                VoteManageFragment.IS_VOTE_PAGE = false;
                if (voteManageFragment == null) {
                    voteManageFragment = new VoteManageFragment();
                    ft.add(R.id.meet_fl, voteManageFragment);
                }
                ft.show(voteManageFragment);
                break;
            }
            //视频控制
            case Constant.FUN_CODE_VIDEO: {
                LiveVideoFragment.isVideoManage = true;
                if (liveVideoFragment == null) {
                    liveVideoFragment = new LiveVideoFragment();
                    ft.add(R.id.meet_fl, liveVideoFragment);
                }
                ft.show(liveVideoFragment);
                break;
            }
            //屏幕管理
            case Constant.FUN_CODE_SCREEN: {
                if (screenManageFragment == null) {
                    screenManageFragment = new ScreenManageFragment();
                    ft.add(R.id.meet_fl, screenManageFragment);
                }
                ft.show(screenManageFragment);
                break;
            }
            //公告管理
            case Constant.FUN_CODE_BULLETIN: {
                if (bulletFragment == null) {
                    bulletFragment = new BulletFragment();
                    ft.add(R.id.meet_fl, bulletFragment);
                }
                ft.show(bulletFragment);
                break;
            }
            default:
                break;
        }
        ft.commitAllowingStateLoss();//允许状态丢失，其他完全一样
//        ft.commit();//出现异常：Can not perform this action after onSaveInstanceState
    }

    private void hideFragment(FragmentTransaction ft) {
        if (agendaFragment != null) ft.hide(agendaFragment);
        if (materialFragment != null) ft.hide(materialFragment);
        if (annotateFragment != null) ft.hide(annotateFragment);
        if (webFragment != null) ft.hide(webFragment);
        if (signFragment != null) ft.hide(signFragment);
        if (chatFragment != null) ft.hide(chatFragment);
        if (drawFragment != null) ft.hide(drawFragment);
        if (clientControlFragment != null) ft.hide(clientControlFragment);
        if (voteManageFragment != null) ft.hide(voteManageFragment);
        if (screenManageFragment != null) ft.hide(screenManageFragment);
        if (bulletFragment != null) ft.hide(bulletFragment);
        if (liveVideoFragment != null) ft.hide(liveVideoFragment);
    }

    @Override
    public void onBackPressed() {
        exit2main();
    }

    private void exit2main() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.exit_main_tip);
        builder.setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                jump2main();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void jump2main() {
        finish();
        Intent intent = new Intent(MeetingActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void initView() {
        this.meet_left_ll = (LinearLayout) findViewById(R.id.meet_left_ll);
        this.meet_top_ll = (LinearLayout) findViewById(R.id.meet_top_ll);
        this.meet_iv_logo = (ImageView) findViewById(R.id.meet_iv_logo);
        this.meet_rv = (RecyclerView) findViewById(R.id.meet_rv);
        this.meet_tv_time = (TextView) findViewById(R.id.meet_tv_time);
        this.meet_tv_date = (TextView) findViewById(R.id.meet_tv_date);
        this.meet_tv_meet_name = (TextView) findViewById(R.id.meet_tv_meet_name);
        this.meet_tv_member_role = (TextView) findViewById(R.id.meet_tv_member_role);
        this.meet_tv_member_name = (TextView) findViewById(R.id.meet_tv_member_name);
        this.meet_iv_close = (ImageView) findViewById(R.id.meet_iv_close);
        this.meet_iv_min = (ImageView) findViewById(R.id.meet_iv_min);
        this.meet_top_line = (TextView) findViewById(R.id.meet_top_line);
        this.meet_tv_online = (TextView) findViewById(R.id.meet_tv_online);
        this.meet_iv_message = (ImageView) findViewById(R.id.meet_iv_message);
        this.meet_iv_news = (ImageView) findViewById(R.id.meet_iv_news);
        this.meet_fl = (FrameLayout) findViewById(R.id.meet_fl);
        this.other_feature_ll = (LinearLayout) findViewById(R.id.other_feature_ll);
        if (mBadge == null) {
            /** ************ ******  设置未读消息展示  ****** ************ **/
            mBadge = new QBadgeView(this).bindTarget(meet_iv_message);
            mBadge.setBadgeGravity(Gravity.END | Gravity.TOP);
            mBadge.setBadgeTextSize(14, true);
            mBadge.setShowShadow(true);
            mBadge.setOnDragStateChangedListener((dragState, badge, targetView) -> {
                //只需要空实现，就可以拖拽消除未读消息
            });
        }
        other_feature_ll.setOnClickListener(this);
        meet_iv_close.setOnClickListener(this);
        meet_iv_min.setOnClickListener(this);
        meet_iv_message.setOnClickListener(this);
        meet_iv_news.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.other_feature_ll:
                if (GlobalValue.localRole == InterfaceMacro.Pb_MeetMemberRole.Pb_role_member_compere.getNumber()
                        || GlobalValue.localRole == InterfaceMacro.Pb_MeetMemberRole.Pb_role_member_secretary.getNumber()
                        || GlobalValue.localRole == InterfaceMacro.Pb_MeetMemberRole.Pb_role_admin.getNumber()) {
                    showOtherFeature();
                } else {
                    ToastUtils.showShort(R.string.you_have_no_permission);
                }
                break;
            case R.id.meet_iv_close:
                exit2main();
                break;
            case R.id.meet_iv_min:
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                break;
            case R.id.meet_iv_message:
                showFragment(InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_MESSAGE_VALUE);
                break;
            case R.id.meet_iv_news:
                break;
            default:
                break;
        }
    }

    private void showOtherFeature() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.pop_other_feature, null, false);
        int width = meet_left_ll.getWidth();
        int height = meet_left_ll.getHeight();
        int yoff = meet_top_ll.getHeight();
        featurePop = PopUtil.createAt(inflate, width, height, other_feature_ll, width, yoff);
        ViewHolder viewHolder = new ViewHolder(inflate);
        holderEvent(viewHolder);
    }

    private void setSelected(ViewHolder holder, int code) {
        holder.feature_terminal.setSelected(code == Constant.FUN_CODE_TERMINAL);
        holder.iv_terminal.setSelected(code == Constant.FUN_CODE_TERMINAL);
        holder.tv_terminal.setSelected(code == Constant.FUN_CODE_TERMINAL);

        holder.feature_vote_manage.setSelected(code == Constant.FUN_CODE_VOTE);
        holder.iv_vote_manage.setSelected(code == Constant.FUN_CODE_VOTE);
        holder.tv_vote_manage.setSelected(code == Constant.FUN_CODE_VOTE);

        holder.feature_election_manage.setSelected(code == Constant.FUN_CODE_ELECTION);
        holder.iv_election_manage.setSelected(code == Constant.FUN_CODE_ELECTION);
        holder.tv_election_manage.setSelected(code == Constant.FUN_CODE_ELECTION);

        holder.feature_video_control.setSelected(code == Constant.FUN_CODE_VIDEO);
        holder.iv_video_control.setSelected(code == Constant.FUN_CODE_VIDEO);
        holder.tv_video_control.setSelected(code == Constant.FUN_CODE_VIDEO);

        holder.feature_screen_manage.setSelected(code == Constant.FUN_CODE_SCREEN);
        holder.iv_screen_manage.setSelected(code == Constant.FUN_CODE_SCREEN);
        holder.tv_screen_manage.setSelected(code == Constant.FUN_CODE_SCREEN);

        holder.feature_bulletin.setSelected(code == Constant.FUN_CODE_BULLETIN);
        holder.iv_bulletin.setSelected(code == Constant.FUN_CODE_BULLETIN);
        holder.tv_bulletin.setSelected(code == Constant.FUN_CODE_BULLETIN);
    }

    private void holderEvent(ViewHolder holder) {
        setSelected(holder, saveFunCode);
        holder.feature_terminal.setOnClickListener(v -> {
            setSelected(holder, Constant.FUN_CODE_TERMINAL);
            showFragment(Constant.FUN_CODE_TERMINAL);
            featurePop.dismiss();
        });
        holder.feature_vote_manage.setOnClickListener(v -> {
            setSelected(holder, Constant.FUN_CODE_VOTE);
            showFragment(Constant.FUN_CODE_VOTE);
            featurePop.dismiss();
        });
        holder.feature_election_manage.setOnClickListener(v -> {
            setSelected(holder, Constant.FUN_CODE_ELECTION);
            showFragment(Constant.FUN_CODE_ELECTION);
            featurePop.dismiss();
        });
        holder.feature_video_control.setOnClickListener(v -> {
            setSelected(holder, Constant.FUN_CODE_VIDEO);
            showFragment(Constant.FUN_CODE_VIDEO);
            featurePop.dismiss();
        });
        holder.feature_screen_manage.setOnClickListener(v -> {
            setSelected(holder, Constant.FUN_CODE_SCREEN);
            showFragment(Constant.FUN_CODE_SCREEN);
            featurePop.dismiss();
        });
        holder.feature_bulletin.setOnClickListener(v -> {
            setSelected(holder, Constant.FUN_CODE_BULLETIN);
            showFragment(Constant.FUN_CODE_BULLETIN);
            featurePop.dismiss();
        });
    }


    private PopPushMemberAdapter pushMemberAdapter;
    private PopPushProjectionAdapter pushProjectionAdapter;
    private PopupWindow pushPop;

    @Override
    public void showPushView(List<DevMember> onlineMembers, List<InterfaceDevice.pbui_Item_DeviceDetailInfo> onLineProjectors, int mediaId) {
        LogUtil.d(TAG, "showPushView -->" + "展示推送弹框视图");
        if (pushPop != null && pushPop.isShowing()) {
            pushMemberAdapter.notifyDataSetChanged();
            pushMemberAdapter.notifyChecks();
            pushProjectionAdapter.notifyDataSetChanged();
            pushProjectionAdapter.notifyChecks();
        } else {
            View inflate = LayoutInflater.from(this).inflate(R.layout.pop_push_view, null);
            pushPop = PopUtil.createHalfPop(inflate, meet_left_ll);
            CheckBox pop_push_member_cb = inflate.findViewById(R.id.pop_push_member_cb);
            RecyclerView pop_push_member_rv = inflate.findViewById(R.id.pop_push_member_rv);
            pushMemberAdapter = new PopPushMemberAdapter(R.layout.item_single_button, onlineMembers);
            pop_push_member_rv.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
            pop_push_member_rv.setAdapter(pushMemberAdapter);
            pushMemberAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    pushMemberAdapter.choose(onlineMembers.get(position).getDeviceDetailInfo().getDevcieid());
                    pop_push_member_cb.setChecked(pushMemberAdapter.isChooseAll());
                }
            });
            pop_push_member_cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = pop_push_member_cb.isChecked();
                    pop_push_member_cb.setChecked(checked);
                    pushMemberAdapter.setChooseAll(checked);
                }
            });
            CheckBox pop_push_projection_cb = inflate.findViewById(R.id.pop_push_projection_cb);
            RecyclerView pop_push_projection_rv = inflate.findViewById(R.id.pop_push_projection_rv);
            pushProjectionAdapter = new PopPushProjectionAdapter(R.layout.item_single_button, onLineProjectors);
            pop_push_projection_rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            pop_push_projection_rv.setAdapter(pushProjectionAdapter);
            pushProjectionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    pushProjectionAdapter.choose(onlineMembers.get(position).getDeviceDetailInfo().getDevcieid());
                    pop_push_projection_cb.setChecked(pushProjectionAdapter.isChooseAll());
                }
            });
            pop_push_projection_cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = pop_push_projection_cb.isChecked();
                    pop_push_projection_cb.setChecked(checked);
                    pushProjectionAdapter.setChooseAll(checked);
                }
            });
            //推送文件
            inflate.findViewById(R.id.pop_push_determine).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Integer> devIds = pushMemberAdapter.getDevIds();
                    devIds.addAll(pushProjectionAdapter.getDevIds());
                    if (!devIds.isEmpty()) {
                        pushPop.dismiss();
                        jni.mediaPlayOperate(mediaId, devIds, 0, RESOURCE_ID_0, 0, InterfaceMacro.Pb_MeetPlayFlag.Pb_MEDIA_PLAYFLAG_ZERO.getNumber());
                    } else {
                        ToastUtils.showShort(R.string.please_choose_push_target);
                    }
                }
            });
            //停止推送
            inflate.findViewById(R.id.pop_push_stop).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Integer> devIds = pushMemberAdapter.getDevIds();
                    devIds.addAll(pushProjectionAdapter.getDevIds());
                    if (!devIds.isEmpty()) {
                        pushPop.dismiss();
                        List<Integer> temps = new ArrayList<>();
                        temps.add(0);
                        jni.stopResourceOperate(temps, devIds);
                    } else {
                        ToastUtils.showShort(R.string.please_choose_push_target);
                    }
                }
            });
            //取消
            inflate.findViewById(R.id.pop_push_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pushPop.dismiss();
                    pushPop = null;
                }
            });
        }
    }


    public static class ViewHolder {
        public ImageView iv_terminal;
        public TextView tv_terminal;
        public LinearLayout feature_terminal;
        public ImageView iv_vote_manage;
        public TextView tv_vote_manage;
        public LinearLayout feature_vote_manage;
        public ImageView iv_election_manage;
        public TextView tv_election_manage;
        public LinearLayout feature_election_manage;
        public ImageView iv_video_control;
        public TextView tv_video_control;
        public LinearLayout feature_video_control;
        public ImageView iv_screen_manage;
        public TextView tv_screen_manage;
        public LinearLayout feature_screen_manage;
        public ImageView iv_bulletin;
        public TextView tv_bulletin;
        public LinearLayout feature_bulletin;

        public ViewHolder(View rootView) {
            this.iv_terminal = (ImageView) rootView.findViewById(R.id.iv_terminal);
            this.tv_terminal = (TextView) rootView.findViewById(R.id.tv_terminal);
            this.feature_terminal = (LinearLayout) rootView.findViewById(R.id.feature_terminal);
            this.iv_vote_manage = (ImageView) rootView.findViewById(R.id.iv_vote_manage);
            this.tv_vote_manage = (TextView) rootView.findViewById(R.id.tv_vote_manage);
            this.feature_vote_manage = (LinearLayout) rootView.findViewById(R.id.feature_vote_manage);
            this.iv_election_manage = (ImageView) rootView.findViewById(R.id.iv_election_manage);
            this.tv_election_manage = (TextView) rootView.findViewById(R.id.tv_election_manage);
            this.feature_election_manage = (LinearLayout) rootView.findViewById(R.id.feature_election_manage);
            this.iv_video_control = (ImageView) rootView.findViewById(R.id.iv_video_control);
            this.tv_video_control = (TextView) rootView.findViewById(R.id.tv_video_control);
            this.feature_video_control = (LinearLayout) rootView.findViewById(R.id.feature_video_control);
            this.iv_screen_manage = (ImageView) rootView.findViewById(R.id.iv_screen_manage);
            this.tv_screen_manage = (TextView) rootView.findViewById(R.id.tv_screen_manage);
            this.feature_screen_manage = (LinearLayout) rootView.findViewById(R.id.feature_screen_manage);
            this.iv_bulletin = (ImageView) rootView.findViewById(R.id.iv_bulletin);
            this.tv_bulletin = (TextView) rootView.findViewById(R.id.tv_bulletin);
            this.feature_bulletin = (LinearLayout) rootView.findViewById(R.id.feature_bulletin);
        }

    }

    private final int REQUEST_CODE_EXPORT_NOTE = 1;

    @Override
    public void exportNoteFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE_EXPORT_NOTE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EXPORT_NOTE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            File file = UriUtils.uri2File(uri);
            if (file != null) {
                if (file.getName().endsWith(".txt")) {
                    String content = FileIOUtils.readFile2String(file);
                    EventBus.getDefault().post(new EventMessage.Builder().type(EventType.BUS_EXPORT_NOTE_CONTENT).objects(content).build());
                } else {
                    ToastUtils.showShort(R.string.please_choose_txt_file);
                }
            }
        }
    }
}
