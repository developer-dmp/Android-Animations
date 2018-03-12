package com.pos.animations.second;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.ArgbEvaluator;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.animations.R;
import com.pos.animations.button_to_form.MorphAnimation;
import com.pos.animations.third.ThirdActivity;

import org.w3c.dom.Text;

/**
 * Created by rtd1p on 11/27/2017.
 */

public class SecondLogin extends AppCompatActivity implements View.OnClickListener {

    private Button morphButton, submitButton, skipButton;
    private EditText usernameET, ageET, emailET;
    private TextView displayTV;

    private ViewGroup formViews;
    private View containingCard;
    private View rootLayout;

    private MorphAnimation morphAnimation;

    private boolean animationOccurring = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_login);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        linkElements();

        morphAnimation = new MorphAnimation(containingCard, (FrameLayout) rootLayout, formViews);
    }

    private void linkElements() {
        rootLayout      = findViewById(R.id.container);
        containingCard  = findViewById(R.id.form_card);
        formViews       = (ViewGroup) findViewById(R.id.form_views);

        morphButton  = (Button) findViewById(R.id.form_expand_button);
        submitButton = (Button) findViewById(R.id.submitButton);
        skipButton   = (Button) findViewById(R.id.skipButton);
        morphButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);
        skipButton.setOnClickListener(this);

        usernameET  = (EditText) findViewById(R.id.username_editText);
        ageET       = (EditText) findViewById(R.id.age_editText);
        emailET     = (EditText) findViewById(R.id.email_editText);
        emailET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_GO) {
                    processInformation();
                    return true;
                }
                return false;
            }
        });

        displayTV = (TextView)findViewById(R.id.display_textView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.form_expand_button:
                performAnimation();
                break;
            case R.id.submitButton:
                processInformation();
                break;
            case R.id.skipButton:
                //startActivity(new Intent(this, ThirdActivity.class));
                animate();
                break;
        }
    }

    private void performAnimation() {
        if (!morphAnimation.isPressed()) {
            morphAnimation.morphIntoForm();
            morphButton.setText("Close Form");
            displayTV.setVisibility(View.INVISIBLE);
        } else {
            morphAnimation.morphIntoButton();
            morphButton.setText("Continue with form");
        }
    }

    private void processInformation() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        new AsyncTask<Void, Void, Void>() {

            ProgressDialog progress;
            String displayMessage;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                performAnimation();
                progress = new ProgressDialog(SecondLogin.this);
                progress.setCancelable(false);
                progress.setTitle("Loading");
                progress.setMessage("Submitting information...");
                progress.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    displayMessage = loadUserInformation();
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progress.dismiss();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                displayTV.setText(displayMessage);
                displayTV.setVisibility(View.VISIBLE);
                usernameET.setText("");
                ageET.setText("");
                emailET.setText("");
            }

            private String loadUserInformation() {

                if (usernameET.getText().toString().trim().isEmpty() ||
                        ageET.getText().toString().trim().isEmpty() ||
                        emailET.getText().toString().trim().isEmpty()) {
                    return "Invalid information, please try again.";
                } else {
                    StringBuilder builder = new StringBuilder();
                    builder.append("Username: "+usernameET.getText().toString().trim())
                            .append("\nAge: "+getAgeRange(ageET.getText().toString().trim()))
                            .append("\nEmail: "+emailET.getText().toString().trim());
                    return builder.toString();
                }
            }

            private String getAgeRange(String age) {
                int a = Integer.valueOf(age);
                return String.valueOf(a-1) + " - " + String.valueOf(a+1);
            }
        }.execute();
    }

    private void animate() {

        if (animationOccurring) {
            Toast.makeText(this, "Animation in progress!", Toast.LENGTH_SHORT).show();
            return;
        }
        animationOccurring = true;
        final int duration = 2000;
        int red = getResources().getColor(R.color.red);
        int blue = getResources().getColor(R.color.blue);
        int green = getResources().getColor(R.color.green);
        int yellow = getResources().getColor(R.color.yellow);

        ValueAnimator colorAnimationRB = ValueAnimator.ofObject(new ArgbEvaluator(), red, blue); // from, to
        colorAnimationRB.setDuration(duration); // milliseconds
        colorAnimationRB.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                skipButton.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });

        ValueAnimator colorAnimationBG = ValueAnimator.ofObject(new ArgbEvaluator(), blue, green); // from, to
        colorAnimationBG.setDuration(duration); // milliseconds
        colorAnimationBG.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                skipButton.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });

        ValueAnimator colorAnimationGY = ValueAnimator.ofObject(new ArgbEvaluator(), green, yellow); // from, to
        colorAnimationGY.setDuration(duration); // milliseconds
        colorAnimationGY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                skipButton.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });

        ValueAnimator colorAnimationYR = ValueAnimator.ofObject(new ArgbEvaluator(), yellow, red); // from, to
        colorAnimationYR.setDuration(duration); // milliseconds
        colorAnimationYR.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                skipButton.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });

        final AnimatorSet colorChanger = new AnimatorSet();
        colorChanger.play(colorAnimationRB);
        colorChanger.play(colorAnimationBG).after(colorAnimationRB);
        colorChanger.play(colorAnimationGY).after(colorAnimationBG);
        colorChanger.play(colorAnimationYR).after(colorAnimationGY);
        colorChanger.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                colorChanger.start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animationOccurring = false;
                    }
                }, duration*colorChanger.getChildAnimations().size());
            }
        }, duration*colorChanger.getChildAnimations().size());
    }
}
