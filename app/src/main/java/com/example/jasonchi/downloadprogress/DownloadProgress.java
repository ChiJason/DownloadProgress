package com.example.jasonchi.downloadprogress;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class DownloadProgress extends AppCompatActivity {

    Button download_btn;
    ImageView showPic;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_progress);

        init();

    }

    private void init() {
        download_btn =(Button) findViewById(R.id.download_btn);
        showPic = (ImageView) findViewById(R.id.showPic);

        pd = new ProgressDialog(this);
        pd.setMax(100);
        pd.setIndeterminate(false);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setTitle("Downloading...");
        pd.setCancelable(true);

        download_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DownloadProgress.this, DownloadService.class);
                intent.putExtra("receiver", new DownloadReceiver(new Handler()));
                startService(intent);
                pd.show();
            }
        });
    }
    private class DownloadReceiver extends ResultReceiver {

        public DownloadReceiver(Handler handler) {
            super(handler);
        }

        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == 0) {
                int progress = resultData.getInt("progress");
                pd.setProgress(progress);
                if(pd.getProgress() == 100){
                    pd.dismiss();
                    String imagePath = Environment.getExternalStorageDirectory().toString() + "/ssss.jpg";
                    showPic.setImageDrawable(Drawable.createFromPath(imagePath));
                }
            }

        }
    }
}
