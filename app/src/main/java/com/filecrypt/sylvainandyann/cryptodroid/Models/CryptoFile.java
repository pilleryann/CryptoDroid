package com.filecrypt.sylvainandyann.cryptodroid.Models;

import java.io.File;

/**
 * Created by Snipy on 03.01.16.
 */
public class CryptoFile
{
    private String name;
    private String encryptName;
    private String extensions;
    private int category;

    public CryptoFile(String name, String encryptName, String extensions, int category)
    {
        this.name = name;
        this.encryptName = encryptName;
        this.extensions = extensions;
        this.category = category;
    }

    public String getName()
    {
        return name;
    }

    public String getEncryptName()
    {
        return encryptName;
    }

    public String getExtensions()
    {
        return extensions;
    }

    public int getCategory()
    {
        return category;
    }
}
