package com.jsycloud.groupTree.bean;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    private TreeNode parent;// 父节点

    private final List<TreeNode> children = new ArrayList<TreeNode>();// 子节点

    private String text;// 节点显示的文字

    private String value;// 节点的值

    private boolean isChecked = false;// 是否处于选中状态

    private boolean isExpanded = false;// 是否处于展开状态

    private DeviceInfo deviceInfo = null;// 设备信息

    private ChannelInfoExt channelInfo = null;// 通道信息

    private int type = 0;// 1：组 2：设备 3：通道

    public TreeNode(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public void setParent(TreeNode node) {
        this.parent = node;
    }

    public TreeNode getParent() {
        return this.parent;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public List<TreeNode> getChildren() {
        return this.children;
    }

    public List<TreeNode> getDepChildren() {
        List<TreeNode> depList = new ArrayList<TreeNode>();
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i).getType() == 1) {
                depList.add(children.get(i));
            }
        }
        return depList;
    }

    public void add(TreeNode node) {
        if (!children.contains(node)) {
            children.add(node);
        }
    }

    public void clear() {
        children.clear();
    }

    public int getLevel() {
        return parent == null ? 0 : parent.getLevel() + 1;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public boolean isLeaf() {
        return children.size() < 1 ? true : false;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean isExpanded) {
        this.isExpanded = isExpanded;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public ChannelInfoExt getChannelInfo() {
        return channelInfo;
    }

    public void setChannelInfo(ChannelInfoExt channelInfo) {
        this.channelInfo = channelInfo;
    }

}
