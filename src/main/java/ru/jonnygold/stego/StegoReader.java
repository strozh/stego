/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jonnygold.stego;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.jonnygold.wavelet.Signal;
import ru.jonnygold.wavelet.SimpleTransform;
import ru.jonnygold.wavelet.WaveletTransformer;
import ru.jonnygold.wavelet.filters.HaarFilter;

/**
 *
 * @author Vanchpuck
 */
public class StegoReader implements Readeble{

    private static final int STEGO_MARK = 0x7fffffff;

//    private Stego stego;
    private WaveletTransformer transformer;

    private int xPos;
    private int yPos;
    private int sPos;

    private SignalReader sReader;
    
    private StegoCover stegoCover;

    public StegoReader(StegoCover stegoCover){
        this.stegoCover = stegoCover;
        this.transformer = new WaveletTransformer(HaarFilter.getInstance(), SimpleTransform.getInstance());
        this.xPos = 0;
        this.yPos = 0;
        this.sPos = 0;
        sReader = new HaarSignalReader();
    }

    /*
    * (non-Javadoc)
    * @see ru.jonnygold.stego.Readeble#read()
    * 
    * Reads secret data from Stego
    */
    @Override
    public Secret read() throws IOException{
        // Считываем маркер
        if(readInt() == STEGO_MARK){
            try{
                Secret secret = new Secret();

                // Считываем контрольную сумму
                byte[] delivered_MD5 = new byte[16];
                for(int i=0; i<16; i++){
                    delivered_MD5[i] = readByte();
                }

                // Считываем тип содержимого
                secret.setType(readByte());

                // Дополнительная служебная информация
                readByte();
                readByte();
                readByte();

                // Считываем объём прикрепленных данных
                int length = readInt();
                //System.out.println("aLn: "+length);

                byte[] buffer = new byte[length];

                // Считываем прикрепленные данные
                for(int i=0; i<length; i++){
                    buffer[i] = readByte();
                }
                secret.setAttachment(buffer);
                //System.out.println(new String(buffer));

                // Считываем объём записанных данных
                length = readInt();
                //System.out.println("dLn: "+length);

                buffer = new byte[length];

                // Считываем данные
                for(int i=0; i<length; i++){
                    buffer[i] = readByte();
                }

                // Вычисляем контрольную сумму считанных данных
                byte[] computed_MD5 = MessageDigest.getInstance("MD5").digest(buffer);

                // Если контрольные суммы совпадают, возвращаем содержимое, иначе - ничего не возвращаем
                if(Arrays.equals(delivered_MD5, computed_MD5)){
                    secret.setData(buffer);
                    return secret;
                }
                else{
                    System.err.println("Check sum error");
                    return null;
                }
            } 
            catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(StegoReader.class.getName()).log(Level.SEVERE, null, ex);
                throw new IOException("Ошибка вычисления контрольной суммы");
            }
        }
        else{
            //System.out.println("no mark!!!");
            return null;
        }

    }

    private byte readByte() throws IOException{
        ru.jonnygold.stego.Byte aByte = new ru.jonnygold.stego.Byte();
        for(int i=0; i<8; i++){
            int bit = readBit();
            if(bit == -1){
                throw new IOException("Ошибка чтения");
            }
            aByte.setBit(bit, i);
            // System.out.println(aByte.getValue());
        }

        return aByte.getValue();
    }

    private int readBit(){
        //System.out.println("Считывание...");
        int result = -1;

        Signal signal = null;

        // System.out.println("count: "+stego.getSignalCount());
        Stego stego = stegoCover.getStego();

        for(int s=sPos; s<stego.getSignalCount(); s++){

            // System.out.println("read "+s);
            signal = stego.getSignal(sPos);

            for(int y=yPos; y<signal.height; y++){
                for(int x=xPos; x<signal.width-1; x+=2){
                    Signal segment = signal.getSignal(y, x, 1, 2);

                    result = sReader.readSecret(segment);
                    xPos+=2;
                    // System.out.println(result);
                    if(result != -1) return result;
                }
                xPos = 0;
                yPos++;
            }
            xPos = 0;
            yPos = 0;
            sPos++;
            //System.out.println("sPos++ ");
        }

        //System.out.println("return null");
        return result;
    }

    private int readInt() throws IOException{
        byte bt;
        int val = 0;
        // System.out.println("bt: "+val);
        for(int i=0; i<4; i++){

            bt = readByte();
            // System.out.println("bt: "+bt);
            // if(bt == -1) {
            // throw new IOException("Ошибка чтения");
            // }
            val = (val << 8) | (bt & 0x000000ff);
            // System.out.println("vl: "+val);
        }
        // System.out.println("int: "+val);
        return val;
    }
 
}