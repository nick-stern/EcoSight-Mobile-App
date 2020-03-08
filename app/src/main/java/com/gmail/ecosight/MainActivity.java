package com.gmail.ecosight;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Created a Button class for the History button and set an on click listener to have the button interacted with when clicked
        Button HistBtn = (Button) findViewById(R.id.HistBtn);
        HistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //New intent for the history class and starting the history activity
                Intent historyIntent = new Intent(getApplicationContext(), History.class);
                startActivity(historyIntent);
            }
        });

        //Created a Button class for the Drone button and set an on click listener to have the button interacted with when clicked
        Button DroneBtn = (Button) findViewById(R.id.DroneBtn);
        DroneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //New intent for the drone class and starting the history activity
                Intent droneIntent = new Intent(getApplicationContext(), Drone.class);
                startActivity(droneIntent);
            }
        });

        //Created a Button class for the Camera button and set an on click listener to have the button interacted with when clicked
        Button CameraBtn = (Button) findViewById(R.id.CameraBtn);
        CameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //New intent for the camera class and starting the history activity
                Intent cameraIntent = new Intent(getApplicationContext(), Camera.class);
                startActivity(cameraIntent);
            }
        });
    }
}
