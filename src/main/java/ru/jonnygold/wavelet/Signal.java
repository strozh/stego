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
public class Signal {
    
    public final int height;
    public final int width;
    public final int length;
    
    private double[] data;
    
    protected Signal(double[] input, int h, int w){
        this.data = input;
        this.height = h;
        this.width = w;
        this.length = h*w;
    }
    
    protected Signal(int h, int w){
        this.height = h;
        this.width = w;
        this.length = h*w;
    }
    
    public Signal getSignal(int y, int x, int h, int w){
        
        double[] buffer = new double[h*w];
        
        double[] rowData = new double[w];
        
        for(int oy=y,i=0; i<h; oy++,i++){
            rowData = Arrays.copyOfRange(this.data, oy*this.width+x, oy*this.width+x+w);
            
            System.arraycopy(rowData, 0, buffer, i*w, w);
        }
        
        return new Signal(buffer, h, w);
    }
    
    public void setSignal(Signal input, int y, int x){
        
        double[] inData = input.getData();
        int h = input.height;
        int w = input.width;
        
        double[] rowData = new double[w];
        
        for(int oy=y,i=0; i<h; oy++,i++){
            
            rowData = Arrays.copyOfRange(inData, i*w, i*w+w);
            
            System.arraycopy(rowData, 0, this.data, oy*this.width+x, w);
        }
        
    }
    
    public double[] getData(){
        return this.data;
    }
    
}
