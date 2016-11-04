package com.example.jasonchi.downloadprogress;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;


import com.daimajia.numberprogressbar.NumberProgressBar;
import com.daimajia.numberprogressbar.OnProgressBarListener;

/**
 * Created by JasonChi on 2016/11/3.
 */

public class ProcessProgressDialog extends AppCompatDialogFragment implements OnProgressBarListener{

    private Context contex;
    private NumberProgressBar bar1, bar2;
    private TextView dialogTitle, msg1, msg2;
    private String title;
    private boolean isClose = false;

    public ProcessProgressDialog(Context context, String dialogTitle) {
        this.contex = context;
        this.title = dialogTitle;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        setCancelable(false);

        View dialogView = inflater.inflate(R.layout.custom_dialog, container);

        setupProgressBar(dialogView);

        return dialogView;
    }

    private void setupProgressBar(View view) {
        dialogTitle = (TextView) view.findViewById(R.id.title);
        dialogTitle.setText(title);
        msg1 = (TextView) view.findViewById(R.id.progressbar_1_msg);
        msg2 = (TextView) view.findViewById(R.id.progressbar_2_msg);
        bar1 = (NumberProgressBar) view.findViewById(R.id.progressbar_1);
        bar1.setOnProgressBarListener(this);
        bar2 = (NumberProgressBar) view.findViewById(R.id.progressbar_2);
        bar2.setOnProgressBarListener(this);
    }

    public void setDialogProgress(int progress) {
        bar1.incrementProgressBy(progress);
        bar2.incrementProgressBy(progress);
    }

    public void setProgressMessage(String msg){
        msg1.setText(msg);
    }

    public void setSecondProgressMessage(String msg){
        msg2.setText(msg);
    }

    public boolean isProcessFinished() {
        return bar1.getProgress() ==100 && bar2.getProgress() == 100;
    }

    public boolean isClose(){
        return this.isClose;
    }

    @Override
    public void onProgressChange(int current, int max) {
        if(current == max){
            if(isProcessFinished()){
                isClose = true;
                dismiss();
            }
        }
    }
}
