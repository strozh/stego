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
public class WaveletData2D extends WaveletData<WaveletData1D> {

    private WaveletData1D scaled;
    private WaveletData1D wavelet;
    
    protected WaveletData2D(WaveletData1D scaled, WaveletData1D wavelet, int h, int w){
        super(h, w, TransformDirection.ROW_TRANSFORM);
        this.scaled = scaled;
        this.wavelet = wavelet;
    }
    
    @Override
    public WaveletData1D getScaled() {
        return this.scaled;
    }

    @Override
    public WaveletData1D getWavelet() {
        return this.wavelet;
    }

    @Override
    void setScaled(WaveletData1D scaled) throws Exception {
        this.scaled = scaled;
    }

    @Override
    void setWavelet(WaveletData1D wavelet) throws Exception {
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
