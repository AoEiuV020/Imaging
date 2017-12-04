package com.xingren.imaging;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.xingren.imaging.core.IMGText;
import com.xingren.imaging.view.IMGColorGroup;

/**
 * Created by felix on 2017/12/1 上午11:21.
 */

public class ImageTextDialog extends Dialog implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener {

    private static final String TAG = "ImageTextDialog";

    private EditText mEditText;

    private Callback mCallback;

    private IMGText mDefaultText;

    private IMGColorGroup mColorGroup;

    public ImageTextDialog(Context context, Callback callback) {
        super(context, R.style.ImageTextDialog);
        setContentView(R.layout.image_text_dialog);
        mCallback = callback;
        Window window = getWindow();
        if (window != null) {
            window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mColorGroup = findViewById(R.id.cg_colors);
        mColorGroup.setOnCheckedChangeListener(this);
        mEditText = findViewById(R.id.et_text);

        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.tv_done).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mDefaultText != null) {
            setText(mDefaultText);
            mDefaultText = null;
        }
        mColorGroup.setCheckColor(mEditText.getCurrentTextColor());
    }

    public void setText(IMGText text) {
        if (mEditText != null) {
            mEditText.setText(text.getText());
            mEditText.setTextColor(text.getColor());
            if (!text.isEmpty()) {
                mEditText.setSelection(mEditText.length());
            }
        } else mDefaultText = text;
    }

    public void reset() {
        setText(new IMGText(null, Color.WHITE));
    }

    @Override
    public void onClick(View v) {
        int vid = v.getId();
        if (vid == R.id.tv_done) {
            onDone();
        } else if (vid == R.id.tv_cancel) {
            dismiss();
        }
    }

    private void onDone() {
        String text = mEditText.getText().toString();
        if (!TextUtils.isEmpty(text) && mCallback != null) {
            mCallback.onText(new IMGText(text, mEditText.getCurrentTextColor()));
        }
        dismiss();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        mEditText.setTextColor(mColorGroup.getCheckColor());
    }

    public interface Callback {

        void onText(IMGText text);
    }
}
