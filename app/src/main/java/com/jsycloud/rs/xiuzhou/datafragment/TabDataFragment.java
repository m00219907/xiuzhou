package com.jsycloud.rs.xiuzhou.datafragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jsycloud.rs.xiuzhou.R;
import com.jsycloud.rs.xiuzhou.StartActivity;

import java.util.ArrayList;
import java.util.List;


public class TabDataFragment extends Fragment {

    private StartActivity activity;
    TextView data_fragment_notify;
    ListView data_fragment_list;
    DeadLineAdapter adapter;
    List<String> deadLineInfo = new ArrayList<String>();

    @Override
    public void onAttach(Activity activity) {
        this.activity = (StartActivity)activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.data_fragment, null);

        data_fragment_notify = (TextView)view.findViewById(R.id.data_fragment_notify);
        data_fragment_list = (ListView)view.findViewById(R.id.data_fragment_list);
        deadLineInfo.add("张三 于2016-04-07逾期1天未巡河");
        adapter = new DeadLineAdapter(activity, deadLineInfo);
        data_fragment_list.setAdapter(adapter);

        return view;
    }
}
