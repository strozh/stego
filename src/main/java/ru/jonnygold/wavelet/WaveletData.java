/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jonnygold.wavelet;

/**
 *
 * @author Vanchpuck
 */
public abstract class WaveletData <T extends Signal> extends Signal{
        
    public final int direction;
    
    protected WaveletData(int h, int w, int direction){
        super(h, w);
        this.direction = direction;
    }
    
    abstract T getScaled();
    
    abstract T getWavelet();
    
    abstract void setScaled(T scaled) throws Exception;
    
    abstract void setWavelet(T wavelet) throws Exception;
}
