package com.example.capstone18.capstone;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.capstone18.capstone.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class CustomGrid extends BaseAdapter{
    private Context mContext;
    private final List<String> web;
    private final List<String> Imageid;
    private ImageView imageView;
    public CustomGrid(Context c, List<String> web, List<String> Imageid ) {
        mContext = c;
        this.Imageid = Imageid;
        this.web = web;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return web.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.ingredient_display, null);
            TextView textView = (TextView) grid.findViewById(R.id.grid_text);
            imageView = (ImageView)grid.findViewById(R.id.grid_image);
            textView.setText(web.get(position));
            new ImageLoader(imageView).execute(Imageid.get(position));
        } else {
            grid = (View) convertView;
        }
        return grid;
    }
    private class ImageLoader extends AsyncTask<String,Void,Bitmap> {
        ImageView imageView;

        public ImageLoader(ImageView i){
            imageView=i;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String url=urls[0];
            Bitmap image=null;
            try {
                InputStream stream=new java.net.URL(url).openStream();
                image= BitmapFactory.decodeStream(stream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return image;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}