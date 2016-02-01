package com.opencachingkubutmaps.presentation.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.opencachingkubutmaps.R;
import com.opencachingkubutmaps.domain.valueObject.CachePhotoValue;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.rey.material.widget.ProgressView;

import java.util.ArrayList;

/**
 * Created by Jakub on 01.01.2016.
 */
public class CacheGalleryAdapter extends ArrayAdapter<CachePhotoValue> {
    private final Context context;

    public CacheGalleryAdapter(final Context context, final ArrayList<CachePhotoValue> itemsList){
        super(context, R.layout.cache_gallery_item, itemsList);

        final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
        ImageLoader.getInstance().init(config);

        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent){
        if(convertView == null){
            final LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.cache_gallery_item, parent, false);
        }

        final CachePhotoValue photo = this.getItem(position);

        final TextView title              = (TextView) convertView.findViewById(R.id.gallery_item_title);
        final ImageView imageView         = (ImageView) convertView.findViewById(R.id.gallery_item_thumb);
        final ProgressView progressView   = (ProgressView) convertView.findViewById(R.id.progressCircle);

        title.setText(photo.getTitle());

        this.downloadImageView(imageView, progressView, photo);


        return convertView;
    }

    @SuppressWarnings("MethodMayBeStatic")
    private void downloadImageView(final ImageView imageView, final ProgressView progressView, final CachePhotoValue cachePhotoValue) {
        final ImageLoader imageLoader = ImageLoader.getInstance();

        final DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .showImageForEmptyUri(R.drawable.ic_image_black_48dp_spoiler)
                .showImageOnFail(R.drawable.ic_broken_image_black_36dp)
                .build();

        final ImageLoadingListener listener = new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(final String imageUri, final View view) {}

            @Override
            public void onLoadingFailed(final String imageUri, final View view, final FailReason failReason) {
                progressView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingComplete(final String imageUri, final View view, final Bitmap loadedImage) {
                progressView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingCancelled(final String imageUri, final View view) {}
        };

        final String url;
        if(cachePhotoValue.isSpoiler()){
            imageView.setPadding(10,10,10,10);
            url = null;
        } else {
            imageView.setPadding(0,0,0,0);
            url = cachePhotoValue.getMinUrl();
        }

        imageLoader.displayImage(url, imageView, options, listener);
    }
}
