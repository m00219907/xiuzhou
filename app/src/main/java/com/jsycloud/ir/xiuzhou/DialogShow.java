package com.jsycloud.ir.xiuzhou;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jsycloud.ir.xiuzhou.riverfragment.CheckRiverActivity;
import com.jsycloud.ir.xiuzhou.riverfragment.ChooseHigherAdapter;

public class DialogShow {

    public static void dialogShow1(Context context, boolean bForce, String updateInfo, String serverVersion, final ICheckedCallBack callBack) {

        try {
            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setCancelable(false);
            alertDialog.show();
            Window window = alertDialog.getWindow();
            window.setContentView(R.layout.dialog_update);
            View update_divide_line = window.findViewById(R.id.update_divide_line);
            View negative_text = window.findViewById(R.id.negative_text);
            View positive_text = window.findViewById(R.id.positive_text);
            TextView update_serverversion = (TextView) window.findViewById(R.id.update_serverversion);
            update_serverversion.setText("最新版本：" + serverVersion);
            TextView update_currentversion = (TextView) window.findViewById(R.id.update_currentversion);
            try {
                PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                if (info != null) {
                    update_currentversion.setText("当前版本：" + info.versionName);
                }
            } catch (Exception e) {
            }
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
                }
            });

            positive_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.OnCheckedCallBackDispath(true);
                    alertDialog.dismiss();
                }
            });

        } catch (Exception e) {
        }
    }

    public static void dialogShow2(Context context, String tittle, String buttonText, final IloginClick callBack) {

        try {

            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setCancelable(false);
            alertDialog.show();
            Window window = alertDialog.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            window.setContentView(R.layout.regin_click);
            final EditText login_code_edit = (EditText)window.findViewById(R.id.login_code_edit);
            TextView login_code_tittle = (TextView)window.findViewById(R.id.login_code_tittle);
            login_code_tittle.setText(tittle);
            TextView login_code_postive = (TextView)window.findViewById(R.id.login_code_postive);
            login_code_postive.setText(buttonText);
            View login_code_cancel = window.findViewById(R.id.login_code_cancel);
            login_code_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

            login_code_postive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.OnClick(login_code_edit.getText().toString());
                    alertDialog.dismiss();
                }
            });

        } catch (Exception e) {
        }
    }

    public static void dialogShow3(Context context, final ICheckedCallBack callBack) {

        try {

            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setCancelable(false);
            alertDialog.show();
            Window window = alertDialog.getWindow();
            window.setContentView(R.layout.choose_picture);
            View choose_picture_gallery = window.findViewById(R.id.choose_picture_gallery);
            View choose_picture_camera = window.findViewById(R.id.choose_picture_camera);
            View choose_picture_cancel = window.findViewById(R.id.choose_picture_cancel);

            choose_picture_gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.OnCheckedCallBackDispath(true);
                    alertDialog.dismiss();
                }
            });

            choose_picture_camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.OnCheckedCallBackDispath(false);
                    alertDialog.dismiss();
                }
            });

            choose_picture_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

        } catch (Exception e) {
        }
    }

    public static void dialogShow4(Context context, final boolean bHigher, final IhigherChoosed callBack) {

        try {

            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.show();
            Window window = alertDialog.getWindow();
            window.setContentView(R.layout.choose_higher);
            ListView choose_higher_list = (ListView)window.findViewById(R.id.choose_higher_list);
            /*View choose_department = LayoutInflater.from(context).inflate(R.layout.choose_department, null);
            TextView choose_department_text1 = (TextView)choose_department.findViewById(R.id.choose_department_text1);
            TextView choose_department_text2 = (TextView)choose_department.findViewById(R.id.choose_department_text2);
            TextView choose_department_text3 = (TextView)choose_department.findViewById(R.id.choose_department_text3);
            TextView choose_department_text4 = (TextView)choose_department.findViewById(R.id.choose_department_text4);
            TextView choose_department_text5 = (TextView)choose_department.findViewById(R.id.choose_department_text5);

            if(bHigher) {
                choose_higher_list.addHeaderView(choose_department);
            }*/
            ChooseHigherAdapter chooseHigherAdapter = new ChooseHigherAdapter(context, bHigher);
            choose_higher_list.setAdapter(chooseHigherAdapter);
            choose_higher_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    callBack.OnChoose(position);
                    alertDialog.dismiss();
                }
            });

            alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                 @Override
                 public void onCancel(DialogInterface dialog) {
                     callBack.OnChoose(-100);
                 }
             });

        } catch (Exception e) {
        }
    }

    public static void dialogShow5(Context context, String rivername,String describe, String address, String time, String status) {

        try {

            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setCancelable(false);
            alertDialog.show();
            Window window = alertDialog.getWindow();
            window.setContentView(R.layout.assign_item);
            TextView assign_item_rivername = (TextView)window.findViewById(R.id.assign_item_rivername);
            TextView assign_item_describe = (TextView)window.findViewById(R.id.assign_item_describe);
            TextView assign_item_address = (TextView)window.findViewById(R.id.assign_item_address);
            TextView assign_item_time = (TextView)window.findViewById(R.id.assign_item_time);
            TextView assign_item_status = (TextView)window.findViewById(R.id.assign_item_status);

            assign_item_rivername.setText(rivername);
            assign_item_describe.setText(describe);
            assign_item_address.setText(address);
            assign_item_time.setText(time);
            if (status.equals("0")) {
                assign_item_status.setText("未处理");
            }else if (status.equals("2")) {
                assign_item_status.setText("处理中");
            }else if (status.equals("3")) {
                assign_item_status.setText("逾期未处理");
            }
            window.findViewById(R.id.assign_item_ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

        } catch (Exception e) {
        }
    }

    public static void dialogShow6(Context context, String tipoffCode) {

        try {
            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setCancelable(false);
            alertDialog.show();
            Window window = alertDialog.getWindow();
            window.setContentView(R.layout.tipoff_layout);
            TextView tipoff_code = (TextView)window.findViewById(R.id.tipoff_code);
            tipoff_code.setText(tipoffCode);
            window.findViewById(R.id.tipoff_iknow).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

        } catch (Exception e) {
        }
    }


    public interface ICheckedCallBack {
        public void OnCheckedCallBackDispath(boolean bSucceed);
    }

    public interface IloginClick {
        public void OnClick(String passcode);
    }

    public interface IhigherChoosed {
        public void OnChoose(int index);
    }
}
