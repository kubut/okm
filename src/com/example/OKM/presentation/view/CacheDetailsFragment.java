package com.example.OKM.presentation.view;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.example.OKM.R;
import com.example.OKM.domain.model.CacheModel;
import com.example.OKM.domain.service.HtmlParser;

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
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_cache_details, container, false);

        this.descriptionView = (WebView) view.findViewById(R.id.cache_description);

        this.descriptionView.setBackgroundColor(Color.TRANSPARENT);
        this.ready = true;

        this.syncView();
        return view;
    }

    @Override
    public void setView(CacheModel cacheModel){
        this.loaded = true;
        this.cacheModel = cacheModel;
        syncView();
    }

    @Override
    public void syncView(){
        if(this.loaded && this.ready){
            this.descriptionView.loadDataWithBaseURL(null, this.cacheModel.getDescription(), "text/html", "utf-8", null);
        }
    }
}
