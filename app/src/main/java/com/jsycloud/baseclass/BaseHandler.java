package com.jsycloud.baseclass;

import java.lang.ref.WeakReference;

import android.os.Handler;
import android.os.Message;

public class BaseHandler extends Handler {
    WeakReference<IMessageHandler> ref;

    public BaseHandler(IMessageHandler handlerImp) {
        ref = new WeakReference<IMessageHandler>(handlerImp);
    }

    @Override
    public void handleMessage(Message msg) {
        if (ref != null && ref.get() != null) {
            ref.get().handlerMessage(msg);
        }
    }
}