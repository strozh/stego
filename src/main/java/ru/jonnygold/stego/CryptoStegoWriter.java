/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jonnygold.stego;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Vanchpuck
 */
public class CryptoStegoWriter implements Writeble{


    protected static final String KEY_FACTORY = "PBKDF2WithHmacSHA1";

    // protected static final String ENCRIPTION_ALGORITHM = "AES/CBC/PKCS5Padding";
    protected static final String ENCRIPTION_ALGORITHM = "AES";

    protected static final char[] DEFAULT_PASSWORD = "abcdefgh".toCharArray();

    protected static final byte[] SALT = "megasalt".getBytes();

    protected static final int ITERATIONS_COUNT = 65536;

    protected static final int KEY_LENGTH = 128;

    protected static final int MAX_PASSWORD_LENGTH = 64;



    protected char[] password;

    protected Writeble writer;

    protected SecretKeyFactory factory;

    protected Cipher cipher;

    protected IvParameterSpec ivSpec = null;

    /*
    * Test
    */
    public CryptoStegoWriter(Writeble writer) throws GeneralSecurityException{
        
        this.writer = writer;

        this.password = DEFAULT_PASSWORD;

        factory = SecretKeyFactory.getInstance(KEY_FACTORY);

        cipher = Cipher.getInstance(ENCRIPTION_ALGORITHM);
    }

    @Override
    public void write(Secret data) throws IOException {
        try {
            // setting encrypted data to Secret object
            data.setAttachment(encrypt(data.getAttachment()));
            data.setData(encrypt(data.getData()));

            // writing encrypted data
            this.writer.write(data);
        } 
        catch (GeneralSecurityException e) {
            throw new IOException("Exception during the encryption has been occurred");
        }
    }

    @Override
    public int getCapacity() {
        int capacity = writer.getCapacity();
        int blockSize = cipher.getBlockSize();

        capacity = ((capacity/blockSize) * blockSize) - 1;

        return (capacity > 0) ? capacity : 0;
    }

    public void setPassword(String password) throws GeneralSecurityException{
        if(password == null){
            this.password = DEFAULT_PASSWORD;
        }
        if(password.length() > MAX_PASSWORD_LENGTH){
            throw new GeneralSecurityException("Password is too long");
        } 	
        else{
            this.password = password.toCharArray();
        }
    }

    protected byte[] encrypt(byte[] data) throws GeneralSecurityException { 

        /* Encrypt the message. */ 
        KeySpec spec = new PBEKeySpec(password, SALT, ITERATIONS_COUNT, KEY_LENGTH); 	
        SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), ENCRIPTION_ALGORITHM); 	
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] cipherData = cipher.doFinal(data);

        return cipherData;

    }
 
}