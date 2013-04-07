package de.inselhome.beermat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;
import de.inselhome.beermat.R;

public class CustomTextView extends TextView {

    private static final String LOGTAG = "[beermat] CustomTextView";
    private static final String CUSTOM_FONTS_DIR = "fonts/";

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        String customFont = a.getString(R.styleable.CustomTextView_custom_font);
        setCustomFont(context, customFont);
        a.recycle();
    }

    private void setCustomFont(Context context, String customFont) {
        try {
            Typeface tf = Typeface.createFromAsset(context.getAssets(), CUSTOM_FONTS_DIR + customFont);
            setTypeface(tf);
        } catch (Exception e) {
            Log.e(LOGTAG, "Could not get typeface: " + e.getMessage());
            return;
        }
    }
}
