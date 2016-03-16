package com.gekson.x5demo;

import android.app.Activity;
import android.content.Intent;

import com.tencent.smtt.sdk.QbSdk;

public class PreInitBackgroundActivity extends Activity {

    protected void onCreate(android.os.Bundle savedInstanceState) {

        QbSdk.PreInitCallback initCallback = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished() {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getApplicationContext(), X5FirstTimeActivity.class);
                getApplicationContext().startActivity(intent);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub

            }
        };

        QbSdk.preInit(this, initCallback);
    }

    ;

}
