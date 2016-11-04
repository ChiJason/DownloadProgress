package com.example.jasonchi.downloadprogress;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.daimajia.numberprogressbar.OnProgressBarListener;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class DownloadProgress extends AppCompatActivity implements OnProgressBarListener{

    Button download_btn;
    ImageView showPic;
    BroadcastReceiver broadcastReceiver;
    ProgressDialog pd;
    ProcessProgressDialog processProgressDialog;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_progress);

        init();

    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter iff = new IntentFilter(DownloadService.ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, iff);
    }

    private void init() {
        download_btn =(Button) findViewById(R.id.download_btn);
        showPic = (ImageView) findViewById(R.id.showPic);

        download_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DownloadService.DownloadService(getApplicationContext());
//                showProgressDialog();
                showNumberProgressDialog();
            }
        });

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                pd.setProgress(intent.getIntExtra("progress",0));
                if(pd.getProgress() == 100){
                    String imagePath = Environment.getExternalStorageDirectory().toString() + "/ssss.jpg";
                    showPic.setImageDrawable(Drawable.createFromPath(imagePath));
                    pd.dismiss();
                    File downloadfile = new File(getApplicationContext().getFileStreamPath("/largo_resources_015_1477453513601_S44-3.zip").getPath());
                    if(downloadfile.isFile()){
                        Toast.makeText(DownloadProgress.this, "Download Files....isComplelte", Toast.LENGTH_LONG).show();
                    }
                }
            }
        };

    }

    private void showNumberProgressDialog(){

        processProgressDialog = new ProcessProgressDialog(this, "Processing, please wait...");
        processProgressDialog.show(getSupportFragmentManager(), "");

        timer = new Timer();
        timer.schedule(new TimerTask() {
            int i =0;
            int total = 12000;
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        i+=10;
                        processProgressDialog.setProgressMessage("process 1");
                        processProgressDialog.setSecondProgressMessage("process 2");
                        processProgressDialog.setDialogProgress(i * 100 / total);
                    }
                });
            }
        }, 1000, 100);
//        if(processProgressDialog.isClose()){
//            timer.cancel();
//        }

    }

    private void showProgressDialog(){
        pd = new ProgressDialog(this);
        pd.setMax(100);
        pd.setIndeterminate(false);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setTitle("Downloading...");
        pd.setCancelable(false);
        pd.show();
    }

    @Override
    public void onProgressChange(int current, int max) {
        if(current == max) {
            if(processProgressDialog.isProcessFinished()) {
                processProgressDialog.dismiss();
            }
        }
    }
}
