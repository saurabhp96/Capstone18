package com.example.capstone18.capstone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button recipeButton=findViewById(R.id.recipesButton);
        Button pantryButton=findViewById(R.id.pantryButton);
        Button shoppingListButton = (Button) findViewById(R.id.shoppingListButton);

        recipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent recipeIntent=new Intent(MainActivity.this,Recipes.class);
                startActivity(recipeIntent);

            }
        });

        pantryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pantryIntent=new Intent(MainActivity.this,Pantry.class);
                startActivity(pantryIntent);
            }
        });

        shoppingListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shoppingListIntent = new Intent (MainActivity.this, ShoppingList.class);
                startActivity(shoppingListIntent);
            }
        });

    }
}
