package com.jsycloud.rs.xiuzhou.mapfragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jsycloud.rs.xiuzhou.R;

import java.util.List;

public class AllRiverAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    List<String> riverNames;
    public AllRiverAdapter(Context context, List<String> riverNames){
        mInflater = LayoutInflater.from(context);
        this.riverNames = riverNames;
    }

    @Override
    public int getCount() {
        return riverNames.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.allriver_adapter, parent, false);
        TextView textView = (TextView)view.findViewById(R.id.allriver_rivername);
        textView.setText(riverNames.get(position));
        return view;
    }
}
