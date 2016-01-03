package com.filecrypt.sylvainandyann.cryptodroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.filecrypt.sylvainandyann.cryptodroid.Models.CryptoFileManager;
import com.filecrypt.sylvainandyann.cryptodroid.Models.Utils;

public class EncryptFileFromSharingActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt_file_from_sharing);
        TextView textView = (TextView) findViewById(R.id.statusLabel);
        initExitButton();
        Intent intent = getIntent();
        CryptoFileManager cryptoFileManager = CryptoFileManager.getInstance();
        int categorie = Utils.getCategorieOfIntent(intent);
        try
        {

            if(cryptoFileManager.cryptFile(intent, categorie))
            {
                textView.setText("Encryption successfull");
            }
            else
            {
                textView.setText("Encryption has failed");
            }
        }
        catch(UnsupportedOperationException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    private void initExitButton()
    {
        Button button = (Button) findViewById(R.id.exitButton);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
                System.exit(0);
            }
        });
    }

}
