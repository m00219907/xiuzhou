package com.jsycloud.ir.xiuzhou.mapfragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.PolylineOptions;
import com.jsycloud.ir.xiuzhou.Constant;
import com.jsycloud.ir.xiuzhou.HttpClentLinkNet;
import com.jsycloud.ir.xiuzhou.R;
import com.jsycloud.ir.xiuzhou.StartActivity;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TabMapFragment2 extends Fragment implements LocationSource, AMapLocationListener, View.OnClickListener {

    private View view;
    StartActivity activity;

    private MapView mapView;
    private AMap aMap;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    private LatLng XIUZHOU = new LatLng(30.761567, 120.707474);// 秀洲区政府经纬度

    public ArrayList<riverInfo> riverList = new ArrayList<riverInfo>();

    @Override
    public void onAttach(Activity activity) {
        this.activity = (StartActivity)activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.map_fragment, null);

        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }

        view.findViewById(R.id.map_left).setOnClickListener(this);

        return view;
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        //aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
        //aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(XIUZHOU, 18, 30, 0)));
        // aMap.setMyLocationType()
        getrivercoordinate();
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(activity);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                Constant.curLocation = amapLocation;
                Toast.makeText(activity, Constant.curLocation.getAddress(), Toast.LENGTH_SHORT).show();
            } else {
                //String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                //Toast.makeText(activity, errText, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.map_left:
                Intent intent = new Intent(activity, AllRiverActivity.class);
                activity.startActivity(intent);
                break;
        }
    }

    public void getrivercoordinate() {
        String url = HttpClentLinkNet.BaseAddr + "getrivercoordinate.php";
        AjaxParams params = new AjaxParams();
        HttpClentLinkNet.getInstance().sendReqFinalHttp_Post(url, params, new AjaxCallBack() {
            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(Object t) {
                String jsStr = "";
                String success = "";

                if (t != null) {
                    jsStr = String.valueOf(t);
                }

                try {
                    JSONObject jsObj = new JSONObject(jsStr);
                    if (jsObj.has("success")) {
                        success = jsObj.getString("success");
                    }
                    if(success.equals("1")) {
                        JSONArray eventArray = jsObj.getJSONArray("river");
                        for (int i = 0; i < eventArray.length(); i++) {
                            JSONObject curRiverJSON = eventArray.getJSONObject(i);
                            riverInfo curRiverInfo = new riverInfo();
                            curRiverInfo.setId(curRiverJSON.getString("id"));
                            curRiverInfo.setName(curRiverJSON.getString("name"));
                            curRiverInfo.setNum(curRiverJSON.getString("num"));
                            curRiverInfo.setTownid(curRiverJSON.getString("townid"));
                            curRiverInfo.setTownname(curRiverJSON.getString("townname"));
                            curRiverInfo.setLength(curRiverJSON.getString("length"));
                            curRiverInfo.setStart(curRiverJSON.getString("start"));
                            curRiverInfo.setEnd(curRiverJSON.getString("end"));

                            String pointsStr = curRiverJSON.getString("points");
                            pointsStr = pointsStr.replace("[", "");
                            pointsStr = pointsStr.replace("]","");
                            pointsStr = pointsStr.replace("\"","");
                            pointsStr = pointsStr.replace(",,",",");
                            String pointsArray[] = pointsStr.split(",");
                            ArrayList<LatLng> curpointList = new ArrayList<LatLng>();
                            for (int j = 0; j < pointsArray.length; j+=2) {
                                double latitude = Double.valueOf(pointsArray[j+1]);
                                double longitude = Double.valueOf(pointsArray[j]);
                                LatLng latLng = new LatLng(latitude,longitude);
                                curpointList.add(latLng);
                            }
                            curRiverInfo.setPointList(curpointList);
                            riverList.add(curRiverInfo);
                            drawLines();
                        }
                    }
                } catch (JSONException e) {
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    public void drawLines() {
        for(int i = 0;i<riverList.size();i++){
            ArrayList<LatLng> temp = riverList.get(i).getPointList();
            //for (int j = 0;j<temp.size()-1;j++){
                aMap.addPolyline((new PolylineOptions()).add(temp.get(0), temp.get(temp.size()-1)).width(8).color(Color.RED));
           // }

            String text = "所在区域:"+riverList.get(i).getTownname()+"\n"+
                    "河流编号:"+"FX06013\n"+
                    "河流长度:"+riverList.get(i).getLength()+"\n"+
                    "所在区域:"+riverList.get(i).getStart()+"\n"+
                    "所在区域:"+riverList.get(i).getEnd();

            aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .position(temp.get(0)).title(riverList.get(i).getName())
                    .snippet(text).draggable(true));
        }
    }
}
