package com.logicrack.MaityPoultry.customfonts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


@SuppressLint("AppCompatCustomView")
public class MyTextViewSansBold extends TextView {

    public MyTextViewSansBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextViewSansBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextViewSansBold(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSans-Semibold.ttf");
            setTypeface(tf);
        }
    }

}