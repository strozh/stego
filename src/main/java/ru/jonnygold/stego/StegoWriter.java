/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jonnygold.stego;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.jonnygold.wavelet.Signal;

/**
 *
 * @author Vanchpuck
 */
public class StegoWriter implements Writeble{

    protected static final int STEGO_MARK = 0x7fffffff;

    protected static final int HEADER_LENGTH = 28;

    protected static final int MAX_ATTACHMENT_LEN = 512;


//    private Stego stego;

    private int xIdx;
    private int yIdx;
    private int sIdx;

    private SignalWriter sWriter;
    
    protected StegoCover stegoCover;

    public StegoWriter(StegoCover stegoCover){
        this.stegoCover = stegoCover;

        sWriter = new HaarSignalWriter();

        xIdx = 0;
        yIdx = 0;
        sIdx = 0;
    }

    /*
    * (non-Javadoc)
    * @see ru.jonnygold.stego.Writeble#write(ru.jonnygold.stego.Secret)
    * 
    * Writes secret data in Stego without any additional processing (encryption, compression, etc.)
    */
    @Override
    public void write(Secret data) throws IOException{

        if(data.getData().length > getCapacity()){
            throw new IOException("Not enough free space");
        }

        if(data.getAttachment().length > MAX_ATTACHMENT_LEN/8){
            throw new IOException("Attachment is too long");
        }

        writeInt(STEGO_MARK);

        writeData(getMD5(data.getData()));

        writeByte((byte)data.getType());
        writeByte((byte)data.getType());
        writeByte((byte)data.getType());
        writeByte((byte)data.getType());

        //System.out.println("aLn: "+data.getAttachment().length);
        writeInt(data.getAttachment().length);
        writeData(data.getAttachment());

        //System.out.println("dLn: "+data.getData().length);
        writeInt(data.getData().length);
        writeData(data.getData());
        
        // Выгружаем данные из памяти в контейнер
        stegoCover.putIn();

    }

    /*
    * (non-Javadoc)
    * @see ru.jonnygold.stego.Writeble#getCapacity()
    * 
    * Returns Stego's capacity in bytes
    */
    public int getCapacity() {
        Signal signal = null;

        Stego stego = stegoCover.getStego();
        
        int cpct = 0;
        for(int s=0; s<stego.getSignalCount(); s++){

            signal = stego.getSignal(sIdx);

            for(int y=yIdx; y<signal.height; y++){
                for(int x=xIdx; x<signal.width; x+=2){
                    Signal segment = signal.getSignal(y, x, 1, 2);
                    if(sWriter.availabilityCheck(segment)){
                    cpct++;
                    }
                }
            }
            //System.out.println("s++");
        }

        cpct = (cpct-(HEADER_LENGTH*8)-MAX_ATTACHMENT_LEN/*-(cpct%8)*/)/8;

        return (cpct > 0) ? cpct : 0;
    }


    /*
    * СЛУЖЕБНЫЕ ФУНКЦИИ
    */


    private void writeData(byte[] data) throws IOException{

        for(byte b : data){
            writeByte(b);
        }

    }

    private void writeByte(byte b) throws IOException{
        ru.jonnygold.stego.Byte aByte = new ru.jonnygold.stego.Byte(b);

        int bit = 0;
        while(bit != -1){
            bit = aByte.getNextBit();

            if(bit != -1) writeBit(bit);
        }
    }

    private void writeBit(int value) throws IOException {

        Signal signal = null;
        // System.out.println("c: "+stego.getSignalCount());

        Stego stego = stegoCover.getStego();
        
        for(int s=sIdx; s<stego.getSignalCount(); s++){

        // System.out.println("sIdx: "+sIdx);
        signal = stego.getSignal(sIdx);

        for(int y=yIdx; y<signal.height; y++){
            for(int x=xIdx; x<signal.width-(signal.width%2); x+=2){
                Signal segment = signal.getSignal(y, x, 1, 2);
                // System.out.println(segment.getData()[0]+" "+segment.getData()[1]);

                boolean b = sWriter.writeSecret(segment, value);
                signal.setSignal(segment, y, x);

                xIdx+=2;
                if(b) {
                    // System.out.println(segment.getData()[0]+" "+segment.getData()[1]);
                    // signal.setSignal(segment, y, x);
                    return;
                }
            }
            xIdx = 0;
            yIdx++;
        }
        xIdx = 0;
        yIdx = 0;
        sIdx++;
        //System.out.println("sIdx++ ");
        }

        throw new IOException("Ошибка записи данных");
    }


    private void writeInt(int intVal) throws IOException{

        ru.jonnygold.stego.Integer value = new ru.jonnygold.stego.Integer(intVal);

        // write(value.getBytes());

        byte[] bytes = new byte[4];

        for(int i=0; i<4; i++){
            bytes[i] = value.getByte(i).getValue();
        }

        // write(bytes);

        for(int i=3; i>=0; i--){
            writeByte(bytes[i]);
        }
    }

    private byte[] getMD5(byte[] data) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } 
        catch (NoSuchAlgorithmException ex) {
            System.err.println("Ошибка при расчёте контрольной суммы");
            Logger.getLogger(StegoWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return md.digest(data);
    }
 
}