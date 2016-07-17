package com.jsycloud.ir.xiuzhou.mapfragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jsycloud.ir.xiuzhou.DialogShow;
import com.jsycloud.ir.xiuzhou.R;

import java.util.ArrayList;
import java.util.List;


public class AllRiverActivity extends Activity{

    TextView allriver_curriver;
    ListView allriver_list;
    AllRiverAdapter allRiverAdapter;
    List<String> riverNames = new ArrayList<String>();
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1003:
                    riverNames.add("南湖区");
                    riverNames.add("秀洲区");
                    riverNames.add("秀洲新区");
                    riverNames.add("王江泾镇");
                    riverNames.add("王店镇");
                    riverNames.add("油车港镇");
                    riverNames.add("油车港镇");
                    riverNames.add("洪合镇");
                    riverNames.add("新塍镇");
                    allRiverAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        };
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.allriver_activity);

        allriver_curriver = (TextView)findViewById(R.id.allriver_curriver);
        allriver_list = (ListView)findViewById(R.id.allriver_list);
        allRiverAdapter = new AllRiverAdapter(this, riverNames);
        allriver_list.setAdapter(allRiverAdapter);
        mHandler.sendEmptyMessageDelayed(1003, 500);
        findViewById(R.id.allriver_right_blank).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllRiverActivity.this.finish();
            }
        });
    }
}
