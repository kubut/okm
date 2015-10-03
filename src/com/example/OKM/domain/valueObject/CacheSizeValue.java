package com.example.OKM.domain.valueObject;

import android.content.Context;
import com.example.OKM.R;

/**
 * Created by Jakub on 29.09.2015.
 */
public class CacheSizeValue {
    private String name, key;
    private int symbol;

    public CacheSizeValue(Context context, String key){
        int nameIdentifier = context.getResources().getIdentifier("size_"+key, "string", context.getPackageName());
        int symbolIdentifier = context.getResources().getIdentifier("size_symbol_"+key, "string", context.getPackageName());


        if(nameIdentifier != 0){
            this.name = context.getString(nameIdentifier);
        } else {
            this.name = context.getString(R.string.size_other);
        }

        if(symbolIdentifier != 0){
            this.symbol = Integer.parseInt(context.getString(symbolIdentifier));
        } else {
            this.symbol = Integer.parseInt(context.getString(R.string.size_symbol_other));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getSymbol() {
        return symbol;
    }

    public void setSymbol(int symbol) {
        this.symbol = symbol;
    }
}
