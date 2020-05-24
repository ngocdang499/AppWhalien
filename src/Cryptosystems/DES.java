package Cryptosystems;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;

public class DES {
    private SecretKey secretKey;
    private Cipher cipher;

    public static CipherInputStream EncryptECB(File in, File keyFile)  throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, IOException {
        FileInputStream fis = new FileInputStream(in);

        String key = generateRandomKey();

        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());

        SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = skf.generateSecret(desKeySpec);

        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

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

        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

        cipher.init(cipher.DECRYPT_MODE, secretKey,SecureRandom.getInstance("SHA1PRNG"));
        CipherOutputStream cos = new CipherOutputStream(fos, cipher);

        return cos;
    }

    public static CipherInputStream EncryptCBC(File in, File keyFile)  throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, IOException, InvalidAlgorithmParameterException {
        FileInputStream fis = new FileInputStream(in);

        String key = generateRandomKey();

        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());

        SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = skf.generateSecret(desKeySpec);

        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

        byte[] ivBytes = new byte[8];
        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        cipher.init(cipher.ENCRYPT_MODE, secretKey, iv, SecureRandom.getInstance("SHA1PRNG"));
        CipherInputStream cis = new CipherInputStream(fis, cipher);
        //WriteProgress.write(cis,fos);z

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

        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

        byte[] ivBytes = new byte[8];
        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        cipher.init(cipher.DECRYPT_MODE, secretKey, iv, SecureRandom.getInstance("SHA1PRNG"));
        CipherOutputStream cos = new CipherOutputStream(fos, cipher);
        //WriteProgress.write(fis,cos);
        return cos;
    }

    private static String generateRandomKey() {
        byte[] array = new byte[8]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        return generatedString;
    }

}
