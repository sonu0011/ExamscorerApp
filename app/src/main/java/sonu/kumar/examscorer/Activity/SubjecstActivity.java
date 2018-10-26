package sonu.kumar.examscorer.Activity;

import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sonu.kumar.examscorer.Adapters.SubjectAdapter;
import sonu.kumar.examscorer.BroadcastReceivers.CheckInternetConnection;
import sonu.kumar.examscorer.Models.CommonModel;
import sonu.kumar.examscorer.R;
import sonu.kumar.examscorer.Utills.Constants;
import sonu.kumar.examscorer.Utills.Mysingleton;

public class SubjecstActivity extends AppCompatActivity {
    public static final String TAG="SubjectsActivity";
    RecyclerView recyclerView;
    String branch_id, sem_title;
    int sem_id;
    SubjectAdapter subjectAdapter;
    List<CommonModel> list;
    private CheckInternetConnection checkInternetConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjecst);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        branch_id = getIntent().getStringExtra("branch_id");
        sem_title = getIntent().getStringExtra("sem_title");
        sem_id = getIntent().getIntExtra("sem_id",0);

        if (sem_title != null) {
            getSupportActionBar().setTitle(sem_title);
        }
        list = new ArrayList<>();
        recyclerView = findViewById(R.id.sub_recycleview);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        checkInternetConnection = new CheckInternetConnection();
        registerReceiver(checkInternetConnection, intentFilter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (checkInternetConnection!=null){
            unregisterReceiver(checkInternetConnection);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        final ProgressDialog dialog = new Constants().showDialog(SubjecstActivity.this);
        StringRequest stringRequest1 = new StringRequest(StringRequest.Method.POST,
                Constants.Request_Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: "+response);
                        if (branch_id.equals("1") && sem_id ==3){
                            StringRequest request =new StringRequest(
                                    StringRequest.Method.POST,
                                    Constants.Request_Url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.d(TAG, "onResponse: cse 3rd sem"+response);

                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.d(TAG, "onErrorResponse: cse 3rd sem error"+error.getMessage());

                                        }
                                    }
                            ){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                 Map<String,String> map =new HashMap<>();

                                 map.put("cse3rdsem","yes");
                                 return map;
                                }
                            };
                            Mysingleton.getInstance(getApplicationContext()).addToRequestQuee(request);



                        }
                        Log.d(TAG, "onResponse: " + response);
                        if (response != null) {

                            try {
                              JSONArray    jsonArray = new JSONArray(response);
                                list.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    Log.d(TAG, "onResponse:for loop ");
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String sub_heading = jsonObject.getString("sub_heading");
                                    String title = jsonObject.getString("sub_name");
                                    String code = jsonObject.getString("sub_code");

                                    list.add(new CommonModel(sub_heading, title, code, "subject"));


                                }
                                subjectAdapter = new SubjectAdapter(getApplicationContext(), list);
                                recyclerView.setAdapter(subjectAdapter);
                                dialog.dismiss();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d(TAG, "onResponse: error " + e.getMessage());
                            }


                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.toString());
                Log.d(TAG, "onErrorResponse: "+error.getCause());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Subject_details", "yes");
                map.put("branch_id",branch_id);
                map.put("sem_id",String.valueOf(sem_id));
                return map;
            }
        };
        Mysingleton.getInstance(getApplicationContext()).addToRequestQuee(stringRequest1);

    }
}