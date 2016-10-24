package com.example.jasonchi.downloadprogress;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by JasonChi on 2016/10/14.
 */

public class DownloadReceiver extends ResultReceiver {

    public DownloadReceiver(Handler handler) {
        super(handler);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);
        if (resultCode == 0) {
            int progress = resultData.getInt("progress");
        }
    }
}
