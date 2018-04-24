package com.example.capstone18.capstone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Recipe_Display extends AppCompatActivity {
    public static final String IMAGE_URL="url";
    public static final String RECIPE_NAME="name";
    public static final String INSTRUCTIONS="instructions";
    public static final String RECIPE_ID="recipeId";

    public String apiKey = "q0hVswUOhPmshMS5UZnQXk135TMap1SZItBjsnH12TyNDbPxzx"; //P
    //private String apiKey = "K3hkrfTbpzmshEjPqJ39L31yWXRvp1d3ZvujsnWgbJAHZITIep"; // S
    private String apiHost = "spoonacular-recipe-food-nutrition-v1.p.mashape.com";

    public String urlBase = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/";
    public String url = "";
    public String Jsonoutput;
    //"https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/479101/information"

    private TextView recipe_name,recipe_instructions;
    private ImageView recipe_image;
    private GridView grid;
    Context recipe_context;

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
        recipe_context = getApplicationContext();
        grid=(GridView)findViewById(R.id.grid);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            Toast.makeText(this, "Failed to get recipe info from Recipes list", Toast.LENGTH_LONG).show();
            finishActivity(RESULT_CANCELED);
        }
        new ImageLoader(recipe_image).execute(bundle.getString("IMAGE_URL"));
        recipe_name.setText(bundle.getString("RECIPE_NAME"));
        recipe_instructions.setText(bundle.getString("INSTRUCTIONS"));
        url = urlBase + bundle.getString("RECIPE_ID") + "/information";

        // Send Http request
        Recipe_Display.OkHttpHandler okHttpHandler = new Recipe_Display.OkHttpHandler();
        okHttpHandler.execute(url);
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


    public class OkHttpHandler extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... params) {

            Request.Builder builder = new Request.Builder();
            builder.url(params[0]);
            builder.header("X-Mashape-Key", apiKey)
                    .header("X-Mashape-Host", apiHost);
            Request request = builder.build();
            //txtString.setText("attempt");
            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
                //txtString.setText("fail API connection");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Jsonoutput = s;
            List<String> recipeIngredients = new ArrayList<String>();
            List<String> recipeAmount = new ArrayList<String>();
            List<String> recipeImage = new ArrayList<String>();


            // Parse JSON return
            try {
                String test = "";
                JSONObject jsonroot = new JSONObject(s);
                JSONArray reader = jsonroot.getJSONArray("extendedIngredients");
                for (int i = 0; i < reader.length(); i++) {
                    JSONObject c = reader.getJSONObject(i);
                    recipeIngredients.add(c.getString("name") + "\n" + c.getString("amount") + " " + c.getString("unit"));
                    recipeAmount.add(c.getString("amount") + " " + c.getString("unit"));
                    recipeImage.add(c.getString("image"));
                }
                CustomGrid adapter = new CustomGrid(recipe_context, recipeIngredients, recipeImage);
                grid.setAdapter(adapter);
            } catch (JSONException e) {
                //txtString.setText("fail Json parse");
            }

            //txtString.setVisibility(View.GONE);
            //recipe_view.setVisibility(View.VISIBLE);
        }
    }

    public void okPressed(View v) {
        setResult(RESULT_OK);
        finish();

    }

    public void cancelPressed(View v){setResult(RESULT_CANCELED); finish();}
}
