package com.example.OKM.domain.service;

import android.content.Context;
import com.example.OKM.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;

import java.net.URLEncoder;

/**
 * Created by kubut on 2015-09-01.
 */
public class OkapiService {

    public String getCacheCollectionURL(Context context, LatLng center, String uuid){
        PreferencesService preferencesService = new PreferencesService(context);

        String serviceUrl;
        String apiUrl = preferencesService.getServerAPI();
        String limit = Integer.toString(preferencesService.getCachesLimit());
        String okapiKey = "&consumer_key=" + context.getString(R.string.okapiKey);

        if(preferencesService.isHideFound() && uuid != null){
            serviceUrl = context.getString(R.string.okapi_getNearbyCachesNotFound);
            serviceUrl = serviceUrl.replace("[$UUID$]", uuid);
        } else {
            serviceUrl = context.getString(R.string.okapi_getNearbyCaches);
        }

        serviceUrl = serviceUrl.replace("[$LIMIT$]", limit);
        serviceUrl = serviceUrl.replace("[$CENTER$]", Double.toString(center.latitude)+"|"+Double.toString(center.longitude));

        return apiUrl + serviceUrl + okapiKey;
    }

    public String getUuidURL(Context context, String username) throws Exception{
        PreferencesService preferencesService = new PreferencesService(context);

        String apiUrl = preferencesService.getServerAPI();
        String okapiKey = "&consumer_key=" + context.getString(R.string.okapiKey);
        String serviceUrl = context.getString(R.string.okapi_getUuid).replace("[$USER$]", URLEncoder.encode(username, "UTF-8"));

        return apiUrl + serviceUrl + okapiKey;
    }

    public String getCacheDetails(Context context, String code){
        PreferencesService preferencesService = new PreferencesService(context);

        String apiUrl = preferencesService.getServerAPI();
        String lang = preferencesService.getLanguageSymbol();
        String okapiKey = "&consumer_key=" + context.getString(R.string.okapiKey);
        String serviceUrl = context.getString(R.string.okapi_getCacheDetails).replace("[$CACHE_CODE$]", code);

        serviceUrl = serviceUrl.replace("[$LANG$]", lang);

        return apiUrl + serviceUrl + okapiKey;
    }

}
