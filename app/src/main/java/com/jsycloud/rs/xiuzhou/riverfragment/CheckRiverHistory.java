package com.jsycloud.rs.xiuzhou.riverfragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.jsycloud.rs.xiuzhou.R;

public class CheckRiverHistory extends Activity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.change_password);

        findViewById(R.id.change_pwd_back).setOnClickListener(this);
        findViewById(R.id.change_pwd_commit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.change_pwd_back:
                finish();
                break;
            default:
                break;
        }
    }
}
