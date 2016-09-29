package com.jsycloud.ir.xiuzhou.problemfragment;

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
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jsycloud.ir.xiuzhou.CommonTools;
import com.jsycloud.ir.xiuzhou.Constant;
import com.jsycloud.ir.xiuzhou.DialogShow;
import com.jsycloud.ir.xiuzhou.HttpClentLinkNet;
import com.jsycloud.ir.xiuzhou.R;
import com.jsycloud.ir.xiuzhou.StartActivity;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;

public class TabProblemFragment extends Fragment implements View.OnClickListener{

    private StartActivity activity;
    EditText problem_fragment_river, problem_fragment_discribe, problem_fragment_name, problem_fragment_mobile;
    ImageView problem_fragment_photo1,problem_fragment_photo2,problem_fragment_photo3,problem_fragment_photo4,problem_fragment_photo5;

    TextView problem_fragment_spot;
    private final int PHOTO_REQUEST_CAMERA = 120;// 拍照
    private final int PHOTO_REQUEST_GALLERY = 121;// 从相册中选择

    private final String PHOTO_FILE_NAME = "temp_photo.jpg";
    int curIndex = 0;

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


    @Override
    public void onAttach(Activity activity) {
        this.activity = (StartActivity)activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.problem_fragment, null);

        problem_fragment_river = (EditText)view.findViewById(R.id.problem_fragment_river);
        problem_fragment_discribe = (EditText)view.findViewById(R.id.problem_fragment_discribe);
        problem_fragment_name = (EditText)view.findViewById(R.id.problem_fragment_name);
        problem_fragment_mobile = (EditText)view.findViewById(R.id.problem_fragment_mobile);
        problem_fragment_photo1 = (ImageView)view.findViewById(R.id.problem_fragment_photo1);
        problem_fragment_photo1.setOnClickListener(this);
        problem_fragment_photo1.setTag("1");
        problem_fragment_photo2 = (ImageView)view.findViewById(R.id.problem_fragment_photo2);
        problem_fragment_photo2.setOnClickListener(this);
        problem_fragment_photo2.setTag("0");
        problem_fragment_photo3 = (ImageView)view.findViewById(R.id.problem_fragment_photo3);
        problem_fragment_photo3.setOnClickListener(this);
        problem_fragment_photo3.setTag("0");
        problem_fragment_photo4 = (ImageView)view.findViewById(R.id.problem_fragment_photo4);
        problem_fragment_photo4.setOnClickListener(this);
        problem_fragment_photo4.setTag("0");
        problem_fragment_photo5 = (ImageView)view.findViewById(R.id.problem_fragment_photo5);
        problem_fragment_photo5.setOnClickListener(this);
        problem_fragment_photo5.setTag("0");

        problem_fragment_spot = (TextView)view.findViewById(R.id.problem_fragment_spot);
        String redText = "<font color= '#ff0000'>"+"问题现场"+"</font>";
        problem_fragment_spot.setText(Html.fromHtml(redText));
        TextPaint tp = problem_fragment_spot.getPaint();
        tp.setFakeBoldText(true);

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            if (!Constant.selectRiverId.isEmpty()) {
                curIndex = CommonTools.getRiverIndexByRiverId(Constant.selectRiverId);
                Constant.selectRiverId = "";
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.problem_fragment_photo1:
                if(problem_fragment_photo1.getTag().equals("1")){
                    DialogShow.dialogShow3(activity, new DialogShow.ICheckedCallBack() {
                        @Override
                        public void OnCheckedCallBackDispath(boolean bSucceed) {
                            if (bSucceed) {
                                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                                photoIntent.setType("image/*");
                                activity.startActivityForResult(photoIntent, PHOTO_REQUEST_GALLERY);
                            } else {
                                Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), PHOTO_FILE_NAME)));
                                startActivityForResult(cameraIntent, PHOTO_REQUEST_CAMERA);
                            }
                        }
                    });
                }
                break;
            case R.id.problem_fragment_photo2:
                if(problem_fragment_photo2.getTag().equals("1")){
                    DialogShow.dialogShow3(activity, new DialogShow.ICheckedCallBack() {
                        @Override
                        public void OnCheckedCallBackDispath(boolean bSucceed) {
                            if (bSucceed) {
                                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                                photoIntent.setType("image/*");
                                activity.startActivityForResult(photoIntent, PHOTO_REQUEST_GALLERY);
                            } else {
                                Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), PHOTO_FILE_NAME)));
                                startActivityForResult(cameraIntent, PHOTO_REQUEST_CAMERA);
                            }
                        }
                    });
                }
                break;
            case R.id.problem_fragment_photo3:
                if(problem_fragment_photo3.getTag().equals("1")){
                    DialogShow.dialogShow3(activity, new DialogShow.ICheckedCallBack() {
                        @Override
                        public void OnCheckedCallBackDispath(boolean bSucceed) {
                            if (bSucceed) {
                                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                                photoIntent.setType("image/*");
                                activity.startActivityForResult(photoIntent, PHOTO_REQUEST_GALLERY);
                            } else {
                                Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), PHOTO_FILE_NAME)));
                                startActivityForResult(cameraIntent, PHOTO_REQUEST_CAMERA);
                            }
                        }
                    });
                }
                break;
            case R.id.problem_fragment_photo4:
                if(problem_fragment_photo4.getTag().equals("1")){
                    DialogShow.dialogShow3(activity, new DialogShow.ICheckedCallBack() {
                        @Override
                        public void OnCheckedCallBackDispath(boolean bSucceed) {
                            if (bSucceed) {
                                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                                photoIntent.setType("image/*");
                                activity.startActivityForResult(photoIntent, PHOTO_REQUEST_GALLERY);
                            } else {
                                Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), PHOTO_FILE_NAME)));
                                startActivityForResult(cameraIntent, PHOTO_REQUEST_CAMERA);
                            }
                        }
                    });
                }
                break;
            case R.id.problem_fragment_photo5:
                if(problem_fragment_photo5.getTag().equals("1")){
                    DialogShow.dialogShow3(activity, new DialogShow.ICheckedCallBack() {
                        @Override
                        public void OnCheckedCallBackDispath(boolean bSucceed) {
                            if (bSucceed) {
                                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                                photoIntent.setType("image/*");
                                activity.startActivityForResult(photoIntent, PHOTO_REQUEST_GALLERY);
                            } else {
                                Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), PHOTO_FILE_NAME)));
                                startActivityForResult(cameraIntent, PHOTO_REQUEST_CAMERA);
                            }
                        }
                    });
                }
                break;
//            case R.id.problem_fragment_commit:
//                if(problem_fragment_discribe.getText().toString().isEmpty()){
//                    Toast.makeText(activity, "问题描述不能为空", Toast.LENGTH_SHORT).show();
//                }else {
//                    patrolupimg(0);
//                }
//                break;
            default:
                break;
        }
    }

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

        ContentResolver contentResolver = activity.getContentResolver();
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
        if(problem_fragment_photo1.getTag().equals("1")) {
            problem_fragment_photo1.setTag("2");
            problem_fragment_photo1.setImageBitmap(myBitmap);
            problem_fragment_photo2.setTag("1");
            problem_fragment_photo2.setImageResource(R.drawable.add_image);
            base64Str1 = CommonTools.bitmapToBase64(myBitmap);
        }else if(problem_fragment_photo2.getTag().equals("1")){
            problem_fragment_photo2.setTag("2");
            problem_fragment_photo2.setImageBitmap(myBitmap);
            problem_fragment_photo3.setTag("1");
            problem_fragment_photo3.setImageResource(R.drawable.add_image);
            base64Str2 = CommonTools.bitmapToBase64(myBitmap);
        }else if(problem_fragment_photo3.getTag().equals("1")){
            problem_fragment_photo3.setTag("2");
            problem_fragment_photo3.setImageBitmap(myBitmap);
            problem_fragment_photo4.setTag("1");
            problem_fragment_photo4.setImageResource(R.drawable.add_image);
            base64Str3 = CommonTools.bitmapToBase64(myBitmap);
        }else if(problem_fragment_photo4.getTag().equals("1")){
            problem_fragment_photo4.setTag("2");
            problem_fragment_photo4.setImageBitmap(myBitmap);
            problem_fragment_photo5.setTag("1");
            problem_fragment_photo5.setImageResource(R.drawable.add_image);
            base64Str4 = CommonTools.bitmapToBase64(myBitmap);
        }else if(problem_fragment_photo5.getTag().equals("1")){
            problem_fragment_photo5.setTag("2");
            problem_fragment_photo5.setImageBitmap(myBitmap);
            base64Str5 = CommonTools.bitmapToBase64(myBitmap);
        }
    }

    public void patrolupimg(final int curIndex) {
        if(base64Str1.isEmpty()){
            Toast.makeText(activity, "至少上传一张照片", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = HttpClentLinkNet.BaseAddr + "tipoffupimg.php";
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
                    Log.e("mahezhen", "success: " + curIndex);
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
            }
        });
    }


    public void reportProblem() {
        String url = HttpClentLinkNet.BaseAddr + "tipoff.php";
        AjaxParams params = new AjaxParams();
        params.put("user", problem_fragment_name.getText().toString());
        params.put("mobile", problem_fragment_mobile.getText().toString());
        params.put("riverid", problem_fragment_river.getText().toString());
        params.put("describe", problem_fragment_discribe.getText().toString());
        if(Constant.curLocation!=null) {
            params.put("coordinate", Constant.curLocation.getLongitude() + "," + Constant.curLocation.getLatitude());
            params.put("address", Constant.curLocation.getAddress());
        }else{
            params.put("coordinate", "120.707474,30.761567");
            params.put("address", "浙江省嘉兴市秀洲区区政府");
        }
        params.put("photo1", filename1);
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
                String success;
                if (t != null) {
                    jsStr = String.valueOf(t);
                }

                try {
                    JSONObject jsObj = new JSONObject(jsStr);
                    if (jsObj.has("success")) {
                        success = jsObj.getString("success");
                        if(success.equals("1")){
                            Toast.makeText(activity,"问题上报成功",Toast.LENGTH_SHORT).show();
                            resetUI();
                            DialogShow.dialogShow6(activity, jsObj.getString("tipoffid"));
                        }else{
                            Toast.makeText(activity,"问题上报失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    public void resetUI(){
        curIndex = 0;
        problem_fragment_discribe.setText("");
        problem_fragment_name.setText("");
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
}
