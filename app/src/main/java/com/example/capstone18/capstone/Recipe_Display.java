package com.example.capstone18.capstone;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Recipe_Display extends AppCompatActivity {
    private TextView recipe_name,recipe_instructions;
    private ImageView recipe_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe__display);

        Toolbar toolbar=(Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        recipe_image=(ImageView)findViewById(R.id.recipeImage);
        recipe_name=(TextView)findViewById(R.id.recipe_name);
        recipe_instructions=(TextView)findViewById(R.id.recipe_instructions);


    }

    public void okPressed(View v){

    }

    public void cancelPressed(View v){finishActivity(RESULT_CANCELED);}
}
