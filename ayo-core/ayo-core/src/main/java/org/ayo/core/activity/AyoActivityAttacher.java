package org.ayo.core.activity;

import android.os.Bundle;

import org.ayo.core.attacher.ActivityAttacher;


public abstract class AyoActivityAttacher extends ActivityAttacher {
	
	protected ActivityDelegate agent = new ActivityDelegate();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		agent.attach(getActivity());
	}

	@Override
	protected void onDestroy() {
		agent.detach();
		super.onDestroy();
	}

	protected AyoActivityAttacher getActivityAttacher(){
		return this;
	}

	public <T> T id(int id){
		return (T)findViewById(id);
	}

	public ActivityDelegate getAgent(){
		return agent;
	}
}
