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
class HaarSignalReader implements SignalReader{
 
    private RateFilter rFltr;

    public HaarSignalReader(){
        rFltr = new RateFilter(new WaveletTransformer(HaarFilter.getInstance(), SimpleTransform.getInstance()), 0);
    }

    public HaarSignalReader(TransformLogic alg){
        rFltr = new RateFilter(new WaveletTransformer(HaarFilter.getInstance(), alg), 0);
    }

    @Override
    public int readSecret(Signal signal) {
        int result = -1;

        // System.out.print(signal.getData()[0]+" "+signal.getData()[1]+" ");
        // if(signal.getData()[0] < 2 && signal.getData()[1] < 2) return result;
        // if(signal.getData()[1] < 3) return result;

        WaveletData1D wData = rFltr.getDirectTransform(signal, TransformDirection.ROW_TRANSFORM);

        // System.out.println(wData.getData()[0]+" "+wData.getData()[1]);

        int rate = (int) Math.round(wData.getWavelet().getData()[0]);
        // System.out.println("Прочитано: "+rate);
        if(rate==0 || rate==1){
            // System.out.println("Прочитано: "+rate);
            result = rate;
        }

    return result;
    }
 
}