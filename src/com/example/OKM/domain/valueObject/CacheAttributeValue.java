package com.example.OKM.domain.valueObject;

import android.content.Context;
import com.example.OKM.R;

/**
 * Created by Jakub on 20.12.2015.
 */
public class CacheAttributeValue {
    private String acode, iconName, name, language;
    private int icon;
    private long id;

    public CacheAttributeValue(Context context, long id, String acode){
        this.acode = acode;
        this.iconName = "cache_attr_" + acode;
        this.id = id;

        int iconIdentifier = context.getResources().getIdentifier(this.iconName, "drawable", context.getPackageName());
    }

    public String getAcode() {
        return acode;
    }
    public void setAcode(String acode) {
        this.acode = acode;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public int getIcon() {
        return icon;
    }
    public void setIcon(int icon) {
        this.icon = icon;
    }
    public String getIconName() {
        return iconName;
    }
    public void setIconName(String iconName) {
        this.iconName = iconName;
    }
    public long getId() {
        return id;
    }
}
