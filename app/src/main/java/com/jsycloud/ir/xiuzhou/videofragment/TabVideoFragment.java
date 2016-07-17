package com.jsycloud.ir.xiuzhou.videofragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.jsycloud.ir.xiuzhou.Constant;
import com.jsycloud.ir.xiuzhou.R;
import com.jsycloud.ir.xiuzhou.StartActivity;
import com.videogo.exception.BaseException;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.bean.EZCameraInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TabVideoFragment extends Fragment implements View.OnClickListener {

    private StartActivity activity;
    Spinner video_fragment_spinnertown, video_fragment_spinnerriver;
    private String[] mTownDatas = new String[9];
    private ArrayList<String> mRiverDatas = new ArrayList<String>();
    private HashMap<String, String[]> mTownDatasMap = new HashMap<String, String[]>();
    int curTownIndex = 0;
    int curRiverIndex = 0;

    private EZOpenSDK mEZOpenSDK = null;

    SwipeRefreshLayout myplayers_ptr_frame;

    private ListView mListView = null;
    private EZCameraListAdapter mAdapter = null;

    List<EZCameraInfo> result = new ArrayList<EZCameraInfo>();

    private View mFooterView;
    private ImageView mAnimationView;
    private int mCurrentScrollState;
    private boolean p_is_last = false;
    private boolean b_requestComment = false;

    private Handler mHandler = new Handler(new Handler.Callback(){
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1003:
                    mAdapter.notifyDataSetChanged();
                    myplayers_ptr_frame.setRefreshing(false);
                    hideFooterView();
                    b_requestComment = false;
                    break;
                default:
                    break;
            }
            return true;
        }
    });

    @Override
    public void onAttach(Activity activity) {
        this.activity = (StartActivity)activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cameralist_page, null);
        /*video_fragment_spinnertown = (Spinner)view.findViewById(R.id.video_fragment_spinnertown);
        video_fragment_spinnerriver = (Spinner)view.findViewById(R.id.video_fragment_spinnerriver);
        setupData();

        final ArrayAdapter<String> riverAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, mRiverDatas);
        riverAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        video_fragment_spinnerriver.setAdapter(riverAdapter);
        video_fragment_spinnerriver.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                curRiverIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                curRiverIndex = 0;
            }
        });

        ArrayAdapter<String> townAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, mTownDatas);
        townAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        video_fragment_spinnertown.setAdapter(townAdapter);
        video_fragment_spinnertown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                curTownIndex = position;
                mRiverDatas.clear();
                for (int i = 0; i < mTownDatasMap.get(mTownDatas[position]).length; i++) {
                    mRiverDatas.add(mTownDatasMap.get(mTownDatas[position])[i]);
                }
                riverAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                curTownIndex = 0;
                mRiverDatas.clear();
                for (int i = 0; i < mTownDatasMap.get(mTownDatas[0]).length; i++) {
                    mRiverDatas.add(mTownDatasMap.get(mTownDatas[0])[i]);
                }
                riverAdapter.notifyDataSetChanged();
            }
        });

        MyRectangleView video_fragment_startvideo = (MyRectangleView)view.findViewById(R.id.video_fragment_startvideo);
        video_fragment_startvideo.setRectangleColor(0xff2196f3);
        video_fragment_startvideo.settextStr("查看视频");
        video_fragment_startvideo.setOnClickListener(this);*/
        mEZOpenSDK = EZOpenSDK.getInstance();
        //mEZOpenSDK.openLoginPage();
        mEZOpenSDK.setAccessToken(Constant.accessToken);

        myplayers_ptr_frame = (SwipeRefreshLayout)view.findViewById(R.id.myplayers_ptr_frame);
        myplayers_ptr_frame.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                result.clear();
                getCommentList();
            }
        });

        mListView = (ListView) view.findViewById(R.id.camera_listview);
        mFooterView = activity.getLayoutInflater().inflate(R.layout.loading_dialog, null);
        mFooterView.setPadding(0, 0, 0, 100);
        mAnimationView = (ImageView)mFooterView.findViewById(R.id.dialog_view);
        //mListView.addFooterView(mFooterView);
        /*mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                mCurrentScrollState = scrollState;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (visibleItemCount == totalItemCount) {
                    hideFooterView();
                } else if (firstVisibleItem + visibleItemCount >= totalItemCount
                        && mCurrentScrollState != SCROLL_STATE_IDLE
                        && !b_requestComment
                        && !p_is_last) {
                    b_requestComment = true;
                    showFooterView();
                    getCommentList();
                }
            }
        });*/
        initView();
        return view;
    }

    private void initView() {

        mAdapter = new EZCameraListAdapter(activity, result);
        mListView.setAdapter(mAdapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        getCommentList();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.video_fragment_startvideo:
                Intent intent = new Intent(activity, VideoActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void getCommentList() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<EZCameraInfo> curResult = null;
                    //if(result.size()==0) {
                        curResult = mEZOpenSDK.getCameraList(0, 30);
                    //} else {
                        //curResult = mEZOpenSDK.getCameraList(result.size()/20, 20);
                    //}

                    if(curResult != null) {
                        if (curResult.size() < 20) {
                            p_is_last = true;
                        }
                        result.clear();
                        result.addAll(curResult);
                    }
                    mHandler.sendEmptyMessage(1003);

                } catch (BaseException e) {
                    mHandler.sendEmptyMessage(1003);
                }
            }
        }).start();
    }

    private void hideFooterView() {
        mFooterView.setVisibility(View.GONE);
    }
    private void showFooterView() {
        mFooterView.setVisibility(View.VISIBLE);
        Animation rocketAnimation = AnimationUtils.loadAnimation(activity, R.anim.loading_animation);
        mAnimationView.startAnimation(rocketAnimation);
    }
}
