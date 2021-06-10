package com.example.sockserver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void startTCPClickHandler(View view)
    {
        Toast.makeText(getApplicationContext(), "Start TCP button clicked...", Toast.LENGTH_LONG).show();
        startService(new Intent(MainActivity.this,BgService.class));
    }

    public void exitTheUIApp(View view)
    {
       Toast.makeText(getApplicationContext(), "Exit Button Pressed", Toast.LENGTH_LONG).show();
    }

}