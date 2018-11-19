package sk.android.examscorer.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import sk.android.examscorer.BroadcastReceivers.CheckInternetConnection;
import sk.android.examscorer.R;
import sk.android.examscorer.Utills.Constants;
import sk.android.examscorer.Utills.Mysingleton;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "LoginActivity";
    CheckInternetConnection checkInternetConnection;
    Button loginbtn, button;
    EditText email, password;
    CheckBox rememberme;
    Button signup;
    int i =0;
    TextView forgotpassword;
    CoordinatorLayout coordinatorLayout;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    int user_id;
    android.support.v7.widget.Toolbar toolbar;
    String user_name, user_image, user_email;
    Snackbar snackbar;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG, "onCreate: ");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        checkInternetConnection = new CheckInternetConnection();
        registerReceiver(checkInternetConnection, intentFilter);


        loginbtn = findViewById(R.id.signin_button);
        progressDialog = new ProgressDialog(LoginActivity.this);
        coordinatorLayout = findViewById(R.id.login_coord);

        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_pwd);
        rememberme = findViewById(R.id.login_remember_me_checkbox);
        signup = findViewById(R.id.login_signup);
        forgotpassword = findViewById(R.id.login_forgot_pwd);
        sharedPreferences = getSharedPreferences(Constants.SHARED_KEY, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        boolean b = sharedPreferences.getBoolean(Constants.REMEMBER_ME, false);
        if (b) {
            startActivity(new Intent(LoginActivity.this, BranchActivity.class));
            finish();

        }

        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (password.getRight() - password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action herei
                        if (i==1){
                            i=0;
                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        }
                        else {
                            i = 1;
                            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        }

                                return true;
                    }
                }
                return false;
            }
        });
        //set click listner
        loginbtn.setOnClickListener(this);
        signup.setOnClickListener(this);
        forgotpassword.setOnClickListener(this);





    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (checkInternetConnection != null) {
            //unregister broadcast receiver

            unregisterReceiver(checkInternetConnection);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.signin_button:
                if (email.getText().toString().trim().isEmpty() ||
                        password.getText().toString().trim().isEmpty()) {
                    snackbar = Snackbar.make(coordinatorLayout, "All fields are required", Snackbar.LENGTH_SHORT);
                    View snackbarView = snackbar.getView();
                    TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } else {
                    final ProgressDialog dialog = new Constants().showDialog(LoginActivity.this);
                    StringRequest stringRequest = new StringRequest(StringRequest.Method.POST,
                            Constants.Request_Url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d(TAG, "onResponse: " + response);
                                    if (response.equals("[]")) {
                                        dialog.dismiss();
                                        snackbar = Snackbar.make(coordinatorLayout, "Invalid email address or password", Snackbar.LENGTH_SHORT);
                                        View snackbarView = snackbar.getView();
                                        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                                        textView.setTextColor(Color.WHITE);
                                        snackbar.show();
                                    }
                                    if (!response.equals("[]")) {
                                        JSONArray jsonArray = null;
                                        try {
                                            jsonArray = new JSONArray(response);
                                            for (int i = 0; i < jsonArray.length(); i++) {


                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                user_id = jsonObject.getInt("user_id");
                                                user_name = jsonObject.getString("user_name");
                                                user_image = jsonObject.getString("user_image");
                                                user_email = jsonObject.getString("user_email");


                                            }


                                            if (rememberme.isChecked()) {

                                                editor.putBoolean(Constants.REMEMBER_ME, true);
                                                editor.putInt(Constants.LOGIN_USER_ID, user_id);
                                                if (user_name.length() > 0) {
                                                    editor.putString(Constants.LOGIN_USER_Name, user_name);

                                                }
                                                if (user_image.length() > 0) {

                                                    //editor.putString(Constants.LOGIN_USER_IMAGE,Constants.localhost+"/API/Uploads/"+user_image);
                                                    //editor.putString(Constants.LOGIN_USER_IMAGE,Constants.localhost+"/API/Uploads/"+user_image);
                                                    editor.putString(Constants.LOGIN_USER_IMAGE, Constants.server + "/API/Uploads/" + user_image);


                                                }
                                                editor.putString(Constants.LOGIN_USER_EMAIL, user_email);
                                                editor.commit();

                                            } else {


                                                editor.putBoolean(Constants.REMEMBER_ME, false);
                                                editor.putInt(Constants.LOGIN_USER_ID, user_id);
                                                if (user_image.length() > 0) {
                                                    editor.putString(Constants.LOGIN_USER_Name, user_name);

                                                }
                                                if (user_image.length() > 0) {
                                                    //editor.putString(Constants.LOGIN_USER_IMAGE,Constants.localhost+"/API/Uploads/"+user_image);
                                                    editor.putString(Constants.LOGIN_USER_IMAGE, Constants.server + "/API/Uploads/" + user_image);


                                                }
                                                editor.putString(Constants.LOGIN_USER_EMAIL, user_email);
                                                editor.commit();


                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        dialog.dismiss();
                                        startActivity(new Intent(LoginActivity.this, BranchActivity.class));
                                        finish();

                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                    Log.d(TAG, "onErrorResponse: " + error.getMessage());

                                }
                            }
                    ) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<>();
                            map.put("login_user", "yes");
                            map.put("user_email", email.getText().toString());
                            map.put("user_pwd", password.getText().toString());
                            return map;
                        }
                    };
                    Mysingleton.getInstance(LoginActivity.this).addToRequestQuee(stringRequest);

                }
                break;
            case R.id.login_signup:
                signup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://examscorer.co.in/signup.php"));
                        startActivity(intent);
                    }
                });

                break;
            case R.id.login_forgot_pwd:
                // get prompts.xml view
               startActivity(new Intent(LoginActivity.this,ForgetPassword.class));

                break;

        }

    }


}
