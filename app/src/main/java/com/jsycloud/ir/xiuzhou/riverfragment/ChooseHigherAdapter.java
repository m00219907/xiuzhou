package com.jsycloud.ir.xiuzhou.riverfragment;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jsycloud.ir.xiuzhou.R;

public class ChooseHigherAdapter extends BaseAdapter{

    Context context;
    boolean bHigher;

    public ChooseHigherAdapter(Context context, boolean bHigher){
        this.context = context;
        this.bHigher = bHigher;
    }

    @Override
    public int getCount() {
        if(bHigher) {
            return CheckRiverActivity.higherList.size();
        }else{
            return CheckRiverActivity.lowerList.size();
        }
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
        View curView = LayoutInflater.from(context).inflate(R.layout.choose_higher_item, parent, false);
        TextView assign_text = (TextView) curView.findViewById(R.id.choose_higher_text);
        if(bHigher) {
            assign_text.setText(CheckRiverActivity.higherList.get(position).getUserfullname());
        }else{
            assign_text.setText(CheckRiverActivity.lowerList.get(position).getUserfullname());
        }
        return curView;
    }
}
