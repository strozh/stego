/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jonnygold.wavelet;

import java.util.Arrays;

/**
 *
 * @author Vanchpuck
 */
public class WaveletData1D extends WaveletData{

    private Signal scaled;
    private Signal wavelet;
    
    protected WaveletData1D(Signal scaled, Signal wavelet, int h, int w, int direction){
        super(h, w, direction);
        this.scaled = scaled;
        this.wavelet = wavelet;
    }
        
    @Override
    public Signal getScaled() {
        return this.scaled;
    }

    @Override
    public Signal getWavelet() {
        return this.wavelet;
    }

    @Override
    protected void setScaled(Signal scaled) throws Exception {
        if(scaled.height != this.scaled.height || scaled.width != this.scaled.width){
            throw new Exception("Не совпадают размеры области");
        }
        
        this.scaled = scaled;
    }

    @Override
    protected void setWavelet(Signal wavelet) throws Exception {
        if(wavelet.height != this.wavelet.height || wavelet.width != this.wavelet.width){
            throw new Exception("Не совпадают размеры области");
        }
        
        this.wavelet = wavelet;
    }
    
    @Override
    public double[] getData(){
        double[] data = new double[super.height*super.width];
        
        if(this.direction == TransformDirection.COL_TRANSFORM){
            double[] tmp = scaled.getData();
            System.arraycopy(tmp, 0, data, 0, tmp.length);
            
            tmp = wavelet.getData();
            System.arraycopy(tmp, 0, data, data.length>>1, tmp.length);
        }
        else if(this.direction == TransformDirection.ROW_TRANSFORM){
            int h = this.scaled.height;
            int w = this.scaled.width;
            
            double[] scaledData = this.scaled.getData();
            double[] waveletData = this.wavelet.getData();
            
            double[] tmp;
            
            for(int y=0; y<this.height; y++){
                
                tmp = Arrays.copyOfRange(scaledData, y*w, y*w+w);
                System.arraycopy(tmp, 0, data, y*this.width, w);
                
                tmp = Arrays.copyOfRange(waveletData, y*w, y*w+w);
                System.arraycopy(tmp, 0, data, y*this.width+w, w);
            }
        }
        else{
            //#######################
            //ИСПРАВИТЬ EXCEPTION!!!!
            //#######################
            throw new IndexOutOfBoundsException("Неизвестный индикатор направления преобразования");
        }
        return data;
    }
    
}
