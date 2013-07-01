/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jonnygold.wavelet;

/**
 *
 * @author Vanchpuck
 */
public abstract class Transformer {
    
    abstract WaveletData1D getDirectTransform(Signal input, int direction);
    
    abstract WaveletData2D getDirectTransform2D(Signal input);
    
    abstract Signal getInverseTransform(WaveletData1D input);
    
    abstract Signal getInverseTransform2D(WaveletData2D input);
    
}
