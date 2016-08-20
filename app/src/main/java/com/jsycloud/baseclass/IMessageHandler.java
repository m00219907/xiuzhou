package com.jsycloud.baseclass;

import android.os.Message;

public interface IMessageHandler {
	 /**
     * 在这里处理异步消息
     * 
     * @param msg
     */
    public void handlerMessage(Message msg);
}
