package org.ayo.core.init;

/**
 * Created by Administrator on 2016/4/30.
 */
public class StepOfCrash implements Initializer.Step{
    @Override
    public boolean doSeriousWork() {

        final Thread.UncaughtExceptionHandler d = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                throwable.printStackTrace();
                d.uncaughtException(thread, throwable);
                //Toaster.toastShort(throwable.getLocalizedMessage());
            }
        });
        return true;
    }

    @Override
    public String getName() {
        return "全局异常处理";
    }

    @Override
    public String getNotify() {
        return "全局异常处理设置失败";
    }

    @Override
    public boolean acceptFail() {
        return true;
    }
}
