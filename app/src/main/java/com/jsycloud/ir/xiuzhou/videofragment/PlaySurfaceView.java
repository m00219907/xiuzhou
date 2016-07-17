package com.jsycloud.ir.xiuzhou.videofragment;

import org.MediaPlayer.PlayM4.Player;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

@SuppressLint("NewApi")
public class PlaySurfaceView extends SurfaceView implements Callback {
	
	private final String 	TAG						= "PlaySurfaceView";
	private int m_iWidth = 0;
	private int m_iHeight = 0;
	public int m_iPreviewHandle = -1;
	private int m_iPort = -1;
	private boolean m_bSurfaceCreated = false;

	public PlaySurfaceView(Activity demoActivity) {
		super((Context) demoActivity);
		// TODO Auto-generated constructor stub
		getHolder().addCallback(this);
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		System.out.println("surfaceChanged");
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		m_bSurfaceCreated = true;
		setZOrderOnTop(true);
		getHolder().setFormat(PixelFormat.TRANSLUCENT);
		if (-1 == m_iPort)
		{
			return;
		}
        Surface surface = arg0.getSurface();
        if (true == surface.isValid()) {
        	if (false == Player.getInstance().setVideoWindow(m_iPort, 0, arg0)) {	
        		Log.e(TAG, "Player setVideoWindow failed!");
        	}	
    	}      
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		m_bSurfaceCreated = false;
		if (-1 == m_iPort)
		{
			return;
		}
        if (true == arg0.getSurface().isValid()) 
        {
        	if (false == Player.getInstance().setVideoWindow(m_iPort, 0, null)) 
        	{	
        		Log.e(TAG, "Player setVideoWindow failed!");
        	}
        }
	}
	
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.setMeasuredDimension(m_iWidth - 1, m_iHeight - 1);
    }
	
	public void setParam(int nScreenSize)
	{
		m_iWidth = nScreenSize / 2;
		m_iHeight = (m_iWidth * 3) / 4;
	}
	
	public int getCurWidth()
	{
		return m_iWidth;
	}
	public int getCurHeight()
	{
		return m_iHeight;
	}
	
	public void startPreview(int iUserID, int iChan)
	{
	}
	public void stopPreview()
	{
	}
	private void stopPlayer()
	{
	}

	
	private void processRealData(int iPlayViewNo, int iDataType, byte[] pDataBuffer, int iDataSize, int iStreamMode)
	{
	}
}
