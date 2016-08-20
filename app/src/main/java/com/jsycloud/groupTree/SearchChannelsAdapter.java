package com.jsycloud.groupTree;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsycloud.ir.xiuzhou.R;
import com.jsycloud.groupTree.bean.ChannelInfoExt;



public class SearchChannelsAdapter extends BaseAdapter {

    private List<ChannelInfoExt> mSearchList;

    private final Context mContext;

    private LayoutInflater mInflater = null;

    private IOnSearchChannelsClick onSearchChannelsClick;

    // 是否拥有复选框
    private boolean hasCheckBox = false;
    
    private static final String TAG = "SearchChannelsAdapter";

    public SearchChannelsAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setItemList(List<ChannelInfoExt> selectedList) {
        this.mSearchList = selectedList;
    }

    public void setListner(IOnSearchChannelsClick onSearchChannelsClick) {
        this.onSearchChannelsClick = onSearchChannelsClick;

    }

    @Override
    public int getCount() {
        if (mSearchList != null) {
            return mSearchList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (mSearchList != null) {
            return mSearchList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.group_list_search_item, null);
            viewHolder.searchRlt = (RelativeLayout) convertView.findViewById(R.id.grouplist_search_rlt);
            viewHolder.channelNameTv = (TextView) convertView.findViewById(R.id.channel_name_tv);
            viewHolder.devNameTv = (TextView) convertView.findViewById(R.id.dev_name_tv);
            viewHolder.searchCb = (CheckBox) convertView.findViewById(R.id.search_channel_cb);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final ChannelInfoExt mChannelInfoExt = mSearchList.get(position);
        if (mChannelInfoExt != null) {
            viewHolder.channelNameTv.setText(mChannelInfoExt.getSzName());
            viewHolder.devNameTv.setText(mChannelInfoExt.getDeviceName());
            Log.i(TAG, "clicked channel device name " + mChannelInfoExt.getSzName() + mChannelInfoExt.getDeviceName());
            // 是否在线
            if (mChannelInfoExt.getState() == 1) {
                viewHolder.channelNameTv.setTextColor(mContext.getResources().getColor(R.color.black));
                viewHolder.devNameTv.setTextColor(mContext.getResources().getColor(R.color.black));
                viewHolder.searchCb.setVisibility(View.VISIBLE);
            } else {
                viewHolder.channelNameTv.setTextColor(mContext.getResources().getColor(R.color.gray));
                viewHolder.devNameTv.setTextColor(mContext.getResources().getColor(R.color.gray));
                viewHolder.searchCb.setVisibility(View.GONE);
            }
            viewHolder.searchRlt.setTag(position);
            viewHolder.searchCb.setTag(position);
            viewHolder.searchCb.setChecked(mChannelInfoExt.isSelected());
            if (!hasCheckBox) {
                viewHolder.searchCb.setVisibility(View.GONE);
            }

            // 复选框单击事件
            viewHolder.searchCb.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    int n = (Integer) v.getTag();
                    mSearchList.get(n).setSelected(!mSearchList.get(n).isSelected());
                    SearchChannelsAdapter.this.notifyDataSetChanged();
                    onSearchChannelsClick.onSearchChannelsClick(mChannelInfoExt, false);
                }
            });
            viewHolder.searchRlt.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hasCheckBox) {
                        int n = (Integer) v.getTag();
                        mSearchList.get(n).setSelected(!mSearchList.get(n).isSelected());
                        SearchChannelsAdapter.this.notifyDataSetChanged();
                        onSearchChannelsClick.onSearchChannelsClick(mChannelInfoExt, false);
                    } else {
                        onSearchChannelsClick.onSearchChannelsClick(mChannelInfoExt, true);
                    }
                }
            });

        }

        return convertView;
    }

    /**
     * 获得选中列表
     * 
     * @return
     */
    public List<ChannelInfoExt> getSearchList() {
        if (mSearchList == null || mSearchList.size() == 0) {
            return null;
        }
        List<ChannelInfoExt> nodes = new ArrayList<ChannelInfoExt>();
        for (int i = 0; i < mSearchList.size(); i++) {
            ChannelInfoExt n = mSearchList.get(i);
            if (n.isSelected()) {
                nodes.add(n);
            }
        }
        return nodes;
    }

    private static class ViewHolder {
        private RelativeLayout searchRlt;

        public TextView channelNameTv;

        public TextView devNameTv;

        public CheckBox searchCb;
    }

    public interface IOnSearchChannelsClick {
        public void onSearchChannelsClick(final ChannelInfoExt ChannelInfoExt, boolean flag);
    }

    public boolean isHasCheckBox() {
        return hasCheckBox;
    }

    public void setHasCheckBox(boolean hasCheckBox) {
        this.hasCheckBox = hasCheckBox;
    }

}
