package tk.cryptalker.util;

import java.security.MessageDigest;

import android.util.Log;

public class CryptoUtils {

    private static final String TAG = "CryptoUtils";

    public static String getHash(String value)
    {
        String ret = null;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] bs = md.digest(value.getBytes("UTF-8"));

            StringBuffer sb = new StringBuffer();
            for (byte b : bs) {
                String bt = Integer.toHexString(b & 0xff);
                if(bt.length() == 1) {
                    sb.append("0");
                }
                sb.append(bt);
            }
            ret = sb.toString();
        }
        catch (Exception e) {
            Log.e(TAG, "Error in getHash(): " + e.getMessage());
        }

        return ret;
    }

}
