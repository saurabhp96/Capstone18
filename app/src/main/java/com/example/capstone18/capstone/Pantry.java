package com.example.capstone18.capstone;

import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Pantry extends AppCompatActivity {

    public static int EDIT_IN=1;

    ListView pantryList;
    List<Ingredient> ingredientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);
        pantryList=(ListView)findViewById(R.id.pantry_list);


        try {
            FileInputStream inputStream=openFileInput("pantry.txt");
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            ingredientList=new ArrayList<Ingredient>();
            String line=null;
            while((line=bufferedReader.readLine())!=null) {
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


        ListAdapter adapter=new ArrayAdapter<Ingredient>(this,R.layout.pantry_item,ingredientList);
        pantryList.setAdapter(adapter);

        pantryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editIngredient(i);
            }
        });




    }

    public void editIngredient(int i) {
        Bundle bundle=new Bundle();

        Intent intent=new Intent(this,AddEditIngredient.class);
        intent.putExtras(bundle);
        startActivityForResult(intent,EDIT_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
