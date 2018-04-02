package com.example.capstone18.capstone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class RecipeOptions extends AppCompatActivity {

    private GridView gridView;
    private RadioGroup restrictionGroup;
    private String[] cuisines;
    private TextView excludedView;
    private EditText excludedInput;
    String excludedIngredients="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_options);

        gridView=(GridView)findViewById(R.id.cuisine_grid);
        excludedInput=(EditText)findViewById(R.id.exc_input);
        excludedView=(TextView)findViewById(R.id.excluded_ing_list);
        cuisines=getResources().getStringArray(R.array.cuisines);
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_multiple_choice,cuisines);
        gridView.setAdapter(adapter);


        restrictionGroup=(RadioGroup)findViewById(R.id.cuisine_restriction);
        //https://developer.android.com/guide/topics/ui/controls/checkbox.html
		//https://developer.android.com/reference/android/widget/GridView.html

    }

    public void okPressed(View v){
        Bundle bundle=new Bundle();
        String selectedCuisines="";
        for(int i=0; i<cuisines.length; i++){
           CheckedTextView view= (CheckedTextView) gridView.getChildAt(i);
            if(view.isChecked()){
                selectedCuisines+=view.getText()+",";
            }
        }
        selectedCuisines=selectedCuisines.substring(0,selectedCuisines.length()-1);
        bundle.putString(Recipes.selectedCuisines,selectedCuisines);

        int id=restrictionGroup.getCheckedRadioButtonId();
        if(id==R.id.vegeterian_button){
            bundle.putString(Recipes.restriction,getString(R.string.veg));
        }
        else if(id==R.id.pesc_button){
            bundle.putString(Recipes.restriction,getString(R.string.pesc));
        }

        if(!excludedIngredients.isEmpty()){
            bundle.putString(Recipes.excludedIngredients,excludedIngredients.substring(0,excludedIngredients.length()-1));
        }

        Intent intent=new Intent(this,Recipes.class);
        startActivity(intent,bundle);

    }

    public void cancelPressed(View v){
        finish();
    }

    public void addPressed(View v){
        if(excludedInput.getText()==null||excludedInput.getText().equals("")){
            Toast.makeText(this,"Excluded ingredient is blank",Toast.LENGTH_LONG).show();
            return;
        }
        excludedIngredients+=excludedInput.getText()+",";
        excludedInput.setText("");
        excludedView.setText(excludedIngredients);

    }
}
