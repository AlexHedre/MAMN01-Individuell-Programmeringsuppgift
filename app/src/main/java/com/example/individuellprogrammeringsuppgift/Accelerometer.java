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
        acceleration.setText("Accelerometer värden:" +
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

    /**
     * Displays the inclination of the phone and changes the color of the screen to cyan if it isn't leveled
     */
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
        if (direction.equals("Jämn")) {
            background.setBackgroundColor(Color.WHITE);
        } else {
            background.setBackgroundColor(Color.CYAN);
        }
        inclination.setText(direction);
    }

    /**
     * If the total acceleration on the phone minus gravity is higher than a threshold the phone will vibrate
     */
    public void shakeCheck(SensorEvent event) {
        final float vibration_threshold = 10.0f;
        float totalAcceleration = Math.abs(event.values[0]) + Math.abs(event.values[1]) + Math.abs(event.values[2]);
        float gravityAcceleration = Math.abs(gravity[0]) + Math.abs(gravity[1]) + Math.abs(gravity[2]);
        float acceleration = totalAcceleration - gravityAcceleration;

        if (acceleration > vibration_threshold) {
            vibrator.vibrate(100);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }
}