package com.borowiec.apps.susapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class AlbumsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);
        ActionBar actionBar =  getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        ListView albumsList = findViewById(R.id.albumsList);
        albumsList.setAdapter(listAlbums());
        albumsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("TAG","numer klikanego wiersza w ListView = " + i);
            }
        });
        albumsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                File myDir = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES + "/MaciejBorowiec" );
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public ArrayAdapter<String> listAlbums() {
        ArrayList<String> array = getAlbums();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                AlbumsActivity.this, // tzw Context
                R.layout.albums_list_row, // nazwa pliku xml naszego wiersza na liście
                R.id.albumText, // id pola txt w wierszu
                array ); // tablica przechowująca testowe dane
    }

    public ArrayList<String> getAlbums() {
        File myDir = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES + "/MaciejBorowiec" );
        File[] albums = myDir.listFiles();
        ArrayList<String> names = new ArrayList<>();
        for (File album : albums) {
            names.add(album.getName());
        }
        return names;
    }
}