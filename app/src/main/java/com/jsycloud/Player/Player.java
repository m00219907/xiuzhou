package com.jsycloud.Player;

/**
 * 
 */

import java.util.HashMap;
import java.util.Map;

import android.graphics.Camera;
import android.util.Log;
import android.view.SurfaceView;

import com.company.PlaySDK.IPlaySDK;
import com.company.PlaySDK.IPlaySDKCallBack.fCBDecode;

/**
 * @author 13098
 * 
 */
public abstract class Player implements IPlayer, fCBDecode
{
	public enum PlayerStatus
	{
		PLAYING, // playing now
		STOPED, // stop now
		PAUSE, // pause now
		SEEKING, //seeking now
	}
	
	/**
	 * player listener
	 */
	protected IPlayerListener playerListener; 
	
	/**
	 * window to display
	 */
	protected IWindow window;  
	
	/**
	 * port for PlaySDK
	 */
	protected int playPort;
	
	/**
	 * status of player
	 */
	protected PlayerStatus status;
	
	/**
	 * buffer size of PLAYSDK
	 */
	private static int PLAYSDK_BUF_SIZE;
	
	/**
	 * index of player
	 */
	private int index;
	
	/**
	 * flag map
	 */
	private Map<Object, Object> flagMap;
	
	/**
	 * camera description
	 */
	private Camera camera;
	
	/**
	 * is window attached
	 */
	protected boolean isAttached;
	
	/**
	 * play speed
	 */
	private float playSpeed;

	/**
	 * max scale value
	 */
	private float maxScale;
	
	/**
	 * current time of player
	 */
	private Time curTime;
	
	/**
	 * whether stream is arrived
	 */
	private boolean isStreamArrived;
	
	static
	{
		PLAYSDK_BUF_SIZE = 1500 * 1024;
	}

	public Player()
	{
		status = PlayerStatus.STOPED;
		playPort = -1;
		flagMap = new HashMap<Object, Object>();
		isAttached = false;
		playSpeed = 1.0f;
		maxScale = 8.0f;
		curTime = null;
		isStreamArrived = false;
	}

	@Override
	public void setCamera(Camera Camera)
	{
		this.camera = Camera;
	}

	@Override
	public Camera getCamera()
	{
		return this.camera;
	}

	@Override
	public void attachWindwow(IWindow window)
	{

		// just set window
		this.window = window;
	}

	@Override
	public void detachWindow()
	{
		this.window = null;
		if (playPort == -1)
		{
			return;
		}

		// close stream
		IPlaySDK.PLAYCloseStream(playPort);

		// stop PlaySDK
		IPlaySDK.PLAYStop(this.playPort);

		// reset port
		this.playPort = -1;
	}

	@Override
	public int play()
	{
		// get free port
		int port = IPlaySDK.PLAYGetFreePort();
		if (port == -1)
		{
			return 0;
		}

		this.playPort = port;
		IPlaySDK.PLAYSetStreamOpenMode(this.playPort, 1);
		IPlaySDK.PLAYOpenStream(this.playPort, null, 0, PLAYSDK_BUF_SIZE);

		// TODO:call window.getDispalyView() to decide use which view
		SurfaceView sv = (SurfaceView) window.getDispalyView();

		// call PlaySDK
		int ret = IPlaySDK.PLAYPlay(this.playPort, sv);
		if (ret == 0)
		{
			Log.d(this.toString(), "PLAYPlay NG");
			return 0;
		}
		Log.d(this.toString(), "PLAYPlay OK");
		
		// set decode time callback
		//IPlaySDK.PLAYSetVisibleDecodeCallBack(this.playPort, this, null);
		//IPlaySDK.PLAYSetVisibleDecCallBack(this.playPort, this, 0);
		IPlaySDK.PLAYSetVisibleDecodeCallBack(this.playPort, this, 0);
		return Err.OK;
	}

	@Override
	public int stop()
	{
		if (this.status == PlayerStatus.STOPED)
		{
			return Err.NG;
		}
		
		isStreamArrived = false;
		
		int playRet = IPlaySDK.PLAYStop(this.playPort);
		int closeRet = IPlaySDK.PLAYCloseStream(this.playPort);
		if (playRet == 0 || closeRet == 0)
		{
			Log.d("Define.TAG_PC" + this.toString(), "PLAYStop OR  PLAYCloseStream NG");
			return Err.NG;
		}
		Log.d("Define.TAG_PC" + this.toString(), "PLAYStop and  PLAYCloseStream OK");
		return Err.OK;
	}

	@Override
	public int playAudio()
	{
		if (this.status == PlayerStatus.STOPED)
		{
			return Err.NG;
		}
		return IPlaySDK.PLAYPlaySound(this.playPort);
	}

	@Override
	public int stopAudio()
	{
		if (this.status == PlayerStatus.STOPED)
		{
			return Err.NG;
		}
		return IPlaySDK.PLAYStopSound();
	}

	@Override
	public int snapShot(String filePath)
	{
		if (this.status == PlayerStatus.STOPED)
		{
			return Err.NG;
		}
		int ret = IPlaySDK.PLAYCatchPic(this.playPort, filePath);
		Log.d(this.toString(), "snapShot:" + ((ret == Err.OK) ? "OK" : "NG"));
		return ret;
	}

	@Override
	public int startRecord(String filePath)
	{
		if (this.status == PlayerStatus.STOPED)
		{
			return Err.NG;
		}
		int ret = IPlaySDK.PLAYStartDataRecord(this.playPort, filePath, 0, null, 0);
		//int ret = IPlaySDK.PLAYStartDataRecord(this.playPort, filePath, 0);
		Log.d(this.toString(), "startRecord:" + ((ret == Err.OK) ? "OK" : "NG"));
		return ret;
	}

	@Override
	public int stopRecord()
	{
		if (this.status == PlayerStatus.STOPED)
		{
			return Err.NG;
		}
		int ret = IPlaySDK.PLAYStopDataRecord(this.playPort);
		Log.d(this.toString(), "stopRecord:" + ((ret == Err.OK) ? "OK" : "NG"));
		return ret;
	}

	@Override
	public boolean setPlaySpeed(float speed)
	{
		if (this.status == PlayerStatus.STOPED)
		{
			return false;
		}
		int ret = IPlaySDK.PLAYSetPlaySpeed(this.playPort, speed);
		if (ret != 0)
		{
			this.playSpeed = speed;
		}
		Log.d(this.toString(), "stopRecord:" + ((ret == 0) ? "OK" : "NG"));
		return (ret == 0);
	}

	@Override
	public float getPlaySpeed()
	{
		return this.playSpeed;
	}
	
	

	@Override
	public PlayerStatus getPlayerStatus()
	{
		return this.status;
	}

	@Override
	public void translate(float x, float y)
	{
		if (this.status == PlayerStatus.STOPED)
		{
			return;
		}
		IPlaySDK.PLAYTranslate(this.playPort, x, y, 0);
	}

	@Override
	public float getTranslateX()
	{
		if (this.status == PlayerStatus.STOPED)
		{
			return -1.0f;
		}
		return IPlaySDK.PLAYGetTranslateX(this.playPort, 0);
	}

	@Override
	public float getTranslateY()
	{
		if (this.status == PlayerStatus.STOPED)
		{
			return -1.0f;
		}
		return IPlaySDK.PLAYGetTranslateY(this.playPort, 0);
	}

	@Override
	public void scale(float scale)
	{
		if (this.status == PlayerStatus.STOPED)
		{
			return;
		}
		
		// check max scale value
		float curScaleVal = IPlaySDK.PLAYGetScale(this.playPort, 0);
		curScaleVal *= scale;
		if(curScaleVal > maxScale)
		{
			return;
		}
		Log.d("test", "scaleValue=" + curScaleVal);
		
		// set new scale value
		IPlaySDK.PLAYScale(this.playPort, scale, 0);
	}

	@Override
	public float getScale()
	{
		if (this.status == PlayerStatus.STOPED)
		{
			return -1.0f;
		}
		return IPlaySDK.PLAYGetScale(this.playPort, 0);
	}
	
	
	@Override
	public void setMaxScale(float maxScale)
	{
		this.maxScale = maxScale;
	}

	@Override
	public boolean setIdentity()
	{
		if (this.status == PlayerStatus.STOPED)
		{
			return false;
		}
		return IPlaySDK.PLAYSetIdentity(this.playPort, 0) == 0 ? false : true;
	}

	@Override
	public void reInitView(SurfaceView view)
	{
		if (this.status == PlayerStatus.STOPED)
		{
			return;
		}
		IPlaySDK.InitSurface(this.playPort, view);
	}

	@Override
	public void setPlayerListener(IPlayerListener listener)
	{
		this.playerListener = listener;
	}

	@Override
	public int seek(Time time)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void pause()
	{
		if (this.status == PlayerStatus.STOPED)
		{
			return;
		}
		IPlaySDK.PLAYPause(this.playPort, (short) 1);
	}

	@Override
	public void resume()
	{
		if (this.status == PlayerStatus.STOPED)
		{
			return;
		}
		IPlaySDK.PLAYPause(this.playPort, (short) 0);
	}

	@Override
	public void playNextFrame()
	{
		if (this.status == PlayerStatus.STOPED)
		{
			return;
		}
		IPlaySDK.PLAYOneByOne(this.playPort);
	}

	@Override
	public void addFlag(Object name, Object value)
	{
		flagMap.put(name, value);
	}

	@Override
	public Object getFlag(Object name)
	{
		return flagMap.get(name);
	}

	@Override
	public void setIndex(int index)
	{
		this.index = index;
	}

	@Override
	public int getIndex()
	{
		return this.index;
	}
	
  /**
   * invoke from play SDK, tell us the current player time
   */
  /*public void invoke(int arg0, com.company.PlaySDK.IPlaySDKCallBack.FRAME_DECODE_INFO decodeInfo, com.company.PlaySDK.IPlaySDKCallBack.FRAME_INFO_EX frameInfo, long arg3)
  {
	  Time time = new Time(frameInfo.nYear, frameInfo.nMonth, frameInfo.nDay, frameInfo.nHour, frameInfo.nMinute, frameInfo.nSecond);
	  if(this.playerListener != null)
	  {
		  if((this.curTime == null) || !(this.curTime.equals(time)))
		  {
			  this.playerListener.onPlayerTime(this, time);
			  this.curTime = time;
		  }
	  }
  }*/

  /**
   * 
   * @param data
   * @param length
   * @return
   */
	protected boolean inpudata(byte[] data, int length)
	{
		// if not in playing status, just return
		if(this.status != PlayerStatus.PLAYING)
		{
			return false;
		}
		
		// if stream come first time, tell user
		if(!isStreamArrived)
		{
			isStreamArrived = true;
			if(this.playerListener != null)
			{
				this.playerListener.onStreamArrived(this);
			}
		}
		
		// input data to play SDK
		int ret = IPlaySDK.PLAYInputData(playPort, data, length);
		if (ret == 0)
		{
			 Log.d(this.toString(), "PLAYInputData failed");
		}
		return ret == 0 ? false : true;
	}
}
