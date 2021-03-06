package com.opencachingkubutmaps.presentation.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.Nullable;
import com.afollestad.materialdialogs.MaterialDialog;
import com.opencachingkubutmaps.R;
import com.opencachingkubutmaps.data.dataManagers.AttributesDM;
import com.opencachingkubutmaps.data.services.OkapiCommunication;
import com.opencachingkubutmaps.domain.model.CacheModel;
import com.opencachingkubutmaps.domain.service.JsonTransformService;
import com.opencachingkubutmaps.domain.service.OkapiService;
import com.opencachingkubutmaps.domain.service.PreferencesService;
import com.opencachingkubutmaps.domain.valueObject.CacheAttributeValue;
import com.opencachingkubutmaps.presentation.adapter.AttributesListAdapter;
import com.opencachingkubutmaps.presentation.view.CacheActivity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jakub on 05.12.2015.
 */
public class CachePresenter {
    private CacheActivity cacheActivity;
    private CacheModel cacheModel;
    private OkapiCommunication downloadTask, attrsTask;

    public CachePresenter(final CacheActivity cacheActivity) {
        this.cacheActivity = cacheActivity;
        this.downloadTask = null;
        this.attrsTask = null;
    }

    public void connectContext(final CacheActivity cacheActivity) {
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

    private Context getContext() {
        return this.getActivity().getApplicationContext();
    }

    private CacheActivity getActivity() {
        return this.cacheActivity;
    }

    public void loadCacheDetails(final String code, boolean forceReload) {
        if (!forceReload && (this.cacheModel != null) && this.cacheModel.getCode().equals(code)) {
            this.setCacheDetails(this.cacheModel);
        } else {
            this.downloadCacheDetails(code);
        }
    }

    private void setCacheDetails(final CacheModel cacheModel) {
        this.getActivity().setCacheDetails(cacheModel);
        this.getActivity().switchToCache();
    }

    private void downloadCacheDetails(final String code) {
        final String url = OkapiService.getCacheDetails(this.getContext(), code);
        final PreferencesService preferencesService = new PreferencesService(this.getContext());

        this.downloadTask = new OkapiCommunication() {
            @Override
            public void onPostExecute(final String result) {
                try {
                    if (!this.isCancelled()) {
                        final JSONObject jsonObject = new JSONObject(result);
                        final AttributesDM attributesDM = new AttributesDM(CachePresenter.this.getContext());
                        final JSONArray attrsArray = jsonObject.getJSONArray("attr_acodes");

                        final ArrayList<CacheAttributeValue> attrs = new ArrayList<>();
                        final ArrayList<String> attrsToDownload = new ArrayList<>();
                        for (int i = 0; i < attrsArray.length(); i++) {
                            final CacheAttributeValue attr = attributesDM.findByAcodeAndLanguage(attrsArray.getString(i), preferencesService.getLanguageCode());
                            if (attr != null) {
                                attrs.add(attr);
                            } else {
                                attrsToDownload.add(attrsArray.getString(i));
                            }
                        }

                        if (!attrsToDownload.isEmpty()) {
                            CachePresenter.this.downloadAttrs(attrsToDownload, jsonObject, attributesDM, attrs);
                        } else {
                            CachePresenter.this.prepareCacheDetails(jsonObject, null, attrs);
                        }
                    }
                } catch (final Exception e) {
                    CachePresenter.this.getActivity().switchToTryAgain();
                }
            }
        };
        this.downloadTask.execute(url);
    }

    public void goToNavigation() {
        if (this.cacheModel == null) {
            return;
        }

        final String url = "http://maps.google.com/maps?daddr=" + this.cacheModel.getLocation().latitude + "," + this.cacheModel.getLocation().longitude;
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        this.getActivity().startActivity(intent);
    }

    public void goToGoogleMaps() {
        if (this.cacheModel == null) {
            return;
        }

        final String url = "http://maps.google.com/maps?q=" + this.cacheModel.getLocation().latitude + "," + this.cacheModel.getLocation().longitude + "(" + this.cacheModel.getCode() + ")";
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        this.getActivity().startActivity(intent);
    }

    public void goToWebsite() {
        if (this.cacheModel == null) {
            return;
        }

        final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(this.cacheModel.getUrl()));
        this.getActivity().startActivity(intent);
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

    public void showAttributes(){
        final AttributesListAdapter adapter = new AttributesListAdapter(this.getContext(), this.cacheModel.getAttrs());
        new MaterialDialog
                .Builder(this.getActivity())
                .adapter(adapter,null)
                .show();
    }

    public boolean isHint(){
        return (this.cacheModel != null) && this.cacheModel.isHint();
    }

    private void downloadAttrs(final ArrayList<String> codes, final JSONObject cacheDetail, final AttributesDM attributesDM, final ArrayList<CacheAttributeValue> attributes) {
        final String url = OkapiService.getAttributesURL(this.getContext(), codes);
        final PreferencesService preferencesService = new PreferencesService(this.getContext());

        this.attrsTask = new OkapiCommunication() {
            @Override
            public void onPostExecute(final String result) {
                try {
                    if (!this.isCancelled()) {
                        final JSONObject jsonObject = new JSONObject(result);
                        final ArrayList<CacheAttributeValue> attrs = JsonTransformService.getAttrsFromJson(CachePresenter.this.getContext(), jsonObject, preferencesService.getLanguageCode());
                        attributesDM.addAttrs(attrs);

                        CachePresenter.this.prepareCacheDetails(cacheDetail, attrs, attributes);
                    }
                } catch (final Exception e) {
                    CachePresenter.this.getActivity().switchToTryAgain();
                }
            }
        };
        this.attrsTask.execute(url);
    }

    private void prepareCacheDetails(final JSONObject jsonObject, @Nullable final ArrayList<CacheAttributeValue> addedAttrs, final ArrayList<CacheAttributeValue> attrs) throws Exception {
        this.cacheModel = JsonTransformService.getCacheModelByJson(this.getContext(), jsonObject, addedAttrs, attrs);

        this.setCacheDetails(this.cacheModel);
    }
}
