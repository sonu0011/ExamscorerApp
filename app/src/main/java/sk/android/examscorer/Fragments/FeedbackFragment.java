package sk.android.examscorer.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import sk.android.examscorer.BroadcastReceivers.CheckInternetConnection;
import sk.android.examscorer.R;
import sk.android.examscorer.Utills.Constants;
import sk.android.examscorer.Utills.Mysingleton;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedbackFragment extends Fragment {
    EditText email, message;
    Button feedback_btn;
    String shard_email;
    SharedPreferences sharedPreferences;
    public static final String TAG = "feedbackFragment";


    private CheckInternetConnection checkInternetConnection;
    View view;

    public FeedbackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        checkInternetConnection = new CheckInternetConnection();
        getContext().registerReceiver(checkInternetConnection, intentFilter);
        view = inflater.inflate(R.layout.fragment_feedback, container, false);
        email = view.findViewById(R.id.feedback_email);
        message = view.findViewById(R.id.feedback_message);
        feedback_btn = view.findViewById(R.id.feedback_btn);


        sharedPreferences = getContext().getSharedPreferences(Constants.SHARED_KEY, Context.MODE_PRIVATE);
        shard_email = sharedPreferences.getString(Constants.LOGIN_USER_EMAIL, null);
        if (shard_email != null) {
            email.setText(shard_email);
        }
        feedback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (message.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getContext(), "Add some feedback!!", Toast.LENGTH_SHORT).show();
                } else {
                    final ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("sedning feedback");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    StringRequest stringRequest = new StringRequest(StringRequest.Method.POST,
                            Constants.Request_Url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d(TAG, "onResponse: " + response);
                                    if (response.equals("1")) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), "Thanks for your feedback", Toast.LENGTH_SHORT).show();
                                        message.setText("");
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d(TAG, "onErrorResponse: " + error);
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<>();
                            map.put("feedback_details", "yes");
                            map.put("email_address", shard_email);
                            map.put("message", message.getText().toString());
                            return map;
                        }
                    };
                    Mysingleton.getInstance(getContext()).addToRequestQuee(stringRequest);
                }
            }
        });


        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (checkInternetConnection != null) {
            getContext().unregisterReceiver(checkInternetConnection);
        }
    }
}
