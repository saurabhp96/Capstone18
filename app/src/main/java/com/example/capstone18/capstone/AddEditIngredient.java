package com.example.capstone18.capstone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

public class AddEditIngredient extends AppCompatActivity {

    public static final String ING_NAME="name";
    public static final String ING_AMOUNT="amount";
    public static final String ING_UNIT="unit";

    private EditText ingredientName;
    private EditText ingredientAmount;
    private RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        ingredientName=(EditText)findViewById(R.id.ing_name);
        ingredientAmount=(EditText)findViewById(R.id.ing_amount);
        radioGroup=(RadioGroup)findViewById(R.id.radio_group_unit);
        int selected=radioGroup.getCheckedRadioButtonId();

        Bundle bundle=getIntent().getExtras();

        if(bundle!=null){//Edit ingredient
            ingredientName.setText(bundle.getString(ING_NAME));
            ingredientAmount.setText(String.valueOf(bundle.getDouble(ING_AMOUNT)));
            //TODO:set radiogroup to ingredient unit of measurement

            ingredientName.setEnabled(false);

        }
        else{//Add ingredient


        }

    }

    public void savePressed(View view){

        Bundle bundle=new Bundle();


        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);

    }

    public void cancelPressed(View view){

    }
}
