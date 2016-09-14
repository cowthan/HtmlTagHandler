package org.ayo.core.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ayo.core.attacher.ActivityAttacher;
import org.ayo.core.attacher.TmplBaseActivity;


/**
 */
public abstract class AyoFragment extends Fragment{

    protected View root;
    protected Handler mHandler;
    private ActivityAttacher attacher;

    protected abstract int getLayoutId();
    public abstract void onCreateView(View root);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        root = inflater.inflate(getLayoutId(), null);
        mHandler = new Handler();
        onCreateView(root);
        return root;
    }

    @Override
    public void onDestroyView() {
        mHandler = null;
        super.onDestroyView();
    }

    public ActivityAttacher getActivityAttacher(){
        Activity a = getActivity();
        if(a instanceof TmplBaseActivity){
            attacher = ((TmplBaseActivity)a).getActivityAttacher();
        }else{
            throw new RuntimeException("此fragment必须从属于ActivityAttacher");
        }
        return attacher;
    }

    public View findViewById(int id){
        return root.findViewById(id);
    }

    public View findViewWithTag(Object o){
        return root.findViewWithTag(o);
    }

    public <T> T id(int id){
        return (T)findViewById(id);
    }
}
