/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jonnygold.stego;

import java.io.IOException;

/**
 *
 * @author Vanchpuck
 */
public abstract class StegoCover {
    
    /**
     * Gets Stego from container
     * @return 
     */
    protected abstract Stego getStego();
    
    /**
     * Puts steganograms into cover
     * @throws IOException 
     */
    public abstract void putIn() throws IOException;
    
}
