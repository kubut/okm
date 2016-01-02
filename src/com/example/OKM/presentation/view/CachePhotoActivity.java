package com.example.OKM.presentation.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.example.OKM.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.rey.material.widget.ProgressView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Jakub on 02.01.2016.
 */
public class CachePhotoActivity extends AppCompatActivity {
    private ImageView imageView;
    private ProgressView progressView;
    private RelativeLayout tryAgainView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache_photo);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this.getApplicationContext()).build();
        ImageLoader.getInstance().init(config);

        this.imageView      = (ImageView) findViewById(R.id.photoZoom);
        this.progressView   = (ProgressView) findViewById(R.id.progressCircle);
        this.tryAgainView   = (RelativeLayout) findViewById(R.id.tryAgainView);

        this.loadPhoto(null);
    }

    public void loadPhoto(View view){
        final ImageLoader imageLoader = ImageLoader.getInstance();

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .build();

        ImageLoadingListener listener = new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                imageView.setVisibility(View.GONE);
                progressView.setVisibility(View.VISIBLE);
                tryAgainView.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                imageView.setVisibility(View.GONE);
                progressView.setVisibility(View.GONE);
                tryAgainView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                imageView.setVisibility(View.VISIBLE);
                progressView.setVisibility(View.GONE);
                tryAgainView.setVisibility(View.GONE);
                new PhotoViewAttacher(imageView);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                imageView.setVisibility(View.GONE);
                progressView.setVisibility(View.GONE);
                tryAgainView.setVisibility(View.VISIBLE);
            }
        };

        imageLoader.displayImage(getIntent().getExtras().getString("photo_url"), imageView, options, listener);
    }
}
