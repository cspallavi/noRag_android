package com.example.pallavi.norag;

/**
 * Created by Supratim on 03-03-2018.
 */

public class cardcomplaindata {
    int cid;
    int sid;
    String severity_of_punishment;
    String student_name;
    String mobile_no;
    String g_mobile_no;
    String complain_txt;
    String attachment;
    String date;
    String status;
    int totalvote;
    String myvote;
    float latitude;
    float longitude;


    cardcomplaindata(float latitude,float longitude,int cid,int sid,String severity_of_punishment,String student_name,String mobile_no,String g_mobile_no,String complain_txt, String attachment,String date,String status,int totalvote,String myvote)
    {
        this.latitude=latitude;
        this.longitude=longitude;
        this.cid=cid;
        this.sid=sid;
        this.severity_of_punishment=severity_of_punishment;
        this.student_name=student_name;
        this.mobile_no=mobile_no;
        this.g_mobile_no=g_mobile_no;
        this.complain_txt=complain_txt;
        this.attachment=attachment;
        this.date=date;
        this.status=status;
        this.totalvote=totalvote;
        this.myvote=myvote;

    }
}
