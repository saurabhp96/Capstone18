package com.example.capstone18.capstone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class RecipeOptions extends AppCompatActivity {

    private GridView gridView;
    private RadioGroup restrictionGroup,mealGroup;
    private String[] cuisines;
    private TextView excludedView;
    private EditText excludedInput;
    String excludedIngredients="";
    String viewText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_options);

        gridView=(GridView)findViewById(R.id.cuisine_grid);
        excludedInput=(EditText)findViewById(R.id.exc_input);
        excludedView=(TextView)findViewById(R.id.excluded_ing_list);
        cuisines=getResources().getStringArray(R.array.cuisines);
        Arrays.sort(cuisines);
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_multiple_choice,cuisines);
        gridView.setAdapter(adapter);


        restrictionGroup=(RadioGroup)findViewById(R.id.cuisine_restriction);
        mealGroup=(RadioGroup)findViewById(R.id.meal_group);


    }

    public void okPressed(View v){
        Bundle bundle=new Bundle();
        String selectedCuisines="";
        for(int i=0; i<cuisines.length; i++){
           CheckedTextView view= (CheckedTextView) gridView.getChildAt(i);
            if(view.isChecked()){
                selectedCuisines+=view.getText()+"%2C+";
            }
        }
        if(!selectedCuisines.isEmpty()) {
            selectedCuisines = selectedCuisines.substring(0, selectedCuisines.length() - 1);
            bundle.putString(Recipes.SELECTED_CUISINES, selectedCuisines);
        }

        int id=restrictionGroup.getCheckedRadioButtonId();
        if(id==R.id.vegeterian_button){
            bundle.putString(Recipes.RESTRICTION,getString(R.string.veg));
        }
        else if(id==R.id.pesc_button){
            bundle.putString(Recipes.RESTRICTION,getString(R.string.pesc));
        }

        if(!excludedIngredients.isEmpty()){
            bundle.putString(Recipes.EXCLUDED_INGREDIENTS,excludedIngredients.substring(0,excludedIngredients.length()-4));
        }

        id=mealGroup.getCheckedRadioButtonId();

        switch(id){
            case R.id.breakfast_button:
                bundle.putString(Recipes.MEAL,getString(R.string.breakfast));
                break;
            case R.id.lunch_button:
                bundle.putString(Recipes.MEAL,getString(R.string.lunch));
                break;
            case R.id.dinner_button:
                bundle.putString(Recipes.MEAL,getString(R.string.dinner));
                break;
        }

        Intent intent=new Intent(this,Recipes.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    public void cancelPressed(View v){
        finish();
    }

    public void addPressed(View v){
        if(excludedInput.getText()==null||excludedInput.getText().equals("")){
            Toast.makeText(this,"Excluded ingredient is blank",Toast.LENGTH_LONG).show();
            return;
        }
        excludedIngredients+=excludedInput.getText()+"%2C+";
        viewText += excludedInput.getText()+",";
        excludedInput.setText("");
        excludedView.setText(viewText);

    }
}
