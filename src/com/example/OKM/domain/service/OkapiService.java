package com.example.OKM.domain.service;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import com.example.OKM.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by kubut on 2015-09-01.
 */
public class OkapiService {

    public String getCacheCollectionURL(Context context, LatLng center, String uuid){
        PreferencesService preferencesService = new PreferencesService(context);

        String serviceUrl;
        String apiUrl = preferencesService.getServerAPI();
        String limit = Integer.toString(preferencesService.getCachesLimit());
        String okapiKey = "&consumer_key=" + this.getOkapiKey(context, apiUrl);

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
        String okapiKey = "&consumer_key=" + this.getOkapiKey(context, apiUrl);
        String serviceUrl = context.getString(R.string.okapi_getUuid).replace("[$USER$]", URLEncoder.encode(username, "UTF-8"));

        return apiUrl + serviceUrl + okapiKey;
    }

    public String getCacheDetails(Context context, String code){
        PreferencesService preferencesService = new PreferencesService(context);

        String apiUrl = preferencesService.getServerAPI();
        String lang = preferencesService.getLanguageCode();
        String okapiKey = "&consumer_key=" + this.getOkapiKey(context, apiUrl);
        String serviceUrl = context.getString(R.string.okapi_getCacheDetails).replace("[$CACHE_CODE$]", code);

        serviceUrl = serviceUrl.replace("[$LANG$]", lang);

        return apiUrl + serviceUrl + okapiKey;
    }

    public String getAttributesURL(Context context, ArrayList<String> acodes){
        PreferencesService preferencesService = new PreferencesService(context);

        String apiUrl = preferencesService.getServerAPI();
        String lang = preferencesService.getLanguageCode();
        String okapiKey = "&consumer_key=" + this.getOkapiKey(context, apiUrl);
        String serviceUrl = context.getString(R.string.okapi_getAttrsNames).replace("[$ATTRS_ACODES$]", android.text.TextUtils.join("|", acodes));

        serviceUrl = serviceUrl.replace("[$LANG$]", lang);

        return apiUrl + serviceUrl + okapiKey;
    }

    public String getOkapiKey(Context context, String server){
        server = server.replace("/okapi/","");
        server = server.replace("http://www.opencaching","okapiKey");
        server = server.replace(".","_");

        return context.getString(context.getResources().getIdentifier(server, "string", context.getPackageName()));
    }

}
