/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jonnygold.wavelet;

/**
 *
 * @author Vanchpuck
 */
public class RateFilter extends Transformer{
    
    private int border;
    
    private Transformer transformer;
    
    public RateFilter(WaveletTransformer transformer, int border){
        this.border = border;
        this.transformer = transformer;
    }

    @Override
    public WaveletData1D getDirectTransform(Signal input, int direction) {
        WaveletData1D transformed = transformer.getDirectTransform(input, direction);
        
        Signal wavelet = transformed.getWavelet();
        
        double[] data = wavelet.getData();
        
        filter(data, this.border);
        
        return transformed;
    }

    @Override
    public WaveletData2D getDirectTransform2D(Signal input) {
        WaveletData2D transformed = transformer.getDirectTransform2D(input);
        
        double[] data = transformed.getScaled().getWavelet().getData();
        filter(data, this.border);
        
        data = transformed.getWavelet().getScaled().getData();
        filter(data, this.border);
        
        data = transformed.getWavelet().getWavelet().getData();
        filter(data, this.border);
        
        return transformed;
    }
    
    private void filter(double[] data, int border){
        for(int i=0; i<data.length; i++){
            if(Math.abs(data[i]) < this.border){
                data[i] = 0;
            }
        }
    }

    @Override
    public Signal getInverseTransform(WaveletData1D input) {
        return transformer.getInverseTransform(input);
    }

    @Override
    public Signal getInverseTransform2D(WaveletData2D input) {
        return transformer.getInverseTransform2D(input);
    }
    
    public void setBorder(int border){
        this.border = border;
    }
    
}
