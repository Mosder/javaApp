package com.borowiec.apps.susapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        buttons.put("notes", (LinearLayout) findViewById(R.id.notes));
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 100);

        for (Map.Entry<String, LinearLayout> entry : buttons.entrySet()) {
            entry.getValue().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, Class> activities = new HashMap<>();
                    activities.put("albums", AlbumsActivity.class);
                    activities.put("albumsNew", AlbumsNewActivity.class);
                    activities.put("notes", NotesActivity.class);

                    String key = entry.getKey();
                    if (key.equals("camera"))
                        checkPermission(Manifest.permission.CAMERA, 101);
                    else
                        startActivity(new Intent(MainActivity.this, activities.get(key)));
                }
            });
        }
    }

    public void checkPermission(String permission, int requestCode) {
        // jeśli nie jest przyznane to zażądaj
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
        } else {
            if (requestCode == 100) {
                Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
                createFolders();
            }
            else if (requestCode == 101)
                openCamera();
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
        if (grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == 100)
                createFolders();
            else if (requestCode == 101)
                openCamera();
        }
    }

    public void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 200); // 200 - stała wartość, która później posłuży do identyfikacji tej akcji
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap bitmap = (Bitmap) extras.get("data");

                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Save where?");
                String[] buttons = {"ludzie", "miejsca", "rzeczy"};
                alert.setItems(buttons, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int album) {
                        savePhoto(bitmap, String.valueOf(buttons[album]));
                    }
                });
                alert.show();
            }
        }
    }

    public void savePhoto(Bitmap bitmap, String album) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream); // kompresja, typ pliku jpg, png
        byte[] byteArray = stream.toByteArray();

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String name = df.format(new Date()) + ".jpg";

        FileOutputStream fs = null;
        try {
            File file = new File(String.format("%s/MaciejBorowiec/%s",
                    Environment.DIRECTORY_PICTURES, album), name);
            fs = new FileOutputStream(file);
            fs.write(byteArray);
            fs.close();
        } catch (IOException e) {
            Log.d("file", "Exception: " + e);
        }
    }
}