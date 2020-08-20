/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arpablue.arpahttp;

/**
 *
 * @author augusto
 */
public class Msger {
    
    
    protected boolean mShowData = true;
    
    /**
     * Enable the message in the console, by default is true.
     * @param showData If is true enable the write message.
     */
    public void setShowData(boolean showData){
        mShowData = showData;
    }
    /**
     * Shoe a message by console if the show data attribute is in true.
     * @param text It is the text to be written.
     */
    protected void write( String text ){
        if( !mShowData ){
            return;
        }
        System.out.println(text);
    }
}
