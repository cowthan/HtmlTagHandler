package org.ayo.core.attacher;

import android.app.Activity;
import android.app.Fragment;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.io.Serializable;

/**
 * attach to an activity, everytime you need to create an activity, you actually create an attacher,
 * an attacher will procee all kinds of config and life-circle of an activity,
 * and all this is for zero-claim activity in manifest.
 *
 * PS: Whenever you want to change an attacher object to an activity, all you need to do
 *     is change the parent class to Activity, and define the activity in manifest
 *
 * Created by Administrator on 2016/1/19.
 */
public abstract class ActivityAttacher implements Serializable{

    public static abstract class OnResultCallBack implements Serializable{
        public abstract void onResult(Object t);
    }

    public static final int LAUNCH_MODE_STANDARD = 1;
    public static final int LAUNCH_MODE_SINGLE_TASK = 2;
    public static final int LAUNCH_MODE_SINGLE_TOP = 3;
    public static final int LAUNCH_MODE_SINGLE_INSTANCE = 4;

    public static void startActivity(Context context, Class<? extends ActivityAttacher> attacherClazz, OnResultCallBack onResultCallBack){
        startActivity(context, attacherClazz, SimpleBundle.EMPTY, false, LAUNCH_MODE_STANDARD, onResultCallBack);
    }

    public static void startActivity(Context context, Class<? extends ActivityAttacher> attacherClazz){
        startActivity(context, attacherClazz, SimpleBundle.EMPTY, false, LAUNCH_MODE_STANDARD, null);
    }

    public static void startActivity(Context context, Class<? extends ActivityAttacher> attacherClazz, SimpleBundle bundle, OnResultCallBack onResultCallBack){
        startActivity(context, attacherClazz, bundle, false, LAUNCH_MODE_STANDARD, onResultCallBack);
    }

    public static void startActivity(Context context,
                                     Class<? extends ActivityAttacher> attacherClazz,
                                     SimpleBundle bundle,
                                     boolean needNewStack,
                                     int launchMod){
        startActivity(context, attacherClazz, bundle, needNewStack, launchMod, null);
    }

    public static void startActivity(Context context,
                                     Class<? extends ActivityAttacher> attacherClazz,
                                     SimpleBundle bundle,
                                     boolean needNewStack,
                                     int launchMode,
                                     OnResultCallBack onResultCallBack)
    {
        Class<?> activityClass = null;
        if(launchMode == LAUNCH_MODE_STANDARD){
            activityClass = TmplActivityStandard.class;
        }else if(launchMode == LAUNCH_MODE_SINGLE_TASK){
            activityClass = TmplActivitySingleTask.class;
        }else if(launchMode == LAUNCH_MODE_SINGLE_TOP){
            activityClass = TmplActivitySingleTop.class;
        }else if(launchMode == LAUNCH_MODE_SINGLE_INSTANCE){
            activityClass = TmplActivitySingleInstance.class;
        }else{
            throw new IllegalArgumentException("launchMode不能随便传");
        }
        Intent intent = new Intent(context, activityClass);
        if(needNewStack){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        try {
            int attacherId = AttacherManager.getDefault().addAttacher(attacherClazz.newInstance());
            intent.putExtra("attacher", attacherId);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int bundleId = BundleManager.getDefault().addBundle(bundle);
        intent.putExtra("bundleId", bundleId);

        if(onResultCallBack != null) bundle.putExtra("callback1122334", onResultCallBack);

        context.startActivity(intent);

    }



    private TmplBaseActivity activity;

    public void attach(TmplBaseActivity activity){
        this.activity = activity;
    }

    public TmplBaseActivity getActivity(){
        return this.activity;
    }

    public FragmentManager getSupportFragmentManager() {
        return this.activity.getSupportFragmentManager();
    }


    protected OnResultCallBack getResultCallback(){
        if(getIntent().hasExtra("callback1122334")){
            OnResultCallBack onResultCallBack = getIntent().getExtra("callback1122334");
            return onResultCallBack;
        }else{
            return null;
        }
    }

    protected boolean hasResultCallback(){
        return getIntent().hasExtra("callback1122334");
    }

    ///-------------------
    public void setContentView(int layoutId){
        getActivity().setContentView(layoutId);
    }

    public void setContentView(View view){
        getActivity().setContentView(view);
    }

    public <T extends View> T findViewById(int id){
        return (T)getActivity().findViewById(id);
    }

    public SimpleBundle getIntent(){
        return getActivity().getSimpleBundle();
    }

    public Resources getResources(){
        return getActivity().getResources();
    }

    public MenuInflater getMenuInflater(){
        return getActivity().getMenuInflater();
    }

    public void finish(){
        getActivity().finish();
    }

    public void overridePendingTransition(int enterAnim, int exitAnim){
        getActivity().overridePendingTransition(enterAnim, exitAnim);
    }

    ///-------------------
    protected SimpleBundle getBundle(){
        return getActivity().getSimpleBundle();
    }

    protected void onCreate(Bundle savedInstanceState) {

    }

    protected void onDestroy() {

    }

    protected void onStart() {

    }

    public Boolean onKeyDown(int keyCode, KeyEvent event) {
        return null;
    }

    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

   
    protected void onApplyThemeResource(Resources.Theme theme, int resid, boolean first) {
    
    }

    
    protected void onChildTitleChanged(Activity childActivity, CharSequence title) {
    }

    protected void onNewIntent(Intent intent) {
    }

    protected void onPause() {
    }

    protected void onPostCreate(Bundle savedInstanceState) {
    }

    protected void onPostResume() {
    }

    protected void onRestart() {
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    }

    protected void onResume() {
    }

    protected void onSaveInstanceState(Bundle outState) {
    }

    protected void onStop() {
    }

    protected void onTitleChanged(CharSequence title, int color) {
    }

    protected void onUserLeaveHint() {
    }

//    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
//        return null;
//    }
//
//    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int type) {
//        return null;
//    }

    public Boolean onContextItemSelected(MenuItem item) {
        return null;
    }

    public Boolean onCreateOptionsMenu(Menu menu) {
        return null;
    }

    public Boolean onCreatePanelMenu(int featureId, Menu menu) {
        return null;
    }

    public Boolean onCreateThumbnail(Bitmap outBitmap, Canvas canvas) {
        return null;
    }

    public Boolean onGenericMotionEvent(MotionEvent event) {
        return null;
    }

    public Boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return null;
    }

    public Boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        return null;
    }

    public Boolean onKeyShortcut(int keyCode, KeyEvent event) {
        return null;
    }

    public Boolean onKeyUp(int keyCode, KeyEvent event) {
        return null;
    }

    public Boolean onMenuItemSelected(int featureId, MenuItem item) {
        return null;
    }

    public Boolean onMenuOpened(int featureId, Menu menu) {
        return null;
    }

    public Boolean onNavigateUp() {
        return null;
    }

    public Boolean onNavigateUpFromChild(Activity child) {
        return null;
    }

    public Boolean onOptionsItemSelected(MenuItem item) {
        return null;
    }

    public Boolean onPrepareOptionsMenu(Menu menu) {
        return null;
    }

    public Boolean onPreparePanel(int featureId, View view, Menu menu) {
        return null;
    }

    public Boolean onSearchRequested() {
        return null;
    }

//    public Boolean onSearchRequested(SearchEvent searchEvent) {
//        return null;
//    }

    public Boolean onTouchEvent(MotionEvent event) {
        return null;
    }

    public Boolean onTrackballEvent(MotionEvent event) {
        return null;
    }

    public CharSequence onCreateDescription() {
        return null;
    }

//    public Uri onProvideReferrer() {
//        return null;
//    }

    public View onCreatePanelView(int featureId) {
        return null;
    }

    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return null;
    }

    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return null;
    }

    public void onActionModeFinished(ActionMode mode) {
    }

    public void onActionModeStarted(ActionMode mode) {
    }

//    public void onActivityReenter(int resultCode, Intent data) {
//    }

    public void onAttachedToWindow() {
    }

    public void onAttachFragment(Fragment fragment) {
    }

    public boolean onBackPressed() {
        /**
         * if processed here, return true, or return false to tell activity to call super.onBackPress()
         */
        return false;
    }

    public void onConfigurationChanged(Configuration newConfig) {
    }

    public void onContentChanged() {
    }

    public void onContextMenuClosed(Menu menu) {
    }

//    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
//    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
    }

    public void onCreateNavigateUpTaskStack(TaskStackBuilder builder) {
    }

    public void onDetachedFromWindow() {
    }

//    public void onEnterAnimationComplete() {
//    }

    public void onLowMemory() {
    }

    public void onOptionsMenuClosed(Menu menu) {
    }

    public void onPanelClosed(int featureId, Menu menu) {
    }

//    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
//    }

    public void onPrepareNavigateUpTaskStack(TaskStackBuilder builder) {
    }

//    public void onProvideAssistContent(AssistContent outContent) {
//    }

    public void onProvideAssistData(Bundle data) {
    }

//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//    }

//    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
//    }
//
//    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//    }

//    public void onStateNotSaved() {
//    }

    public void onTrimMemory(int level) {
    }

    public void onUserInteraction() {
    }

//    public void onVisibleBehindCanceled() {
//    }

    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
    }

    public void onWindowFocusChanged(boolean hasFocus) {
    }



}

