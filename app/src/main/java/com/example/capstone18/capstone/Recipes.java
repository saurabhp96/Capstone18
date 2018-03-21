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

    public String url= "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/findByIngredients?ranking=1&number=5&ingredients=apples%2Cflour%2Csugar";

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
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                txtString = (TextView)findViewById(R.id.textString);
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
            builder.header("X-Mashape-Key", "q0hVswUOhPmshMS5UZnQXk135TMap1SZItBjsnH12TyNDbPxzx")
                    .header("X-Mashape-Host", "spoonacular-recipe-food-nutrition-v1.p.mashape.com");
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
            //txtString.setText(s);
            jsonReturn = s;

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
        }
    }

}