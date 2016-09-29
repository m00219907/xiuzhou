package com.jsycloud.groupTree;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jsycloud.activity.RealPlayActivity;
import com.jsycloud.groupTree.bean.ChannelInfoExt;
import com.jsycloud.ir.xiuzhou.R;
import com.jsycloud.groupTree.bean.TreeNode;


public class GroupListAdapter extends BaseAdapter {

    private final Context con;

    private final LayoutInflater lif;

    private final List<TreeNode> allsCache = new ArrayList<TreeNode>();

    private final List<TreeNode> alls = new ArrayList<TreeNode>();

    public GroupListAdapter(Context context) {
        this.con = context;
        this.lif = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addNode(TreeNode node) {
        if (node == null) {
            return;
        }
        if (node.getType() == 3) {
            TreeNode tempNode = node;
            while (tempNode.getParent() != null) {
                if (tempNode.getParent().getText().equals("区建设")) {
                    alls.add(node);
                    break;
                }
                tempNode = tempNode.getParent();
            }
        }
        for (int i = 0; i < node.getChildren().size(); i++) {
            addNode(node.getChildren().get(i));
        }
    }

    public void clearDate() {
        alls.clear();
        allsCache.clear();
    }

    public void clearNode() {
        alls.clear();
    }

    @Override
    public int getCount() {
        return alls.size();
    }

    @Override
    public Object getItem(int position) {
        return alls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        View curView = this.lif.inflate(R.layout.cameralist_small_item, null);
        View camera_item_rl = curView.findViewById(R.id.camera_item_rl);
        TextView camera_river_name = (TextView) curView.findViewById(R.id.camera_river_name);
        TextView camera_town_name = (TextView) curView.findViewById(R.id.camera_town_name);
        String[] names = alls.get(position).getText().split("-");
        camera_river_name.setText(names[0]);
        if(names.length > 1){
            camera_town_name.setText(names[1].split("_")[0]);
        }else {
            camera_town_name.setVisibility(View.GONE);
        }
        camera_item_rl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TreeNode curNode = alls.get(position);

                ChannelInfoExt chnlInfoExt = curNode.getChannelInfo();
                String channelName =  chnlInfoExt.getSzName();
                String channelId = chnlInfoExt.getSzId();
                String deviceId = chnlInfoExt.getDeviceId();
                Intent intent = new Intent();
                intent.putExtra("channelName", channelName);
                intent.putExtra("channelId", channelId);
                intent.putExtra("deviceId", deviceId);
                intent.setClass(con, RealPlayActivity.class);
                con.startActivity(intent);
            }
        });

        return curView;
    }


}
