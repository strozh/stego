/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jonnygold.stego;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Vanchpuck
 */
interface StegoFactory {
 
    public Stego createStego(File file) throws IOException;

    public void putStego(File file, Stego stego) throws IOException;
 
}