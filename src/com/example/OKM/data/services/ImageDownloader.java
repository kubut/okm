package com.example.OKM.data.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import com.example.OKM.data.model.URLDrawable;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Jakub on 26.12.2015.
 */
public class ImageDownloader extends AsyncTask<String, Void, Drawable> {
    protected URLDrawable urlDrawable;
    private Context context;

    public ImageDownloader(URLDrawable d, Context context) {
        this.urlDrawable = d;
        this.context = context;
    }

    @Override
    @Nullable
    protected Drawable doInBackground(String... params) {
        String source = params[0];
        return fetchDrawable(source);
    }

    @Nullable
    public Drawable fetchDrawable(String urlString) {
        try {
            InputStream is = fetch(urlString);
//            Drawable drawable = Drawable.createFromStream(is, "src");

            Bitmap bmp = BitmapFactory.decodeStream(is);
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            bmp.setDensity(dm.densityDpi);
            Drawable drawable = new BitmapDrawable(context.getResources(), bmp);
            Log.e("kubut", bmp.getWidth() +" , "+ drawable.getIntrinsicHeight());
            drawable.setBounds(0, 0, bmp.getWidth(), bmp.getHeight());
            return drawable;
        } catch (Exception e) {
            return null;
        }
    }

    private InputStream fetch(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        return new BufferedInputStream(connection.getInputStream());
    }
}
