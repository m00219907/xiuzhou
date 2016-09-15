package com.jsycloud.ir.xiuzhou.riverfragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.jsycloud.ir.xiuzhou.Constant;
import com.jsycloud.ir.xiuzhou.HttpClentLinkNet;
import com.jsycloud.ir.xiuzhou.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MoreActivity extends Activity {

    ImageView more_activity_image1, more_activity_image2, more_activity_image3,
              more_activity_image4, more_activity_image5, more_activity_image6;
    ImageLoader imageLoader;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.more_activity);

        more_activity_image1 = (ImageView)findViewById(R.id.more_activity_image1);
        more_activity_image2 = (ImageView)findViewById(R.id.more_activity_image2);
        more_activity_image3 = (ImageView)findViewById(R.id.more_activity_image3);
        more_activity_image4 = (ImageView)findViewById(R.id.more_activity_image4);
        more_activity_image5 = (ImageView)findViewById(R.id.more_activity_image5);
        more_activity_image6 = (ImageView)findViewById(R.id.more_activity_image6);
        //imageLoader.init();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        getfunction();
    }

    public void getfunction() {
        String url = HttpClentLinkNet.BaseAddr + "getfunction.php";
        AjaxParams params = new AjaxParams();
        params.put("userid", Constant.userid);
        HttpClentLinkNet.getInstance().sendReqFinalHttp_Post(url, params, new AjaxCallBack() {
            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(Object t) {
                String jsStr = "";
                String success = "";

                if (t != null) {
                    jsStr = String.valueOf(t);
                }

                try {
                    JSONObject jsObj = new JSONObject(jsStr);
                    if (jsObj.has("success")) {
                        success = jsObj.getString("success");
                    }
                    if (success.equals("1")) {
                        JSONArray eventArray = jsObj.getJSONArray("function");
                        for(int i = 0;i<eventArray.length();i++){
                            final JSONObject curJSON = eventArray.getJSONObject(i);
                            if(i == 0){
                                more_activity_image1.setVisibility(View.VISIBLE);
                                imageLoader.displayImage(curJSON.getString("image"), more_activity_image1);
                                more_activity_image1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent4 = new Intent(MoreActivity.this, WebviewActivity.class);
                                        try {
                                            intent4.putExtra("url", curJSON.getString("url"));
                                        }catch (Exception e){}
                                        startActivity(intent4);
                                    }
                                });
                            }else if(i == 1){
                                more_activity_image2.setVisibility(View.VISIBLE);
                                imageLoader.displayImage(curJSON.getString("image"), more_activity_image2);
                                more_activity_image2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent4 = new Intent(MoreActivity.this, WebviewActivity.class);
                                        try {
                                            intent4.putExtra("url", curJSON.getString("url"));
                                        }catch (Exception e){}
                                        startActivity(intent4);
                                    }
                                });
                            }else if(i == 2){
                                more_activity_image3.setVisibility(View.VISIBLE);
                                imageLoader.displayImage(curJSON.getString("image"), more_activity_image3);
                                more_activity_image3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent4 = new Intent(MoreActivity.this, WebviewActivity.class);
                                        try {
                                            intent4.putExtra("url", curJSON.getString("url"));
                                        }catch (Exception e){}
                                        startActivity(intent4);
                                    }
                                });
                            }else if(i == 3){
                                more_activity_image4.setVisibility(View.VISIBLE);
                                imageLoader.displayImage(curJSON.getString("image"), more_activity_image4);
                                more_activity_image4.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent4 = new Intent(MoreActivity.this, WebviewActivity.class);
                                        try {
                                            intent4.putExtra("url", curJSON.getString("url"));
                                        }catch (Exception e){}
                                        startActivity(intent4);
                                    }
                                });
                            }else if(i == 4){
                                more_activity_image5.setVisibility(View.VISIBLE);
                                imageLoader.displayImage(curJSON.getString("image"), more_activity_image5);
                                more_activity_image5.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent4 = new Intent(MoreActivity.this, WebviewActivity.class);
                                        try {
                                            intent4.putExtra("url", curJSON.getString("url"));
                                        }catch (Exception e){}
                                        startActivity(intent4);
                                    }
                                });
                            }else if(i == 5){
                                more_activity_image6.setVisibility(View.VISIBLE);
                                imageLoader.displayImage(curJSON.getString("image"), more_activity_image6);
                                more_activity_image6.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent4 = new Intent(MoreActivity.this, WebviewActivity.class);
                                        try {
                                            intent4.putExtra("url", curJSON.getString("url"));
                                        }catch (Exception e){}
                                        startActivity(intent4);
                                    }
                                });
                            }
                        }

                    } else {
                        Toast.makeText(MoreActivity.this, "暂无更多", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                Toast.makeText(MoreActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }
}
