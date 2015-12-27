package com.example.OKM.data.services;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import com.example.OKM.data.model.URLDrawable;
import com.example.OKM.domain.valueObject.CacheLogValue;

/**
 * Created by Jakub on 25.12.2015.
 */
public class URLImageParser implements Html.ImageGetter {
    Context context;
    View container;
    CacheLogValue cacheLogValue;

    public URLImageParser(View t, Context context, CacheLogValue cacheLogValue) {
        this.context = context;
        this.container = t;
        this.cacheLogValue = cacheLogValue;
    }

    @Override
    public Drawable getDrawable(final String source) {
        URLDrawable urlDrawable = new URLDrawable();
        Drawable drawable = cacheLogValue.getDrawableByUrl(source);

        if(drawable != null){
            return drawable;
        }

        new ImageDownloader(urlDrawable, this.context){
            @Override
            protected void onPostExecute(Drawable result) {
                cacheLogValue.addDrawable(source, result);

                urlDrawable.setDrawable(result);

                URLImageParser.this.container.invalidate();
            }
        }.execute(source);

        return urlDrawable;
    }
}
