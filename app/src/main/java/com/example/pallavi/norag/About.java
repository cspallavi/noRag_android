package com.example.pallavi.norag;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
/*import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;*/
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/*import static com.example.pallavi.norag.R.id.friendList;
import static com.example.pallavi.norag.R.id.friendRequests;
import static com.example.pallavi.norag.R.id.friends1;
import static com.example.pallavi.norag.R.id.logout;
import static com.example.pallavi.norag.R.id.myProfile;
import static com.example.pallavi.norag.R.id.recentChats;
import static com.example.pallavi.norag.R.id.registeredUsers;
import static com.example.pallavi.norag.R.id.passwordChange;*/


/**
 * Created by Pallavi on 17/07/2017.
 */

public class About extends Fragment {
    CircleImageView profile_photo;JSONObject jo;String n1,n2,n3,n6,n7,n8,n9,n10,n11,n12,n13;int n4;int response1,n0;String n5;File f;
    TextView friend_status,friend_status1,studied_in1,workplace1,userid1,marital_status1,qualify1,age1,name1,friends,status1,lives_in1,gender1;Button start_chat;Toolbar t;String s;
    String data;String s1,s2;SharedPreferences sp;int Response;
    private BottomSheetBehavior mBottomSheetBehavior;
    int sessionid,roleid,wait=0;
    String requesturl,baseurl;
    EditText nametxt,phonetxt,gphonetxt,emailtxt,addresstxt,rollnotxt,oldpasswordtxt,newpasswordtxt,cnewpasswordtxt;
    MaterialDialog md;
    Button changepassword,save;
    ImageView edit;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       final View view= inflater.inflate(R.layout.activity_about, container, false);
        final MaterialDialog mate=new MaterialDialog.Builder(getActivity())
                .title("No-Rag")
                .content("Please Wait")
                .progress(true, 0)
                .show();

        mate.setCanceledOnTouchOutside(false);
        baseurl=getString(R.string.base_url);
        final SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(getActivity());
        //sessionid=sharedPref.getInt("sessionuserid", -1);
        roleid=sharedPref.getInt("role",-1);
        save=(Button)view.findViewById(R.id.save);
        changepassword=(Button)view.findViewById(R.id.changepassword);
        edit=(ImageView)view.findViewById(R.id.edit);
        nametxt=(EditText)view.findViewById(R.id.nametxt);
        phonetxt=(EditText)view.findViewById(R.id.phonetxt);
        gphonetxt=(EditText)view.findViewById(R.id.gphonenotxt);
        emailtxt=(EditText)view.findViewById(R.id.emailtxt);
        rollnotxt=(EditText)view.findViewById(R.id.rollnotxt);
        addresstxt=(EditText)view.findViewById(R.id.addresstxt);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save.setVisibility(View.VISIBLE);
                nametxt.setEnabled(true);
                phonetxt.setEnabled(true);
                gphonetxt.setEnabled(true);
                emailtxt.setEnabled(true);
                addresstxt.setEnabled(true);
                rollnotxt.setEnabled(true);
            }
        });


        if (roleid==1)
        {
            requesturl=baseurl+"showstudentdetail/";
            sessionid=sharedPref.getInt("studentsessionid", -1);
            data="{\"sessionid\":\""+sessionid+"\"}";
        }
        else if (roleid==2)
        {
            requesturl=baseurl+"showdetails/";
            sessionid=sharedPref.getInt("authoritysessionid", -1);

            data="{\"sessionid\":\""+sessionid+"\"}";
        }
        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
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
                               // Toast.makeText(getActivity(), "Network Failure", Toast.LENGTH_LONG).show();
                                Snackbar sn=Snackbar.make(view.findViewById(R.id.coordinatorlayout), "Network Failure", Snackbar.LENGTH_LONG);
                                sn.setActionTextColor(Color.MAGENTA);
                                View sbView = sn.getView();
                                sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.myblue));
                                sn.show();

                                wait = 0;
                            }
                        });
                        Log.v("ON FAILURE", "NEW CALLBACK FAILURE");
                        mate.dismiss();
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        Log.v("PALLAVI", "ENTERED ON RESPONSE");
                                            /*    MainActivity.this.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Email.setText(null);
                                                        Password.setText(null);
                                                    }
                                                });*/
                        try {
                            s1 = response.body().string();
                        } catch (Exception e2) {
                            e2.printStackTrace();

                        }
                        Log.v("THE JSON OBJ IS CREATED", s1);

                        //  final String s2 = s1.substring(s1.indexOf('[') + 1, s1.length() - 1);
                        //s1.substring(s1.indexOf('[')+1, s1.length() - 1);

                        try {
                            jo = new JSONObject(s1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try {


                                    if (roleid==1)
                                    {
                                        String name = jo.getString("name");
                                        String phoneno = jo.getString("mobile_no");
                                        String email = jo.getString("email");
                                        String address = jo.getString("address");
                                        String rollno = jo.getString("roll_no");
                                        String gphoneno=jo.getString("g_mobile_no");
                                        nametxt.setText(name);
                                        phonetxt.setText(phoneno);
                                        gphonetxt.setText(gphoneno);
                                        emailtxt.setText(email);
                                        rollnotxt.setText(rollno);
                                        addresstxt.setText(address);
                                        nametxt.setEnabled(false);
                                        phonetxt.setEnabled(false);
                                        gphonetxt.setEnabled(false);
                                        emailtxt.setEnabled(false);
                                        addresstxt.setEnabled(false);
                                        rollnotxt.setEnabled(false);

                                    }
                                    else if (roleid == 2) {
                                        String name = jo.getString("name");
                                        String phoneno = jo.getString("phone");
                                        String email = jo.getString("email");
                                        String branch = jo.getString("branch");
                                        String designatioln = jo.getString("designation");
                                        LinearLayout l3=(LinearLayout)view.findViewById(R.id.layout3);
                                        l3.setVisibility(View.GONE);
                                        LinearLayout l5=(LinearLayout)view.findViewById(R.id.layout5);
                                        l5.setVisibility(View.GONE);
                                        LinearLayout l6=(LinearLayout)view.findViewById(R.id.layout6);
                                        l6.setVisibility(View.GONE);
                                        nametxt.setText(name);
                                        phonetxt.setText(phoneno);
                                       // EditText gphonetxt=(EditText) findViewById(R.id.gphonenotxt);
                                        //gphonetxt.setText(gphoneno);

                                        emailtxt.setText(email);
                                      //  save.setVisibility(View.GONE);
                                        nametxt.setEnabled(false);
                                        phonetxt.setEnabled(false);
                                        //gphonetxt.setEnabled(false);
                                        emailtxt.setEnabled(false);

                                    }
                                    save.setVisibility(View.GONE);

                                }
                                catch (JSONException e1) {
                                    e1.printStackTrace();
                                    wait = 0;

                                }

                            }
                        });
                    mate.dismiss();
                    }
                });
            }
        });
        th.start();


      //  final View v1=new View(R.layout.sheet_content);
       // t=(Toolbar)findViewById(R.id.toobar);


        if(wait==0) {
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    wait = 1;
                    if (roleid == 1) {
                        requesturl = baseurl+"changestudentsettings/";
                        sessionid = sharedPref.getInt("studentsessionid", -1);
                        String name = nametxt.getText().toString();
                        String phone = phonetxt.getText().toString();
                        String gphoneno = gphonetxt.getText().toString();
                        String rollno = rollnotxt.getText().toString();
                        String email = emailtxt.getText().toString();
                        String address = addresstxt.getText().toString();

                        data = "{\"sessionid\":\"" + sessionid + "\",\"mobile_no\":\"" + phone + "\",\"g_mobile_no\":\"" + gphoneno + "\",\"email\":\"" + email + "\",\"address\":\"" + address + "\",\"roll_no\":\"" + rollno + "\",\"name\":\"" + name + "\"}";

                    }

                    else if (roleid == 2) {
                        requesturl = baseurl+"changesettings/";
                        sessionid = sharedPref.getInt("authoritysessionid", -1);
                        String name = nametxt.getText().toString();
                        String phone = phonetxt.getText().toString();
                        //String gphoneno=gphonetxt.getText().toString();
                        //String rollno=rollnotxt.getText().toString();
                        String email = emailtxt.getText().toString();
                        String designation = "";
                        String branch = "";
                        //String address=addresstxt.getText().toString();

                        data = "{\"sessionid\":\"" + sessionid + "\",\"phone\":\"" + phone + "\",\"designation\":\"" + designation + "\",\"branch\":\"" + branch + "\",\"email\":\"" + email + "\",\"name\":\"" + name + "\"}";


                    }
                    Log.v("Request data is",""+data);
                    Thread th=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            wait=1;
                            OkHttpClient client = new OkHttpClient();

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
                                           // Toast.makeText(getActivity(), "client request not sent", Toast.LENGTH_LONG).show();
                                            Log.v("ON FAILURE", "NEW CALLBACK FAILURE");
                                            Snackbar sn=Snackbar.make(view.findViewById(R.id.coordinatorlayout),"Network Failure", Snackbar.LENGTH_LONG);
                                            sn.setActionTextColor(Color.MAGENTA);
                                            View sbView = sn.getView();
                                            sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.myblue));
                                            sn.show();

                                            wait = 0;
                                        }
                                    });

                                }

                                @Override
                                public void onResponse(Call call, final Response response) throws IOException {
                                    Log.v("PALLAVI", "ENTERED ON RESPONSE");
                                            /*    MainActivity.this.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Email.setText(null);
                                                        Password.setText(null);
                                                    }
                                                });*/
                                    try {
                                        s1 = response.body().string();
                                        wait=0;
                                    } catch (Exception e2) {
                                        wait=0;
                                        e2.printStackTrace();

                                    }
                                    Log.v("THE JSON OBJ IS CREATED", s1);

                                    //  final String s2 = s1.substring(s1.indexOf('[') + 1, s1.length() - 1);
                                    //s1.substring(s1.indexOf('[')+1, s1.length() - 1);

                                    try {
                                        jo = new JSONObject(s1);
                                        wait = 0;

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        wait = 0;
                                    }

                                   getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            try {

                                                if (roleid==1)
                                                {
                                                    String name = jo.getString("name");
                                                    String phoneno = jo.getString("mobile_no");
                                                    String email = jo.getString("email");
                                                    String address = jo.getString("address");
                                                    String rollno = jo.getString("roll_no");
                                                    String gphoneno=jo.getString("g_mobile_no");
                                                    nametxt.setText(name);
                                                    phonetxt.setText(phoneno);
                                                    gphonetxt.setText(gphoneno);
                                                    emailtxt.setText(email);
                                                    rollnotxt.setText(rollno);
                                                    addresstxt.setText(address);
                                                    nametxt.setEnabled(false);
                                                    phonetxt.setEnabled(false);
                                                    gphonetxt.setEnabled(false);
                                                    emailtxt.setEnabled(false);
                                                    addresstxt.setEnabled(false);
                                                    rollnotxt.setEnabled(false);

                                                }
                                                else if (roleid == 2) {
                                                    String name = jo.getString("name");
                                                    String phoneno = jo.getString("phone");
                                                    String email = jo.getString("email");
                                                    String branch = jo.getString("branch");
                                                    String designatioln = jo.getString("designation");
                                                    LinearLayout l3=(LinearLayout)view.findViewById(R.id.layout3);
                                                    l3.setVisibility(View.GONE);
                                                    LinearLayout l5=(LinearLayout)view.findViewById(R.id.layout5);
                                                    l5.setVisibility(View.GONE);
                                                    LinearLayout l6=(LinearLayout)view.findViewById(R.id.layout6);
                                                    l6.setVisibility(View.GONE);
                                                    nametxt.setText(name);
                                                    phonetxt.setText(phoneno);
                                                    // EditText gphonetxt=(EditText) findViewById(R.id.gphonenotxt);
                                                    //gphonetxt.setText(gphoneno);
                                                    emailtxt.setText(email);
                                                  //  save.setVisibility(View.GONE);
                                                    nametxt.setEnabled(false);
                                                    phonetxt.setEnabled(false);
                                                    //gphonetxt.setEnabled(false);
                                                    emailtxt.setEnabled(false);
                                                  //  addresstxt.setEnabled(false);
                                                 //   rollnotxt.setEnabled(false);
                                                    //EditText rollnotxt=(EditText) findViewById(R.id.rollnotxt);
                                                    //rollnotxt.setText(rollno);
                                                    //EditText addresstxt=(EditText) findViewById(R.id.addresstxt);
                                                    //addresstxt.setText(address);
                                                }
                                                save.setVisibility(View.GONE);
                                                Snackbar sn=Snackbar.make(view.findViewById(R.id.coordinatorlayout),"Record Updated Successfully", Snackbar.LENGTH_LONG);
                                                sn.setActionTextColor(Color.MAGENTA);
                                                View sbView = sn.getView();
                                                sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.myblue));
                                                sn.show();

                                                wait = 0;
                                            }
                                            catch (JSONException e1) {
                                                e1.printStackTrace();
                                                wait = 0;

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




        //final TopSheetDialog dialog = new TopSheetDialog(this);
        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch( v.getId() ) {
                    case R.id.changepassword:
                        {
                          //  mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            //dialog.setContentView(R.layout.sheet_content);
                            //dialog.setContentView();
                            //dialog.show();
                            boolean wrapInScrollView=true;
                            //sheet.setVisibility(View.INVISIBLE);
                       md=null;
                            md=new MaterialDialog.Builder(getActivity())
                                    .title("Change Password")
                                    .customView(R.layout.sheet_content, wrapInScrollView)
                                    .positiveText("Cancel")
                                    .show();

                            final Button sp=(Button)md.getCustomView().findViewById(R.id.savepassword);
                            if (wait==0)
                            {
                                sp.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //Toast.makeText(About.this,"You Want to change password",Toast.LENGTH_SHORT);
                                        wait=1;
                                        oldpasswordtxt=(EditText)md.getCustomView().findViewById(R.id.oldpasswordtxt);
                                        newpasswordtxt=(EditText)md.getCustomView().findViewById(R.id.newpasswordtxt);
                                        cnewpasswordtxt=(EditText)md.getCustomView().findViewById(R.id.cnewpasswordtxt);
                                        String oldpassword=oldpasswordtxt.getText().toString();
                                        String newpassword=newpasswordtxt.getText().toString();
                                        String cnewpassword=cnewpasswordtxt.getText().toString();
                                        Log.v("Old",""+oldpassword);
                                        Log.v("New",""+newpassword);
                                        Log.v("CNew",""+cnewpassword);
                                        if (roleid==1)
                                        {
                                            sessionid=sharedPref.getInt("studentsessionid",-1);
                                            requesturl = baseurl+"changestudentpassword/";
                                        }
                                        else if (roleid==2)
                                        {
                                            sessionid=sharedPref.getInt("authoritysessionid",-1);
                                            requesturl = baseurl+"changepassword/";

                                        }

                                        data = "{\"sessionid\":\"" + sessionid + "\",\"oldpassword\":\"" + oldpassword + "\",\"newpassword\":\"" + newpassword + "\",\"cnewpassword\":\"" + cnewpassword + "\"}";

                                        Thread th=new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                wait=1;
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
                                                               // Toast.makeText(getActivity(),"Some Network Failure Happened",Toast.LENGTH_SHORT);
                                                                Snackbar sn=Snackbar.make(view.findViewById(R.id.coordinatorlayout),"Network Failure", Snackbar.LENGTH_LONG);
                                                                sn.setActionTextColor(Color.MAGENTA);
                                                                View sbView = sn.getView();
                                                                sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.myblue));
                                                                sn.show();
                                                                md.dismiss();
                                                                wait=0;
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onResponse(Call call, Response response) throws IOException {

                                                        try {
                                                            s1 = response.body().string();
                                                            wait=0;
                                                        } catch (Exception e2) {
                                                            wait=0;
                                                            e2.printStackTrace();

                                                        }


                                                        getActivity().runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                //Toast.makeText(About.this,"Password Changed Successfully",Toast.LENGTH_SHORT);

                                                                Log.v("THE JSON OBJ IS CREATED", s1);

                                                                //  final String s2 = s1.substring(s1.indexOf('[') + 1, s1.length() - 1);
                                                                //s1.substring(s1.indexOf('[')+1, s1.length() - 1);

                                                                try {
                                                                    jo = new JSONObject(s1);
                                                                    wait = 0;

                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                    wait = 0;
                                                                }
                                                                try {

                                                                    int response=jo.getInt("return_status");
                                                                    if (response==1)
                                                                    {
                                                                        String message=jo.getString("message");
                                                                        //Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT);
                                                                        Snackbar sn=Snackbar.make(view.findViewById(R.id.coordinatorlayout),message, Snackbar.LENGTH_LONG);
                                                                        sn.setActionTextColor(Color.MAGENTA);
                                                                        View sbView = sn.getView();
                                                                        sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.myblue));
                                                                        sn.show();
                                                                        md.dismiss();
                                                                    }
                                                                    else if (response==2)
                                                                    {
                                                                        String message=jo.getString("message");
                                                                      //  Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT);
                                                                        Snackbar sn=Snackbar.make(view.findViewById(R.id.coordinatorlayout),message, Snackbar.LENGTH_LONG);
                                                                        sn.setActionTextColor(Color.MAGENTA);
                                                                        View sbView = sn.getView();
                                                                        sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.myblue));
                                                                        sn.show();
                                                                        md.dismiss();

                                                                    }

                                                                    wait = 0;
                                                                }
                                                                catch (JSONException e1) {
                                                                    e1.printStackTrace();

                                                                    wait = 0;

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

                            break;
                    }

                }
            }
        });
       // setSupportActionBar(t);
    return view;
    }

}
