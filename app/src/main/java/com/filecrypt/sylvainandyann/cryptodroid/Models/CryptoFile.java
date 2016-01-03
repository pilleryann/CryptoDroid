package com.filecrypt.sylvainandyann.cryptodroid.Models;

import java.io.File;

/**
 * Created by Snipy on 03.01.16.
 */
public class CryptoFile
{
    private String name;
    private String encryptName;
    private int category;

    public CryptoFile(String name, String encryptName, int category)
    {
        this.name = name;
        this.encryptName = encryptName;
        this.category = category;
    }
}
