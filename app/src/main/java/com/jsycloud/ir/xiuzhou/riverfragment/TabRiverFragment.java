package com.jsycloud.ir.xiuzhou.riverfragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jsycloud.ir.xiuzhou.Constant;
import com.jsycloud.ir.xiuzhou.HttpClentLinkNet;
import com.jsycloud.ir.xiuzhou.MyRectangleView;
import com.jsycloud.ir.xiuzhou.R;
import com.jsycloud.ir.xiuzhou.StartActivity;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TabRiverFragment extends Fragment implements View.OnClickListener{

    private StartActivity activity;
    SwipeRefreshLayout river_fragment_ptr_frame;
    RiverFragmentAdapter riverFragmentAdapter;
    TextView river_fragment_remind_content, river_fragment_remind_time;
    MyRectangleView river_fragment_checkriver, river_fragment_checkriver_history, river_fragment_regetcode, river_fragment_sendcode;

    List<criticism> criticismList = new ArrayList<criticism>();
    List<report> assignList = new ArrayList<report>();
    List<report> reportList = new ArrayList<report>();

    @Override
    public void onAttach(Activity activity) {
        this.activity = (StartActivity)activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.river_fragment, null);

        river_fragment_ptr_frame = (SwipeRefreshLayout)view.findViewById(R.id.river_fragment_ptr_frame);
        river_fragment_ptr_frame.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getremind();
                getcriticism();
                getassign();
                getreport();
            }
        });

        ListView river_fragment_list = (ListView)view.findViewById(R.id.river_fragment_list);

        View river_fragment_header = inflater.inflate(R.layout.river_fragment_header, null);
        river_fragment_remind_content = (TextView)river_fragment_header.findViewById(R.id.river_fragment_remind_content);
        river_fragment_remind_time = (TextView)river_fragment_header.findViewById(R.id.river_fragment_remind_time);
        river_fragment_list.addHeaderView(river_fragment_header);

        View river_fragment_foot = inflater.inflate(R.layout.river_fragment_foot, null);
        river_fragment_checkriver = (MyRectangleView)river_fragment_foot.findViewById(R.id.river_fragment_checkriver);
        river_fragment_checkriver.setRectangleColor(0xff4caf50);
        river_fragment_checkriver.settextStr("巡河");
        river_fragment_checkriver.setOnClickListener(this);

        river_fragment_checkriver_history = (MyRectangleView)river_fragment_foot.findViewById(R.id.river_fragment_checkriver_history);
        river_fragment_checkriver_history.setRectangleColor(0xff2196f3);
        river_fragment_checkriver_history.settextStr("巡河记录");
        river_fragment_checkriver_history.setOnClickListener(this);

        river_fragment_regetcode = (MyRectangleView)river_fragment_foot.findViewById(R.id.river_fragment_regetcode);
        river_fragment_regetcode.setRectangleColor(0xff666666);
        river_fragment_regetcode.settextStr("重新获取委托码");
        river_fragment_regetcode.setOnClickListener(this);

        river_fragment_sendcode = (MyRectangleView)river_fragment_foot.findViewById(R.id.river_fragment_sendcode);
        river_fragment_sendcode.setRectangleColor(0xffff0000);
        river_fragment_sendcode.settextStr("发送委托码");
        river_fragment_sendcode.setOnClickListener(this);
        river_fragment_list.addFooterView(river_fragment_foot);

        riverFragmentAdapter = new RiverFragmentAdapter(activity, criticismList, assignList, reportList);
        river_fragment_list.setAdapter(riverFragmentAdapter);

        if(!Constant.isLogByCode){
            getremind();
            getcriticism();
            getassign();
            getreport();
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.river_fragment_checkriver:
                Intent intent1 = new Intent(activity, CheckRiverActivity.class);
                startActivity(intent1);
                break;
            case R.id.river_fragment_checkriver_history:
                Intent intent2 = new Intent(activity, CheckRiverHistory.class);
                startActivity(intent2);
                break;
            case R.id.river_fragment_regetcode:
                getauthcode(true);
                break;
            case R.id.river_fragment_sendcode:
                getauthcode(false);
                break;
            default:
                break;
        }
    }

    public void getremind() {
        String url = HttpClentLinkNet.BaseAddr + "getremind.php";
        AjaxParams params = new AjaxParams();
        params.put("userid", Constant.userid);
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
                        river_fragment_remind_content.setText(jsObj.getString("content"));
                        river_fragment_remind_time.setText(jsObj.getString("time"));
                    }else if(success.equals("2")){
                        river_fragment_remind_content.setText("暂无提醒");
                        river_fragment_remind_time.setText("");
                    }
                } catch (JSONException e) {
                }

                river_fragment_ptr_frame.setRefreshing(false);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    public void getcriticism() {
        String url = HttpClentLinkNet.BaseAddr + "getcriticism.php";
        AjaxParams params = new AjaxParams();
        params.put("userid", Constant.userid);
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
                        criticismList.clear();
                        JSONArray eventArray = jsObj.getJSONArray("criticism");
                        for(int i = 0;i<eventArray.length();i++){
                            JSONObject curRiverJSON = eventArray.getJSONObject(i);
                            criticism curCriticism = new criticism();
                            curCriticism.setContent(curRiverJSON.getString("content"));
                            curCriticism.setTime(curRiverJSON.getString("time"));
                            criticismList.add(curCriticism);
                        }
                        riverFragmentAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                }
                river_fragment_ptr_frame.setRefreshing(false);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    public void getassign() {
        String url = HttpClentLinkNet.BaseAddr + "getassign.php";
        AjaxParams params = new AjaxParams();
        params.put("userid", Constant.userid);
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
                        assignList.clear();
                        JSONArray eventArray = jsObj.getJSONArray("assign");
                        for(int i = 0;i<eventArray.length();i++){
                            JSONObject curRiverJSON = eventArray.getJSONObject(i);
                            report curReport = new report();
                            curReport.setUserid(curRiverJSON.getString("userid"));
                            curReport.setUsername(curRiverJSON.getString("username"));
                            curReport.setRiverid(curRiverJSON.getString("riverid"));
                            curReport.setRivername(curRiverJSON.getString("rivername"));
                            curReport.setAddress(curRiverJSON.getString("address"));
                            curReport.setPhoto1(curRiverJSON.getString("photo1"));
                            curReport.setPhoto2(curRiverJSON.getString("photo2"));
                            curReport.setPhoto3(curRiverJSON.getString("photo3"));
                            curReport.setPhoto4(curRiverJSON.getString("photo4"));
                            curReport.setPhoto5(curRiverJSON.getString("photo5"));
                            curReport.setTime(curRiverJSON.getString("time"));
                            curReport.setStatus(curRiverJSON.getString("status"));
                            assignList.add(curReport);
                        }
                        riverFragmentAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                }
                river_fragment_ptr_frame.setRefreshing(false);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    public void getreport() {
        String url = HttpClentLinkNet.BaseAddr + "getreport.php";
        AjaxParams params = new AjaxParams();
        params.put("userid", Constant.userid);
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
                        reportList.clear();
                        JSONArray eventArray = jsObj.getJSONArray("report");
                        for(int i = 0;i<eventArray.length();i++){
                            JSONObject curRiverJSON = eventArray.getJSONObject(i);
                            report curReport = new report();
                            curReport.setUserid(curRiverJSON.getString("userid"));
                            curReport.setUsername(curRiverJSON.getString("username"));
                            curReport.setRiverid(curRiverJSON.getString("riverid"));
                            curReport.setRivername(curRiverJSON.getString("rivername"));
                            curReport.setAddress(curRiverJSON.getString("address"));
                            curReport.setPhoto1(curRiverJSON.getString("photo1"));
                            curReport.setPhoto2(curRiverJSON.getString("photo2"));
                            curReport.setPhoto3(curRiverJSON.getString("photo3"));
                            curReport.setPhoto4(curRiverJSON.getString("photo4"));
                            curReport.setPhoto5(curRiverJSON.getString("photo5"));
                            curReport.setTime(curRiverJSON.getString("time"));
                            curReport.setStatus(curRiverJSON.getString("status"));
                            reportList.add(curReport);
                        }
                        riverFragmentAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                }
                river_fragment_ptr_frame.setRefreshing(false);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    public void getauthcode(final boolean bNew) {
        String url = HttpClentLinkNet.BaseAddr + "getauthcode.php";
        AjaxParams params = new AjaxParams();
        params.put("userid", Constant.userid);
        if(bNew){
            params.put("isnew", "1");
        }
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
                        if(bNew){
                            Toast.makeText(activity,"重新获取到的委托码是："+jsObj.getString("authcode"),Toast.LENGTH_SHORT).show();
                        }else {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.putExtra("sms_body", "巡河委托码：" + jsObj.getString("authcode"));
                            intent.setType("vnd.android-dir/mms-sms");
                            activity.startActivity(intent);
                        }
                    }else if(success.equals("2")){
                        if(bNew){
                            Toast.makeText(activity, "重新获取委托码失败", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(activity, "当前无委托码", Toast.LENGTH_SHORT).show();
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

    public class criticism{
        String content;
        String time;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

    public class report{
        String userid;
        String username;
        String riverid;
        String rivername;
        String describe;
        String address;
        String photo1;
        String photo2;
        String photo3;
        String photo4;
        String photo5;
        String time;
        String status;

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getRiverid() {
            return riverid;
        }

        public void setRiverid(String riverid) {
            this.riverid = riverid;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public String getRivername() {
            return rivername;
        }

        public void setRivername(String rivername) {
            this.rivername = rivername;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPhoto1() {
            return photo1;
        }

        public void setPhoto1(String photo1) {
            this.photo1 = photo1;
        }

        public String getPhoto2() {
            return photo2;
        }

        public void setPhoto2(String photo2) {
            this.photo2 = photo2;
        }

        public String getPhoto3() {
            return photo3;
        }

        public void setPhoto3(String photo3) {
            this.photo3 = photo3;
        }

        public String getPhoto5() {
            return photo5;
        }

        public void setPhoto5(String photo5) {
            this.photo5 = photo5;
        }

        public String getPhoto4() {
            return photo4;
        }

        public void setPhoto4(String photo4) {
            this.photo4 = photo4;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

}
