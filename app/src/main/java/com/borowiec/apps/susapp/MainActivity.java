package com.borowiec.apps.susapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
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
        buttons.put("albumsNew", (LinearLayout) findViewById(R.id.albumsNew));
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 100);

        for (Map.Entry<String, LinearLayout> entry : buttons.entrySet()) {
            if (entry.getKey().equals("camera")) {
                entry.getValue().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                        startActivity(intent);
                    }
                });
            }
            else if (entry.getKey().equals("albums")) {
                entry.getValue().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, AlbumsActivity.class);
                        startActivity(intent);
                    }
                });
            }
            else if (entry.getKey().equals("albumsNew")) {
                entry.getValue().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, AlbumsNewActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    public void checkPermission(String permission, int requestCode) {
        // jeśli nie jest przyznane to zażądaj
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
        } else {
            Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
            createFolders();
        }
    }

    public void createFolders() {
        File pic = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES );
        File dir = new File(pic, "MaciejBorowiec");
        dir.mkdir();
        String[] albums = {"miejsca", "ludzie", "rzeczy"};
        for (String album : albums) {
            File al = new File(dir, album);
            al.mkdir();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createFolders();
                } else {
                    //nie
                }
                break;
            case 101 :

                break;
        }

    }
}