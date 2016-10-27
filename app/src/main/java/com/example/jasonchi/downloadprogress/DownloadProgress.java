package com.example.jasonchi.downloadprogress;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class DownloadProgress extends AppCompatActivity {

    Button download_btn;
    ImageView showPic;
    BroadcastReceiver broadcastReceiver;
    ProgressDialog pd;

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
                DownloadService.DownloadService(getApplicationContext());
                showProgressDialog();
            }
        });

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                pd.setProgress(intent.getIntExtra("progress",0));
                if(pd.getProgress() == 100){
//                    String imagePath = Environment.getExternalStorageDirectory().toString() + "/ssss.jpg";
//                    showPic.setImageDrawable(Drawable.createFromPath(imagePath));
                    pd.dismiss();
                    File downloadfile = new File(Environment.getExternalStorageDirectory().toString()+"//largo_resources_015_1477453513601_S44-3.zip");
                    if(downloadfile.isFile()){
                        Toast.makeText(DownloadProgress.this, "Download Files....isComplelte", Toast.LENGTH_LONG).show();
                    }
                }
            }
        };

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

}
