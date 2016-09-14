package org.ayo.core.app;

import android.app.Activity;

import org.ayo.core.attacher.TmplBaseActivity;

import java.util.Stack;

/**
 */
public class AyoActivityManager {

    private static Stack<Activity> activityStack;
    private static AyoActivityManager instance;
    private AyoActivityManager(){
    }
    public static AyoActivityManager getDefault(){
        if(instance==null){
            instance=new AyoActivityManager();
        }
        return instance;
    }
    public void popActivity(){
        Activity activity=activityStack.lastElement();
        if(activity!=null){
            activity.finish();
            activity=null;
        }
    }
    public void popActivity(Activity activity){
        if(activity!=null){
            activity.finish();
            activityStack.remove(activity);
            activity=null;
        }
    }
    public Activity currentActivity(){
        Activity activity=activityStack.lastElement();
        return activity;
    }
    public void pushActivity(Activity activity){
        if(activityStack==null){
            activityStack=new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    public void popAllActivityExceptOne(Class cls){
        while(true){
            Activity activity=currentActivity();
            if(activity==null){
                break;
            }
            if(activity.getClass().equals(cls) ){
                break;
            }
            popActivity(activity);
        }
    }


    public static String getUIClass(Activity activity){
        if(activity instanceof TmplBaseActivity){
            return ((TmplBaseActivity)activity).getActivityAttacher().getClass().getSimpleName();
        }else{
            return activity.getClass().getSimpleName();
        }
    }


}
