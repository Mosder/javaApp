package com.borowiec.apps.susapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PhotoListAdapter extends ArrayAdapter {
    private ArrayList<String> _list;
    private Context _context;
    private int _resource;
    private String _albumName;
    public PhotoListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects, String album) {
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
        String imagePath = Environment.DIRECTORY_PICTURES + String.format("/MaciejBorowiec/%s/%s", this._albumName, this._list.get(position));
        Bitmap bmp = betterImageDecode(imagePath); // własna funkcja betterImageDecode opisana jest poniżej
        photo.setImageBitmap(bmp); // wstawienie bitmapy do ImageView
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
