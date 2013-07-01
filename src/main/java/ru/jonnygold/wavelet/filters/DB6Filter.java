/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jonnygold.wavelet.filters;

/**
 *
 * @author Vanchpuck
 */
public class DB6Filter extends WaveletFilter{
    
    private DB6Filter(double[] h, double[] g, double[] ih, double[] ig){
        super(h, g, ih, ig);
    }
    
    public static DB6Filter getInstance(){
        double[] h = new double[]{0.23523360389270459000, 0.57055845791730841000, 0.32518250026371026000, -0.09546720778426010000, -0.06041610415535419300, 0.02490874986589095700};
        double[] g = new double[]{0.02490874986589095700, 0.06041610415535419300, -0.09546720778426010000, -0.32518250026371026000, 0.57055845791730841000, -0.23523360389270459000};
        
//        System.out.println("-------");
//        System.out.println(h[0]*2);
//        System.out.println(h[1]*2);
//        System.out.println(h[2]*2);
//        System.out.println(h[3]*2);
//        System.out.println(h[4]*2);
//        System.out.println(h[5]*2);
//        System.out.println();
//        System.out.println(g[0]*2);
//        System.out.println(g[1]*2);
//        System.out.println(g[2]*2);
//        System.out.println(g[3]*2);
//        System.out.println(g[4]*2);
//        System.out.println(g[5]*2);
        
        double[] ih = new double[]{0.4704672077854092, 1.1411169158346168, 0.6503650005274205, -0.1909344155685202, -0.12083220831070839, 0.049817499731781914};
        double[] ig = new double[]{0.049817499731781914, 0.12083220831070839, -0.1909344155685202, -0.6503650005274205, 1.1411169158346168, -0.4704672077854092};
        
        return new DB6Filter(h, g, ih, ig);
    }
    
}
