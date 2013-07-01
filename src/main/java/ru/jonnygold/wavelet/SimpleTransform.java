/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jonnygold.wavelet;

import java.util.Arrays;
import javax.xml.transform.Source;
import ru.jonnygold.wavelet.filters.WaveletFilter;

/**
 *
 * @author Vanchpuck
 */
public class SimpleTransform extends TransformLogic{

    /* *************************************************
     * ПЕРЕДЕЛАТЬ!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * *************************************************/
    private final double nom = Math.sqrt(2);
    
    @Override
    WaveletData1D getDirectTransform(Signal input, WaveletFilter filter, int direction) {
        
        WaveletData1D transform;
        
        switch(direction){
            case TransformDirection.COL_TRANSFORM : transform = getColDirectTransform(input, filter);break;
            default : transform = getRowDirectTransform(input, filter);
        }
        
        return transform;
    }
    
    @Override
    public Signal getInverseTransform(WaveletData input, WaveletFilter filter) {
        
        Signal src;
        
        switch(input.direction){
            case TransformDirection.ROW_TRANSFORM : src = getRowInverseTransform(input, filter); break;
            case TransformDirection.COL_TRANSFORM : src = getColInverseTransform(input, filter); break;
            default : throw new IllegalArgumentException("Illegal transform direction code");
        }
        return src;
        
    }
    
    
    private WaveletData1D getRowDirectTransform(Signal input, WaveletFilter filter){
        
        double[] inputData = input.getData();
        
        int h = input.height;
        int w = input.width>>1;
        
        double[] scaledData = new double[h*w];
        double[] waveletData = new double[h*w];
        Signal scaled = new Signal(scaledData, h, w);
        Signal wavelet = new Signal(waveletData, h, w);
        
        double[] tmpRow;
        
        double[] tmpScaled;
        double[] tmpWavelet;
        
        for(int y=0; y<h; y++){
            tmpRow = Arrays.copyOfRange(inputData, y*input.width, y*input.width+input.width);
            
            tmpScaled = new double[w];
            tmpWavelet = new double[w];
            
            convolutionDirect(tmpScaled, tmpWavelet, tmpRow, filter);
            
            scaled.setSignal(new Signal(tmpScaled, 1, w), y, 0);
            wavelet.setSignal(new Signal(tmpWavelet, 1, w), y, 0);
        }
        
        return new WaveletData1D(scaled, wavelet, input.height, input.width, TransformDirection.ROW_TRANSFORM);
        
    }
    
    private void convolutionDirect(double[] scaled, double[] wavelet, double[] data, WaveletFilter filter){
        int fHalf = filter.h.length>>1;
        
        for (int i=0,j=0; j< data.length-(filter.h.length-1); i++,j+=2) {
            scaled[i] = 0;
            wavelet[i] = 0;
            for(int fIdx=0; fIdx<filter.h.length; fIdx++){
                scaled[i] += data[j+fIdx]*filter.h[fIdx];
                wavelet[i] += data[j+fIdx]*filter.g[fIdx];
            }            
        }
   
        for(int i=filter.h.length-2; i>0; i-=2){
            int turnIdx = scaled.length-(i>>1);
            
            scaled[turnIdx] = 0;
            wavelet[turnIdx] = 0;
            
            for(int fIdx=0; fIdx<i; fIdx++){
                scaled[turnIdx] += data[data.length-i+fIdx]*filter.h[fIdx];
                wavelet[turnIdx] += data[data.length-i+fIdx]*filter.g[fIdx];
            }
            
            for(int fIdx=i,idx=0; fIdx<filter.h.length; fIdx++,idx++){
                scaled[turnIdx] += data[idx]*filter.h[fIdx];
                wavelet[turnIdx] += data[idx]*filter.g[fIdx];
            }
            
//            for(int fIdx1=0, fIdx2=fHalf; fIdx1<fHalf; fIdx1++,fIdx2--){
//                scaled[i] += (data[data.length-fIdx2]*filter.h[fIdx1] + data[fIdx1]*filter.h[fIdx1+fHalf]);
//                wavelet[i] += (data[data.length-fIdx2]*filter.g[fIdx1] + data[fIdx1]*filter.g[fIdx1+fHalf]);
//            }
        }
//        for(int i=fHalf+1; i<filter.h.length; i++){        
//            scaled[scaled.length-i] = 0;
//            wavelet[wavelet.length-i] = 0;
//            for(int fIdx1=0, fIdx2=fHalf; fIdx1<fHalf; fIdx1++,fIdx2--){
//                scaled[scaled.length-i] += (data[data.length-fIdx2]*filter.h[fIdx1] + data[fIdx1]*filter.h[fIdx1+fHalf]);
//                wavelet[wavelet.length-i] += (data[data.length-fIdx2]*filter.g[fIdx1] + data[fIdx1]*filter.g[fIdx1+fHalf]);
//            }
//        }
//        for(int i=0; i<scaled.length; i++){
//            scaled[i] = Math.round(scaled[i]);
//            wavelet[i] = Math.round(wavelet[i]);
//        }
    }
    
    private void convolutionInverse(double[] scaled, double[] wavelet, double[] data, WaveletFilter filter){
        int fHalf = filter.ih.length>>1;
        
        for(int i=0; i<data.length; i++){
            data[i] = 0;
        }
        
        for(int i=0; i<filter.ih.length-2; i+=2){
            //data[i] = 0;
//            System.out.println("i="+i);
            for(int j=0; j<filter.ih.length-(i+2); j++){
                data[i+j] += scaled[i>>1]*filter.ih[j] + wavelet[i>>1]*filter.ig[j];
//                System.out.println("data["+(i+j)+"] += scaled["+(i>>1)+"]*filter.h["+j+"]");
//                data[i+j] += scaled[scaled.length-((i>>1)+1)]*filter.ih[j+i+2] + wavelet[wavelet.length-((i>>1)+1)]*filter.ig[j+i+2];
//                System.out.println("data["+(i+j)+"] += scaled["+(scaled.length-((i>>1)+1))+"]*filter.h["+(j+i+2)+"]");
            }
            for(int j=i+2,idx=0; j<filter.ih.length; j++,idx++){
                data[idx] += scaled[scaled.length-((i>>1)+1)]*filter.ih[j];// + wavelet[i>>1]*filter.ig[j];
//                System.out.println("data["+(idx)+"] += scaled["+(scaled.length-((i>>1)+1))+"]*filter.ih["+j+"]");
            }
        }
        
//        for(int j=0; j<fHalf; j++){
//            data[j] = (scaled[0]*filter.ih[0] + scaled[scaled.length-1]*filter.ih[2] + wavelet[0]*filter.ig[0] + wavelet[wavelet.length-1]*filter.ig[2]);
//        }
        
        for(int i=0,j=filter.h.length-2; i<scaled.length-(fHalf-1); i++,j+=2){
            for(int fIdx=filter.h.length,idx=0; fIdx>0; fIdx-=2,idx++){
                data[j] += scaled[i+idx]*filter.ih[fIdx-2] + wavelet[i+idx]*filter.ig[fIdx-2];
//                System.out.println("data["+j+"] += scaled["+(i+idx)+"]*filter.h["+(fIdx-2)+"]");
                data[j+1] += scaled[i+idx]*filter.ih[fIdx-1] + wavelet[i+idx]*filter.ig[fIdx-1];
//                System.out.println("data["+(j+1)+"] += scaled["+(i+idx)+"]*filter.h["+(fIdx-1)+"]");
            }
        }
        
//        for(int x=0,i=2; x<scaled.length-1; x++,i+=fHalf){
//
//            for(int j=0; j<fHalf; j++){
//                data[i+j] = (scaled[x]*filter.ih[fHalf+j] + scaled[x+1]*filter.ih[j] + wavelet[x]*filter.ig[fHalf+j] + wavelet[x+1]*filter.ig[j]);
//            }
//
//        }
//        for(int i=0; i<data.length; i++){
//            data[i] = Math.round(data[i]);
//        }
    }
    
    private WaveletData1D getColDirectTransform(Signal input, WaveletFilter filter){
        
        double[] data = input.getData();
        
        int width = input.width;
        int height = input.height>>1;
        
        Signal scaled = new Signal(new double[width*height], height, width);
        Signal wavelet = new Signal(new double[width*height], height, width);
        
        double[] dataCol = new double[input.height];
        double[] tmpScaled, tmpWavelet;
        
        for(int x=0; x<width; x++){
            for(int y=0; y<input.height; y++){
                dataCol[y] = data[y*width+x];
            }
            
            tmpScaled = new double[height];
            tmpWavelet = new double[height];
            
            convolutionDirect(tmpScaled, tmpWavelet, dataCol, filter);

            scaled.setSignal(new Signal(tmpScaled, height, 1), 0, x);
            wavelet.setSignal(new Signal(tmpWavelet, height, 1), 0, x);
        }
        
        return new WaveletData1D(scaled, wavelet, input.height, input.width, TransformDirection.COL_TRANSFORM);
    }
    
    private Signal getRowInverseTransform(WaveletData input, WaveletFilter filter){
        
        Signal scaled = input.getScaled();
        Signal wavelet = input.getWavelet();
        
        int width = input.getScaled().width;
        int height = input.getScaled().height;
        
        double[] srcData = new double[input.height*input.width];
        Signal src = new Signal(srcData, input.height, input.width);
        
        double[] tmpScaled, tmpWavelet;
        
        int fHalf = filter.h.length>>1;
        
        double[] tmp;
        
        for(int y=0; y<height; y++){
            tmpScaled = scaled.getSignal(y, 0, 1, width).getData();
            tmpWavelet = wavelet.getSignal(y, 0, 1, width).getData();
            
            tmp = new double[input.width];
            
            convolutionInverse(tmpScaled, tmpWavelet, tmp, filter);
            
            src.setSignal(new Signal(tmp, 1, input.width), y, 0);                
        }
        
        return src;
    }
    
    private Signal getColInverseTransform(WaveletData input, WaveletFilter filter){
        
        Signal scaled = input.getScaled();
        Signal wavelet = input.getWavelet();
        
        int width = scaled.width;
        int height = scaled.height;
        
        Signal src = new Signal(new double[input.height*input.width], input.height, input.width);
        
        double[] tmpScaled, tmpWavelet, tmp;
        
        for(int x=0; x<width; x++){
            tmpScaled = scaled.getSignal(0, x, height, 1).getData();
            tmpWavelet = wavelet.getSignal(0, x, height, 1).getData();
            
            tmp = new double[input.height];
            
            convolutionInverse(tmpScaled, tmpWavelet, tmp, filter);
                        
            src.setSignal(new Signal(tmp, input.height, 1), 0, x);
        }
        
        return src;
    }
    
    public static SimpleTransform getInstance(){
        return new SimpleTransform();
    }

    
    
}
