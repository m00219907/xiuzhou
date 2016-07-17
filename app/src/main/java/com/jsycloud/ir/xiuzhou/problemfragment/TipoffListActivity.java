package com.jsycloud.ir.xiuzhou.problemfragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
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

public class TipoffListActivity extends Activity {

    public List<tipoffItem> tipoffList = new ArrayList<tipoffItem>();
    ListView tipoff_list_listview;
    TipoffListAdapter tipoffListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tipofflist_activity);
        findViewById(R.id.tipoff_list_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TipoffListActivity.this.finish();
            }
        });

        tipoff_list_listview = (ListView)findViewById(R.id.tipoff_list_listview);
        tipoffListAdapter = new TipoffListAdapter(this, tipoffList);
        tipoff_list_listview.setAdapter(tipoffListAdapter);
        tipoff_list_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    public void gettipoffList(String page) {
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
                    if (success.equals("1") && jsObj.has("data")) {
                        ArrayList<tipoffItem> curtipoffItem = new ArrayList<tipoffItem>();
                        try {
                            JSONArray eventArray = jsObj.getJSONArray("data");
                            for (int i = 0; i < eventArray.length(); i++) {
                                JSONObject typeItemArray = eventArray.getJSONObject(i);
                                tipoffItem tipoffitem = new tipoffItem();
                                tipoffitem.setId(typeItemArray.getString("id"));
                                tipoffitem.setTimertp(typeItemArray.getString("timertp"));
                                tipoffitem.setAddress(typeItemArray.getString("address"));
                                curtipoffItem.add(tipoffitem);
                            }

                            tipoffList.clear();
                            tipoffList.addAll(curtipoffItem);
                            tipoffListAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                        }
                    } else {
                        Toast.makeText(TipoffListActivity.this, "投诉举报记录为空", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                Toast.makeText(TipoffListActivity.this, "投诉举报记录为空", Toast.LENGTH_SHORT).show();
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }


    public class tipoffItem{
        String id;
        String user;
        String timertp;
        String address;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
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
    }
}
