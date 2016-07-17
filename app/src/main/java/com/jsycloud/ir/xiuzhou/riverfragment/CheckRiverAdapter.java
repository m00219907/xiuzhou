package com.jsycloud.ir.xiuzhou.riverfragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jsycloud.ir.xiuzhou.R;

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
        View curView = convertView;
        if(curView == null){
            curView = mInflater.inflate(R.layout.allriver_adapter, parent, false);
        }
        viewHolder curViewHolder = (viewHolder)curView.getTag();
        if (curViewHolder == null) {
            curViewHolder = new viewHolder();
        }

        curViewHolder.allriver_adaper_rivername = (TextView)curView.findViewById(R.id.allriver_adaper_rivername);
        curViewHolder.allriver_adaper_rivername.setText(allHistory.get(position).getRiver());

        curViewHolder.allriver_adaper_riveraddress = (TextView)curView.findViewById(R.id.allriver_adaper_riveraddress);
        curViewHolder.allriver_adaper_riveraddress.setText(allHistory.get(position).getAddress());

        curViewHolder.allriver_adaper_discribe = (TextView)curView.findViewById(R.id.allriver_adaper_discribe);
        curViewHolder.allriver_adaper_discribe.setText(allHistory.get(position).getDescribe());

        curViewHolder.allriver_adaper_time = (TextView)curView.findViewById(R.id.allriver_adaper_time);
        curViewHolder.allriver_adaper_time.setText(allHistory.get(position).getTimertp());

        return curView;
    }

    public class viewHolder{
        public TextView allriver_adaper_rivername;
        public TextView allriver_adaper_riveraddress;
        public TextView allriver_adaper_discribe;
        public TextView allriver_adaper_time;
    }
}

