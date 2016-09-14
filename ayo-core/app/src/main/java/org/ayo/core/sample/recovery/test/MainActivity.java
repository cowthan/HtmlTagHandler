package org.ayo.core.sample.recovery.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.ayo.core.sample.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_main);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.btn) {
            Activity activity = null;
            activity.finish();
        } else if (view.getId() == R.id.btn2) {
            Intent intent = new Intent(MainActivity.this, TestActivity.class);
            startActivity(intent);
        }
    }

}
