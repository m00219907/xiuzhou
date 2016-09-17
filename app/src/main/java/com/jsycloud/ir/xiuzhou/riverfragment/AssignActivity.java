package com.jsycloud.ir.xiuzhou.riverfragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jsycloud.ir.xiuzhou.CommonTools;
import com.jsycloud.ir.xiuzhou.Constant;
import com.jsycloud.ir.xiuzhou.DialogShow;
import com.jsycloud.ir.xiuzhou.DialogUtils;
import com.jsycloud.ir.xiuzhou.HttpClentLinkNet;
import com.jsycloud.ir.xiuzhou.R;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;

public class AssignActivity extends Activity implements View.OnClickListener{

    TextView assign_activity_river, assign_activity_detail, assign_activity_advice, assign_activity_item,
            assign_activity_who, assign_activity_time, assign_activity_plan, assign_activity_plantime;
    EditText assign_activity_discribe;
    ImageView assign_activity_photo1, assign_activity_photo2, assign_activity_photo3, assign_activity_photo4, assign_activity_photo5;

    private final int PHOTO_REQUEST_CAMERA = 120;// 拍照
    private final int PHOTO_REQUEST_GALLERY = 121;// 从相册中选择

    private final String PHOTO_FILE_NAME = "temp_photo.jpg";

    String base64Str1 = "";
    String base64Str2 = "";
    String base64Str3 = "";
    String base64Str4 = "";
    String base64Str5 = "";

    String filename1 = "";
    String filename2 = "";
    String filename3 = "";
    String filename4 = "";
    String filename5 = "";

    String assignid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assign_activity);
        findViewById(R.id.assign_activity_commit).setOnClickListener(this);

        assign_activity_river = (TextView)findViewById(R.id.assign_activity_river);
        assign_activity_detail = (TextView)findViewById(R.id.assign_activity_detail);
        assign_activity_advice = (TextView)findViewById(R.id.assign_activity_advice);
        assign_activity_item = (TextView)findViewById(R.id.assign_activity_item);
        assign_activity_who = (TextView)findViewById(R.id.assign_activity_who);
        assign_activity_time = (TextView)findViewById(R.id.assign_activity_time);
        assign_activity_plan = (TextView)findViewById(R.id.assign_activity_plan);
        assign_activity_plantime = (TextView)findViewById(R.id.assign_activity_plantime);

        assign_activity_discribe = (EditText)findViewById(R.id.assign_activity_discribe);

        assign_activity_photo1 = (ImageView)findViewById(R.id.assign_activity_photo1);
        assign_activity_photo1.setOnClickListener(this);
        assign_activity_photo1.setTag("1");
        assign_activity_photo2 = (ImageView)findViewById(R.id.assign_activity_photo2);
        assign_activity_photo2.setOnClickListener(this);
        assign_activity_photo2.setTag("0");
        assign_activity_photo3 = (ImageView)findViewById(R.id.assign_activity_photo3);
        assign_activity_photo3.setOnClickListener(this);
        assign_activity_photo3.setTag("0");
        assign_activity_photo4 = (ImageView)findViewById(R.id.assign_activity_photo4);
        assign_activity_photo4.setOnClickListener(this);
        assign_activity_photo4.setTag("0");
        assign_activity_photo5 = (ImageView)findViewById(R.id.assign_activity_photo5);
        assign_activity_photo5.setOnClickListener(this);
        assign_activity_photo5.setTag("0");

        assignid = getIntent().getStringExtra("assignid");
        getAssignInfo(assignid);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.assign_activity_commit:
                if(assign_activity_discribe.getText().toString().isEmpty()){
                    Toast.makeText(AssignActivity.this, "处理过程不能为空!",Toast.LENGTH_SHORT).show();
                }else if(base64Str1.isEmpty()){
                    Toast.makeText(AssignActivity.this, "至少选择一张照片!",Toast.LENGTH_SHORT).show();
                }else {
                    patrolupimg(0);
                }
                break;
            case R.id.assign_activity_photo1:
                if(assign_activity_photo1.getTag().equals("1")){
                    DialogShow.dialogShow3(this, new DialogShow.ICheckedCallBack() {
                        @Override
                        public void OnCheckedCallBackDispath(boolean bSucceed) {
                            if (bSucceed) {
                                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                                photoIntent.setType("image/*");
                                startActivityForResult(photoIntent, PHOTO_REQUEST_GALLERY);
                            } else {
                                Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), PHOTO_FILE_NAME)));
                                startActivityForResult(cameraIntent, PHOTO_REQUEST_CAMERA);
                            }
                        }
                    });
                }
                break;
            case R.id.assign_activity_photo2:
                if(assign_activity_photo2.getTag().equals("1")){
                    DialogShow.dialogShow3(this, new DialogShow.ICheckedCallBack() {
                        @Override
                        public void OnCheckedCallBackDispath(boolean bSucceed) {
                            if (bSucceed) {
                                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                                photoIntent.setType("image/*");
                                startActivityForResult(photoIntent, PHOTO_REQUEST_GALLERY);
                            } else {
                                Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), PHOTO_FILE_NAME)));
                                startActivityForResult(cameraIntent, PHOTO_REQUEST_CAMERA);
                            }
                        }
                    });
                }
                break;
            case R.id.assign_activity_photo3:
                if(assign_activity_photo3.getTag().equals("1")){
                    DialogShow.dialogShow3(this, new DialogShow.ICheckedCallBack() {
                        @Override
                        public void OnCheckedCallBackDispath(boolean bSucceed) {
                            if (bSucceed) {
                                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                                photoIntent.setType("image/*");
                                startActivityForResult(photoIntent, PHOTO_REQUEST_GALLERY);
                            } else {
                                Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), PHOTO_FILE_NAME)));
                                startActivityForResult(cameraIntent, PHOTO_REQUEST_CAMERA);
                            }
                        }
                    });
                }
                break;
            case R.id.assign_activity_photo4:
                if(assign_activity_photo4.getTag().equals("1")){
                    DialogShow.dialogShow3(this, new DialogShow.ICheckedCallBack() {
                        @Override
                        public void OnCheckedCallBackDispath(boolean bSucceed) {
                            if (bSucceed) {
                                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                                photoIntent.setType("image/*");
                                startActivityForResult(photoIntent, PHOTO_REQUEST_GALLERY);
                            } else {
                                Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), PHOTO_FILE_NAME)));
                                startActivityForResult(cameraIntent, PHOTO_REQUEST_CAMERA);
                            }
                        }
                    });
                }
                break;
            case R.id.assign_activity_photo5:
                if(assign_activity_photo5.getTag().equals("1")){
                    DialogShow.dialogShow3(this, new DialogShow.ICheckedCallBack() {
                        @Override
                        public void OnCheckedCallBackDispath(boolean bSucceed) {
                            if (bSucceed) {
                                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                                photoIntent.setType("image/*");
                                startActivityForResult(photoIntent, PHOTO_REQUEST_GALLERY);
                            } else {
                                Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), PHOTO_FILE_NAME)));
                                startActivityForResult(cameraIntent, PHOTO_REQUEST_CAMERA);
                            }
                        }
                    });
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                File tempFile = new File(getFilePathFromUrl(uri));
                File uploadFile = saveBitmapToFile(tempFile);
                if(uploadFile != null && uploadFile.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(uploadFile.getAbsolutePath());
                    setImageUI(myBitmap);
                }
            }
        }else if(requestCode == PHOTO_REQUEST_CAMERA && resultCode == Activity.RESULT_OK){
            File tempFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), PHOTO_FILE_NAME);
            File uploadFile = saveBitmapToFile(tempFile);
            if(uploadFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(uploadFile.getAbsolutePath());
                setImageUI(myBitmap);
            }
        }
    }

    public String getFilePathFromUrl(Uri uri){
        String filePath = "";
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(uri, filePathColumn, null, null, null);
        if(cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(columnIndex);
            cursor.close();
        }
        return filePath;
    }

    public void setImageUI(Bitmap myBitmap){
        if(assign_activity_photo1.getTag().equals("1")) {
            assign_activity_photo1.setTag("2");
            assign_activity_photo1.setImageBitmap(myBitmap);
            assign_activity_photo2.setTag("1");
            assign_activity_photo2.setImageResource(R.drawable.add_image);
            base64Str1 = CommonTools.bitmapToBase64(myBitmap);
        }else if(assign_activity_photo2.getTag().equals("1")){
            assign_activity_photo2.setTag("2");
            assign_activity_photo2.setImageBitmap(myBitmap);
            assign_activity_photo3.setTag("1");
            assign_activity_photo3.setImageResource(R.drawable.add_image);
            base64Str2 = CommonTools.bitmapToBase64(myBitmap);
        }else if(assign_activity_photo3.getTag().equals("1")){
            assign_activity_photo3.setTag("2");
            assign_activity_photo3.setImageBitmap(myBitmap);
            assign_activity_photo4.setTag("1");
            assign_activity_photo4.setImageResource(R.drawable.add_image);
            base64Str3 = CommonTools.bitmapToBase64(myBitmap);
        }else if(assign_activity_photo4.getTag().equals("1")){
            assign_activity_photo4.setTag("2");
            assign_activity_photo4.setImageBitmap(myBitmap);
            assign_activity_photo5.setTag("1");
            assign_activity_photo5.setImageResource(R.drawable.add_image);
            base64Str4 = CommonTools.bitmapToBase64(myBitmap);
        }else if(assign_activity_photo5.getTag().equals("1")){
            assign_activity_photo5.setTag("2");
            assign_activity_photo5.setImageBitmap(myBitmap);
            base64Str5 = CommonTools.bitmapToBase64(myBitmap);
        }
    }

    public File saveBitmapToFile(File file){
        try {
            Bitmap selectedBitmap = CommonTools.decodeBitmap(file.getAbsolutePath());

            File uploadImage = new File(Constant.appFolder, "/1.jpg");
            FileOutputStream outputStream = new FileOutputStream(uploadImage);
            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);
            return uploadImage;
        } catch (Exception e) {
            return null;
        }
    }

    public void getAssignInfo(String assignid) {
        String url = HttpClentLinkNet.BaseAddr + "getassigninfo.php";
        AjaxParams params = new AjaxParams();
        params.put("assignid", assignid);
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
                        assign_activity_river.setText(jsObj.getString("rivername"));
                        assign_activity_detail.setText(jsObj.getString("user") + " " + jsObj.getString("usermobile") + " (" +
                                jsObj.getString("usergroup") + ")");
                        assign_activity_advice.setText(jsObj.getString("item"));
                        assign_activity_item.setText(jsObj.getString("itemdesc"));
                        assign_activity_who.setText(jsObj.getString("depart") + "(联系人：" + jsObj.getString("departcontact") + " " +
                                jsObj.getString("departcontactnum") + ")");
                        assign_activity_time.setText(jsObj.getString("time"));
                        assign_activity_plan.setText(jsObj.getString("plan"));
                        assign_activity_plantime.setText(jsObj.getString("expecttime"));
                    } else {
                        Toast.makeText(AssignActivity.this, "暂无更多", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                Toast.makeText(AssignActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    public void patrolupimg(final int curIndex) {
        if(base64Str1.isEmpty()){
            Toast.makeText(this, "至少上传一张照片", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!DialogUtils.isShowWaitDialog()) {
            DialogUtils.showWaitDialog(this, "上传中...", -1);
        }
        String url = HttpClentLinkNet.BaseAddr + "patroldealupimg.php";
        AjaxParams params = new AjaxParams();
        if(curIndex == 0){
            params.put("photo", base64Str1);
        }else if(curIndex == 1) {
            params.put("photo", base64Str2);
        }else if(curIndex == 2){
            params.put("photo", base64Str3);
        }else if(curIndex == 3){
            params.put("photo", base64Str4);
        }else {
            params.put("photo", base64Str5);
        }
        HttpClentLinkNet.getInstance().sendReqFinalHttp_Post(url, params, new AjaxCallBack() {
            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(Object t) {
                String jsStr = "";
                if (t != null) {
                    jsStr = String.valueOf(t);
                }
                try {
                    JSONObject jsObj = new JSONObject(jsStr);
                    if(curIndex == 0){
                        filename1 = jsObj.getString("filename");
                        if(base64Str2.isEmpty()){
                            reportProblem();
                        }else{
                            patrolupimg(1);
                        }
                    }else if(curIndex == 1){
                        filename2 = jsObj.getString("filename");
                        if(base64Str3.isEmpty()){
                            reportProblem();
                        }else{
                            patrolupimg(2);
                        }
                    }else if(curIndex == 2){
                        filename3 = jsObj.getString("filename");
                        if(base64Str4.isEmpty()){
                            reportProblem();
                        }else{
                            patrolupimg(3);
                        }
                    }else if(curIndex == 3){
                        filename4 = jsObj.getString("filename");
                        if(base64Str5.isEmpty()){
                            reportProblem();
                        }else{
                            patrolupimg(4);
                        }
                    }else {
                        filename5 = jsObj.getString("filename");
                        reportProblem();
                    }
                } catch (JSONException e) {
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                if(curIndex == 0){
                    if(base64Str2.isEmpty()){
                        reportProblem();
                    }else{
                        patrolupimg(1);
                    }
                }else if(curIndex == 1){
                    if(base64Str3.isEmpty()){
                        reportProblem();
                    }else{
                        patrolupimg(2);
                    }
                }else if(curIndex == 2){
                    if(base64Str4.isEmpty()){
                        reportProblem();
                    }else{
                        patrolupimg(3);
                    }
                }else if(curIndex == 3){
                    if(base64Str5.isEmpty()){
                        reportProblem();
                    }else{
                        patrolupimg(4);
                    }
                }else {
                    reportProblem();
                }
            }
        });
    }

    public void reportProblem() {
        String url = HttpClentLinkNet.BaseAddr + "patroldeal.php";
        AjaxParams params = new AjaxParams();
        params.put("assignid", assignid);
        if(Constant.curLocation!=null) {
            params.put("coordinate", Constant.curLocation.getLongitude() + "," + Constant.curLocation.getLatitude());
            params.put("address", Constant.curLocation.getAddress());
        }else{
            params.put("coordinate", "120.707474,30.761567");
            params.put("address", "浙江省嘉兴市秀洲区区政府");
        }
        params.put("process", assign_activity_discribe.getText().toString());
        if(!filename1.isEmpty()){
            params.put("photo1", filename1);
        }
        if(!filename2.isEmpty()){
            params.put("photo2", filename2);
        }
        if(!filename3.isEmpty()){
            params.put("photo3", filename3);
        }
        if(!filename4.isEmpty()){
            params.put("photo4", filename4);
        }
        if(!filename5.isEmpty()){
            params.put("photo5", filename5);
        }
        HttpClentLinkNet.getInstance().sendReqFinalHttp_Post(url, params, new AjaxCallBack() {
            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(Object t) {
                String jsStr = "";
                if (t != null) {
                    jsStr = String.valueOf(t);
                }
                try {
                    Log.e("mahezhen", " reportProblem  sucess   reportProblem: ");
                    JSONObject jsObj = new JSONObject(jsStr);
                    String sucess = jsObj.getString("success");
                    if(sucess.equals("1")){
                        Toast.makeText(AssignActivity.this, "交办成功!",Toast.LENGTH_SHORT).show();
                        AssignActivity.this.finish();
                    }else{
                        Toast.makeText(AssignActivity.this, "交办失败..",Toast.LENGTH_SHORT).show();
                        AssignActivity.this.finish();
                    }
                } catch (JSONException e) {
                }

                if (DialogUtils.isShowWaitDialog()) {
                    DialogUtils.dismissDialog();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                if (DialogUtils.isShowWaitDialog()) {
                    DialogUtils.dismissDialog();
                }
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }
}
