package sonu.kumar.examscorer.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import sonu.kumar.examscorer.BroadcastReceivers.CheckInternetConnection;
import sonu.kumar.examscorer.R;
import sonu.kumar.examscorer.Utills.Constants;
import sonu.kumar.examscorer.Utills.Mysingleton;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String TAG ="LoginActivity";
    CheckInternetConnection checkInternetConnection;
    Button loginbtn,button;
    TextInputEditText email,password;
    CheckBox rememberme;
    TextView signup,forgotpassword;
    CoordinatorLayout coordinatorLayout;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    int user_id;
    String user_name,user_image,user_email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Register Broadcast Receiver
        Log.d(TAG, "onCreate: ");

        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        checkInternetConnection = new CheckInternetConnection();
        registerReceiver(checkInternetConnection, intentFilter);


        loginbtn =findViewById(R.id.signin_button);
        progressDialog =new ProgressDialog(LoginActivity.this);
        coordinatorLayout =findViewById(R.id.login_coord);
        email =findViewById(R.id.login_email);
        password =findViewById(R.id.login_pwd);
        rememberme  = findViewById(R.id.login_remember_me_checkbox);
        signup =findViewById(R.id.login_signup);
        forgotpassword =findViewById(R.id.login_forgot_pwd);
        sharedPreferences = getSharedPreferences(Constants.SHARED_KEY,MODE_PRIVATE);
        editor =sharedPreferences.edit();
       boolean b =  sharedPreferences.getBoolean(Constants.REMEMBER_ME,false);
        if (b){
           startActivity(new Intent(LoginActivity.this,BranchActivity.class));
           finish();

       }
        //set click listner
        loginbtn.setOnClickListener(this);
        signup.setOnClickListener(this);
        forgotpassword.setOnClickListener(this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (checkInternetConnection !=null){
            //unregister broadcast receiver

            unregisterReceiver(checkInternetConnection);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signin_button:
                    if (email.getText().toString().trim().isEmpty() ||
                            password.getText().toString().trim().isEmpty()){
                        Snackbar.make(coordinatorLayout,"All fields are required",Snackbar.LENGTH_SHORT).show();
                    }
                    else {
                        progressDialog.setTitle("Loging");
                        progressDialog.setMessage("Please Wait..");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                        StringRequest stringRequest =new StringRequest(StringRequest.Method.POST,
                                Constants.Request_Url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d(TAG, "onResponse: "+response);
                                        if (response.equals("[]")){
                                            progressDialog.dismiss();
                                            Snackbar.make(coordinatorLayout,"Invalid email address or password",Snackbar.LENGTH_SHORT).show();
                                        }
                                        if (!response.equals("[]")){
                                            JSONArray jsonArray = null;
                                            try {
                                                jsonArray = new JSONArray(response);
                                                for (int i=0;i<jsonArray.length();i++) {


                                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                    user_id = jsonObject.getInt("user_id");
                                                    user_name = jsonObject.getString("user_name");
                                                    user_image = jsonObject.getString("user_image");
                                                    user_email = jsonObject.getString("user_email");


                                                }


                                                if (rememberme.isChecked()){

                                                    editor.putBoolean(Constants.REMEMBER_ME,true);
                                                    editor.putInt(Constants.LOGIN_USER_ID,user_id);
                                                    if (user_image.length() >0){
                                                        editor.putString(Constants.LOGIN_USER_Name,user_name);

                                                    }
                                                    if (user_image.length() >0){
                                                        editor.putString(Constants.LOGIN_USER_IMAGE,"http://192.168.43.126/ExamscorerApp/API/Uploads/"+user_image);


                                                    }
                                                    editor.putString(Constants.LOGIN_USER_EMAIL,user_email);
                                                    editor.commit();

                                                }
                                                else {


                                                    editor.putBoolean(Constants.REMEMBER_ME,false);
                                                    editor.putInt(Constants.LOGIN_USER_ID,user_id);
                                                    if (user_image.length() >0){
                                                        editor.putString(Constants.LOGIN_USER_Name,user_name);

                                                    }
                                                    if (user_image.length() >0){
                                                        editor.putString(Constants.LOGIN_USER_IMAGE,"http://192.168.43.126/ExamscorerApp/API/Uploads/"+user_image);


                                                    }editor.putString(Constants.LOGIN_USER_EMAIL,user_email);
                                                    editor.commit();



                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            progressDialog.dismiss();
                                            startActivity(new Intent(LoginActivity.this,BranchActivity.class));
                                            finish();

                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressDialog.dismiss();
                                        Log.d(TAG, "onErrorResponse: "+error.getMessage());

                                    }
                                }
                        ){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> map =new HashMap<>();
                                map.put("login_user","yes");
                                map.put("user_email",email.getText().toString());
                                map.put("user_pwd",password.getText().toString());
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
                        Intent intent =new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://examscorer.co.in/signup.php"));
                        startActivity(intent);
                    }
                });

                break;
            case R.id.login_forgot_pwd:
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(LoginActivity.this);
                View promptsView = li.inflate(R.layout.popuplayout, null);

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        LoginActivity.this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final TextInputEditText userInputemail =  promptsView
                        .findViewById(R.id.get_new_pwd_email);
                final TextInputEditText user_input_pwd =  promptsView
                        .findViewById(R.id.get_new_pwd_edittext);
                final Button button =  promptsView
                        .findViewById(R.id.get_new_pass_btn);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (userInputemail.getText().toString().trim().isEmpty() ||
                                user_input_pwd.getText().toString().trim().isEmpty()){
                            Toast.makeText(LoginActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                        }
                        else if (user_input_pwd.getText().toString().trim().length() <8){
                            Toast.makeText(LoginActivity.this, "Password should be of 8  characters", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            StringRequest sr =new StringRequest(
                                    StringRequest.Method.POST,
                                    Constants.Request_Url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.d(TAG, "onResponse: forgot pass "+response);
                                            if (response.equals("0")){
                                                Toast.makeText(LoginActivity.this, "User does not exit", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            if (response.equals("string")){
                                                Toast.makeText(LoginActivity.this, "Error,Please try again", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            if (response.equals("1")){
                                                Toast.makeText(LoginActivity.this, "New Password created", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(LoginActivity.this,LoginActivity.class));
                                                finish();

                                            }

                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.d(TAG, "onErrorResponse: "+error.getMessage());

                                        }
                                    }
                            ){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String,String> map =new HashMap<>();
                                    map.put("forgot_password","yes");
                                    map.put("user_email",userInputemail.getText().toString());
                                    map.put("user_pwd",user_input_pwd.getText().toString());
                                    return map;
                                }
                            };
                            Mysingleton.getInstance(LoginActivity.this).addToRequestQuee(sr);

                        }
                    }

                });



                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();


                break;

        }

    }


}
