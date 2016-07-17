package com.jsycloud.ir.xiuzhou.datafragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jsycloud.ir.xiuzhou.R;

import java.util.List;

public class CriticismAdapter extends BaseAdapter{

    LayoutInflater mInflater;
    List<TabDataFragment.reportItem> allWaterReport;

    public CriticismAdapter(Context context, List<TabDataFragment.reportItem> allWaterReport){
        mInflater = LayoutInflater.from(context);
        this.allWaterReport = allWaterReport;
    }
    @Override
    public int getCount() {
        return allWaterReport.size();
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

        criticismHolder.criticism_adapter_tittle = (TextView)curView.findViewById(R.id.criticism_adapter_tittle);
        criticismHolder.criticism_adapter_tittle.setText(allWaterReport.get(position).getTittle());

        criticismHolder.criticism_adapter_content = (TextView)curView.findViewById(R.id.criticism_adapter_content);
        criticismHolder.criticism_adapter_content.setText(allWaterReport.get(position).getContent());

        criticismHolder.criticism_adapter_path = (TextView)curView.findViewById(R.id.criticism_adapter_path);
        criticismHolder.criticism_adapter_path.setText(allWaterReport.get(position).getFile());

        criticismHolder.criticism_adapter_time = (TextView)curView.findViewById(R.id.criticism_adapter_time);
        criticismHolder.criticism_adapter_time.setText(allWaterReport.get(position).getTime());

        return curView;
    }

    public final class criticismViewHolder{
        public TextView criticism_adapter_tittle;
        public TextView criticism_adapter_content;
        public TextView criticism_adapter_path;
        public TextView criticism_adapter_time;
    }
}
