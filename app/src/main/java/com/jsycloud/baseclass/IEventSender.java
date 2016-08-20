package com.jsycloud.baseclass;

import android.os.Bundle;

public interface IEventSender {
    /**
     * 与activity同步通信
     * 
     * @param eventId
     * @param data
     */
    public void sendToActivity(int eventId, Bundle data);
}
