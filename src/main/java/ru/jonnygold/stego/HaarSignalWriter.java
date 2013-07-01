/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jonnygold.stego;

import ru.jonnygold.wavelet.*;
import ru.jonnygold.wavelet.filters.HaarFilter;

/**
 *
 * @author Vanchpuck
 */
class HaarSignalWriter implements SignalWriter{

    public static final int ERR_VAL = 4;
    public static final int FLTR_VAL = 3;

    RateFilter rFltr;

    public HaarSignalWriter(){
        rFltr = new RateFilter(new WaveletTransformer(HaarFilter.getInstance(), SimpleTransform.getInstance()), FLTR_VAL);
    }

    public HaarSignalWriter(TransformLogic alg){
        rFltr = new RateFilter(new WaveletTransformer(HaarFilter.getInstance(), alg), FLTR_VAL);
    }

    @Override
    public boolean writeSecret(Signal signal, int secret) {

        WaveletData1D wData = rFltr.getDirectTransform(signal, TransformDirection.ROW_TRANSFORM);

        if(wData.getWavelet().getData()[0]==0){

            wData.getWavelet().getData()[0] = secret;
            signal.setSignal(rFltr.getInverseTransform(wData), 0, 0);
            return true;

        } 
        return false;
    }

    // @Override
    public boolean availabilityCheck(Signal segment) {
        Signal segment1 = segment;
        boolean tmp = writeSecret(segment1, (byte)1);
        if(!tmp) {
            return false;
        }
        tmp = writeSecret(segment1, (byte)0);
        if(!tmp) {
            return false;
        }
        return true;
    }
 
}