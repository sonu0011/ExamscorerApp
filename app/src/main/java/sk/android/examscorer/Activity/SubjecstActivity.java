package sk.android.examscorer.Activity;

import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sk.android.examscorer.Adapters.SubjectAdapter;
import sk.android.examscorer.BroadcastReceivers.CheckInternetConnection;
import sk.android.examscorer.Models.CommonModel;
import sk.android.examscorer.R;
import sk.android.examscorer.Utills.Constants;
import sk.android.examscorer.Utills.Mysingleton;

public class SubjecstActivity extends AppCompatActivity {
    public static final String TAG="SubjectsActivity";
    RecyclerView recyclerView;
    String branch_id, sem_title;
    int sem_id;
    SubjectAdapter subjectAdapter;
    List<CommonModel> list;
    private CheckInternetConnection checkInternetConnection;
    private String branch_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjecst);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        branch_id = getIntent().getStringExtra("branch_id");
        branch_title = getIntent().getStringExtra("branch_title");
        sem_title = getIntent().getStringExtra("sem_title");
        sem_id = getIntent().getIntExtra("sem_id",0);

        if (sem_title != null && branch_title!=null) {
            getSupportActionBar().setTitle(branch_title+" ("+sem_title+"  Semester)");
        }
        list = new ArrayList<>();
        recyclerView = findViewById(R.id.sub_recycleview);
        recyclerView.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration =new DividerItemDecoration(SubjecstActivity.this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
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
        final String s ="("+sem_title+"  Semester)";
        final ProgressDialog dialog = new Constants().showDialog(SubjecstActivity.this);
        StringRequest stringRequest1 = new StringRequest(StringRequest.Method.POST,
                Constants.Request_Url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            list.clear();
                                JSONArray    jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    Log.d(TAG, "onResponse:for loop ");
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String sub_heading = jsonObject.getString("sub_heading");
                                    String title = jsonObject.getString("sub_name");
                                    String code = jsonObject.getString("sub_code");

                                    list.add(new CommonModel(sub_heading, title, code, "subject"));


                                }
                                subjectAdapter = new SubjectAdapter(getApplicationContext(), list,s,branch_title);
                                recyclerView.setAdapter(subjectAdapter);
                                dialog.dismiss();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d(TAG, "onResponse: error " + e.getMessage());
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