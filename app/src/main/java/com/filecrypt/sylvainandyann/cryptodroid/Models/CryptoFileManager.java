package com.filecrypt.sylvainandyann.cryptodroid.Models;


public class CryptoFileManager {

    private static CryptoFileManager instance;

    public  boolean isLoginValid(String userName, String password){

        return userName.equals("user") && password.equals("123");
    }

    public  boolean isLoginExist(){
        return true;
    }

    /**
     * Charge le login pour lire les catégorie et les fichiers ainsi que les décrypter.
     * @param userName
     * @param password
     */
    public void setLogin(String userName,String password){

    }

    public String[] getCategorieList(){
        String[] result = {"Images","Photos","Text"};
        return result;
    }

    public String[] getFilesListFromCategorie(int categorieIndex){
        String[][] results = {{"Image1","Image2","Image3"},{"Photo1","Photo2","Photo3"},{"Text1","Text2","Text3"}};
        return results[categorieIndex];
    }

    public boolean openDecryptedFile(int fileIndex){
        return true;
    }

    public static CryptoFileManager getInstance(){
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
