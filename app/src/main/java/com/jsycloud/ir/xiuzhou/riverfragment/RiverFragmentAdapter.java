package com.jsycloud.ir.xiuzhou.riverfragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jsycloud.ir.xiuzhou.Constant;
import com.jsycloud.ir.xiuzhou.DialogShow;
import com.jsycloud.ir.xiuzhou.R;

import java.util.List;

public class RiverFragmentAdapter extends BaseAdapter{

    Context context;
    LayoutInflater inflater;
    List<TabRiverFragment.criticism> criticismList;
    List<TabRiverFragment.report> assignList;
    List<TabRiverFragment.report> reportList;

    public RiverFragmentAdapter(Context context,List<TabRiverFragment.criticism> criticismList,List<TabRiverFragment.report> assignList,List<TabRiverFragment.report> reportList){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.criticismList = criticismList;
        this.assignList = assignList;
        this.reportList = reportList;
    }

    @Override
    public int getCount() {
        if(Constant.isLogByCode) {
            return 0;
        }else{
            return criticismList.size() + assignList.size() + reportList.size() + 2;
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
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < criticismList.size()) {
            return 0;
        } else if (position == criticismList.size()) {
            return 1;
        } else if (position < assignList.size() + criticismList.size() +1) {
            return 2;
        } else if (position == assignList.size() + criticismList.size() +1) {
            return 3;
        }else {
            return 4;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position < criticismList.size()) {
            View curView = inflater.inflate(R.layout.criticism_item, parent, false);
            TextView criticism_item_content = (TextView) curView.findViewById(R.id.criticism_item_content);
            TextView criticism_item_time = (TextView) curView.findViewById(R.id.criticism_item_time);
            criticism_item_content.setText(criticismList.get(position).getContent());
            criticism_item_time.setText(criticismList.get(position).getTime());
            return curView;
        } else if (position == criticismList.size()) {
            View curView = inflater.inflate(R.layout.assign_text, parent, false);
            TextView assign_text = (TextView) curView.findViewById(R.id.assign_text);
            assign_text.setText("交办：");
            return curView;
        } else if (position < assignList.size() + criticismList.size() +1) {
            final int curPos = position - criticismList.size() - 1;
            View curView = inflater.inflate(R.layout.assign_text, parent, false);
            TextView assign_text = (TextView) curView.findViewById(R.id.assign_text);
            assign_text.setText("你有一条来至" + assignList.get(curPos).getUsername() + "的交办信息（点击查看详情）");
            curView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogShow.dialogShow5(context, assignList.get(curPos).getRivername(), assignList.get(curPos).getDescribe(),
                            assignList.get(curPos).getAddress(),assignList.get(curPos).getTime(),assignList.get(curPos).getStatus());
                }
            });
            return curView;
        } else if (position == assignList.size() + criticismList.size() +1) {
            View curView = inflater.inflate(R.layout.assign_text, parent, false);
            TextView assign_text = (TextView) curView.findViewById(R.id.assign_text);
            assign_text.setText("上报：");
            return curView;
        }else {
            View curView = inflater.inflate(R.layout.assign_text, parent, false);
            final int curPos = position - criticismList.size() - assignList.size() -2;
            TextView assign_text = (TextView) curView.findViewById(R.id.assign_text);
            assign_text.setText("你有一条来至" + reportList.get(curPos).getUsername() + "的上报信息（点击查看详情）");
            curView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogShow.dialogShow5(context, reportList.get(curPos).getRivername(), reportList.get(curPos).getDescribe(),
                            reportList.get(curPos).getAddress(), reportList.get(curPos).getTime(), reportList.get(curPos).getStatus());
                }
            });
            return curView;
        }
    }
}
