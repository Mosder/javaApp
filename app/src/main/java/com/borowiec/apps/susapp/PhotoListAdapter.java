package com.borowiec.apps.susapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotoListAdapter extends ArrayAdapter {
    private ArrayList<File> _list;
    private Context _context;
    private int _resource;
    private String _albumName;
    public PhotoListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<File> objects, String album) {
        super(context, resource, objects);
        this._list = objects;
        this._context = context;
        this._resource = resource;
        this._albumName = album;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(_resource, null);

        ImageView photo = (ImageView) convertView.findViewById(R.id.photo);
        String imagePath = this._list.get(position).getPath();
        Bitmap bmp = betterImageDecode(imagePath); // własna funkcja betterImageDecode opisana jest poniżej
        photo.setImageBitmap(bmp); // wstawienie bitmapy do ImageView

        ImageView del = (ImageView) convertView.findViewById(R.id.bDel);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(_context);
                alert.setTitle("Confirmation");
                alert.setMessage("Delete this photo?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        _list.get(position).delete();
                        _list.remove(position);
                        notifyDataSetChanged();
                    }

                });
                alert.setNegativeButton("NO", null);
                alert.show();
            }
        });

        ImageView info = (ImageView) convertView.findViewById(R.id.bInfo);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(_context);
                alert.setTitle("Information");
                alert.setMessage("Info:\n" + _list.get(position).getPath());
                alert.setNeutralButton("OK", null);
                alert.show();
            }
        });

        ImageView edit = (ImageView) convertView.findViewById(R.id.bEdit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(_context);
                alert.setTitle("Add note");
                alert.setMessage("Set title, content and color");
                View editView = View.inflate(_context, R.layout.note_dialog, null);
                int[] colors = Utils.colors();
                EditText title = editView.findViewById(R.id.title);
                EditText content = editView.findViewById(R.id.content);
                final int[] currentColor = {colors[0]};
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
                        DatabaseManager db = new DatabaseManager (
                            _context, // activity z galerią zdjęć
                            "NotesBorowiecMaciej.db", // nazwa bazy
                            null,
                            1 //wersja bazy, po zmianie schematu bazy należy ją zwiększyć
                        );
                        db.insert(String.valueOf(title.getText()), String.valueOf(content.getText()), currentColor[0]);
                    }
                });
                alert.setNegativeButton("CANCEL", null);
                alert.show();
            }
        });

        return convertView;
    }

    private Bitmap betterImageDecode(String filePath) {
        Bitmap myBitmap;
        BitmapFactory.Options options = new BitmapFactory.Options(); //opcje przekształcania bitmapy
        options.inSampleSize = 4; // zmniejszenie jakości bitmapy 4x
        myBitmap = BitmapFactory.decodeFile(filePath, options);
        return myBitmap;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }
}
