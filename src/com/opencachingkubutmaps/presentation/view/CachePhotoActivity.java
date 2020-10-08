package com.opencachingkubutmaps.presentation.view;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.opencachingkubutmaps.R;
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
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_cache_photo);

        final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this.getApplicationContext()).build();
        ImageLoader.getInstance().init(config);

        this.imageView = (ImageView) this.findViewById(R.id.photoZoom);
        this.progressView = (ProgressView) this.findViewById(R.id.progressCircle);
        this.tryAgainView = (RelativeLayout) this.findViewById(R.id.tryAgainView);

        this.loadPhoto(null);
    }

    @SuppressWarnings("UnusedParameters")
    public void loadPhoto(final View view) {
        this.imageView.setVisibility(View.INVISIBLE);
        this.progressView.setVisibility(View.VISIBLE);
        this.tryAgainView.setVisibility(View.GONE);

        final String url  = this.getIntent().getExtras().getString("photo_url");

        Glide
                .with(this)
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        CachePhotoActivity.this.imageView.setVisibility(View.GONE);
                        CachePhotoActivity.this.progressView.setVisibility(View.GONE);
                        CachePhotoActivity.this.tryAgainView.setVisibility(View.VISIBLE);

                        assert e != null;
                        e.printStackTrace();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        CachePhotoActivity.this.imageView.setVisibility(View.VISIBLE);
                        CachePhotoActivity.this.progressView.setVisibility(View.GONE);
                        CachePhotoActivity.this.tryAgainView.setVisibility(View.GONE);
                        //noinspection ResultOfObjectAllocationIgnored
                        new PhotoViewAttacher(CachePhotoActivity.this.imageView);
                        return false;
                    }
                })
                .into(this.imageView);
    }
}
