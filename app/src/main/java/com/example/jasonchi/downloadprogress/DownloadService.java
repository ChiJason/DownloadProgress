package com.example.jasonchi.downloadprogress;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by JasonChi on 2016/10/13.
 */

public class DownloadService extends IntentService {

    public static String ACTION= MyApplication.getInstance().getPackageName() + ".intent.action." + "SHOW_PRPGRESS";

    public DownloadService() {
        super("Service");
    }

    public static void DownloadService(Context context){
        Intent intent = new Intent(context, DownloadService.class);
//        intent.putExtra("receiver", new DownloadReceiver(new Handler()));
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {



        int count;
        try {
            URL url = new URL("http://api.androidhive.info/progressdialog/hive.jpg");
            URLConnection conection = url.openConnection();
            conection.connect();
            // this will be useful so that you can show a tipical 0-100% progress bar
            int lenghtOfFile = conection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            // Output stream
            OutputStream output = new FileOutputStream("/sdcard/ssss.jpg");
            //"

            byte data[] = new byte[1024];

            long total = 0;



            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called

//                Bundle getData = new Bundle();
//                getData.putInt("progress", (int)((total*100)/lenghtOfFile));
                Intent it = new Intent(ACTION);
                it.putExtra("progress", (int)((total*100)/lenghtOfFile));
                LocalBroadcastManager.getInstance(this).sendBroadcast(it);

                //receiver.send(0, getData);
                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }
    }
}
