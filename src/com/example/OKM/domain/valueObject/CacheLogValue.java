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
    private int cacheColor;
    private Context context;
    private HashMap<String, Drawable> drawables;

    public CacheLogValue(Context context){
        this.context = context;
        this.drawables = new HashMap<>();
    }

    public void addDrawable(String url, Drawable drawable){
        this.drawables.put(url, drawable);
    }

    @Nullable
    public Drawable getDrawableByUrl(String url){
        return this.drawables.get(url);
    }

    public void setType(String type) {
        this.type = type;

        switch (type){
            case "Found it":
                this.cacheColor = this.context.getResources().getColor(R.color.cacheFound);
                break;
            case "Didn't find it":
                this.cacheColor = this.context.getResources().getColor(R.color.cacheNotFound);
                break;
            default:
                this.cacheColor = this.context.getResources().getColor(R.color.cacheOther);
                break;
        }
    }

    public int getCacheColor(){
        return this.cacheColor;
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
