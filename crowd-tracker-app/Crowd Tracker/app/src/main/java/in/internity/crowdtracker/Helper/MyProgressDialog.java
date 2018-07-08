package in.internity.crowdtracker.Helper;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MyProgressDialog {

    Dialog dialog;
    private ProgressBar progressBar;
    private TextView textView;

    public MyProgressDialog(Context context, String Message) {

        dialog = new Dialog(context);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(false);


        RelativeLayout relativeLayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        relativeLayout.setLayoutParams(layoutParams);
        progressBar = new ProgressBar(context);
        textView=new TextView(context);
        textView.setText(""+Message);
        textView.setTextColor(Color.WHITE);

        RelativeLayout.LayoutParams layoutParams_progress = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams_progress.addRule(RelativeLayout.CENTER_IN_PARENT);
        RelativeLayout.LayoutParams layoutParams_text = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams_text.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams_text.setMargins(0,50,0,0);

        LinearLayout.LayoutParams linearlayoutParams_progress = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearlayoutParams_progress.gravity = Gravity.CENTER;
        progressBar.setLayoutParams(layoutParams_progress);

        relativeLayout.addView(progressBar);
        relativeLayout.addView(textView,layoutParams_text);

        dialog.getWindow().setContentView(relativeLayout, layoutParams);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));


    }

    public void show() {

        if (!dialog.isShowing() && dialog != null) {
            dialog.show();

        }

    }

    public void dismiss() {

        if (dialog.isShowing() && dialog != null) {
            dialog.dismiss();
        }
    }

    public void setCancelable(boolean cancelable) {
        dialog.setCancelable(cancelable);
    }


    public void setCanceledOnTouchOutside(boolean flag) {
        dialog.setCanceledOnTouchOutside(flag);
    }

    public void setColor(int colour) {
        progressBar.getIndeterminateDrawable().setColorFilter(colour, android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    public boolean isShowing() {

        return dialog.isShowing();
    }


    public void setMessage(String message) {
        textView.setText(message);
    }
}