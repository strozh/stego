/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jonnygold.stego;

/**
 *
 * @author Vanchpuck
 */
class Integer {
 
    private final static int FILTER = 1;

    private final static int BIT_COUNT = 32;

    private final static int BYTE_COUNT = 4;

    private int value;
    private int idx = 0;

    public Integer(){
        this.value = 0;
        this.idx = 0;
    }

    public Integer(int val){
        this.value = val;
        this.idx = 0;
    }

    public int getNextBit(){
        if(idx == BIT_COUNT){
            idx = 0;
            return -1;
        }
        int bit = (FILTER & (value >> idx));
        idx++;

        return bit;
    }

    public int getBit(int position){
        if(position<0 || position>BIT_COUNT-1){
            throw new IllegalArgumentException("Недопустимая позиция бита");
        }
        return (FILTER & (value >> position));
    }

    public void setBit(int bit, int position){
        if(position<0 || position>BIT_COUNT-1){
            throw new IllegalArgumentException("Недопустимая позиция бита");
        }
        switch(bit){
            case 1 : value = value | (1 << position); break;
            case 0 : value = value & ~(1 << position); break;
            default : throw new IllegalArgumentException("Value out of range. Value: "+bit);
        }

    }

    public ru.jonnygold.stego.Byte getByte(int position){

        if(position<0 || position>BYTE_COUNT-1){
            throw new IllegalArgumentException("Недопустимая позиция байта");
        }
        return new ru.jonnygold.stego.Byte((byte) ((value >> position*8) & 0x000000ff));

    }

    // public byte[] getBytes(){
    // 
    // byte[] bytes = new byte[4];
    // System.out.println("!!!!!");
    // for(int i=3; i>=0; i--){
    // System.out.println("Step");
    // bytes[3-i] = this.getByte(i).getValue();
    // }
    // return bytes;
    // 
    //// if(position<0 || position>BYTE_COUNT-1){
    //// throw new IllegalArgumentException("Недопустимая позиция байта");
    //// }
    //// return new ru.jonnygold.stego.Byte((byte) ((value >> position*8) & 0x000000ff));
    // 
    // }

    public int getValue(){
        return this.value;
    }
 
}