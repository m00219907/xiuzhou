package com.jsycloud.groupTree;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsycloud.ir.xiuzhou.R;
import com.jsycloud.groupTree.bean.TreeNode;


public class GroupListAdapter extends BaseAdapter {

    private final Context con;

    private final LayoutInflater lif;

    private final List<TreeNode> allsCache = new ArrayList<TreeNode>();

    private final List<TreeNode> alls = new ArrayList<TreeNode>();

    // 是否拥有复选框
    private boolean hasCheckBox = false;

    private IOnCheckBoxClick onCheckBoxClick;

    private IOnItemClickListener onItemClickListener;

    public void setListner(IOnCheckBoxClick onCheckBoxClick, IOnItemClickListener onItemClickListener) {
        this.onCheckBoxClick = onCheckBoxClick;
        this.onItemClickListener = onItemClickListener;
    }

    public GroupListAdapter(Context context) {
        this.con = context;
        this.lif = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addNode(TreeNode node) {
        if (node == null)
            return;
        if (node.getParent() != null && node.getParent().getText().equals("秀洲区治水办")) {
            alls.add(node);
            allsCache.add(node);
        }
        if (node.isLeaf())
            return;
        for (int i = 0; i < node.getChildren().size(); i++) {
            addNode(node.getChildren().get(i));
        }
    }

    public void setAllUnExpanded() {
        for (int i = 0; i < alls.size(); i++) {
            if (alls.get(i).isExpanded()) {
                alls.get(i).setExpanded(false);
            }
        }
    }

    public List<TreeNode> getSeletedNodes() {
        List<TreeNode> nodes = new ArrayList<TreeNode>();
        for (int i = 0; i < allsCache.size(); i++) {
            TreeNode n = allsCache.get(i);
            if (n.isChecked() && n.getType() == 3) {
                nodes.add(n);
            }
        }
        return nodes;
    }

    private void filterNode(TreeNode n) {
        alls.clear();
        if (n.isExpanded()) {
            if (n.getLevel() >= 3) {
                TreeNode node = new TreeNode("", "...");
                node.setType(1);
                node.setExpanded(true);
                alls.add(node);
            }
            if (n.getParent() != null && n.getParent().getLevel() > 0) {
                alls.add(n.getParent());
            }
            alls.add(n);
            // 把全部子节点设置成不展开
            List<TreeNode> tempList = n.getChildren();
            for (TreeNode treeNode : tempList) {
                treeNode.setExpanded(false);
            }
            alls.addAll(tempList);
        } else if (!n.isExpanded()) {
            if (n.getParent() != null && n.getParent().getParent() != null && n.getParent().getParent().getLevel() > 0) {
                if (n.getParent().getParent().getLevel() > 1) {
                    TreeNode node = new TreeNode("", "...");
                    node.setType(1);
                    node.setExpanded(true);
                    alls.add(node);
                }
                alls.add(n.getParent().getParent());
            }
            if (n.getParent() != null && n.getParent().getLevel() > 0) {
                alls.add(n.getParent());
            }
            alls.addAll(n.getParent().getChildren());
        }
    }

    public void setExpandLevel(int level) {
        alls.clear();
        for (int i = 0; i < allsCache.size(); i++) {
            TreeNode n = allsCache.get(i);
            if (n.getLevel() <= level) {
                // if (n.getLevel() == level) {// 上层都设置展开状态
                // n.setExpanded(true);
                // } else {// 最后一层都设置折叠状态
                // n.setExpanded(false);
                // }
                n.setExpanded(false);
                alls.add(n);
            }
        }
        this.notifyDataSetChanged();
    }

    public boolean ExpandOrCollapse(int position) {
        if (position > alls.size()) {
            return false;
        }
        TreeNode n = alls.get(position);
        // int allSize = n.getChildren().size();
        // List<TreeNode> list = getSeletedNodes();
        // if (list != null && list.size() > 0) {
        // allSize += list.size();
        // }
        // if (allSize > 32) {
        // return false;
        // }
        if (n != null) {
            if (!n.isLeaf()) {
                n.setExpanded(!n.isExpanded());
                filterNode(n);
            } else if (n.getValue().equals("...")) {
                alls.clear();
                // 点击“...” 把缓存中的node展开全部设置成false。
                for (int i = 0; i < allsCache.size(); i++) {
                    allsCache.get(i).setExpanded(false);
                }
                alls.addAll(allsCache.get(0).getParent().getChildren());
            } else if (n.isLeaf()) {
                return false;
            }
            this.notifyDataSetChanged();
        }
        return true;
    }

    public void clearDate() {
        alls.clear();
        allsCache.clear();
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
        ViewHolder holder = null;
        if (view == null) {
            view = this.lif.inflate(R.layout.group_list_item, null);
            holder = new ViewHolder();
            holder.rltItem = (RelativeLayout) view.findViewById(R.id.group_item_rlt);
            holder.ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
            holder.tvText = (TextView) view.findViewById(R.id.tvText);
            holder.ivExEc = (ImageView) view.findViewById(R.id.ivExEc);
            holder.chbSelect = (CheckBox) view.findViewById(R.id.chbSelect);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // item单击事件
        holder.rltItem.setOnClickListener(new OnClickListener() {
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
                
                onItemClickListener.onItemClick(n, !n.isChecked(), position);
            }
        });
        // 复选框单击事件
        holder.chbSelect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TreeNode n = (TreeNode) v.getTag();
                onCheckBoxClick.onCheckBoxClick(n, !n.isChecked(), position);
            }
        });

        // 得到当前节点
        TreeNode n = alls.get(position);

        int allLevel = alls.get(alls.size() - 1).getLevel() + 1;

        if (n != null) {

            // 显示图标
            int iconId = 0;
            switch (n.getType()) {
                case 1:
                    iconId = R.drawable.list_body_group_n;
                    break;
                case 2:
                    if (isOffline(n)) {
                        iconId = R.drawable.list_body_device_d;
                    } else {
                        iconId = R.drawable.list_body_device_n;
                    }
                    break;
                case 3:
                    if (isOffline(n)) {
                        iconId = R.drawable.list_body_camera_d;
                    } else {
                        iconId = R.drawable.list_body_camera_n;
                    }
                    break;
                default:
                    iconId = R.drawable.list_body_group_n;
                    break;
            }

            holder.ivIcon.setBackgroundResource(iconId);

            if (n.getValue().equals("...")) {
                holder.ivIcon.setBackgroundResource(R.drawable.grouptree_more_selector);
            }

            // 显示文本
            holder.tvText.setText(n.getText() + " ");
            holder.rltItem.setTag(n);
            holder.chbSelect.setTag(n);
            holder.chbSelect.setChecked(n.isChecked());
            holder.ivExEc.setImageResource(R.drawable.open_content_selector);

            // 控制复选框和下拉图片的显示
            if (n.getType() == 1) {
                holder.chbSelect.setVisibility(View.GONE);
                if (n.isExpanded() || n.isLeaf()) {
                    holder.ivExEc.setVisibility(View.GONE);
                } else {
                    holder.ivExEc.setVisibility(View.VISIBLE);
                }
            } else if (n.getType() == 2) {
                if (n.isLeaf()) {
                    holder.ivExEc.setVisibility(View.GONE);
                    holder.chbSelect.setVisibility(View.GONE);
                }
                else {
                    holder.ivExEc.setVisibility(View.GONE);
                    holder.chbSelect.setVisibility(View.VISIBLE);
                }
                // 设备不在线则不显示选择框
                if (n.getDeviceInfo().getStatus() != 1) {
                    holder.chbSelect.setVisibility(View.GONE);
                }

            } else {
                // 单击时控制子节点展开和折叠,状态图标改变
                if (n.getParent().getDeviceInfo() != null && n.getParent().getDeviceInfo().getStatus() != 1) {
                    holder.chbSelect.setVisibility(View.GONE);
                }else {
                    holder.chbSelect.setVisibility(View.VISIBLE);
                }
                
                if ( n.getChannelInfo().getState() != 1) {
                    holder.chbSelect.setVisibility(View.GONE);
                }else {
                    holder.chbSelect.setVisibility(View.VISIBLE);
                }
                holder.ivExEc.setVisibility(View.GONE);
            }

            if (!hasCheckBox) {
                holder.chbSelect.setVisibility(View.GONE);
            }

            // 设置不同层级节点的item颜色
            if ((n.getValue().equals("...") || n.getLevel() == 1) && n.isExpanded()) {
                holder.rltItem.setBackgroundResource(R.drawable.common_group_selector);
            } else if ((allLevel - n.getLevel()) == 3 && n.isExpanded()) {
                holder.rltItem.setBackgroundResource(R.drawable.common_device_selector);
            } else if ((allLevel - n.getLevel()) == 2 && n.isExpanded()) {
                holder.rltItem.setBackgroundResource(R.drawable.common_channel_selector);
            } else {
                holder.rltItem.setBackgroundResource(R.drawable.common_list_selector);
            }
        }

        return view;
    }

    private boolean isOffline(TreeNode n) {
        boolean ret = false;
        if (n.getType() == 2 && n.getDeviceInfo().getStatus() != 1) {
            ret = true;
        } else if (n.getType() == 3 && n.getParent().getDeviceInfo() != null &&
                n.getParent().getDeviceInfo().getStatus() != 1) {
                ret = true;
        } else if (n.getType() == 3 && n.getChannelInfo().getState() != 1) {
            ret = true;
        }
        
        return ret;
    }

    public interface IOnCheckBoxClick {
        public void onCheckBoxClick(final TreeNode treeNode, final boolean isChecked, final int position);
    }

    public interface IOnItemClickListener {
        public void onItemClick(final TreeNode treeNode, final boolean isChecked, final int position);
    }

    public class ViewHolder {
        RelativeLayout rltItem;

        CheckBox chbSelect;// 选中与否

        ImageView ivIcon;// 图标

        TextView tvText;// 文本〉〉〉

        ImageView ivExEc;// 展开或折叠标记">"或"v"
    }
}
