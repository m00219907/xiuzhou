package com.jsycloud.ir.xiuzhou.mapfragment;

import com.amap.api.maps2d.model.LatLng;

import java.util.ArrayList;

public class riverInfo {

    public String id = "";
    public String name = "";
    public String num = "";
    public String townid = "";
    public String townname = "";
    public String length = "";
    public String start = "";
    public String end = "";

    public ArrayList<LatLng> pointList = new ArrayList<LatLng>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTownid() {
        return townid;
    }

    public void setTownid(String townid) {
        this.townid = townid;
    }

    public String getTownname() {
        return townname;
    }

    public void setTownname(String townname) {
        this.townname = townname;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public ArrayList<LatLng> getPointList() {
        return pointList;
    }

    public void setPointList(ArrayList<LatLng> pointList) {
        this.pointList = pointList;
    }
}
