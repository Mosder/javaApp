package com.borowiec.apps.susapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class NotesArrayAdapter extends ArrayAdapter {
    private DatabaseManager _db;
    private ArrayList<Note> _list;
    private Context _context;
    private int _resource;
    public NotesArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Note> objects, DatabaseManager db) {
        super(context, resource, objects);
        this._list = objects;
        this._context = context;
        this._resource = resource;
        this._db = db;
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

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(_context);
                alert.setTitle("Edit note");
                String[] buttons = {"edit", "delete", "title sort", "color sort"};
                alert.setItems(buttons, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int btn) {
                        if (btn == 0) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(_context);
                            alert.setTitle("Edit note");
                            alert.setMessage("Change title, content and color");
                            View editView = View.inflate(_context, R.layout.note_dialog, null);
                            int[] colors = Utils.colors();
                            EditText title = editView.findViewById(R.id.title);
                            title.setText(note.getTitle());
                            EditText content = editView.findViewById(R.id.content);
                            content.setText(note.getContent());
                            final int[] currentColor = {note.getColor()};
                            title.setTextColor(currentColor[0]);
                            LinearLayout colorsLayout = editView.findViewById(R.id.colors);
                            for (int color : colors) {
                                View colorView = new View(_context);
                                colorView.setBackgroundColor(color);
                                int size = (int)editView.getResources().getDimension(R.dimen.colorsSize);
                                colorView.setLayoutParams(new LinearLayout.LayoutParams(size, size));
                                colorView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        currentColor[0] = color;
                                        title.setTextColor(currentColor[0]);
                                    }
                                });
                                colorsLayout.addView(colorView);
                            }
                            alert.setView(editView);
                            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    _db.editNote(note.getId(), String.valueOf(title.getText()),
                                            String.valueOf(content.getText()), currentColor[0]);
                                    _list = _db.getAll();
                                    notifyDataSetChanged();
                                }
                            });
                            alert.setNegativeButton("CANCEL", null);
                            alert.show();
                        }
                        else if (btn == 1) {
                            _db.delNote(note.getId());
                            _list.remove(position);
                            notifyDataSetChanged();
                        }
                        else if (btn == 2) {
                            Collections.sort(_list, new Comparator<Note>() {
                                @Override
                                public int compare(Note a, Note b) {
                                    return a.getTitle().compareTo(b.getTitle());
                                }
                            });
                            notifyDataSetChanged();
                        }
                        else if (btn == 3) {
                            Collections.sort(_list, new Comparator<Note>() {
                                @Override
                                public int compare(Note a, Note b) {
                                    return a.getColor() - b.getColor();
                                }
                            });
                            notifyDataSetChanged();
                        }
                    }
                });
                alert.show();
                return false;
            }
        });

        return convertView;
    }
}
