package com.jsycloud.ir.xiuzhou.riverfragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.jsycloud.ir.xiuzhou.Constant;
import com.jsycloud.ir.xiuzhou.HttpClentLinkNet;
import com.jsycloud.ir.xiuzhou.R;

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
        checkriver_history_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CheckRiverHistory.this, WebviewActivity.class);
                intent.putExtra("url", HttpClentLinkNet.BaseAddr + "page_patrollogshow.php?pid=" + allHistory.get(position).getId());
                startActivity(intent);
            }
        });

        getAllHistory("");
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
        String url = HttpClentLinkNet.BaseAddr + "getpatrollogall.php";
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
                        ArrayList<HistoryItem> curHistorylist = new ArrayList<HistoryItem>();
                        try {
                            JSONArray eventArray = jsObj.getJSONArray("data");
                            for (int i = 0; i < eventArray.length(); i++) {
                                JSONObject typeItemArray = eventArray.getJSONObject(i);
                                HistoryItem historyItem = new HistoryItem();
                                historyItem.setId(typeItemArray.getString("id"));
                                historyItem.setTimertp(typeItemArray.getString("timertp"));
                                historyItem.setAddress(typeItemArray.getString("address"));
                                historyItem.setDescribe(typeItemArray.getString("describe"));
                                historyItem.setRiver(typeItemArray.getString("river"));
                                curHistorylist.add(historyItem);
                            }
                            allHistory.addAll(curHistorylist);
                            checkRiverAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                        }
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
        String id;
        String timertp;
        String address;
        String river;
        String describe;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

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
