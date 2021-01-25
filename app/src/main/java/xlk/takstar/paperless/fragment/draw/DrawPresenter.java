package xlk.takstar.paperless.fragment.draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mogujie.tt.protobuf.InterfaceDevice;
import com.mogujie.tt.protobuf.InterfaceMacro;
import com.mogujie.tt.protobuf.InterfaceMember;
import com.mogujie.tt.protobuf.InterfaceWhiteboard;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AlertDialog;
import xlk.takstar.paperless.App;
import xlk.takstar.paperless.R;
import xlk.takstar.paperless.base.BasePresenter;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.model.EventMessage;
import xlk.takstar.paperless.model.EventType;
import xlk.takstar.paperless.model.GlobalValue;
import xlk.takstar.paperless.model.bean.DevMember;
import xlk.takstar.paperless.service.fab.FabService;
import xlk.takstar.paperless.ui.ArtBoard;
import xlk.takstar.paperless.util.FileUtil;
import xlk.takstar.paperless.util.LogUtil;

import static xlk.takstar.paperless.fragment.draw.DrawFragment.disposePicOpermemberid;
import static xlk.takstar.paperless.fragment.draw.DrawFragment.disposePicSrcmemid;
import static xlk.takstar.paperless.fragment.draw.DrawFragment.disposePicSrcwbidd;
import static xlk.takstar.paperless.fragment.draw.DrawFragment.isSharing;
import static xlk.takstar.paperless.fragment.draw.DrawFragment.launchSrcmemid;
import static xlk.takstar.paperless.fragment.draw.DrawFragment.launchSrcwbid;
import static xlk.takstar.paperless.fragment.draw.DrawFragment.mSrcmemid;
import static xlk.takstar.paperless.fragment.draw.DrawFragment.mSrcwbid;
import static xlk.takstar.paperless.fragment.draw.DrawFragment.pathList;
import static xlk.takstar.paperless.fragment.draw.DrawFragment.savePicData;
import static xlk.takstar.paperless.fragment.draw.DrawFragment.tempPicData;
import static xlk.takstar.paperless.fragment.draw.DrawFragment.togetherIDs;
import static xlk.takstar.paperless.fragment.draw.DrawFragment.points;
import static xlk.takstar.paperless.model.Constant.ANNOTATION_FILE_DIRECTORY_ID;
import static xlk.takstar.paperless.ui.ArtBoard.LocalPathList;
import static xlk.takstar.paperless.ui.ArtBoard.artBoardWidth;
import static xlk.takstar.paperless.util.ConvertUtil.bs2bmp;

/**
 * @author Created by xlk on 2020/12/7.
 * @desc
 */
public class DrawPresenter extends BasePresenter<DrawContract.View> implements DrawContract.Presenter {

    private final Context cxt;
    private List<InterfaceMember.pbui_Item_MemberDetailInfo> members = new ArrayList<>();
    public List<DevMember> devMembers = new ArrayList<>();
    private PointF pointF;//收到添加墨迹通知时，保存所有的绘制点(点连成线)

    public DrawPresenter(DrawContract.View view, Context cxt) {
        super(view);
        this.cxt = cxt;
    }

    @Override
    protected void busEvent(EventMessage msg) throws InvalidProtocolBufferException {
        switch (msg.getType()) {
            //会议白板
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_WHITEBOARD_VALUE: {
                byte[] datas = (byte[]) msg.getObjects()[0];
                switch (msg.getMethod()) {
                    case InterfaceMacro.Pb_Method.Pb_METHOD_MEET_INTERFACE_ASK_VALUE://收到打开白板操作
                        openArtBoard(datas);
                        break;
                    case InterfaceMacro.Pb_Method.Pb_METHOD_MEET_INTERFACE_ENTER_VALUE://同意加入通知
                        agreeJoin(datas);
                        break;
                    case InterfaceMacro.Pb_Method.Pb_METHOD_MEET_INTERFACE_REJECT_VALUE://拒绝加入通知
                        rejectJoin(datas);
                        break;
                    case InterfaceMacro.Pb_Method.Pb_METHOD_MEET_INTERFACE_EXIT_VALUE://参会人员退出白板通知
                        exitShareInform(datas);
                        break;
                    case InterfaceMacro.Pb_Method.Pb_METHOD_MEET_INTERFACE_ADDRECT_VALUE://添加矩形、直线、圆形通知
                        addLineInform(datas);
                        break;
                    case InterfaceMacro.Pb_Method.Pb_METHOD_MEET_INTERFACE_ADDINK_VALUE://添加墨迹通知
                        addInkInform(datas);
                        break;
                    case InterfaceMacro.Pb_Method.Pb_METHOD_MEET_INTERFACE_ADDTEXT_VALUE://添加文本通知
                        addTextInform(datas);
                        break;
                    case InterfaceMacro.Pb_Method.Pb_METHOD_MEET_INTERFACE_DEL_VALUE://白板删除记录通知
                        delInform(datas, 1);
                        break;
                    case InterfaceMacro.Pb_Method.Pb_METHOD_MEET_INTERFACE_CLEAR_VALUE://白板清空记录通知
                        delInform(datas, 2);
                        break;
                }
                break;
            }
            //参会人员变更通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEMBER_VALUE:
                LogUtil.d(TAG, "BusEvent -->" + "参会人员变更通知");
                queryMember();
                break;
            //设备寄存器变更通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_DEVICEINFO_VALUE:
                LogUtil.d(TAG, "BusEvent -->" + "设备寄存器变更通知");
                queryMember();
                break;
            //会场设备变更通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_ROOMDEVICE_VALUE:
                LogUtil.d(TAG, "BusEvent -->" + "会场设备变更通知");
                queryMember();
                break;
            //界面状态变更通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_DEVICEMEETSTATUS_VALUE: {
                LogUtil.i(TAG, "busEvent 界面状态变更通知");
                queryMember();
                break;
            }
            case EventType.BUS_SHARE_PIC: {
                InterfaceWhiteboard.pbui_Item_MeetWBPictureDetail object = (InterfaceWhiteboard.pbui_Item_MeetWBPictureDetail) msg.getObjects()[0];
                addPicInform(object);
                break;
            }
            case EventType.BUS_SCREEN_SHOT://屏幕截图
                LogUtil.d(TAG, "BusEvent -->" + "绘制屏幕截图");
                mView.drawZoomBmp(FabService.screenShotBitmap);
                break;
            default:
                break;
        }
    }

    /**
     * 白板删除和清空
     *
     * @param datas
     * @param type  =1 撤销，=2 清空
     */
    private void delInform(byte[] datas, int type) throws InvalidProtocolBufferException {
        InterfaceWhiteboard.pbui_Type_MeetClearWhiteBoard object = InterfaceWhiteboard.pbui_Type_MeetClearWhiteBoard.parseFrom(datas);
        int operid = object.getOperid();
        int opermemberid = object.getOpermemberid();
        long srcwbid = object.getSrcwbid();
        if (togetherIDs.contains(opermemberid)) {
            LogUtil.e(TAG, "delInform :  白板删除记录通知EventBus --->>> ");
            //1.先清空画板
            mView.initCanvas();
            if (type == 1) { //删除
                //2.删除指定的路径
                for (int i = 0; i < pathList.size(); i++) {
                    if (pathList.get(i).operid == operid && pathList.get(i).opermemberid == opermemberid
                            && pathList.get(i).srcwbid == srcwbid) {
                        LogUtil.e(TAG, "delInform :  确认过眼神 --> ");
                        pathList.remove(i);
                        i--;
                    }
                }
            } else if (type == 2) {//清空
                //遍历删除多个，for循环不能删除多个，因为pathList删除后长度会改变，而 i 作为索引一直在增加
                Iterator<ArtBoard.DrawPath> iterator = pathList.iterator();
                while (iterator.hasNext()) {
                    if (iterator.next().opermemberid == opermemberid /*&& iterator.next().srcwbid == srcwbid*/) {
                        LogUtil.d(TAG, "delInform: 删除全部..");
                        iterator.remove();
                    }
                }
            }
            //删除后重新绘制不需要删除的
            mView.drawAgain(pathList);
            //因为清空了画板，所以自己绘制的要重新再绘制
            mView.drawAgain(LocalPathList);
        }
    }

    private void addPicInform(InterfaceWhiteboard.pbui_Item_MeetWBPictureDetail object) {
        int operid = object.getOperid();
        int srcmemid = object.getSrcmemid();
        long srcwbid = object.getSrcwbid();
        ByteString rPicData = object.getPicdata();
        int opermemberid = object.getOpermemberid();
        if (object.getSrcmemid() == mSrcmemid && object.getSrcwbid() == mSrcwbid) {
            //自己不是发起人的时候,每次收到绘画通知都要判断是不是同一个发起人和白板标识
            //并且集合中没有这一号人,将其添加进集合中
            if (!togetherIDs.contains(opermemberid))
                togetherIDs.add(opermemberid);
        }
        if (togetherIDs.contains(opermemberid)) {
            Bitmap bitmap = bs2bmp(rPicData);
            if (bitmap == null) {
                LogUtil.e(TAG, "addPicInform 数组转bitmap得到空");
                return;
            }
            mView.drawZoomBmp(bitmap);
            /** **** **  保存  ** **** **/
            ArtBoard.DrawPath drawPath = new ArtBoard.DrawPath();
            drawPath.operid = operid;
            drawPath.srcwbid = srcwbid;
            drawPath.srcmemid = srcmemid;
            drawPath.opermemberid = opermemberid;
            drawPath.picdata = rPicData;
            //将路径保存到共享中绘画信息
            pathList.add(drawPath);
        }
    }

    private void addTextInform(byte[] datas) throws InvalidProtocolBufferException {
        InterfaceWhiteboard.pbui_Item_MeetWBTextDetail object = InterfaceWhiteboard.pbui_Item_MeetWBTextDetail.parseFrom(datas);
        int operid = object.getOperid();
        int opermemberid = object.getOpermemberid();
        int srcmemid = object.getSrcmemid();
        long srcwbid = object.getSrcwbid();
        long utcstamp = object.getUtcstamp();
        int figuretype = object.getFiguretype();
        int fontsize = object.getFontsize();
        int fontflag = object.getFontflag();// Normal/Bold/Italic/Bold Italic
        LogUtil.e(TAG, "DrawBoardActivity.receiveAddText 394行:  文本大小 --->>> " + fontsize);
        int argb = object.getArgb();
        String fontname = object.getFontname().toStringUtf8();// 宋体/黑体...
        float lx = object.getLx();  // 获取到文本起点x
        float ly = object.getLy();  // 获取到文本起点y
        String ptext = object.getPtext().toStringUtf8();  // 文本内容
        LogUtil.e(TAG, "DrawBoardActivity.receiveAddText :  收到的文本内容 --->>> " + ptext);
        if (object.getSrcmemid() == mSrcmemid && object.getSrcwbid() == mSrcwbid) {
            //自己不是发起人的时候,每次收到绘画通知都要判断是不是同一个发起人和白板标识
            //如果共享集合中没有这一号人,将其添加进集合中
            if (!togetherIDs.contains(opermemberid)) {
                togetherIDs.add(opermemberid);
            }
        }
        if (togetherIDs.contains(opermemberid)) {//说明当前这个是同一个发起人
            if (figuretype == InterfaceMacro.Pb_MeetPostilFigureType.Pb_WB_FIGURETYPE_FREETEXT.getNumber()) {
                if (lx < 0) {
                    lx = 0;
                }
                if (ly < 0) {
                    ly = 0;
                }
//                if (lx > artBoardWidth) {
//                    lx = artBoardWidth;
//                }
//                if (ly > artBoardHeight) {
//                    ly = artBoardHeight;
//                }
                int x = (int) (lx);
                int y = (int) (ly);
                Paint newPaint = getNewPaint(3, argb);
                if (fontsize < 30) fontsize = 30;//设置一个最小值,不然文字会太小
                newPaint.setTextSize(fontsize);
                newPaint.setFlags(fontflag);
                newPaint.setStyle(Paint.Style.FILL_AND_STROKE);

                Rect rect = new Rect();
                newPaint.getTextBounds(ptext, 0, ptext.length(), rect);
                int width = rect.width();//所有文本的宽度
                int height = rect.height();//文本的高度（用于换行显示）
                int w = (int) (lx + width);
                int h = (int) (lx + height);
                mView.setCanvasSize(w, h);//根据需要的文本宽高进行增加画板宽高

                int remainWidth = artBoardWidth - x;//可容许显示文本的宽度
                int ilka = width / ptext.length();//每个文本的宽度
                int canSee = remainWidth / ilka;//可以显示的文本个数

                if (remainWidth < width) {// 小于所有文本的宽度（不够显示）
                    mView.funDraw(newPaint, height, canSee - 1, x, y, ptext);
                } else {//足够空间显示则直接画出来
                    mView.drawText(ptext, lx, ly, newPaint);
                }
                mView.invalidate();
                ArtBoard.DrawPath drawPath = new ArtBoard.DrawPath();
                drawPath.opermemberid = opermemberid;
                drawPath.operid = operid;
                drawPath.srcwbid = srcwbid;
                drawPath.srcmemid = srcmemid;
                drawPath.paint = newPaint;
                drawPath.height = height;
                drawPath.text = ptext;
                drawPath.pointF = new PointF(x, y);
                drawPath.cansee = canSee;
                drawPath.lw = remainWidth;
                drawPath.rw = width;
                pathList.add(drawPath);
            }
        }
    }

    private void addInkInform(byte[] datas) throws InvalidProtocolBufferException {
        InterfaceWhiteboard.pbui_Type_MeetWhiteBoardInkItem object = InterfaceWhiteboard.pbui_Type_MeetWhiteBoardInkItem.parseFrom(datas);
        int operid = object.getOperid();
        //当前发送端的人员ID,用来判断是否是正在一起同屏的对象发的墨迹操作
        int opermemberid = object.getOpermemberid();
        int figuretype = object.getFiguretype();
        int srcmemid = object.getSrcmemid();
        long srcwbid = object.getSrcwbid();
        int linesize = object.getLinesize();
        int argb = object.getArgb();
        List<Float> pinklistList = object.getPinklistList();
        LogUtil.e(TAG, "DrawBoardActivity.receiveAddInk :  收到添加墨迹操作EventBus --->>> 白板标识=" + srcwbid + ",  发起人ID=" + srcmemid + "; 对比=" + mSrcwbid + "," + mSrcmemid);
        if (pinklistList.size() > 0) {
            if (srcmemid == mSrcmemid && srcwbid == mSrcwbid) {
                //自己不是发起人的时候,每次收到绘画通知都要判断是不是同一个发起人和白板标识
                //并且集合中没有这一号人,将其添加进集合中
                if (!togetherIDs.contains(opermemberid))
                    togetherIDs.add(opermemberid);
            }
            if (!isSharing) {
                LogUtil.e(TAG, "DrawBoardActivity.receiveAddInk :  不在共享中 --> ");
                return;
            }
            if (togetherIDs.contains(opermemberid)) {
                LogUtil.e(TAG, "DrawBoardActivity.receiveAddInk 266行:   接收到的xy个数--->>> " + object.getPinklistCount() + " , " + pinklistList.size());
                points.clear();
                for (int i = 0; i < pinklistList.size(); i++) {
                    Float aFloat = pinklistList.get(i);
                    if (i % 2 == 0) {
                        pointF = new PointF();
                        pointF.x = aFloat;
                    } else {
                        pointF.y = aFloat;
                        mView.setCanvasSize((int) pointF.x, (int) pointF.y);
                        points.add(pointF);
                    }
                }
                //新建 paint 和 path
                Paint newPaint = getNewPaint(linesize, argb);
                Path allInkPath = new Path();
                PointF p1 = new PointF();
                PointF p2 = new PointF();
                //绘画
                float sx, sy;
                if (figuretype == InterfaceMacro.Pb_MeetPostilFigureType.Pb_WB_FIGURETYPE_INK.getNumber()) {
                    p1.x = points.get(0).x;
                    p1.y = points.get(0).y;
                    Path newPath = new Path();
                    sx = p1.x;
                    sy = p1.y;
                    newPath.moveTo(p1.x, p1.y);
                    for (int i = 1; i < points.size() - 1; i++) {
                        p2.x = points.get(i).x;
                        p2.y = points.get(i).y;
                        float dx = Math.abs(p2.x - sx);
                        float dy = Math.abs(p2.y - sy);
                        if (dx >= 3 || dy >= 3) {
                            float cx = (p2.x + sx) / 2;
                            float cy = (p2.y + sy) / 2;
                            newPath.quadTo(sx, sy, cx, cy);
                        }
                        mView.drawPath(newPath, newPaint);
                        mView.invalidate();
                        sx = p2.x;
                        sy = p2.y;
                        allInkPath.addPath(newPath);
                    }
                    ArtBoard.DrawPath drawPath = new ArtBoard.DrawPath();
                    drawPath.paint = newPaint;
                    drawPath.path = allInkPath;
                    drawPath.operid = operid;
                    drawPath.srcwbid = srcwbid;
                    drawPath.srcmemid = srcmemid;
                    drawPath.opermemberid = opermemberid;
                    //将路径保存到共享中绘画信息
                    pathList.add(drawPath);
                    points.clear();
                }
            }
        }
    }

    private void addLineInform(byte[] datas) throws InvalidProtocolBufferException {
        InterfaceWhiteboard.pbui_Item_MeetWBRectDetail object = InterfaceWhiteboard.pbui_Item_MeetWBRectDetail.parseFrom(datas);
        int operid = object.getOperid();
        int opermemberid = object.getOpermemberid();
        int srcmemid2 = object.getSrcmemid();
        long srcwbid2 = object.getSrcwbid();
        long utcstamp = object.getUtcstamp();
        int figuretype = object.getFiguretype();
        int linesize = object.getLinesize();
        int color = object.getArgb();
        List<Float> ptList = object.getPtList();
        if (object.getSrcmemid() == mSrcmemid && object.getSrcwbid() == mSrcwbid) {
            //自己不是发起人的时候,每次收到绘画通知都要判断是不是同一个发起人和白板标识
            //并且集合中没有这一号人,将其添加进集合中
            if (!togetherIDs.contains(opermemberid)) {
                togetherIDs.add(opermemberid);
            }
        }

        if (togetherIDs.contains(opermemberid)) {
            Paint newPaint = getNewPaint(linesize, color);
            Path newPath = new Path();
            float[] allPoint = getFloats(ptList);
            float maxX = Math.max(allPoint[0], allPoint[2]);
            float maxY = Math.max(allPoint[1], allPoint[3]);
            mView.setCanvasSize((int) maxX, (int) maxY);
            LogUtil.d(TAG, "receiveAddLine: 收到的四个点: 长度:" + allPoint.length + ",起点: "
                    + allPoint[0] + " , " + allPoint[1] + ",  终点:  " + allPoint[2] + " , " + allPoint[3]);
            //根据图形类型绘制
            if (figuretype == InterfaceMacro.Pb_MeetPostilFigureType.Pb_WB_FIGURETYPE_RECTANGLE.getNumber()) {
                //矩形
                newPath.addRect(allPoint[0], allPoint[1], allPoint[2], allPoint[3], Path.Direction.CW);
            } else if (figuretype == InterfaceMacro.Pb_MeetPostilFigureType.Pb_WB_FIGURETYPE_LINE.getNumber()) {
                //直线
                newPath.moveTo(allPoint[0], allPoint[1]);
                newPath.lineTo(allPoint[2], allPoint[3]);
            } else if (figuretype == InterfaceMacro.Pb_MeetPostilFigureType.Pb_WB_FIGURETYPE_ELLIPSE.getNumber()) {
                //圆
                newPath.addOval(allPoint[0], allPoint[1], allPoint[2], allPoint[3], Path.Direction.CW);
            }
            mView.drawPath(newPath, newPaint);
            mView.invalidate();
            ArtBoard.DrawPath drawPath = new ArtBoard.DrawPath();
            drawPath.paint = newPaint;
            drawPath.path = newPath;
            drawPath.operid = operid;
            drawPath.srcwbid = srcwbid2;
            drawPath.srcmemid = srcmemid2;
            drawPath.opermemberid = opermemberid;
            //将路径保存到共享中绘画信息
            pathList.add(drawPath);
        }
    }

    private Paint getNewPaint(int linesize, int color) {
        Paint newPaint = new Paint();
        newPaint.setColor(color);
        newPaint.setStrokeWidth(linesize);
        newPaint.setStyle(Paint.Style.STROKE);// 画笔样式：实线
        PorterDuffXfermode mode2 = new PorterDuffXfermode(
                PorterDuff.Mode.DST_OVER);
        newPaint.setXfermode(null);// 转换模式
        newPaint.setAntiAlias(true);// 抗锯齿
        newPaint.setDither(true);// 防抖动
        newPaint.setStrokeJoin(Paint.Join.ROUND);// 设置线段连接处的样式为圆弧连接
        newPaint.setStrokeCap(Paint.Cap.ROUND);// 设置两端的线帽为圆的
        return newPaint;
    }

    private float[] getFloats(List<Float> ptList) {
        float[] allPoint = new float[4];
        for (int i = 0; i < ptList.size(); i++) {
            Float aFloat = ptList.get(i);
            switch (i) {
                case 0:
                    allPoint[0] = aFloat;
                    break;
                case 1:
                    allPoint[1] = aFloat;
                    break;
                case 2:
                    allPoint[2] = aFloat;
                    break;
                case 3:
                    allPoint[3] = aFloat;
                    break;
            }
        }
        return allPoint;
    }

    private void exitShareInform(byte[] datas) throws InvalidProtocolBufferException {
        InterfaceWhiteboard.pbui_Type_MeetWhiteBoardOper object = InterfaceWhiteboard.pbui_Type_MeetWhiteBoardOper.parseFrom(datas);
        int srcmemid = object.getSrcmemid();
        long srcwbid = object.getSrcwbid();
        int opermemberid = object.getOpermemberid();
        LogUtil.d(TAG, "exitShareInform -->" + "参会人员退出白板通知");
        if (srcmemid == mSrcmemid && srcwbid == mSrcwbid) {//如果是一起同屏的才操作
            if (togetherIDs.contains(opermemberid)) {//集合中有当前这个人
                for (int i = 0; i < togetherIDs.size(); i++) {
                    if (togetherIDs.get(i) == opermemberid) {
                        togetherIDs.remove(i);//删除
                        i--;
                        whoTip(opermemberid, cxt.getString(R.string.tip_exit_the_shared));
                        if (togetherIDs.size() == 0 && mSrcmemid == GlobalValue.localMemberId) {
                            //自己发起的时才退出,因为如果是本人是发起人,
                            //可能还有其他人在共享中但是没有操作,所以你只是没有添加到集合中而已
                            LogUtil.e(TAG, "exitShareInform :  没有人在共享了,退出共享 --> ");
                            stopShare();
                        }
                    }
                }
            }
        }
    }

    private void rejectJoin(byte[] datas) throws InvalidProtocolBufferException {
        InterfaceWhiteboard.pbui_Type_MeetWhiteBoardOper object1 = InterfaceWhiteboard.pbui_Type_MeetWhiteBoardOper.parseFrom(datas);
        long srcwbid1 = object1.getSrcwbid();
        int srcmemid1 = object1.getSrcmemid();
        if (mSrcmemid == 0) {
            if (srcmemid1 == launchSrcwbid && srcwbid1 == launchSrcwbid)
                whoTip(object1.getOpermemberid(), cxt.getString(R.string.tip_repulse_join));
        } else {
            if (srcwbid1 == mSrcwbid && srcmemid1 == mSrcmemid) //发起人的白板标识和人员ID一致
                whoTip(object1.getOpermemberid(), cxt.getString(R.string.tip_repulse_join));
        }
    }

    private void agreeJoin(byte[] datas) throws InvalidProtocolBufferException {
        InterfaceWhiteboard.pbui_Type_MeetWhiteBoardOper agreeData = InterfaceWhiteboard.pbui_Type_MeetWhiteBoardOper.parseFrom(datas);
        int opermemberid = agreeData.getOpermemberid();
        long srcwbid = agreeData.getSrcwbid();
        int srcmemid = agreeData.getSrcmemid();
        if (srcwbid == launchSrcwbid && srcmemid == launchSrcmemid) {
            mSrcmemid = launchSrcmemid;
            mSrcwbid = launchSrcwbid;
            launchSrcmemid = 0;
            launchSrcwbid = 0;
            togetherIDs.clear();
        }
        LogUtil.e(TAG, "agreeJoin : 收到同意加入通知  srcwbid=" + srcwbid + ",mSrcwbid=" + mSrcwbid + ",mSrcmemid=" + mSrcmemid + ",srcmemid=" + srcmemid);
        if (srcwbid == mSrcwbid && srcmemid == mSrcmemid) {//发起人的白板标识和人员ID一致
            togetherIDs.add(opermemberid);//添加到正在一起共享的人员ID集合中
            LogUtil.e(TAG, "agreeJoin ID: " + opermemberid + " 同意加入,  大小: " + togetherIDs.size());
            whoTip(opermemberid, cxt.getString(R.string.tip_join_the_sharing));
            isSharing = true;
            mView.updateShareStatus();
        }
    }

    private void whoTip(int opermemberid, String something) {
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getPersonid() == opermemberid) {
                String name = members.get(i).getName().toStringUtf8();
                ToastUtils.showShort(name + something);
            }
        }
    }

    private void openArtBoard(byte[] datas) throws InvalidProtocolBufferException {
        InterfaceWhiteboard.pbui_Type_MeetStartWhiteBoard object1 = InterfaceWhiteboard.pbui_Type_MeetStartWhiteBoard.parseFrom(datas);
        int operflag = object1.getOperflag();
        String medianame = object1.getMedianame().toStringUtf8();
        disposePicOpermemberid = object1.getOpermemberid();
        disposePicSrcmemid = object1.getSrcmemid();
        disposePicSrcwbidd = object1.getSrcwbid();
        LogUtil.e(TAG, "DrawBoardActivity.receiveOpenWhiteBoard :  收到白板打开操作 --> 人员ID:" + disposePicSrcmemid
                + ",白板标识:" + disposePicSrcwbidd + ", operflag= " + operflag);
        if (operflag == InterfaceMacro.Pb_MeetPostilOperType.Pb_MEETPOTIL_FLAG_FORCEOPEN.getNumber()) {
            LogUtil.e(TAG, "DrawBoardActivity.receiveOpenWhiteBoard 619行:   --->>> 这是强制式打开白板");
            togetherIDs.clear();
            //强制打开白板  直接强制同意加入
            jni.agreeJoin(GlobalValue.localMemberId, disposePicSrcmemid, disposePicSrcwbidd);
            isSharing = true;//如果同意加入就设置已经在共享中
            mView.updateShareStatus();
            mSrcwbid = disposePicSrcwbidd;//发起人的白板标识
            mSrcmemid = disposePicSrcmemid;//设置发起的人员ID
            togetherIDs.add(mSrcmemid);//添加到同屏人员集合中
            if (tempPicData != null) {
                savePicData = tempPicData;
                tempPicData = null;
                mView.drawZoomBmp(bs2bmp(savePicData));
            }
        } else if (operflag == InterfaceMacro.Pb_MeetPostilOperType.Pb_MEETPOTIL_FLAG_REQUESTOPEN.getNumber()) {
            //询问打开白板
            whetherOpen(disposePicSrcmemid, disposePicSrcwbidd, medianame, disposePicOpermemberid);
        }
    }

    private void whetherOpen(final int srcmemid, final long srcwbId, String mediaName, final int opermemberid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(cxt);
        builder.setTitle(cxt.getString(R.string.title_whether_agree_join, mediaName));
        builder.setPositiveButton(cxt.getString(R.string.agree), (dialog, which) -> {
            togetherIDs.clear();
            //同意加入
            jni.agreeJoin(GlobalValue.localMemberId, srcmemid, srcwbId);
            isSharing = true;//如果同意加入就设置已经在共享中
            mView.updateShareStatus();
            mSrcmemid = srcmemid;//设置发起的人员ID
            mSrcwbid = srcwbId;//设置发起人的白板标识
            togetherIDs.add(mSrcmemid);//添加到同屏人员集合中
            LogUtil.e(TAG, "whetherOpen :   --> 同意加入 添加: 人员ID为 " + srcmemid + " 的参会人后集合大小:" + togetherIDs.size());
            if (tempPicData != null) {
                savePicData = tempPicData;
                tempPicData = null;
                mView.drawZoomBmp(bs2bmp(savePicData));
                /** **** **  保存  ** **** **/
                ArtBoard.DrawPath drawPath = new ArtBoard.DrawPath();
                drawPath.operid = GlobalValue.operid;
                GlobalValue.operid = 0;
                drawPath.srcwbid = srcwbId;
                drawPath.srcmemid = srcmemid;
                drawPath.opermemberid = opermemberid;
                drawPath.picdata = savePicData;
                //将路径保存到共享中绘画信息
                pathList.add(drawPath);
            }
            dialog.dismiss();
        });
        builder.setNegativeButton(cxt.getString(R.string.reject), (dialog, which) -> {
            jni.rejectJoin(GlobalValue.localMemberId, srcmemid, srcwbId);
            dialog.dismiss();
        });
        builder.create().show();
    }

    @Override
    public void queryMember() {
        InterfaceMember.pbui_Type_MemberDetailInfo pbui_type_memberDetailInfo = jni.queryMember();
        members.clear();
        if (pbui_type_memberDetailInfo != null) {
            members.addAll(pbui_type_memberDetailInfo.getItemList());
        }
        queryDevice();
    }

    @Override
    public void queryDevice() {
        InterfaceDevice.pbui_Type_DeviceDetailInfo pbui_type_deviceDetailInfo = jni.queryDeviceInfo();
        devMembers.clear();
        if (pbui_type_deviceDetailInfo != null) {
            List<InterfaceDevice.pbui_Item_DeviceDetailInfo> pdevList = pbui_type_deviceDetailInfo.getPdevList();
            for (int i = 0; i < pdevList.size(); i++) {
                InterfaceDevice.pbui_Item_DeviceDetailInfo dev = pdevList.get(i);
                if (dev.getNetstate() == 1 && dev.getFacestate() == 1) {
                    for (int j = 0; j < members.size(); j++) {
                        InterfaceMember.pbui_Item_MemberDetailInfo member = members.get(j);
                        if (member.getPersonid() == dev.getMemberid()
                                && dev.getDevcieid() != GlobalValue.localDeviceId) {
                            devMembers.add(new DevMember(dev, member));
                        }
                    }
                }
            }
        }
        LogUtil.i(TAG, "devMembers数量=" + devMembers.size());
        mView.updateMembers(devMembers);
    }

    public void savePicture(String fileName, boolean isUpload, Bitmap bitmap) {
        //重新创建一个，画板获取的bitmap对象会自动回收掉
        Bitmap bitmap1 = Bitmap.createBitmap(bitmap);
        FileUtils.createOrExistsDir(Constant.download_dir);
        File uploadPicFile = new File(Constant.download_dir, fileName + ".png");
        FileUtil.saveBitmap(bitmap1, uploadPicFile);
        Timer tupload = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (isUpload) {
                    /** **** **  上传到服务器  ** **** **/
                    String path = uploadPicFile.getPath();
                    String fileEnd = path.substring(path.lastIndexOf(".") + 1).toLowerCase();
                    jni.uploadFile(InterfaceMacro.Pb_Upload_Flag.Pb_MEET_UPLOADFLAG_ONLYENDCALLBACK.getNumber(),
                            ANNOTATION_FILE_DIRECTORY_ID, 0, fileName + "." + fileEnd, path, 0, Constant.UPLOAD_DRAW_PIC);
                }
            }
        };
        tupload.schedule(timerTask, 2000);//  5秒后运行上传
    }

    //停止多人批注
    public void stopShare() {
        if (isSharing) {
            togetherIDs.clear();
            List<Integer> alluserid = new ArrayList<>();
            alluserid.add(GlobalValue.localMemberId);
            jni.broadcastStopWhiteBoard(InterfaceMacro.Pb_MeetPostilOperType.Pb_MEETPOTIL_FLAG_EXIT.getNumber(),
                    App.appContext.getString(R.string.exit_white_board), GlobalValue.localMemberId, mSrcmemid, mSrcwbid, alluserid);
            isSharing = false;
            mView.updateShareStatus();
            mSrcwbid = 0;
            ToastUtils.showShort(R.string.you_have_exit_annotation);
        }
    }
}
