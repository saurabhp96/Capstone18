package com.example.capstone18.capstone;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

public class Recipe_Display extends AppCompatActivity {
    public static final String IMAGE_URL="url";
    public static final String RECIPE_NAME="name";
    public static final String INSTRUCTIONS="instructions";

    private TextView recipe_name,recipe_instructions;
    private ImageView recipe_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe__display);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        recipe_image = (ImageView) findViewById(R.id.recipeImage);
        recipe_name = (TextView) findViewById(R.id.recipe_name);
        recipe_instructions = (TextView) findViewById(R.id.recipe_instructions);

        Bundle bundle = getIntent().getExtras();

        if (bundle == null) {
            Toast.makeText(this, "Failed to get recipe info from Recipes list", Toast.LENGTH_LONG).show();
            finishActivity(RESULT_CANCELED);
        }

        new ImageLoader(recipe_image).execute(bundle.getString(IMAGE_URL));
        recipe_name.setText(bundle.getString(RECIPE_NAME));
        recipe_instructions.setText(bundle.getString(INSTRUCTIONS));


    }

    private class ImageLoader extends AsyncTask<String,Void,Bitmap>{
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

    public void okPressed(View v) {
        finishActivity(RESULT_OK);

    }

    public void cancelPressed(View v){finishActivity(RESULT_CANCELED);}
}
