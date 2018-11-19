package sk.android.examscorer.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import sk.android.examscorer.R;
import sk.android.examscorer.Utills.Constants;

public class SplashActivity extends AppCompatActivity {

    private Runnable runnable;
    private Handler handler;
    public static final String TAG = "SplashActivity";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    boolean b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sharedPreferences = getSharedPreferences(Constants.FIRST_TIME_LAUNCH, MODE_PRIVATE);
        b = sharedPreferences.getBoolean(Constants.SHARED_KEY, false);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (b) {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


                } else {
                    startActivity(new Intent(SplashActivity.this, IntroActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                }
                finish();
            }

        };
        handler.postDelayed(runnable, 2000);

    }
}

