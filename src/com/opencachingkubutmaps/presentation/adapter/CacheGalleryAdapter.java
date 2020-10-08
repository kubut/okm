package com.opencachingkubutmaps.presentation.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.opencachingkubutmaps.R;
import com.opencachingkubutmaps.domain.valueObject.CachePhotoValue;
import com.rey.material.widget.ProgressView;

import java.util.ArrayList;

/**
 * Created by Jakub on 01.01.2016.
 */
public class CacheGalleryAdapter extends ArrayAdapter<CachePhotoValue> {
    private final Context context;

    public CacheGalleryAdapter(final Context context, final ArrayList<CachePhotoValue> itemsList) {
        super(context, R.layout.cache_gallery_item, itemsList);

        final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
        ImageLoader.getInstance().init(config);

        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            final LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.cache_gallery_item, parent, false);
        }

        final CachePhotoValue photo = this.getItem(position);

        final TextView title = (TextView) convertView.findViewById(R.id.gallery_item_title);
        final ImageView imageView = (ImageView) convertView.findViewById(R.id.gallery_item_thumb);
        final ProgressView progressView = (ProgressView) convertView.findViewById(R.id.progressCircle);

        title.setText(photo.getTitle());

        this.downloadImageView(imageView, progressView, photo);


        return convertView;
    }

    @SuppressWarnings("MethodMayBeStatic")
    private void downloadImageView(final ImageView imageView, final ProgressView progressView, final CachePhotoValue cachePhotoValue) {
        if (cachePhotoValue.isSpoiler()) {
            imageView.setPadding(10, 10, 10, 10);
            progressView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageDrawable(context.getDrawable(R.drawable.ic_image_black_48dp_spoiler));
        } else {
            imageView.setPadding(0, 0, 0, 0);

            Glide
                    .with(context)
                    .load(cachePhotoValue.getUrl())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressView.setVisibility(View.GONE);
                            imageView.setVisibility(View.VISIBLE);

                            imageView.setImageDrawable(context.getDrawable(R.drawable.ic_broken_image_black_36dp));
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressView.setVisibility(View.GONE);
                            imageView.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .into(imageView);
        }
    }
}
