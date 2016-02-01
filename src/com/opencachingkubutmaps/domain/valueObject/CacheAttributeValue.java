package com.opencachingkubutmaps.domain.valueObject;

import android.content.Context;

/**
 * Created by Jakub on 20.12.2015.
 */
public class CacheAttributeValue {
    private final String acode;
    private String name;
    private String language;
    private final int icon;
    private final long id;

    public CacheAttributeValue(final Context context, final long id, final String acode){
        this.acode = acode;
        final String iconName = "cache_attr_" + acode;
        this.id = id;

        this.icon = context.getResources().getIdentifier(iconName, "string", context.getPackageName());
    }

    public String getAcode() {
        return this.acode;
    }

    public String getName() {
        return this.name;
    }
    public void setName(final String name) {
        this.name = name;
    }
    public String getLanguage() {
        return this.language;
    }
    public void setLanguage(final String language) {
        this.language = language;
    }
    public int getIcon() {
        return this.icon;
    }

    public long getId() {
        return this.id;
    }
}
