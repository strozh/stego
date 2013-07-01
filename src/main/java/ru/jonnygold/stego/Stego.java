/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jonnygold.stego;

import java.io.File;
import java.util.List;
import ru.jonnygold.wavelet.Signal;
import ru.jonnygold.wavelet.WaveletData1D;
import ru.jonnygold.wavelet.filters.WaveletFilter;

/**
 *
 * @author Vanchpuck
 */
class Stego {
 
    private File file;

    private List<Signal> signalList;

    private int capacity;

    // protected Stego(WaveletData1D wData, WaveletFilter filter){
    // this.wData = wData;
    // this.filter = filter;
    // }

    // protected Stego(Signal signal){
    // this.signal = signal;
    // }

    protected Stego(List<Signal> signalList){
        this.signalList = signalList;
    }

    public Signal getSignal(int signalIdx){
        return this.signalList.get(signalIdx);
    }

    public List<Signal> getSignalList(){
        return this.signalList;
    }

    public void addSignal(Signal signal){
        this.signalList.add(signal);
    }

    public int getSignalCount(){
        return this.signalList.size();
    }

    // public void setSignal(int signalIdx, Signal signal, int x, int y){
    // this.signalList.get(signalIdx).setSignal(signal, y, x);
    //// this.signal.setSignal(signal, y, x);
    // }

    // protected Signal getContainer(){
    // return this.wData.getWavelet();
    // }
 
 
}