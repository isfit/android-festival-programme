package org.isfit.festival.programme;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.isfit.festival.programme.util.Support;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.CompressFormat;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

public class FestivalProgramme extends Application {

    public static final String EVENTS_FILE_PATH = "events.json";
    public static FestivalProgramme instance;
    public static String eventsJSON = null;

    @Override
    public void onCreate() {
        super.onCreate();

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory().cacheOnDisc().build();

        // Create global configuration and initialize ImageLoader with this
        // configuration
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .discCacheSize(50 * 1024 * 1024).discCacheFileCount(100)
                .discCacheExtraOptions(1000, 1000, CompressFormat.JPEG, 75)
                .build();
        ImageLoader.getInstance().init(config);
        FileInputStream fis = null;
        try {
            fis = openFileInput(EVENTS_FILE_PATH);
            StringBuffer fileContent = new StringBuffer("");

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) != -1) {
                fileContent.append(new String(buffer));
            }
            eventsJSON = fileContent.toString();
        } catch (FileNotFoundException e) {
            Log.d(Support.DEBUG, "File not found in cache. Skipping.");
            // TODO show error that network is required if cache not present.
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        instance = this;

        
        

    }

    public static FestivalProgramme getInstance() {
        return instance;
    }

    public void writetoEventCache(String response) {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(EVENTS_FILE_PATH, MODE_PRIVATE);
            fos.write(response.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public boolean isWifi() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }
}
