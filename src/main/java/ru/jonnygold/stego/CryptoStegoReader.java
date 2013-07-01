package ru.jonnygold.stego;

import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoStegoReader implements Readeble{


    protected static final String KEY_FACTORY = "PBKDF2WithHmacSHA1";

    protected static final String ENCRIPTION_ALGORITHM = "AES";

    protected static final char[] DEFAULT_PASSWORD = "abcdefgh".toCharArray();

    protected static final byte[] SALT = "megasalt".getBytes();

    protected static final int ITERATIONS_COUNT = 65536;

    protected static final int KEY_LENGTH = 128;

    protected static final int MAX_PASSWORD_LENGTH = 64;



    protected Readeble reader;

    protected char[] password;

    protected SecretKeyFactory factory;

    protected Cipher cipher;



    public CryptoStegoReader(Readeble reader) throws GeneralSecurityException{

        this.reader = reader;

        this.password = DEFAULT_PASSWORD;

        factory = SecretKeyFactory.getInstance(KEY_FACTORY);

        cipher = Cipher.getInstance(ENCRIPTION_ALGORITHM);
    }

    @Override
    public Secret read() throws IOException {
        Secret data = reader.read();

        try {
            data.setAttachment(decrypt(data.getAttachment()));
            //System.out.println("Read att: "+data.getAttachment());

            data.setData(decrypt(data.getData()));
            //System.out.println("Read data: "+data.getData());
        } 
        catch (IncorrectPasswordException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            throw new IOException("Incorrect password");
        } 
        catch (GeneralSecurityException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
//			throw new IOException("Decryption error");
        }

        return data;
    }

    protected byte[] decrypt(byte[] data) throws IncorrectPasswordException, GeneralSecurityException{

        KeySpec spec = new PBEKeySpec(password, SALT, ITERATIONS_COUNT, KEY_LENGTH); 	
        SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), ENCRIPTION_ALGORITHM);

        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] decrypted = null;
        try {
            decrypted = cipher.doFinal(data);
        } 
        catch (GeneralSecurityException e) {
            throw new IncorrectPasswordException(e.getMessage());
        } 

        return decrypted;
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

}