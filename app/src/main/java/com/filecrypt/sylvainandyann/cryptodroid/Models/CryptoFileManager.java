package com.filecrypt.sylvainandyann.cryptodroid.Models;


public class CryptoFileManager {

    private static CryptoFileManager instance;

    public  boolean isLoginValid(String userName, String password){
        return false;
    }

    public  boolean isLoginExist(){
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

    public static CryptoFileManager getIntance(){
        if(instance==null){
            instance= new CryptoFileManager();
        }
        return instance;


    }
    private CryptoFileManager (){

    }

    public void addCategorie(String categorie){

    }

    public void cryptFile(int categorieIndex,String file){

    }
}
