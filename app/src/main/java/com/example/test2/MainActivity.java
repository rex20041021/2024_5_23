package com.example.test2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.pm.ShortcutInfoCompat;

import android.app.Activity;
import android.app.GameManager;
import android.app.Service;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

// 最外面的那層acitivity(應該也不會動到)
public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private Game game;

    private View decorView;

    DisplayMetrics displayMetrics = new DisplayMetrics();
    private SensorManager mSensorManager;
    private Sensor accSensor;


    // 感應陀螺儀、加速規、磁力計
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.equals(accSensor)) {
            game.setSensorDx(event.values[0]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    // onCreate階段
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 鎖定自動旋轉
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        game = new Game(this);
        setContentView(game);

        // 隱藏標題(title)跟狀態欄(navigation bar)
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0) decorView.setSystemUiVisibility(hideSystemBars());
            }
        });

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    // 隱藏標題(title)跟狀態欄(navigation bar)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    }

    // 隱藏標題(title)跟狀態欄(navigation bar)
    private int hideSystemBars() {
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }

    @Override
    public void onResume() {
        super.onResume();
        game.resumeBGM();
        mSensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }


    // onPause階段
    @Override
    protected void onPause() {
        game.pause();
        game.pauseBGM();
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

}