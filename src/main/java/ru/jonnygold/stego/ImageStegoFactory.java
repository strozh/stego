/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jonnygold.stego;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import ru.jonnygold.wavelet.Signal;
import ru.jonnygold.wavelet.SignalFactory;
//import sun.security.jca.GetInstance;

/**
 *
 * @author Vanchpuck
 */
class ImageStegoFactory implements StegoFactory{

    private static ImageStegoFactory factory;

    private ImageStegoFactory(){}

    public static ImageStegoFactory getInstance(){
    if(factory == null){
        return new ImageStegoFactory();
    }
    return factory;
    }

    @Override
    public Stego createStego(File file) throws IOException {

    // BufferedImage bi = ImageIO.read(file);
    // List<Signal> sl = new ArrayList<Signal>();
    // sl.addAll(getSignalList(bi));

    // Initialize List to hold amount of Signals
    List<Signal> signalList = new ArrayList<Signal>();

    // Prepare reader
    ImageReader reader = ImageIO.getImageReadersByFormatName(getFileExt(file)).next(); 
    ImageInputStream inStream = ImageIO.createImageInputStream(file); 
    reader.setInput(inStream);

    // Read all images from file
    BufferedImage image = null;
    try{
        /*** НА ДАННОМ ЭТАПЕ ПИШЕМ ТОЛЬКО В ПЕРВОЕ ИЗОБРАЖЕНИЕ ***/
        for(int i=0; i<1; i++){
            image = reader.read(i, null);
            //System.out.println("Get image from file");

            // Get signal from image and add it into the List
            signalList.addAll(getSignalList(image));
        }
    }
    catch(IndexOutOfBoundsException ex){}

    inStream.flush();
    inStream.close();
    reader.dispose();

    // Create and return stego
    return new Stego(signalList);
        // return new Stego(sl);
    }

    public void putStego(File file, Stego stego) throws IOException{

        // Image container retrieving
        BufferedImage image = ImageIO.read(file);

        // ///////***///////
        // 
        // putSignal(image, stego.getSignalList());
        // 
        // int w = image.getWidth();
        // int h = image.getHeight();
        // System.out.println("w: "+w);
        // System.out.println("h: "+h);
        // 
        // int[] d = new int[w*h];
        // image.getData().getSamples(0, 0, w, h, 0, d);
        //// for(int i : d){
        //// System.out.print(i+" ");
        //// }
        // System.out.println();
        // 
        // ImageIO.write(image, getFileExt(file), file);
        // 
        // 
        // image = ImageIO.read(file);
        // int[] dd = new int[image.getWidth()*image.getHeight()];
        // image.getData().getSamples(0, 0, w, h, 0, dd);
        //// for(int i : dd){
        //// System.out.print(i+" ");
        //// }
        // System.out.println();
        // 
        // System.out.println("----");
        // for(int i=0; i<d.length; i++){
        // if(d[i] != dd[i]){
        // System.out.println("i = "+i+" / "+d[i]+" != "+dd[i]);
        // }
        // }
        // System.out.println("----");
        // 
        // 
        // ///////***///////

        // ImageWriter initialising
        ImageWriter writer = ImageIO.getImageWritersByFormatName(getFileExt(file)).next();
        ImageOutputStream outStream = ImageIO.createImageOutputStream(file);
        writer.setOutput(outStream);

        putSignal(image, stego.getSignalList());

        writer.write(image);

        outStream.flush();
        outStream.close();
        writer.dispose();

    }

    private List<Signal> getSignalList(BufferedImage image){
        /***
        * ДОБАВИТЬ ПРОВЕРКУ ТИПА ИЗОБРАЖЕНИЯ!!!
        */
        // Initialize List for Signals storing
        List<Signal> signalList = new ArrayList<Signal>();

        int width = image.getWidth();
        int height = image.getHeight();

        Raster raster = image.getRaster();


        // Get signal from color band component and add int into List of Signals
        double[] data = new double[width * height]; 
        for(int i=0; i<raster.getNumBands(); i++){
            //System.out.println(" Get color band "+i);
            data = new double[width * height];
            raster.getSamples(0, 0, width, height, i, data);

            signalList.add(SignalFactory.createSignal2D(data, height, width));
        }

        return signalList;
    }

    private void putSignal(BufferedImage image, List<Signal> signalList){

        WritableRaster raster = image.getRaster();

        if(raster.getNumBands() != signalList.size()){
            throw new IllegalStateException("Неподходящее изображение");
        }

        int width = image.getWidth();
        int height = image.getHeight();

        //System.out.println("signalList.size(): "+signalList.size());
        double[] data;
        for(int i=0; i<signalList.size(); i++){
            /*** ДОБАВИТЬ ПРОВЕРКУ СООТВЕТСТВИЯ РАЗМЕРОВ ***/
            data = signalList.get(i).getData();
            normalize(data);
            raster.setSamples(0, 0, width, height, i, data);
        }


    // return null;
    }

    private void normalize(double[] data){
        for(int i=0; i<data.length; i++){
            if(data[i] < 0) data[i] = 0;//Math.abs(data[i]);
            if(data[i] > 255) data[i] = 255;
        }
    }

    private String getFileExt(File file){
        String fileName = file.getName();
        return fileName.substring(fileName.lastIndexOf(".")+1, fileName.length());
    }
 
}
