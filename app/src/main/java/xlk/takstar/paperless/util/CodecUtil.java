package xlk.takstar.paperless.util;

import android.media.MediaCodecInfo;
import android.media.MediaCodecList;


/**
 * @author xlk
 * @date 2019/6/28
 */
public class CodecUtil {

    /**
     * 返回能够编码指定MIME类型的第一个编解码器，如果未找到匹配，则返回null。
     *
     * @param mimeType eg: "video/avc" 、audio/3gpp"
     * @return
     */
    public static MediaCodecInfo selectCodec(String mimeType) {
        int numCodecs = MediaCodecList.getCodecCount();
        for (int i = 0; i < numCodecs; i++) {
            MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);
            if (!codecInfo.isEncoder()) {
                continue;
            }
            String[] types = codecInfo.getSupportedTypes();
            for (int j = 0; j < types.length; j++) {
                if (types[j].equalsIgnoreCase(mimeType)) {
                    return codecInfo;
                }
            }
        }
        return null;
    }

    /**
     * 返回由编解码器和该测试代码支持的颜色格式。如果找不到匹配，则抛出一个测试失败——测试所知道的一组格式应该被扩展到新平台上。
     *
     * @param codecInfo
     * @param mimeType
     * @return
     */
    public static int selectColorFormat(MediaCodecInfo codecInfo, String mimeType) {
        MediaCodecInfo.CodecCapabilities capabilities = codecInfo.getCapabilitiesForType(mimeType);
        for (int i = 0; i < capabilities.colorFormats.length; i++) {
            int colorFormat = capabilities.colorFormats[i];
            if (isRecognizedFormat(colorFormat)) {
                return colorFormat;
            }
        }
        return 0;
    }

    /**
     * 如果这是本测试代码理解的颜色格式（即我们知道如何读取和生成这种格式的帧），则返回true。
     */
    private static boolean isRecognizedFormat(int colorFormat) {
        switch (colorFormat) {
            // 这些是后台能解析的颜色格式
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar:
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar:
                return true;
            default:
                return false;
        }
    }
}
