package sonu.kumar.examscorer.Activity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sonu.kumar.examscorer.Adapters.SemAdapter;
import sonu.kumar.examscorer.BroadcastReceivers.CheckInternetConnection;
import sonu.kumar.examscorer.Models.CommonModel;
import sonu.kumar.examscorer.R;
import sonu.kumar.examscorer.Utills.Constants;
import sonu.kumar.examscorer.Utills.Mysingleton;

public class SemesterActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    AdView adView;
    String branch_id,branch_title;
    SemAdapter semAdapter;
    List<CommonModel>list;
    public static final String TAG ="SEMESTERACTIVITY";
    private CheckInternetConnection checkInternetConnection;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester);

        MobileAds.initialize(SemesterActivity.this,"ca-app-pub-9104180069881340~6525361897");
        adView =findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);


        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         branch_id = getIntent().getStringExtra("branch_id");
         branch_title = getIntent().getStringExtra("branch_title");
         if (branch_title!=null){
             getSupportActionBar().setTitle(branch_title);
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

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        showINterstialADS();
//    }

    private void showINterstialADS() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
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



                            }semAdapter =new SemAdapter(list,getApplicationContext(),branch_id);
                            recyclerView.setAdapter(semAdapter);

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
