package com.example.OKM.presentation.adapter;

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
import com.example.OKM.R;
import com.example.OKM.domain.model.IMainDrawerItem;

import java.util.ArrayList;

/**
 * Created by kubut on 2015-08-02.
 */
public class MainDrawerListAdapter extends ArrayAdapter<IMainDrawerItem>{
    private Context context;

    public MainDrawerListAdapter(Context context, ArrayList<IMainDrawerItem> itemsList){
        super(context, R.layout.main_drawer_item, itemsList);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int color;
        Drawable drawable;
        PorterDuffColorFilter filter;
        IMainDrawerItem item;
        TextView title;
        ImageView icon;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.main_drawer_item, parent, false);
        }

        title = (TextView) convertView.findViewById(R.id.drawerItemTitle);
        icon = (ImageView) convertView.findViewById(R.id.drawerItemIcon);
        item = getItem(position);

        if(item.isActive()){
            color = context.getResources().getColor(R.color.textColorPrimary);
        } else {
            color = context.getResources().getColor(R.color.textColorPrimaryInactive);
        }

        title.setText(item.getTitle());
        icon.setImageDrawable(ContextCompat.getDrawable(context, item.getIcon()));
        title.setTextColor(color);
        drawable = ContextCompat.getDrawable(context, item.getIcon());
        filter = new PorterDuffColorFilter(color,PorterDuff.Mode.SRC_ATOP);
        drawable.setColorFilter(filter);

        return convertView;
    }
}
