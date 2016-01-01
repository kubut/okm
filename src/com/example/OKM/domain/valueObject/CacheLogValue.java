package com.example.OKM.domain.valueObject;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import com.example.OKM.R;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Jakub on 24.12.2015.
 */
public class CacheLogValue {
    private Date date;
    private String user, type, comment;
    private int color;
    private Context context;

    public CacheLogValue(Context context){
        this.context = context;
    }

    public void setType(String type) {
        this.type = type;

        switch (type){
            case "Found it":
                this.color = this.context.getResources().getColor(R.color.cacheFound);
                break;
            case "Didn't find it":
                this.color = this.context.getResources().getColor(R.color.cacheNotFound);
                break;
            default:
                this.color = this.context.getResources().getColor(R.color.cacheOther);
                break;
        }
    }

    public int getColor(){
        return this.color;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getType() {
        return type;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
}
