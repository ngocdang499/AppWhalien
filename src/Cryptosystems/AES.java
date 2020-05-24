package Cryptosystems;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Random;

public class AES {
    private SecretKey secretKey;
    private Cipher cipher;

    public static CipherInputStream EncryptECB(File in, File keyFile)  throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, IOException {
        FileInputStream fis = new FileInputStream(in);


        SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

        cipher.init(cipher.ENCRYPT_MODE, secretKey,SecureRandom.getInstance("SHA1PRNG"));
        CipherInputStream cis = new CipherInputStream(fis, cipher);
        //WriteProgress.write(cis,fos);

        ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream(keyFile));
        try {
            objOut.writeObject(secretKey);
        } finally {
            objOut.close();
        }

        return cis;
    }

    public static CipherOutputStream DecryptECB(File out, File keyFile)  throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, IOException {

        FileOutputStream fos = new FileOutputStream(out);

        ObjectInputStream objIn = new ObjectInputStream(new FileInputStream(keyFile));
        SecretKey secretKey = null;
        try {
            secretKey = (SecretKey)objIn.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            objIn.close();
        }

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

        cipher.init(cipher.DECRYPT_MODE, secretKey,SecureRandom.getInstance("SHA1PRNG"));
        CipherOutputStream cos = new CipherOutputStream(fos, cipher);

        return cos;
    }

    public static CipherInputStream EncryptCBC(File in, File keyFile)  throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, IOException, InvalidAlgorithmParameterException {
        FileInputStream fis = new FileInputStream(in);

        SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        byte[] ivBytes = new byte[16];
        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        cipher.init(cipher.ENCRYPT_MODE, secretKey, iv, SecureRandom.getInstance("SHA1PRNG"));
        CipherInputStream cis = new CipherInputStream(fis, cipher);

        ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream(keyFile));
        try {
            objOut.writeObject(secretKey);
        } finally {
            objOut.close();
        }

        return cis;
    }

    public static CipherOutputStream DecryptCBC(File out, File keyFile)  throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, IOException, InvalidAlgorithmParameterException {

        FileOutputStream fos = new FileOutputStream(out);

        ObjectInputStream objIn = new ObjectInputStream(new FileInputStream(keyFile));
        SecretKey secretKey = null;
        try {
            secretKey = (SecretKey)objIn.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            objIn.close();
        }

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        byte[] ivBytes = new byte[16];
        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        cipher.init(cipher.DECRYPT_MODE, secretKey, iv, SecureRandom.getInstance("SHA1PRNG"));
        System.out.print("here");
        CipherOutputStream cos = new CipherOutputStream(fos, cipher);

        return cos;
    }


}
