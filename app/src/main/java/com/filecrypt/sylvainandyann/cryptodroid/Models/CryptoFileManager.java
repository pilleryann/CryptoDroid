package com.filecrypt.sylvainandyann.cryptodroid.Models;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CryptoFileManager
{
    private static CryptoFileManager instance;
    private File dataFolder;
    private SharedPreferences settings;
    private HashMap<String, CryptoFile> mapFiles;
    private String username;
    private String password;

    private CryptoFileManager()
    {
        loadMap();
    }

    public static CryptoFileManager getInstance()
    {
        if(instance == null)
        {
            instance = new CryptoFileManager();
        }
        return instance;
    }

    private void loadMap()
    {

        File file = new File(dataFolder, ".map");
        try
        {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
            //noinspection unchecked
            mapFiles = (HashMap<String, CryptoFile>) inputStream.readObject();
            inputStream.close();
        }
        catch(IOException | ClassNotFoundException e)
        {
            // TODO toast can't save data
            e.printStackTrace();
        }
    }

    private void saveMap()
    {
        File file = new File(dataFolder, ".map");
        try
        {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(mapFiles);
            outputStream.flush();
            outputStream.close();
        }
        catch(IOException e)
        {
            // TODO toast can't load data
            e.printStackTrace();
        }
    }

    public boolean isLoginValid(String username, String password)
    {

        return username.equals(this.username) && password.equals(this.password);
    }

    public boolean isLoginExist()
    {
        return true;
    }

    /**
     * Charge le login pour lire les catégorie et les fichiers ainsi que les décrypter.
     *
     * @param username
     * @param password
     */
    public void setLogin(String username, String password)
    {
        this.username = username;
        this.password = password;
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
        // 3 : Raw data
        String[][] results = {{"Image1", "Image2", "Image3"}, {"Video1", "Video2", "Video3"}, {"Text1", "Text2", "Text3"}};
        return results[categorieIndex];
    }

    // decrypt and open the file
    public boolean openDecryptedFile(String filename)
    {
        return true;
    }

    public boolean cryptFile(Intent intent, int categorieIndex)
    {
        try
        {
            if(Intent.ACTION_SEND.equals(intent.getAction()))
            {
                Uri uri = intent.getClipData().getItemAt(0).getUri();
                if(null == uri)
                {
                    throw new Exception("URI is null");
                }
                else
                {
                    String encryptedFile = encryptFile(uri.getPath());
                    String filename = org.apache.commons.io.FilenameUtils.getBaseName(uri.getPath());
                    mapFiles.put(filename, new CryptoFile(filename, encryptedFile, categorieIndex));
                    saveMap();
                    // TODO make toast to confirm
                }
            }
            else if(Intent.ACTION_SEND_MULTIPLE.equals(intent.getAction()))
            {
                // TODO for multiple file encryption
            }
            else
            {
                // TODO Other Action use, do toast and nothing
            }
        }
        catch(Exception e)
        {
            // TODO catch exception, encryption failed
        }
        return false;
    }

    /**
     * Generate the key for encryption from a password store in prefenreces setting
     *
     * @return array of byte who contains the generated key
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     */
    private byte[] getKey() throws NoSuchAlgorithmException, NoSuchProviderException
    {
        if(!settings.contains("password"))
        {
            settings.edit().putString("password", password);
            settings.edit().apply();
        }
        String key = settings.getString("password", password);
        byte[] keyStart = key.getBytes();

        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(keyStart);
        keyGenerator.init(128, secureRandom);
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    /**
     * @param path path of the file to encrypt
     * @return String which is the path of the encrypted file
     * @throws Exception
     */
    private String encryptFile(String path) throws Exception
    {

        byte[] data = fileToByteArray(path);
        byte[] key = getKey();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] cryptData = cipher.doFinal(data);

        // Save file to device
        String filename = org.apache.commons.io.FilenameUtils.getBaseName(path);
        String extension = org.apache.commons.io.FilenameUtils.getExtension(path);
        if(!dataFolder.exists())
        {
            if(!dataFolder.mkdirs())
            {
                throw new IOException("Cannot create new folder : " + dataFolder.getPath());
            }
        }

        File encryptFile = new File(dataFolder.getAbsolutePath(), "crypt_" + filename + "." + extension);
        if(encryptFile.exists())
        {
            if(!encryptFile.delete())
            {
                throw new IOException("Cannot override existing file : " + encryptFile.getPath());
            }
        }
        if(encryptFile.createNewFile())
        {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(encryptFile));
            bufferedOutputStream.write(cryptData);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        }
        else
        {
            throw new IOException("Cannot create new file : " + encryptFile.getPath());
        }
        return encryptFile.getPath();
    }

    private byte[] fileToByteArray(String path) throws IOException
    {
        File file = new File(path);
        long size = file.length();
        byte[] res = new byte[(int) size];
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
        //noinspection ResultOfMethodCallIgnored
        bufferedInputStream.read(res, 0, res.length);
        bufferedInputStream.close();
        return res;
    }

    public void setDataFolder(File dataFolder)
    {
        this.dataFolder = dataFolder;
    }

    public void setSettings(SharedPreferences settings)
    {
        this.settings = settings;
    }
}
