package com.hijamaplanet;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;

public class FontClass {
    private Context context;
    private Typeface custom_font1,custom_font2,custom_font3;

    public FontClass(Context context){
        this.context=context;
    }

    public Typeface getUbuntuFont() {
        return ResourcesCompat.getFont(context, R.font.ubuntu);
    }

    public Typeface getBoldFont() {
        return ResourcesCompat.getFont(context, R.font.lato_bold);
    }

    public Typeface getRegularFont() {
        return ResourcesCompat.getFont(context, R.font.lato_regular);
    }

}
