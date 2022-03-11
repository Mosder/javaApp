package com.borowiec.apps.susapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private final Map<String, LinearLayout> buttons = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttons.put("camera", (LinearLayout) findViewById(R.id.camera));
        buttons.put("albums", (LinearLayout) findViewById(R.id.albums));
        buttons.put("collage", (LinearLayout) findViewById(R.id.collage));
        buttons.put("network", (LinearLayout) findViewById(R.id.network));

        for (Map.Entry<String, LinearLayout> entry : buttons.entrySet()) {
            entry.getValue().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}