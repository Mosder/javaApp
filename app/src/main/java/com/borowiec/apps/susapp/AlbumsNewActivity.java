package com.borowiec.apps.susapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class AlbumsNewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums_new);
        ActionBar actionBar =  getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button addAlbum = findViewById(R.id.albumsAdd);
        addAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(AlbumsNewActivity.this);
                alert.setTitle("Add album");
                alert.setMessage("Input album name");
                EditText input = new EditText(AlbumsNewActivity.this);
                alert.setView(input);
                alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        createDir(String.valueOf(input.getText()));
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //wyświetl which
                    }
                });
                alert.show();
            }
        });

        ListView albumsList = findViewById(R.id.albumsList);
        albumsList.setAdapter(listAlbums());
        albumsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(AlbumsNewActivity.this, AlbumNewActivity.class);
                ArrayList<String> albums = getAlbums();
                intent.putExtra("albumName", albums.get(i));
                startActivity(intent);
            }
        });
        albumsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder alert = new AlertDialog.Builder(AlbumsNewActivity.this);
                alert.setTitle("Confirmation");
                alert.setMessage("Remove this album?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<String> albums = getAlbums();
                        File dirToKill = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES + "/MaciejBorowiec/" + albums.get(i));
                        for (File file : dirToKill.listFiles()){
                            file.delete();
                        }
                        dirToKill.delete();
                        albumsList.setAdapter(listAlbums());
                    }

                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alert.show();
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
                AlbumsNewActivity.this, // tzw Context
                R.layout.albums_list_row, // nazwa pliku xml naszego wiersza na liście
                R.id.albumText, // id pola txt w wierszu
                array ); // tablica przechowująca testowe dane
        return adapter;
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

    public void createDir(String albumName) {
        File myDir = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES + "/MaciejBorowiec" );
        File dir = new File(myDir, albumName);
        dir.mkdir();
        ListView albumsList = findViewById(R.id.albumsList);
        albumsList.setAdapter(listAlbums());
    }
}