package com.example.OKM.presentation.view;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.OKM.R;
import com.example.OKM.domain.model.CacheModel;
import com.example.OKM.presentation.adapter.LogsListAdapter;

public class CacheLogsFragment extends Fragment implements ICacheTabs{
    private ListView logsView;
    private boolean ready, loaded;
    private ArrayAdapter adapter;

    public CacheLogsFragment(){
        this.ready = false;
        this.loaded = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_cache_logs, container, false);

        this.logsView = (ListView) view.findViewById(R.id.cache_logs);

        this.ready = true;
        this.syncView();

        return view;
    }

    @Override
    public void syncView() {
        if(this.loaded && this.ready){
            this.logsView.setAdapter(this.adapter);
        }
    }

    @Override
    public void setView(Context context, CacheModel cacheModel) {
        this.loaded = true;
        this.adapter = new LogsListAdapter(context, cacheModel.getLogs());

        this.syncView();
    }
}
