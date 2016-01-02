package com.example.OKM.presentation.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.OKM.R;
import com.example.OKM.domain.valueObject.CachePhotoValue;
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
    private Context context;

    public CacheGalleryAdapter(Context context, ArrayList<CachePhotoValue> itemsList){
        super(context, R.layout.cache_gallery_item, itemsList);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
        ImageLoader.getInstance().init(config);

        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.cache_gallery_item, parent, false);
        }

        CachePhotoValue photo = getItem(position);

        TextView title              = (TextView) convertView.findViewById(R.id.gallery_item_title);
        ImageView imageView         = (ImageView) convertView.findViewById(R.id.gallery_item_thumb);
        ProgressView progressView   = (ProgressView) convertView.findViewById(R.id.progressCircle);

        title.setText(photo.getTitle());

        this.downloadImageView(imageView, progressView, photo);


        return convertView;
    }

    public void downloadImageView(final ImageView imageView, final ProgressView progressView, final CachePhotoValue cachePhotoValue) {
        ImageLoader imageLoader = ImageLoader.getInstance();

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .showImageForEmptyUri(R.drawable.ic_image_black_48dp_spoiler)
                .showImageOnFail(R.drawable.ic_broken_image_black_36dp)
                .build();

        ImageLoadingListener listener = new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {}

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                progressView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {}
        };

        String url;
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
