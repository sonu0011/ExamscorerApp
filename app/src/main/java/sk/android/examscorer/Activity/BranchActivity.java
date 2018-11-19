package sk.android.examscorer.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import sk.android.examscorer.BroadcastReceivers.CheckInternetConnection;
import sk.android.examscorer.BuildConfig;
import sk.android.examscorer.Fragments.HomeFragment;
import sk.android.examscorer.MyFirebaseInstanceIDService;
import sk.android.examscorer.R;
import sk.android.examscorer.SharedPreference;
import sk.android.examscorer.Utills.Constants;
import sk.android.examscorer.Utills.CustomTypefaceSpan;
import sk.android.examscorer.Utills.Mysingleton;

public class BranchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    android.support.v7.widget.Toolbar toolbar;
    DrawerLayout drawerLayoutl;
    FrameLayout frameLayout;
    NavigationView navigationView;
    Button selectoption;
    ImageView home_search_btn;
    RelativeLayout relativeLayout;
    CircleImageView profile_image;
    TextView user_name, user_email;
    TextView favcout;
    int user_id;
    String selected_option;
    EditText home_search_editext;
    public static final String TAG = "HOmeActivity";
    String shared_image_url, shared_user_name, shared_user_email;
    SharedPreferences sharedPreferences;
    private CheckInternetConnection checkInternetConnection;
    CoordinatorLayout coordinatorLayout;
    RadioButton sub, code;
    String fb, tw, ins, ps, yt;
    AlertDialog alert;
    private String VersionUpdate;
    private String url="http://examscorer.co.in/ExamscorerApp/API/json.php";

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (checkInternetConnection != null) {
            unregisterReceiver(checkInternetConnection);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch);

        new VersionCheck().execute();

        sub = findViewById(R.id.subjectradionbutton);
        code = findViewById(R.id.subcoderadiobutton);
        navigationView = findViewById(R.id.navigationview);

        Menu menu = navigationView.getMenu();
        MenuItem item = menu.findItem(R.id.followus);


        View view = LayoutInflater.from(BranchActivity.this).inflate(R.layout.text, null);
        item.setActionView(view);


        home_search_editext = findViewById(R.id.home_paper_search);
        coordinatorLayout = findViewById(R.id.branch_coor);
        sharedPreferences = getSharedPreferences(Constants.SHARED_KEY, MODE_PRIVATE);
        shared_image_url = sharedPreferences.getString(Constants.LOGIN_USER_IMAGE, null);

        shared_user_name = sharedPreferences.getString(Constants.LOGIN_USER_Name, null);
        shared_user_email = sharedPreferences.getString(Constants.LOGIN_USER_EMAIL, null);
        user_id = sharedPreferences.getInt(Constants.LOGIN_USER_ID, 0);


        Log.d(TAG, "onCreate: image urlo " + shared_image_url);
        Log.d(TAG, "onCreate: username" + shared_user_name);
        Log.d(TAG, "onCreate: userid" + user_id);


        relativeLayout = navigationView.getHeaderView(0).findViewById(R.id.headerView);
        profile_image = navigationView.getHeaderView(0).findViewById(R.id.profile_image);
        user_name = navigationView.getHeaderView(0).findViewById(R.id.profiel_user_name);
        user_email = navigationView.getHeaderView(0).findViewById(R.id.profile_email_address);


        user_email.setTextColor(Color.WHITE);
        user_name.setTextColor(Color.WHITE);
        toolbar = findViewById(R.id.toolbar);
        frameLayout = findViewById(R.id.framelauout);
        //selectoption =findViewById(R.id.select_option_btn);
        setSupportActionBar(toolbar);
        drawerLayoutl = findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayoutl, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );


        drawerLayoutl.addDrawerListener(actionBarDrawerToggle);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle.syncState();
        getSupportFragmentManager().beginTransaction().replace(R.id.framelauout, new HomeFragment())
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
                        startActivity(new Intent(BranchActivity.this, ProfileActivity.class));
                    }
                });
        home_search_editext.clearFocus();


        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }
        sendToken();

    }

    public void sendToken() {
        Intent intent = new Intent(this, MyFirebaseInstanceIDService.class);
        startService(intent);
        final String token = SharedPreference.getInstance(this).getDeviceToken();
        Log.d(TAG, "sendToken: " + token);
        if (Constants.device_id == null) {
            Log.e(TAG, "sendToken: token not generated");
            return;
        } else {
            Log.e(TAG, "sendToken: token generated is " + token);
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_REGISTER_DEVICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse: token response " + response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.d(TAG, "onResponse: " + obj.getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: token response" + error.toString());

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String email = sharedPreferences.getString(Constants.LOGIN_USER_EMAIL, null);

                Log.d(TAG, "getParams: " + email + token);
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("token", token);
                return params;
            }
        };
        Mysingleton.getInstance(this).addToRequestQuee(stringRequest);
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "dark.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @Override
    protected void onStart() {
        super.onStart();

        home_search_editext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });

        StringRequest sr = new StringRequest(StringRequest.Method.POST,
                Constants.Request_Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: url response " + response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                if (!jsonObject.getString("facebook").equals("")) {
                                    Log.d(TAG, "onResponse:  facebook url is" + jsonObject.getString("facebook"));
                                    Constants.facebook = jsonObject.getString("facebook");
                                }
                                if (!jsonObject.getString("playstore").equals("")) {
                                    Log.d(TAG, "onResponse:  playstore url is" + jsonObject.getString("playstore"));
                                    Constants.playstore = jsonObject.getString("playstore");
                                }

                                if (!jsonObject.getString("instagram").equals("")) {
                                    Log.d(TAG, "onResponse:  instagram url is" + jsonObject.getString("instagram"));
                                    Constants.instagram = jsonObject.getString("instagram");
                                }
                                if (!jsonObject.getString("youtube").equals("")) {
                                    Log.d(TAG, "onResponse:  youtube url is" + jsonObject.getString("youtube"));
                                    Constants.youtube = jsonObject.getString("youtube");
                                }
                                if (!jsonObject.getString("twitter").equals("")) {
                                    Log.d(TAG, "onResponse:  twitter url is" + jsonObject.getString("twitter"));
                                    Constants.twiteer = jsonObject.getString("twitter");
                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("AllUrls", "yes");
                return map;
            }
        };
        Mysingleton.getInstance(BranchActivity.this).addToRequestQuee(sr);
    }

    private void performSearch() {
        final View view = this.getCurrentFocus();


        if (home_search_editext.getText().toString().trim().isEmpty()) {
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            Toast.makeText(BranchActivity.this, "Enter some keyword for search", Toast.LENGTH_SHORT).show();
            return;
        }
//                if (selectoption.getText().toString().equals("   Please Select")){
//                    Toast.makeText(BranchActivity.this, "Please select any option", Toast.LENGTH_SHORT).show();
//                    return;
//                }


        else {
            final ProgressDialog dialog = new Constants().showDialog(BranchActivity.this);
            if (sub.isChecked()) {
                selected_option = "Subject";
            }
            if (code.isChecked()) {
                selected_option = "Subject code";
            }
            StringRequest sr = new StringRequest(StringRequest.Method.POST,
                    Constants.Request_Url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "onResponse: ");
                            if (response.equals("[]")) {
                                if (view != null) {
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    assert imm != null;
                                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                }

                                dialog.dismiss();

                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "No result found, try again !!", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                                View snackbarView = snackbar.getView();
                                TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.WHITE);
                                snackbar.show();
                                return;

                            } else {
                                dialog.dismiss();
                                Intent intent = new Intent(BranchActivity.this, SearchedResultsActivity.class);
                                intent.putExtra("selectedoption", selected_option);
                                intent.putExtra("keyword", home_search_editext.getText().toString());
                                startActivity(intent);
                            }


                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("seachedresult", "yes");
                    map.put("keyword", home_search_editext.getText().toString());
                    map.put("selectedoption", selected_option);
                    return map;
                }
            };
            Mysingleton.getInstance(BranchActivity.this).addToRequestQuee(sr);


        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayoutl.isDrawerOpen(GravityCompat.START)) {
            drawerLayoutl.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile_setting:
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(ps + getPackageName())));
                }
                break;
            case R.id.sign_out:
                new Constants().ShowLogoutDialog(BranchActivity.this);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelauout, new HomeFragment())
                        .commit();
                break;
            case R.id.nav_notes:
                startActivity(new Intent(BranchActivity.this, NotesActivity.class));

                break;

            case R.id.nav_about_us:
                startActivity(new Intent(BranchActivity.this, AboutActivity.class));

                break;
            case R.id.nav_reappearpapers:
                startActivity(new Intent(BranchActivity.this, ReappearsActivity.class));
                break;

            case R.id.nav_share:
                Log.d(TAG, "onNavigationItemSelected: share" + Constants.playstore);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Download App");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, Constants.playstore);
                startActivity(Intent.createChooser(intent, "Share via"));
//                intent.setData(Uri.parse(Constants.playstore));
//                startActivity(intent);

                break;
            case R.id.nav_facebook:
                Log.d(TAG, "onNavigationItemSelected: facebook" + Constants.facebook);
                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse(Constants.facebook));
                startActivity(intent1);
                break;
            case R.id.nav_twitter:
                Intent intent2 = new Intent(Intent.ACTION_VIEW);
                intent2.setData(Uri.parse(Constants.twiteer));
                startActivity(intent2);
                break;
            case R.id.nav_instagram:
                Intent intent3 = new Intent(Intent.ACTION_VIEW);
                intent3.setData(Uri.parse(Constants.instagram));
                startActivity(intent3);
                break;
            case R.id.nav_youtube:
                Intent intent4 = new Intent(Intent.ACTION_VIEW);
                intent4.setData(Uri.parse(Constants.youtube));
                startActivity(intent4);

                break;
            case R.id.nav_fav:
                startActivity(new Intent(BranchActivity.this, FavouritesActivity.class));
                break;

        }
        drawerLayoutl.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        favcout = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_fav));

        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST,
                Constants.Request_Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        favcout.setGravity(Gravity.CENTER_VERTICAL);
                        favcout.setTypeface(null, Typeface.BOLD);
                        favcout.setTextColor(Color.BLUE);
                        favcout.setText(response);

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("fav_count", "yes");
                map.put("user_id", String.valueOf(user_id));
                return map;
            }
        };
        Mysingleton.getInstance(getApplicationContext()).addToRequestQuee(stringRequest);


        if (shared_user_name != null) {
            if (shared_user_name != null) {
                user_name.setText("Hi, " + shared_user_name);


            }
            else {
                user_name.setText("Hi");
            }
        }
        if (shared_image_url != null) {
            if (!
                    //shared_image_url.equals(Constants.localhost+"/ExamscorerApp/API/Uploads/")
                    shared_image_url.equals(Constants.server + "/ExamscorerApp/API/Uploads/")
                    ) {

                Glide.with(BranchActivity.this).load(shared_image_url).into(profile_image);

            }
        }

        if (shared_user_email != null) {
            user_email.setText(shared_user_email);

        }
    }

    @SuppressLint("StaticFieldLeak")
    private class VersionCheck extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            StringRequest stringRequest = new StringRequest(StringRequest.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "onResponse: version response " + response);
                            try {
                                JSONObject jsonObject =new JSONObject(response);
                                JSONArray jsonArray =jsonObject.getJSONArray("version");
                                for (int i =0;i<jsonArray.length();i++){
                                   JSONObject jsonObject1 =jsonArray.getJSONObject(i);
                                   VersionUpdate =  jsonObject1.getString("version");
                                   if (!VersionUpdate.equals(BuildConfig.VERSION_NAME)){

                                       AlertDialog.Builder builder = new AlertDialog.Builder(BranchActivity.this);
                                       builder.setTitle("Our App got Update");
                                       builder.setIcon(R.mipmap.ic_launcher);
                                       builder.setCancelable(false);
                                       builder.setMessage("New version available, select update to update our app")
                                               .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                                                   @Override
                                                   public void onClick(DialogInterface dialog, int which) {

                                                       final String appName = getPackageName();

                                                       try {
                                                           startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appName)));
                                                       } catch (android.content.ActivityNotFoundException anfe) {
                                                           startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
                                                       }

                                                       finish();

                                                   }
                                               });

                                       alert= builder.create();
                                       alert.show();
                                   }
                                   else {
                                       if (alert!=null){
                                           alert.dismiss();
                                       }
                                   }
                                   Constants.version =VersionUpdate;
                                    Log.d(TAG, "onResponse: version"+VersionUpdate);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
            };
            Mysingleton.getInstance(getApplicationContext()).addToRequestQuee(stringRequest);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }


    }
}
