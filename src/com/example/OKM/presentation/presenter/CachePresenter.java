package com.example.OKM.presentation.presenter;

import android.content.Context;
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

    public CachePresenter(CacheActivity cacheActivity){
        this.cacheActivity = cacheActivity;
        this.okapiService = new OkapiService();
    }

    public void connectContext(CacheActivity cacheActivity){
        this.cacheActivity = cacheActivity;
    }

    public void disconnectContext(){
        this.cacheActivity = null;
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
        this.getActivity().getTabDetails().setView(cacheModel);
        this.getActivity().switchToCache();
    }

    public void downloadCacheDetails(String code){
        String url = okapiService.getCacheDetails(this.getContext(), code);

        new OkapiCommunication(){
            @Override
            public void onPostExecute(String result){
                try{
                    JsonTransformService transformService = new JsonTransformService();
                    cacheModel = transformService.getCacheModelByJson(getContext(), new JSONObject(result));
                    setCacheDetails(cacheModel);
                } catch (Exception e){
                    getActivity().switchToTryAgain();
                }
            }
        }.execute(url);
    }
}
