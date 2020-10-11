package com.opencachingkubutmaps.domain.service;

import android.content.Context;
import com.opencachingkubutmaps.R;
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
        String serviceUrl = context.getString(R.string.okapi_getCacheDetails).replace("[$CACHE_CODE$]", code);
        serviceUrl = serviceUrl.replace("[$LPC$]", preferencesService.getLpc() + "");

        return apiUrl + serviceUrl + okapiKey;
    }

    public static String getAttributesURL(final Context context, final ArrayList<String> acodes){
        final PreferencesService preferencesService = new PreferencesService(context);

        final String apiUrl = preferencesService.getServerAPI();
        final String okapiKey = "&consumer_key=" + OkapiService.getOkapiKey(context, apiUrl);
        final String serviceUrl = context.getString(R.string.okapi_getAttrsNames).replace("[$ATTRS_ACODES$]", android.text.TextUtils.join("|", acodes));

        return apiUrl + serviceUrl + okapiKey;
    }

    public static String getLogSubmitUrl(final Context context, String cacheCode, String logType, String comment, int rating, String password) {
        final PreferencesService preferencesService = new PreferencesService(context);

        final String apiUrl = preferencesService.getServerAPI();
        final String okapiKey = "&consumer_key=" + OkapiService.getOkapiKey(context, apiUrl);
        String serviceUrl = context
                .getString(R.string.okapi_submit_log)
                .replace("[$CACHE_CODE$]", cacheCode)
                .replace("[$LOG_TYPE$]", logType)
                .replace("[$LANG_PREF$]", context.getString(R.string.langPref))
                .replace("[$LOG_COMMENT$]", comment);

        if(rating > 0) {
            serviceUrl = serviceUrl.replace("[$LOG_RATING$]", Integer.toString(rating));
        } else {
            serviceUrl = serviceUrl.replace("rating=[$LOG_RATING$]", "");
        }

        if(password != null) {
            serviceUrl = serviceUrl.replace("[$LOG_PASSWORD$]", password);
        } else {
            serviceUrl = serviceUrl.replace("password=[$LOG_PASSWORD$]", "");
        }

        return apiUrl + serviceUrl + okapiKey;
    }


    public static String getOkapiKey(final Context context, String server){
        server = server.replace("/okapi/","");
        server = server.replace("https://www.opencaching","okapiKey");
        server = server.replace("https://opencaching","okapiKey");
        server = server.replace(".","_");

        return context.getString(context.getResources().getIdentifier(server, "string", context.getPackageName()));
    }

    public static String getOkapiSecret(final Context context, String server){
        server = server.replace("/okapi/","");
        server = server.replace("https://www.opencaching","okapiSecret");
        server = server.replace("https://opencaching","okapiSecret");
        server = server.replace(".","_");

        return context.getString(context.getResources().getIdentifier(server, "string", context.getPackageName()));
    }
}
