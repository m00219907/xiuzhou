package com.jsycloud.rs.xiuzhou.problemfragment;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jsycloud.rs.xiuzhou.Constant;
import com.jsycloud.rs.xiuzhou.DialogShow;
import com.jsycloud.rs.xiuzhou.HttpClentLinkNet;
import com.jsycloud.rs.xiuzhou.MyRectangleView;
import com.jsycloud.rs.xiuzhou.R;
import com.jsycloud.rs.xiuzhou.StartActivity;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class TabProblemFragment extends Fragment implements View.OnClickListener{

    private StartActivity activity;
    TextView problem_fragment_coordinate, problem_fragment_postion, problem_fragment_chooseriver;
    EditText problem_fragment_discribe, problem_fragment_name, problem_fragment_phone;
    ImageView problem_fragment_photo;

    private final int CHOOSE_RIVER = 109;// 选择河流
    private final int PHOTO_REQUEST_CAMERA = 120;// 拍照
    private final int PHOTO_REQUEST_GALLERY = 121;// 从相册中选择

    private final String PHOTO_FILE_NAME = "temp_photo.jpg";

    File uploadFile;

    @Override
    public void onAttach(Activity activity) {
        this.activity = (StartActivity)activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.problem_fragment, null);

        problem_fragment_coordinate = (TextView)view.findViewById(R.id.problem_fragment_coordinate);
        problem_fragment_postion = (TextView)view.findViewById(R.id.problem_fragment_postion);
        problem_fragment_chooseriver = (TextView)view.findViewById(R.id.problem_fragment_chooseriver);
        problem_fragment_chooseriver.setOnClickListener(this);
        problem_fragment_discribe = (EditText)view.findViewById(R.id.problem_fragment_discribe);
        problem_fragment_name = (EditText)view.findViewById(R.id.problem_fragment_name);
        problem_fragment_phone = (EditText)view.findViewById(R.id.problem_fragment_phone);
        problem_fragment_photo = (ImageView)view.findViewById(R.id.problem_fragment_photo);

        MyRectangleView problem_fragment_uploadpic = (MyRectangleView)view.findViewById(R.id.problem_fragment_uploadpic);
        problem_fragment_uploadpic.setRectangleColor(0xff2196f3);
        problem_fragment_uploadpic.settextStr("上传照片");
        problem_fragment_uploadpic.setOnClickListener(this);

        MyRectangleView problem_fragment_commit = (MyRectangleView)view.findViewById(R.id.problem_fragment_commit);
        problem_fragment_commit.setRectangleColor(0xff2196f3);
        problem_fragment_commit.settextStr("提交");
        problem_fragment_commit.setOnClickListener(this);

        if(Constant.curLocation != null){
            problem_fragment_coordinate.setText(Constant.curLocation.getLatitude() + "," + Constant.curLocation.getLongitude());
            problem_fragment_postion.setText(Constant.curLocation.getAddress());
        }else{
            problem_fragment_coordinate.setText("定位失败");
            problem_fragment_postion.setText("定位失败");
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.problem_fragment_chooseriver:
                Intent intent = new Intent(activity, RiverChooseActivity.class);
                activity.startActivityForResult(intent, CHOOSE_RIVER);
                break;
            case R.id.problem_fragment_uploadpic:
                DialogShow.dialogShow3(activity, new DialogShow.ICheckedCallBack(){
                    @Override
                    public void OnCheckedCallBackDispath(boolean bSucceed) {
                        if(bSucceed){
                            Intent photoIntent = new Intent(Intent.ACTION_PICK);
                            photoIntent.setType("image/*");
                            activity.startActivityForResult(photoIntent, PHOTO_REQUEST_GALLERY);
                        }else{
                            Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), PHOTO_FILE_NAME)));
                            startActivityForResult(cameraIntent, PHOTO_REQUEST_CAMERA);
                        }
                    }
                });
                break;
            case R.id.problem_fragment_commit:
                reportProblem();
                break;
            default:
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSE_RIVER && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String riverName = data.getExtras().getString("riverName");
                problem_fragment_chooseriver.setText(riverName);
            }
        } else if (requestCode == PHOTO_REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                File tempFile = new File(getFilePathFromUrl(uri));
                uploadFile = saveBitmapToFile(tempFile);
                if(uploadFile != null && uploadFile.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(uploadFile.getAbsolutePath());
                    problem_fragment_photo.setVisibility(View.VISIBLE);
                    problem_fragment_photo.setImageBitmap(myBitmap);
                }
            }
        }else if(requestCode == PHOTO_REQUEST_CAMERA && resultCode == Activity.RESULT_OK){
            File tempFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), PHOTO_FILE_NAME);
            uploadFile = saveBitmapToFile(tempFile);
            if(uploadFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(uploadFile.getAbsolutePath());
                problem_fragment_photo.setVisibility(View.VISIBLE);
                problem_fragment_photo.setImageBitmap(myBitmap);
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

    public void reportProblem() {
        if(problem_fragment_discribe.getText().toString().isEmpty()){
            Toast.makeText(activity, "问题描述不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(uploadFile == null || !uploadFile.exists()){
            Toast.makeText(activity, "至少上传一张照片", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = HttpClentLinkNet.BaseAddr + "report.php";
        AjaxParams params = new AjaxParams();
        params.put("userid", Constant.userid);
        params.put("riverid", "riverid");
        params.put("describe", problem_fragment_discribe.getText().toString());
        params.put("coordinate", problem_fragment_coordinate.getText().toString());
        params.put("address", problem_fragment_postion.getText().toString());
        try {
            params.put("photo1", uploadFile);
        }catch (Exception e){
        }
        HttpClentLinkNet.getInstance().sendReqFinalHttp_Post(url, params, new AjaxCallBack() {
            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(Object t) {
                String jsStr = "";
                String serverVersion = "1.0.0";
                String updateInfo = "";
                boolean bForce = false;
                String downloadUrl = "";
                if (t != null) {
                    jsStr = String.valueOf(t);
                }

                try {
                    JSONObject jsObj = new JSONObject(jsStr);
                    if (jsObj.has("version")) {
                        serverVersion = jsObj.getString("version");
                    }
                    if (jsObj.has("what")) {
                        updateInfo = jsObj.getString("what");
                    }
                    if (jsObj.has("force")) {
                        bForce = jsObj.getString("force").equals("1");
                    }
                    if (jsObj.has("url")) {
                        downloadUrl = jsObj.getString("url");
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

    public File saveBitmapToFile(File file){
        try {
            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE=75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            File uploadImage = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/1.jpg");
            FileOutputStream outputStream = new FileOutputStream(uploadImage);
            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);
            return uploadImage;
        } catch (Exception e) {
            return null;
        }
    }
}
