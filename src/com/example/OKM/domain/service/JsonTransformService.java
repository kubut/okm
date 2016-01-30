package com.example.OKM.domain.service;

import android.content.Context;
import android.support.annotation.Nullable;
import com.example.OKM.domain.model.CacheMakerModel;
import com.example.OKM.domain.model.CacheModel;
import com.example.OKM.domain.valueObject.*;
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by kubut on 2015-09-07.
 */
@SuppressWarnings("ObjectAllocationInLoop")
public class JsonTransformService {
    public static HashMap<String, CacheMakerModel> getCacheMarkersByJson(final Context context, final JSONObject jsonArray) {
        final Iterator iterator = jsonArray.keys();
        final HashMap<String, CacheMakerModel> list = new HashMap<>();

        while (iterator.hasNext()) {
            final String key = (String) iterator.next();

            try {
                final JSONObject jsonCache = jsonArray.getJSONObject(key);
                final JSONObject owner = jsonCache.getJSONObject("owner");
                final String[] cacheLocation = jsonCache.getString("location").split("\\|");
                final String lastFound;

                if (jsonCache.getString("last_found").isEmpty()) {
                    lastFound = null;
                } else {
                    lastFound = jsonCache.getString("last_found");
                }

                int rating = -1;

                try{
                    rating = jsonCache.getInt("rating");
                } catch (final Exception e){
                    e.printStackTrace();
                }

                final CacheMakerModel cache = new CacheMakerModel(
                        context,
                        new LatLng(Double.parseDouble(cacheLocation[0]), Double.parseDouble(cacheLocation[1])),
                        jsonCache.getString("name"),
                        jsonCache.getString("code"),
                        jsonCache.getString("type"),
                        owner.getString("username"),
                        lastFound,
                        jsonCache.getString("size2"),
                        rating
                );

                list.put(cache.getCode(), cache);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    public static String getUuidByJson(final JSONObject jsonObject) throws Exception {
        return jsonObject.getString("uuid");
    }

    public static ArrayList<CacheAttributeValue> getAttrsFromJson(final Context context, final JSONObject jsonObject, final String lang) throws Exception {
        final ArrayList<CacheAttributeValue> attrs = new ArrayList<>();

        final Iterator<?> keys = jsonObject.keys();
        while (keys.hasNext()) {
            final String key = (String) keys.next();
            if (jsonObject.get(key) instanceof JSONObject) {
                String name = ((JSONObject) jsonObject.get(key)).getString("name");
                //noinspection StringConcatenationMissingWhitespace
                name = name.substring(0,1).toUpperCase() + name.substring(1);

                final CacheAttributeValue attr = new CacheAttributeValue(context, 0, key);
                attr.setLanguage(lang);
                attr.setName(name);

                attrs.add(attr);
            }
        }

        return attrs;
    }

    public static CacheModel getCacheModelByJson(final Context context, final JSONObject jsonObject, @Nullable final ArrayList<CacheAttributeValue> addedAttrs, final ArrayList<CacheAttributeValue> attrs) throws Exception {
        final String[] cacheLocation = jsonObject.getString("location").split("\\|");
        final LatLng cachePosition = new LatLng(Double.parseDouble(cacheLocation[0]), Double.parseDouble(cacheLocation[1]));
        final JSONObject owner = jsonObject.getJSONObject("owner");

        final CacheModel cacheModel = new CacheModel();

        cacheModel.setCode(jsonObject.getString("code"));
        cacheModel.setLocation(cachePosition);
        cacheModel.setType(new CacheTypeValue(context, jsonObject.getString("type")));
        cacheModel.setSize(new CacheSizeValue(context, jsonObject.getString("size2")));
        cacheModel.setUrl(jsonObject.getString("url"));
        cacheModel.setOwner(owner.getString("username"));
        cacheModel.setHint(jsonObject.getString("hint2"));
        cacheModel.setDescription(HtmlParser.parseHtml(jsonObject.getString("description"), context));
        cacheModel.appendPhotos(JsonTransformService.getCachePhotosValueByJson(jsonObject.getJSONArray("images")));

        try{
            cacheModel.setRating(jsonObject.getInt("rating"));
        } catch (final Exception e){
            cacheModel.setRating(-1);
        }

        cacheModel.appendLogs(JsonTransformService.getCacheLogsValueByJson(context, jsonObject.getJSONArray("latest_logs")));
        cacheModel.appendAttrs(attrs);
        if ((addedAttrs != null) && !addedAttrs.isEmpty()) {
            cacheModel.appendAttrs(addedAttrs);
        }

        return cacheModel;
    }

    private static ArrayList<CacheLogValue> getCacheLogsValueByJson(final Context context, final JSONArray jsonArray) throws Exception{
        final ArrayList<CacheLogValue> logs = new ArrayList<>();
        final int length = jsonArray.length();

        for (int i = 0; i < length; i++) {
            final JSONObject jsonLog = jsonArray.getJSONObject(i);

            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

            final CacheLogValue log = new CacheLogValue(context);
            log.setComment(HtmlParser.parseHtml(jsonLog.getString("comment"), context));
            log.setDate(simpleDateFormat.parse(jsonLog.getString("date")));
            log.setType(jsonLog.getString("type"));
            log.setUser(jsonLog.getJSONObject("user").getString("username"));

            logs.add(log);
        }

        return logs;
    }

    private static ArrayList<CachePhotoValue> getCachePhotosValueByJson(final JSONArray jsonArray) throws Exception{
        final ArrayList<CachePhotoValue> photos = new ArrayList<>();
        final int length = jsonArray.length();

        for (int i = 0; i < length; i++) {
            final JSONObject jsonPhoto = jsonArray.getJSONObject(i);

            final CachePhotoValue photo = new CachePhotoValue();
            photo.setMinUrl(jsonPhoto.getString("thumb_url"));
            photo.setUrl(jsonPhoto.getString("url"));
            photo.setTitle(jsonPhoto.getString("caption"));
            photo.setSpoiler(jsonPhoto.getBoolean("is_spoiler"));
            photos.add(photo);
        }

        return photos;
    }
}
