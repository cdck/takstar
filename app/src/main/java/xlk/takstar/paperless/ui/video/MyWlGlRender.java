package xlk.takstar.paperless.ui.video;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLException;
import android.opengl.GLSurfaceView;
import android.view.Surface;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import xlk.takstar.paperless.R;
import xlk.takstar.paperless.util.LogUtil;


/**
 * @author xlk
 * @date 2019/6/28
 */
public class MyWlGlRender implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {
    private final String TAG = "MyWlGlRender-->";

    private Context context;
    private FloatBuffer vertexBuffer;
    private final float[] vertexData = {
            1f, 1f, 0f,
            -1f, 1f, 0f,
            1f, -1f, 0f,
            -1f, -1f, 0f
    };

    private FloatBuffer textureBuffer;
    private final float[] textureVertexData = {
            1f, 0f,
            0f, 0f,
            1f, 1f,
            0f, 1f
    };

    /**
     * mediacodec
     */
    private int programId_mediacodec;
    private int aPositionHandle_mediacodec;
    private int textureid_mediacodec;
    private int uTextureSamplerHandle_mediacodec;
    private int aTextureCoordHandle_mediacodec;

    private SurfaceTexture surfaceTexture;
    private Surface surface;

    /**
     * yuv
     */
    private int programId_yuv;
    private int aPositionHandle_yuv;
    private int aTextureCoordHandle_yuv;
    private int sampler_y;
    private int sampler_u;
    private int sampler_v;
    private int[] textureid_yuv;

    int w;
    int h;

    Buffer y;
    Buffer u;
    Buffer v;

    /**
     * release
     */
    private int programId_stop;
    private int aPositionHandle_stop;
    private int aTextureCoordHandle_stop;

    int codecType = -1;
    private boolean cutimg = false;
    private int sWidth = 0;
    private int sHeight = 0;

    private WlOnGlSurfaceViewOncreateListener wlOnGlSurfaceViewOncreateListener;
    private WlOnRenderRefreshListener wlOnRenderRefreshListener;

    public MyWlGlRender(Context context) {
        this.context = context;
        vertexBuffer = ByteBuffer.allocateDirect(vertexData.length * 4)//分配新的直接字节缓冲区
                .order(ByteOrder.nativeOrder())//修改此缓冲区的字节顺序
                .asFloatBuffer()//创建此字节缓冲区的视图，作为 float 缓冲区
                .put(vertexData);//将4个包含给定float值的字节按照当前的字节顺序写入到此缓冲区的当前位置，然后然后将该位置增加 4。
        /*
         * 设置此缓冲区的位置。如果标记已定义且大于新的位置，则丢弃该标记
         * 新位置值；必须为 非负 且 不大于当前限制
         */
        vertexBuffer.position(0);

        textureBuffer = ByteBuffer.allocateDirect(textureVertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(textureVertexData);
        textureBuffer.position(0);
    }

    void setFrameData(int w, int h, byte[] by, byte[] bu, byte[] bv) {
        this.w = w;
        this.h = h;
        this.y = ByteBuffer.wrap(by);
        this.u = ByteBuffer.wrap(bu);
        this.v = ByteBuffer.wrap(bv);
    }

    void setCodecType(int codecType) {
        this.codecType = codecType;
        if (codecType == 2) {
            LogUtil.v(TAG, "清空屏幕--");
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
            GLES20.glClearColor(0f, 0f, 0f, 1f);
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        LogUtil.v(TAG, "onSurfaceCreated :   --> ");
        initMediacodecShader();
        initYuvShader();
        initStop();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        LogUtil.v(TAG, "onSurfaceChanged :   --> " + "onSurfaceChanged, width:" + width + ",height :" + height);
        sWidth = width;
        sHeight = height;
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
//        LogUtil.v(TAG, "onDrawFrame --> codecType= " + codecType);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glClearColor(0f, 0f, 0f, 1f);
        if (codecType == 1) {
            renderMediacodec();
        } else if (codecType == 0) {
            renderYuv();
        } else {
            renderStop();
        }
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        if (cutimg) {
            cutimg = false;
            Bitmap bitmap = cutBitmap(sWidth, sHeight);
            if (wlOnGlSurfaceViewOncreateListener != null) {
                wlOnGlSurfaceViewOncreateListener.onCutVideoImg(bitmap);
            }
        }
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        if (wlOnRenderRefreshListener != null) {
            wlOnRenderRefreshListener.onRefresh();
        }
    }

    void setWlOnGlSurfaceViewOncreateListener(WlOnGlSurfaceViewOncreateListener wlOnGlSurfaceViewOncreateListener) {
        this.wlOnGlSurfaceViewOncreateListener = wlOnGlSurfaceViewOncreateListener;
    }

    void setWlOnRenderRefreshListener(WlOnRenderRefreshListener wlOnRenderRefreshListener) {
        this.wlOnRenderRefreshListener = wlOnRenderRefreshListener;
    }

    /**
     * 初始化硬件解码shader
     */
    private void initMediacodecShader() {
        //将glsl文件中的内容装换成字符串源码
        String vertexShader = WlShaderUtils.readRawTextFile(context, R.raw.vertex_base);
        String fragmentShader = WlShaderUtils.readRawTextFile(context, R.raw.fragment_mediacodec);

        programId_mediacodec = WlShaderUtils.createProgram(vertexShader, fragmentShader);
        aPositionHandle_mediacodec = GLES20.glGetAttribLocation(programId_mediacodec, "av_Position");
        aTextureCoordHandle_mediacodec = GLES20.glGetAttribLocation(programId_mediacodec, "af_Position");
        uTextureSamplerHandle_mediacodec = GLES20.glGetUniformLocation(programId_mediacodec, "sTexture");

        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);

        textureid_mediacodec = textures[0];
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureid_mediacodec);
        WlShaderUtils.checkGlError("glBindTexture mTextureID");

        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);
        surfaceTexture = new SurfaceTexture(textureid_mediacodec);
        surfaceTexture.setOnFrameAvailableListener(this);
        surface = new Surface(surfaceTexture);
        if (wlOnGlSurfaceViewOncreateListener != null) {
            wlOnGlSurfaceViewOncreateListener.onGlSurfaceViewOncreate(surface);
        }
    }

    /**
     * 使用硬件解码shader
     */
    private void renderMediacodec() {
        LogUtil.v(TAG, "onDrawFrame renderMediacodec -->");
        GLES20.glUseProgram(programId_mediacodec);
        surfaceTexture.updateTexImage();
        vertexBuffer.position(0);
        GLES20.glEnableVertexAttribArray(aPositionHandle_mediacodec);
        GLES20.glVertexAttribPointer(aPositionHandle_mediacodec, 3, GLES20.GL_FLOAT, false,
                12, vertexBuffer);
        textureBuffer.position(0);
        GLES20.glEnableVertexAttribArray(aTextureCoordHandle_mediacodec);
        GLES20.glVertexAttribPointer(aTextureCoordHandle_mediacodec, 2, GLES20.GL_FLOAT, false, 8, textureBuffer);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureid_mediacodec);
        GLES20.glUniform1i(uTextureSamplerHandle_mediacodec, 0);
    }

    private void initYuvShader() {
        String vertexShader = WlShaderUtils.readRawTextFile(context, R.raw.vertex_base);
        String fragmentShader = WlShaderUtils.readRawTextFile(context, R.raw.fragment_yuv);
        programId_yuv = WlShaderUtils.createProgram(vertexShader, fragmentShader);
        aPositionHandle_yuv = GLES20.glGetAttribLocation(programId_yuv, "av_Position");
        aTextureCoordHandle_yuv = GLES20.glGetAttribLocation(programId_yuv, "af_Position");

        sampler_y = GLES20.glGetUniformLocation(programId_yuv, "sampler_y");
        sampler_u = GLES20.glGetUniformLocation(programId_yuv, "sampler_u");
        sampler_v = GLES20.glGetUniformLocation(programId_yuv, "sampler_v");

        textureid_yuv = new int[3];
        GLES20.glGenTextures(3, textureid_yuv, 0);
        for (int i = 0; i < 3; i++) {
            // 绑定纹理空间
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureid_yuv[i]);
            //设置属性 当显示的纹理比加载的纹理大时 使用纹理坐标中最接近的若干个颜色 通过加权算法获得绘制颜色
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            // 比加载的小
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            // 如果纹理坐标超出范围 0,0-1,1 坐标会被截断在范围内
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        }
    }

    private void renderYuv() {
        if (w > 0 && h > 0 && y != null && u != null && v != null) {
            LogUtil.v(TAG, "onDrawFrame renderYuv -->");
            GLES20.glUseProgram(programId_yuv);
            GLES20.glEnableVertexAttribArray(aPositionHandle_yuv);
            GLES20.glVertexAttribPointer(aPositionHandle_yuv, 3, GLES20.GL_FLOAT, false,
                    12, vertexBuffer);
            textureBuffer.position(0);
            GLES20.glEnableVertexAttribArray(aTextureCoordHandle_yuv);
            GLES20.glVertexAttribPointer(aTextureCoordHandle_yuv, 2, GLES20.GL_FLOAT, false, 8, textureBuffer);

            //使 GL_TEXTURE0 单元 活跃 opengl最多支持16个纹理
            //纹理单元是显卡中所有的可用于在shader中进行纹理采样的显存 数量与显卡类型相关，至少16个
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            //绑定纹理空间 下面的操作就会作用在这个空间中
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureid_yuv[0]);
            //创建一个2d纹理 使用亮度颜色模型并且纹理数据也是亮度颜色模型
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, w, h, 0, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, y);
            //绑定采样器与纹理单元
            GLES20.glUniform1i(sampler_y, 0);


            GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureid_yuv[1]);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, w / 2, h / 2, 0, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE,
                    u);
            GLES20.glUniform1i(sampler_u, 1);

            GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureid_yuv[2]);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, w / 2, h / 2, 0, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE,
                    v);
            GLES20.glUniform1i(sampler_v, 2);
            y.clear();
            u.clear();
            v.clear();
            y = null;
            u = null;
            v = null;
        }
    }

    private void initStop() {
        String vertexShader = WlShaderUtils.readRawTextFile(context, R.raw.vertex_base);
        String fragmentShader = WlShaderUtils.readRawTextFile(context, R.raw.fragment_no);
        programId_stop = WlShaderUtils.createProgram(vertexShader, fragmentShader);
        aPositionHandle_stop = GLES20.glGetAttribLocation(programId_stop, "av_Position");
        aTextureCoordHandle_stop = GLES20.glGetAttribLocation(programId_stop, "af_Position");
    }

    private void renderStop() {
        LogUtil.v(TAG, "onDrawFrame renderStop -->");
        GLES20.glUseProgram(programId_stop);
        vertexBuffer.position(0);
        GLES20.glEnableVertexAttribArray(aPositionHandle_stop);
        GLES20.glVertexAttribPointer(aPositionHandle_stop, 3, GLES20.GL_FLOAT, false,
                12, vertexBuffer);
        textureBuffer.position(0);
        GLES20.glEnableVertexAttribArray(aTextureCoordHandle_stop);
        GLES20.glVertexAttribPointer(aTextureCoordHandle_stop, 2, GLES20.GL_FLOAT, false, 8, textureBuffer);
    }

    private Bitmap cutBitmap(int w, int h) {
        int bitmapBuffer[] = new int[w * h];
        int bitmapSource[] = new int[w * h];
        IntBuffer intBuffer = IntBuffer.wrap(bitmapBuffer);
        intBuffer.position(0);
        try {
            GLES20.glReadPixels(0, 0, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE,
                    intBuffer);
            int offset1, offset2;
            for (int i = 0; i < h; i++) {
                offset1 = i * w;
                offset2 = (h - i - 1) * w;
                for (int j = 0; j < w; j++) {
                    int texturePixel = bitmapBuffer[offset1 + j];
                    int blue = (texturePixel >> 16) & 0xff;
                    int red = (texturePixel << 16) & 0x00ff0000;
                    int pixel = (texturePixel & 0xff00ff00) | red | blue;
                    bitmapSource[offset2 + j] = pixel;
                }
            }
        } catch (GLException e) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(bitmapSource, w, h, Bitmap.Config.ARGB_8888);
        intBuffer.clear();
        return bitmap;
    }

    void cutVideoImg() {
        cutimg = true;
    }

    public void destory() {
        setCodecType(2);
        if (vertexBuffer != null) vertexBuffer.clear();
        if (textureBuffer != null) textureBuffer.clear();
        if (surfaceTexture != null) {
            surfaceTexture.setOnFrameAvailableListener(null);
//        surfaceTexture.detachFromGLContext();
            surfaceTexture.release();
        }
    }

    public void glClear() {
        LogUtil.v(TAG, "glClear -->清除缓冲区");
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClear(GLES20.GL_STENCIL_BUFFER_BIT);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT);
    }
}
