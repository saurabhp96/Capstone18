package com.example.capstone18.capstone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class Recipes extends AppCompatActivity {
    String jsonReturn;
    TextView txtString;
    public String apiKey = "q0hVswUOhPmshMS5UZnQXk135TMap1SZItBjsnH12TyNDbPxzx";
    public String apiHost = "spoonacular-recipe-food-nutrition-v1.p.mashape.com";

    public String urlBase = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/searchComplex?limitLicense=true&offset=0&number=5&instructionsRequired=true&addRecipeInformation=true&ranking=2";
    public String urlIngredients = "";
    public String url= "";

    // new base call
    //"https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/searchComplex?limitLicense=true
    // &intolerances=peanut%2C+shellfish&type=main+course&maxFat=100&maxCalories=1500&minProtein=5
    // &maxProtein=100&cuisine=american&excludeIngredients=coconut%2C+mango&instructionsRequired=true&minFat=5
    // &includeIngredients=onions%2C+lettuce%2C+tomato&minCarbs=5&maxCarbs=100&offset=0&query=burger&minCalories=150&addRecipeInformation=true&number=10&ranking=0"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        /*
        FileOutputStream stream = openFileOutput("nameoffile.txt",Context.MODE_PRIVATE);
        PrintWriter w=new PrintWriter(stream); w.println("apple;2;lb"); w.println("sugar;2;cup");
        */

        // Recipe Button
        Button button = (Button) findViewById(R.id.button);
        // Code here executes on main thread after user presses button
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                txtString = (TextView)findViewById(R.id.textString);

                // Create URL string
                String ingredients[] = {"apple, sugar, flour"};
                urlIngredients = "&includeIngredients=";
                for(int i = 0; i < ingredients.length; i++){
                    urlIngredients = urlIngredients + ingredients[i];
                    if(i < ingredients.length -1)
                        urlIngredients = urlIngredients +"%2C+";
                }
                url = urlBase + urlIngredients;

                // Send Http request
                OkHttpHandler okHttpHandler= new OkHttpHandler();
                okHttpHandler.execute(url);
            }
        });
    }

    public class OkHttpHandler extends AsyncTask<String,Void,String> {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String...params) {

            Request.Builder builder = new Request.Builder();
            builder.url(params[0]);
            builder.header("X-Mashape-Key", apiKey)
                    .header("X-Mashape-Host", apiHost);
            Request request = builder.build();
            txtString.setText("attempt");
            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            }catch (Exception e){
                e.printStackTrace();
                txtString.setText("fail API connection");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            txtString.setText(s);
            /*
            jsonReturn = s;

            // Parse JSON return
            try{
                String test = "";

                JSONArray reader = new JSONArray(jsonReturn); //[{"name":"item 1"},{"name": "item2"}]

                for (int i = 0; i < reader.length(); i++){
                    JSONObject c = reader.getJSONObject(i);
                    test = test + c.getString("title") + " " + c.getString("likes") + "\n";
                }
                txtString.setText(test);

                //txtString.setText(jsonReturn);
            }
            catch(JSONException e){
                txtString.setText("fail Json parse");
            }
            */
        }
    }
}