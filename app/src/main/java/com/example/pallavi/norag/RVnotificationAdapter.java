package com.example.pallavi.norag;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Supratim on 15/06/2017.
 */

public class RVnotificationAdapter extends RecyclerView.Adapter<RVnotificationAdapter.PersonViewHolder> {
   RecyclerView rv;
    CoordinatorLayout cl;
    Activity main;
    List<cardnotificationdata> c;
    int s;
    int sessionid,roleid;
    String data,requesturl,baseurl;



    RVnotificationAdapter(List asd,Activity main,CoordinatorLayout cl){
        this.c=asd;
        this.main=main;
        this.cl=cl;


    }
    public void removeAt(int position) {
        c.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, c.size());
    }
    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_notification, parent, false);
        RVnotificationAdapter.PersonViewHolder pvh = new RVnotificationAdapter.PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final PersonViewHolder holder,final int position) {

        PersonViewHolder pv = (PersonViewHolder) holder;

        baseurl=main.getString(R.string.base_url);
        holder.notification.setText(c.get(position).notification);
        holder.nsid.setText(String.valueOf(c.get(position).nsid));
        holder.date.setText(String.valueOf(c.get(position).date));
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home=new Intent(main.getApplication(), com.example.pallavi.norag.Introduction.class);
                home.putExtra("code",2);
                main.startActivity(home);
                //MapsActivity.this.finish();
            }
        });
        holder.delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(main);

                roleid=sharedPref.getInt("role",-1);
                int nsid=c.get(position).nsid;
                if (roleid==1)
                {
                    requesturl=baseurl+"deletestudentnotification/";
                    sessionid=sharedPref.getInt("studentsessionid", -1);
                    s=sessionid;
                    data="{\"nsid\":\""+nsid+"\",\"sessionid\":\"" + s + "\"}";
                }
                else if (roleid==2)
                {
                    requesturl=baseurl+"deleteauthoritynotification/";
                    sessionid=sharedPref.getInt("authoritysessionid", -1);
                    s=sessionid;
                    data="{\"nid\":\""+nsid+"\",\"sessionid\":\"" + s + "\"}";
                }


                Thread th=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client=new OkHttpClient();
                        Request request = new Request.Builder()
                                .url(requesturl)
                                .post(RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), data))
                                .build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                main.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Toast.makeText(main,"Network Failure",Toast.LENGTH_LONG).show();
                                       // cl=(CoordinatorLayout)rv.findViewById(R.id.coordinatorlayout);

                                       Snackbar sn=Snackbar.make(main.findViewById(R.id.coordinatorlayout), "Network Error", Snackbar.LENGTH_LONG);
                                        sn.setActionTextColor(Color.MAGENTA);
                                        View sbView = sn.getView();
                                        sbView.setBackgroundColor(ContextCompat.getColor(main, R.color.myblue));
                                        sn.show();

                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                                main.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Toast.makeText(main,"Successfully deleted",Toast.LENGTH_LONG).show();
                                       // cl=(CoordinatorLayout)findViewById(R.id.coordinatorlayout);
                                        Snackbar sn=Snackbar.make(main.findViewById(R.id.coordinatorlayout), "Successfully deleted", Snackbar.LENGTH_LONG);
                                        sn.setActionTextColor(Color.MAGENTA);
                                        View sbView = sn.getView();
                                        sbView.setBackgroundColor(ContextCompat.getColor(main, R.color.myblue));
                                        sn.show();

                                        removeAt(position);
                                    }
                                });
                            }
                        });

                    }
                });
                th.start();

            }
        });
    }

    @Override
    public int getItemCount() {
        return c.size();
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView notification;
        TextView nsid;
        TextView date;
        ImageButton delbtn;

        public PersonViewHolder(View itemView) {


            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.crv);
            notification=(TextView)itemView.findViewById(R.id.notificationmessage);
            nsid=(TextView)itemView.findViewById(R.id.nid);
            delbtn=(ImageButton)itemView.findViewById(R.id.delnotification);
            date=(TextView)itemView.findViewById(R.id.notificationdate);


        }
    }
}
