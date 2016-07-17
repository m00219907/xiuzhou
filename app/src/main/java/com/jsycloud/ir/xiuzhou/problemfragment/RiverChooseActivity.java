package com.jsycloud.ir.xiuzhou.problemfragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.jsycloud.ir.xiuzhou.Constant;
import com.jsycloud.ir.xiuzhou.R;
import com.jsycloud.ir.xiuzhou.StartActivity;

import hfyt.hzlh.com.customwheel.ArrayWheelAdapter;
import hfyt.hzlh.com.customwheel.OnWheelChangedListener;
import hfyt.hzlh.com.customwheel.WheelView;

public class RiverChooseActivity extends Activity implements View.OnClickListener, OnWheelChangedListener {

    int curIndex = 0;
    private String[] mProvinceDatas = new String[Constant.allriverList.size()];
    WheelView river_choose_wheel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.river_choose_activity);
        setupData();
        river_choose_wheel = (WheelView)findViewById(R.id.river_choose_wheel);
        river_choose_wheel.addChangingListener(this);
        river_choose_wheel.setViewAdapter(new ArrayWheelAdapter<String>(this, mProvinceDatas));
        river_choose_wheel.setVisibleItems(7);

        findViewById(R.id.river_choose_cancel).setOnClickListener(this);
        findViewById(R.id.river_choose_ok).setOnClickListener(this);
        findViewById(R.id.river_choose_top).setOnClickListener(this);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.river_choose_cancel:
                finish();
                break;
            case R.id.river_choose_ok:
                Intent intent = new Intent(this, StartActivity.class);
                intent.putExtra("curIndex", curIndex);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.river_choose_top:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        curIndex = newValue;
    }

    private void setupData() {
        for(int i = 0;i<Constant.allriverList.size();i++) {
            mProvinceDatas[i] = Constant.allriverList.get(i).getName();
        }

    }
}
