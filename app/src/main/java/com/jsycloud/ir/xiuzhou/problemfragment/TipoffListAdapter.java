package com.jsycloud.ir.xiuzhou.problemfragment;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class TipoffListAdapter extends BaseAdapter{

    Context context;
    public List<TipoffListActivity.tipoffItem> tipoffList = new ArrayList<TipoffListActivity.tipoffItem>();

    public TipoffListAdapter(Context context, List<TipoffListActivity.tipoffItem> tipoffList){
        this.context = context;
        this.tipoffList = tipoffList;
    }

    @Override
    public int getCount() {
        return tipoffList.size();
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
        return null;
    }
}
