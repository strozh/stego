/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jonnygold.stego;

import java.io.IOException;
import ru.jonnygold.wavelet.Signal;

/**
 *
 * @author Vanchpuck
 */
interface SignalReader {
 
    public int readSecret(Signal signal) ;
 
}