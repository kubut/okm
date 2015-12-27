package com.example.OKM.presentation.view;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.OKM.R;
import com.example.OKM.domain.model.CacheModel;

public class CacheGalleryFragment extends Fragment implements ICacheTabs{

    public CacheGalleryFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Log.i("Kubut","gal");

        return inflater.inflate(R.layout.fragment_cache_gallery, container, false);
    }

    @Override
    public void syncView() {

    }

    @Override
    public void setView(Context context, CacheModel cacheModel) {

    }
}
