package com.filecrypt.sylvainandyann.cryptodroid.Models;


import android.content.Intent;

public class Utils{

    public static int getCategorieOfIntent(Intent intent){
        String type = intent.getType();
        int value =3;
        if (type.startsWith("image/")){
            value=0;
        }else if (type.startsWith("video/")){
            value =1;
        }else if (type.startsWith("text/")){
            value = 2;
        }
        return value;
    }

}
