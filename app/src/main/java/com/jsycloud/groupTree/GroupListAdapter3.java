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
        View view = this.lif.inflate(R.layout.group_list_item, null);

        RelativeLayout rltItem = (RelativeLayout) view.findViewById(R.id.group_item_rlt);
        ImageView ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
        TextView tvText = (TextView) view.findViewById(R.id.tvText);
        ImageView ivExEc = (ImageView) view.findViewById(R.id.ivExEc);
        CheckBox chbSelect = (CheckBox) view.findViewById(R.id.chbSelect);

        // item单击事件
        rltItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TreeNode n = (TreeNode) v.getTag();
                if (n.getType() == 3 && n.getParent().getDeviceInfo() != null && n.getParent().getDeviceInfo().getStatus() != 1) {  //离线状态
                    return;
                }
                //判断通道是否在线
                if (n.getType() == 3 && n.getChannelInfo().getState() != 1) {
                    return;
                }

                onItemClickListener.onItemClick3(n, !n.isChecked(), position);
            }
        });

        TreeNode n = alls.get(position);
        tvText.setText(n.getText() + " ");
        rltItem.setTag(n);
        chbSelect.setTag(n);
        chbSelect.setChecked(n.isChecked());
        ivExEc.setImageResource(R.drawable.open_content_selector);
        return view;
    }

    public interface IOnItemClickListener {
        public void onItemClick3(final TreeNode treeNode, final boolean isChecked, final int position);
    }
}
