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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class signinas extends AppCompatActivity {
    private RadioGroup radioGroup;
    private Button enter;
    private RadioButton radioButton;
    private int roleid,role,roleasid;
    String troleas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signinas);
        //Slide s=new Slide();
        //s.setDuration(200);
        //getWindow().setEnterTransition(s);
        //TransitionInflater tf=TransitionInflater.from(this);
        //Transition t=tf.inflateTransition(R.transition.activitytransition);
        //getWindow().setExitTransition(t);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(signinas.this);
        roleasid = sharedPref.getInt("roleas", -1);
        if (roleasid==-1)
        {
            radioGroup = (RadioGroup) findViewById(R.id.radio);
            Animation bottomToTop = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom);
            radioGroup.startAnimation(bottomToTop);
            enter = (Button) findViewById(R.id.enter);
            enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int selectedId = radioGroup.getCheckedRadioButtonId();

                    // find the radiobutton by returned id
                    radioButton = (RadioButton) findViewById(selectedId);
                    try {
                        troleas = radioButton.getText().toString();
                    }
                    catch (Exception e)
                    {
                        troleas="";
                    }

                    if (troleas.equalsIgnoreCase("proctor"))
                        roleasid=1;
                    else if (troleas.equalsIgnoreCase("faculty"))
                        roleasid=2;
                    else if (troleas.equalsIgnoreCase("studentmember"))
                        roleasid=3;
                    else
                        roleasid=4;

                    if (roleasid==4)
                    {
                        Toast.makeText(signinas.this,"Please choose an appropriate option",Toast.LENGTH_LONG).show();
                    }
                    else {

                        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(signinas.this);
                        SharedPreferences.Editor ed = sharedPrefs.edit();
                        ed.putInt("roleas", roleasid);
                        ed.commit();
                        Intent authoritylogin = new Intent(signinas.this, AuthorityLogin.class);
                        //ActivityOptionsCompat compat=ActivityOptionsCompat.makeSceneTransitionAnimation(signinas.this,null);
                        // startActivity(authoritylogin,compat.toBundle());
                        startActivity(authoritylogin);
                        signinas.this.finish();
                    }
                }
            });
        }
        else if (roleasid!=-1)
        {
            //  Toast.makeText(Signin.this, radioButton.getText(), Toast.LENGTH_SHORT).show();
            //  Log.v("Error",""+radioButton.getText());
                 Intent authoritylogin = new Intent(signinas.this, AuthorityLogin.class);
                //ActivityOptionsCompat compat=ActivityOptionsCompat.makeSceneTransitionAnimation(signinas.this,null);
               // startActivity(authoritylogin,compat.toBundle());
                startActivity(authoritylogin);
                signinas.this.finish();


        }
    }
}
