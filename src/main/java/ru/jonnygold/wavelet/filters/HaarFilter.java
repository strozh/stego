/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jonnygold.wavelet.filters;

/**
 *
 * @author Vanchpuck
 */
public class HaarFilter extends WaveletFilter{
    
    private HaarFilter(double[] h, double[] g, double[] ih, double[] ig){
        super(h, g, ih, ig);
    }
    
    public static HaarFilter getInstance(){
//        double[] h = new double[]{0.4829629131445341, 0.8365163037378097, 0.2241438680420134, -0.1294095225512604};
//        double[] g = new double[]{-0.1294095225512604, -0.2241438680420134, 0.8365163037378097, -0.4829629131445341};
        double[] h = new double[]{0.5,0.5};
        double[] g = new double[]{0.5,-0.5};
        
        double[] ih = new double[]{1,1};
        double[] ig = new double[]{1,-1};
        
        return new HaarFilter(h, g, ih, ig);
    }
    
}