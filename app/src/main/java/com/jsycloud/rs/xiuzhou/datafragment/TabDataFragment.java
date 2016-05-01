package com.jsycloud.rs.xiuzhou.datafragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jsycloud.rs.xiuzhou.Constant;
import com.jsycloud.rs.xiuzhou.HttpClentLinkNet;
import com.jsycloud.rs.xiuzhou.R;
import com.jsycloud.rs.xiuzhou.SharePreferenceDataUtil;
import com.jsycloud.rs.xiuzhou.StartActivity;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TabDataFragment extends Fragment implements View.OnClickListener{

    private StartActivity activity;
    TextView data_fragment_notice, data_fragment_criticism;
    View data_fragment_leftcolor, data_fragment_rightcolor;
    ListView data_fragment_noticeList, data_fragment_criticismList;
    NoticeAdapter noticeAdapter;
    CriticismAdapter criticismAdapter;
    List<noticeItem> allNotice = new ArrayList<noticeItem>();
    List<criticismItem> allCriticism = new ArrayList<criticismItem>();

    @Override
    public void onAttach(Activity activity) {
        this.activity = (StartActivity)activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.data_fragment, null);

        data_fragment_notice = (TextView)view.findViewById(R.id.data_fragment_notice);
        data_fragment_notice.setOnClickListener(this);
        data_fragment_criticism = (TextView)view.findViewById(R.id.data_fragment_criticism);
        data_fragment_criticism.setOnClickListener(this);

        data_fragment_leftcolor = view.findViewById(R.id.data_fragment_leftcolor);
        data_fragment_rightcolor = view.findViewById(R.id.data_fragment_rightcolor);

        data_fragment_noticeList = (ListView)view.findViewById(R.id.data_fragment_noticeList);
        noticeAdapter = new NoticeAdapter(activity, allNotice);
        data_fragment_noticeList.setAdapter(noticeAdapter);

        data_fragment_criticismList = (ListView)view.findViewById(R.id.data_fragment_criticismList);
        criticismAdapter = new CriticismAdapter(activity, allCriticism);
        data_fragment_criticismList.setAdapter(criticismAdapter);

        getNotice();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.data_fragment_notice:
                data_fragment_leftcolor.setBackgroundColor(0xff2196f3);
                data_fragment_rightcolor.setBackgroundColor(0xffffffff);
                data_fragment_noticeList.setVisibility(View.VISIBLE);
                data_fragment_criticismList.setVisibility(View.GONE);
                getNotice();
                break;
            case R.id.data_fragment_criticism:
                data_fragment_leftcolor.setBackgroundColor(0xffffffff);
                data_fragment_rightcolor.setBackgroundColor(0xff2196f3);
                data_fragment_noticeList.setVisibility(View.GONE);
                data_fragment_criticismList.setVisibility(View.VISIBLE);
                getCriticism();
                break;
            default:
                break;
        }
    }

    public void getNotice() {
        String url = HttpClentLinkNet.BaseAddr + "getnotice.php";
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
                    if(success.equals("1") && jsObj.has("notice")) {
                        ArrayList<noticeItem> curNoticelist = new ArrayList<noticeItem>();
                        try {
                                JSONArray eventArray = jsObj.getJSONArray("notice");
                                for (int i = 0; i < eventArray.length(); i++) {
                                    JSONObject typeItemArray = eventArray.getJSONObject(i);
                                    noticeItem historyItem = new noticeItem();
                                    historyItem.setTitle(typeItemArray.getString("title"));
                                    historyItem.setContent(typeItemArray.getString("content"));
                                    historyItem.setTime(typeItemArray.getString("time"));
                                    historyItem.setUser(typeItemArray.getString("user"));
                                    curNoticelist.add(historyItem);
                                }

                            allNotice.clear();
                            allNotice.addAll(curNoticelist);
                            noticeAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
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

    public void getCriticism() {
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
                    if(success.equals("1") && jsObj.has("criticism")) {
                        ArrayList<criticismItem> curCriticismItemlist = new ArrayList<criticismItem>();
                        try {
                                JSONArray eventArray = jsObj.getJSONArray("criticism");
                                for (int i = 0; i < eventArray.length(); i++) {
                                    JSONObject typeItemArray = eventArray.getJSONObject(i);
                                    criticismItem historyItem = new criticismItem();
                                    historyItem.setContent(typeItemArray.getString("content"));
                                    historyItem.setTime(typeItemArray.getString("time"));
                                    historyItem.setUser(typeItemArray.getString("user"));
                                    curCriticismItemlist.add(historyItem);
                                }

                            allCriticism.clear();
                            allCriticism.addAll(curCriticismItemlist);
                            criticismAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
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

    public class noticeItem{
        String title;
        String content;
        String time;
        String user;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

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

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }
    }

    public class criticismItem{
        String content;
        String time;
        String user;

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

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }
    }
}
