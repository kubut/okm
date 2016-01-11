package com.example.OKM.presentation.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.OKM.R;
import com.example.OKM.domain.valueObject.CacheAttributeValue;

import java.util.ArrayList;

/**
 * Created by Jakub on 24.12.2015.
 */
public class AttributesListAdapter extends ArrayAdapter<CacheAttributeValue> {
    private final Context context;

    public AttributesListAdapter(final Context context, final ArrayList<CacheAttributeValue> itemsList){
        super(context, R.layout.attribute_item, itemsList);
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if(convertView == null){
            final LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.attribute_item, parent, false);
        }

        final Typeface font = Typeface.createFromAsset(this.getContext().getAssets(), "fontawesome-webfont.ttf");

        final TextView title = (TextView) convertView.findViewById(R.id.attributeName);
        final TextView icon = (TextView) convertView.findViewById(R.id.attributeIcon);
        final CacheAttributeValue item = this.getItem(position);

        title.setText(item.getName());
        icon.setText(item.getIcon());
        icon.setTypeface(font);

        return convertView;
    }
}
