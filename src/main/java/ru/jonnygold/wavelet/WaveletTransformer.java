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
public class WaveletTransformer extends Transformer{
    
    private WaveletFilter filter;
    private TransformLogic alg;
    
    public WaveletTransformer(WaveletFilter filter, TransformLogic alg){
        this.alg = alg;
        this.filter = filter;
    }
    
    public WaveletFilter getFilter(){
        return this.filter;
    }
    
    public TransformLogic getTransformLogic(){
        return this.alg;
    }
    
    public void setFilter(WaveletFilter filter){
        this.filter = filter;
    }
    
    public void setFilter(TransformLogic alg){
        this.alg = alg;
    }
    
    @Override
    public WaveletData1D getDirectTransform(Signal input, int direction) {
        return alg.getDirectTransform(input, filter, direction);
    }

    @Override
    public WaveletData2D getDirectTransform2D(Signal input) {
        WaveletData1D rowTransform = alg.getDirectTransform(input, filter, TransformDirection.ROW_TRANSFORM);
        
        WaveletData1D scaled = alg.getDirectTransform(rowTransform.getScaled(), filter, TransformDirection.COL_TRANSFORM);
        
        WaveletData1D wavelet = alg.getDirectTransform(rowTransform.getWavelet(), filter, TransformDirection.COL_TRANSFORM);
        
        return new WaveletData2D(scaled, wavelet, input.height, input.width);
    }

    @Override
    public Signal getInverseTransform(WaveletData1D input) {
        return alg.getInverseTransform(input, filter);
    }

    @Override
    public Signal getInverseTransform2D(WaveletData2D input) {
        
        Signal scaled = alg.getInverseTransform(input.getScaled(), filter);
        Signal wavelet = alg.getInverseTransform(input.getWavelet(), filter);
        
        WaveletData1D rowTransform = new WaveletData1D(scaled, wavelet, input.height, input.width, TransformDirection.ROW_TRANSFORM);
        
        Signal src = alg.getInverseTransform(rowTransform, filter);
        
        return src;
    }
    
}
