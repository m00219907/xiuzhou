package com.jsycloud.rs.xiuzhou.riverfragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jsycloud.rs.xiuzhou.R;
import com.jsycloud.rs.xiuzhou.StartActivity;


public class TabRiverFragment extends Fragment {

    private StartActivity activity;

    @Override
    public void onAttach(Activity activity) {
        this.activity = (StartActivity)activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.river_fragment, null);

        activity.tab_map_toplayout_text.setText("河长中心");
        return view;
    }
}
