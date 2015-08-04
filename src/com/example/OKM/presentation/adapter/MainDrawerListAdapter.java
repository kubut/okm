package com.example.OKM.presentation.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
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

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.main_drawer_item, parent, false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.drawerItemTitle);
        ImageView icon = (ImageView) convertView.findViewById(R.id.drawerItemIcon);

        IMainDrawerItem item = getItem(position);

        title.setText(item.getTitle());
        icon.setImageDrawable(ContextCompat.getDrawable(context, item.getIcon()));

        if(item.isActive()){
            color = context.getResources().getColor(R.color.textColorPrimary);
        } else {
            color = context.getResources().getColor(R.color.textColorPrimaryInactive);
        }

        title.setTextColor(color);
        ContextCompat.getDrawable(context, item.getIcon()).setColorFilter(new
                PorterDuffColorFilter(color,PorterDuff.Mode.SRC_ATOP));

        return convertView;
    }
}
