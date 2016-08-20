package com.jsycloud.baseclass;

import android.os.Bundle;

public interface IEventHandler {
    /**
     * 在这里处理来自fragment的消息
     * 
     * @param msgId
     * @param data
     */
    public void handleFragmentEvent(int msgId, Bundle data);
}
