package com.borowiec.apps.susapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class NotesArrayAdapter extends ArrayAdapter {
    private ArrayList<Note> _list;
    private Context _context;
    private int _resource;
    private String _albumName;
    public NotesArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Note> objects) {
        super(context, resource, objects);
        this._list = objects;
        this._context = context;
        this._resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // inflater - klasa konwertująca xml na kod javy
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.note_list_row, null);
        // convertView = inflater.inflate(_resource, null);
        // szukamy każdego TextView w layoucie

        Note note = this._list.get(position);
        TextView index = (TextView) convertView.findViewById(R.id.noteIndex);
        index.setText(String.valueOf(note.getId()));
        TextView title = (TextView) convertView.findViewById(R.id.noteTitle);
        title.setText(note.getTitle());
        title.setTextColor(note.getColor());
        TextView content = (TextView) convertView.findViewById(R.id.noteContent);
        content.setText(note.getContent());
        TextView filePath = (TextView) convertView.findViewById(R.id.filePath);
        filePath.setText(note.getFilePath());

        return convertView;
    }
}
