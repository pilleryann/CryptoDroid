package com.filecrypt.sylvainandyann.cryptodroid.Models;


public class CryptoFileManager {


    public static boolean isLoginValid(String userName, String password){
        return false;
    }

    public static boolean isLoginExist(String userName, String password){
        return false;
    }

    /**
     * Charge le login pour lire les catégorie et les fichiers ainsi que les décrypter.
     * @param userName
     * @param password
     */
    public void setLogin(String userName,String password){

    }

    public String[] getCategorieList(){
        return null;
    }

    public String[] getFilesListFromCategorie(int categorieIndex){
        return null;
    }

    public boolean openDecryptedFile(int fileIndex){
        return false;
    }
}
