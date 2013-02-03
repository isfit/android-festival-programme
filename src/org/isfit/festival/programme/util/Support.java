package org.isfit.festival.programme.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import android.os.Build;

public class Support {
    public static final String DEBUG = "ISFiT_d"; 
    
    public static void checkNotNull(Object object) {
        if (object == null) {
            throw new NullPointerException("The object is null");
        }
    }
    
    /**
     * Stringifies an InputStream so we can handle it easier.
     * 
     * @param stream
     *            The {@link InputStream} to stringify
     * @return A stringified JSON response.
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    public static String stringify(InputStream stream) throws IOException,
            UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(reader);
        return bufferedReader.readLine();
    }
    
    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

}
