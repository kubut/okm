package com.opencachingkubutmaps.presentation.view;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.opencachingkubutmaps.R;
import com.opencachingkubutmaps.domain.model.CacheModel;

public class CacheDetailsFragment extends Fragment implements ICacheTabs{
    private WebView descriptionView;
    private CacheModel cacheModel;
    private boolean loaded;
    private boolean ready;

    public CacheDetailsFragment(){
        this.ready = false;
        this.loaded = false;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.fragment_cache_details, container, false);

        this.descriptionView = (WebView) view.findViewById(R.id.cache_description);

        this.descriptionView.setBackgroundColor(Color.TRANSPARENT);
        this.ready = true;

        this.syncView();
        return view;
    }

    @Override
    public void setView(final Context context, final CacheModel cacheModel){
        this.loaded = true;
        this.cacheModel = cacheModel;
        this.syncView();
    }

    @Override
    public void syncView(){
        if(this.loaded && this.ready){
            this.descriptionView.loadDataWithBaseURL(null, this.cacheModel.getDescription(), "text/html", "utf-8", null);
        }
    }
}
