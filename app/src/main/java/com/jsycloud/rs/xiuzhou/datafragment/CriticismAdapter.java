package com.jsycloud.rs.xiuzhou.datafragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jsycloud.rs.xiuzhou.R;

import java.util.List;

public class CriticismAdapter extends BaseAdapter{

    LayoutInflater mInflater;
    List<TabDataFragment.criticismItem> allCriticism;

    public CriticismAdapter(Context context, List<TabDataFragment.criticismItem> allCriticism){
        mInflater = LayoutInflater.from(context);
        this.allCriticism = allCriticism;
    }
    @Override
    public int getCount() {
        return allCriticism.size();
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
            curView = mInflater.inflate(R.layout.criticism_adapter, parent, false);
        }
        criticismViewHolder criticismHolder = (criticismViewHolder)curView.getTag();
        if (criticismHolder == null) {
            criticismHolder = new criticismViewHolder();
        }

        criticismHolder.criticism_adapter_content = (TextView)curView.findViewById(R.id.criticism_adapter_content);
        criticismHolder.criticism_adapter_content.setText(allCriticism.get(position).getContent());

        criticismHolder.criticism_adapter_time = (TextView)curView.findViewById(R.id.criticism_adapter_time);
        criticismHolder.criticism_adapter_time.setText(allCriticism.get(position).getTime());

        criticismHolder.criticism_adapter_user = (TextView)curView.findViewById(R.id.criticism_adapter_user);
        criticismHolder.criticism_adapter_user.setText(allCriticism.get(position).getUser());

        return curView;
    }

    public final class criticismViewHolder{
        public TextView criticism_adapter_content;
        public TextView criticism_adapter_time;
        public TextView criticism_adapter_user;
    }
}
