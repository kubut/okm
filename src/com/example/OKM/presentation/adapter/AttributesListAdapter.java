package com.example.OKM.presentation.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.OKM.R;
import com.example.OKM.domain.model.IMainDrawerItem;
import com.example.OKM.domain.valueObject.CacheAttributeValue;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Jakub on 24.12.2015.
 */
public class AttributesListAdapter extends ArrayAdapter<CacheAttributeValue> {
    private Context context;

    public AttributesListAdapter(Context context, ArrayList<CacheAttributeValue> itemsList){
        super(context, R.layout.attribute_item, itemsList);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.attribute_item, parent, false);
        }

        Typeface font = Typeface.createFromAsset(this.getContext().getAssets(), "fontawesome-webfont.ttf");

        TextView title = (TextView) convertView.findViewById(R.id.attributeName);
//        ImageView icon = (ImageView) convertView.findViewById(R.id.attributeIcon);
        TextView icon = (TextView) convertView.findViewById(R.id.attributeIcon);
        CacheAttributeValue item = getItem(position);

        title.setText(item.getName());
        icon.setText(item.getIcon());
        icon.setTypeface(font);
//        icon.setImageResource(item.getIcon());

        return convertView;
    }
}
