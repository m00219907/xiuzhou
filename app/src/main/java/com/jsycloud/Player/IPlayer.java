package com.jsycloud.Player;

import android.graphics.Camera;
import android.view.SurfaceView;

import com.jsycloud.Player.Player.PlayerStatus;

public interface IPlayer {
	
	public void setCamera(Camera Camera);
	public Camera getCamera();
	public void attachWindwow(IWindow window);
	public void detachWindow();
	public int play();
	public int stop();
	public int playAudio();
	public int stopAudio();
	public int snapShot(String filePath);
	public int startRecord(String filePath);
	public int stopRecord();
	public boolean setPlaySpeed(float speed);
	public float getPlaySpeed();
	public PlayerStatus getPlayerStatus();
	public void translate(float x, float y);
	public float getTranslateX();
	public float getTranslateY();
	public void scale(float scale);
	public float getScale();
	public void setMaxScale(float maxScale);
	public boolean setIdentity();
	public void reInitView(SurfaceView view);
	public void setPlayerListener(IPlayerListener listener);
	public int seek(Time time);
	public void pause();
	public void resume();
	public void playNextFrame();
	public void addFlag(Object name, Object value);
	public Object getFlag(Object name);
	public int getIndex();
	public void setIndex(int index);
}
