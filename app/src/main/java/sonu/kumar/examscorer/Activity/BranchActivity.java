package sonu.kumar.examscorer.Activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import sonu.kumar.examscorer.Adapters.ReappearAdapter;
import sonu.kumar.examscorer.BroadcastReceivers.CheckInternetConnection;
import sonu.kumar.examscorer.Fragments.HomeFragment;
import sonu.kumar.examscorer.R;
import sonu.kumar.examscorer.Utills.Constants;
import sonu.kumar.examscorer.Utills.CustomTypefaceSpan;
import sonu.kumar.examscorer.Utills.Mysingleton;

public class BranchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
android.support.v7.widget.Toolbar toolbar;
DrawerLayout drawerLayoutl;
FrameLayout frameLayout;
NavigationView navigationView;
Button selectoption;
 ImageView home_search_btn;
RelativeLayout relativeLayout;
CircleImageView profile_image;
TextView user_name,user_email;
TextView favcout;
int user_id;
String selected_option;
EditText home_search_editext;
public static final String TAG ="HOmeActivity";
String shared_image_url,shared_user_name,shared_user_email;
SharedPreferences sharedPreferences;
    private CheckInternetConnection checkInternetConnection;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (checkInternetConnection !=null){
            unregisterReceiver(checkInternetConnection);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch);
        navigationView = findViewById(R.id.navigationview);
        home_search_btn =findViewById(R.id.homesearch_btn);
        home_search_editext =findViewById(R.id.home_paper_search);
        sharedPreferences =getSharedPreferences(Constants.SHARED_KEY,MODE_PRIVATE);
        shared_image_url = sharedPreferences.getString(Constants.LOGIN_USER_IMAGE,null);
        shared_user_name =sharedPreferences.getString(Constants.LOGIN_USER_Name,null);
        shared_user_email =sharedPreferences.getString(Constants.LOGIN_USER_EMAIL,null);
        user_id =sharedPreferences.getInt(Constants.LOGIN_USER_ID,0);


        relativeLayout = navigationView.getHeaderView(0).findViewById(R.id.headerView);
        profile_image =navigationView.getHeaderView(0).findViewById(R.id.profile_image);
        user_name =navigationView.getHeaderView(0).findViewById(R.id.profiel_user_name);
        user_email =navigationView.getHeaderView(0).findViewById(R.id.profile_email_address);
        toolbar =findViewById(R.id.toolbar);
        frameLayout =findViewById(R.id.framelauout);
        selectoption =findViewById(R.id.select_option_btn);
        setSupportActionBar(toolbar);
        drawerLayoutl= findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayoutl,toolbar,R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );

        drawerLayoutl.addDrawerListener(actionBarDrawerToggle);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle.syncState();
        getSupportFragmentManager().beginTransaction().replace(R.id.framelauout,new HomeFragment())
                .commit();
        navigationView.setCheckedItem(R.id.nav_home);
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        checkInternetConnection = new CheckInternetConnection();
        registerReceiver(checkInternetConnection, intentFilter);
        //set navigate icon
        navigationView.getMenu().getItem(0).setActionView(R.layout.navigate_layout);

        navigationView.getMenu().getItem(1).setActionView(R.layout.navigate_layout);

        navigationView.getMenu().getItem(3).setActionView(R.layout.navigate_layout);

        navigationView.getMenu().getItem(4).setActionView(R.layout.navigate_layout);

        navigationView.getMenu().getItem(5).setActionView(R.layout.navigate_layout);


        relativeLayout.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BranchActivity.this,ProfileActivity.class));
            }
        });
        home_search_editext.clearFocus();


        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }

    }
    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "dark.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @Override
    protected void onStart() {
        super.onStart();
        home_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (home_search_editext.getText().toString().trim().isEmpty()){
                    Toast.makeText(BranchActivity.this, "Enter some keyword for search", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selectoption.getText().toString().equals("   Please Select")){
                    Toast.makeText(BranchActivity.this, "Please select any option", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    Intent intent =new Intent(BranchActivity.this,SearchedResultsActivity.class);
                    intent.putExtra("selectedoption",selected_option);
                    intent.putExtra("keyword",home_search_editext.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayoutl.isDrawerOpen(GravityCompat.START)){
            drawerLayoutl.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.profile_setting:
                startActivity(new Intent(BranchActivity.this,ProfileActivity.class));
                break;
            case R.id.sign_out:
                new Constants().ShowLogoutDialog(BranchActivity.this);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelauout,new HomeFragment())
                        .commit();
                break;
            case R.id.nav_notes:
                startActivity(new Intent(BranchActivity.this,NotesActivity.class));

                break;

            case R.id.nav_about_us:
                startActivity(new Intent(BranchActivity.this,AboutActivity.class));

                break;
            case R.id.nav_reappearpapers:
                startActivity(new Intent(BranchActivity.this, ReappearsActivity.class));
                break;

            case R.id.nav_share:
                Intent intent =new Intent(Intent.ACTION_VIEW);
                intent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id=com.agbe.jaquar");
                startActivity(Intent.createChooser(intent,"Share Via"));

                break;
            case R.id.nav_facebook:
                Intent intent1 =new Intent(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse("https://www.facebook.com/ExamScorer-223627784852151/"));
              startActivity(intent1);

                break;
            case R.id.nav_twitter:
                Intent intent2 =new Intent(Intent.ACTION_VIEW);
                intent2.setData(Uri.parse("https://twitter.com/skr3704"));
                startActivity(intent2);

                break;
            case R.id.nav_instagram:
                Intent intent3 =new Intent(Intent.ACTION_VIEW);
                intent3.setData(Uri.parse("https://www.instagram.com/examscorer/"));
                startActivity(intent3);

                break;
            case R.id.nav_youtube:
                Intent intent4 =new Intent(Intent.ACTION_VIEW);
                intent4.setData(Uri.parse("https://www.youtube.com/channel/UCNPqfWK6Cd4ksdrhkQB7B9w"));
                startActivity(intent4);

                break;
            case R.id.nav_fav:
                startActivity(new Intent(BranchActivity.this,FavouritesActivity.class));
                break;

        }
        drawerLayoutl.closeDrawer(GravityCompat.START);
        return true;
    }

    public void SelectOptions(View view) {
        PopupMenu popupMenu =new PopupMenu(this,view);
        popupMenu.inflate(R.menu.select_option_menu);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.option_subject:
                        selectoption.setText("   Subject");
                        selected_option ="Subject";
                        return true;
                    case R.id.option_sub_code:
                        selectoption.setText("   Subject code");
                        selected_option ="Subject code";
                        return true;
                     default:
                         return false;
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        favcout =(TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_fav));
        StringRequest stringRequest =new StringRequest(StringRequest.Method.POST,
                Constants.Request_Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: "+response);
                        favcout.setGravity(Gravity.CENTER_VERTICAL);
                        favcout.setTypeface(null, Typeface.BOLD);
                        favcout.setTextColor(Color.RED);
                        favcout.setText(response);

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map =new HashMap<>();
                map.put("fav_count","yes");
                map.put("user_id", String.valueOf(user_id));
                return map;
            }
        };
        Mysingleton.getInstance(getApplicationContext()).addToRequestQuee(stringRequest);


        //Request to fetch profile data based on user id


        StringRequest stringRequest1 = new StringRequest(StringRequest.Method.POST,
                Constants.Request_Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse profile data: "+response);
                        if (response!=null){
                            try {
                                JSONArray jsonArray =new JSONArray(response);
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject =jsonArray.getJSONObject(i);
                                    String  email  = jsonObject.getString("user_email");
                                    if (email !=null){
                                        user_email.setText(email);
                                    }
                                    String name =jsonObject.getString("user_name");
                                    if (name !=null){
                                        user_name.setText("Hi, "+name);
                                    }
                                    String image =jsonObject.getString("user_profile_pic");
                                    if (!image.equals("http://192.168.43.126/ExamscorerApp/API/Uploads/")){
                                        Log.d(TAG, "onResponse: not equal "+image);
                                        Glide.with(BranchActivity.this).load(image).into(profile_image);
                                    }


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("fetchprofiledetails", "set");
                map.put("user_id", String.valueOf(user_id));
                return map;
            }
        };
        Mysingleton.getInstance(BranchActivity.this).addToRequestQuee(stringRequest1);
    }
}
