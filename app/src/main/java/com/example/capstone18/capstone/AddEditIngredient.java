package com.example.capstone18.capstone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AddEditIngredient extends AppCompatActivity {

    public static final String ING_NAME="name";
    public static final String ING_AMOUNT="amount";
    public static final String ING_UNIT="unit";
    public static final String ING_INDEX="index";

    private EditText ingredientName;
    private EditText ingredientAmount;
    private RadioGroup radioGroup;
    private int ingredientIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        ingredientName=(EditText)findViewById(R.id.ing_name);
        ingredientAmount=(EditText)findViewById(R.id.ing_amount);
        radioGroup=(RadioGroup)findViewById(R.id.radio_group_unit);


        Bundle bundle=getIntent().getExtras();

        if (bundle != null) {//Edit ingredient
            ingredientName.setText(bundle.getString(ING_NAME));
            ingredientAmount.setText(String.valueOf(bundle.getDouble(ING_AMOUNT)));
            ingredientIndex=bundle.getInt(ING_INDEX);
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                if (bundle.getString(ING_UNIT).equals(radioButton.getText())) {
                    radioGroup.check(radioButton.getId());
                }
            }

            ingredientName.setEnabled(false);
        }


    }

    public void savePressed(View view){
        String name=ingredientName.getText().toString();
        String inputAmount=ingredientAmount.getText().toString();
        int selectedId=radioGroup.getCheckedRadioButtonId();
        if(name==null||inputAmount==null||selectedId==-1){
            Toast.makeText(this,"All fields are required",Toast.LENGTH_LONG).show();
            return;
        }

        String unit=getString(R.string.physical_units);

        switch (selectedId){
            case R.id.radio_gram:
                unit=getString(R.string.gram);
                break;
            case R.id.radio_kg:
                unit=getString(R.string.kilogram);
                break;
            case R.id.radio_lb:
                unit=getString(R.string.lb);
                break;
        }

        Bundle bundle=new Bundle();
        bundle.putString(ING_NAME,name);
        bundle.putInt(ING_INDEX,ingredientIndex);
        bundle.putString(ING_UNIT,unit);
        bundle.putDouble(ING_AMOUNT,Double.parseDouble(inputAmount));


        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish();

    }

    public void cancelPressed(View view){
        setResult(RESULT_CANCELED);
        finish();
    }
}
