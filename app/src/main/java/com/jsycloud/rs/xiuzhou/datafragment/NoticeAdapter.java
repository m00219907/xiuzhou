package com.jsycloud.rs.xiuzhou.datafragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jsycloud.rs.xiuzhou.R;

import java.util.List;

public class NoticeAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    List<TabDataFragment.noticeItem> allNotice;

    public NoticeAdapter(Context context, List<TabDataFragment.noticeItem> allNotice){
        mInflater = LayoutInflater.from(context);
        this.allNotice = allNotice;
    }
    @Override
    public int getCount() {
        return allNotice.size();
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
            curView = mInflater.inflate(R.layout.notice_adapter, parent, false);
        }
        noticeViewHolder noticeHolder = (noticeViewHolder)curView.getTag();
        if (noticeHolder == null) {
            noticeHolder = new noticeViewHolder();
        }

        noticeHolder.notice_adapter_tittle = (TextView)curView.findViewById(R.id.notice_adapter_tittle);
        noticeHolder.notice_adapter_tittle.setText(allNotice.get(position).getTitle());

        noticeHolder.notice_adapter_content = (TextView)curView.findViewById(R.id.notice_adapter_content);
        noticeHolder.notice_adapter_content.setText(allNotice.get(position).getContent());

        noticeHolder.notice_adapter_time = (TextView)curView.findViewById(R.id.notice_adapter_time);
        noticeHolder.notice_adapter_time.setText(allNotice.get(position).getTime());

        noticeHolder.notice_adapter_user = (TextView)curView.findViewById(R.id.notice_adapter_user);
        noticeHolder.notice_adapter_user.setText(allNotice.get(position).getUser());

        return curView;
    }

    public final class noticeViewHolder{
        public TextView notice_adapter_tittle;
        public TextView notice_adapter_content;
        public TextView notice_adapter_time;
        public TextView notice_adapter_user;
    }
}
