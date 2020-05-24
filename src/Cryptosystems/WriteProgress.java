package Cryptosystems;

import javafx.concurrent.Task;
import javafx.fxml.FXML;

import javax.crypto.*;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

public class WriteProgress extends Task<Void> {
    public int byteCount;
    public int total;
    public ArrayList<File> pickList;
    public String outPath;
    public String mode;
    public String rsaKeyPath = null;
    public int cipherMode;
    public WriteProgress(ArrayList<File> fileList, int _total, String _out, String _mode, String _rsaKey, int _cipherMode) {
        byteCount = 0;
        total = _total;
        pickList = fileList;
        outPath = _out;
        mode = _mode;
        if(mode.contains("RSA")) {
            rsaKeyPath = _rsaKey;
            System.out.println(_rsaKey);
        }
        cipherMode = _cipherMode;
    }

    @Override
    protected Void call() throws Exception {

        for(File inFile : pickList) {

            System.out.println(inFile.getPath());
            String keyF = outPath + File.separator + inFile.getName().split("[.]")[0] + "_key.txt";
            //String outF = outPath.getText()+File.separator+inFile.getName().replace(".enc","");
            File keyFile = new File(keyF);


            if (cipherMode == Cipher.ENCRYPT_MODE) {
                String outF = outPath + File.separator + inFile.getName() + ".enc";
                File outFile = new File(outF);

                CipherInputStream cis = null;
                FileOutputStream fos = new FileOutputStream(outFile);
                Cipher cipher = null;
                try {
                    switch (mode) {
                        case "DES/ECB":
                            cis = DES.EncryptECB(inFile, keyFile);
                            break;
                        case "DES/CBC":
                            cis = DES.EncryptCBC(inFile, keyFile);
                            break;
                        case "AES/ECB":
                            cis = AES.EncryptECB(inFile, keyFile);
                            break;
                        case "AES/CBC":
                            cis = AES.EncryptCBC(inFile, keyFile);
                            break;
                        case "RSA/ECB":
                            System.out.println(rsaKeyPath);
                            keyFile = new File(rsaKeyPath);
                            System.out.println(inFile.length());

                            cipher = RSA.EncryptECB(keyFile);
                            break;
                        default:
                            break;
                    }
                } catch (InvalidKeySpecException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException | IOException ex) {
                    ex.printStackTrace();
                }

                if (mode.contains("RSA")) {
                    FileInputStream fis = new FileInputStream(inFile);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = fis.read(buffer)) != -1) {
                        byte[] obuf = cipher.update(buffer, 0, len);
                        if ( obuf != null ) fos.write(obuf);
                        byteCount += len;

                        this.updateProgress(byteCount, total);
                    }
                    byte[] obuf = cipher.doFinal();
                    if ( obuf != null ) fos.write(obuf);
                }
                else {
                    System.out.println("here");
                    byte[] buffer = new byte[1024];
                    int numOfBytesRead;
                    while ((numOfBytesRead = cis.read(buffer)) != -1) {
                        fos.write(buffer, 0, numOfBytesRead);
                        byteCount += numOfBytesRead;

                        this.updateProgress(byteCount, total);

                    }
                    cis.close();
                    fos.close();
                }
            }
            else if (cipherMode == Cipher.DECRYPT_MODE) {
                String outF = outPath + File.separator + inFile.getName().replace( ".enc", "");
                File outFile = new File(outF);

                CipherOutputStream cos = null;
                FileInputStream fis = new FileInputStream(inFile);
                Cipher cipher = null;
                try {
                    switch (mode) {
                        case "DES/ECB":
                            cos = DES.DecryptECB(outFile, keyFile);
                            break;
                        case "DES/CBC":
                            cos = DES.DecryptCBC(outFile, keyFile);
                            break;
                        case "AES/ECB":
                            cos = AES.DecryptECB(outFile, keyFile);
                            break;
                        case "AES/CBC":
                            cos = AES.DecryptCBC(outFile, keyFile);
                            break;
                        case "RSA/ECB":
                            keyFile = new File(rsaKeyPath);
                            cipher = RSA.DecryptECB(keyFile);
                            break;
                        default:
                            break;
                    }
                } catch (InvalidKeySpecException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException | IOException ex) {
                    ex.printStackTrace();
                }

                System.out.println(mode);
                if (mode.contains("RSA")) {
                    fis = new FileInputStream(inFile);
                    FileOutputStream fos = new FileOutputStream(outFile);
                    byte[] buffer = new byte[1024];
                    System.out.println(outFile.getPath());
                    int len;
                    while ((len = fis.read(buffer)) != -1) {
                        byte[] obuf = cipher.update(buffer, 0, len);
                        if ( obuf != null ) fos.write(obuf);
                        byteCount += len;

                        this.updateProgress(byteCount, total);
                    }

                    byte[] obuf = cipher.doFinal();
                    if ( obuf != null ) fos.write(obuf);
                }
                else {
                    System.out.println("here");
                    System.out.println(total);
                    byte[] buffer = new byte[1024];
                    int numOfBytesRead;
                    while ((numOfBytesRead = fis.read(buffer)) != -1) {
                        System.out.println(byteCount);
                        cos.write(buffer, 0, numOfBytesRead);
                        byteCount += numOfBytesRead;

                        this.updateProgress(byteCount, total);

                    }
                    cos.close();
                }
                fis.close();
            }
        }
        return null;
    }
}
