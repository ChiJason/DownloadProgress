package com.example.jasonchi.downloadprogress;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

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
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String SFTPHOST = "192.168.1.130";
        int SFTPPORT = 22;
        String SFTPUSER = "ko_dev";
        String SFTPPASS = "ko_dev";
        String SFTPWORKINGDIR = "/home/ko_dev/003/";

        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;


        int count;
        try {
            JSch jSch = new JSch();
            session = jSch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
            session.setPassword(SFTPPASS);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;
            channelSftp.cd(SFTPWORKINGDIR);
            SftpATTRS attrs = channelSftp.lstat(SFTPWORKINGDIR + "largo_resources_015_1477453513601_S44-3.zip");
            long lenghtOfFile = attrs.getSize();

            File tempDir = getResourcePath(this);
            File downloadFile = new File(tempDir, "largo_resources_015_1477453513601_S44-3.zip");
            File getFile = new File((this.getApplicationContext().getFileStreamPath("largo_resources_015_1477453513601_S44-3.zip").getPath()));

            // download the file
            InputStream input = new BufferedInputStream(channelSftp.get("largo_resources_015_1477453513601_S44-3.zip"));

            // Output stream
            OutputStream output = new BufferedOutputStream(new FileOutputStream(getFile));
            //"

            byte data[] = new byte[1024];
            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called

                Intent it = new Intent(ACTION);
                it.putExtra("progress", (int)((total*100)/lenghtOfFile));
                LocalBroadcastManager.getInstance(this).sendBroadcast(it);

                // writing data to file
                output.write(data, 0, count);
                Log.e("File", ""+total);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
            Intent it = new Intent(ACTION);
            it.putExtra("progress", 100);
            LocalBroadcastManager.getInstance(this).sendBroadcast(it);
        }
    }

    private File getResourcePath(Context context) {
        String localPath = "/storage/extSdCard/Android/data/com.ogloba.kiosk.android";
        if (localPath.length() > 0) {
            File file = new File(localPath);
            if (file.isDirectory()) {
                return file;
            } else {
                return Environment.getExternalStorageDirectory();
            }

        } else {
            return Environment.getExternalStorageDirectory();
        }
    }

    public File getDownloadTempDir(Context context) {
        File dir = new File(getResourcePath(context), "largo_temp");

        if (!dir.isDirectory()) {
            dir.mkdirs();
        }
        return dir;
    }
}
