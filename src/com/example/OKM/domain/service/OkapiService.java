package com.example.OKM.domain.service;

import android.content.Context;
import com.example.OKM.R;
import com.google.android.gms.maps.model.LatLng;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by kubut on 2015-09-01.
 */
@SuppressWarnings("StringConcatenationMissingWhitespace")
public class OkapiService {

    public static String getCacheCollectionURL(final Context context, final LatLng center, final String uuid){
        final PreferencesService preferencesService = new PreferencesService(context);

        String serviceUrl;
        final String apiUrl = preferencesService.getServerAPI();
        final String limit = Integer.toString(preferencesService.getCachesLimit());
        final String okapiKey = "&consumer_key=" + OkapiService.getOkapiKey(context, apiUrl);

        if(preferencesService.isHideFound() && (uuid != null)){
            serviceUrl = context.getString(R.string.okapi_getNearbyCachesNotFound);
            serviceUrl = serviceUrl.replace("[$UUID$]", uuid);
        } else {
            serviceUrl = context.getString(R.string.okapi_getNearbyCaches);
        }

        serviceUrl = serviceUrl.replace("[$LIMIT$]", limit);
        serviceUrl = serviceUrl.replace("[$CENTER$]", Double.toString(center.latitude)+"|"+Double.toString(center.longitude));

        return apiUrl + serviceUrl + okapiKey;
    }

    public static String getUuidURL(final Context context, final String username) throws Exception{
        final PreferencesService preferencesService = new PreferencesService(context);

        final String apiUrl = preferencesService.getServerAPI();
        final String okapiKey = "&consumer_key=" + OkapiService.getOkapiKey(context, apiUrl);
        final String serviceUrl = context.getString(R.string.okapi_getUuid).replace("[$USER$]", URLEncoder.encode(username, "UTF-8"));

        return apiUrl + serviceUrl + okapiKey;
    }

    public static String getCacheDetails(final Context context, final String code){
        final PreferencesService preferencesService = new PreferencesService(context);

        final String apiUrl = preferencesService.getServerAPI();
        final String okapiKey = "&consumer_key=" + OkapiService.getOkapiKey(context, apiUrl);
        final String serviceUrl = context.getString(R.string.okapi_getCacheDetails).replace("[$CACHE_CODE$]", code);

        return apiUrl + serviceUrl + okapiKey;
    }

    public static String getAttributesURL(final Context context, final ArrayList<String> acodes){
        final PreferencesService preferencesService = new PreferencesService(context);

        final String apiUrl = preferencesService.getServerAPI();
        final String okapiKey = "&consumer_key=" + OkapiService.getOkapiKey(context, apiUrl);
        final String serviceUrl = context.getString(R.string.okapi_getAttrsNames).replace("[$ATTRS_ACODES$]", android.text.TextUtils.join("|", acodes));

        return apiUrl + serviceUrl + okapiKey;
    }

    private static String getOkapiKey(final Context context, String server){
        server = server.replace("/okapi/","");
        server = server.replace("http://www.opencaching","okapiKey");
        server = server.replace(".","_");

        return context.getString(context.getResources().getIdentifier(server, "string", context.getPackageName()));
    }

}
