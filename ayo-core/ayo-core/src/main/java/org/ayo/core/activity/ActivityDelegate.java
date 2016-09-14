package org.ayo.core.activity;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.ayo.core.systembar.SystemBarTintManager;


class ActivityDelegate {
	
	private Activity killer;
	public ActivityDelegate(){
	}
	public Activity getActivity(){
		return killer;
	}
	private void check(){
		if(killer == null){
			throw new IllegalStateException("ActivityAttacher所代理的Activity为空");
		}
	}
	private boolean checkSilencely(){
		try {
			check();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 需要在onCreate里调用
	 * @param activity
	 */
	public void attach(Activity activity){
		this.killer = activity;
	}
	/**
	 * 需要在onDestroy里调用
	 */
	public void detach(){
		this.killer = null;
	}


	/**
	 * 是否给status bar和navigate bar留出位置，true表示留
	 * @param enable
     */
	protected void enableSystemBarTakenByContent(boolean enable){

		checkLayout((ViewGroup) getActivity().getWindow().getDecorView(), "");

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			ViewGroup content = (ViewGroup) getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
			View contentRoot = content.getChildAt(0);
			Log.i("111", contentRoot.getClass().getSimpleName());
			contentRoot.setFitsSystemWindows(!enable);
			((ViewGroup) contentRoot).setClipChildren(false);
			((ViewGroup) contentRoot).setClipToPadding(false);

		}else {
			Toast.makeText(getActivity(), "不支持SystemBar一体化", Toast.LENGTH_LONG).show();
		}
	}


	public static void checkLayout(ViewGroup root, String prefix){
		if(root == null) return;
		for(int i = 0; i < root.getChildCount(); i++){
			View v = root.getChildAt(i);
			Log.i("111", prefix + ": " + v.getClass().getSimpleName());
			if(v instanceof ViewGroup){
				checkLayout(((ViewGroup) v), "    " + prefix);
			}
		}
	}

	protected void renderSystemBar(int statusBarColor, int navigateBarColor){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

			Window win = getActivity().getWindow();
			WindowManager.LayoutParams winParams = win.getAttributes();
			final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
			if (true) {
				winParams.flags |= bits;
			} else {
				winParams.flags &= ~bits;
			}
			win.setAttributes(winParams);


			SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintColor(statusBarColor);

			tintManager.setNavigationBarTintEnabled(true);
			tintManager.setNavigationBarTintColor(navigateBarColor);
		}else{

		}
	}


//	public void fullScreenColor(Activity context, int color) {
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			Window win = context.getWindow();
//			WindowManager.LayoutParams winParams = win.getAttributes();
//			final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//			if (true) {
//				winParams.flags |= bits;
//			} else {
//				winParams.flags &= ~bits;
//			}
//			win.setAttributes(winParams);
//			SystemBarTintManager mTintManager = new SystemBarTintManager(context);
//			mTintManager.setStatusBarTintEnabled(true);
//			mTintManager.setNavigationBarTintEnabled(true);
//			mTintManager.setTintColor(color);
//
//		}
//	}

}
