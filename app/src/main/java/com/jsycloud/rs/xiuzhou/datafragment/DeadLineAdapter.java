package com.jsycloud.rs.xiuzhou.datafragment;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jsycloud.rs.xiuzhou.R;

import java.util.List;

public class DeadLineAdapter extends BaseAdapter{

    LayoutInflater mInflater;
    List<String> deadLineInfo;

    public DeadLineAdapter(Context context, List<String> deadLineInfo){
        mInflater = LayoutInflater.from(context);
        this.deadLineInfo = deadLineInfo;
    }
    @Override
    public int getCount() {
        return deadLineInfo.size();
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
        textView.setText(deadLineInfo.get(position));
        return view;
    }
}
