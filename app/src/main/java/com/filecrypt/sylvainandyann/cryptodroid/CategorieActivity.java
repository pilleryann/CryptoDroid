package com.filecrypt.sylvainandyann.cryptodroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.filecrypt.sylvainandyann.cryptodroid.Models.CryptoFileManager;

import java.util.List;

public class CategorieActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    public static final String EXTRA_CATEGORIE_INDEX="CATEGORIE_INDEX";
    private ListView listCategories;
    private CryptoFileManager fileManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorie);
        listCategories =(ListView)findViewById(R.id.listViewCategorie);
        listCategories.setOnItemClickListener(this);

        String[] listCategoriesString = fileManager.getCategorieList();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listCategoriesString);
        listCategories.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_categorie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectCategorie(position);
    }

    private void selectCategorie(int fileIndex){
        Intent intent = new Intent(this, CategorieActivity.class);
        intent.putExtra(EXTRA_CATEGORIE_INDEX,fileIndex);
        startActivity(intent);
    }
}
