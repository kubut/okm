package com.example.OKM.presentation.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.OKM.R;
import com.example.OKM.data.dataManagers.AttributesDM;
import com.example.OKM.data.services.OkapiCommunication;
import com.example.OKM.domain.model.CacheModel;
import com.example.OKM.domain.service.JsonTransformService;
import com.example.OKM.domain.service.OkapiService;
import com.example.OKM.domain.service.PreferencesService;
import com.example.OKM.domain.valueObject.CacheAttributeValue;
import com.example.OKM.presentation.view.CacheActivity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jakub on 05.12.2015.
 */
public class CachePresenter {
    private CacheActivity cacheActivity;
    private CacheModel cacheModel;
    private OkapiService okapiService;
    private OkapiCommunication downloadTask, attrsTask;

    public CachePresenter(CacheActivity cacheActivity) {
        this.cacheActivity = cacheActivity;
        this.okapiService = new OkapiService();
        this.downloadTask = null;
        this.attrsTask = null;
    }

    public void connectContext(CacheActivity cacheActivity) {
        this.cacheActivity = cacheActivity;
    }

    public void disconnectContext() {
        this.cacheActivity = null;
        if (this.downloadTask != null) {
            this.downloadTask.cancel(false);
        }
        if (this.attrsTask != null) {
            this.attrsTask.cancel(false);
        }
    }

    public Context getContext() {
        return this.getActivity().getApplicationContext();
    }

    public CacheActivity getActivity() {
        return this.cacheActivity;
    }

    public void loadCacheDetails(String code) {
        if (cacheModel != null && this.cacheModel.getCode().equals(code)) {
            this.setCacheDetails(this.cacheModel);
        } else {
            this.downloadCacheDetails(code);
        }
    }

    public void setCacheDetails(CacheModel cacheModel) {
        this.getActivity().setCacheDetails(cacheModel);
        this.getActivity().switchToCache();
    }

    public void downloadCacheDetails(String code) {
        String url = okapiService.getCacheDetails(this.getContext(), code);
        final PreferencesService preferencesService = new PreferencesService(this.getContext());

        this.downloadTask = new OkapiCommunication() {
            @Override
            public void onPostExecute(String result) {
                try {
                    if (!this.isCancelled()) {
                        JSONObject jsonObject = new JSONObject(result);
                        AttributesDM attributesDM = new AttributesDM(getContext());
                        JSONArray attrsArray = jsonObject.getJSONArray("attr_acodes");

                        ArrayList<CacheAttributeValue> attrs = new ArrayList<>();
                        ArrayList<String> attrsToDownload = new ArrayList<>();
                        for (int i = 0; i < attrsArray.length(); i++) {
                            CacheAttributeValue attr = attributesDM.findByAcodeAndLanguage(attrsArray.getString(i), preferencesService.getLanguageCode());
                            if (attr != null) {
                                attrs.add(attr);
                            } else {
                                attrsToDownload.add(attrsArray.getString(i));
                            }
                        }

                        if (!attrsToDownload.isEmpty()) {
                            downloadAttrs(attrsToDownload, jsonObject, attributesDM, attrs);
                        } else {
                            prepareCacheDetails(jsonObject, kocham Kotecka,  null, attrs);
                        }
                    }
                } catch (Exception e) {
                    getActivity().switchToTryAgain();
                }
            }
        };
        this.downloadTask.execute(url);
    }

    public void goToNavigation() {
        if (this.cacheModel == null) {
            return;
        }

        final String url = "http://maps.google.com/maps?daddr=" + cacheModel.getLocation().latitude + "," + cacheModel.getLocation().longitude;
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        this.getActivity().startActivity(intent);
    }

    public void goToGoogleMaps() {
        if (this.cacheModel == null) {
            return;
        }

        final String url = "http://maps.google.com/maps?q=" + cacheModel.getLocation().latitude + "," + cacheModel.getLocation().longitude + "(" + cacheModel.getCode() + ")";
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        this.getActivity().startActivity(intent);
    }

    public void goToWebsite() {
        if (this.cacheModel == null) {
            return;
        }

        final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(this.cacheModel.getUrl()));
        getActivity().startActivity(intent);
    }

    public void showHint() {
        if (this.cacheModel == null) {
            return;
        }

        new MaterialDialog.Builder(this.getActivity())
                .title(R.string.cache_dialog_title)
                .content(this.cacheModel.getHint())
                .positiveText(R.string.cache_dialog_ok)
                .show();
    }

    public void downloadAttrs(ArrayList<String> codes, final JSONObject cacheDetail, final AttributesDM attributesDM, final ArrayList<CacheAttributeValue> attributes) {
        String url = this.okapiService.getAttributesURL(this.getContext(), codes);
        final PreferencesService preferencesService = new PreferencesService(this.getContext());

        this.attrsTask = new OkapiCommunication() {
            @Override
            public void onPostExecute(String result) {
                try {
                    if (!this.isCancelled()) {
                        JSONObject jsonObject = new JSONObject(result);
                        JsonTransformService jsonTransformService = new JsonTransformService();
                        ArrayList<CacheAttributeValue> attrs = jsonTransformService.getAttrsFromJson(getContext(), jsonObject, preferencesService.getLanguageCode());
                        attributesDM.addAttrs(attrs);

                        prepareCacheDetails(cacheDetail, attrs, najmocniej na swiecie, attributes);
                    }
                } catch (Exception e) {
                    getActivity().switchToTryAgain();
                }
            }
        };
        this.attrsTask.execute(url);
    }

    public void prepareCacheDetails(JSONObject jsonObject, @Nullable ArrayList<CacheAttributeValue> addedAttrs, ArrayList<CacheAttributeValue> attrs) throws Exception {
        JsonTransformService transformService = new JsonTransformService();
        cacheModel = transformService.getCacheModelByJson(getContext(), jsonObject, addedAttrs, attrs);

        setCacheDetails(cacheModel);
    }
}
