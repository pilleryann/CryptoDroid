package com.filecrypt.sylvainandyann.cryptodroid.Models;


import android.content.Intent;

public class CryptoFileManager
{
    private int categoryIndexUse = -1;

    private static CryptoFileManager instance;

    private CryptoFileManager()
    {

    }

    public static CryptoFileManager getInstance()
    {
        if(instance == null)
        {
            instance = new CryptoFileManager();
        }
        return instance;
    }

    public boolean isLoginValid(String userName, String password)
    {

        return userName.equals("user") && password.equals("123");
    }

    public boolean isLoginExist()
    {
        return true;
    }

    /**
     * Charge le login pour lire les catégorie et les fichiers ainsi que les décrypter.
     *
     * @param userName
     * @param password
     */
    public void setLogin(String userName, String password)
    {

    }

    public String[] getCategorieList()
    {
        String[] result = {"Images", "Photos", "Text"};
        return result;
    }

    public String[] getFilesListFromCategorie(int categorieIndex)
    {
        // 0 : Image
        // 1 : Video
        // 2 : Text
        String[][] results = {{"Image1", "Image2", "Image3"}, {"Video1", "Video2", "Video3"}, {"Text1", "Text2", "Text3"}};
        categoryIndexUse = categorieIndex;
        return results[categorieIndex];
    }

    // decrypt and open the file
    public boolean openDecryptedFile(int fileIndex)
    {
        return true;
    }

    public boolean cryptFile(Intent intent, int categorieIndex)
    {
        return false;
    }


}
