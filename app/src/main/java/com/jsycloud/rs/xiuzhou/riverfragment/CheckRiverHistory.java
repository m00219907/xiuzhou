package com.jsycloud.rs.xiuzhou.riverfragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.jsycloud.rs.xiuzhou.Constant;
import com.jsycloud.rs.xiuzhou.HttpClentLinkNet;
import com.jsycloud.rs.xiuzhou.R;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CheckRiverHistory extends Activity implements View.OnClickListener {

    ListView checkriver_history_list;
    CheckRiverAdapter checkRiverAdapter;
    List<HistoryItem> allHistory = new ArrayList<HistoryItem>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.checkriver_history);

        findViewById(R.id.checkriver_history_back).setOnClickListener(this);
        checkriver_history_list = (ListView)findViewById(R.id.checkriver_history_list);
        checkRiverAdapter = new CheckRiverAdapter(this, allHistory);
        checkriver_history_list.setAdapter(checkRiverAdapter);
        getAllHistory("0");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.checkriver_history_back:
                finish();
                break;
            default:
                break;
        }
    }

    public void getAllHistory(String page) {
        String url = HttpClentLinkNet.BaseAddr + "getpatrollog.php";
        AjaxParams params = new AjaxParams();
        params.put("userid", Constant.userid);
        params.put("page", page);
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
                    if(success.equals("1") && jsObj.has("data")) {
                        ArrayList<HistoryItem> curHistorylist = new ArrayList<HistoryItem>();
                        try {
                            if (jsObj.has("rt") && jsObj.getString("rt").equals("1")) {
                                JSONArray eventArray = jsObj.getJSONArray("data");
                                for (int i = 0; i < eventArray.length(); i++) {
                                    JSONObject typeItemArray = eventArray.getJSONObject(i);
                                    HistoryItem historyItem = new HistoryItem();
                                    historyItem.setTimertp(typeItemArray.getString("timertp"));
                                    historyItem.setAddress(typeItemArray.getString("address"));
                                    historyItem.setDescribe(typeItemArray.getString("river"));
                                    historyItem.setRiver(typeItemArray.getString("describe"));
                                    curHistorylist.add(historyItem);
                                }
                            }

                            allHistory.clear();
                            allHistory.addAll(curHistorylist);
                            checkRiverAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                        }
                    }else{
                        Toast.makeText(CheckRiverHistory.this, "巡河记录为空", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                Toast.makeText(CheckRiverHistory.this, "巡河记录为空", Toast.LENGTH_SHORT).show();
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    public class HistoryItem{
        String timertp;
        String address;
        String river;
        String describe;

        public String getTimertp() {
            return timertp;
        }

        public void setTimertp(String timertp) {
            this.timertp = timertp;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public String getRiver() {
            return river;
        }

        public void setRiver(String river) {
            this.river = river;
        }
    }
}
