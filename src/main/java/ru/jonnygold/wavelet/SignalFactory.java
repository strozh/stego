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
public class SignalFactory {
    
    public static Signal createSignal(int[] input){
        
        double[] data = new double[input.length];
        
        for(int i=0; i<input.length; i++){
            data[i] = (double)input[i];
        }
        
        return new Signal(data, 1, data.length);
    }
    
    public static Signal createSignal(double[] input){                
        return new Signal(input, 1, input.length);
    }
    
    public static Signal createSignal2D(int[] input, int h, int w){
        
        /*******
         * EXCEPTION ИСПАРВИТЬ
         */
        if(input.length != h*w){
            throw new IndexOutOfBoundsException("Размерность блока не соответствует длине массива данных.");
        }
        
        double[] data = new double[input.length];
        
        for(int i=0; i<input.length; i++){
            data[i] = (double)input[i];
        }
        
        return new Signal(data, h, w);
    }
    
    public static Signal createSignal2D(double[] input, int h, int w){    
        /*******
         * EXCEPTION ИСПАРВИТЬ
         */
        if(input.length != h*w){
            throw new IndexOutOfBoundsException("Размерность блока не соответствует длине массива данных.");
        }
        return new Signal(input, h, w);
    }
    
}
