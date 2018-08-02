package com.example.pallavi.norag;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class notification extends Fragment {
    int sessionid,roleid,nsid,sid;
    String data;
    String requesturl,baseurl,date;
    RecyclerView rnv;
    LinearLayoutManager lm;
    RVnotificationAdapter rvnd;
    String res;
    JSONObject jsonObject;
    ArrayList<cardnotificationdata> cnd=new ArrayList<>();
    CoordinatorLayout cl;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
       final View view= inflater.inflate(R.layout.notifcation_main, container, false);
       // Log.v("The notification  ","Yes it is true");
      //  Slide s=new Slide();
      //  s.setDuration(1000);
       // getActivity().getWindow().setEnterTransition(s);

        baseurl=getString(R.string.base_url);
        final MaterialDialog mate=new MaterialDialog.Builder(getActivity())
                .title("No-Rag")
                .content("Please Wait While Notifications are fetched")
                .progress(true, 0)
                .show();

        mate.setCanceledOnTouchOutside(false);
        Log.v("notification called ","Yes it is true");
        SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(getActivity());
        sessionid=sharedPref.getInt("sessionuserid", -1);
        roleid=sharedPref.getInt("role",-1);


        rnv=(RecyclerView)view.findViewById(R.id.rnv);
        //rnv.setHasFixedSize(true);
        lm=new LinearLayoutManager(getActivity());
        rnv.setLayoutManager(lm);
        if (roleid==1) {
            requesturl = baseurl+"seestudentnotification/";
            sessionid=sharedPref.getInt("studentsessionid",-1);
        }
            else if (roleid==2) {
            requesturl = baseurl+"seeauthoritynotification/";
            sessionid=sharedPref.getInt("authoritysessionid",-1);
            }
        data="{\"sessionid\":\""+sessionid+"\"}";
        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {

               OkHttpClient client=new OkHttpClient();
                //int cacheSize = 10 * 1024 * 1024;
              //  OkHttpClient.Builder builder = new OkHttpClient.Builder()
                //        .cache(new Cache(getActivity().getCacheDir(), cacheSize));
                //OkHttpClient client=new OkHttpClient();

                Log.d("The request of data is ",data);
                Request request = new Request.Builder()
                        .url(requesturl)
                        .post(RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),data))
                        //.get()
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                       getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                              Toast.makeText(getActivity(),"Network Error",Toast.LENGTH_LONG).show();
                        //    cl=(CoordinatorLayout)view.findViewById(R.id.coordinatorlayout);
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

                        res=response.body().string();
                        Log.d("The response is coming",res);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String s2=res.substring(res.indexOf('[')+1,res.lastIndexOf(']'));
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
                                       if (roleid==1) {
                                            nsid = jsonObject.getInt("nsid");
                                            sid = jsonObject.getInt("sid");
                                          // String newdate=jsonObject.getString("date");
                                          //date=newdate.substring(0,newdate.indexOf(' '))+" "+newdate.substring(newdate.indexOf(' ')+1,newdate.indexOf('.'));

                                       }
                                        else if(roleid==2)
                                       {
                                            nsid = jsonObject.getInt("nid");
                                            sid = jsonObject.getInt("aid");

                                       }

                                        String notification=jsonObject.getString("notification");
                                        String newdate=jsonObject.getString("date");
                                        date=newdate.substring(0,newdate.indexOf('T'))+" "+newdate.substring(newdate.indexOf('T')+1,newdate.indexOf('.'));

                                        //String date=jsonObject.getString("date");
                                        //int noofnotification=jsonObject.getInt("noofnotification");

                                        //cd.add(i,new carddata(userpic2,id,quserid,ques,questionaskname,nooflike,noofunlike,noofanswers,statuslike,statusunlike,sessionuserid,follow,editqbtn,likeqbtn,unlikeqbtn));
                                        cnd.add(i,new cardnotificationdata(nsid,sid,notification,date));
                                    } catch (Exception e) {
                                        e.printStackTrace();


                                    }
                                    rvnd=new RVnotificationAdapter(cnd,getActivity(),cl);

                                    final Context ctx=rnv.getContext();
                                    LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(ctx, R.anim.layout_animation_fall_down);

                                    rnv.setLayoutAnimation(animation);
                                    rnv.setAdapter(rvnd);
                                    //Log.v("conm","shcjs");
                                }

                            }
                        });
                    mate.dismiss();

                    }
                });
            }
        });
        th.start();




        return  view;

    }
}
