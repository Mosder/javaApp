package com.borowiec.apps.susapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class DatabaseManager extends SQLiteOpenHelper {
    public DatabaseManager(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE notes (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 'title' TEXT, 'content' TEXT, 'color' INT, 'filePath' TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS notes");
        onCreate(db);
    }
    public boolean insert(String title, String content, int color, String path){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("content", content);
        contentValues.put("color", color);
        contentValues.put("filePath", path);
        db.insertOrThrow("notes", null, contentValues); // gdy insert się nie powiedzie, będzie błąd
        db.close();
        return true;
    }

    @SuppressLint("Range")
    public ArrayList<Note> getAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Note> notes = new ArrayList<>();
        Cursor result = db.rawQuery("SELECT * FROM notes" , null);
        while(result.moveToNext()) {
            notes.add(new Note(
                result.getString(result.getColumnIndex("title")),
                result.getString(result.getColumnIndex("content")),
                result.getInt(result.getColumnIndex("color")),
                result.getInt(result.getColumnIndex("_id")),
                result.getString(result.getColumnIndex("filePath"))
            ));
        }
        return notes;
    }

    public int delNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete("notes",
                "_id = ? ",
                new String[]{String.valueOf(id)}); // chodzi o id w tej linii
    }

    public void editNote(int id, String title, String content, int color) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("content", content);
        contentValues.put("color", color);

        db.update("notes",
                contentValues,
                "_id = ? ",
                new String[]{String.valueOf(id)}); // chodzi o id w tej linii
        db.close();
    }
}
