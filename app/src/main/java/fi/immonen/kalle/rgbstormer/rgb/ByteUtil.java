package fi.immonen.kalle.rgbstormer.rgb;

/**
 * Created by TeZla on 15.1.2015.
 */
public class ByteUtil {

    public static final int SKIP_FIRST = 1;

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static byte[] invertAllButFirstByte(byte[] bytes) {

        for (int i = SKIP_FIRST; i < bytes.length; i++) {
            bytes[i] ^= 0xff;
        }
        return bytes;
    }
}
