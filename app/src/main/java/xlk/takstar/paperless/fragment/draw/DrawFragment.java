package xlk.takstar.paperless.fragment.draw;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.UriUtils;
import com.google.protobuf.ByteString;
import com.mogujie.tt.protobuf.InterfaceMacro;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.adapter.DevMemberAdapter;
import xlk.takstar.paperless.base.BaseFragment;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.model.EventMessage;
import xlk.takstar.paperless.model.EventType;
import xlk.takstar.paperless.model.GlobalValue;
import xlk.takstar.paperless.model.bean.DevMember;
import xlk.takstar.paperless.service.fab.FabService;
import xlk.takstar.paperless.ui.ArtBoard;
import xlk.takstar.paperless.ui.ColorPickerDialog;
import xlk.takstar.paperless.util.FileUtil;
import xlk.takstar.paperless.util.LogUtil;
import xlk.takstar.paperless.util.PopUtil;

import static xlk.takstar.paperless.ui.ArtBoard.LocalPathList;
import static xlk.takstar.paperless.util.ConvertUtil.bmp2bs;

/**
 * @author Created by xlk on 2020/12/7.
 * @desc
 */
public class DrawFragment extends BaseFragment<DrawPresenter> implements DrawContract.View, View.OnClickListener {
    private ImageView iv_exit;
    private ImageView iv_save;
    private ImageView iv_clear;
    private ImageView iv_picture;
    private ImageView iv_text;
    private ImageView iv_color;
    private ImageView iv_back;
    private ImageView iv_round;
    private ImageView iv_rect;
    private ImageView iv_line;
    private ImageView iv_sline;
    private ImageView iv_paint;
    private ImageView iv_eraser;
    private ImageView iv_drag;
    private ImageView iv_size;
    private ImageView iv_annotation;
    private RelativeLayout draw_root;
    private LinearLayout tool_ll;
    private ArtBoard artBoard;
    private PopupWindow memberPop, sizePop;
    private DevMemberAdapter devMemberAdapter;
    private List<ImageView> ivs = new ArrayList<>();
    private final int REQUEST_CODE_PICTURE = 1;
    private boolean isAddScreenShot;//是否在发起共享时,添加截图图片

    public static boolean isDrawing = false;
    public static long launchSrcwbid;
    public static int launchSrcmemid;
    public static boolean isSharing;//当前是否在多人批注
    public static List<PointF> points = new ArrayList<>();
    public static List<ArtBoard.DrawPath> LocalSharingPathList = new ArrayList<>();//共享状态下本机的操作
    public static List<Integer> localOperids = new ArrayList<>();//存放本机的操作ID
    public static List<ArtBoard.DrawPath> pathList = new ArrayList<>();
    public static int disposePicOpermemberid;
    public static int disposePicSrcmemid;
    public static long disposePicSrcwbidd;
    public static List<Integer> togetherIDs = new ArrayList<>();//同屏的ID
    public static int launchPersonId = GlobalValue.localMemberId;//默认发起的人员ID是本机
    public static int mSrcmemid = GlobalValue.localMemberId;//发起的人员ID默认是本机
    public static long mSrcwbid;//发起人的白板标识
    public static ByteString savePicData;//图片数据
    public static ByteString tempPicData;//临时图片数据

    @Override
    protected int getLayoutId() {
        return R.layout.draw_fragment;
    }

    @Override
    protected void initView(View inflate) {
        iv_exit = inflate.findViewById(R.id.iv_exit);
        iv_save = inflate.findViewById(R.id.iv_save);
        iv_clear = inflate.findViewById(R.id.iv_clear);
        iv_picture = inflate.findViewById(R.id.iv_picture);
        iv_text = inflate.findViewById(R.id.iv_text);
        iv_color = inflate.findViewById(R.id.iv_color);
        iv_back = inflate.findViewById(R.id.iv_back);
        iv_round = inflate.findViewById(R.id.iv_round);
        iv_rect = inflate.findViewById(R.id.iv_rect);
        iv_line = inflate.findViewById(R.id.iv_line);
        iv_sline = inflate.findViewById(R.id.iv_sline);
        iv_paint = inflate.findViewById(R.id.iv_paint);
        iv_eraser = inflate.findViewById(R.id.iv_eraser);
        iv_drag = inflate.findViewById(R.id.iv_drag);
        iv_size = inflate.findViewById(R.id.iv_size);
        iv_annotation = inflate.findViewById(R.id.iv_annotation);
        tool_ll = inflate.findViewById(R.id.tool_ll);
        iv_exit.setOnClickListener(this);
        iv_save.setOnClickListener(this);
        iv_clear.setOnClickListener(this);
        iv_picture.setOnClickListener(this);
        iv_text.setOnClickListener(this);
        iv_color.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_round.setOnClickListener(this);
        iv_rect.setOnClickListener(this);
        iv_line.setOnClickListener(this);
        iv_sline.setOnClickListener(this);
        iv_paint.setOnClickListener(this);
        iv_eraser.setOnClickListener(this);
        iv_drag.setOnClickListener(this);
        iv_size.setOnClickListener(this);
        iv_annotation.setOnClickListener(this);

        draw_root = inflate.findViewById(R.id.draw_root);
        ivs.add(iv_round);
        ivs.add(iv_rect);
        ivs.add(iv_line);
        ivs.add(iv_sline);
        ivs.add(iv_paint);
        ivs.add(iv_eraser);
        ivs.add(iv_drag);
        ivs.add(iv_text);
    }

    @Override
    protected DrawPresenter initPresenter() {
        return new DrawPresenter(this, getContext());
    }

    @Override
    protected void onShow() {
//        isDrawing = true;
        presenter.queryMember();
        if (FabService.screenShotBitmap != null) {
            isAddScreenShot = true;//设置发起同屏时是否发送图片
            artBoard.drawZoomBmp(FabService.screenShotBitmap);
        }
        updateShareStatus();
    }

    @Override
    protected void onHide() {
//        isDrawing = false;
//        clear();
        dismissPop(memberPop);
        dismissPop(sizePop);
    }

    private void clear() {
        if (FabService.screenShotBitmap != null) {
            FabService.screenShotBitmap.recycle();
            FabService.screenShotBitmap = null;
        }
        presenter.stopShare();
        LocalPathList.clear();
        LocalSharingPathList.clear();
        localOperids.clear();
        togetherIDs.clear();
        pathList.clear();
        artBoard.clear();
        tempPicData = null;
        savePicData = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDrawing = false;
        clear();
        mSrcmemid = 0;
        mSrcwbid = 0;
        disposePicOpermemberid = 0;
        disposePicSrcmemid = 0;
        disposePicSrcwbidd = 0;
        ArtBoard.artBoardWidth = 0;
        ArtBoard.artBoardHeight = 0;
        artBoard.destroyDrawingCache();
        artBoard = null;
    }

    @Override
    protected void initial() {
        isDrawing = true;
        draw_root.post(this::start);
    }

    private void start() {
        int width = draw_root.getWidth();
        int height = draw_root.getHeight();
        LogUtil.i(TAG, "start 画板宽高=" + width + "," + height);
        artBoard = new ArtBoard(getContext(), width, height);
        draw_root.addView(artBoard);
        onShow();
        artBoard.setDrawTextListener((x, y, screenX, screenY) -> {
            LogUtils.d(TAG, "showEdtPop -->" + x + "," + y);
            View inflate = LayoutInflater.from(getContext()).inflate(R.layout.pop_edittext, null, false);
            int w = ConvertUtils.dp2px(150);
            int h = ConvertUtils.dp2px(50);
            PopupWindow pop = new PopupWindow(inflate, w, h);
            pop.setBackgroundDrawable(new BitmapDrawable());
            // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
            pop.setTouchable(true);
            // true:设置触摸外面时消失
            pop.setOutsideTouchable(true);
            pop.setFocusable(true);
            pop.setAnimationStyle(R.style.pop_animation_t_b);
            pop.showAtLocation(artBoard, Gravity.START | Gravity.TOP, screenX, screenY - h / 2);
            EditText edt = inflate.findViewById(R.id.edt);
            pop.setOnDismissListener(() -> {
                String content = edt.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    artBoard.drawText(x, y, content);
                }
            });
        });
    }

    public void setSelect(int index) {
        for (int i = 0; i < ivs.size(); i++) {
            ImageView imageView = ivs.get(i);
            if (i == index) {
                imageView.setSelected(true);
                artBoard.setDrag(index == 6);
            } else {
                imageView.setSelected(false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_exit:
                presenter.stopShare();
                clear();
                EventBus.getDefault().post(new EventMessage.Builder().type(EventType.BUS_EXIT_DRAW).build());
                break;
            case R.id.iv_save:
                saveDrawPicture();
                break;
            case R.id.iv_clear:
                artBoard.clear();
                //已经清空了就不必发送了
                isAddScreenShot = false;
                break;
            case R.id.iv_picture:
                chooseLocalFile(REQUEST_CODE_PICTURE);
                break;
            case R.id.iv_text:
                setSelect(7);
                artBoard.setDrawType(ArtBoard.DRAW_TEXT);
                break;
            case R.id.iv_color:
                new ColorPickerDialog(getContext(), new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void colorChanged(int color) {
                        artBoard.setPaintColor(color);
                    }
                }, Color.BLACK).show();
                break;
            case R.id.iv_back:
                artBoard.revoke();
                break;
            case R.id.iv_round:
                setSelect(0);
                artBoard.setDrawType(ArtBoard.DRAW_CIRCLE);
                break;
            case R.id.iv_rect:
                setSelect(1);
                artBoard.setDrawType(ArtBoard.DRAW_RECT);
                break;
            case R.id.iv_line:
                setSelect(2);
                artBoard.setDrawType(ArtBoard.DRAW_LINE);
                break;
            case R.id.iv_sline:
                setSelect(3);
                artBoard.setDrawType(ArtBoard.DRAW_SLINE);
                break;
            case R.id.iv_paint:
                setSelect(4);
                artBoard.setDrawType(ArtBoard.DRAW_SLINE);
                break;
            case R.id.iv_eraser:
                setSelect(5);
                artBoard.setDrawType(ArtBoard.DRAW_ERASER);
                break;
            case R.id.iv_drag:
                setSelect(6);
                break;
            case R.id.iv_size:
                showSizePop();
                break;
            case R.id.iv_annotation:
                if (isSharing) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle(R.string.are_you_sure_to_exit_shared_comments);
                    builder.setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            presenter.stopShare();
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                } else {
                    showMemberPop();
                }
                break;
            default:
                break;
        }
    }

    private void saveDrawPicture() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final EditText edt = new EditText(getContext());
        builder.setTitle(getResources().getString(R.string.please_enter_file_name));
        edt.setHint(getResources().getString(R.string.please_enter_file_name));
        edt.setText(String.valueOf(System.currentTimeMillis()));
        //编辑光标移动到最后
        edt.setSelection(edt.getText().toString().length());
        builder.setView(edt);
        builder.setPositiveButton(getResources().getString(R.string.save_server), (dialog, which) -> {
            String name = edt.getText().toString().trim();
            if (name.isEmpty()) {
                ToastUtils.showShort(R.string.please_enter_file_name);
            } else {
                presenter.savePicture(name, true, artBoard.getCanvasBmp());
                dialog.dismiss();
            }
        });
        builder.setNeutralButton(getResources().getString(R.string.save_local), (dialog, which) -> {
            final String name = edt.getText().toString().trim();
            if (name.isEmpty()) {
                ToastUtils.showShort(R.string.please_enter_file_name);
            } else {
                presenter.savePicture(name, false, artBoard.getCanvasBmp());
                ToastUtils.showLong(getResources().getString(R.string.tip_save_as, Constant.download_dir));
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void showSizePop() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.pop_seekbar, null, false);
        int width = ConvertUtils.dp2px(200);
        int height = ConvertUtils.dp2px(50);
        int xoff = tool_ll.getWidth();
        int yoff = iv_size.getHeight();
        sizePop = PopUtil.createPopupWindowAs(inflate, width, height, iv_size, xoff, -yoff);
        SeekBar pop_seekbar = inflate.findViewById(R.id.pop_seekbar);
        TextView tv_size = inflate.findViewById(R.id.tv_size);
        tv_size.setText(String.valueOf(artBoard.getPaintWidth()));
        pop_seekbar.setProgress(artBoard.getPaintWidth());
        pop_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 1) {
                    seekBar.setProgress(1);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                artBoard.setPaintWidth(progress);
                tv_size.setText(String.valueOf(progress));
            }
        });
    }

    @Override
    public void updateMembers(List<DevMember> devMembers) {
        if (memberPop != null && memberPop.isShowing()) {
            devMemberAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void drawZoomBmp(Bitmap bs2bmp) {
        artBoard.drawZoomBmp(bs2bmp);
    }

    @Override
    public void updateShareStatus() {
        //如果在共享中则启用停止功能,反之启用发起功能
        iv_annotation.setSelected(isSharing);
    }

    @Override
    public void setCanvasSize(int maxX, int maxY) {
        artBoard.setCanvasSize(maxX, maxY);
    }

    @Override
    public void drawPath(Path path, Paint paint) {
        artBoard.drawPath(path, paint);
    }

    @Override
    public void invalidate() {
        artBoard.invalidate();
    }

    @Override
    public void funDraw(Paint paint, int height, int canSee, int fx, int fy, String text) {
        artBoard.funDraw(paint, height, canSee, fx, fy, text);
    }

    @Override
    public void drawText(String ptext, float lx, float ly, Paint paint) {
        artBoard.drawText(ptext, lx, ly, paint);
    }

    @Override
    public void initCanvas() {
        artBoard.initCanvas();
    }

    @Override
    public void drawAgain(List<ArtBoard.DrawPath> pathList) {
        artBoard.drawAgain(pathList);
    }

    private void showMemberPop() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.pop_dev_member, null, false);
        View meet_fl = getActivity().findViewById(R.id.meet_fl);
        View meet_left_ll = getActivity().findViewById(R.id.meet_left_ll);
        int width = meet_fl.getWidth();
        int height = meet_fl.getHeight();
        int width1 = meet_left_ll.getWidth();
        int height1 = meet_left_ll.getHeight();
        LogUtils.i(TAG, "showDetailsPop 宽高=" + width + "," + height + ",功能菜单宽高=" + width1 + "," + height1);
        memberPop = PopUtil.createPopupWindow(inflate, width * 2 / 3, height * 2 / 3, iv_annotation, Gravity.CENTER, width1 / 2, 0);
        CheckBox cb_all = inflate.findViewById(R.id.cb_all);
        RecyclerView rv_member = inflate.findViewById(R.id.rv_member);
        devMemberAdapter = new DevMemberAdapter(R.layout.item_dev_member, presenter.devMembers);
        rv_member.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        rv_member.setAdapter(devMemberAdapter);
        devMemberAdapter.setOnItemClickListener((adapter, view, position) -> {
            devMemberAdapter.setCheck(presenter.devMembers.get(position).getMemberDetailInfo().getPersonid());
            cb_all.setChecked(devMemberAdapter.isCheckAll());
        });
        cb_all.setOnClickListener(v -> {
            boolean checked = cb_all.isChecked();
            cb_all.setChecked(checked);
            devMemberAdapter.setCheckAll(checked);
        });
        inflate.findViewById(R.id.btn_launch).setOnClickListener(v -> {
            List<Integer> memberIds = devMemberAdapter.getCheckMemberIds();
            if (memberIds.isEmpty()) {
                ToastUtils.showShort(R.string.please_choose_member);
                return;
            }
            // 发起共享批注  强制：Pb_MEETPOTIL_FLAG_FORCEOPEN  Pb_MEETPOTIL_FLAG_REQUESTOPEN
            if (mSrcmemid == 0) {
                //当前已经在同屏中,并且自己是发起人;则操作ID不需要重新获取
                launchSrcwbid = System.currentTimeMillis();
                launchSrcmemid = GlobalValue.localMemberId;
            } else {
                launchSrcwbid = mSrcwbid;
                launchSrcmemid = mSrcmemid;
            }
            LogUtil.d(TAG, "发起同屏：" + memberIds.toString());
            jni.coerceStartWhiteBoard(InterfaceMacro.Pb_MeetPostilOperType.Pb_MEETPOTIL_FLAG_REQUESTOPEN.getNumber(),
                    GlobalValue.localMemberName, GlobalValue.localMemberId,
                    launchSrcmemid, launchSrcwbid, memberIds);
            if (isAddScreenShot) {//从截图批注端启动的画板
                isAddScreenShot = false;
                addScreenShot();
            }
            memberPop.dismiss();
        });
        inflate.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            memberPop.dismiss();
        });
    }

    private void addScreenShot() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FabService.screenShotBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] bytes = baos.toByteArray();
            ByteString picdata = ByteString.copyFrom(bytes);
            long time = System.currentTimeMillis();
            int operid = (int) (time / 10);
            localOperids.add(operid);
            ArtBoard.DrawPath drawPath = new ArtBoard.DrawPath();
            drawPath.picdata = picdata;
            LocalPathList.add(drawPath);
            LocalSharingPathList.add(drawPath);
            LogUtil.d(TAG, "发起同屏时添加截图: LocalPathList.size() : " + LocalPathList.size());
            jni.addPicture(operid, GlobalValue.localMemberId, launchSrcmemid, launchSrcwbid, time,
                    InterfaceMacro.Pb_MeetPostilFigureType.Pb_WB_FIGURETYPE_PICTURE.getNumber(), 0, 0, picdata);
        } finally {
            if (!FabService.screenShotBitmap.isRecycled()) FabService.screenShotBitmap.recycle();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PICTURE) {
            File file = UriUtils.uri2File(data.getData());
            if (file != null) {
                LogUtil.i(TAG, "onActivityResult 选择的图片路径=" + file.getAbsolutePath());
                if (FileUtil.isPicture(file.getName()) && !file.getName().endsWith(".gif")) {
                    // 执行操作
                    Bitmap dstbmp = BitmapFactory.decodeFile(file.getAbsolutePath());
                    //将图片绘制到画板中
                    Bitmap bitmap = artBoard.drawZoomBmp(dstbmp);
                    //保存图片信息
                    ArtBoard.DrawPath drawPath = new ArtBoard.DrawPath();
                    drawPath.picdata = bmp2bs(bitmap);
                    ArtBoard.LocalPathList.add(drawPath);
                    if (isSharing) {
                        long time = System.currentTimeMillis();
                        int operid = (int) (time / 10);
                        localOperids.add(operid);
                        LocalSharingPathList.add(drawPath);
                        jni.addPicture(operid, GlobalValue.localMemberId, mSrcmemid, mSrcwbid, time,
                                InterfaceMacro.Pb_MeetPostilFigureType.Pb_WB_FIGURETYPE_PICTURE.getNumber(),
                                0, 0, bmp2bs(bitmap));
                    }
                } else {
                    ToastUtils.showShort(R.string.please_choose_picture_file);
                }
            }
        }
    }
}
