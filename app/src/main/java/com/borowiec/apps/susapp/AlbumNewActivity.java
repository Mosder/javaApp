package com.borowiec.apps.susapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class AlbumNewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_new);

        Bundle bundle = getIntent().getExtras();
        String albumName = bundle.getString("albumName");

        ListView albumList = findViewById(R.id.photosList);
        albumList.setAdapter(listPhotos(albumName));
    }

    public ArrayAdapter<File> listPhotos(String albumName) {
        ArrayList<File> array = getPhotos(albumName);
        PhotoListAdapter adapter = new PhotoListAdapter (
                AlbumNewActivity.this,
                R.layout.photos_list_row,
                getPhotos(albumName),
                albumName);
        return adapter;
    }

    public ArrayList<File> getPhotos(String albumName) {
        File album = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES + "/MaciejBorowiec/" + albumName );
        File[] photos = album.listFiles();
        ArrayList<File> names = new ArrayList<>();
        for (File photo : photos) {
            names.add(photo);
        }
        return names;
    }
}