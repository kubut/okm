package com.opencachingkubutmaps.domain.valueObject;

import android.content.Context;
import com.opencachingkubutmaps.R;

/**
 * Created by Jakub on 29.09.2015.
 */
public class CacheSizeValue {
    private final String name;

    public CacheSizeValue(final Context context, final String key){
        final int nameIdentifier = context.getResources().getIdentifier("size_"+key, "string", context.getPackageName());

        if(nameIdentifier != 0){
            this.name = context.getString(nameIdentifier);
        } else {
            this.name = context.getString(R.string.size_other);
        }
    }

    public String getName() {
        return this.name;
    }

}
