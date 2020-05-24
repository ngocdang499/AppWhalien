package Cryptosystems;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSA {
    public static void generateRSAKeyPair(String fileBase) throws NoSuchAlgorithmException, IOException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();

        try (FileOutputStream fos = new FileOutputStream(fileBase + ".key")) {
            fos.write(kp.getPrivate().getEncoded());
            fos.flush();
        }

        try (FileOutputStream fos = new FileOutputStream(fileBase + ".pub")) {
            fos.write(kp.getPublic().getEncoded());
            fos.flush();
        }

    }


    public static Cipher EncryptECB(File pubKeyFile ) throws Exception {
        FileInputStream fis = new FileInputStream(pubKeyFile);
        byte[] keyByte = new byte[fis.available()];
        fis.read(keyByte);
        fis.close();

        // Tạo public key
        X509EncodedKeySpec ks = new X509EncodedKeySpec(keyByte);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey pubKey = kf.generatePublic(ks);

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);

        return cipher;
    }

    public static Cipher DecryptECB(File priKeyFile ) throws Exception {
        FileInputStream fis = new FileInputStream(priKeyFile);
        byte[] keyByte = new byte[fis.available()];
        fis.read(keyByte);
        fis.close();

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyByte);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PrivateKey priKey = factory.generatePrivate(spec);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);

        return cipher;
    }
}
