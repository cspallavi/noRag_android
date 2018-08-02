package com.example.pallavi.norag;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class Authority extends AppCompatActivity {
    JSONObject jsonObject;
    String jsonresponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority);


        Button abtn=(Button) findViewById(R.id.abutton);
        TextView atv=(TextView) findViewById(R.id.atextView);
        FirebaseMessaging.getInstance().subscribeToTopic("authority");
        FirebaseMessaging.getInstance().subscribeToTopic("student");

    /*    abtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseMessaging.getInstance().subscribeToTopic("authority");
                //okhttp client
                String notification="{\"body\":\"This message is from authority \",\"title\":\" Help from Authority\"\"}";
                final String message="{\"topic\":\"student\",\"notification\":"+notification +"\"}";
                Thread th=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client=new OkHttpClient();
                        Request request=new Request.Builder()
                                .header("Authorization", "AIzaSyDHmzuN5sv7Bl0yQNI3OyQ5_XmauGsRjYQ")
                                .url("https://fcm.googleapis.com/v1/projects/norag-1164e/messages:send HTTP/1.1")
                                .post(RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),message))
                                .build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                                Authority.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Authority.this,"Your Message didnt went",Toast.LENGTH_SHORT);

                                    }
                                });

                            }

                            @Override
                            public void onResponse(Call call,Response response) throws IOException {
                                jsonresponse =response.body().string();

                                Log.v("The new jsonresponse is", jsonresponse);
                                Authority.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {



                                        //JSONObject jsonresponse;
                                        try {
                                            JSONObject jsonObject=new JSONObject(jsonresponse);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        try {
                                            String successmessage=jsonObject.getString("message_id");


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                            }
                        });
                    }
                });
                th.start();
            }
        });*/
    }



}
