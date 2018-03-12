package com.pos.animations.splash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.pos.animations.R;
import com.pos.animations.button_to_form.ButtonToFormActivity;

/**
 * Created by rtd1p on 11/14/2017.
 */

public class SplashActivity extends AppCompatActivity implements AnimationCompleteInterface {

    private final static int SPLASH_DISPLAY_TIME = 1000;

    BroadcastReceiver broadcast_receiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        final View imageView = findViewById(R.id.splash_icon);

        broadcast_receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish_activity")) {
                    finish();
                    // DO WHATEVER YOU WANT.
                }
            }
        };
        registerReceiver(broadcast_receiver, new IntentFilter("finish_activity"));

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                presentActivity(imageView);
                //SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_TIME);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_TIME*3);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcast_receiver);
    }

    private void presentActivity(View v) {
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, v, "transition");
        int revealX = (int) (v.getX() + v.getWidth() / 2);
        int revealY = (int) (v.getY() + v.getHeight() / 2);

        Intent intent = new Intent(this, ButtonToFormActivity.class);
        intent.putExtra(ButtonToFormActivity.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(ButtonToFormActivity.EXTRA_CIRCULAR_REVEAL_Y, revealY);

        //ActivityCompat.startActivity(this, intent, options.toBundle());
        startActivity(intent, options.toBundle());
    }

    @Override
    public void onComplete() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                finish();
//            }
//        }).start();

    }
}
