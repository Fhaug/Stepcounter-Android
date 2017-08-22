package com.haugsand.fredrikh.activitynavigation;

/**
 * Created by FredrikH on 13.04.2017.
 */
public class Steps {

    //private variables
    int _id;
    String _date;
    String _steps;

    // Empty constructor
    public Steps(){

    }
    // constructor
    public Steps(int id, String date, String steps){
        this._id = id;
        this._date = date;
        this._steps = steps;
    }

    // constructor
    public Steps(String date, String steps){
        this._date = date;
        this._steps = steps;
    }
    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting dates
    public String getDate(){
        return this._date;
    }

    // setting dates
    public void setDate(String date){
        this._date = date;
    }

    // getting steps
    public String getSteps(){
        return this._steps;
    }

    // setting steps
    public void setSteps(String steps){
        this._steps = steps;
    }
}