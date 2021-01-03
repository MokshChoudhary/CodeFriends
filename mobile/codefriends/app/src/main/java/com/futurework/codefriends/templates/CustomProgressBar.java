package com.futurework.codefriends.templates;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.futurework.codefriends.R;

import androidx.annotation.NonNull;

public class CustomProgressBar extends Dialog{
    private TextView title;
    private TextView msg;
    private Button button;

    public Button getButton() {
        return button;
    }

    public CustomProgressBar(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_progress);
        setCancelable(false);
        title = this.getWindow().findViewById(R.id.progress_title);
        msg = this.getWindow().findViewById(R.id.progress_msg);
        button = this.getWindow().findViewById(R.id.button2);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setMessage(String msg) {
        this.msg.setText(msg);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
