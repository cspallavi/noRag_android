package com.example.pallavi.norag;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register extends AppCompatActivity {
    Button bt3;
    EditText Rollno;
    EditText Email;
    EditText Password;
    EditText Cpassword;
    String data,s1,e,r,p,cp,baseurl,requestedurl;
    JSONObject jo;
    MaterialDialog mate;
    int flag=0;int c1,c2,ind1,ind2;
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
       // Slide s=new Slide();
        //s.setDuration(500);
        //getWindow().setEnterTransition(s);
        //TransitionInflater tf=TransitionInflater.from(this);
        //Transition t=tf.inflateTransition(R.transition.activitytransition);
        //getWindow().setExitTransition(t);
        CardView cv =(CardView)findViewById(R.id.cav);
        Animation bottomToTop = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom);
        cv.startAnimation(bottomToTop);
        baseurl=getString(R.string.base_url);
        bt3=(Button)findViewById(R.id.btn_signup1);
        mate=new MaterialDialog.Builder(Register.this)
                .title("No-Rag")
                .content("Please Wait While You Are Redirected")
                .progress(true, 0)
                .build();
        // Email = (EditText) findViewById(R.id.email);
      //  register();
        Toast.makeText(Register.this,"entered Register",Toast.LENGTH_LONG).show();
        bt3.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(Register.this, "clicked signup", Toast.LENGTH_LONG).show();

                            Rollno = (EditText) findViewById(R.id.rollno);
                            Email = (EditText) findViewById(R.id.email);
                            Password = (EditText) findViewById(R.id.input_password);
                            Cpassword = (EditText) findViewById(R.id.input_cpassword1);
                            r = Rollno.getText().toString();
                            e = Email.getText().toString();
                            p = Password.getText().toString();
                            cp = Cpassword.getText().toString();

                        if (r.equalsIgnoreCase("") || p.equalsIgnoreCase("") || e.equalsIgnoreCase("") ||cp.equalsIgnoreCase(""))
                        {
                            Toast.makeText(Register.this,"Please provide all the details",Toast.LENGTH_LONG).show();
                            return;
                        }

                        else
                            mate.show();

                        boolean b= Pattern.matches("[a-zA-Z0-9+._%-+]{1,256}" +
                                "@" +
                                "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" +
                                "(" +
                                "." +
                                "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" +
                                ")+",e);
                        // if(b==true)
                        //  Toast.makeText(MainActivity.this, "matches", Toast.LENGTH_LONG).show();

                        if(b==false)
                        {
                            Email.setError("email entered does not follow the correct format");
                        }

                        else
                        {
                            c1=0;c2=0;
                            Toast.makeText(Register.this, "Roll No is" + r, Toast.LENGTH_LONG).show();
                            requestedurl=baseurl+"studentregistration/";
                            Thread t = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    data = "{\"email\":\"" + e + "\",\"password\":\"" + p + "\",\"cpassword\":\"" + cp + "\",\"roll_no\":\"" + r + "\"}";
                                    Log.v("THE REQUESTED DATA IS ", data);
                                    OkHttpClient client = new OkHttpClient();
                                    Request request = new Request.Builder()
                                            .url(requestedurl)
                                            .post(RequestBody.create(okhttp3.MediaType.parse("application/json;charset=utf-8"), data))
                                            .build();
                                    //header = {'X-CSRFToken': csrftoken}
                                    //cookies = {'csrftoken': csrftoken}
                                    client.newCall(request).enqueue(new Callback() {
                                        @Override
                                        public void onFailure(Call call, final IOException e) {
                                            Register.this.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //Log.v("ON FAILURE ", "CLIENT REQUEST NOT SENT ");
                                                  //  Toast.makeText(Register.this, "Network Failure", Toast.LENGTH_LONG).show();
                                                    mate.dismiss();
                                                    Snackbar sn=Snackbar.make(findViewById(R.id.coordinatorlayout),"Network Failure", Snackbar.LENGTH_LONG);
                                                    sn.setActionTextColor(Color.MAGENTA);
                                                    View sbView = sn.getView();
                                                    sbView.setBackgroundColor(ContextCompat.getColor(Register.this, R.color.myblue));
                                                    sn.show();
                                                    e.printStackTrace();
                                                }
                                            });

                                        }

                                        @Override
                                        public void onResponse(Call call, final Response response) throws IOException {
                                            s1 = response.body().string();
                                            Log.v("Message","The original response body is "+s1);
                                            final String s2;
                                            s2 = s1.substring(s1.indexOf('[') + 1, s1.length() - 1);
                                            Log.v("Message","In the response function "+s2);
                                            try {
                                                jo = new JSONObject(s2);
                                            } catch (JSONException e1) {
                                                e1.printStackTrace();
                                            }

                                            Log.v("The response is:", s1);
                                            Register.this.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        int statusResponse = Integer.parseInt(String.valueOf(jo.getInt("response_data")));
                                                        if (statusResponse == 1)
                                                        //    Toast.makeText(Register.this, "Registration successful", Toast.LENGTH_LONG).show();
                                                        {
                                                            mate.dismiss();
                                                            Snackbar sn=Snackbar.make(findViewById(R.id.coordinatorlayout),"Registration done successfully", Snackbar.LENGTH_LONG);
                                                            sn.setActionTextColor(Color.MAGENTA);
                                                            View sbView = sn.getView();
                                                            sbView.setBackgroundColor(ContextCompat.getColor(Register.this, R.color.myblue));
                                                            sn.show();
                                                            new Handler().postDelayed(new Runnable(){
                                                                @Override
                                                                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                                                                    Intent loginpage = new Intent(Register.this,Login.class);
                                                                   // ActivityOptionsCompat compat=ActivityOptionsCompat.makeSceneTransitionAnimation(Register.this,null);
                                                                    //Register.this.startActivity(registerintent,compat.toBundle());
                                                                    Register.this.startActivity(loginpage);
                                                                    Register.this.finish();
                                                                }
                                                            }, SPLASH_DISPLAY_LENGTH);
                                                        }
                                                        else if (statusResponse == 2)
                                                           // Toast.makeText(Register.this, "password not matched", Toast.LENGTH_SHORT).show();
                                                        {
                                                            mate.dismiss();
                                                            Snackbar sn=Snackbar.make(findViewById(R.id.coordinatorlayout),"Password does not match", Snackbar.LENGTH_LONG);
                                                            sn.setActionTextColor(Color.MAGENTA);
                                                            View sbView = sn.getView();
                                                            sbView.setBackgroundColor(ContextCompat.getColor(Register.this, R.color.myblue));
                                                            sn.show();
                                                        }


                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });

                                        }
                                    });


                                }
                            });
                            t.start();
                        }

                    }
                }

        );
    }
    /*  public void setInputTypeToEmail(View view){

          Email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
      }*/
   // public void register(){

    //}

}
