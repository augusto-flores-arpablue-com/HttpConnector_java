/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arpablue.arpahttp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author augusto
 */
public class HttpCouple {
    private String mName;
    private String mValue;
    public HttpCouple(){}
    public HttpCouple( String name, String value){
        setName( name );
        setValue( value );
    }
    public void setName( String name ){
        mName = name;
        if( mName == null){
            return;
        }
        mName = mName.trim();
        
    }
    public String getName(){
        return mName;
    }
    public void setValue( String value ){
        mValue = value;
        if( mValue == null){
            return;
        }
        mValue = mValue.trim();
    }
    public String getValue(){
        try {
            return URLEncoder.encode(mValue,"UTF-8");
        } catch (UnsupportedEncodingException ex) {
            
        }
        return "";
    }
    public boolean isThis(String name){
        if(( name == null ) && ( this.getName() == null )){
            return true;
        }
        if( name == null ){
            return false;
        }
        if( this.getName() == null){
            return false;
        }
        name = name.trim();
        return this.getName().equalsIgnoreCase(name);
    }
    
}
