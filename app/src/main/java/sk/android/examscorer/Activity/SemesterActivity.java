package sk.android.examscorer.Activity;

import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;

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

import sk.android.examscorer.Adapters.SemAdapter;
import sk.android.examscorer.BroadcastReceivers.CheckInternetConnection;
import sk.android.examscorer.Models.CommonModel;
import sk.android.examscorer.R;
import sk.android.examscorer.Utills.Constants;
import sk.android.examscorer.Utills.Mysingleton;

public class SemesterActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    String branch_id,branch_title;
    SemAdapter semAdapter;
    List<CommonModel>list;
    public static final String TAG ="SEMESTERACTIVITY";
    private CheckInternetConnection checkInternetConnection;
    private String branch_heading;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         branch_id = getIntent().getStringExtra("branch_id");
         branch_heading = getIntent().getStringExtra("branch_heading");
         branch_title = getIntent().getStringExtra("branch_title");
         if (branch_heading!=null){
             getSupportActionBar().setTitle(branch_heading);
         }
         list =new ArrayList<>();
         recyclerView =findViewById(R.id.common_recycleview);
         recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        checkInternetConnection = new CheckInternetConnection();
        registerReceiver(checkInternetConnection, intentFilter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showINterstialADS();
    }

    private void showINterstialADS() {

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
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        final ProgressDialog dialog = new Constants().showDialog(SemesterActivity.this);
        StringRequest stringRequest =new StringRequest(StringRequest.Method.POST,
                Constants.Request_Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: "+response);
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response);
                            list.clear();
                            for (int i=0;i<jsonArray.length();i++){

                                Log.d(TAG, "onResponse:for loop " );
                                JSONObject jsonObject =jsonArray.getJSONObject(i);
                                int  id = jsonObject.getInt("sem_id");
                                String title = jsonObject.getString("sem_title");
                                String image = jsonObject.getString("sem_image");

                                list.add(new CommonModel(id,image,title));



                            }semAdapter =new SemAdapter(list,getApplicationContext(),branch_id,branch_title);
                            recyclerView.setAdapter(semAdapter);
                            dialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: "+error.toString());

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map =new HashMap<>();
                map.put("Sem_details","yes");

                return map;
            }
        };
        Mysingleton.getInstance(getApplicationContext()).addToRequestQuee(stringRequest);

    }
}
