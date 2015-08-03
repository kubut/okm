package com.example.OKM.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.main_drawer_item, parent, false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.drawerItemTitle);

        IMainDrawerItem item = getItem(position);

        title.setText(item.getTitle());

        if(item.isActive()){
            title.setTextColor(context.getResources().getColor(R.color.textColorPrimary));
        } else {
            title.setTextColor(context.getResources().getColor(R.color.textColorPrimaryInactive));
        }

        return convertView;
    }
}
