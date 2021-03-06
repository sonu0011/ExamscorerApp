package sk.android.examscorer.Activity;

import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

import sk.android.examscorer.Adapters.PaperAdapter;
import sk.android.examscorer.BroadcastReceivers.CheckInternetConnection;
import sk.android.examscorer.Models.CommonModel;
import sk.android.examscorer.R;
import sk.android.examscorer.Utills.Constants;
import sk.android.examscorer.Utills.Mysingleton;

public class FavouritesActivity extends AppCompatActivity {

    public static final String TAG="FavouritesActivity";
    RecyclerView recyclerView;
    String sub_code;
    PaperAdapter paperAdapter;
    CoordinatorLayout coordinatorLayout;
    List<CommonModel> list;
    private SharedPreferences sharedPreferences;
    private CheckInternetConnection checkInternetConnection;
    ImageView nofav;
    TextView nofavtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        Log.d(TAG, "onCreate: ");
        nofav =findViewById(R.id.nofav);
        nofavtext =findViewById(R.id.nofavtext);
        coordinatorLayout =findViewById(R.id.favcordinate);
        sharedPreferences =getSharedPreferences(Constants.SHARED_KEY,MODE_PRIVATE);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sub_code = getIntent().getStringExtra("sub_code");
        getSupportActionBar().setTitle("Favourites");
        list = new ArrayList<>();
        recyclerView = findViewById(R.id.sub_recycleview);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        checkInternetConnection = new CheckInternetConnection();
        registerReceiver(checkInternetConnection, intentFilter);



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (checkInternetConnection !=null){
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
        Log.d(TAG, "onStart: ");
        final ProgressDialog dialog = new Constants().showDialog(FavouritesActivity.this);
        StringRequest stringRequest1 = new StringRequest(StringRequest.Method.POST,
                Constants.Request_Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: "+response);
                        dialog.dismiss();
                        if (response.equals("[]")){
//                            Snackbar.make(coordinatorLayout,"No favourites found",Snackbar.LENGTH_LONG).show();
                            //Toast.makeText(FavouritesActivity.this, "No favourites found", Toast.LENGTH_SHORT).show();
                            nofav.setVisibility(View.VISIBLE);
                            nofavtext.setVisibility(View.VISIBLE);
                            return;
                        }
                        Log.d(TAG, "onResponse: " + response);
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response);
                            list.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {

                                Log.d(TAG, "onResponse:for loop ");
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String title = jsonObject.getString("paper_title");
                                String link = jsonObject.getString("paper_link");
                                String branch_name = jsonObject.getString("branch_name");

                                int paper_id =jsonObject.getInt("paper_id");
                                list.add(new CommonModel( title, link,paper_id,branch_name));


                            }
                            paperAdapter = new PaperAdapter(1,FavouritesActivity.this,list,nofav,nofavtext);
                            recyclerView.setAdapter(paperAdapter);
                            dialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                int user_id  = sharedPreferences.getInt(Constants.LOGIN_USER_ID,0);

                Map<String, String> map = new HashMap<>();
                map.put("fav_details", "yes");
                map.put("user_id", String.valueOf(user_id));
                return map;
            }
        };
        Mysingleton.getInstance(getApplicationContext()).addToRequestQuee(stringRequest1);

    }
}