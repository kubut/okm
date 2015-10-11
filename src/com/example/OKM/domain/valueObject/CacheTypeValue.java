package com.example.OKM.domain.valueObject;

import android.content.Context;
import com.example.OKM.R;

/**
 * Created by kubut on 2015-09-15.
 */
public class CacheTypeValue {
    private String name, key, drawableName;
    private int icon;

    public CacheTypeValue(Context context, String key){
        this.key = key;
        this.drawableName = "cache_icon_" + key.toLowerCase();

        int iconIdentifier = context.getResources().getIdentifier(this.drawableName, "drawable", context.getPackageName());
        int nameIdentifier = context.getResources().getIdentifier(key, "string", context.getPackageName());

        if(nameIdentifier != 0){
            this.name = context.getString(nameIdentifier);
        } else {
            this.name = context.getString(R.string.Other);
        }

        if(iconIdentifier != 0){
            this.icon = iconIdentifier;
        } else {
            this.icon = R.drawable.cache_icon_other;
        }
    }

    public String getKey() {
        return key;
    }

    public String getDrawableName() {
        return drawableName;
    }

    public int getIcon() {
        return icon;
    }

    public String getName() {

        return name;
    }
}
