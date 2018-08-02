package com.example.pallavi.norag;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.DateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.solver.Cache;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.widget.LinearLayout.VERTICAL;

public class complain extends Fragment {
int roleid,sessionid;
String requesturl,data,s1;
    RecyclerView rv;
    LinearLayoutManager lm;
    JSONObject jsonObject;
    ArrayList<cardcomplaindata> ccd=new ArrayList<>();
    RVcomplainadapter rvad;
    String baseurl;
    CoordinatorLayout cl;
    MaterialDialog mate;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {
         view= inflater.inflate(R.layout.activity_complain, container, false);
     mate=new MaterialDialog.Builder(getActivity())
                .title("No-Rag")
                .content("Please Wait While Complains are fetched")
                .progress(true, 0)
                .show();
//        mate.show();

        mate.setCanceledOnTouchOutside(false);
        baseurl=getString(R.string.base_url);
        Log.v("Supratim","In the complain activity");
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(getActivity());
        roleid=sp.getInt("role",-1);
        if (roleid==1)
        {
            sessionid=sp.getInt("studentsessionid",-1);
            requesturl=baseurl+"seeparticularcomplain/";
            data="{\"sessionid\":\""+sessionid+"\"}";


        }
        else if(roleid==2)
        {
            sessionid=sp.getInt("authoritysessionid",-1);
            requesturl=baseurl+"seecomplains/";
            data="{\"sessionid\":\""+sessionid+"\"}";
        }

        rv=(RecyclerView)view.findViewById(R.id.rcv);
        rv.setHasFixedSize(true);
        lm=new LinearLayoutManager(getActivity());
        rv.setLayoutManager(lm);

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                final OkHttpClient client = new OkHttpClient();

                final Request request = new Request.Builder()
                        .url(requesturl)
                        .post(RequestBody.create(okhttp3.MediaType.parse("application/json;charset=utf-8"), data))
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.v("Error","Network Failure");
                                //Toast.makeText(getActivity(), "Network Failure", Toast.LENGTH_LONG).show();

                                Snackbar sn=Snackbar.make(view.findViewById(R.id.coordinatorlayout), "Network Failure", Snackbar.LENGTH_LONG);
                                sn.setActionTextColor(Color.MAGENTA);
                                View sbView = sn.getView();
                                sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.myblue));
                                sn.show();
                                mate.dismiss();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        s1= response.body().string();

                        // Log.v("Error",s1);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.v("MYTAG",s1+" S");
                                String s2=s1.substring(s1.indexOf('[')+1,s1.lastIndexOf(']'));
                                String s3[]=s2.split("[}][,]");

                                for(String s4:s3)
                                {
                                    Log.v("response",s4+'}');

                                }
                                for (int i=0;i<s3.length;i++)
                                {
                                    if(i!=s3.length-1)
                                    {
                                        s3[i]=s3[i]+'}';
                                    }
                                    try {
                                        jsonObject =new JSONObject(s3[i]);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                    try {
                                        int cid=jsonObject.getInt("cid");
                                        int sid=jsonObject.getInt("sid");
                                        String severity_of_punishment=jsonObject.getString("severity_of_punishment");
                                        String student_name=jsonObject.getString("student_name");
                                        String mobile_no=jsonObject.getString("mobile_no");
                                        String g_mobile_no=jsonObject.getString("g_mobile_no");
                                        String complain_txt=jsonObject.getString("complain_txt");
                                        String attachment=jsonObject.getString("attachment");
                                        String date=jsonObject.getString("date");
                                        //String date=newdate.substring(0,newdate.indexOf('T'))+" "+newdate.substring(newdate.indexOf('T')+1,newdate.indexOf('.'));
                                        //String date=String.valueOf(new DateTime())
                                        String status=jsonObject.getString("status");
                                        float latitude=Float.parseFloat(jsonObject.getString("latitude"));
                                        float longitude=Float.parseFloat(jsonObject.getString("longitude"));

                                        int totalvote=jsonObject.getInt("totalvotes");
                                        String myvote=String.valueOf(Integer.parseInt(jsonObject.getString("myvote")));

                                        //int unlikeqbtn=0;
                                        //int myvote=0;
                                        //cd.add(i,new carddata(userpic2,id,quserid,ques,questionaskname,nooflike,noofunlike,noofanswers,statuslike,statusunlike,sessionuserid,follow,editqbtn,likeqbtn,unlikeqbtn));
                                        ccd.add(i,new cardcomplaindata(latitude,longitude,cid,sid,severity_of_punishment,student_name,mobile_no,g_mobile_no,complain_txt,attachment,date,status,totalvote,myvote));
                                        mate.dismiss();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        mate.dismiss();
                                    }
                                }



                                rvad=new RVcomplainadapter(ccd,getActivity());

                                final Context ctx=rv.getContext();
                                LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(ctx, R.anim.layout_animation_fall_down);
                                rv.setLayoutAnimation(animation);
                                rv.setAdapter(rvad);
                                ItemTouchHelper.Callback callback =
                                        new SimpleItemTouchHelperCallback(rvad);
                                ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
                                touchHelper.attachToRecyclerView(rv);



                            }

                        });

                    }
                });
            }
        });
        th.start();

   /*     FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    return view;
    }

}
