package sk.android.examscorer.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import sk.android.examscorer.R;
import sk.android.examscorer.Utills.Constants;
import sk.android.examscorer.Utills.Mysingleton;

public class ForgetPassword extends AppCompatActivity {
    private static final String TAG = "ForgetPassword";
    EditText userInputemail, user_input_pwd;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        getSupportActionBar().setTitle("Forgot Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient1));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        userInputemail =
                findViewById(R.id.get_new_pwd_email);
        user_input_pwd = findViewById(R.id.get_new_pwd_edittext);
        button = findViewById(R.id.get_new_pass_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog dialog = new Constants().showDialog(ForgetPassword.this);
                if (userInputemail.getText().toString().trim().isEmpty() ||
                        user_input_pwd.getText().toString().trim().isEmpty()) {
                    dialog.dismiss();
                    Toast.makeText(ForgetPassword.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (user_input_pwd.getText().toString().trim().length() < 8) {
                   dialog.dismiss();
                    Toast.makeText(ForgetPassword.this, "Password should be of 8  characters", Toast.LENGTH_SHORT).show();
                } else {
                    StringRequest sr = new StringRequest(
                            StringRequest.Method.POST,
                            Constants.Request_Url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d(TAG, "onResponse: forgot pass " + response);
                                    if (response.equals("0")) {
                                        dialog.dismiss();
                                        Toast.makeText(ForgetPassword.this, "User does not exit", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    if (response.equals("string")) {
                                        dialog.dismiss();
                                        Toast.makeText(ForgetPassword.this, "Error,Please try again", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    if (response.equals("1")) {
                                       dialog.dismiss();
                                        Toast.makeText(ForgetPassword.this, "New Password created", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ForgetPassword.this, LoginActivity.class));
                                        finish();

                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                   dialog.dismiss();
                                    Log.d(TAG, "onErrorResponse: " + error.getMessage());

                                }
                            }
                    ) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<>();
                            map.put("forgot_password", "yes");
                            map.put("user_email", userInputemail.getText().toString());
                            map.put("user_pwd", user_input_pwd.getText().toString());
                            return map;
                        }
                    };
                    Mysingleton.getInstance(ForgetPassword.this).addToRequestQuee(sr);


                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

