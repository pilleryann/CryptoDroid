package com.filecrypt.sylvainandyann.cryptodroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.filecrypt.sylvainandyann.cryptodroid.Models.CryptoFileManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText userName;
    private EditText password;
    private Button enterButton;
    private Button saveButton;
    private CryptoFileManager fileManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fileManager=CryptoFileManager.getIntance();
        userName = (EditText)findViewById(R.id.editTextUserName);
        password = (EditText)findViewById(R.id.editTextPassword);
        enterButton = (Button)findViewById(R.id.buttonEnter);
        saveButton = (Button)findViewById(R.id.buttonSaveUser);

        saveButton.setOnClickListener(this);
        enterButton.setOnClickListener(this);

        boolean userExist = fileManager.isLoginExist();
        if(userExist) {
            saveButton.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void onClick(View v) {
        String userStr = userName.getText().toString();
        String passwordStr = password.getText().toString();
        if(v.equals(enterButton)){
            enterUser(userStr,passwordStr);
        }else if (v.equals(saveButton)){
            saveUser(userStr,passwordStr);
        }

    }

    private void saveUser(String user,String password){
       boolean loginValid = fileManager.isLoginValid(user,password);
        if(loginValid){
            Intent intent = new Intent(this, CategorieActivity.class);
            startActivity(intent);
        }else{
            //Afficher un message d'erreur
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Login Failed");
            builder.setMessage("The login or the password is false.");
            this.password.getText().clear();
            this.userName.getText().clear();
        }
    }

    private void enterUser(String user,String password){
        fileManager.setLogin(user,password);
        Intent intent = new Intent(this, CategorieActivity.class);
        startActivity(intent);
    }
}
