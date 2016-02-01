package com.opencachingkubutmaps.presentation.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.opencachingkubutmaps.R;
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
    public void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_cache_photo);

        final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this.getApplicationContext()).build();
        ImageLoader.getInstance().init(config);

        this.imageView      = (ImageView) this.findViewById(R.id.photoZoom);
        this.progressView   = (ProgressView) this.findViewById(R.id.progressCircle);
        this.tryAgainView   = (RelativeLayout) this.findViewById(R.id.tryAgainView);

        this.loadPhoto(null);
    }

    @SuppressWarnings("UnusedParameters")
    public void loadPhoto(final View view){
        final ImageLoader imageLoader = ImageLoader.getInstance();

        final DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .build();

        final ImageLoadingListener listener = new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(final String imageUri, final View view) {
                CachePhotoActivity.this.imageView.setVisibility(View.GONE);
                CachePhotoActivity.this.progressView.setVisibility(View.VISIBLE);
                CachePhotoActivity.this.tryAgainView.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingFailed(final String imageUri, final View view, final FailReason failReason) {
                CachePhotoActivity.this.imageView.setVisibility(View.GONE);
                CachePhotoActivity.this.progressView.setVisibility(View.GONE);
                CachePhotoActivity.this.tryAgainView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingComplete(final String imageUri, final View view, final Bitmap loadedImage) {
                CachePhotoActivity.this.imageView.setVisibility(View.VISIBLE);
                CachePhotoActivity.this.progressView.setVisibility(View.GONE);
                CachePhotoActivity.this.tryAgainView.setVisibility(View.GONE);
                //noinspection ResultOfObjectAllocationIgnored
                new PhotoViewAttacher(CachePhotoActivity.this.imageView);
            }

            @Override
            public void onLoadingCancelled(final String imageUri, final View view) {
                CachePhotoActivity.this.imageView.setVisibility(View.GONE);
                CachePhotoActivity.this.progressView.setVisibility(View.GONE);
                CachePhotoActivity.this.tryAgainView.setVisibility(View.VISIBLE);
            }
        };

        imageLoader.displayImage(this.getIntent().getExtras().getString("photo_url"), this.imageView, options, listener);
    }
}
