package com.jsycloud.rs.xiuzhou.riverfragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jsycloud.rs.xiuzhou.MyRectangleView;
import com.jsycloud.rs.xiuzhou.R;
import com.jsycloud.rs.xiuzhou.StartActivity;

public class TabRiverFragment extends Fragment implements View.OnClickListener{

    private StartActivity activity;
    MyRectangleView river_fragment_checkriver, river_fragment_checkriver_history, river_fragment_entrust_checkriver;

    @Override
    public void onAttach(Activity activity) {
        this.activity = (StartActivity)activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.river_fragment, null);

        river_fragment_checkriver = (MyRectangleView)view.findViewById(R.id.river_fragment_checkriver);
        river_fragment_checkriver.setRectangleColor(0xff4caf50);
        river_fragment_checkriver.settextStr("巡河");
        river_fragment_checkriver.setOnClickListener(this);

        river_fragment_checkriver_history = (MyRectangleView)view.findViewById(R.id.river_fragment_checkriver_history);
        river_fragment_checkriver_history.setRectangleColor(0xff2196f3);
        river_fragment_checkriver_history.settextStr("巡河记录");
        river_fragment_checkriver_history.setOnClickListener(this);

        river_fragment_entrust_checkriver = (MyRectangleView)view.findViewById(R.id.river_fragment_entrust_checkriver);
        river_fragment_entrust_checkriver.setRectangleColor(0xffff0000);
        river_fragment_entrust_checkriver.settextStr("委托巡河");
        river_fragment_entrust_checkriver.setOnClickListener(this);

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
