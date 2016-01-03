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

public class FilesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView fileListView;
    private CryptoFileManager fileManager;
    private  ArrayAdapter<String>  fileListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);
        fileManager = CryptoFileManager.getInstance();
        Intent intent = getIntent();
        int indexCategories = intent.getIntExtra(CategorieActivity.EXTRA_CATEGORIE_INDEX, -1);
      //  int indexCategories = savedInstanceState.getInt(CategorieActivity.EXTRA_CATEGORIE_INDEX);
        String[] listFileString = fileManager.getFilesListFromCategorie(indexCategories);
        fileListAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listFileString);
        fileListView =(ListView)findViewById(R.id.listViewFiles);
        fileListView.setAdapter(fileListAdapter);
        fileListView.setOnItemClickListener(this);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_files, menu);
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
        openFile(position);
    }

    private void openFile(int filePosition){
        String fileName  = fileListAdapter.getItem(filePosition);
        fileManager.openDecryptedFile(fileName);

    }
}
