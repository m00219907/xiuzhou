package com.jsycloud.rs.xiuzhou.riverfragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jsycloud.rs.xiuzhou.R;

import java.util.List;

public class CheckRiverAdapter  extends BaseAdapter {

    LayoutInflater mInflater;
    List<CheckRiverHistory.HistoryItem> allHistory;
    public CheckRiverAdapter(Context context, List<CheckRiverHistory.HistoryItem> allHistory){
        mInflater = LayoutInflater.from(context);
        this.allHistory = allHistory;
    }

    @Override
    public int getCount() {
        return allHistory.size();
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
        textView.setText(allHistory.get(position).getAddress());
        return view;
    }
}

