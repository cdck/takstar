package xlk.takstar.paperless.video;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.os.Environment;
import android.util.Range;
import android.view.Surface;

import com.blankj.utilcode.util.LogUtils;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mogujie.tt.protobuf.InterfaceBase;
import com.mogujie.tt.protobuf.InterfaceDevice;
import com.mogujie.tt.protobuf.InterfaceMacro;
import com.mogujie.tt.protobuf.InterfaceMember;
import com.mogujie.tt.protobuf.InterfacePlaymedia;
import com.mogujie.tt.protobuf.InterfaceStop;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import xlk.takstar.paperless.App;
import xlk.takstar.paperless.base.BasePresenter;
import xlk.takstar.paperless.model.Constant;
import xlk.takstar.paperless.model.EventMessage;
import xlk.takstar.paperless.model.EventType;
import xlk.takstar.paperless.model.GlobalValue;
import xlk.takstar.paperless.model.bean.DevMember;
import xlk.takstar.paperless.model.bean.MediaBean;
import xlk.takstar.paperless.util.DateUtil;
import xlk.takstar.paperless.util.LogUtil;

import static xlk.takstar.paperless.App.read2file;
import static xlk.takstar.paperless.model.Constant.RESOURCE_ID_0;

/**
 * @author Created by xlk on 2020/12/10.
 * @desc
 */
public class VideoPresenter extends BasePresenter<VideoContract.View> implements VideoContract.Presenter {

    private List<InterfaceMember.pbui_Item_MemberDetailInfo> members = new ArrayList<>();
    public List<InterfaceDevice.pbui_Item_DeviceDetailInfo> onLineProjectors = new ArrayList<>();
    public List<DevMember> onLineMember = new ArrayList<>();
    private int mMediaId;
    private int mStatus;
    private int currentPre;
    private long lastPushTime;
    long framepersecond = 80;//估计每秒的播放时间 单位：毫秒
    private int length;
    private String saveMimeType = "";
    private int initW, initH;
    private MediaCodec mediaCodec;
    LinkedBlockingQueue<MediaBean> queue = new LinkedBlockingQueue<>();
    private releaseThread timeThread;
    private boolean isStop;
    private MediaCodec.BufferInfo info;
    private MediaFormat mediaFormat;
    private Surface mSurface;
    private int mValue;
    private boolean isShareing;//是否正在同屏中
    private List<Integer> currentShareIds = new ArrayList<>();
    private int mDeviceId;
    private int mSubId;

    public VideoPresenter(VideoContract.View view) {
        super(view);
    }

    @Override
    public void setDeviceId(int deviceId, int subId, int mediaId) {
        mDeviceId = deviceId;
        mSubId = subId;
        mMediaId = mediaId;
    }

    @Override
    public void queryMember() {
        InterfaceMember.pbui_Type_MemberDetailInfo info = jni.queryMember();
        members.clear();
        if (info != null) {
            members.addAll(info.getItemList());
        }
        queryDevice();
    }

    @Override
    public void queryDevice() {
        InterfaceDevice.pbui_Type_DeviceDetailInfo info = jni.queryDeviceInfo();
        onLineProjectors.clear();
        onLineMember.clear();
        if (info != null) {
            List<InterfaceDevice.pbui_Item_DeviceDetailInfo> pdevList = info.getPdevList();
            for (int i = 0; i < pdevList.size(); i++) {
                InterfaceDevice.pbui_Item_DeviceDetailInfo dev = pdevList.get(i);
                int devcieid = dev.getDevcieid();
                int netstate = dev.getNetstate();
                int facestate = dev.getFacestate();
                int memberid = dev.getMemberid();
                if (devcieid == GlobalValue.localDeviceId) {
                    continue;
                }
                if (netstate == 1) {
                    if (Constant.isThisDevType(InterfaceMacro.Pb_DeviceIDType.Pb_DeviceIDType_MeetProjective_VALUE, devcieid)) {//在线的投影机
                        onLineProjectors.add(dev);
                    } else {
                        if (facestate == 1) {
                            for (int j = 0; j < members.size(); j++) {
                                InterfaceMember.pbui_Item_MemberDetailInfo member = members.get(j);
                                if (member.getPersonid() == memberid) {
                                    onLineMember.add(new DevMember(dev, member));
                                    break;
                                }
                            }
                        }
                    }

                }
            }
        }
        mView.updateRecyclerView();
    }

    @Override
    protected void busEvent(EventMessage msg) throws InvalidProtocolBufferException {
        switch (msg.getType()) {
            case EventType.BUS_MANDATORY_PLAY:
                mView.setCanNotExit();
                break;
            //播放进度通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEDIAPLAYPOSINFO_VALUE: {
                playProgressInform(msg);
                break;
            }
            case EventType.BUS_YUV_DISPLAY: {
                Object[] objs = msg.getObjects();
                int res = (int) objs[0];
                int w = (int) objs[1];
                int h = (int) objs[2];
                byte[] y = (byte[]) objs[3];
                byte[] u = (byte[]) objs[4];
                byte[] v = (byte[]) objs[5];
                mView.updateYuv(w, h, y, u, v);
                break;
            }
            case EventType.BUS_VIDEO_DECODE: {
                Object[] objs = msg.getObjects();
                int iskeyframe = (int) objs[0];
                int res = (int) objs[1];
                int codecid = (int) objs[2];
                int width = (int) objs[3];
                int height = (int) objs[4];
                byte[] packet = (byte[]) objs[5];
                long pts = (long) objs[6];
                byte[] codecdata = (byte[]) objs[7];
                String mimeType = Constant.getMimeType(codecid);
                if (packet != null) {
                    lastPushTime = System.currentTimeMillis();
                    length = packet.length;
                    //LogUtil.d(TAG, "getEventMessage :  mimeType --> " + mimeType + "，宽高：" + width + "," + height + ", pts=" + pts);
                    if (!saveMimeType.equals(mimeType) || initW != width || initH != height || mediaCodec == null) {
                        if (mediaCodec != null) {
                            //调用stop方法使其进入 uninitialzed 状态，这样才可以重新配置MediaCodec
                            mediaCodec.stop();
                        }
                        saveMimeType = mimeType;
                        initCodec(width, height, codecdata);
                    }
                    read2file(packet, codecdata);
                }
                mediaCodecDecode(packet, length, pts, iskeyframe);
                if (timeThread == null && !isStop) {
                    timeThread = new releaseThread();
                    timeThread.start();
                }
                break;
            }
            //停止播放通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_STOPPLAY_VALUE: {
                if (msg.getMethod() == InterfaceMacro.Pb_Method.Pb_METHOD_MEET_INTERFACE_NOTIFY_VALUE) {
                    byte[] o = (byte[]) msg.getObjects()[0];
                    InterfaceStop.pbui_Type_MeetStopPlay stopPlay = InterfaceStop.pbui_Type_MeetStopPlay.parseFrom(o);
                    if (stopPlay.getRes() == 0) {
                        LogUtil.d(TAG, "BusEvent -->" + "停止播放通知");
                        mView.close();
                    }
                }
                break;
            }
            //设备寄存器变更通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_DEVICEINFO_VALUE: {
                LogUtil.d(TAG, "BusEvent -->" + "设备寄存器变更通知");
                queryDevice();
                break;
            }
            //参会人员变更通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_MEMBER_VALUE: {
                LogUtil.d(TAG, "BusEvent -->" + "参会人员变更通知");
                queryMember();
                break;
            }
            //界面状态变更通知
            case InterfaceMacro.Pb_Type.Pb_TYPE_MEET_INTERFACE_DEVICEMEETSTATUS_VALUE: {
                LogUtil.d(TAG, "BusEvent -->" + "界面状态变更通知");
                queryMember();
                break;
            }
            default:
                break;
        }
    }

    /**
     * 初始化解码器
     *
     * @param w         宽
     * @param h         高
     * @param codecdata pps/sps 编码配置数据
     */
    private void initCodec(int w, int h, byte[] codecdata) {
        try {
            mView.setCodecType(1);
            //1.创建了一个编解码器，此时编解码器处于未初始化状态（Uninitialized）
            mediaCodec = MediaCodec.createDecoderByType(saveMimeType);
            /**  宽高要判断是否是解码器所支持的范围  */
            MediaCodecInfo.CodecCapabilities capabilitiesForType = mediaCodec.getCodecInfo().getCapabilitiesForType(saveMimeType);
            MediaCodecInfo.VideoCapabilities videoCapabilities = capabilitiesForType.getVideoCapabilities();
            Range<Integer> supportedWidths = videoCapabilities.getSupportedWidths();
            Integer upper = supportedWidths.getUpper();
            Integer lower = supportedWidths.getLower();
            Range<Integer> supportedHeights = videoCapabilities.getSupportedHeights();
            Integer upper1 = supportedHeights.getUpper();
            Integer lower1 = supportedHeights.getLower();
            initW = w;
            initH = h;
            w = supportedWidths.clamp(w);
            h = supportedHeights.clamp(h);
            LogUtil.e(TAG, "initCodec :   --> " + upper + ", " + lower + " ,,高：" + upper1 + ", " + lower1);
            initMediaFormat(w, h, codecdata);
            boolean formatSupported = capabilitiesForType.isFormatSupported(mediaFormat);
            LogUtil.i(TAG, "initCodec :  是否支持 --> " + formatSupported);
            info = new MediaCodec.BufferInfo();
            try {
                //2.对编解码器进行配置，这将使编解码器转为配置状态（Configured）
                mediaCodec.configure(mediaFormat, mSurface, null, 0);
            } catch (IllegalArgumentException e) {
                String message = e.getMessage();
                String string = e.toString();
                String localizedMessage = e.getLocalizedMessage();
                LogUtils.e(TAG, "initCodec  configure方法异常捕获 --> \n" + message + "\n toString: " + string + "\n localizedMessage: " + localizedMessage);
                e.printStackTrace();
            } catch (MediaCodec.CodecException e) {
                //可能是由于media内容错误、硬件错误、资源枯竭等原因所致
                //可恢复错误（recoverable errors）：如果isRecoverable() 方法返回true,然后就可以调用stop(),configure(...),以及start()方法进行修复
                //短暂错误（transient errors）：如果isTransient()方法返回true,资源短时间内不可用，这个方法可能会在一段时间之后重试。
                //isRecoverable()和isTransient()方法不可能同时都返回true。
                LogUtils.e(TAG, "initCodec :   -->可恢复错误： " + e.isRecoverable() + ",短暂错误：" + e.isTransient());
            }
            //3.调用start()方法使其转入执行状态（Executing）
            mediaCodec.start();
            initMediaMuxer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initMediaFormat(int w, int h, byte[] codecdata) {
        mediaFormat = MediaFormat.createVideoFormat(saveMimeType, w, h);
        mediaFormat.setInteger(MediaFormat.KEY_WIDTH, w);
        mediaFormat.setInteger(MediaFormat.KEY_HEIGHT, h);
        //设置最大输出大小
        mediaFormat.setLong(MediaFormat.KEY_MAX_INPUT_SIZE, w * h);
        if (codecdata != null) {
            mediaFormat.setByteBuffer("csd-0", ByteBuffer.wrap(codecdata));
            mediaFormat.setByteBuffer("csd-1", ByteBuffer.wrap(codecdata));
        }
        boolean formatSupported = mediaCodec.getCodecInfo().getCapabilitiesForType(saveMimeType).isFormatSupported(mediaFormat);
        LogUtils.e(TAG, "formatSupported=" + formatSupported + ",initMediaFormat :   --> " + mediaFormat);
    }

    private BufferedOutputStream outputStream;

    private void initMediaMuxer() throws IOException {
        if (!read2file) return;
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp.mp4");
        if (file.exists()) {
            file.delete();
        }
        outputStream = new BufferedOutputStream(new FileOutputStream(file));
    }

    public void read2file(byte[] outData, byte[] codecdata) {
        if (!read2file) return;
        try {
            outputStream.write(outData, 0, outData.length);
            outputStream.write(codecdata, 0, codecdata.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mediaCodecDecode(byte[] bytes, int size, long pts, int iskeyframe) {
        if (isStop) return;
        if (bytes != null && bytes.length > 0) {
            //把网络接收到的视频数据先加入到队列中
            queue.offer(new MediaBean(bytes, size, pts, iskeyframe));
        } else {
            //bytes为null也不能立马返回，需要处理从视频队列中送数据到解码buffer 和 解码好的视频的显示
        }
        int queuesize = queue.size();
        LogUtil.v(TAG, " mediaCodecDecode -->queuesize: " + queuesize);
        if (queuesize > 500) {
            //当解码速度太慢，导致视频数据积累太多，这种情况下要处理丢包，丢包的策略把前面的关键帧组全部丢包，保留后面两个关键帧组
            //丢帧必须按照I帧P帧连续的丢，否则会造成花屏的情况
            int keyframenum = 0;
            MediaBean poll;
            //先统计队列中有多少个关键帧
            for (int ni = 0; ni < queuesize; ++ni) {
                poll = queue.peek();
                if (poll.getIskeyframe() == 1)
                    keyframenum++;
            }
            for (int ni = 0; ni < queuesize; ++ni) {
                poll = queue.peek();
                if (poll.getIskeyframe() == 1) {
                    keyframenum--;
                    if (keyframenum < 2) {
                        //将该帧放回队列头，因为丢包已经丢了前面的关键帧组，保留后面的两个组
                        break;
                    }
                }
                LogUtil.e(TAG, "mediaCodecDecode 其它帧在此丢掉 -->");
                //其它帧在此丢掉,不处理
                queue.poll();
            }
            //重新计算队列大小
            queuesize = queue.size();
        }
        //判断解码器是否初始化完成
        if (mediaCodec == null)
            return;
        //队列中有视频帧，检查解码队列中是否有空闲可用的buffer，有则取视频帧送进去解码
        if (queuesize > 0) {
            int inputBufferIndex;
            try {
                inputBufferIndex = mediaCodec.dequeueInputBuffer(0);
                if (inputBufferIndex >= 0) {
                    //有空闲可用的解码buffer
                    ByteBuffer byteBuffer = mediaCodec.getInputBuffer(inputBufferIndex);
                    byteBuffer.clear();
                    //将视频队列中的头取出送到解码队列中
                    MediaBean poll = queue.poll();
                    byteBuffer.put(poll.getBytes());
                    LogUtil.i(TAG, "mediaCodecDecode pts=" + poll.getPts());
                    mediaCodec.queueInputBuffer(inputBufferIndex, 0, poll.getSize(), poll.getPts(), 0);
                } else {
                    LogUtil.e(TAG, "mediaCodecDecode dequeueInputBuffer inputBufferIndex=" + inputBufferIndex);
                }
            } catch (IllegalStateException e) {
                //如果解码出错，需要提示用户或者程序自动重新初始化解码
                mediaCodec = null;
                LogUtil.e(TAG, "mediaCodecDecode dequeueInputBuffer " + e);
                return;
            }
        }
        //判断解码显示buffer是否初始化完成
        if (info == null)
            return;
        //判断下一帧的播放时间是否已经到了
//        if (System.currentTimeMillis() - lastplaytime < framepersecond) {
//            return;
//        }
        int index = mediaCodec.dequeueOutputBuffer(info, 0);
//        MediaCodec.INFO_TRY_AGAIN_LATER
//        MediaCodec.INFO_OUTPUT_FORMAT_CHANGED
//        MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED
        if (index >= 0) {
            //返回输出缓冲区，如果索引不是dequeued输出缓冲区，或者编解码器配置了输出面，则返回null
            ByteBuffer outputBuffer = mediaCodec.getOutputBuffer(index);
            if (outputBuffer != null) {
                outputBuffer.position(info.offset);
                outputBuffer.limit(info.offset + info.size);
            }
            LogUtil.v(TAG, "mediaCodecDecode --> dequeueOutputBuffer：查看info：" + index
                    + "\nflags：" + info.flags + ", offset：" + info.offset + ", size：" + info.size
                    + ", presentationTimeUs：" + info.presentationTimeUs);
//            mediaCodec.releaseOutputBuffer(index, info.presentationTimeUs);
            //如果配置编码器时指定了有效的surface，传true将此输出缓冲区显示在surface
            mediaCodec.releaseOutputBuffer(index, true);
        } else {
            LogUtil.e(TAG, "mediaCodecDecode dequeueOutputBuffer值=" + index);
        }
    }

    /**
     * 播放进度通知
     *
     * @param msg
     * @throws InvalidProtocolBufferException
     */
    private void playProgressInform(EventMessage msg) throws InvalidProtocolBufferException {
        byte[] datas = (byte[]) msg.getObjects()[0];
        InterfacePlaymedia.pbui_Type_PlayPosCb playPos = InterfacePlaymedia.pbui_Type_PlayPosCb.parseFrom(datas);
        mMediaId = playPos.getMediaId();
        //当status>0时，为文件ID号
        mStatus = playPos.getStatus();
        currentPre = playPos.getPer();
        int sec = playPos.getSec();
        //只有在播放中才更新进度相关UI
        if (mStatus == 0) {
            byte[] timedata = jni.queryFileProperty(InterfaceMacro.Pb_MeetFilePropertyID.Pb_MEETFILE_PROPERTY_TIME.getNumber(),
                    mMediaId);
            InterfaceBase.pbui_CommonInt32uProperty commonInt32uProperty = InterfaceBase.pbui_CommonInt32uProperty.parseFrom(timedata);
            int propertyval = commonInt32uProperty.getPropertyval();
            mView.updateProgressUi(currentPre, DateUtil.convertTime((long) sec * 1000), DateUtil.convertTime((long) propertyval));

            byte[] fileName = jni.queryFileProperty(InterfaceMacro.Pb_MeetFilePropertyID.Pb_MEETFILE_PROPERTY_NAME.getNumber(),
                    mMediaId);
            InterfaceBase.pbui_CommonTextProperty pbui_commonTextProperty = InterfaceBase.pbui_CommonTextProperty.parseFrom(fileName);
            mView.updateTopTitle(pbui_commonTextProperty.getPropertyval().toStringUtf8());
        }
        if (mStatus == 0 || mStatus == 1) mView.updateAnimator(mStatus);
    }

    @Override
    public void launchScreen(List<Integer> deviceIds, int value) {
        for (int id : deviceIds) {
            if (!currentShareIds.contains(id)) {
                currentShareIds.add(id);
            }
        }
        isShareing = true;
        List<Integer> temps = new ArrayList<>(currentShareIds);
        temps.add(GlobalValue.localDeviceId);
        mValue = value;
        if (mDeviceId == -1) {
            jni.mediaPlayOperate(mMediaId, temps, currentPre, RESOURCE_ID_0, value, 0);
        } else {
            List<Integer> a = new ArrayList<>();
            a.add(0);
            jni.streamPlay(mDeviceId, mSubId, value, a, temps);
        }
    }

    @Override
    public void playOrPause() {
        List<Integer> devIds = new ArrayList<>();
        devIds.add(GlobalValue.localDeviceId);
        if (isShareing) {
            devIds.addAll(currentShareIds);
        }
        if (isPlaying()) {
            jni.setPlayPause(RESOURCE_ID_0, devIds);
        } else {
            jni.setPlayRecover(RESOURCE_ID_0, devIds);
        }
    }

    @Override
    public void stopPlay() {
        if (isShareing) {
            jni.stopResourceOperate(RESOURCE_ID_0, currentShareIds);
            currentShareIds.clear();
            isShareing = false;
        }
    }

    @Override
    public void cutVideoImg() {
        //截图时确保只有播放中才暂停播放
        if (isPlaying()) {
            List<Integer> devIds = new ArrayList<>();
            devIds.add(GlobalValue.localDeviceId);
            jni.setPlayPause(RESOURCE_ID_0, devIds);
        }
    }

    @Override
    public void releaseMediaRes() {
        LogUtil.e(TAG, "releaseMediaRes ");
        isStop = true;
        List<Integer> b = new ArrayList<>();
        b.add(GlobalValue.localDeviceId);
        jni.stopResourceOperate(RESOURCE_ID_0, b);
    }

    @Override
    public void releasePlay() {
        if (timeThread != null) {
            timeThread.interrupt();
            timeThread = null;
        }
        releaseMediaCodec();
    }

    /**
     * 释放资源
     */
    private void releaseMediaCodec() {
        App.threadPool.execute(() -> {
            if (mediaCodec != null) {
                try {
                    LogUtil.e(TAG, "releaseMediaCodec :   --> ");
                    mediaCodec.reset();
                    //调用stop()方法使编解码器返回到未初始化状态（Uninitialized），此时这个编解码器可以再次重新配置
                    mediaCodec.stop();
                    //调用flush()方法使编解码器重新返回到刷新子状态（Flushed）
                    mediaCodec.flush();
                    //使用完编解码器后，你必须调用release()方法释放其资源
                    mediaCodec.release();
                } catch (MediaCodec.CodecException e) {
                    LogUtil.e(TAG, "run :  CodecException --> " + e.getMessage());
                } catch (IllegalStateException e) {
                    LogUtil.e(TAG, "run :  IllegalStateException --> " + e.getMessage());
                } catch (Exception e) {
                    LogUtil.e(TAG, "run :  Exception --> " + e.getMessage());
                }
            }
            mediaCodec = null;
            mediaFormat = null;
        });
    }

    /**
     * 跟去播放状态判断是否播放中
     *
     * @return true 播放中
     */
    private boolean isPlaying() {
        switch (mStatus) {
            //播放中
            case 0:
                return true;
            //暂停
            case 1:
                return false;
            //停止
            case 2:
                return false;
            //恢复
            case 3:
                return true;
            default:
                return true;
        }
    }

    @Override
    public void setPlayPlace(int progress) {
        List<Integer> devIds = new ArrayList<>();
        devIds.add(GlobalValue.localDeviceId);
        if (isShareing) {
            devIds.addAll(currentShareIds);
        }
        jni.setPlayPlace(RESOURCE_ID_0, progress, devIds, mValue, 0);
    }

    @Override
    public String queryDevName(int deivceid) {
        InterfaceDevice.pbui_Item_DeviceDetailInfo info = jni.queryDevInfoById(deivceid);
        if (info != null) {
            return info.getDevname().toStringUtf8();
        }
        return "";
    }

    @Override
    public void setSurface(Surface surface) {
        mSurface = surface;
    }

    class releaseThread extends Thread {

        @Override
        public void run() {
            super.run();
            while (!isStop) {
                //距离上次有数据的时间超过了framepersecond毫秒就进行手动发送
                if (System.currentTimeMillis() - lastPushTime >= framepersecond) {
                    LogUtil.v(TAG, "releaseThread 手动发送空数据 -->");
                    EventBus.getDefault().post(new EventMessage.Builder()
                            .type(EventType.BUS_VIDEO_DECODE)
                            .objects(0, 0, 0, 0, 0, null, 1L, null)
                            .build()
                    );
                    try {
                        sleep(25);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
