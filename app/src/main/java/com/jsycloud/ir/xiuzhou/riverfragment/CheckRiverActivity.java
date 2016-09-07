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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jsycloud.ir.xiuzhou.CommonTools;
import com.jsycloud.ir.xiuzhou.Constant;
import com.jsycloud.ir.xiuzhou.DialogShow;
import com.jsycloud.ir.xiuzhou.DialogUtils;
import com.jsycloud.ir.xiuzhou.HttpClentLinkNet;
import com.jsycloud.ir.xiuzhou.R;
import com.jsycloud.ir.xiuzhou.mapfragment.riverInfo;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class CheckRiverActivity extends Activity implements View.OnClickListener {

    TextView check_river_chooseriver, check_river_touptext, check_river_todowntext;
    RadioButton check_river_item1_image,check_river_item2_image,check_river_item3_image,check_river_item4_image;
    RadioButton check_river_item5_image,check_river_item6_image,check_river_item7_image,check_river_item8_image, check_river_item9_image;
    EditText check_river_item1_desc,check_river_item2_desc,check_river_item3_desc,check_river_item4_desc;
    EditText check_river_item5_desc,check_river_item6_desc,check_river_item7_desc,check_river_item8_desc, check_river_item9_desc;
    ImageView check_river_todown, check_river_toup, check_river_togerther;
    View check_river_photo;
    ImageView check_river_photo1,check_river_photo2,check_river_photo3,check_river_photo4,check_river_photo5;
    View check_river_todownlayout, check_river_touplayout, check_river_togertherlayout;

    private final int CHOOSE_RIVER = 109;// 选择河流
    private final int PHOTO_REQUEST_CAMERA = 120;// 拍照
    private final int PHOTO_REQUEST_GALLERY = 121;// 从相册中选择
    private final String PHOTO_FILE_NAME = "temp_photo.jpg";
    int curIndex = -1;
    int curPhotoIndex = 0;
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

    public static List<riverMaster> depart = new ArrayList<riverMaster>();
    public static List<riverMaster> higherList = new ArrayList<riverMaster>();
    public static List<riverMaster> lowerList = new ArrayList<riverMaster>();
    String report = "";
    String assign = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.check_river);

        check_river_chooseriver = (TextView)findViewById(R.id.check_river_chooseriver);
        check_river_chooseriver.setOnClickListener(this);
        check_river_todown = (ImageView)findViewById(R.id.check_river_todown);
        check_river_todown.setTag("0");
        check_river_todown.setOnClickListener(this);
        check_river_toup = (ImageView)findViewById(R.id.check_river_toup);
        check_river_toup.setTag("0");
        check_river_toup.setOnClickListener(this);
        check_river_togerther = (ImageView)findViewById(R.id.check_river_togerther);
        check_river_togerther.setTag("0");
        check_river_togerther.setOnClickListener(this);
        check_river_touptext = (TextView)findViewById(R.id.check_river_touptext);
        check_river_todowntext = (TextView)findViewById(R.id.check_river_todowntext);

        check_river_photo = findViewById(R.id.check_river_photo);
        check_river_photo1 = (ImageView)findViewById(R.id.check_river_photo1);
        check_river_photo2 = (ImageView)findViewById(R.id.check_river_photo2);
        check_river_photo3 = (ImageView)findViewById(R.id.check_river_photo3);
        check_river_photo4 = (ImageView)findViewById(R.id.check_river_photo4);
        check_river_photo5 = (ImageView)findViewById(R.id.check_river_photo5);

        check_river_item1_image = (RadioButton)findViewById(R.id.check_river_item1_image);
        check_river_item2_image = (RadioButton)findViewById(R.id.check_river_item2_image);
        check_river_item3_image = (RadioButton)findViewById(R.id.check_river_item3_image);
        check_river_item4_image = (RadioButton)findViewById(R.id.check_river_item4_image);
        check_river_item5_image = (RadioButton)findViewById(R.id.check_river_item5_image);
        check_river_item6_image = (RadioButton)findViewById(R.id.check_river_item6_image);
        check_river_item7_image = (RadioButton)findViewById(R.id.check_river_item7_image);
        check_river_item8_image = (RadioButton)findViewById(R.id.check_river_item8_image);
        check_river_item9_image = (RadioButton)findViewById(R.id.check_river_item9_image);

        check_river_item1_desc = (EditText)findViewById(R.id.check_river_item1_desc);
        check_river_item2_desc = (EditText)findViewById(R.id.check_river_item2_desc);
        check_river_item3_desc = (EditText)findViewById(R.id.check_river_item3_desc);
        check_river_item4_desc = (EditText)findViewById(R.id.check_river_item4_desc);
        check_river_item5_desc = (EditText)findViewById(R.id.check_river_item5_desc);
        check_river_item6_desc = (EditText)findViewById(R.id.check_river_item6_desc);
        check_river_item7_desc = (EditText)findViewById(R.id.check_river_item7_desc);
        check_river_item8_desc = (EditText)findViewById(R.id.check_river_item8_desc);
        check_river_item9_desc = (EditText)findViewById(R.id.check_river_item9_desc);

        check_river_todownlayout = findViewById(R.id.check_river_todownlayout);
        check_river_touplayout = findViewById(R.id.check_river_touplayout);
        check_river_togertherlayout = findViewById(R.id.check_river_togertherlayout);

        findViewById(R.id.check_river_back).setOnClickListener(this);
        findViewById(R.id.check_river_commit).setOnClickListener(this);

        //check_river_uploadpic = (MyRectangleView)findViewById(R.id.check_river_uploadpic);
        //check_river_uploadpic.setRectangleColor(0xff2196f3);
        //check_river_uploadpic.settextStr("上传照片");
        //check_river_uploadpic.setOnClickListener(this);
        //check_river_uploadpic.invalidate();
        findViewById(R.id.check_river_uploadpic).setOnClickListener(this);

//        if(Constant.isLogByCode){
//            check_river_todownlayout.setVisibility(View.GONE);
//            check_river_touplayout.setVisibility(View.GONE);
//            check_river_togertherlayout.setVisibility(View.GONE);
//        }else{
//            check_river_todownlayout.setVisibility(View.VISIBLE);
//            check_river_touplayout.setVisibility(View.VISIBLE);
//            check_river_togertherlayout.setVisibility(View.VISIBLE);
//        }

        getriverpatrol();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSE_RIVER && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                int index = data.getExtras().getInt("curIndex", 0);
                if(index != curIndex) {
                    curIndex = index;
                    check_river_chooseriver.setText(Constant.allriverList.get(curIndex).getName());
                    getHigherList();
                    getLowerList();
                    check_river_todown.setImageResource(R.drawable.setting_network_close);
                    check_river_todown.setTag("0");
                    check_river_todowntext.setText("");
                    assign = "";

                    check_river_toup.setImageResource(R.drawable.setting_network_close);
                    check_river_toup.setTag("0");
                    check_river_touptext.setText("");
                    report = "";
                }
            }
        } else if (requestCode == PHOTO_REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                File tempFile = new File(getFilePathFromUrl(uri));
                File uploadFile = saveBitmapToFile(tempFile);
                if(uploadFile!= null && uploadFile.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(uploadFile.getAbsolutePath());
                    setImageUI(myBitmap);
                    curPhotoIndex++;
                }
            }
        }else if(requestCode == PHOTO_REQUEST_CAMERA && resultCode == Activity.RESULT_OK){
            File tempFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), PHOTO_FILE_NAME);
            File uploadFile = saveBitmapToFile(tempFile);
            if(uploadFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(uploadFile.getAbsolutePath());
                setImageUI(myBitmap);
                curPhotoIndex++;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setImageUI(Bitmap myBitmap){
        check_river_photo.setVisibility(View.VISIBLE);
        if(curPhotoIndex == 0) {
            check_river_photo1.setImageBitmap(myBitmap);
            base64Str1 = CommonTools.bitmapToBase64(myBitmap);
        }else if(curPhotoIndex == 1){
            check_river_photo2.setImageBitmap(myBitmap);
            base64Str2 = CommonTools.bitmapToBase64(myBitmap);
        }else if(curPhotoIndex == 2){
            check_river_photo3.setImageBitmap(myBitmap);
            base64Str3 = CommonTools.bitmapToBase64(myBitmap);
        }else if(curPhotoIndex == 3){
            check_river_photo4.setImageBitmap(myBitmap);
            base64Str4 = CommonTools.bitmapToBase64(myBitmap);
        }else if(curPhotoIndex == 4){
            check_river_photo5.setImageBitmap(myBitmap);
            base64Str5 = CommonTools.bitmapToBase64(myBitmap);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.check_river_back:
                finish();
                break;
            case R.id.check_river_chooseriver:
                if(Constant.allriverList.size()==0){
                    Toast.makeText(this,"河流列表为空！",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(this, CheckRiverChooseActivity.class);
                    startActivityForResult(intent, CHOOSE_RIVER);
                }
                break;
            case R.id.check_river_todown:
                if(check_river_todown.getTag().equals("0")){
                    check_river_todown.setImageResource(R.drawable.setting_network_open);
                    check_river_todown.setTag("1");
                    DialogShow.dialogShow4(this, false, new DialogShow.IhigherChoosed() {
                        @Override
                        public void OnChoose(int index) {
                            if (index >= 0 && index < lowerList.size()) {
                                check_river_todowntext.setText(lowerList.get(index).getUserfullname());
                                assign = lowerList.get(index).getUserid();
                            }else if(index == -100){
                                check_river_todown.setImageResource(R.drawable.setting_network_close);
                                check_river_todown.setTag("0");
                                check_river_todowntext.setText("");
                                assign = "";
                            }
                        }
                    });
                }else{
                    check_river_todown.setImageResource(R.drawable.setting_network_close);
                    check_river_todown.setTag("0");
                    check_river_todowntext.setText("");
                    assign = "";
                }
                break;
            case R.id.check_river_toup:
                if(check_river_toup.getTag().equals("0")){
                    check_river_toup.setImageResource(R.drawable.setting_network_open);
                    check_river_toup.setTag("1");
                    DialogShow.dialogShow4(this, true, new DialogShow.IhigherChoosed() {
                        @Override
                        public void OnChoose(int index) {
                            if(index >= 0 && index < depart.size()){
                                check_river_touptext.setText(depart.get(index).getUserfullname());
                                report = depart.get(index).getUserid();
                            }else if(index >= depart.size() && index < depart.size() + higherList.size()){
                                check_river_touptext.setText(higherList.get(index - depart.size()).getUserfullname());
                                report = higherList.get(index - depart.size()).getUserid();
                            }else if(index == -100){
                                check_river_toup.setImageResource(R.drawable.setting_network_close);
                                check_river_toup.setTag("0");
                                check_river_touptext.setText("");
                                report = "";
                            }

                        }
                    });
                }else{
                    check_river_toup.setImageResource(R.drawable.setting_network_close);
                    check_river_toup.setTag("0");
                    check_river_touptext.setText("");
                    report = "";
                }
                break;
            case R.id.check_river_togerther:
                if(check_river_togerther.getTag().equals("0")){
                    check_river_togerther.setImageResource(R.drawable.setting_network_open);
                    check_river_togerther.setTag("1");
                }else{
                    check_river_togerther.setImageResource(R.drawable.setting_network_close);
                    check_river_togerther.setTag("0");
                }
                break;
            case R.id.check_river_uploadpic:
                DialogShow.dialogShow3(this, new DialogShow.ICheckedCallBack() {
                    @Override
                    public void OnCheckedCallBackDispath(boolean bSucceed) {
                        if (bSucceed) {
                            Intent photoIntent = new Intent(Intent.ACTION_PICK);
                            photoIntent.setType("image/*");
                            CheckRiverActivity.this.startActivityForResult(photoIntent, PHOTO_REQUEST_GALLERY);
                        } else {
                            Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), PHOTO_FILE_NAME)));
                            startActivityForResult(cameraIntent, PHOTO_REQUEST_CAMERA);
                        }
                    }
                });
                break;
            case R.id.check_river_commit:
                if(Constant.curLocation == null){
                    Toast.makeText(this, "定位失败，不允许巡河", Toast.LENGTH_SHORT).show();
                } else if(curIndex == -1){
                    Toast.makeText(this, "未选择河流", Toast.LENGTH_SHORT).show();
                }else if(!bCheckSuc()){
                } else {
                    patrolupimg(0);
                }
                break;
            default:
                break;
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

    public void patrolupimg(final int curIndex) {
        if(base64Str1.isEmpty()){
            Toast.makeText(this, "至少上传一张照片", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!DialogUtils.isShowWaitDialog()) {
            DialogUtils.showWaitDialog(this, "上传中...", -1);
        }
        String url = HttpClentLinkNet.BaseAddr + "patrolupimg.php";
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
        String url = HttpClentLinkNet.BaseAddr + "patrol.php";
        AjaxParams params = new AjaxParams();
        params.put("userid", Constant.userid);
        params.put("riverid", Constant.allriverList.get(curIndex).getId());
        if(Constant.curLocation!=null) {
            params.put("coordinate", Constant.curLocation.getLongitude() + "," + Constant.curLocation.getLatitude());
            params.put("address", Constant.curLocation.getAddress());
        }else{
            params.put("coordinate", "120.707474,30.761567");
            params.put("address", "浙江省嘉兴市秀洲区区政府");
        }
        if(!check_river_item1_image.isChecked()){
            params.put("item1", "0");
        }else {
            params.put("item1", "1");
        }
        params.put("item1desc", check_river_item1_desc.getText().toString());
        if(!check_river_item2_image.isChecked()){
            params.put("item2", "0");
        }else {
            params.put("item2", "1");
        }
        params.put("item2desc", check_river_item2_desc.getText().toString());
        if(!check_river_item3_image.isChecked()){
            params.put("item3", "0");
        }else {
            params.put("item3", "1");
        }
        params.put("item3desc", check_river_item3_desc.getText().toString());
        if(!check_river_item4_image.isChecked()){
            params.put("item4", "0");
        }else {
            params.put("item4", "1");
        }
        params.put("item4desc", check_river_item4_desc.getText().toString());
        if(!check_river_item5_image.isChecked()){
            params.put("item5", "0");
        }else {
            params.put("item5", "1");
        }
        params.put("item5desc", check_river_item5_desc.getText().toString());
        if(!check_river_item6_image.isChecked()){
            params.put("item6", "0");
        }else {
            params.put("item6", "1");
        }
        params.put("item6desc", check_river_item6_desc.getText().toString());
        if(!check_river_item7_image.isChecked()){
            params.put("item7", "0");
        }else {
            params.put("item7", "1");
        }
        params.put("item7desc", check_river_item7_desc.getText().toString());
        if(!check_river_item8_image.isChecked()){
            params.put("item8", "0");
        }else {
            params.put("item8", "1");
        }
        params.put("item8desc", check_river_item8_desc.getText().toString());
        if(!check_river_item9_image.isChecked()){
            params.put("flood", "0");
        }else {
            params.put("flood", "1");
        }
        params.put("flooddesc", check_river_item9_desc.getText().toString());
        params.put("entrust", "0");
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
        if(!assign.isEmpty()){
            params.put("assign", assign);
        }else{
            params.put("assign", "0");
        }
        if(!report.isEmpty()){
            params.put("report", report);
        }else{
            params.put("report", "0");
        }
        if(check_river_togerther.getTag().equals("0")){
            params.put("union", "0");
        }else{
            params.put("union", "1");
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
                        Toast.makeText(CheckRiverActivity.this, "巡河成功!",Toast.LENGTH_SHORT).show();
                        CheckRiverActivity.this.finish();
                    }else{
                        Toast.makeText(CheckRiverActivity.this, "巡河失败..",Toast.LENGTH_SHORT).show();
                        CheckRiverActivity.this.finish();
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

    public File saveBitmapToFile(File file){
        try {
            Bitmap selectedBitmap = CommonTools.decodeBitmap(file.getAbsolutePath());

            File uploadImage = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/1.jpg");
            FileOutputStream outputStream = new FileOutputStream(uploadImage);
            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);
            return uploadImage;
        } catch (Exception e) {
            return null;
        }
    }

    public void getriverpatrol() {
        String url = HttpClentLinkNet.BaseAddr + "getriverpatrol.php";
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
                if (t != null) {
                    jsStr = String.valueOf(t);
                }

                try {
                    JSONObject jsObj = new JSONObject(jsStr);
                    String sucess = jsObj.getString("success");
                    Constant.allriverList.clear();
                    if(sucess.equals("1")){
                        JSONArray eventArray = jsObj.getJSONArray("river");
                        for (int i = 0; i < eventArray.length(); i++) {
                            JSONObject curRiverJSON = eventArray.getJSONObject(i);
                            riverInfo curRiverInfo = new riverInfo();
                            curRiverInfo.setId(curRiverJSON.getString("id"));
                            curRiverInfo.setName(curRiverJSON.getString("name"));
                            Constant.allriverList.add(curRiverInfo);
                        }
                        curIndex = 0;
                        check_river_chooseriver.setText(Constant.allriverList.get(0).getName());
                        getHigherList();
                        getLowerList();
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

    public void getHigherList() {
        String url = HttpClentLinkNet.BaseAddr + "getrivermaster.php";
        AjaxParams params = new AjaxParams();
        params.put("userid", Constant.userid);
        params.put("riverid", Constant.allriverList.get(curIndex).getId());
        params.put("master", "1");
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
                    String sucess = jsObj.getString("success");
                    depart.clear();
                    higherList.clear();
                    if(sucess.equals("1")){
                        JSONArray eventArray = jsObj.getJSONArray("user");
                        for (int i = 0; i < eventArray.length(); i++) {
                            JSONObject curRiverJSON = eventArray.getJSONObject(i);
                            riverMaster curRiverMaster = new riverMaster();
                            curRiverMaster.setUserid(curRiverJSON.getString("userid"));
                            curRiverMaster.setUserfullname(curRiverJSON.getString("userfullname"));
                            higherList.add(curRiverMaster);
                        }

                        JSONArray departArray = jsObj.getJSONArray("depart");
                        for (int i = 0; i < departArray.length(); i++) {
                            JSONObject curdepartJSON = departArray.getJSONObject(i);
                            riverMaster curdepart = new riverMaster();
                            curdepart.setUserid(curdepartJSON.getString("dptid"));
                            curdepart.setUserfullname(curdepartJSON.getString("dptname"));
                            depart.add(curdepart);
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

    public void getLowerList() {
        String url = HttpClentLinkNet.BaseAddr + "getrivermaster.php";
        AjaxParams params = new AjaxParams();
        params.put("userid", Constant.userid);
        params.put("riverid", Constant.allriverList.get(curIndex).getId());
        params.put("master", "0");
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
                    String sucess = jsObj.getString("success");
                    lowerList.clear();
                    if(sucess.equals("1")){
                        JSONArray eventArray = jsObj.getJSONArray("user");
                        for (int i = 0; i < eventArray.length(); i++) {
                            JSONObject curRiverJSON = eventArray.getJSONObject(i);
                            riverMaster curRiverMaster = new riverMaster();
                            curRiverMaster.setUserid(curRiverJSON.getString("userid"));
                            curRiverMaster.setUserfullname(curRiverJSON.getString("userfullname"));
                            lowerList.add(curRiverMaster);
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

    boolean bCheckSuc(){
        if(!check_river_item1_image.isChecked() && check_river_item1_desc.getText().length() < 4){
            Toast.makeText(CheckRiverActivity.this, "第1项必须大于四个字!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!check_river_item2_image.isChecked() && check_river_item2_desc.getText().length() < 4){
            Toast.makeText(CheckRiverActivity.this, "第2项必须大于四个字!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!check_river_item3_image.isChecked() && check_river_item3_desc.getText().length() < 4){
            Toast.makeText(CheckRiverActivity.this, "第3项必须大于四个字!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!check_river_item4_image.isChecked() && check_river_item4_desc.getText().length() < 4){
            Toast.makeText(CheckRiverActivity.this, "第4项必须大于四个字!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!check_river_item5_image.isChecked() && check_river_item5_desc.getText().length() < 4){
            Toast.makeText(CheckRiverActivity.this, "第5项必须大于四个字!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!check_river_item6_image.isChecked() && check_river_item6_desc.getText().length() < 4){
            Toast.makeText(CheckRiverActivity.this, "第6项必须大于四个字!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!check_river_item7_image.isChecked() && check_river_item7_desc.getText().length() < 4){
            Toast.makeText(CheckRiverActivity.this, "第7项必须大于四个字!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!check_river_item8_image.isChecked() && check_river_item8_desc.getText().length() < 4){
            Toast.makeText(CheckRiverActivity.this, "第8项必须大于四个字!",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public class riverMaster{
        String userid;
        String userfullname;

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getUserfullname() {
            return userfullname;
        }

        public void setUserfullname(String userfullname) {
            this.userfullname = userfullname;
        }
    }

}
