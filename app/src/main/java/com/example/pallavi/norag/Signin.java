package com.example.pallavi.norag;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.security.AuthProvider;

public class Signin extends AppCompatActivity {

    private RadioGroup radioGroup;
    private Button signinbtn;
    private RadioButton radioButton;
    private int roleid,role;
    String trole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
       // Slide s=new Slide();
       // s.setDuration(500);
        //getWindow().setEnterTransition(s);

        //getWindow().setExitTransition(s);
        //TransitionInflater tf=TransitionInflater.from(this);
        //Transition t=tf.inflateTransition(R.transition.activitytransition);
        //getWindow().setExitTransition(t);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(Signin.this);
        role = sharedPref.getInt("role", -1);
         if (role==-1)
        {
            radioGroup = (RadioGroup) findViewById(R.id.radio);
            //TextView textview= (TextView) findViewById(R.id.norag);
            Animation bottomToTop = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom);
            radioGroup.startAnimation(bottomToTop);
            signinbtn = (Button) findViewById(R.id.signin);

            signinbtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    // get selected radio button from radioGroup
                    int selectedId = radioGroup.getCheckedRadioButtonId();

                    // find the radiobutton by returned id
                    radioButton = (RadioButton) findViewById(selectedId);
                   try {
                       trole = radioButton.getText().toString();
                       Log.v("Error",""+radioButton.getText());
                   }
                   catch (Exception e)
                   {
                       trole="";
                   }
                       if (trole.equalsIgnoreCase("student"))
                            roleid=1;
                       else if (trole.equalsIgnoreCase("authority"))
                            roleid=2;
                       else
                           roleid=-1;


                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(Signin.this);
                    SharedPreferences.Editor ed = sharedPrefs.edit();
                    ed.putInt("role", roleid);
                    ed.commit();

                    if (roleid == 1) {
                        Intent studentlogin = new Intent(Signin.this, Login.class);
                        //ActivityOptionsCompat compat=ActivityOptionsCompat.makeSceneTransitionAnimation(Signin.this,null);
                        startActivity(studentlogin);
                        Signin.this.finish();
                    } else if (roleid == 2) {
                        Intent authoritylogin = new Intent(Signin.this, signinas.class);
                        //ActivityOptionsCompat compat=ActivityOptionsCompat.makeSceneTransitionAnimation(Signin.this,null);
                        startActivity(authoritylogin);
                        //startActivity(authoritylogin);
                        Signin.this.finish();
                    }
                    else
                    {
                        Toast.makeText(Signin.this,"Please choose an appropriate option", Toast.LENGTH_LONG).show();
                    }
                    //Toast.makeText(Signin.this, radioButton.getText(), Toast.LENGTH_SHORT).show();
                }



            });
        }
         else if (role!=-1)
         {
           //  Toast.makeText(Signin.this, radioButton.getText(), Toast.LENGTH_SHORT).show();
           //  Log.v("Error",""+radioButton.getText());
             if (role == 1) {
                 Intent studentlogin = new Intent(Signin.this, Login.class);
                 //ActivityOptionsCompat compat=ActivityOptionsCompat.makeSceneTransitionAnimation(Signin.this,null);
                 startActivity(studentlogin);
                 //startActivity(studentlogin);
                 Signin.this.finish();
             } else if (role == 2) {
                 Intent authoritylogin = new Intent(Signin.this, signinas.class);
                 //ActivityOptionsCompat compat=ActivityOptionsCompat.makeSceneTransitionAnimation(Signin.this,null);
                 startActivity(authoritylogin);
                 //startActivity(authoritylogin);
                 Signin.this.finish();
             }

         }

    }
}
