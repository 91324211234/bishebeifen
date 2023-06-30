package com.example.test_app1;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

public class SettingActivity extends AppCompatActivity {
    Switch aSwitch;
    int i = 1;
    int currentNightMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_setting);
        aSwitch = findViewById(R.id.switch2);

        currentNightMode = getBaseContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                aSwitch.setChecked(false);
//                i = 1;
                // Night mode is not active, we're using the light theme
                break;
            case Configuration.UI_MODE_NIGHT_YES:

                aSwitch.setChecked(true);
//                i = 1;
                // Night mode is active, we're using dark theme
                break;
        }


    }

    public void dark(View view) {


        int currentNightMode = getBaseContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if(Configuration.UI_MODE_NIGHT_NO == currentNightMode){
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);
            Log.e("MODE", String.valueOf(this.getApplicationContext().getResources().getConfiguration().uiMode));
//            restartApplication();
        }

        else{
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO);
            Log.e("MODE", String.valueOf(this.getApplicationContext().getResources().getConfiguration().uiMode));
//            restartApplication();
        }
    }

    private void restartApplication() {
        final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    @Override
    public void recreate() {
        super.recreate();
        currentNightMode = getBaseContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                aSwitch.setChecked(true);
//                i = 1;
                // Night mode is not active, we're using the light theme
                break;
            case Configuration.UI_MODE_NIGHT_YES:

                aSwitch.setChecked(false);
//                i = 1;
                // Night mode is active, we're using dark theme
                break;
        }
    }
}