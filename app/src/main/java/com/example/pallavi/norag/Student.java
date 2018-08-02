package com.example.pallavi.norag;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v13.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.pallavi.norag.Manifest.*;

public class Student extends Fragment{
    String jsonresponse, token;
    JSONObject jsonObject;
    LocationManager locationManager;
    String mprovider,text,additionaltext;
    static ViewGroup layout ;  //define it globally
    MaterialBetterSpinner materialDesignSpinner;
    EditText complaintxt;
    Button complainsend;
    SharedPreferences sp;
    int wait=0;
    int roleid,sessionid;
    String requesturl,baseurl,data;
    Float latitude,longitude;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.activity_student_, container, false);
        layout = (ViewGroup) view.findViewById(R.id.coordinatorlayout);
        Log.v("Check Error", "In the student activity");
        //Toast.makeText(getActivity(), "Shake Here Hard", Toast.LENGTH_SHORT);
        text="";
        String[] SPINNERLIST = {"I am new MPH", "I am near ITRC", "I am near EC Department", "I am near ATM"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
         materialDesignSpinner = (MaterialBetterSpinner)view.
                findViewById(R.id.spinnerstatus);
        materialDesignSpinner.setAdapter(arrayAdapter);
        //materialDesignSpinner.setOnClickListener(new AdapterView.OnItemClickListener());

        materialDesignSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.v("Student","Test");
               text=String.valueOf(materialDesignSpinner.getAdapter().getItem(position));
                Log.v("Student",text);
                if (!text.equalsIgnoreCase(""))
                {
                    Intent newintent = new Intent(getActivity(), SensorService.class);
                    newintent.putExtra("helptext", text);
                    getActivity().startService(newintent);

                }
            }

        });

        baseurl=getString(R.string.base_url);
        sp= PreferenceManager.getDefaultSharedPreferences(getActivity());
        roleid=sp.getInt("role",-1);
        //sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        latitude=sp.getFloat("sourcelatitude",0);
        longitude=sp.getFloat("sourcelongitude",0);
        if (roleid==1)
        {
            requesturl=baseurl+"addcomplain/";
            //requesturl="http://192.168.43.132:8000/index/addcomplain/";
            sessionid=sp.getInt("studentsessionid",-1);
        }
        complaintxt=(EditText)view.findViewById(R.id.complainbox);
         complainsend=(Button)view.findViewById(R.id.complainsend);

        complainsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   additionaltext = complaintxt.getText().toString();


                   if(additionaltext.equalsIgnoreCase("")) {
                       Student st = new Student();
                       Snackbar sn = Snackbar.make(Student.layout, "Please enter something in complain box or Simply Shake it", Snackbar.LENGTH_LONG);
                       sn.setActionTextColor(Color.MAGENTA);
                       View sbView = sn.getView();
                       sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.myblue));
                       sn.show();
                       return;
                   }


                String attachment="No attachment";
                String text1=additionaltext.replaceAll("\\n","");;

                Log.v("text",text1);


                Log.v("Location Service",latitude.toString()+" "+longitude.toString());

                data = "{\"text\":\"" + text1 + "\",\"attachment\":\"" + attachment + "\",\"latitude\":\"" + latitude + "\",\"longitude\":\"" + longitude + "\",\"sessionid\":\"" + sessionid + "\"}";

                    Thread th=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("Thread",data);

                            OkHttpClient client=new OkHttpClient();
                            Request request=new Request.Builder()
                                    .url(requesturl)
                                    .post(RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),data))
                                    .build();


                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                    Log.v("Check Error","Failure");


                                    //Snackbar sn=Snackbar.make(st.getActivity().findViewById(R.id.coordinatorlayout),"Network Failure", Snackbar.LENGTH_LONG);
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            Snackbar sn=Snackbar.make(view.findViewById(R.id.coordinatorlayout),"Network Failure", Snackbar.LENGTH_LONG);
                                            sn.setActionTextColor(Color.MAGENTA);
                                            View sbView = sn.getView();
                                            sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.myblue));
                                            sn.show();
                                        }
                                    });




                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    jsonresponse =response.body().string();
                                    Log.v("Check Error","Code Works till here in on Response");
                                    Log.v("The new jsonresponse is", jsonresponse);
                                    try {
                                        jsonObject=new JSONObject(jsonresponse);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            String successmessage=jsonObject.getString("message");
                                            int returnstatus=jsonObject.getInt("return_status");
                                            Log.v("The success message is ",""+successmessage);

                                            complaintxt.setText("");
                                            Snackbar sn=Snackbar.make(view.findViewById(R.id.coordinatorlayout),successmessage, Snackbar.LENGTH_LONG);
                                            sn.setActionTextColor(Color.MAGENTA);
                                            View sbView = sn.getView();
                                            sbView.setBackgroundColor(ContextCompat.getColor(getActivity().getApplication(), R.color.myblue));
                                            sn.show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();

                                        }
                                    }
                                });


                                    //  FirebaseMessaging.getInstance().subscribeToTopic("student");
                                    //  FirebaseMessaging.getInstance().subscribeToTopic("authority");

                                }
                            });
                        }
                    });
                    th.start();

            }
        });

      /*  materialDesignSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("Student","Test");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
      */
       // Log.v("Student",text);
        Intent newintent = new Intent(getActivity(), SensorService.class);
        newintent.putExtra("helptext",text);
        getActivity().startService(newintent);




return  view;

    }


    public void onBackPressed() {

        onBackPressed();
        Intent intent = new Intent(getActivity(), SensorService.class);
        //Start Service
        getActivity().stopService(intent);
    }





}
