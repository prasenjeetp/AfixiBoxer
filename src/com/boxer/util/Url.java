package com.boxer.util;



public class Url {
    
    //private variables
    int _id;
    String _name;
    String _phone_number;
     
    // Empty constructor
    public Url(){
         
    }
    // constructor
    public Url(int id, String url ){
        this._id = id;
        this._name = url;
        this._phone_number = "Empty";
    }
     
    // constructor
    public Url(String url ){
        this._name = url;
        this._phone_number = "Empty";
    }
    // getting ID
    public int getID(){
        return this._id;
    }
     
    // setting id
    public void setID(int id){
        this._id = id;
    }
     
    // getting name
    public String getName(){
        return this._name;
    }
     
    // setting name
    public void setName(String name){
        this._name = name;
    }
     
    // getting phone number
    public String getPhoneNumber(){
        return this._phone_number;
    }
     
    // setting phone number
    public void setPhoneNumber(String phone_number){
        this._phone_number = phone_number;
    }
}