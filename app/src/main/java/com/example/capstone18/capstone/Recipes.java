package com.example.capstone18.capstone;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Recipes extends AppCompatActivity {
    public static final String SELECTED_CUISINES = "selected";
    public static final String RESTRICTION = "restriction";
    public static final String EXCLUDED_INGREDIENTS = "excluded";
    public static final String MEAL = "meal";

    final Context context=this;
    TextView txtString;
    ListView recipe_view;
    Context recipe_context;
    Intent intent;

    public String apiKey = "q0hVswUOhPmshMS5UZnQXk135TMap1SZItBjsnH12TyNDbPxzx"; //P
    //private String apiKey = "K3hkrfTbpzmshEjPqJ39L31yWXRvp1d3ZvujsnWgbJAHZITIep"; // S
    private String apiHost = "spoonacular-recipe-food-nutrition-v1.p.mashape.com";

    public String urlBase = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/searchComplex?limitLicense=false&fillIngredients=true&offset=0&number=15&instructionsRequired=true&addRecipeInformation=true&ranking=0";
    public String urlIngredients = "&includeIngredients=";
    public String url = "";
    public String Jsonoutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        // get view components
        Button button = (Button) findViewById(R.id.button);
        txtString = (TextView) findViewById(R.id.textString);
        recipe_view = (ListView) findViewById(R.id.recipe_list);
        recipe_context = getApplicationContext();
        intent = new Intent(recipe_context, Recipe_Display.class);

        // Toolbar
        Toolbar toolbar=(Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab=getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        // Hiding elements
        recipe_view.setVisibility(View.GONE);
        button.setVisibility(View.GONE);

        // Get Recipe Ingredients
        FileInputStream stream=null;

        try {
            stream=openFileInput("pantry.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader reader=new BufferedReader(new InputStreamReader(stream));

        try {
            String line=reader.readLine();
            while(line!=null){
                String[] tokens=line.split(";");
                urlIngredients=addIngredientToList(urlIngredients,new Ingredient(tokens[0],Double.parseDouble(tokens[1]),tokens[2]));
                line=reader.readLine();
                if (line!=null)
                    urlIngredients = urlIngredients + "%2C+";
            }
            reader.close();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(urlIngredients.equals("&includeIngredients=") || urlIngredients.equals("")){
            txtString.setText("No Ingredients");
        }

        // Set recipe options
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String meal = bundle.getString(MEAL);
            if (meal != null)
                urlIngredients = urlIngredients + "&type=" + meal;
            String cuisine = bundle.getString(SELECTED_CUISINES);
            if (cuisine != null)
                urlIngredients = urlIngredients + "&cuisine=" + cuisine;
            String diet = bundle.getString(RESTRICTION);
            if (diet != null)
                urlIngredients = urlIngredients + "&diet=" + diet;
            String excludedIngredients = bundle.getString(EXCLUDED_INGREDIENTS);
            if (excludedIngredients != null)
                urlIngredients = urlIngredients + "&excludeIngredients=" + excludedIngredients;
        }
        url = urlBase + urlIngredients;
        // Send Http request
        OkHttpHandler okHttpHandler = new OkHttpHandler();
        okHttpHandler.execute(url);

        String temptext = "Finding Recipes";
        txtString.setText(temptext);

        /*
        // Button press
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                button.setVisibility(View.GONE);
                txtString.setVisibility(View.GONE);
                recipe_view.setVisibility(View.VISIBLE);
                url = urlBase + urlIngredients;
                // Send Http request
                OkHttpHandler okHttpHandler = new OkHttpHandler();
                okHttpHandler.execute(url);
            }
        });
        */

        // Select Recipe
        recipe_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //http://www.mkyong.com/android/android-prompt-user-input-dialog-example/
                LayoutInflater li=LayoutInflater.from(context);
                View previewView=li.inflate(R.layout.preview_display,null);
                final int pos=position;

                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setView(previewView);

                ImageView recipeImage=(ImageView)previewView.findViewById(R.id.preview_image);
                TextView recipeName=(TextView)previewView.findViewById(R.id.preview_name);
                TextView recipeTime=(TextView)previewView.findViewById(R.id.preview_time);

                try {
                    JSONObject root=new JSONObject(Jsonoutput);
                    JSONArray reader = root.getJSONArray("results");
                    JSONObject c = reader.getJSONObject(position);
                    String imageURL=c.getString("image");
                    new ImageLoader(recipeImage).execute(imageURL);
                    recipeName.setText(c.getString("title"));
                    recipeTime.setText(c.getString("readyInMinutes")+" minutes");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        okPressed(pos);
                    }
                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                builder.create().show();

            }
        });

    }

    /**
     * This method is called when the user chooses a recipe after previewing it
     */
    private void okPressed(int position) {
        //TODO: need to deduct from pantry
        txtString.setText(Long.toString(position));
        int idnum = position;
        String images = "";
        String instructions = "";
        String title="";
        String recipeId = "";

        // Parse JSON return
        try {
            JSONObject jsonroot = new JSONObject(Jsonoutput);
            JSONArray reader = jsonroot.getJSONArray("results");
            JSONObject c = reader.getJSONObject(idnum);
            images = c.getString("image");
            title = c.getString("title");
            recipeId = c.getString("id");

            JSONArray instructionlist = c.getJSONArray("analyzedInstructions").getJSONObject(0).getJSONArray("steps");
            for (int j = 0; j < instructionlist.length(); j++) {
                int tmp = j + 1;
                instructions = instructions + "Step " + tmp + ": " + instructionlist.getJSONObject(j).getString("step") + " \n \n";
            }
        } catch (JSONException e) {
            txtString.setText("fail Json parse");
        }

        Intent intent = new Intent(Recipes.this, Recipe_Display.class);
        Bundle bundle = new Bundle();
        bundle.putString("IMAGE_URL", images);
        bundle.putString("RECIPE_NAME",title);
        bundle.putString("INSTRUCTIONS", instructions);
        bundle.putString("RECIPE_ID", recipeId);

        //txtString.setText(bundle.getString("INSTRUCTIONS"));
        if (intent == null)
            txtString.setText("fail intent");
        intent.putExtras(bundle);
        startActivity(intent);

    }

    private String addIngredientToList(String list, Ingredient ingredient) {
        list = list + ingredient.getName();
        return list;
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
                txtString.setText("fail API connection");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            txtString.setText(s);
            Jsonoutput = s;
            List<String> recipetest = new ArrayList<String>();

            // Parse JSON return
            try {
                String test = "";
                JSONObject jsonroot = new JSONObject(s);
                JSONArray reader = jsonroot.getJSONArray("results");
                for (int i = 0; i < reader.length(); i++) {
                    JSONObject c = reader.getJSONObject(i);
                    recipetest.add(c.getString("title"));// + "     Likes: " + c.getString("likes"));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(recipe_context, android.R.layout.simple_list_item_1, recipetest);
                recipe_view.setAdapter(adapter);
            } catch (JSONException e) {
                txtString.setText("fail Json parse");
            }

            txtString.setVisibility(View.GONE);
            recipe_view.setVisibility(View.VISIBLE);
        }
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
}