/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package ru.jonnygold.stego;

/**
*
* @author Vanchpuck
*/
class Byte {

    private byte value;
    private int idx = 0;
    private byte fltr = java.lang.Byte.parseByte("00000001", 2);

    protected Byte(){
        this.value = 0;
        this.idx = 0;
        this.fltr = java.lang.Byte.parseByte("00000001", 2);
    }

    protected Byte(byte b){
        this.value = b;
        this.idx = 0;
        this.fltr = java.lang.Byte.parseByte("00000001", 2);
    }

    public int getNextBit(){
        if(idx == 8){
            idx = 0;
            return -1;
        }
        int bit = (fltr & (value >> idx));
        idx++;

        return bit;
    }

    public int getBit(int position){
        if(position<0 || position>7){
            throw new IllegalArgumentException("Недопустимая позиция бита");
        }
        return (fltr & (value >> position));
    }

    public void setBit(int bit, int position){
        if(position<0 || position>7){
            throw new IllegalArgumentException("Недопустимая позиция бита");
        }
        switch(bit){
            case 1 : value = (byte) (value | (1 << position)); break;
            case 0 : value = (byte) (value & ~(1 << position)); break;
            default : throw new IllegalArgumentException("Value out of range. Value: "+bit);
        }

    }

    public byte getValue(){
        return this.value;
    }

}
