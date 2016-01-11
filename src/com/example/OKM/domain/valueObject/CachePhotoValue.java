package com.example.OKM.domain.valueObject;

/**
 * Created by Jakub on 01.01.2016.
 */
public class CachePhotoValue {
    private String minUrl, url, title;
    private boolean spoiler;

    public boolean isSpoiler() {
        return this.spoiler;
    }
    public void setSpoiler(final boolean spoiler) {
        this.spoiler = spoiler;
    }
    public String getMinUrl() {
        return this.minUrl;
    }
    public void setMinUrl(final String minUrl) {
        this.minUrl = minUrl;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(final String url) {
        this.url = url;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(final String title) {
        this.title = title;
    }
}
