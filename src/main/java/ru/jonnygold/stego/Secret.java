/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jonnygold.stego;

/**
 *
 * @author Vanchpuck
 */
public class Secret {
 
    public static class Type{

        public static final int TEXT = 0;

        public static final int FILE = 1;

        public static final int UNKNOWN = 2;

    }

    private byte[] data;

    private byte[] attachment;

    private int type;

    public Secret(){
        data = null;

        attachment = null;

        type = Type.UNKNOWN;
    }

    public Secret(int type, byte[] attachment, byte[] data){
        if(type != Type.FILE && type != Type.TEXT && type != Type.UNKNOWN){
            throw new IllegalArgumentException("Неверно задан тип данных");
        }

        this.data = data;

        this.attachment = attachment;

        this.type = type;
    }

    public byte[] getAttachment(){
        return this.attachment;
    }

    public byte[] getData(){
        return this.data;
    }

    public int getType(){
        return this.type;
    }

    public void setData(byte[] data){
        this.data = data;
    }

    public void setAttachment(byte[] data){
        this.attachment = data;
    }

    public void setType(int type){
        if(type != Type.FILE && type != Type.TEXT && type != Type.UNKNOWN){
            throw new IllegalArgumentException("Неверно задан тип данных");
        }
        this.type = type;
    }
}