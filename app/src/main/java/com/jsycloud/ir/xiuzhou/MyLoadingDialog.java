package com.jsycloud.ir.xiuzhou;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MyLoadingDialog {

    public static Animation rocketAnimation;
    public static ImageView spaceshipImage;

	public static Dialog createLoadingDialog(Context context, String msg) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.loading_dialog, null);
        spaceshipImage = (ImageView) v.findViewById(R.id.dialog_view);

        rocketAnimation = AnimationUtils.loadAnimation(context, R.anim.loading_animation);

		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);

		WindowManager.LayoutParams lp = loadingDialog.getWindow().getAttributes();
		lp.width=80;
		lp.height=40;
		loadingDialog.getWindow().setAttributes(lp);
		loadingDialog.setCancelable(false);
		loadingDialog.setContentView(v, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		return loadingDialog;

	}

    public static void startHitFmAnimat(){
        spaceshipImage.startAnimation(rocketAnimation);
    }
}
