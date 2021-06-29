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
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.UriUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.mogujie.tt.protobuf.InterfaceDevice;
import com.mogujie.tt.protobuf.InterfaceMacro;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;
import xlk.takstar.paperless.R;
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
import xlk.takstar.paperless.fragment.score.ScoreManageFragment;
import xlk.takstar.paperless.fragment.screen.ScreenManageFragment;
import xlk.takstar.paperless.fragment.sign.SignFragment;
import xlk.takstar.paperless.fragment.terminal.TerminalControlFragment;
import xlk.takstar.paperless.fragment.vote.VoteManageFragment;
import xlk.takstar.paperless.fragment.web.WebFragment;
import xlk.takstar.paperless.main.MainActivity;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.model.EventMessage;
import xlk.takstar.paperless.model.EventType;
import xlk.takstar.paperless.model.bean.DevMember;
import xlk.takstar.paperless.model.node.FeaturesNodeAdapter;
import xlk.takstar.paperless.model.node.FeaturesParentNode;
import xlk.takstar.paperless.ui.RvItemDecoration;
import xlk.takstar.paperless.util.LogUtil;
import xlk.takstar.paperless.util.PopUtil;

import static xlk.takstar.paperless.model.Constant.RESOURCE_ID_0;

public class MeetingActivity extends BaseActivity<MeetingPresenter> implements MeetingContract.View, View.OnClickListener {

    public static boolean firstTimeIn = false;
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
    private VoteManageFragment voteManageFragment;
    private ScreenManageFragment screenManageFragment;
    private BulletFragment bulletFragment;
    private LiveVideoFragment liveVideoFragment;
    private ScoreManageFragment scoreManageFragment;
    private FeaturesNodeAdapter nodeAdapter;
    /**
     * 保存当前点击的目录id
     */
    private int currentClickDirId = -1;

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
        firstTimeIn = true;
        initView();
        presenter.initial();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String draw = intent.getStringExtra("draw");
        LogUtil.i(TAG, "onNewIntent draw=" + draw);
        if (draw != null && draw.equals("draw")) {
            if (nodeAdapter != null) {
                nodeAdapter.clearChildSelectedStatus();
                nodeAdapter.setDefaultSelected(InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_WHITEBOARD_VALUE);
                nodeAdapter.clickFeature(FeaturesNodeAdapter.ClickType.FEATURE,InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_WHITEBOARD_VALUE);
                nodeAdapter.notifyDataSetChanged();
            }
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
    public void collapseOtherFeature() {
        if (nodeAdapter != null) nodeAdapter.expandOrCollapseOtherFeature(false);
        if (saveFunCode > Constant.FUN_CODE) {
            saveFunCode = -1;
            updateMeetingFeatures();
        }
    }

    @Override
    public void updateMeetingFeatures() {
        if (nodeAdapter == null) {
            nodeAdapter = new FeaturesNodeAdapter(presenter.features);
            meet_rv.setAdapter(nodeAdapter);
            meet_rv.addItemDecoration(new RvItemDecoration(this));
            meet_rv.setLayoutManager(new LinearLayoutManager(this));
            nodeAdapter.setNodeClickItemListener(new FeaturesNodeAdapter.NodeClickItem() {
                @Override
                public void onClickItem(FeaturesNodeAdapter.ClickType clickType, Object... obj) {
                    int id = (int) obj[0];
                    if (obj.length > 1) {
                        currentClickDirId = id;
                        //点击了目录
                        showFragment(InterfaceMacro.Pb_Meet_FunctionCode.Pb_MEET_FUNCODE_MATERIAL_VALUE);
                    } else {
                        currentClickDirId = -1;
                        //点击了某一功能
                        showFragment(id);
                    }
                }
            });
        } else {
            nodeAdapter.setList(presenter.features);
            nodeAdapter.notifyDataSetChanged();
        }
        if (saveFunCode == -1) {
            setChooseDefaultFeature();
        } else {
            LogUtils.i(TAG, "之前展示的功能id=" + saveFunCode);
            if (saveFunCode < Constant.FUN_CODE) {//之前展示的是后台提供的功能模块
                //判断更新后之前展示的功能还在不在
                boolean hasFeature = false;
                for (int i = 0; i < presenter.features.size(); i++) {
                    BaseNode baseNode = presenter.features.get(i);
                    if (baseNode instanceof FeaturesParentNode) {
                        FeaturesParentNode parentNode = (FeaturesParentNode) baseNode;
                        if (parentNode.getFeatureId() == saveFunCode) {
                            hasFeature = true;
                            break;
                        }
                    }
                }
                if (!hasFeature) {
                    //更新后没有该功能了
                    setChooseDefaultFeature();
                }
            } else {//之前展示的是固定的其它功能模块
                //不需要操作
//                showFragment(saveFunCode);
//                nodeAdapter.expandOrCollapseOtherFeature(true);
//                nodeAdapter.setSelectChildFeature(saveFunCode);
            }
        }
    }

    /**
     * 设置默认选中第一个功能，如果一个功能都没有就进行隐藏掉之前显示的功能fragment视图
     */
    private void setChooseDefaultFeature() {
        //presenter.features最少都有一个其它功能模块
        BaseNode baseNode = presenter.features.get(0);
        if (baseNode instanceof FeaturesParentNode) {
            FeaturesParentNode parentNode = (FeaturesParentNode) baseNode;
            showFragment(parentNode.getFeatureId());
            nodeAdapter.setDefaultSelected(parentNode.getFeatureId());
        } else {
            showFragment(-1);
            nodeAdapter.setDefaultSelected(-1);
        }
//            else if (presenter.features.size() > 1) {
//                BaseNode baseNode1 = presenter.features.get(1);
//                if (baseNode1 instanceof FeaturesParentNode) {
//                    FeaturesParentNode parentNode = (FeaturesParentNode) baseNode1;
//                    showFragment(parentNode.getFeatureId());
//                    nodeAdapter.setDefaultSelected(parentNode.getFeatureId());
//                }
//            }
    }

    @Override
    public void showFragment(int funcode) {
        LogUtil.i(TAG, "showFragment funcode=" + funcode);
        saveFunCode = funcode;
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
                Bundle bundle = new Bundle();
                bundle.putInt("dirId", currentClickDirId);
                if (materialFragment == null) {
                    materialFragment = new MaterialFragment();
                    ft.add(R.id.meet_fl, materialFragment);
                }
                materialFragment.setArguments(bundle);
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
                Bundle bundle = new Bundle();
                bundle.putBoolean("isVideoManage", false);
                if (liveVideoFragment == null) {
                    liveVideoFragment = new LiveVideoFragment();
                    ft.add(R.id.meet_fl, liveVideoFragment);
                }
                liveVideoFragment.setArguments(bundle);
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
                Bundle bundle = new Bundle();
                bundle.putBoolean("isVideoManage", true);
                if (liveVideoFragment == null) {
                    liveVideoFragment = new LiveVideoFragment();
                    ft.add(R.id.meet_fl, liveVideoFragment);
                }
                liveVideoFragment.setArguments(bundle);
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
            //评分管理
            case Constant.FUN_CODE_SCORE: {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isScoreManage", true);
                if (scoreManageFragment == null) {
                    scoreManageFragment = new ScoreManageFragment();
                    ft.add(R.id.meet_fl, scoreManageFragment);
                }
                scoreManageFragment.setArguments(bundle);
                ft.show(scoreManageFragment);
                break;
            }
            //评分查看
            case 31: {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isScoreManage", false);
                if (scoreManageFragment == null) {
                    scoreManageFragment = new ScoreManageFragment();
                    ft.add(R.id.meet_fl, scoreManageFragment);
                }
                scoreManageFragment.setArguments(bundle);
                ft.show(scoreManageFragment);
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
        if (scoreManageFragment != null) ft.hide(scoreManageFragment);
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
        if (mBadge == null) {
            /** ************ ******  设置未读消息展示  ****** ************ **/
            mBadge = new QBadgeView(this).bindTarget(meet_iv_message);
            mBadge.setBadgeGravity(Gravity.START | Gravity.TOP);
            mBadge.setBadgeTextSize(6, true);
            mBadge.setShowShadow(true);
            mBadge.setOnDragStateChangedListener((dragState, badge, targetView) -> {
                //只需要空实现，就可以拖拽消除未读消息
            });
        }
        meet_iv_close.setOnClickListener(this);
        meet_iv_min.setOnClickListener(this);
        meet_iv_message.setOnClickListener(this);
        meet_iv_news.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
            pushMemberAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
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
            pushProjectionAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
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
                        jni.mediaPlayOperate(mediaId, devIds, 0, RESOURCE_ID_0,
                                0, InterfaceMacro.Pb_MeetPlayFlag.Pb_MEDIA_PLAYFLAG_ZERO.getNumber());
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

    private final int REQUEST_CODE_EXPORT_NOTE = 1;

    @Override
    public void exportNoteFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE_EXPORT_NOTE);
    }

    @Override
    public void exitDraw() {

    }

    @Override
    public void updateMeetingBadgeNumber(int count) {
        runOnUiThread(() -> {
            mBadge.setBadgeNumber(count);
        });
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
