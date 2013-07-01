/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jonnygold.stego;

import ru.jonnygold.wavelet.Signal;

/**
 *
 * @author Vanchpuck
 */
interface SignalWriter {
 
    public boolean writeSecret(Signal signal, int secret);

    // public boolean checkSecret(WaveletData1D wData, byte secret);

    public boolean availabilityCheck(Signal segment);
 
}