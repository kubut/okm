package com.opencachingkubutmaps.presentation.view;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.opencachingkubutmaps.R;
import com.opencachingkubutmaps.domain.model.CacheModel;
import com.opencachingkubutmaps.presentation.adapter.CacheGalleryAdapter;

public class CacheGalleryFragment extends Fragment implements ICacheTabs{
    private boolean loaded;
    private boolean ready;
    private CacheModel cacheModel;
    private ListView galleryView;

    public CacheGalleryFragment(){
        this.ready = false;
        this.loaded = false;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.fragment_cache_gallery, container, false);

        this.galleryView = (ListView) view.findViewById(R.id.cache_gallery_list);
        this.ready = true;

        this.syncView();

        return view;
    }

    @Override
    public void syncView() {
        if(this.loaded && this.ready){
            final CacheGalleryAdapter adapter = new CacheGalleryAdapter(this.getContext(), this.cacheModel.getPhotos());
            this.galleryView.setAdapter(adapter);
            this.galleryView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                    CacheGalleryFragment.this.openPhoto(adapter.getItem(position).getUrl());
                }
            });
        }
    }

    private void openPhoto(final String url){
        final Intent intent = new Intent(this.getContext(), CachePhotoActivity.class);
        intent.putExtra("photo_url", url);
        this.startActivity(intent);
    }

    @Override
    public void setView(final Context context, final CacheModel cacheModel) {
        this.loaded = true;
        this.cacheModel = cacheModel;
        this.syncView();
    }
}
