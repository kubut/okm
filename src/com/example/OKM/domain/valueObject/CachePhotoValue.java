package com.example.OKM.domain.valueObject;

/**
 * Created by Jakub on 01.01.2016.
 */
public class CachePhotoValue {
    private String minUrl, url, title;
    private boolean spoiler;

    public boolean isSpoiler() {
        return spoiler;
    }
    public void setSpoiler(boolean spoiler) {
        this.spoiler = spoiler;
    }
    public String getMinUrl() {
        return minUrl;
    }
    public void setMinUrl(String minUrl) {
        this.minUrl = minUrl;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}
