package org.ayo.core.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.ayo.core.app.AyoActivityManager;

/**
 * Created by Administrator on 2016/4/20.
 */
public class AyoActivity extends AppCompatActivity {

    protected ActivityDelegate agent = new ActivityDelegate();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        agent.attach(this);
        AyoActivityManager.getDefault().pushActivity(this);
    }

    @Override
    protected void onDestroy() {
        agent.detach();
        AyoActivityManager.getDefault().popActivity(this);
        super.onDestroy();
    }

    protected AyoActivity getActivity(){
        return this;
    }

    public <T> T id(int id){
        return (T)findViewById(id);
    }

    public ActivityDelegate getAgent(){
        return agent;
    }
}
