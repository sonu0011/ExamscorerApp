package sonu.kumar.examscorer.Activity;

import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.media.Image;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

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

import sonu.kumar.examscorer.Adapters.ImagesAdapter;
import sonu.kumar.examscorer.Adapters.PPT_AND_PDF_ADAPTER;
import sonu.kumar.examscorer.BroadcastReceivers.CheckInternetConnection;
import sonu.kumar.examscorer.Models.AnotherCommonModel;
import sonu.kumar.examscorer.R;
import sonu.kumar.examscorer.Utills.Constants;
import sonu.kumar.examscorer.Utills.Mysingleton;

public class ImagesActivity extends AppCompatActivity {
String notes_cat_id,notes_subcat_id,notes_subcat_title;
    private CheckInternetConnection checkInternetConnection;
    RecyclerView recyclerView;
    ImagesAdapter adapter;
    List<AnotherCommonModel> list;
    public static final String TAG ="ImagesActivity";
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        progressDialog =new ProgressDialog(ImagesActivity.this);
        list =new ArrayList<>();
        notes_cat_id =getIntent().getStringExtra("cat_id");
        notes_subcat_id =getIntent().getStringExtra("subcat_id");
        notes_subcat_title =getIntent().getStringExtra("subcat_title");

        getSupportActionBar().setTitle(notes_subcat_title);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        checkInternetConnection = new CheckInternetConnection();
        registerReceiver(checkInternetConnection, intentFilter);

        recyclerView  =findViewById(R.id.image_recycleview);
//        recyclerView.addItemDecoration(new DividerItemDecoration(ImagesActivity.this,
//                DividerItemDecoration.HORIZONTAL));
//        recyclerView.addItemDecoration(new DividerItemDecoration(ImagesActivity.this,
//                DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager  =new GridLayoutManager(getApplicationContext(),3);
        recyclerView.setLayoutManager(gridLayoutManager);

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
    protected void onStart() {
        super.onStart();
        progressDialog.setMessage("Loading..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        StringRequest stringRequest1 = new StringRequest(StringRequest.Method.POST,
                Constants.Request_Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response);
                            list.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Log.d(TAG, "onResponse:for loop ");
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String notes_downlad_link = jsonObject.getString("notes_download_link");
                                String notes_downlad_title = jsonObject.getString("notes_download_title");


                                list.add(new AnotherCommonModel(notes_downlad_link,notes_downlad_title,1));


                            }
                            adapter = new ImagesAdapter(ImagesActivity.this, list);
                            recyclerView.setAdapter(adapter);
                            progressDialog.dismiss();

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
                Map<String, String> map = new HashMap<>();
                map.put("images_details", "yes");
                map.put("cat_id", notes_cat_id);
                map.put("subcat_id",notes_subcat_id);
                return map;
            }
        };
        Mysingleton.getInstance(getApplicationContext()).addToRequestQuee(stringRequest1);


    }
}
