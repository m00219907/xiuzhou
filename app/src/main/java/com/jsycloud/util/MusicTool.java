package com.jsycloud.util;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import com.jsycloud.ir.xiuzhou.R;
import com.jsycloud.ir.xiuzhou.AppApplication;

public class MusicTool {
    private SoundPool         soundPool;     // SoundPooll播放池

    private final Context     father;

    HashMap<Integer, Integer> soundMap;      // 声音列表

    private int               loadId = -1;

    public boolean            isPlay = false;

    int                       streamId;


    // Context mContext;
    // 播放ID
    public MusicTool() {
        this.father = AppApplication.get();
    }

    // 初始化音乐播放
    public void initSound() {
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundMap = new HashMap<Integer, Integer>();
        // soundMap.put(1, soundPool.load(c, R.raw.welcom_background, 1));
        // soundMap.put(2, soundPool.load(c, R.raw.r_285, 2));
        SetRes(0, R.raw.phonecall);
        SetRes(1, R.raw.msg);
    }

    /**
     * @param index 音乐池Id
     * @param resid 音乐文件资源号
     */
    public void SetRes(int index, int resid) {
        loadId = soundPool.load(father, resid, 1);
        Log.i("music tool", "loadId = " + loadId);
        soundMap.put(index, loadId);
    }

    /**
     * 播放音乐
     * 
     * @param sound 音乐ID
     * @param loop 循环次数 0--1次 1--2次 -1--循环
     */
    public void playSound(int sound, int loop) {
        /*
         * if(isPlay){ return; }
         */
        // float streamVolumeCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        float volume = 1f;
        // soundPool.resume(soundMap.get(sound));
        if (soundMap == null) { 
        	initSound();
        }
        streamId = soundPool.play(soundMap.get(sound), volume, volume, 1, loop, 1);
        isPlay = true;
        // Log.d("aaaa", "play: "+soundMap.get(sound));
    }

    // 暂停
    public void pauseSound(int sound) {
        soundPool.pause(soundMap.get(sound));

    }

    // 停止播放音乐
    public void stopSound(int sound) {
        if (!isPlay) {
            return;
        }
        soundPool.stop(streamId);
        clear();
        isPlay = false;
        // Log.d("aaaa", "stop: " + soundMap.get(sound));
    }

    public void clear() {
        if (loadId != -1) {
            soundPool.unload(loadId);
        }
        soundPool.release();
        soundPool = null;
        soundMap.clear();
        soundMap = null;
    }
}
