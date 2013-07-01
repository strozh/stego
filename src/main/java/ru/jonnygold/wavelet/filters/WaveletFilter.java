/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jonnygold.wavelet.filters;

import ru.jonnygold.wavelet.*;

/**
 *
 * @author Vanchpuck
 */
public class WaveletFilter {
    
    public final double[] h;
    public final double[] g;
    public final double[] ih;
    public final double[] ig;
    
    protected WaveletFilter(double[] h, double[] g, double[] ih, double[] ig){
        this.h = h;
        this.g = g;
        this.ih = ih;
        this.ig = ig;
    }
    
}
