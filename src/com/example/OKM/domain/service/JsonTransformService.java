package com.example.OKM.domain.service;

import android.content.Context;
import android.text.Html;
import com.example.OKM.domain.model.CacheMakerModel;
import com.example.OKM.domain.model.CacheModel;
import com.example.OKM.domain.valueObject.CacheSizeValue;
import com.example.OKM.domain.valueObject.CacheTypeValue;
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by kubut on 2015-09-07.
 */
public class JsonTransformService {
    public HashMap<String, CacheMakerModel> getCacheMarkersByJson(Context context, JSONObject jsonArray){
        Iterator iterator = jsonArray.keys();
        HashMap<String, CacheMakerModel> list = new HashMap<>();

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
                        context,
                        new LatLng(Double.parseDouble(cacheLocation[0]), Double.parseDouble(cacheLocation[1])),
                        jsonCache.getString("name"),
                        jsonCache.getString("code"),
                        jsonCache.getString("type"),
                        owner.getString("username"),
                        lastFound,
                        jsonCache.getString("size2")
                        );

                list.put(cache.getCode(), cache);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        return list;
    }

    public String getUuidByJson(JSONObject jsonObject) throws Exception{
        return jsonObject.getString("uuid");
    }

    //TODO: add attrs
    public CacheModel getCacheModelByJson(Context context, JSONObject jsonObject) throws Exception{
        String cacheLocation[] = jsonObject.getString("location").split("\\|");
        LatLng cachePosition = new LatLng(Double.parseDouble(cacheLocation[0]), Double.parseDouble(cacheLocation[1]));
        JSONObject owner = jsonObject.getJSONObject("owner");

        CacheModel cacheModel = new CacheModel();

        cacheModel.setCode(jsonObject.getString("code"));
        cacheModel.setName(jsonObject.getString("name"));
        cacheModel.setLocation(cachePosition);
        cacheModel.setType(new CacheTypeValue(context, jsonObject.getString("type")));
        cacheModel.setSize(new CacheSizeValue(context, jsonObject.getString("size2")));
        cacheModel.setUrl(jsonObject.getString("url"));
        cacheModel.setOwner(owner.getString("username"));
        cacheModel.setDescription(HtmlParser.parseHtml(jsonObject.getString("description"), context));

        return cacheModel;
    }
}
