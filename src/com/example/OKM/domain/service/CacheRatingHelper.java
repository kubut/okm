package com.example.OKM.domain.service;

import android.content.Context;
import android.widget.TextView;
import com.example.OKM.R;

/**
 * Created by Jakub on 27.01.2016
 */
public class CacheRatingHelper {
    public static void setStars(final int rating, final Context context, final TextView ratingView){
        final StringBuilder stars = new StringBuilder();

        for(int i = 0; i < rating; i++){
            stars.append(context.getString(R.string.cache_star_full));
        }

        for(int i = 0; i < (5 - rating); i++){
            stars.append(context.getString(R.string.cache_star));
        }

        ratingView.setText(stars.toString());
    }
}
