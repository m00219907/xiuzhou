package com.jsycloud.rs.xiuzhou;


import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class DialogShow {

    private static boolean isShowing;

    public static void dialogShow1(Context context, boolean bForce, String updateInfo, final ICheckedCallBack callBack) {

        try {
            if (isShowing){
                return;
            }

            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setCancelable(false);
            alertDialog.show();
            Window window = alertDialog.getWindow();
            window.setContentView(R.layout.dialog_update);
            View update_divide_line = window.findViewById(R.id.update_divide_line);
            View negative_text = window.findViewById(R.id.negative_text);
            View positive_text = window.findViewById(R.id.positive_text);
            TextView message = (TextView) window.findViewById(R.id.message);
            message.setText(updateInfo);
            if(bForce){
                update_divide_line.setVisibility(View.GONE);
                negative_text.setVisibility(View.GONE);
            }

            negative_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    isShowing = false;
                }
            });

            positive_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.OnCheckedCallBackDispath(true);
                    alertDialog.dismiss();
                    isShowing = false;
                }
            });

            isShowing = true;
        } catch (Exception e) {
        }
    }

    public static void dialogShow2(Context context, String reginInfo) {

        try {
            if (isShowing){
                return;
            }

            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setCancelable(false);
            alertDialog.show();
            Window window = alertDialog.getWindow();
            window.setContentView(R.layout.regin_click);
            TextView regin_click_info = (TextView) window.findViewById(R.id.regin_click_info);
            regin_click_info.setText("Äãµã»÷ÁË:" + reginInfo);
            View regin_click_iknow = window.findViewById(R.id.regin_click_iknow);
            regin_click_iknow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    isShowing = false;
                }
            });

            isShowing = true;
        } catch (Exception e) {
        }
    }

    public interface ICheckedCallBack {
        public void OnCheckedCallBackDispath(boolean bSucceed);
    }
}
