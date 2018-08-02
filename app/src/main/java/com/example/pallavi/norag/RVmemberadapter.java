package com.example.pallavi.norag;

/**
 * Created by Supratim on 03-03-2018.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Supratim on 06/05/2017.
 */
public class RVmemberadapter extends RecyclerView.Adapter<RVmemberadapter.PersonViewHolder>{
    List<cardmemberdata> c;
    RecyclerView rv;
    Activity main;
    PersonViewHolder pv;
    Intent mappage,viewprofilepage;
    Bundle mBundle;
    int roleid;
    String cid;
    String sid;
    String answerid,message;
    String quesid;
    String ansid,s1;
    int sessionid,s;
    int check = 0;
    boolean userSelect=false;
    String data,requesturl,baseurl;
    String res;
    JSONObject jsonObject;
    boolean wait=true;
    Intent replypage;
    RVcomplainadapter rvad;
    //boolean wait1=true;
    RVmemberadapter(List asd,Activity main){
        this.c=asd;
        this.main=main;


    }
    public void addelement(cardmemberdata member){
        c.add(0,member);
        notifyItemChanged(0);

    }

    public void removecomplainAt(int position) {
        c.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, c.size());
    }



    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_members, parent, false);
        check=0;
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;




    }

    @Override
    public void onBindViewHolder(final PersonViewHolder holder, final int position) {

        pv=holder;
        SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(main);
        baseurl=main.getString(R.string.base_url);
        roleid=sp.getInt("role",-1);
        if (roleid==2)
        {
           // holder.deletebtn.setVisibility(View.GONE);
            sessionid=sp.getInt("authoritysessionid",-1);
        }
        if (roleid==1)
        {

            sessionid=sp.getInt("studentsessionid",-1);

        }
        holder.nametv.setText(c.get(position).name);
        holder.atypetv.setText(c.get(position).atype);
        holder.branchtv.setText(c.get(position).branch);
        holder.designationtv.setText(c.get(position).designation);
        holder.phonetv.setText(c.get(position).phone);
        holder.emailtv.setText(c.get(position).email);


    }
    @Override
    public int getItemCount() {
        return c.size();
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;

        //TextView noofanswer;
        ImageView personPhoto;
        TextView nametv,atypetv,branchtv,phonetv,designationtv,emailtv;


        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cmv);

            // personPhoto=(ImageView)itemView.findViewById(R.id.complainimageButton);
            nametv=(TextView)itemView.findViewById(R.id.nametv);
            emailtv=(TextView)itemView.findViewById(R.id.emailtv);
            phonetv=(TextView)itemView.findViewById(R.id.phonetv);
            atypetv=(TextView)itemView.findViewById(R.id.atypetv);
            designationtv=(TextView)itemView.findViewById(R.id.designationtv);
            branchtv=(TextView)itemView.findViewById(R.id.branchtv);

            //ll=(LinearLayout)itemView.findViewById(R.id.authoritylayout);
            //qid=(TextView)itemView.findViewById(R.id.qid);
            // answerbutton=(ImageButton)itemView.findViewById(R.id.answerButton);


            //quesid= Integer.parseInt(qid.getText().toString());
            //Log.v("The id of question is", String.valueOf(quesid));


        }
    }


}
