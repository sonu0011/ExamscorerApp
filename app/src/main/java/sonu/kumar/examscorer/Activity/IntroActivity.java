package sonu.kumar.examscorer.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import sonu.kumar.examscorer.Adapters.CustomPagerAdapter;
import sonu.kumar.examscorer.R;
import sonu.kumar.examscorer.Utills.Constants;

public class IntroActivity extends AppCompatActivity {
    ViewPager viewPager;
    Button prev,next;
    int currentpage;
    LinearLayout linearLayout;
    TextView[] dotstextview;
   CustomPagerAdapter customPagerAdapter;
   SharedPreferences sharedPreferences;
   SharedPreferences.Editor editor;
   public static final String TAG ="IntroActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        prev =findViewById(R.id.prev_btn);
        next =findViewById(R.id.next_btn);

        sharedPreferences =getSharedPreferences(Constants.FIRST_TIME_LAUNCH,MODE_PRIVATE);
        editor =sharedPreferences.edit();
        editor.putBoolean(Constants.SHARED_KEY,true);
        editor.apply();
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(currentpage - 1);
                Log.d(TAG, "onClick: "+currentpage);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(currentpage + 1);
                Log.d(TAG, "onClick: "+currentpage);
            }
        });
        viewPager =findViewById(R.id.intro_viewpager);
        linearLayout =findViewById(R.id.intro_dots_layout);
        customPagerAdapter =new CustomPagerAdapter(IntroActivity.this);
        viewPager.setAdapter(customPagerAdapter);
        addDots(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addDots(position);
                currentpage =position;
                Log.d(TAG, "onPageSelected: "+position +" "+currentpage);
                if (position ==0){

                    next.setEnabled(true);
                    prev.setEnabled(false);
                    prev.setVisibility(View.INVISIBLE);
                    next.setText("Next");
                }
                else if (position ==dotstextview.length -1){
                    next.setEnabled(true);
                    prev.setEnabled(true);
                    prev.setVisibility(View.VISIBLE);
                    next.setText("Finish");
                    prev.setText("Back");
                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(IntroActivity.this,LoginActivity.class));
                            finish();
                        }
                    });

                }
                else {
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
        dotstextview =new TextView[3];
        linearLayout.removeAllViews();
        for (int i=0;i<dotstextview.length;i++){
            dotstextview[i] =new TextView(this);

            dotstextview[i].setText(Html.fromHtml("&#8226"));
            dotstextview[i].setTextSize(35);
            dotstextview[i].setTextColor(getResources().getColor(R.color.colorTransparent));
            linearLayout.addView(dotstextview[i]);
        }
        if (dotstextview.length >0){
            dotstextview[position].setTextColor(Color.WHITE);
        }
    }


}
