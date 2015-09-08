package com.example.OKM.domain.service;

import android.content.Context;
import android.os.AsyncTask;
import com.example.OKM.R;
import com.example.OKM.data.services.OkapiCommunication;
import com.example.OKM.presentation.view.SettingsActivity;
import com.google.android.gms.maps.model.LatLng;

import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.Objects;
import java.util.concurrent.Callable;

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

    public String getUuidURL(Context context, String username){
        PreferencesService preferencesService = new PreferencesService(context);

        String apiUrl = preferencesService.getServerAPI();
        String okapiKey = "&consumer_key=" + context.getString(R.string.okapiKey);
        String serviceUrl = context.getString(R.string.okapi_getUuid).replace("[$USER$]", URLEncoder.encode(username));

        return apiUrl + serviceUrl + okapiKey;
    }

}
