package com.example.individuellprogrammeringsuppgift;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Called when the user taps the Compass button
     */
    public void goToCompass(View view) {
        Intent intent = new Intent(this, Compass.class);
        startActivity(intent);
    }

    /**
     * Called when the user taps the Accelerometer button
     */
    public void goToAccelerometer(View view) {
        Intent intent = new Intent(this, Accelerometer.class);
        startActivity(intent);
    }
}