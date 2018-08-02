package com.example.pallavi.norag;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Splash extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TextView textview= (TextView) findViewById(R.id.norag);
        //LinearLayout ll=(LinearLayout) findViewById(R.id.header);
       // Toolbar t=(Toolbar)findViewById(R.id.toolbar);
        //t.setVisibility(View.GONE);
        Animation bottomToTop = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom);
        textview.startAnimation(bottomToTop);
       // ll.startAnimation(bottomToTop);
        //TransitionInflater tf=TransitionInflater.from(this);
        //Transition t=tf.inflateTransition(R.transition.activitytransition);
        //getWindow().setExitTransition(t);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(Splash.this,Signin.class);
                //ActivityOptionsCompat compat=ActivityOptionsCompat.makeSceneTransitionAnimation(Splash.this,null);
                Splash.this.startActivity(mainIntent);
                Splash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }
}
