package com.example.pallavi.norag;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
//import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionMenu;
import com.github.clans.fab.FloatingActionButton;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;
import static android.support.design.widget.Snackbar.make;

public class members extends Fragment {
    int roleid,sessionid;
    RecyclerView rv;
    String requesturl,baseurl;
    String data;
    GridLayoutManager gm;
    LinearLayout lm;
    JSONObject jsonObject;
    ArrayList<cardmemberdata> cmd=new ArrayList<>();
    RVmemberadapter rvmd;
    String s1;
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton addproctore, addfaculty, addstudentmember;
    MaterialDialog md;
    Button save;
    boolean wrapInScrollView=true;
    Spinner branchsp,designationsp;
    LinearLayout ll2;
    TextView nametv,emailtv,phonenotv;
    EditText nametxt,emailtxt,phonenotxt;
    String name,email,phoneno,branch,designation;
    JSONObject jo;
    CoordinatorLayout cl;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.member_main, container, false);
        final MaterialDialog mate=new MaterialDialog.Builder(getActivity())
                .title("No-Rag")
                .content("Please Wait While Members are fetched")
                .progress(true, 0)
                .show();
        mate.setCanceledOnTouchOutside(false);

        baseurl=getString(R.string.base_url);
        Log.v("Supratim","In the member activity");
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(getActivity());
        roleid=sp.getInt("role",-1);
        materialDesignFAM = (FloatingActionMenu)view.findViewById(R.id.fabmenu);
        addproctore = (FloatingActionButton)view.findViewById(R.id.addproctore);
        //addproctore = (FloatingActionButton)view.findV(R.id.addproctore);
        addfaculty = (FloatingActionButton)view.findViewById(R.id.addfaculty);
        addstudentmember = (FloatingActionButton)view.findViewById(R.id.addstudentmember);

        if (roleid==1)
        {
            sessionid=sp.getInt("studentsessionid",-1);
            materialDesignFAM.setVisibility(View.GONE);
            String atype="all";
            data = "{\"atype\":\"" + atype + "\",\"sessionid\":\"" + sessionid + "\"}";
            requesturl=baseurl+"showparticularauthority/";
        }
        else if (roleid==2)
        {
            sessionid=sp.getInt("authoritysessionid",-1);
            String atype="all";
            data = "{\"atype\":\"" + atype + "\",\"sessionid\":\"" + sessionid + "\"}";
            requesturl=baseurl+"showparticularauthority/";

        }


        //sheet.setVisibility(View.INVISIBLE);
        md=null;


        addproctore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu first item clicked
                md=new MaterialDialog.Builder(getActivity())
                        .title("Add Proctore")
                        .customView(R.layout.sheet_authority, wrapInScrollView)
                        .positiveText("Cancel")
                        .show();
                nametxt=(EditText) md.findViewById(R.id.nametxt);
                emailtxt=(EditText) md.findViewById(R.id.emailtxt);
                phonenotxt=(EditText) md.findViewById(R.id.phonenotxt);
                branchsp=(Spinner)md.findViewById(R.id.spinnerbranch);
                designationsp=(Spinner)md.findViewById(R.id.spinnerdesignation);
                save=(Button)md.findViewById(R.id.save);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        name=nametxt.getText().toString();
                        email=emailtxt.getText().toString();
                        phoneno=phonenotxt.getText().toString();
                        //name=nametv.getText().toString();
                        branch=branchsp.getSelectedItem().toString();
                        designation=designationsp.getSelectedItem().toString();
                        data = "{\"name\":\"" + name + "\",\"email\":\"" + email + "\",\"phone\":\"" + phoneno + "\",\"designation\":\"" + designation + "\",\"branch\":\"" + branch + "\",\"sessionid\":\"" + sessionid + "\"}";
                        Log.v("Checking",data);
                        requesturl=baseurl+"proctoreregistration/";
                        // add_authoity();

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
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //  Log.v("Error","Network Failure");
                                                Log.v("Checking","Network Failure");
                                                Toast.makeText(getActivity(),"Network Failure",Toast.LENGTH_SHORT).show();
                                                //cl=(CoordinatorLayout)view.findViewById(R.id.coordinatorlayout);
                                                Snackbar sn=Snackbar.make(view.findViewById(R.id.coordinatorlayout), "Network Failure", Snackbar.LENGTH_LONG);
                                                sn.setActionTextColor(Color.MAGENTA);
                                                View sbView = sn.getView();
                                                sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.myblue));
                                                sn.show();
                                               // mate.dismiss();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException{
                                        // Log.v("Checking");

                                        try {
                                            s1 = response.body().string();
                                            Log.v("Checking",s1);
                                            //  wait=0;
                                        } catch (Exception e2) {
                                            //  wait=0;
                                            e2.printStackTrace();

                                        }


                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //Toast.makeText(About.this,"Password Changed Successfully",Toast.LENGTH_SHORT);

                                                //   Log.v("THE JSON OBJ IS CREATED", s1);
                                                Log.v("Checking","In response ui thread");
                                                //  final String s2 = s1.substring(s1.indexOf('[') + 1, s1.length() - 1);
                                                //s1.substring(s1.indexOf('[')+1, s1.length() - 1);

                                                try {
                                                    jo = new JSONObject(s1);
                                                    //          wait = 0;

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    //        wait = 0;
                                                }
                                                try {

                                                 //   int response=jo.getInt("return_status");

                                                        String message=jo.getString("message");
                                                        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT);

                                                 //   cl=(CoordinatorLayout)view.findViewById(R.id.coordinatorlayout);

                                                    Snackbar sn=Snackbar.make(view.findViewById(R.id.coordinatorlayout), message, Snackbar.LENGTH_LONG);
                                                    sn.setActionTextColor(Color.MAGENTA);
                                                    View sbView = sn.getView();
                                                    sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.myblue));
                                                    sn.show();

                                                    md.dismiss();

                                                    //   wait = 0;
                                                }
                                                catch (JSONException e1) {
                                                    e1.printStackTrace();

                                                    //    wait = 0;

                                                }
                                            }
                                        });
                                //    mate.dismiss();
                                    }
                                });
                            }
                        });
                        th.start();
                    }
                });

            }
        });
        addfaculty.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu second item clicked
                md=new MaterialDialog.Builder(getActivity())
                        .title("Add Faculty")
                        .customView(R.layout.sheet_authority, wrapInScrollView)
                        .positiveText("Cancel")
                        .show();
                Log.d("Checking","In faculty");
                nametxt=(EditText) md.findViewById(R.id.nametxt);
                emailtxt=(EditText) md.findViewById(R.id.emailtxt);
                phonenotxt=(EditText) md.findViewById(R.id.phonenotxt);
                branchsp=(Spinner)md.findViewById(R.id.spinnerbranch);

                designationsp=(Spinner)md.findViewById(R.id.spinnerdesignation);
                save=(Button)md.findViewById(R.id.save);
                Log.d("Checking","In faculty");
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("Checking","In faculty");
                        name=nametxt.getText().toString();
                        email=emailtxt.getText().toString();
                        phoneno=phonenotxt.getText().toString();
                        //name=nametv.getText().toString();
                        branch=branchsp.getSelectedItem().toString();
                        designation=designationsp.getSelectedItem().toString();
                        data = "{\"name\":\"" + name + "\",\"email\":\"" + email + "\",\"phone\":\"" + phoneno + "\",\"designation\":\"" + designation + "\",\"branch\":\"" + branch + "\",\"sessionid\":\"" + sessionid + "\"}";
                        Log.v("Checking",data);
                        requesturl=baseurl+"facultyregistration/";
                        // add_authoity();

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
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //  Log.v("Error","Network Failure");
                                                Log.v("Checking","Network Failure");
                                                Toast.makeText(getActivity(),"Network Failure",Toast.LENGTH_SHORT).show();
                                              //  cl=(CoordinatorLayout)view.findViewById(R.id.coordinatorlayout);
                                                Snackbar sn=Snackbar.make(view.findViewById(R.id.coordinatorlayout), "Network Failure", Snackbar.LENGTH_LONG);
                                                sn.setActionTextColor(Color.MAGENTA);
                                                View sbView = sn.getView();
                                                sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.myblue));
                                                sn.show();

                                                //  mate.dismiss();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException{
                                        // Log.v("Checking");

                                        try {
                                            s1 = response.body().string();
                                            Log.v("Checking",s1);
                                            //  wait=0;
                                        } catch (Exception e2) {
                                            //  wait=0;
                                            e2.printStackTrace();

                                        }


                                       getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //Toast.makeText(About.this,"Password Changed Successfully",Toast.LENGTH_SHORT);

                                                //   Log.v("THE JSON OBJ IS CREATED", s1);
                                                Log.v("Checking","In response ui thread");
                                                //  final String s2 = s1.substring(s1.indexOf('[') + 1, s1.length() - 1);
                                                //s1.substring(s1.indexOf('[')+1, s1.length() - 1);

                                                try {
                                                    jo = new JSONObject(s1);
                                                    //          wait = 0;

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    //        wait = 0;
                                                }
                                                try {

                                                    //   int response=jo.getInt("return_status");

                                                    String message=jo.getString("message");
                                                    Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT);
                                                   // cl=(CoordinatorLayout)view.findViewById(R.id.coordinatorlayout);
                                                    Snackbar sn=Snackbar.make(view.findViewById(R.id.coordinatorlayout), message, Snackbar.LENGTH_LONG);
                                                    sn.setActionTextColor(Color.MAGENTA);
                                                    View sbView = sn.getView();
                                                    sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.myblue));
                                                    sn.show();

                                                    md.dismiss();

                                                    //   wait = 0;
                                                }
                                                catch (JSONException e1) {
                                                    e1.printStackTrace();

                                                    //    wait = 0;

                                                }
                                            }
                                        });
                                 //   mate.dismiss();
                                    }
                                });
                            }
                        });
                        th.start();
                    }
                });

            }
        });

        addstudentmember.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu third item clicked
                md=new MaterialDialog.Builder(getActivity())
                        .title("Add Student Member")
                        .customView(R.layout.sheet_authority, wrapInScrollView)
                        .positiveText("Cancel")
                        .show();

                ll2=(LinearLayout)md.findViewById(R.id.layout12);
                ll2.setVisibility(View.GONE);
                nametxt=(EditText) md.findViewById(R.id.nametxt);
                emailtxt=(EditText) md.findViewById(R.id.emailtxt);
                phonenotxt=(EditText) md.findViewById(R.id.phonenotxt);
                branchsp=(Spinner)md.findViewById(R.id.spinnerbranch);

                designationsp=(Spinner)md.findViewById(R.id.spinnerdesignation);
                save=(Button)md.findViewById(R.id.save);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        name=nametxt.getText().toString();
                        email=emailtxt.getText().toString();
                        phoneno=phonenotxt.getText().toString();
                        //name=nametv.getText().toString();
                        branch=branchsp.getSelectedItem().toString();
                        designation=designationsp.getSelectedItem().toString();
                        data = "{\"name\":\"" + name + "\",\"email\":\"" + email + "\",\"phone\":\"" + phoneno + "\",\"branch\":\"" + branch + "\",\"sessionid\":\"" + sessionid + "\"}";
                        Log.v("Checking",data);
                        requesturl=baseurl+"studentmemberregistration/";
                        // add_authoity();

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
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //  Log.v("Error","Network Failure");
                                                Log.v("Checking","Network Failure");
                                                Toast.makeText(getActivity(),"Network Failure",Toast.LENGTH_SHORT).show();
                                              //  cl=(CoordinatorLayout)view.findViewById(R.id.coordinatorlayout);
                                                Snackbar sn=Snackbar.make(view.findViewById(R.id.coordinatorlayout), "Network Failure", Snackbar.LENGTH_LONG);
                                                sn.setActionTextColor(Color.MAGENTA);
                                                View sbView = sn.getView();
                                                sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.myblue));
                                                sn.show();

                                            }
                                        });
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException{
                                        // Log.v("Checking");

                                        try {
                                            s1 = response.body().string();
                                            Log.v("Checking",s1);
                                            //  wait=0;
                                        } catch (Exception e2) {
                                            //  wait=0;
                                            e2.printStackTrace();

                                        }


                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //Toast.makeText(About.this,"Password Changed Successfully",Toast.LENGTH_SHORT);

                                                //   Log.v("THE JSON OBJ IS CREATED", s1);
                                                Log.v("Checking","In response ui thread");
                                                //  final String s2 = s1.substring(s1.indexOf('[') + 1, s1.length() - 1);
                                                //s1.substring(s1.indexOf('[')+1, s1.length() - 1);

                                                try {
                                                    jo = new JSONObject(s1);
                                                    //          wait = 0;

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    //        wait = 0;
                                                }
                                                try {

                                                    //   int response=jo.getInt("return_status");

                                                    String message=jo.getString("message");
                                                    Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT);
                                                   // cl=(CoordinatorLayout)view.findViewById(R.id.coordinatorlayout);
                                                    Snackbar sn=Snackbar.make(view.findViewById(R.id.coordinatorlayout), message, Snackbar.LENGTH_LONG);
                                                    sn.setActionTextColor(Color.MAGENTA);
                                                    View sbView = sn.getView();
                                                    sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.myblue));
                                                    sn.show();

                                                    md.dismiss();

                                                    //   wait = 0;
                                                }
                                                catch (JSONException e1) {
                                                    e1.printStackTrace();

                                                    //    wait = 0;

                                                }
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
        });
        rv=(RecyclerView)view.findViewById(R.id.rmv);
        rv.setHasFixedSize(true);
        gm=new GridLayoutManager(getActivity(),2);
        rv.setLayoutManager(gm);
        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client=new OkHttpClient();
                Request request = new Request.Builder()
                        .url(requesturl)
                        .post(RequestBody.create(okhttp3.MediaType.parse("application/json;charset=utf-8"), data))
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                       getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.v("Network Error","Error In Network");
                               // Toast.makeText(getActivity(),"Network Failure",Toast.LENGTH_SHORT).show();
                                //Snackbar.make(lm,"sdasdsa",Snackbar.LENGTH_SHORT).show();
                                //cl=(CoordinatorLayout)view.findViewById(R.id.coordinatorlayout);
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
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.v("s1",s1);
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


                                        String name=jsonObject.getString("name");
                                        String phone=jsonObject.getString("phone");
                                        String email=jsonObject.getString("email");
                                        String branch=jsonObject.getString("branch");
                                        String designation=jsonObject.getString("designation");
                                        String atype=jsonObject.getString("atype");

                                        //cd.add(i,new carddata(userpic2,id,quserid,ques,questionaskname,nooflike,noofunlike,noofanswers,statuslike,statusunlike,sessionuserid,follow,editqbtn,likeqbtn,unlikeqbtn));
                                       cmd.add(i,new cardmemberdata(name,phone,email,branch,designation,atype));

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }


                                //Snackbar.make(cl,"Successfull",Snackbar.LENGTH_SHORT).show();
                               rvmd=new RVmemberadapter(cmd,getActivity());
                                final Context ctx=rv.getContext();
                                LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(ctx, R.anim.layout_animation_fall_down);

                                rv.setLayoutAnimation(animation);
                                rv.setAdapter(rvmd);



                            }
                        });
                    mate.dismiss();
                    }
                });
            }
        });
        th.start();
        return view;
    }

}
