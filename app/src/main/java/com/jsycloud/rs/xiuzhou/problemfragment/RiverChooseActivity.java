package com.jsycloud.rs.xiuzhou.problemfragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.jsycloud.rs.xiuzhou.R;
import com.jsycloud.rs.xiuzhou.StartActivity;

import hfyt.hzlh.com.customwheel.ArrayWheelAdapter;
import hfyt.hzlh.com.customwheel.OnWheelChangedListener;
import hfyt.hzlh.com.customwheel.WheelView;

public class RiverChooseActivity extends Activity implements View.OnClickListener, OnWheelChangedListener {

    int curIndex = 0;
    private String[] mProvinceDatas = new String[31];
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
                intent.putExtra("riverName", mProvinceDatas[curIndex]);
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
        mProvinceDatas[0] = "安徽省";
        mProvinceDatas[1] = "北京市";
        mProvinceDatas[2] = "重庆市";
        mProvinceDatas[3] = "福建省";
        mProvinceDatas[4] = "甘肃省";
        mProvinceDatas[5] = "广东省";
        mProvinceDatas[6] = "广西壮族自治区";
        mProvinceDatas[7] = "贵州省";
        mProvinceDatas[8] = "海南省";
        mProvinceDatas[9] = "河北省";
        mProvinceDatas[10] = "河南省";
        mProvinceDatas[11] = "黑龙江省";
        mProvinceDatas[12] = "湖北省";
        mProvinceDatas[13] = "湖南省";
        mProvinceDatas[14] = "吉林省";
        mProvinceDatas[15] = "江苏省";
        mProvinceDatas[16] = "江西省";
        mProvinceDatas[17] = "辽宁省";
        mProvinceDatas[18] = "内蒙古自治区";
        mProvinceDatas[19] = "宁夏回族自治区";
        mProvinceDatas[20] = "青海省";
        mProvinceDatas[21] = "山东省";
        mProvinceDatas[22] = "山西省";
        mProvinceDatas[23] = "陕西省";
        mProvinceDatas[24] = "上海市";
        mProvinceDatas[25] = "四川省";
        mProvinceDatas[26] = "天津市";
        mProvinceDatas[27] = "西藏自治区";
        mProvinceDatas[28] = "新疆维吾尔自治区";
        mProvinceDatas[29] = "云南省";
        mProvinceDatas[30] = "浙江省";
    }
}
