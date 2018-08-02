package com.example.pallavi.norag;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.ViewGroupCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.zip.Inflater;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.widget.Toast.LENGTH_LONG;
import static com.example.pallavi.norag.Utils.context;


public class SensorService extends Service implements SensorEventListener, LocationListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity
    JSONObject jsonObject;
    Student main;
    String jsonresponse, data;
    int wait = 0;
    MaterialDialog m;
    boolean active = false;
    int roleid, sessionid;
    SharedPreferences sp;
    String requesturl,baseurl;
    LocationManager locationManager;
    String mprovider;
    Location location;
    Float latitude,longitude;
    String additionaltext;

    @Override
    public IBinder onBind(Intent intent) {
        active = false;
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.d("MYTAG", intent.getAction()+"");
try {
    if (intent.getAction() != null && intent.getAction().equals("STOP")) {

        stopSelf();
        Intent intent1 = new Intent(this, shakeit.class);
        intent1.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), shakeit.class));//.getAppWidgetI‌​ds(new ComponentName(getApplication(), shakeit.class));
        intent1.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent1);
    } else {
        Intent intent1 = new Intent(this, shakeit.class);
        intent1.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), shakeit.class));//.getAppWidgetI‌​ds(new ComponentName(getApplication(), shakeit.class));
        intent1.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent1);
    }
}
catch (Exception e)
{
    e.printStackTrace();
}
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer,
                SensorManager.SENSOR_DELAY_UI, new Handler());

        active = false;
        try{
            additionaltext=intent.getStringExtra("helptext");
            Log.v("Sensor additional Text",additionaltext);
        }
        catch (Exception e)
        {
            additionaltext="";
            Log.v("Sensor additional Text",additionaltext);
        }


        //additionaltext=intent.getExtras().getString("helptext");


        return START_STICKY;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        mAccelLast = mAccelCurrent;
        mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
        float delta = mAccelCurrent - mAccelLast;
        mAccel = mAccel * 0.9f + delta; // perform low-cut filter
        baseurl=getString(R.string.base_url);


        if (mAccel > 11) {
            //showNotification();
            active=true;
            Log.v("Check","Sensor is working fine");
    /*        if (active==true)
            {
                final AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle("Title")
                        .setMessage("Are you sure?")

                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.v("Sensor","Positive Button is clicked");

                            }
                        })

                        .create();

                alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                alertDialog.show();
            }
*/
            boolean wrapInScrollView = true;

            sp= PreferenceManager.getDefaultSharedPreferences(SensorService.this);
            roleid=sp.getInt("role",-1);
            if (roleid==1)
            {
                requesturl=baseurl+"addcomplain/";
                //requesturl="http://192.168.43.132:8000/index/addcomplain/";
                sessionid=sp.getInt("studentsessionid",-1);
            }
            //float latitude=0.1;
            //additionaltext=sp.getString("helptext","");
            //Intent newintent = new Intent(getApplicationContext(), SensorService.class);
            //additionaltext=newintent.getExtras().getString("helptext");
            Log.v("Sensor additional Text",additionaltext);


            String attachment="No attachment";
            String text="Help Me Authority "+additionaltext;
            //latitude= Float.parseFloat(String.valueOf(location.getLatitude()));
            //longitude=Float.parseFloat(String.valueOf(location.getLongitude()));
            Log.v("Sensor","Dialogue Box is working fine");
            //String message="{\"text\":"+ text+",\"attachment\":"+attachment +"}";
            //sp = PreferenceManager.getDefaultSharedPreferences(Introduction.this);
            sp = PreferenceManager.getDefaultSharedPreferences(SensorService.this);
            latitude=sp.getFloat("sourcelatitude",0);
            longitude=sp.getFloat("sourcelongitude",0);
            Log.v("Location Service",latitude.toString()+" "+longitude.toString());

            data = "{\"text\":\"" + text + "\",\"attachment\":\"" + attachment + "\",\"latitude\":\"" + latitude + "\",\"longitude\":\"" + longitude + "\",\"sessionid\":\"" + sessionid + "\"}";
            if (wait==0)
            {
                Log.v("MYTAG","Thread is getting called");
                Thread th=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("Thread",data);
                        wait=1;
                        OkHttpClient client=new OkHttpClient();
                        Request request=new Request.Builder()
                                .url(requesturl)
                                .post(RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),data))
                                .build();


                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                                Log.v("Check Error","Failure");
                                wait=0;
                            //    Intent newIntent = new Intent(getApplicationContext(), SensorService.class);
                               // if ((newIntent.getAction()!=null || newIntent.getAction()==null) && (newIntent.getAction().equals("STOP") || newIntent.getAction().equals("START"))) {
                                   // Student st = new Student();
                                   // Toast.makeText(st.getActivity(),"Network Failure", Toast.LENGTH_LONG).show();
                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),
                                                "Network Failure",
                                                Toast.LENGTH_LONG).show();
                                    }
                                });
                                //Log.v("MYTAG","Service failed due to bad network");
                                //}
                               // else
                                //    {
                              /*      Student st = new Student();
                                    //Snackbar sn=Snackbar.make(st.getActivity().findViewById(R.id.coordinatorlayout),"Network Failure", Snackbar.LENGTH_LONG);
                                    Snackbar sn = Snackbar.make(Student.layout, "Network Failure", Snackbar.LENGTH_LONG);
                                    sn.setActionTextColor(Color.MAGENTA);
                                    View sbView = sn.getView();
                                    sbView.setBackgroundColor(ContextCompat.getColor(st.getActivity(), R.color.myblue));
                                    sn.show();
                                */
                               // }

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

                                try {
                                    final String successmessage=jsonObject.getString("message");
                                    int returnstatus=jsonObject.getInt("return_status");
                                    Log.v("The success message is ",""+successmessage);
                                    //Intent newIntent = new Intent(getApplicationContext(), SensorService.class);
                              //      if((newIntent.getAction()!=null || newIntent.getAction()==null) && (newIntent.getAction().equals("STOP") || newIntent.getAction().equals("START")))
                                //    {
                                       // Student st = new Student();
                                      //  Toast.makeText(st.getActivity(),successmessage, Toast.LENGTH_LONG).show();
                                    Handler handler = new Handler(Looper.getMainLooper());
                                    handler.post(new Runnable() {

                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(),
                                                    successmessage,
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    });
                                        //Log.v("MYTAG","Service failed due to bad network");
                                  //  }

                                 //   else
                                   //     {
                                    /*    Snackbar sn = Snackbar.make(Student.layout, successmessage, Snackbar.LENGTH_LONG);
                                        sn.setActionTextColor(Color.MAGENTA);
                                        View sbView = sn.getView();
                                        sbView.setBackgroundColor(ContextCompat.getColor(getApplication(), R.color.myblue));
                                        sn.show();
                                    */
                                    wait=0;
                                    //}
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                          wait=0;
                                }

                                //  FirebaseMessaging.getInstance().subscribeToTopic("student");
                                //  FirebaseMessaging.getInstance().subscribeToTopic("authority");

                            }
                        });
                    }
                });
                th.start();
            }


        }

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}


    /**
     * show notification when Accel is more then the given int.
     */
  /*  private void showNotification() {
        final NotificationManager mgr = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder note = new NotificationCompat.Builder(this);
        note.setContentTitle("Device Accelerometer Notification");
        note.setTicker("New Message Alert!");
        note.setAutoCancel(true);
        // to set default sound/light/vibrate or all
        note.setDefaults(Notification.DEFAULT_ALL);
        // Icon to be set on Notification
        note.setSmallIcon(R.drawable.ic_launcher);
        // This pending intent will open after notification click
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this,
                MainActivity.class), 0);
        // set pending intent to notification builder
        note.setContentIntent(pi);
        mgr.notify(101, note.build());
    }
*/


