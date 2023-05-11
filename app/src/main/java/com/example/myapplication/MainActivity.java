package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private boolean color = false;
    private View view;
    private long lastUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = findViewById(R.id.arkaplan);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();
    }

    private void getAccelerometer(SensorEvent event){
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelationSquareRoot = (x*x+y*y+z*z) / (sensorManager.GRAVITY_EARTH * sensorManager.GRAVITY_EARTH);

        long actualTime = event.timestamp;

        if (accelationSquareRoot >= 2) {
            if (actualTime - lastUpdate < 200) {
                return;
            }

            lastUpdate = actualTime;
            Toast.makeText(this, "Arkaplan rengi degistirildi.", Toast.LENGTH_LONG).show();
            if (color) {
                view.setBackgroundColor(Color.rgb(55 , 0 , 179));
            } else {
                view.setBackgroundColor(Color.RED);
            }
            color = !color;
        }

    }


    public void onAccuracyChanged (Sensor sensor,int accuracy){

    }

    public void onSensorChanged (SensorEvent event){
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            getAccelerometer(event);
        }
    }

    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    protected void onStop(){
        super.onStop();
        sensorManager.unregisterListener(this);
    }
}