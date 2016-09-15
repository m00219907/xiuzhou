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

public class GroupListAdapter3 extends BaseAdapter {

    private final Context con;
    private final LayoutInflater lif;
    private final List<TreeNode> alls = new ArrayList<TreeNode>();

    private IOnItemClickListener onItemClickListener;

    public GroupListAdapter3(Context context) {
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
                if (tempNode.getParent().getText().equals("嘉兴智慧河道")) {
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
        View  curView = this.lif.inflate(R.layout.cameralist_small_item, null);
        View camera_item_rl = curView.findViewById(R.id.camera_item_rl);
        TextView camera_name_tv = (TextView) curView.findViewById(R.id.camera_name_tv);
        camera_name_tv.setText(alls.get(position).getText());
        // item单击事件
        camera_item_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TreeNode n = alls.get(position);
                onItemClickListener.onItemClick3(n, !n.isChecked(), position);
            }
        });

        // 得到当前节点
        TreeNode n = alls.get(position);

        return curView;
    }

    public interface IOnItemClickListener {
        public void onItemClick3(final TreeNode treeNode, final boolean isChecked, final int position);
    }
}
