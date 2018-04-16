package com.example.capstone18.capstone;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class Recipes extends AppCompatActivity {
    public static final String SELECTED_CUISINES = "selected";
    public static final String RESTRICTION = "restriction";
    public static final String EXCLUDED_INGREDIENTS = "excluded";
    public static final String MEAL = "meal";

    TextView txtString;
    ListView recipe_view;
    Context recipe_context;
    Intent intent;

    public String apiKey = "q0hVswUOhPmshMS5UZnQXk135TMap1SZItBjsnH12TyNDbPxzx"; //P
    //private String apiKey = "K3hkrfTbpzmshEjPqJ39L31yWXRvp1d3ZvujsnWgbJAHZITIep"; // S
    public String apiHost = "spoonacular-recipe-food-nutrition-v1.p.mashape.com";

    public String urlBase = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/searchComplex?limitLicense=false&offset=0&number=5&instructionsRequired=true&addRecipeInformation=true&ranking=0";
    public String urlIngredients = "&includeIngredients=";
    public String url = "";

    public String Jsonoutput;

    // new reference call
    //"https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/searchComplex?limitLicense=true
    // &intolerances=peanut%2C+shellfish&type=main+course&maxFat=100&maxCalories=1500&minProtein=5
    // &maxProtein=100&cuisine=american&excludeIngredients=coconut%2C+mango&instructionsRequired=true&minFat=5
    // &includeIngredients=onions%2C+lettuce%2C+tomato&minCarbs=5&maxCarbs=100&offset=0&query=burger&minCalories=150&addRecipeInformation=true&number=10&ranking=0"

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
        txtString.setText(urlIngredients);

        // Code here executes on main thread after user presses button
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                url = urlBase + urlIngredients;
                // Send Http request
                OkHttpHandler okHttpHandler = new OkHttpHandler();
                okHttpHandler.execute(url);
            }
        });

        // Select Recipe
        recipe_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txtString.setText(Long.toString(id));
                int idnum = (int) id;
                String images = "";
                String instructions = "";

                // Parse JSON return
                try {
                    JSONObject jsonroot = new JSONObject(Jsonoutput);
                    JSONArray reader = jsonroot.getJSONArray("results");
                    JSONObject c = reader.getJSONObject(idnum);
                    images = c.getString("image");

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
                bundle.putString("RECIPE_NAME", parent.getItemAtPosition(position).toString());
                bundle.putString("INSTRUCTIONS", instructions);

                if (intent == null)
                    txtString.setText("fail intent");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

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
            txtString.setText("attempt");
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
                    recipetest.add(c.getString("title") + "     Likes: " + c.getString("likes"));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(recipe_context, android.R.layout.simple_list_item_1, recipetest);
                recipe_view.setAdapter(adapter);
            } catch (JSONException e) {
                txtString.setText("fail Json parse");
            }
        }
    }
}