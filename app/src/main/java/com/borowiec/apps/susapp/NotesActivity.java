package com.borowiec.apps.susapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ListView;

import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        this.setNotes();
    }

    private void setNotes() {
        DatabaseManager db = new DatabaseManager (
                NotesActivity.this,
                "NotesBorowiecMaciej.db",
                null,
                1
        );
        ArrayList<Note> notes = db.getAll();
        NotesArrayAdapter adapter = new NotesArrayAdapter(
                NotesActivity.this,
                R.layout.note_list_row,
                notes,
                db
        );
        ListView notesList = findViewById(R.id.notesList);
        notesList.setAdapter(adapter);
    }
}