package com.jsycloud.rs.xiuzhou.riverfragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jsycloud.rs.xiuzhou.R;
import com.jsycloud.rs.xiuzhou.StartActivity;

public class TabRiverFragment extends Fragment implements View.OnClickListener{

    private StartActivity activity;

    @Override
    public void onAttach(Activity activity) {
        this.activity = (StartActivity)activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.river_fragment, null);

        view.findViewById(R.id.river_fragment_checkriver).setOnClickListener(this);
        view.findViewById(R.id.river_fragment_checkriver_history).setOnClickListener(this);
        view.findViewById(R.id.river_fragment_entrust_checkriver).setOnClickListener(this);

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
            case R.id.river_fragment_entrust_checkriver:
                break;
            default:
                break;
        }
    }
}
