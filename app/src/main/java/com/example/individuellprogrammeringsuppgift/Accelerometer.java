package com.example.individuellprogrammeringsuppgift;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.*;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class Accelerometer extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Vibrator vibrator;

    private TextView acceleration;
    private TextView inclination;
    private ConstraintLayout background;
    private float[] gravity = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        acceleration = (TextView) findViewById(R.id.acceleration);
        inclination = (TextView) findViewById(R.id.inclination);
        background = (ConstraintLayout) findViewById((R.id.background));

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        acceleration.setText("Accelerometer values:" +
            "\nX: " + Math.round(event.values[0]*10)/10.0 +
            "\nY: " + Math.round(event.values[1]*10)/10.0 +
            "\nZ: " + Math.round(event.values[2]*10)/10.0
        );

        final float alpha = 0.8f;
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        displayInclination();
        shakeCheck(event);
    }

    public void displayInclination() {
        String direction = "Jämn";
        if (Math.abs(gravity[0]) > Math.abs(gravity[1])) {
            if (gravity[0] > 3) {
                direction = "Vänster";
            } else if (gravity[0] < -3) {
                direction = "Höger";
            }
        } else {
            if (gravity[1] > 2) {
                direction = "Bakåt";
            } else if (gravity[1] < -2) {
                direction = "Framåt";
            }
        }
        if (direction == "Jämn") {
            background.setBackgroundColor(Color.WHITE);
        } else {
            background.setBackgroundColor(Color.CYAN);
        }
        inclination.setText(direction);
    }

    public void shakeCheck(SensorEvent event) {
        if (8 < (event.values[0] + event.values[1] + event.values[2]) - (gravity[0] + gravity[1] + gravity[2])) {
            vibrator.vibrate(100);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }
}