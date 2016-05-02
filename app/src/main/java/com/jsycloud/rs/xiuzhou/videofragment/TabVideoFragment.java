package com.jsycloud.rs.xiuzhou.videofragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.jsycloud.rs.xiuzhou.R;
import com.jsycloud.rs.xiuzhou.StartActivity;

import java.util.ArrayList;
import java.util.HashMap;


public class TabVideoFragment extends Fragment implements View.OnClickListener {

    private StartActivity activity;
    Spinner video_fragment_spinnertown, video_fragment_spinnerriver;
    private String[] mTownDatas = new String[9];
    private ArrayList<String> mRiverDatas = new ArrayList<String>();
    private HashMap<String, String[]> mTownDatasMap = new HashMap<String, String[]>();
    int curTownIndex = 0;
    int curRiverIndex = 0;

    @Override
    public void onAttach(Activity activity) {
        this.activity = (StartActivity)activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_fragment, null);
        video_fragment_spinnertown = (Spinner)view.findViewById(R.id.video_fragment_spinnertown);
        video_fragment_spinnerriver = (Spinner)view.findViewById(R.id.video_fragment_spinnerriver);
        setupData();

        final ArrayAdapter<String> riverAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, mRiverDatas);
        riverAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        video_fragment_spinnerriver.setAdapter(riverAdapter);
        video_fragment_spinnerriver.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                curRiverIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                curRiverIndex = 0;
            }
        });

        ArrayAdapter<String> townAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, mTownDatas);
        townAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        video_fragment_spinnertown.setAdapter(townAdapter);
        video_fragment_spinnertown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                curTownIndex = position;
                mRiverDatas.clear();
                for (int i = 0; i < mTownDatasMap.get(mTownDatas[position]).length; i++) {
                    mRiverDatas.add(mTownDatasMap.get(mTownDatas[position])[i]);
                }
                riverAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                curTownIndex = 0;
                mRiverDatas.clear();
                for (int i = 0; i < mTownDatasMap.get(mTownDatas[0]).length; i++) {
                    mRiverDatas.add(mTownDatasMap.get(mTownDatas[0])[i]);
                }
                riverAdapter.notifyDataSetChanged();
            }
        });

        view.findViewById(R.id.video_fragment_startvideo).setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.video_fragment_startvideo:
                Intent intent = new Intent(activity, VideoActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void setupData() {
        mTownDatas[0] = "全区";
        String[] anhuidata={"新滕塘(含北支)","红旗塘(含配套河套)","大新港","濮院港","五灵泾港","虹阳港","三店塘","长住桥港","洛东港","横泾桥港、新开河","长水塘","后福庄港、荷花兵","建设港",
                "和睦桥港","木桥港(新城段)","乌镇港","朱家桥港","新滕塘西支","八字港","观音桥港","马泾港","秀清港","杨家桥港、秀新桥港","王店市河","下睦港","苏州塘秀洲段","夏婆桥港","桃园港","新农港","新兵‘毛纹港","宣家桥港、太平桥港"};
        mTownDatasMap.put("全区", anhuidata);

        mTownDatas[1] = "秀洲新区";
        String[] beijingdata={"大新港","秀清港","夏婆桥港"};
        mTownDatasMap.put("秀洲新区", beijingdata);

        mTownDatas[2] = "高照街道";
        String[] chongqingdata = {"马泾港"};
        mTownDatasMap.put("高照街道", chongqingdata);

        mTownDatas[3] = "新城街道";
        String[] fujiandata = {"长住桥港", "和睦桥港", "木桥港(新城段)", "朱家桥港"};
        mTownDatasMap.put("新城街道", fujiandata);

        mTownDatas[4] = "王江泾镇";
        String[] gansudata = {"虹阳港", "后福庄港、荷花兵", "杨家桥港、秀新桥港", "苏州塘秀洲段", "新兵‘毛纹港",};
        mTownDatasMap.put("王江泾镇", gansudata);

        mTownDatas[5] = "王店镇";
        String[] guangdongdata = {"长水塘", "建设港", "莲花桥港", "王店市河", "宣家桥港、太平桥港"};
        mTownDatasMap.put("王店镇", guangdongdata);

        mTownDatas[6] = "油车港镇";
        String[] guangxidata = {"红旗塘(含配套河套)", "三店塘", "下睦港"};
        mTownDatasMap.put("油车港镇", guangxidata);

        mTownDatas[7] = "洪合镇";
        String[] guizhoudata = {"五灵泾港", "横泾桥港、新开河"};
        mTownDatasMap.put("洪合镇", guizhoudata);

        mTownDatas[8] = "新滕塘";
        String[] hainandata = {"新滕塘(含北支)", "濮院港","洛东港","乌镇港", "新滕塘西支", "八字港", "观音桥港", "桃园港", "新农港"};
        mTownDatasMap.put("新滕塘", hainandata);
    }
}
