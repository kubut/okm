package com.example.OKM.presentation.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.OKM.R;
import com.example.OKM.data.services.OkapiCommunication;
import com.example.OKM.domain.model.CacheModel;
import com.example.OKM.domain.service.JsonTransformService;
import com.example.OKM.domain.service.OkapiService;
import com.example.OKM.presentation.view.CacheActivity;
import org.json.JSONObject;

/**
 * Created by Jakub on 05.12.2015.
 */
public class CachePresenter {
    private CacheActivity cacheActivity;
    private CacheModel cacheModel;
    private OkapiService okapiService;
    private OkapiCommunication downloadTask;

    public CachePresenter(CacheActivity cacheActivity){
        this.cacheActivity = cacheActivity;
        this.okapiService = new OkapiService();
        this.downloadTask = null;
    }

    public void connectContext(CacheActivity cacheActivity){
        this.cacheActivity = cacheActivity;
    }

    public void disconnectContext(){
        this.cacheActivity = null;
        if(this.downloadTask != null){
            this.downloadTask.cancel(false);
        }
    }

    public Context getContext(){
        return this.getActivity().getApplicationContext();
    }

    public CacheActivity getActivity(){
        return this.cacheActivity;
    }

    public void loadCacheDetails(String code){
        if(cacheModel != null && this.cacheModel.getCode().equals(code)){
            this.setCacheDetails(this.cacheModel);
        } else {
            this.downloadCacheDetails(code);
        }
    }

    public void setCacheDetails(CacheModel cacheModel){
        this.getActivity().setCacheDetails(cacheModel);
        this.getActivity().switchToCache();
    }

    public void downloadCacheDetails(String code){
        String url = okapiService.getCacheDetails(this.getContext(), code);

        this.downloadTask = new OkapiCommunication(){
            @Override
            public void onPostExecute(String result){
                try{
                    if(!this.isCancelled()) {
                        JsonTransformService transformService = new JsonTransformService();
                        cacheModel = transformService.getCacheModelByJson(getContext(), new JSONObject(result));
                        setCacheDetails(cacheModel);
                    }
                } catch (Exception e){
                    getActivity().switchToTryAgain();
                }
            }
        };
        this.downloadTask.execute(url);
    }

    public void goToNavigation(){
        if(this.cacheModel == null){
            return;
        }

        final String url = "http://maps.google.com/maps?daddr=" + cacheModel.getLocation().latitude + "," + cacheModel.getLocation().longitude;
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
        this.getActivity().startActivity(intent);
    }

    public void goToGoogleMaps(){
        if(this.cacheModel == null){
            return;
        }

        final String url = "http://maps.google.com/maps?q=" + cacheModel.getLocation().latitude + "," + cacheModel.getLocation().longitude + "("+cacheModel.getCode()+")";
        final Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
        intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
        this.getActivity().startActivity(intent);
    }

    public void goToWebsite(){
        if(this.cacheModel == null){
            return;
        }

        final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(this.cacheModel.getUrl()));
        getActivity().startActivity(intent);
    }

    public void showHint(){
        if(this.cacheModel == null){
            return;
        }

        new MaterialDialog.Builder(this.getActivity())
                .title(R.string.cache_dialog_title)
                .content(this.cacheModel.getHint())
                .positiveText(R.string.cache_dialog_ok)
                .show();
    }
}
