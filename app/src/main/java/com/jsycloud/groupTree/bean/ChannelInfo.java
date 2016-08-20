package com.jsycloud.groupTree.bean;

import java.io.Serializable;

public class ChannelInfo implements Serializable {

    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;

    private int devType; // 类型 1：枪机 2：球机 3：半球

    private String szId; // 通道ID:设备ID+通道号

    private String szName; // 名称

    private long right; // 权限

    private double longitude; // 经度

    private double latitude; // 维度

    public int getDevType() {
        return devType;
    }

    public void setDevType(int type) {
        this.devType = type;
    }

    public String getSzId() {
        return szId;
    }

    public void setSzId(String szId) {
        this.szId = szId;
    }

    public String getSzName() {
        return szName;
    }

    public void setSzName(String szName) {
        this.szName = szName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getRight() {
        return right;
    }

    public void setRight(long right) {
        this.right = right;
    }

}

