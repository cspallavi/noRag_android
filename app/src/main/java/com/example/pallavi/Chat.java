package com.example.pallavi.norag;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.widget.LinearLayout.VERTICAL;




/**
 * Created by Pallavi on 28/06/2017aa.
 */

public class Chat extends AppCompatActivity {

    Toolbar t;String data;String s1;JSONObject jo;JSONArray array;ImageView send1=null;int fid;int response1;String data1;
    String s;int sessionid;int mid,senderid;String message,sender_name;String image;String username;String image1;int wait=0;int count_messages;
    int c;
    //PchatAdapter pca;
 /*protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.private_chats);*/
     /*tv=(TextView)findViewById(R.id.tv);

     s = getIntent().getStringExtra("senderid");
         Log.v("HIIIIIII",s);
    SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(PrivateChats.this);
     sessionid=sp.getInt("sessionid",-1);
    try{
        tv.setText(s);
     }
     catch (Exception e)
     {
         e.printStackTrace();
     }

     tv.setText(s+"");*/
    private List<Get_set_pchat> pchatList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DivideItemDecoration.PchatAdapter mAdapter;String message1;
    EditText get_message1;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Log.v("Pallavi","entered chat");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_main_for_pchat);
        t=(Toolbar)findViewById(R.id.tb1);
        // imageView1 = (CircleImageView) findViewById(R.id.imageView);
        setSupportActionBar(t);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_for_pchat);

        //imageView = (ImageView) findViewById(R.id.imageView);
        //Picasso.with(MainActivity.this).load(R.drawable.talkative).into(imageView);
        mAdapter = new DivideItemDecoration.PchatAdapter(pchatList,Chat.this);
        //RecyclerView.LayoutManager l = new LinearLayoutManager(getApplicationContext());
        //recyclerView.setLayoutManager(mLayoutManager);



        send1=(ImageView) findViewById(R.id.send);
        get_message1=(EditText)findViewById(R.id.get_message);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(Chat.this,VERTICAL,true));
// set the adapter
        recyclerView.setAdapter(mAdapter);
        //recyclerView.smoothScrollToPosition();
        ////////////////
        ////////////////
        ///////////////
        //RecyclerView.LayoutManager r=recyclerView.getLayoutManager();
        // refresh();
        //mAdapter.scroll_to_bottom(r);
        preparePchatData();
        getPchatData();
        pollingPchatData();

     /*   recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(Chat.this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Toast.makeText(Chat.this,"HIIIIIIIIIIIIIII"+senderid,Toast.LENGTH_LONG).show();
                        //Intent intent1 = new Intent("com.example.pallavi.chat_app.PrivateChats");
                        // intent.putExtra("SESSIONID", sessionid);
                       // Chat.this.startActivity(intent1);
                        // do whatever
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );*/

        //recyclerView.setAdapter(mAdapter);


    }
    /*public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.private_chat_o_menu,menu);
        return true;

    }*/

    //@Override
   /* public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==block)
        {
            MaterialDialog dialog = new MaterialDialog.Builder(this)
                    .title(R.string.title)
                    .content(R.string.content)
                    .positiveText(R.string.agree)
                    .negativeText(R.string.negative)

                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            // TODO
                            Log.v("PALLAVIIIIIII","BEFORE CALLING BLOCK");
                            //Toast.makeText(PrivateChats.this,"BEFRE CALLING THE FUNCTION BLOCK",Toast.LENGTH_LONG).show();
                            block();
                        }
                    })
                    .show();

        }
        if (item.getItemId()==about)
        {
            Intent intent=new Intent("com.example.pallavi.chat_app.About_more");
            intent.putExtra("userid",s);
            intent.putExtra("Response",1);
            startActivity(intent);
        }
        if(item.getItemId()==myProfile)
        {

            Intent in=new Intent("com.example.pallavi.chat_app.SetMyProfile");
            startActivity(in);
        }



        return true;

    }

    /* public boolean onCreateOptionsMenu(Menu menu){
            MenuInflater inflater=getMenuInflater();
            inflater.inflate(R.menu.overflow_menu,menu);
            return true;

        }
        public boolean onOptionsItemSelected(MenuItem item){
            if(item.getItemId()==friendRequests)
            {
                Intent intent=new Intent("com.example.pallavi.chat_app.FriendRequests");
                startActivity(intent);
            }
            return true;
        }
    */
 /*   void block()
    {
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(PrivateChats.this);
        sessionid=sp.getInt("sessionid",-1);
        s = getIntent().getStringExtra("senderid");

        Thread th1=new Thread(new Runnable() {
            @Override
            public void run() {
                Log.v("PALLAVIIIIIIIII","YOU ENTERED NEW THREAD AND FID IS "+fid);
                data1="{\"btn\":"+3+",\"fid\":"+fid+",\"sessionid\":"+sessionid+",\"viewuserid\":"+s+"}";
                Log.v("The data to be sent is",data1);
                OkHttpClient client1=new OkHttpClient();
                Request request1=new Request.Builder()
                        .url("http://www.palzone.ml/service_pallavi/friend_request_accept_or_deny.php")
                        .post(RequestBody.create(okhttp3.MediaType.parse("application/json;charset=utf-8"),data1))
                        .build();
                client1.newCall(request1).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        PrivateChats.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(PrivateChats.this,"Client Request not sent",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    String s2;
                    @Override
                    public void onResponse(Call call1, Response response11) throws IOException {
                        Log.v("ENTERED RESPONSE","   PALLAVI     " );
                        try {
                            s1 = response11.body().string();
                            s2 = s1.substring(s1.indexOf('[') + 1, s1.length() - 1);
                        }
                        catch(Exception e2)
                        {
                            e2.printStackTrace();
                        }
                        Log.v("PALLAVI  RESPONSE IS ",s1);
                        try {
                            jo = new JSONObject(s2);
                        }
                        catch(JSONException e3)
                        {
                            e3.printStackTrace();
                        }

                        PrivateChats.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    response1=jo.getInt("response");
                                    Log.v("THE RESPONSE VALUE IS..",response1+"");
                                    if(response1==1)
                                    {
                                        MaterialDialog dialog1 = new MaterialDialog.Builder(PrivateChats.this)

                                                .content("Blocking successfull!!!!")
                                                .positiveText(R.string.agree)

                                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                    @Override
                                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                        // TODO
                                                        Intent intent=new Intent("com.example.pallavi.chat_app.FriendList");
                                                        startActivity(intent);
                                                        //Toast.makeText(PrivateChats.this,"blocking successful",Toast.LENGTH_LONG).show();
                                                    }
                                                })
                                                .show();

                                    }
                                }
                                catch(JSONException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }
                });
            }
        });
        th1.start();

    }*/
    private void preparePchatData() {


        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(Chat.this);
        sessionid=sp.getInt("sessionid",-1);
        s = getIntent().getStringExtra("senderid");
        //sessionid=1;
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                Log.v("PALLAVIIIIII","ENTERED NEW THREAD");
                data="{\"sessionid\":"+sessionid+",\"view_userid\":"+s+"}";
                Log.v("THE REQUESTED DATA IS :",data);
                OkHttpClient client=new OkHttpClient();
                Request request=new Request.Builder()
                        .url("http://www.palzone.ml/service_pallavi/load_message.php")
                        .post(RequestBody.create(okhttp3.MediaType.parse("application/json;charset=utf-8"),data))
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Chat.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Chat.this,"CLIENT REQUEST NOT SENT ",Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call,final Response response) throws IOException {
                        Log.v("PALLAVI", "ENTERED ON RESPONSE");
                        try {
                            //s0=response.body();
                            s1 = response.body().string();
                        } catch (Exception e2) {
                            e2.printStackTrace();

                        }
                        Log.v("THE JSON OBJ IS CREATED", s1);

                        final String s2 = s1.substring(s1.indexOf('[') + 1, s1.length() - 1);


                        Chat.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    array=new JSONArray(s1);
                                    c=array.length();
                                    for(int i=0;i<array.length();i++) {
                                        jo=array.getJSONObject(i);
                                        mid = Integer.parseInt(String.valueOf(jo.getInt("m_id")));
                                        senderid=Integer.parseInt(String.valueOf(jo.getInt("senderid")));
                                        message=String.valueOf(jo.getString("message"));
                                        sender_name=String.valueOf(jo.getString("sender_name"));
                                        image=String.valueOf(jo.getString("image_path"));
                                        fid=Integer.parseInt(String.valueOf(jo.getInt("fid")));

                                        Get_set_pchat pchat =new Get_set_pchat(sender_name,message,senderid,image);
                                        pchatList.add(pchat);
                                        //scroll.fullScroll(View.FOCUS_DOWN);
                                        // Log.v("PALLAVIIIIII","id"+senderid);

                                    }
                                    mAdapter.notifyDataSetChanged();

                                }
                                catch (JSONException e1) {
                                    e1.printStackTrace();


                                }
                            }
                        });


                    }

                });
                //refresh();
            }
        });
        t.start();

    }

    void getPchatData(){
        Log.v("PALLAVIIIIIIIII","enter getPchatData");
        try{
            send1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.v("pallaviiiiiiiiii", "send button clicked");
                    message1 = get_message1.getText().toString();
                    if ((wait == 0)&&((message1!=null)&&(message1!=" ") )){
                        get_message1.setText("");
                        wait=1;

                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Chat.this);
                        sessionid = sp.getInt("sessionid", -1);
                        s = getIntent().getStringExtra("senderid");
                        //sessionid=1;
                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Log.v("PALLAVIIIIII", "ENTERED NEW THREAD");
                                data = "{\"sessionid\":" + sessionid + ",\"userid2\":" + s + ",\"message\":" + "\"" + message1 + "\"" + ",\"fid\":"+fid+"}";
                                Log.v("THE REQUESTED DATA IS :", data);
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder()
                                        .url("http://www.palzone.ml/service_pallavi/create_conversation.php")
                                        .post(RequestBody.create(okhttp3.MediaType.parse("application/json;charset=utf-8"), data))
                                        .build();
                                client.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        wait=0;
                                        Chat.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(Chat.this, "CLIENT REQUEST NOT SENT ", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onResponse(Call call, final Response response) throws IOException {
                                        Log.v("PALLAVI", "ENTERED ON RESPONSE");
                                        try {
                                            //s0=response.body();
                                            s1 = response.body().string();
                                        } catch (Exception e2) {
                                            e2.printStackTrace();

                                        }
                                        Log.v("THE JSON OBJ IS CREATED", s1);

                                        final String s2 = s1.substring(s1.indexOf('[') + 1, s1.length() - 1);


                                        try {
                                            jo = new JSONObject(s2);
                                            int response1 = Integer.parseInt(String.valueOf(jo.getInt("response")));
                                            if(response1 ==3)
                                            {
                                                MaterialDialog dialog1 = new MaterialDialog.Builder(Chat.this)

                                                        .content("This person is not in your friend list.You cannot send message to him/her unless you send a friend request and that is accepted by him/her!")
                                                        .positiveText("agree")

                                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                            @Override
                                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                                // TODO
                                                                Intent intent=new Intent("com.example.pallavi.chat_app.FriendList");
                                                                startActivity(intent);
                                                                //Toast.makeText(PrivateChats.this,"blocking successful",Toast.LENGTH_LONG).show();
                                                            }
                                                        })
                                                        .show();

                                            }
                                            if (response1 == 1){
                                                username = String.valueOf(jo.getString("username"));
                                                image1 = String.valueOf(jo.getString("image_path"));
                                                Log.v("VALUE OF RESPONSE IS", "" + response1);

                                                Chat.this.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(Chat.this, "message inserted", Toast.LENGTH_LONG).show();
                                                        recyclerView.setLayoutManager(new LinearLayoutManager(Chat.this,VERTICAL,true));
                                                        //preparePchatData();
                                                    }
                                                });

                                       /* PrivateChats.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                preparePchatData();
                                            }
                                        });*/

                                                Chat.this.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Get_set_pchat pchat1 = new Get_set_pchat(username, message1, sessionid, image1);
                                                        //pchatList.add(pchat);
                                                        mAdapter.addelement(pchat1);
                                                        //mAdapter.notifyDataSetChanged();

                                                        wait = 0;

                                                    }

                                                });
                                            }
                                            wait = 0;
                                        } catch (JSONException e) {
                                            wait=0;
                                            Log.v("IN EXCEPTION", e.toString());
                                            e.printStackTrace();
                                        }


                                    }
                                });

                            }
                        });
                        t.start();


                    }
                    else{
                        Toast.makeText(Chat.this,"enter some text before clicking send",Toast.LENGTH_LONG).show();
                    }
                }
            });
            /*Get_set_pchat pchat =new Get_set_pchat(username,message1,sessionid,image1);
            pchatList.add(pchat);
            mAdapter.notifyDataSetChanged();*/

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    private void pollingPchatData() {


        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(Chat.this);
        sessionid=sp.getInt("sessionid",-1);
        s = getIntent().getStringExtra("senderid");
        //sessionid=1;
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                Log.v("PALLAVIIIIII","ENTERED NEW THREAD");
                data="{\"sessionid\":"+sessionid+",\"view_userid\":"+s+"}";
                Log.v("THE REQUESTED DATA IS :",data);
                OkHttpClient client=new OkHttpClient();
                Request request=new Request.Builder()
                        .url("http://www.palzone.ml/service_pallavi/polling.php")
                        .post(RequestBody.create(okhttp3.MediaType.parse("application/json;charset=utf-8"),data))
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Chat.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Chat.this,"CLIENT REQUEST NOT SENT ",Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call,final Response response) throws IOException {
                        Log.v("PALLAVI", "ENTERED ON RESPONSE");
                        try {
                            //s0=response.body();
                            s1 = response.body().string();
                        } catch (Exception e2) {
                            e2.printStackTrace();

                        }


                        final String s2 = s1.substring(s1.indexOf('[') + 1, s1.length() - 1);
                        Log.v("THE JSON OBJ IS CREATED", s2);


                        Chat.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    jo=new JSONObject(s2);
                                    count_messages = Integer.parseInt(String.valueOf(jo.getInt("count_messages")));
                                    //Toast.makeText(PrivateChats.this,"THE PREVIOUS COUNT WASSSSSSSS"+c+"YOUR NEW COUNT ISS"+count_messages,Toast.LENGTH_LONG).show();

                                    //  Log.v("THE PREVIOUS COUNT WASSSSSSSS"+c,"YOUR NEW COUNT ISS"+count_messages);
                                    if(count_messages>c)
                                    {
                                        preparePchatData();
                                        return;
                                    }




                                }
                                catch (JSONException e1) {
                                    e1.printStackTrace();


                                }
                            }
                        });


                    }
                });
                //refresh();
            }
        });
        t.start();

    }
    void refresh()
    {
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                pollingPchatData();
            }
        },2000);
    }


}