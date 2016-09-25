package com.jsycloud.groupTree;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsycloud.groupTree.bean.TreeNode;
import com.jsycloud.ir.xiuzhou.R;

import java.util.ArrayList;
import java.util.List;

public class GroupListAdapter2 extends BaseAdapter {

    private final Context con;
    private final LayoutInflater lif;
    private final List<TreeNode> alls = new ArrayList<TreeNode>();

    private IOnItemClickListener onItemClickListener;

    public GroupListAdapter2(Context context) {
        this.con = context;
        this.lif = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setListner( IOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void addNode(TreeNode node) {
        if (node == null) {
            return;
        }
        if (node.getType() == 3) {
            TreeNode tempNode = node;
            while (tempNode.getParent() != null) {
                if (tempNode.getParent().getText().equals("秀洲区治水办")) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
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
        camera_item_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TreeNode n = alls.get(position);
                onItemClickListener.onItemClick(n, !n.isChecked(), position);
            }
        });

        // 得到当前节点
        TreeNode n = alls.get(position);

        return curView;
    }

    public interface IOnItemClickListener {
        public void onItemClick(final TreeNode treeNode, final boolean isChecked, final int position);
    }
}
