/* 
 * @ProjectName VideoGoJar
 * @Copyright HangZhou Hikvision System Technology Co.,Ltd. All Right Reserved
 * 
 * @FileName CameraListAdapter.java
 * @Description 这里对文件进行描述
 * 
 * @author chenxingyf1
 * @data 2014-7-14
 * 
 * @note 这里写本文件的详细功能描述和注释
 * @note 历史记录
 * 
 * @warning 这里写本文件的相关警告
 */
package com.jsycloud.ir.xiuzhou.videofragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsycloud.ir.xiuzhou.R;
import com.videogo.constant.IntentConsts;
import com.videogo.openapi.bean.EZCameraInfo;
import com.videogo.universalimageloader.core.ImageLoader;
import com.videogo.universalimageloader.core.assist.FailReason;
import com.videogo.universalimageloader.core.listener.ImageLoadingListener;
import com.videogo.util.LogUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * 摄像头列表适配器
 * @author chenxingyf1
 * @data 2014-7-14
 */
public class EZCameraListAdapter extends BaseAdapter {
    private static final String TAG = "CameraListAdapter";

    private Context mContext = null;
    private List<EZCameraInfo> mCameraInfoList = null;
    private ImageLoader mImageLoader;
    private ExecutorService mExecutorService = null;// 线程池
    public Map<String, EZCameraInfo> mExecuteItemMap = null;
    
    /**
     * 自定义控件集合
     * 
     * @author dengsh
     * @data 2012-6-25
     */
    public static class ViewHolder {
        public ImageView iconIv;

        public TextView cameraNameTv;
        View camera_item_rl;
    }
    
    public EZCameraListAdapter(Context context, List<EZCameraInfo> mCameraInfoList) {
        mContext = context;
        this.mCameraInfoList = mCameraInfoList;
        mImageLoader = ImageLoader.getInstance();
        mExecuteItemMap = new HashMap<String, EZCameraInfo>();
    }
    
    public void clearImageCache() {
        mImageLoader.clearMemoryCache();
    }
    
    public void addItem(EZCameraInfo item) {
        mCameraInfoList.add(item);
    }

    public void removeItem(EZCameraInfo item) {
        for(int i = 0; i < mCameraInfoList.size(); i++) {
            if(item == mCameraInfoList.get(i)) {
                mCameraInfoList.remove(i);
            }
        }
    }
    
    public void clearItem() {
        //mExecuteItemMap.clear();
        mCameraInfoList.clear();
    }
    
    /* (non-Javadoc)
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
        return mCameraInfoList.size();
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public EZCameraInfo getItem(int position) {
    	EZCameraInfo item = null;
        if (position >= 0 && getCount() > position) {
            item = mCameraInfoList.get(position);
        }
        return item;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // 自定义视图
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();

            // 获取list_item布局文件的视图
            convertView = LayoutInflater.from(mContext).inflate(R.layout.cameralist_small_item, null);

            // 获取控件对象
            viewHolder.iconIv = (ImageView) convertView.findViewById(R.id.item_icon);
            viewHolder.iconIv.setDrawingCacheEnabled(false);
            viewHolder.iconIv.setWillNotCacheDrawing(true);
            viewHolder.cameraNameTv = (TextView) convertView.findViewById(R.id.camera_name_tv);
            viewHolder.camera_item_rl = convertView.findViewById(R.id.camera_item_rl);
            
            // 设置控件集到convertView
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final EZCameraInfo cameraInfo = getItem(position);
        if(cameraInfo != null) {
            
            viewHolder.cameraNameTv.setText(cameraInfo.getCameraName());   
            //viewHolder.iconIv.setVisibility(View.INVISIBLE);

            String imageUrl = cameraInfo.getPicUrl();
            /*if(!TextUtils.isEmpty(imageUrl)) {
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                        .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                        .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                        .build();//构建完成
                // 依次从内存和sd中获取，如果没有则网络下载
                mImageLoader.displayImage(imageUrl, viewHolder.iconIv, options, mImgLoadingListener);
            }*/

            viewHolder.camera_item_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EZCameraInfo cameraInfo = mCameraInfoList.get(position);
                    Intent intent = new Intent(mContext, EZRealPlayActivity.class);
                    intent.putExtra(IntentConsts.EXTRA_CAMERA_INFO, cameraInfo);
                    mContext.startActivity(intent);
                }
            });
        }
        
        return convertView;
    }

    private final ImageLoadingListener mImgLoadingListener = new ImageLoadingListener() {

        @Override
        public void onLoadingStarted(String imageUri, View view) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            LogUtil.errorLog(TAG, "onLoadingFailed: " + failReason.toString());
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (view != null && view instanceof ImageView && loadedImage != null) {
                ImageView imgView = (ImageView) view;
                imgView.setImageBitmap(loadedImage);
                imgView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {
            // TODO Auto-generated method stub

        }
    };
        
    public void shutDownExecutorService() {
        if (mExecutorService != null) {
            if (!mExecutorService.isShutdown()) {
                mExecutorService.shutdown();
            }
            mExecutorService = null;
        }
    }
}
