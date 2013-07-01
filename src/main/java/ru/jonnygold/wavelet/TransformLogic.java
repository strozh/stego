/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jonnygold.wavelet;

import ru.jonnygold.wavelet.filters.WaveletFilter;

/**
 *
 * @author Vanchpuck
 */
/*************
 * 
 * ВОЗМОЖНО, ЛУЧШЕ СДЕЛАТЬ ЕГО ИНТЕРФЕЙСОМ
 */
public abstract class TransformLogic {
    
    abstract WaveletData1D getDirectTransform(Signal input, WaveletFilter filter, int direction);
    
    abstract Signal getInverseTransform(WaveletData input, WaveletFilter filter);
    
}
