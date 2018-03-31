package com.example.capstone18.capstone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.RadioGroup;

public class RecipeOptions extends AppCompatActivity {

    private GridView gridView;
    private RadioGroup restrictionGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_options);

        gridView=(GridView)findViewById(R.id.cuisine_grid);
        String[] cuisines=getResources().getStringArray(R.array.cuisines);
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_multiple_choice,cuisines);
        gridView.setAdapter(adapter);
        restrictionGroup=(RadioGroup)findViewById(R.id.cuisine_restriction);


    }

    public void okPressed(View v){
        Bundle bundle=new Bundle();
        Intent intent=new Intent(this,Recipes.class);
        startActivity(intent,bundle);

    }

    public void cancelPressed(View v){
        finish();
    }
}
