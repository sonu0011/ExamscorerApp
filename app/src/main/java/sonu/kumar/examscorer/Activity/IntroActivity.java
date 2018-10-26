package sonu.kumar.examscorer.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import sonu.kumar.examscorer.Adapters.CustomPagerAdapter;
import sonu.kumar.examscorer.R;
import sonu.kumar.examscorer.Utills.Constants;

public class IntroActivity extends AppCompatActivity {
    ViewPager viewPager;
    Button prev, next;
    int currentpage;
    LinearLayout linearLayout;
    TextView[] dotstextview;
    CustomPagerAdapter customPagerAdapter;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String TAG = "IntroActivity";
    private AlertDialog.Builder mbuilder;
    private AlertDialog alertDialog;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            mbuilder = new AlertDialog.Builder(IntroActivity.this);
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.internetconnectiondialog, null);
            mbuilder.setView(view);
            mbuilder.setCancelable(false);
            mbuilder.create();
            alertDialog = mbuilder.show();

            view.findViewById(R.id.cancel_internet).setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick:Cancel ");
                    alertDialog.dismiss();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        ((Activity) IntroActivity.this).finishAffinity();
                    }


                }
            });
            view.findViewById(R.id.interner_settings).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: InternetSettings");
                    startActivity(new Intent(Settings.ACTION_SETTINGS));
//                        Intent intent = new Intent(Intent.ACTION_MAIN);
//                        intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
//                        startActivity(intent);
                }
            });

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {     // Comment 1
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

//                .... write file into storage ...

            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},    // Comment 2
                        666);
            }
        }
        prev = findViewById(R.id.prev_btn);
        next = findViewById(R.id.next_btn);

        sharedPreferences = getSharedPreferences(Constants.FIRST_TIME_LAUNCH, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean(Constants.SHARED_KEY, true);
        editor.apply();
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(currentpage - 1);
                Log.d(TAG, "onClick: " + currentpage);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(currentpage + 1);
                Log.d(TAG, "onClick: " + currentpage);
            }
        });
        viewPager = findViewById(R.id.intro_viewpager);
        linearLayout = findViewById(R.id.intro_dots_layout);
        customPagerAdapter = new CustomPagerAdapter(IntroActivity.this);
        viewPager.setAdapter(customPagerAdapter);
        addDots(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addDots(position);
                currentpage = position;
                Log.d(TAG, "onPageSelected: " + position + " " + currentpage);
                if (position == 0) {

                    next.setEnabled(true);
                    //prev.setText("SKIP");
                    prev.setEnabled(true);
                    prev.setVisibility(View.INVISIBLE);
                    next.setText("Next");
//               if (prev.getText().toString().equals("SKIP")){
//                   prev.setOnClickListener(new View.OnClickListener() {
//                       @Override
//                       public void onClick(View view) {
//                           startActivity(new Intent(IntroActivity.this,LoginActivity.class));
//                           finish();
//                       }
//                   });
//               }

                } else if (position == dotstextview.length - 1) {
                    next.setEnabled(true);
                    prev.setEnabled(true);
                    prev.setVisibility(View.VISIBLE);
                    next.setText("Finish");
                    prev.setText("Back");
                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(IntroActivity.this, LoginActivity.class));
                            finish();
                        }
                    });

                } else {
                    next.setEnabled(true);
                    prev.setEnabled(true);
                    prev.setVisibility(View.VISIBLE);
                    next.setText("Next");
                    prev.setText("Back");

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void addDots(int position) {
        dotstextview = new TextView[3];
        linearLayout.removeAllViews();
        for (int i = 0; i < dotstextview.length; i++) {
            dotstextview[i] = new TextView(this);

            dotstextview[i].setText(Html.fromHtml("&#8226"));
            dotstextview[i].setTextSize(35);
            dotstextview[i].setTextColor(getResources().getColor(R.color.colorTransparent));
            linearLayout.addView(dotstextview[i]);
        }
        if (dotstextview.length > 0) {
            dotstextview[position].setTextColor(Color.WHITE);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {


        } else {
            Log.d(TAG, "onRestart: internet connection availabel");
            if (alertDialog !=null){
                alertDialog.dismiss();
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: ");
        switch (requestCode) {
            case 666: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {   // Comment 1.

                    //... we got the permission allowed, now we can try to write the file into storage again ...
                    Log.d(TAG, "onRequestPermissionsResult:  request granted");

                } else {
                    Log.d(TAG, "onRequestPermissionsResult:  permission denied");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        Log.d(TAG, "onRequestPermissionsResult: request denied");
                        Snackbar s = Snackbar.make(findViewById(android.R.id.content), "We require a write permission. Please allow it in Settings.", Snackbar.LENGTH_INDEFINITE)
                                .setAction("SETTINGS", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                                        intent.setData(uri);
                                        startActivityForResult(intent, 1000);
                                    }
                                });
                        View snackbarView = s.getView();
                        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        textView.setMaxLines(3);    // Comment 4.
                        s.show();
                    }

                }
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: ");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "onActivityResult:  there is result");

        }
        else {
            Log.d(TAG, "onActivityResult: result is null");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "on activity result permission denied", Toast.LENGTH_SHORT).show();
                Snackbar s = Snackbar.make(findViewById(android.R.id.content),"We require a write permission. Please allow it in Settings.",Snackbar.LENGTH_INDEFINITE)
                        .setAction("SETTINGS", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivityForResult(intent, 1000);
                            }
                        });
                View snackbarView = s.getView();
                TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                textView.setMaxLines(6);
                s.show();
            }
        }
    }
}
