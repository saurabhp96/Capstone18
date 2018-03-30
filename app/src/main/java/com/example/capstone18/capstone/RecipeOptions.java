package com.example.capstone18.capstone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;

public class RecipeOptions extends AppCompatActivity {

    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_options);

        gridView=(GridView)findViewById(R.id.cuisine_grid);
        String[] cuisines=getResources().getStringArray(R.array.cuisines);
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_multiple_choice,cuisines);
        gridView.setAdapter(adapter);


    }
}
