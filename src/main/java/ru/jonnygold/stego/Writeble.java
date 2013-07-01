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
public interface Writeble {

    /*
    * Writes secret data in Stego
    */
    public void write(Secret data) throws IOException;

    /*
    * Returns stego's capacity in bytes
    */
    public int getCapacity();
 
}