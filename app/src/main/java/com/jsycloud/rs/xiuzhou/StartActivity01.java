package com.jsycloud.rs.xiuzhou;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class StartActivity01 extends Activity{

    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 101:
                        Intent intent = new Intent(StartActivity01.this, StartActivity.class);
                        startActivity(intent);
                        finish();
                    break;
                default:
                    break;
            }
        };
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.start_activity01);
        mHandler.sendEmptyMessageDelayed(101, 3000);
    }
}
