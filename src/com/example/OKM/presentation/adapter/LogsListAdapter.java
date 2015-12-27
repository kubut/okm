package com.example.OKM.presentation.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.OKM.R;
import com.example.OKM.data.services.URLImageParser;
import com.example.OKM.domain.valueObject.CacheLogValue;

import java.util.ArrayList;

/**
 * Created by Jakub on 24.12.2015.
 */
public class LogsListAdapter extends ArrayAdapter<CacheLogValue> {
    private Context context;

    public LogsListAdapter(Context context, ArrayList<CacheLogValue> itemsList){
        super(context, R.layout.log_item, itemsList);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.log_item, parent, false);
        }

        CardView entry = (CardView) convertView.findViewById(R.id.log_entry);
        TextView comment = (TextView) convertView.findViewById(R.id.log_comment);
        CacheLogValue item = getItem(position);

        URLImageParser urlImageParser = new URLImageParser(comment, this.context, item);
        comment.setText(Html.fromHtml(item.getComment(), urlImageParser, null));
        entry.setBackgroundColor(item.getCacheColor());

        return convertView;
    }
}
