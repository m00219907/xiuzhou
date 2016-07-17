package com.jsycloud.ir.xiuzhou;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Dialog;
import android.content.Context;

public class DialogUtils {
	private static Dialog pd;
	public static LoadingTimeOut ltt = null;
	private static Timer timer = null;
	private static TimerTask timerTask = null;

	public static void showWaitDialog(Context context, String str, long msec) {
		pd = MyLoadingDialog.createLoadingDialog(context, str);
		pd.setCancelable(true);
		pd.setCanceledOnTouchOutside(false);
        MyLoadingDialog.startHitFmAnimat();
		if (msec != -1) {
			timer = new Timer();
			timerTask = new TimerTask() {
				@Override
				public void run() {
					if (isShowWaitDialog()) {
						if (null != ltt) {
							ltt.loadTimeout();
						}
						dismissDialog();

					}
				}
			};
			timer.schedule(timerTask, msec);
		}
		try {
			if (pd != null && !pd.isShowing()) {

				pd.show();

			}
		} catch (Exception e) {
		}
	}

	public static Dialog getPD() {
		return pd;
	}

	public static void showWaitDialog(Context context, String str) {
		try{pd = MyLoadingDialog.createLoadingDialog(context, str);
		pd.setCancelable(true);
		pd.setCanceledOnTouchOutside(false);
		pd.show();
            MyLoadingDialog.startHitFmAnimat();
		}catch(Exception e){
			
		}
	}

	public static void showWaitDialog(Context context, String str,
			final LoadingTimeOut ltt, long msec) {
		pd = MyLoadingDialog.createLoadingDialog(context, str);
		pd.setCancelable(true);
		pd.setCanceledOnTouchOutside(false);
        MyLoadingDialog.startHitFmAnimat();
		if (msec != -1) {
			timer = new Timer();
			timerTask = new TimerTask() {
				@Override
				public void run() {
						if (null != ltt) {
							ltt.loadTimeout();
						}
						dismissDialog();
				}
			};
			timer.schedule(timerTask, msec);
		}
		pd.show();
	}

	public static void dismissDialog() {
		try {
			if (pd != null && pd.isShowing()) {
				cleanModeTimerTask();
				pd.dismiss();
				pd = null;
			}
		} catch (Exception e) {
		}
	}

	public static boolean isShowWaitDialog() {
		if (pd == null) {
			return false;
		}

		return pd.isShowing();
	}

	public interface LoadingTimeOut {
		public void loadTimeout();
	}

	// 关闭模式切换timer
	private static void cleanModeTimerTask() {
		if (timerTask != null) {
			timerTask.cancel();
			timerTask = null;
		}
		if (timer != null) {
			timer.cancel();
			timer.purge();
			timer = null;
		}
		ltt = null;
	}
}
