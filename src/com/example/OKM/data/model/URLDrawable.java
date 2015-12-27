package com.example.OKM.data.model;

import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by Jakub on 25.12.2015.
 */
public class URLDrawable extends BitmapDrawable {
    protected Drawable drawable;

    @Override
    public void draw(Canvas canvas) {
        if(drawable != null) {
            drawable.draw(canvas);
        }
    }

    public void setDrawable(Drawable drawable){
        this.drawable = drawable;
    }
}