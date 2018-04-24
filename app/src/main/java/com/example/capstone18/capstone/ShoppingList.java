package com.example.capstone18.capstone;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ShoppingList extends AppCompatActivity {

    public static final int EDIT_INTENT =1;
    public static final int ADD_INTENT=2;

    ListView shoppingList;
    List<Ingredient> ingredientList;

    @Override
    protected void onStop() {
        try {
            FileOutputStream outputStream=openFileOutput("shoppingList.txt", Context.MODE_PRIVATE);
            PrintWriter writer=new PrintWriter(outputStream);
            for(Ingredient i:ingredientList){
                writer.println(i.getName()+";"+i.getQuantity()+";"+i.getMeasurementUnit());
            }
            writer.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        shoppingList =(ListView)findViewById(R.id.shopping_list);
        ingredientList = new ArrayList<Ingredient>();

        Toolbar toolbar=(Toolbar)findViewById(R.id.shopping_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        File file = new File(this.getFilesDir(), "shoppingList.txt");

        if(file.exists()) {
            try {
                FileInputStream inputStream = openFileInput("shoppingList.txt");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    String[] tokens = line.split(";");
                    ingredientList.add(new Ingredient(tokens[0], Double.parseDouble(tokens[1]), tokens[2]));
                }
                bufferedReader.close();
                inputStream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        ListAdapter adapter=new ArrayAdapter<Ingredient>(this,R.layout.shopping_list_item,ingredientList);
        shoppingList.setAdapter(adapter);

        shoppingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editIngredient(i);
            }
        });
    }

    public void editIngredient(int i) {
        Bundle bundle=new Bundle();
        Ingredient ingredient=ingredientList.get(i);
        bundle.putInt(AddEditIngredient.ING_INDEX,i);
        bundle.putString(AddEditIngredient.ING_NAME,ingredient.getName());
        bundle.putString(AddEditIngredient.ING_UNIT,ingredient.getMeasurementUnit());
        bundle.putDouble(AddEditIngredient.ING_AMOUNT,ingredient.getQuantity());
        Intent intent=new Intent(this,AddEditIngredient.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, EDIT_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_OK)
            return;

        Bundle bundle=data.getExtras();
        if(bundle==null)
            return;

        if (requestCode == EDIT_INTENT) {
            String name = bundle.getString(AddEditIngredient.ING_NAME);
            String unit = bundle.getString(AddEditIngredient.ING_UNIT);
            double amount = bundle.getDouble(AddEditIngredient.ING_AMOUNT);
            Ingredient selected = ingredientList.get(bundle.getInt(AddEditIngredient.ING_INDEX));
            selected.setName(name);
            selected.setMeasurementUnit(unit);
            selected.setQuantity(amount);
        } else if (requestCode == ADD_INTENT) {
            Ingredient newIng = new Ingredient(bundle.getString(AddEditIngredient.ING_NAME),
                    bundle.getDouble(AddEditIngredient.ING_AMOUNT),
                    bundle.getString(AddEditIngredient.ING_UNIT));
            int index;
            if ((index = ingredientList.indexOf(newIng)) >= 0) {
                Ingredient listIng = ingredientList.get(index);
                String listUnit = listIng.getMeasurementUnit();
                double conversionFactor = findConversionFactor(newIng.getMeasurementUnit(), listUnit);
                listIng.setQuantity(listIng.getQuantity() + conversionFactor * newIng.getQuantity());
            } else {
                ingredientList.add(newIng);
            }

        } else {
            Toast.makeText(this, "Error, with returning from activity", Toast.LENGTH_LONG).show();
            return;
        }
        shoppingList.setAdapter(new ArrayAdapter<Ingredient>(this,R.layout.shopping_list_item,ingredientList));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.add_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                addIngredient();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void addIngredient() {
        Intent intent=new Intent(this,AddEditIngredient.class);
        startActivityForResult(intent,ADD_INTENT);
    }

    /**
     * Function to find the conversion factor from one unit of measurement to another unit of measurement
     * @param from the unit of measurement to convert from
     * @param to the unit of measurement to convert to
     * @return conversion factor
     */
    private double findConversionFactor(String from, String to) {
        String lb=getString(R.string.lb);
        String kg=getString(R.string.kilogram);
        String gram=getString(R.string.gram);

        if(from.equals(to)){
            return 1.0;
        }

        if(from.equals(lb)){
            if(to.equals(kg)){
                return 0.45359237;
            }
            else if(to.equals(gram)){
                return 0.45359237/1000;
            }

        }
        else if(from.equals(kg)){
            if(to.equals(gram)){
                return 1000.0;
            }
            else if(to.equals(lb)){
                return 2.20462262185;
            }
        }
        else if(from.equals(gram)){
            if(to.equals(kg)){
                return 1.0/1000;
            }
            else if(to.equals(lb)){
                return 2.20462262185/1000;
            }

        }
        return -1.0;
    }
}
