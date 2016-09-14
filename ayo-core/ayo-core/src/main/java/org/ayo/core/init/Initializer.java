package org.ayo.core.init;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 *
 *
 */
public class Initializer {

    /**
     * 一个初始化任务，如极光的初始化是一个step，sd卡的初始化是一个step，http的初始化也是一个step
     */
    public interface Step{

        /** 做一些严肃的工作，也就是正事 */
        boolean doSeriousWork();

        /** 这一步的名字 */
        String getName();

        /** 初始化失败，给用户的提示 */
        String getNotify();

        /** 如果这一步初始化失败，是否可以继续，对于一些不影响系统使用的功能，可以接受失败 */
        boolean acceptFail();

    }

    public interface StepListner{
        /**
         * suffering是说的谁？说的用户，你在初始化时，遭罪的是用户
         * @param step  当前的任务
         * @param isSuccess  当前的任务是否成功了
         * @param currentStep 当前是第几个任务，从1开始
         * @param total  一共几个任务
         * @return true表示可以继续初始化，false表示某一步出错了，并且错误不可接受，app已经没法用了，没必要继续了
         */
        boolean onSuffering(Step step, boolean isSuccess, int currentStep, int total);
    }

    private List<Step> steps = new ArrayList<Step>();
    private StepListner stepListner;

    private Initializer(){

    }

    public static Initializer initailizer(){
        return new Initializer();
    }

    public Initializer addStep(Step step){
        steps.add(step);
        return this;
    }

    public Initializer setStepListener(StepListner stepListener){
        this.stepListner = stepListener;
        return this;
    }

    /**
     * 开始初始化
     */
    public void suffer(){
        int total = steps.size();
        if(total == 0){
            if(this.stepListner != null) this.stepListner.onSuffering(null, true, 0, 0);
            return;
        }

        int current = 0;
        for(Step step: steps){
            current += 1;
            Log.i("initialize", "初始化--" + step.getName());
            boolean isSuccess = step.doSeriousWork();
            Log.i("initialize", "初始化--" + isSuccess);
            if(this.stepListner != null) {
                if(!this.stepListner.onSuffering(step, isSuccess, current, total)){
                    break;
                }
            }
        }
    }

}
