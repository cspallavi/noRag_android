package com.example.pallavi.norag;

/**
 * Created by Supratim on 15/06/2017.
 */

public class cardnotificationdata {

    public int nsid;
    public int sid;
    public String notification;
    public String date;
    //public String date;

    cardnotificationdata(int nsid,int sid,String notification,String date){

        this.nsid=nsid;
        this.sid=sid;
        this.notification=notification;
        this.date=date;
      //  this.date=date;
    }
}
