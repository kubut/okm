package com.example.OKM.domain.valueObject;

import android.content.Context;
import com.example.OKM.R;

/**
 * Created by kubut on 2015-09-15.
 */
public class CacheTypeValue {
    private final String name;
    private final int icon;

    public CacheTypeValue(final Context context, final String key){
        final String drawableName = "cache_icon_" + key.toLowerCase();

        final int iconIdentifier = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
        final int nameIdentifier = context.getResources().getIdentifier(key, "string", context.getPackageName());

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

    public int getIcon() {
        return this.icon;
    }

    public String getName() {

        return this.name;
    }
}
