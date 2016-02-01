package com.opencachingkubutmaps.presentation.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.opencachingkubutmaps.R;
import com.opencachingkubutmaps.domain.model.IMainDrawerItem;

import java.util.ArrayList;

/**
 * Created by kubut on 2015-08-02.
 */
public class MainDrawerListAdapter extends ArrayAdapter<IMainDrawerItem>{
    private final Context context;

    public MainDrawerListAdapter(final Context context, final ArrayList<IMainDrawerItem> itemsList){
        super(context, R.layout.main_drawer_item, itemsList);
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final int color;
        final Drawable drawable;
        final PorterDuffColorFilter filter;
        final IMainDrawerItem item;
        final TextView title;
        final ImageView icon;

        if(convertView == null){
            final LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.main_drawer_item, parent, false);
        }

        title = (TextView) convertView.findViewById(R.id.drawerItemTitle);
        icon = (ImageView) convertView.findViewById(R.id.drawerItemIcon);
        item = this.getItem(position);

        if(item.isActive()){
            color = ContextCompat.getColor(this.context, R.color.textColorPrimary);
        } else {
            color = ContextCompat.getColor(this.context, R.color.textColorPrimaryInactive);
        }

        title.setText(item.getTitle());
        icon.setImageDrawable(ContextCompat.getDrawable(this.context, item.getIcon()));
        title.setTextColor(color);
        drawable = ContextCompat.getDrawable(this.context, item.getIcon());
        filter = new PorterDuffColorFilter(color,PorterDuff.Mode.SRC_ATOP);
        drawable.setColorFilter(filter);

        return convertView;
    }
}
