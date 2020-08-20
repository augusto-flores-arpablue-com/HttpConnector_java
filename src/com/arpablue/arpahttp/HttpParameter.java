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
public class HttpParameter {
    private String mName ;
    private String mId;
    private String mValue;
    /**
     * Default constructor.
     */
    public HttpParameter(){}
    /**
     * It constructor receive the name and the value of the parameter.
     * @param name It is the name of the parameter.
     * @param value  It is the value of the parameter.
     */
    public HttpParameter( String name, String value){
        
    }
    public void setName(String name){
        mName = name;
        if( mName == null ){
            return;
        }
        mName = mName.trim();
        this.setId(mName);
    }
    protected String getName(){
        return mName;
    }
    /**
     * It set in format a text to be used as ID.
     * @param target It is the text to be formated.
     * @return It is the text after to apply the format.
     */
    protected static String formatID( String target ){
        if( target == null ){
            return null;
        }
        target = target.trim();
        target = target.toLowerCase();
        target = target.replace(" ", "");
        return target;
    }
    protected void setId( String name ){
        
        mId = formatID( name );
    }
    protected String getId(){
        return mId;
    }
    public void setValue( String value ){
        this.mValue = value;
    }
    public String getValue(){
        return this.mValue;
    }
    /**
     * It compare if the current Parameter has the same name tthat the parameter specified.
     * @param name It is the name to compare.
     * @return It is true if the name is the same.
     */
    public boolean isThis( String name ){
        if( (name == null) &&(this.getName() == null ) ){
            return true;
        }
        if( name == null ){
            return false;
        }
        if( this.getName() == null ){
            return false;
        }
        name = formatID( name );
        return this.getId().equalsIgnoreCase(name);
    }
}
