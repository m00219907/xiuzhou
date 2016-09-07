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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsycloud.ir.xiuzhou.CommonTools;
import com.jsycloud.ir.xiuzhou.DialogShow;
import com.jsycloud.ir.xiuzhou.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class AssignActivity extends Activity implements View.OnClickListener{

    TextView assign_activity_river, assign_activity_detail, assign_activity_advice, assign_activity_name, assign_activity_mobile;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assign_activity);
        findViewById(R.id.assign_activity_commit).setOnClickListener(this);

        assign_activity_river = (TextView)findViewById(R.id.assign_activity_river);
        assign_activity_detail = (TextView)findViewById(R.id.assign_activity_detail);
        assign_activity_advice = (TextView)findViewById(R.id.assign_activity_advice);
        assign_activity_name = (TextView)findViewById(R.id.assign_activity_name);
        assign_activity_mobile = (TextView)findViewById(R.id.assign_activity_mobile);

        assign_activity_discribe = (EditText)findViewById(R.id.assign_activity_discribe);

        assign_activity_photo1 = (ImageView)findViewById(R.id.assign_activity_photo1);
        assign_activity_photo1.setOnClickListener(this);
        assign_activity_photo1.setTag("1");
        assign_activity_photo2 = (ImageView)findViewById(R.id.assign_activity_photo2);
        assign_activity_photo2.setOnClickListener(this);
        assign_activity_photo2.setTag("1");
        assign_activity_photo3 = (ImageView)findViewById(R.id.assign_activity_photo3);
        assign_activity_photo3.setOnClickListener(this);
        assign_activity_photo3.setTag("1");
        assign_activity_photo4 = (ImageView)findViewById(R.id.assign_activity_photo4);
        assign_activity_photo4.setOnClickListener(this);
        assign_activity_photo4.setTag("1");
        assign_activity_photo5 = (ImageView)findViewById(R.id.assign_activity_photo5);
        assign_activity_photo5.setOnClickListener(this);
        assign_activity_photo5.setTag("1");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.assign_activity_commit:
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
