package com.opencachingkubutmaps.domain.valueObject;

import android.content.Context;
import com.opencachingkubutmaps.R;

/**
 * Created by kubut on 2015-09-15.
 */
public class CacheTypeValue {
    private final String name;
    private final int icon;
    private final int selectedIcon;

    public CacheTypeValue(final Context context, final String key){
        final String drawableName = "cache_icon_" + key.toLowerCase();

        final int iconIdentifier = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
        final int selectedIconIdentifier = context.getResources().getIdentifier(drawableName+"_sel", "drawable", context.getPackageName());
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

        if(selectedIconIdentifier != 0){
            this.selectedIcon = selectedIconIdentifier;
        } else {
            this.selectedIcon = R.drawable.cache_icon_other_sel;
        }
    }

    public int getIcon() {
        return this.icon;
    }

    public int getSelectedIcon(){
        return this.selectedIcon;
    }

    public String getName() {

        return this.name;
    }
}
