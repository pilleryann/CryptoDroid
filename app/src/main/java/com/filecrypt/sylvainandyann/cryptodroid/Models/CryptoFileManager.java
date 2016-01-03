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
import java.util.LinkedList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CryptoFileManager
{
    private static CryptoFileManager instance;
    private File dataFolder;
    private File cacheDir;
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
        if(file.exists())
        {
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
        else
        {
            mapFiles = new HashMap<>();
            saveMap();
        }
    }

    private void saveMap()
    {
        File file = new File(dataFolder, ".map");

        try
        {
            if(file.createNewFile())
            {
                ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
                outputStream.writeObject(mapFiles);
                outputStream.flush();
                outputStream.close();
            }
            else
            {
                throw new Exception("Can't save data to .map");
            }
        }
        catch(Exception e)
        {
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
        return new String[]{"Images", "Photos", "Text", "Raw"};
    }

    // 0 : Image
    // 1 : Video
    // 2 : Text
    // 3 : Raw data
    public List<String> getFilesListFromCategorie(int categorieIndex)
    {
        List<String> res = new LinkedList<>();
        for(CryptoFile file : mapFiles.values())
        {
            if(file.getCategory() == categorieIndex)
            {
                res.add(file.getName());
            }
        }
        return res;
    }

    // decrypt and open the file
    public Intent openDecryptedFile(String filename)
    {
        CryptoFile cryptoFile = mapFiles.get(filename);
        try
        {
            byte[] data = decryptFile(filename);
            File tmpFile = File.createTempFile("cryptodroid_tmp", cryptoFile.getExtensions(), cacheDir);
            FileOutputStream outputStream = new FileOutputStream(tmpFile);
            outputStream.write(data);
            outputStream.close();

            // launch app
            Uri uri = Uri.fromFile(tmpFile);
            return new Intent(Intent.ACTION_VIEW, uri);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
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
                    String extensions = org.apache.commons.io.FilenameUtils.getExtension(uri.getPath());
                    mapFiles.put(filename, new CryptoFile(filename, encryptedFile, extensions, categorieIndex));
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

    private byte[] decryptFile(String path) throws Exception
    {
        byte[] data = fileToByteArray(path);
        byte[] key = getKey();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

        return cipher.doFinal(data);
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

    public void setCacheDir(File cacheDir)
    {
        this.cacheDir = cacheDir;
    }
}
