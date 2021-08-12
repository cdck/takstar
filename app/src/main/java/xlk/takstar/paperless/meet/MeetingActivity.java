package xlk.takstar.paperless.meet;

import android.app.Activity;
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
import android.widget.RelativeLayout;
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
import xlk.takstar.paperless.fragment.livevideo.VideoFragment;
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
import xlk.takstar.paperless.model.node.FeaturesFootNode;
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
    private TextView meet_top_line;
    private TextView meet_tv_online;
    private RelativeLayout meet_iv_message;
    private RelativeLayout meet_iv_news;
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
    private VideoFragment videoFragment;
    private ScoreManageFragment scoreManageFragment;
    private FeaturesNodeAdapter nodeAdapter;
    /**
     * 保存当前点击的目录id
     */
    private int currentClickDirId = -1;
    private boolean alreadyShow = false;
    private ImageView welcome_view;
    private int firstDirId;

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
                nodeAdapter.setDefaultSelected(Constant.function_code_board);
                nodeAdapter.clickFeature(FeaturesNodeAdapter.ClickType.FEATURE, Constant.function_code_board);
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

    /**
     * 展开会议资料的时候将更多功能进行收缩
     */
    private void expandMaterial() {
        boolean isExpanded = false;
        for (int i = 0; i < presenter.features.size(); i++) {
            BaseNode baseNode = presenter.features.get(i);
            if (baseNode instanceof FeaturesParentNode) {
                FeaturesParentNode parentNode = (FeaturesParentNode) baseNode;
                if (parentNode.getFeatureId() == Constant.function_code_material) {
                    isExpanded = parentNode.isExpanded();
                    break;
                }
            }
        }
        if (isExpanded) {
            //展开会议资料功能时，需要先将更多功能（所有展开状态的node）进行收缩
            for (int i = 0; i < presenter.features.size(); i++) {
                BaseNode baseNode = presenter.features.get(i);
                if (baseNode instanceof FeaturesFootNode) {
                    FeaturesFootNode parentNode = (FeaturesFootNode) baseNode;
                    parentNode.setExpanded(false);
                    break;
                }
            }
            nodeAdapter.setList(presenter.features);
            nodeAdapter.notifyDataSetChanged();
        }
    }

    /**
     * @param isExpanded 这次将要进行的状态 =true展开，=false收缩
     */
    public void setMoreFeaturesSticky(boolean isExpanded) {
        if (isExpanded) {
            //展开更多功能时，需要先将会议资料（所有展开状态的node）进行收缩，不然更多功能展开后会显示不全
            for (int i = 0; i < presenter.features.size(); i++) {
                BaseNode baseNode = presenter.features.get(i);
                if (baseNode instanceof FeaturesParentNode) {
                    FeaturesParentNode parentNode = (FeaturesParentNode) baseNode;
                    if (parentNode.getFeatureId() == Constant.function_code_material) {
                        parentNode.setExpanded(false);
                        break;
                    }
                }
            }
            nodeAdapter.setList(presenter.features);
            nodeAdapter.notifyDataSetChanged();
        }
        for (int i = 0; i < presenter.features.size(); i++) {
            BaseNode baseNode = presenter.features.get(i);
            if (baseNode instanceof FeaturesFootNode) {
                meet_rv.scrollToPosition(i);
                LinearLayoutManager manager = (LinearLayoutManager) meet_rv.getLayoutManager();
                manager.scrollToPositionWithOffset(i, 0);
                return;
            }
        }
    }

    @Override
    public void setFirstDirId(int dirId) {
        LogUtils.i("setFirstDirId currentClickDirId=" + currentClickDirId + ",dirId=" + dirId + ",firstDirId=" + firstDirId);
        firstDirId = dirId;
    }

    private void showWelcomePage() {
        saveFunCode = -1;
        showFragment(-1);//主要是用于隐藏掉当前展示的fragment
        nodeAdapter.setDefaultSelected(-1);//取消选中状态
        welcome_view.setVisibility(View.VISIBLE);
        meet_fl.setVisibility(View.GONE);
//        for (int i = 0; i < presenter.features.size(); i++) {
//            BaseNode baseNode = presenter.features.get(i);
//            if (baseNode instanceof FeaturesParentNode) {
//                FeaturesParentNode node = (FeaturesParentNode) baseNode;
//                if (node.getFeatureId() == Constant.function_code_material) {
//                    showFragment(Constant.function_code_material);
//                    nodeAdapter.clearChildSelectedStatus();
//                    nodeAdapter.setDefaultSelected(Constant.function_code_material);
//                    nodeAdapter.clickFeature(FeaturesNodeAdapter.ClickType.FEATURE, Constant.function_code_material);
//                    nodeAdapter.notifyDataSetChanged();
//                    alreadyShow = true;
//                    return;
//                }
//            }
//        }
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
                    switch (clickType) {
                        //点击了某一功能
                        case FEATURE: {
                            int id = (int) obj[0];
                            if (id == Constant.function_code_material) {
                                expandMaterial();
                                break;
                            }
                            if (id != Constant.function_code_board) {
                                currentClickDirId = -1;
                            }
                            LogUtils.i("点击了某一功能 id=" + id);
                            showFragment(id);
                            break;
                        }
                        //点击了目录
                        case DIRECTORY: {
                            currentClickDirId = (int) obj[0];
                            LogUtils.i("点击了目录 currentClickDirId=" + currentClickDirId);
                            showFragment(Constant.function_code_material);
                            break;
                        }
                        //点击了更多功能
                        case FOOT_FEATURE: {
                            saveFunCode = Constant.FUN_CODE;
                            boolean isExpanded = (boolean) obj[0];
                            LogUtils.i("点击了更多功能 isExpanded=" + isExpanded);
                            setMoreFeaturesSticky(isExpanded);
                            break;
                        }
                    }
                }
            });
        } else {
            nodeAdapter.setList(presenter.features);
            nodeAdapter.notifyDataSetChanged();
        }
        if (saveFunCode == -1) {
            showWelcomePage();
//            setChooseDefaultFeature();
        } else {
            LogUtils.i(TAG, "之前展示的功能id=" + saveFunCode);
            if (saveFunCode < Constant.FUN_CODE) {//说明之前展示的是参会人功能模块
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
                    showWelcomePage();
//                    setChooseDefaultFeature();
                }
            } else {//之前展示的是固定的其它功能模块
                //不需要操作
            }
        }
    }

    /**
     * 设置默认选中第一个功能，如果一个功能都没有就进行隐藏掉之前显示的功能fragment视图
     */
//    private void setChooseDefaultFeature() {
//        if (!presenter.features.isEmpty()) {
//            BaseNode baseNode = presenter.features.get(0);
//            if (baseNode instanceof FeaturesParentNode) {
//                FeaturesParentNode parentNode = (FeaturesParentNode) baseNode;
//                if (parentNode.getFeatureId() == Constant.function_code_material) {
//                    showWelcomePage();
//                } else {
//                    showFragment(parentNode.getFeatureId());
//                    nodeAdapter.setDefaultSelected(parentNode.getFeatureId());
//                }
//            }
//        } else {
//            showWelcomePage();
//        }
//    }

    @Override
    public void showFragment(int funcode) {
        if (funcode != -1 /*&& !alreadyShow*/) {
            welcome_view.setVisibility(View.GONE);
            meet_fl.setVisibility(View.VISIBLE);
//            alreadyShow = true;
        }
        LogUtil.i(TAG, "showFragment funcode=" + funcode);
        if (funcode != Constant.function_code_board) {
            saveFunCode = funcode;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        hideFragment(ft);
        switch (funcode) {
            //会议议程
            case Constant.function_code_agenda: {
                if (agendaFragment == null) {
                    agendaFragment = new AgendaFragment();
                    ft.add(R.id.meet_fl, agendaFragment);
                }
                ft.show(agendaFragment);
                break;
            }
            //会议资料
            case Constant.function_code_material: {
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
            case Constant.function_code_annotation: {
                if (annotateFragment == null) {
                    annotateFragment = new AnnotateFragment();
                    ft.add(R.id.meet_fl, annotateFragment);
                }
                ft.show(annotateFragment);
                break;
            }
            //互动交流
            case Constant.function_code_chat: {
                if (chatFragment == null) {
                    chatFragment = new ChatFragment();
                    ft.add(R.id.meet_fl, chatFragment);
                }
                ft.show(chatFragment);
                break;
            }
            //视频直播
            case Constant.function_code_video: {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isManage", false);
                if (videoFragment == null) {
                    videoFragment = new VideoFragment();
                    ft.add(R.id.meet_fl, videoFragment);
                }
                videoFragment.setArguments(bundle);
                ft.show(videoFragment);
                break;
            }
            //电子白板
            case Constant.function_code_board: {
                if (drawFragment == null) {
                    drawFragment = new DrawFragment();
                    ft.add(R.id.meet_fl, drawFragment);
                }
                ft.show(drawFragment);
                break;
            }
            //网页浏览
            case Constant.function_code_web: {
                if (webFragment == null) {
                    webFragment = new WebFragment();
                    ft.add(R.id.meet_fl, webFragment);
                }
                ft.show(webFragment);
                break;
            }
//            //签到信息
//            case Constant.function_code_sign: {
//                if (signFragment == null) {
//                    signFragment = new SignFragment();
//                    ft.add(R.id.meet_fl, signFragment);
//                }
//                ft.show(signFragment);
//                break;
//            }
            //评分查看
            case Constant.function_code_rate: {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isManage", false);
                if (scoreManageFragment == null) {
                    scoreManageFragment = new ScoreManageFragment();
                    ft.add(R.id.meet_fl, scoreManageFragment);
                }
                scoreManageFragment.setArguments(bundle);
                ft.show(scoreManageFragment);
                break;
            }
            //公告查看
            case Constant.function_code_bulletin: {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isManage", false);
                if (bulletFragment == null) {
                    bulletFragment = new BulletFragment();
                    ft.add(R.id.meet_fl, bulletFragment);
                }
                bulletFragment.setArguments(bundle);
                ft.show(bulletFragment);
                break;
            }
            //投票查看
            case Constant.function_code_vote: {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isVote", true);
                bundle.putBoolean("isManage", false);
                if (voteManageFragment == null) {
                    voteManageFragment = new VoteManageFragment();
                    ft.add(R.id.meet_fl, voteManageFragment);
                }
                voteManageFragment.setArguments(bundle);
                ft.show(voteManageFragment);
                break;
            }
            //选举查看
            case Constant.function_code_election: {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isVote", false);
                bundle.putBoolean("isManage", false);
                if (voteManageFragment == null) {
                    voteManageFragment = new VoteManageFragment();
                    ft.add(R.id.meet_fl, voteManageFragment);
                }
                voteManageFragment.setArguments(bundle);
                ft.show(voteManageFragment);
                break;
            }
            /* **** **  其它功能  ** **** */
            //终端控制
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
                Bundle bundle = new Bundle();
                bundle.putBoolean("isVote", true);
                bundle.putBoolean("isManage", true);
                if (voteManageFragment == null) {
                    voteManageFragment = new VoteManageFragment();
                    ft.add(R.id.meet_fl, voteManageFragment);
                }
                voteManageFragment.setArguments(bundle);
                ft.show(voteManageFragment);
                break;
            }
            //选举管理
            case Constant.FUN_CODE_ELECTION: {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isVote", false);
                bundle.putBoolean("isManage", true);
                if (voteManageFragment == null) {
                    voteManageFragment = new VoteManageFragment();
                    ft.add(R.id.meet_fl, voteManageFragment);
                }
                voteManageFragment.setArguments(bundle);
                ft.show(voteManageFragment);
                break;
            }
            //视频控制
            case Constant.FUN_CODE_VIDEO: {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isManage", true);
                if (videoFragment == null) {
                    videoFragment = new VideoFragment();
                    ft.add(R.id.meet_fl, videoFragment);
                }
                videoFragment.setArguments(bundle);
                ft.show(videoFragment);
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
                Bundle bundle = new Bundle();
                bundle.putBoolean("isManage", true);
                if (bulletFragment == null) {
                    bulletFragment = new BulletFragment();
                    ft.add(R.id.meet_fl, bulletFragment);
                }
                bulletFragment.setArguments(bundle);
                ft.show(bulletFragment);
                break;
            }
            //评分管理
            case Constant.FUN_CODE_SCORE: {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isManage", true);
                if (scoreManageFragment == null) {
                    scoreManageFragment = new ScoreManageFragment();
                    ft.add(R.id.meet_fl, scoreManageFragment);
                }
                scoreManageFragment.setArguments(bundle);
                ft.show(scoreManageFragment);
                break;
            }
            //签到查看
            case Constant.FUN_CODE_SIGN: {
                if (signFragment == null) {
                    signFragment = new SignFragment();
                    ft.add(R.id.meet_fl, signFragment);
                }
                ft.show(signFragment);
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
        if (videoFragment != null) ft.hide(videoFragment);
        if (scoreManageFragment != null) ft.hide(scoreManageFragment);
    }

    @Override
    public void onBackPressed() {
        exit2main();
    }

    private void exit2main() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.pop_operating_tip, null);
        PopupWindow pop = PopUtil.createSmallPop(inflate, getWindow().getDecorView());
        TextView tv_title = inflate.findViewById(R.id.tv_title);
        TextView tv_message = inflate.findViewById(R.id.tv_message);
        tv_title.setText(getString(R.string.operating_tips));
        tv_message.setText(getString(R.string.exit_main_tip));
        inflate.findViewById(R.id.btn_cancel).setOnClickListener(v -> pop.dismiss());
        inflate.findViewById(R.id.btn_define).setOnClickListener(v -> {
            pop.dismiss();
            jump2main();
        });
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

        this.meet_top_line = (TextView) findViewById(R.id.meet_top_line);
        this.meet_tv_online = (TextView) findViewById(R.id.meet_tv_online);
        this.meet_iv_message = (RelativeLayout) findViewById(R.id.meet_iv_message);
        this.meet_iv_news = (RelativeLayout) findViewById(R.id.meet_iv_news);
        this.meet_fl = (FrameLayout) findViewById(R.id.meet_fl);
        welcome_view = (ImageView) findViewById(R.id.welcome_view);
        if (mBadge == null) {
            /** ************ ******  设置未读消息展示  ****** ************ **/
            mBadge = new QBadgeView(this).bindTarget(meet_iv_message);
            mBadge.setBadgeGravity(Gravity.END | Gravity.TOP);
            mBadge.setBadgeTextSize(8, true);
            mBadge.setShowShadow(true);
            mBadge.setOnDragStateChangedListener((dragState, badge, targetView) -> {
                //只需要空实现，就可以拖拽消除未读消息
            });
        }
        findViewById(R.id.meet_iv_close).setOnClickListener(this);
        findViewById(R.id.meet_iv_min).setOnClickListener(this);
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
//                moveTaskToBack(true);
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                break;
            case R.id.meet_iv_message:
                showFragment(Constant.function_code_chat);
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
