package com.example.OKM.domain.valueObject;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import com.example.OKM.R;

import java.util.Date;

/**
 * Created by Jakub on 24.12.2015.
 */
public class CacheLogValue {
    private Date date;
    private String user;
    private String comment;
    private int color;
    private final Context context;

    public CacheLogValue(final Context context){
        this.context = context;
    }

    public void setType(final String type) {
        switch (type){
            case "Found it":
                this.color = ContextCompat.getColor(this.context, R.color.cacheFound);
                break;
            case "Didn't find it":
                this.color = ContextCompat.getColor(this.context, R.color.cacheNotFound);
                break;
            default:
                this.color = ContextCompat.getColor(this.context, R.color.cacheOther);
                break;
        }
    }

    public int getColor(){
        return this.color;
    }
    public Date getDate() {
        return this.date;
    }
    public void setDate(final Date date) {
        this.date = date;
    }
    public String getUser() {
        return this.user;
    }
    public void setUser(final String user) {
        this.user = user;
    }

    public String getComment() {
        return this.comment;
    }
    public void setComment(final String comment) {
        this.comment = comment;
    }
}
