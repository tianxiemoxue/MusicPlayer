package com.example.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.musicplayer.R;

public class SlashScreen {
	public static final int SLASH_LEFT = 1;
	public static final int SLASH_DOWN = 2;
	public static final int SLASH_FADE_OUT = 3;
	private Activity mActivity;
	private Dialog slashDialog;
	public SlashScreen (Activity activity){
		this.mActivity = activity;
	}
	
	public void show(final int imageResource, final int animation){
		Runnable ruannable = new Runnable() {
			
			public void run() {
				DisplayMetrics metrics = new DisplayMetrics();
				
				LinearLayout root = new LinearLayout(mActivity);
				root.setMinimumWidth(metrics.widthPixels);
				root.setMinimumHeight(metrics.heightPixels);
				root.setOrientation(LinearLayout.VERTICAL);
				root.setBackgroundColor(Color.BLACK);
				
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPreferredConfig = Bitmap.Config.ARGB_4444;
				Bitmap bitmap = BitmapFactory.decodeResource(mActivity.getResources(), imageResource, options);
				Drawable drawable = new BitmapDrawable(bitmap);
				root.setBackgroundDrawable(drawable);
				
				slashDialog = new Dialog(mActivity, android.R.style.Theme_Translucent_NoTitleBar);
				if((mActivity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN)
						== WindowManager.LayoutParams.FLAG_FULLSCREEN){
					slashDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
							WindowManager.LayoutParams.FLAG_FULLSCREEN);
				}
				
				Window window = slashDialog.getWindow();
				switch(animation){
				case SLASH_LEFT:
					window.setWindowAnimations(R.style.dialog_anim_slide_left);
					break;
				case SLASH_DOWN:
					window.setWindowAnimations(R.style.dialog_exit_slide_down);
					break;
				case SLASH_FADE_OUT:
					window.setWindowAnimations(R.style.dialog_exit_fade_out);
					break;
				default:
						break;
				}
				
				slashDialog.setContentView(root);
				slashDialog.setCancelable(false);
				slashDialog.show();
				
			}
		};
		mActivity.runOnUiThread(ruannable);
	}
	
	public void removeSlashScreen(){
		if(slashDialog != null &&slashDialog.isShowing()){
			slashDialog.dismiss();
			slashDialog = null;
		}
	}

}
