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
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import org.ayo.core.activity.AyoActivity;

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
public abstract class TmplBaseActivity extends AyoActivity {

    private ActivityAttacher attacher;
    private SimpleBundle bundle;

    public ActivityAttacher getActivityAttacher(){
        return attacher;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            onCreateSilence(savedInstanceState);
        }catch (Exception e){
            e.printStackTrace();
            if(attacher != null){
                throw new RuntimeException(attacher.getClass().getName() + "初始化时出问题了");
            }else{
                throw new RuntimeException("attacher不能是空");
            }
        }
    }

    protected void onCreateSilence(Bundle savedInstanceState) {

        int attacherId = getIntent().getIntExtra("attacher", 0);
        attacher = AttacherManager.getDefault().getAttacher(attacherId);
        attacher.attach(this);
        AttacherManager.getDefault().removeAttacher(attacherId);

        int bundleId = getIntent().getIntExtra("bundleId", 0);
        if(bundleId > 0){
            bundle = BundleManager.getDefault().getBundle(bundleId);
            BundleManager.getDefault().removeBundle(bundleId);
        }

        attacher.onCreate(savedInstanceState);

        ///how to get an attacher
//        if(getIntent() != null && getIntent().getExtras() != null){
//            if(getIntent().hasExtra("attacher")){
//                attacher = (ActivityAttacher) getIntent().getSerializableExtra("attacher");
//            }else{
//                throw new IllegalStateException("没有传入Attacher参数");
//            }
//
//            if(getIntent().hasExtra("bundleId")){
//                int bundleId = getIntent().getIntExtra("bundleId", 0);
//                if(bundleId > 0){
//                    bundle = BundleManager.getDefault().getBundle(bundleId);
//                    if(bundle == null){
//                        throw new IllegalStateException("bundle为null， 不对！");
//                    }
//                    BundleManager.getDefault().removeBundle(bundleId);
//                }else{
//                    throw new IllegalStateException("没有传入bundleId参数！");
//                }
//            }
//
//        }else{
//            throw new IllegalStateException("没有传入参数！");
//        }
    }

    public SimpleBundle getSimpleBundle(){
        return bundle;
    }

    @Override
    protected void onDestroy() {
        attacher.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        attacher.onStart();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Boolean b = attacher.onKeyDown(keyCode, event);
        if(b == null){
            return super.onKeyDown(keyCode, event);
        }else{
            return b.booleanValue();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        attacher.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onApplyThemeResource(Resources.Theme theme, int resid, boolean first) {
        //attacher.onApplyThemeResource(theme, resid, first);
        super.onApplyThemeResource(theme, resid, first);
    }

    @Override
    protected void onChildTitleChanged(Activity childActivity, CharSequence title) {
        attacher.onChildTitleChanged(childActivity, title);
        super.onChildTitleChanged(childActivity, title);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        attacher.onNewIntent(intent);
    }

    @Override
    protected void onPause() {
        attacher.onPause();
        super.onPause();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        attacher.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        attacher.onPostResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        attacher.onRestart();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        attacher.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        attacher.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        attacher.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        attacher.onStop();
        super.onStop();
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        attacher.onTitleChanged(title, color);
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        attacher.onUserLeaveHint();
    }

//    @Nullable
//    @Override
//    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
//        ActionMode m = attacher.onWindowStartingActionMode(callback);
//        if(m == null){
//            return super.onWindowStartingActionMode(callback);
//        }else{
//            return m;
//        }
//    }

//    @Override
//    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int type) {
//        ActionMode m = attacher.onWindowStartingActionMode(callback);
//        if(m == null){
//            return super.onWindowStartingActionMode(callback);
//        }else{
//            return m;
//        }
//    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Boolean b = attacher.onContextItemSelected(item);
        if(b == null){
            return super.onContextItemSelected(item);
        }else{
            return b.booleanValue();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Boolean b = attacher.onCreateOptionsMenu(menu);
        if(b == null){
            return super.onCreateOptionsMenu(menu);
        }else{
            return b.booleanValue();
        }
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        Boolean b = attacher.onCreatePanelMenu(featureId, menu);
        if(b == null){
            return super.onCreatePanelMenu(featureId, menu);
        }else{
            return b.booleanValue();
        }
    }

    @Override
    public boolean onCreateThumbnail(Bitmap outBitmap, Canvas canvas) {
        Boolean b = attacher.onCreateThumbnail(outBitmap, canvas);
        if(b == null){
            return super.onCreateThumbnail(outBitmap, canvas);
        }else{
            return b.booleanValue();
        }
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        Boolean b = attacher.onGenericMotionEvent(event);
        if(b == null){
            return super.onGenericMotionEvent(event);
        } else{
            return b.booleanValue();
        }
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        Boolean b = attacher.onKeyLongPress(keyCode, event);
        if(b == null){
            return super.onKeyLongPress(keyCode, event);
        } else{
            return b.booleanValue();
        }
    }

    @Override
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        Boolean b = attacher.onKeyMultiple(keyCode, repeatCount, event);
        if(b == null){
            return super.onKeyMultiple(keyCode, repeatCount, event);
        } else{
            return b.booleanValue();
        }
    }

    @Override
    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        Boolean b = attacher.onKeyShortcut(keyCode, event);
        if(b == null){
            return super.onKeyShortcut(keyCode, event);
        } else{
            return b.booleanValue();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Boolean b = attacher.onKeyUp(keyCode, event);
        if(b == null){
            return super.onKeyUp(keyCode, event);
        } else{
            return b.booleanValue();
        }
    }

//    @Override
//    public boolean onMenuItemSelected(int featureId, MenuItem item) {
//        Boolean b = attacher.onMenuItemSelected(featureId, item);
//        if(b == null){
//            return super.onMenuItemSelected(featureId, item);
//        } else{
//            return b.booleanValue();
//        }
//    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        Boolean b = attacher.onMenuOpened(featureId, menu);
        if(b == null){
            return super.onMenuOpened(featureId, menu);
        } else{
            return b.booleanValue();
        }
    }

    @Override
    public boolean onNavigateUp() {
        Boolean b = attacher.onNavigateUp();
        if(b == null){
            return super.onNavigateUp();
        } else{
            return b.booleanValue();
        }
    }

    @Override
    public boolean onNavigateUpFromChild(Activity child) {
        Boolean b = attacher.onNavigateUpFromChild(child);
        if(b == null){
            return super.onNavigateUpFromChild(child);
        } else{
            return b.booleanValue();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Boolean b = attacher.onOptionsItemSelected(item);
        if(b == null){
            return super.onOptionsItemSelected(item);
        } else{
            return b.booleanValue();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Boolean b = attacher.onPrepareOptionsMenu(menu);
        if(b == null){
            return super.onPrepareOptionsMenu(menu);
        } else{
            return b.booleanValue();
        }
    }

    @Override
    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        Boolean b = attacher.onPreparePanel(featureId, view, menu);
        if(b == null){
            return super.onPreparePanel(featureId, view, menu);
        } else{
            return b.booleanValue();
        }
    }

    @Override
    public boolean onSearchRequested() {
        Boolean b = attacher.onSearchRequested();
        if(b == null){
            return super.onSearchRequested();
        } else{
            return b.booleanValue();
        }
    }

//    @Override
//    public boolean onSearchRequested(SearchEvent searchEvent) {
//        Boolean b = attacher.onSearchRequested(searchEvent);
//        if(b == null){
//            return super.onSearchRequested(searchEvent);
//        } else{
//            return b.booleanValue();
//        }
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Boolean b = attacher.onTouchEvent(event);
        if(b == null){
            return super.onTouchEvent(event);
        } else{
            return b.booleanValue();
        }
    }

    @Override
    public boolean onTrackballEvent(MotionEvent event) {
        Boolean b = attacher.onTrackballEvent(event);
        if(b == null){
            return super.onTrackballEvent(event);
        } else{
            return b.booleanValue();
        }
    }

    @Nullable
    @Override
    public CharSequence onCreateDescription() {
        CharSequence b = attacher.onCreateDescription();
        if(b == null){
            return super.onCreateDescription();
        } else{
            return b;
        }
    }

//    @Override
//    public Uri onProvideReferrer() {
//        Uri b = attacher.onProvideReferrer();
//        if(b == null){
//            return super.onProvideReferrer();
//        } else{
//            return b;
//        }
//    }

    @Nullable
    @Override
    public View onCreatePanelView(int featureId) {
        View b = attacher.onCreatePanelView(featureId);
        if(b == null){
            return super.onCreatePanelView(featureId);
        } else{
            return b;
        }
    }

    @Nullable
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View b = attacher.onCreateView(name, context, attrs);
        if(b == null){
            return super.onCreateView(name, context, attrs);
        } else{
            return b;
        }
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        View b = attacher.onCreateView(parent, name, context, attrs);
        if(b == null){
            return super.onCreateView(parent, name, context, attrs);
        } else{
            return b;
        }
    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        super.onActionModeFinished(mode);
        attacher.onActionModeFinished(mode);
    }

    @Override
    public void onActionModeStarted(ActionMode mode) {
        super.onActionModeStarted(mode);
        attacher.onActionModeStarted(mode);
    }

//    @Override
//    public void onActivityReenter(int resultCode, Intent data) {
//        super.onActivityReenter(resultCode, data);
//        attacher.onActivityReenter(resultCode, data);
//    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        attacher.onAttachedToWindow();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        attacher.onAttachFragment(fragment);
    }

    @Override
    public void onBackPressed() {
        boolean b = attacher.onBackPressed();
        if(!b){
            super.onBackPressed();
        }else{

        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        attacher.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onContentChanged() {
        attacher.onContentChanged();
        super.onContentChanged();
    }

    @Override
    public void onContextMenuClosed(Menu menu) {
        attacher.onContextMenuClosed(menu);
        super.onContextMenuClosed(menu);
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        attacher.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public void onCreateNavigateUpTaskStack(TaskStackBuilder builder) {
        super.onCreateNavigateUpTaskStack(builder);
        attacher.onCreateNavigateUpTaskStack(builder);
    }

    @Override
    public void onDetachedFromWindow() {
        attacher.onDetachedFromWindow();
        super.onDetachedFromWindow();
    }

//    @Override
//    public void onEnterAnimationComplete() {
//        attacher.onEnterAnimationComplete();
//        super.onEnterAnimationComplete();
//    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        attacher.onLowMemory();
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        attacher.onOptionsMenuClosed(menu);
        super.onOptionsMenuClosed(menu);
    }

    @Override
    public void onPanelClosed(int featureId, Menu menu) {
        attacher.onPanelClosed(featureId, menu);
        super.onPanelClosed(featureId, menu);
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
//        super.onCreate(savedInstanceState, persistentState);
//        attacher.onCreate(savedInstanceState, persistentState);
//    }
//
//    @Override
//    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
//        super.onPostCreate(savedInstanceState, persistentState);
//        attacher.onPostCreate(savedInstanceState, persistentState);
//    }

    @Override
    public void onPrepareNavigateUpTaskStack(TaskStackBuilder builder) {
        super.onPrepareNavigateUpTaskStack(builder);
        attacher.onPrepareNavigateUpTaskStack(builder);
    }

//    @Override
//    public void onProvideAssistContent(AssistContent outContent) {
//        super.onProvideAssistContent(outContent);
//        attacher.onProvideAssistContent(outContent);
//    }

    @Override
    public void onProvideAssistData(Bundle data) {
        super.onProvideAssistData(data);
        attacher.onProvideAssistData(data);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        attacher.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
//        attacher.onRestoreInstanceState(savedInstanceState, persistentState);
//        super.onRestoreInstanceState(savedInstanceState, persistentState);
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);
//        attacher.onSaveInstanceState(outState, outPersistentState);
//    }

//    @Override
//    public void onStateNotSaved() {
//        super.onStateNotSaved();
//        attacher.onStateNotSaved();
//    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        attacher.onTrimMemory(level);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        attacher.onUserInteraction();
    }

//    @Override
//    public void onVisibleBehindCanceled() {
//        super.onVisibleBehindCanceled();
//        attacher.onVisibleBehindCanceled();
//    }

    @Override
    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        super.onWindowAttributesChanged(params);
        //attacher.onWindowAttributesChanged(params);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        attacher.onWindowFocusChanged(hasFocus);
    }



}

