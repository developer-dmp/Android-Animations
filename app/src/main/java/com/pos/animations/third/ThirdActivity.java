package com.pos.animations.third;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.graphics.drawable.ArgbEvaluator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.pos.animations.R;

/**
 * Created by rtd1p on 12/5/2017.
 */

public class ThirdActivity extends AppCompatActivity {

    Button dialogButton;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_third);

        dialogButton = (Button) findViewById(R.id.dialogButton);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animate();
            }
        });
    }

    private void animate() {
        int duration = 750;
        int red = getResources().getColor(R.color.red);
        int blue = getResources().getColor(R.color.blue);
        int green = getResources().getColor(R.color.green);
        int yellow = getResources().getColor(R.color.yellow);

        ValueAnimator colorAnimationRB = ValueAnimator.ofObject(new ArgbEvaluator(), red, blue); // from, to
        colorAnimationRB.setDuration(duration); // milliseconds
        colorAnimationRB.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                dialogButton.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });

        ValueAnimator colorAnimationBG = ValueAnimator.ofObject(new ArgbEvaluator(), blue, green); // from, to
        colorAnimationBG.setDuration(duration); // milliseconds
        colorAnimationBG.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                dialogButton.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });

        ValueAnimator colorAnimationGY = ValueAnimator.ofObject(new ArgbEvaluator(), green, yellow); // from, to
        colorAnimationGY.setDuration(duration); // milliseconds
        colorAnimationGY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                dialogButton.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });

        AnimatorSet colorChanger = new AnimatorSet();
        colorChanger.play(colorAnimationRB).before(colorAnimationBG).before(colorAnimationGY);
        colorChanger.start();
    }
}
