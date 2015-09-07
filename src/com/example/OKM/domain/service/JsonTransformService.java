package com.example.OKM.domain.service;

import com.example.OKM.domain.model.CacheMakerModel;
import com.example.OKM.domain.model.CacheMarkerCollectionModel;
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by kubut on 2015-09-07.
 */
public class JsonTransformService {
    public CacheMarkerCollectionModel getCacheMarkersByJson(JSONObject jsonArray){
        Iterator iterator = jsonArray.keys();
        CacheMarkerCollectionModel list = new CacheMarkerCollectionModel();

        while (iterator.hasNext()){
            String key = (String)iterator.next();

            try{
                JSONObject jsonCache = jsonArray.getJSONObject(key);
                JSONObject owner = jsonCache.getJSONObject("owner");
                String cacheLocation[] = jsonCache.getString("location").split("\\|");
                String lastFound;

                if(jsonCache.getString("last_found").isEmpty()){
                    lastFound = null;
                } else {
                    lastFound = jsonCache.getString("last_found");
                }

                CacheMakerModel cache = new CacheMakerModel(
                        new LatLng(Double.parseDouble(cacheLocation[0]), Double.parseDouble(cacheLocation[1])),
                        jsonCache.getString("name"),
                        jsonCache.getString("code"),
                        jsonCache.getString("type"),
                        owner.getString("username"),
                        lastFound,
                        jsonCache.getString("size2")
                        );

                list.addCache(cache);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        return list;
    }
}
