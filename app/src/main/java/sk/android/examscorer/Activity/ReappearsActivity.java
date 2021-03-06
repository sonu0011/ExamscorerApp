package sk.android.examscorer.Activity;

import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
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

import sk.android.examscorer.Adapters.ReappearAdapter;
import sk.android.examscorer.BroadcastReceivers.CheckInternetConnection;
import sk.android.examscorer.Models.CommonModel;
import sk.android.examscorer.R;
import sk.android.examscorer.Utills.Constants;
import sk.android.examscorer.Utills.Mysingleton;

public class ReappearsActivity extends AppCompatActivity {

    public static final String TAG="ReappearsActivity";
    RecyclerView recyclerView;
    String sub_code;
    ReappearAdapter reappearAdapter;
    CoordinatorLayout coordinatorLayout;
    List<CommonModel> list;
    private CheckInternetConnection checkInternetConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reappears);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Reappear Papers");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: ");
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.search_menu);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

            Log.d(TAG, "onCreateOptionsMenu: "+searchView);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    ArrayList<CommonModel> arrayList = new ArrayList<>();
                    for (CommonModel model : list) {
                        if (model.getSp_title().toLowerCase().contains(newText.toLowerCase())) {
                            arrayList.add(model);
                        }
                    }
                    reappearAdapter.FilterList(arrayList);

                    return false;
                }
            });



        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        final ProgressDialog dialog = new Constants().showDialog(ReappearsActivity.this);
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
                                String title = jsonObject.getString("reappear_title");
                                String link = jsonObject.getString("reappear_link");
                                String reappear_code =jsonObject.getString("reappear_code");


                                list.add(new CommonModel( title, link,reappear_code,5));


                            }
                            reappearAdapter = new ReappearAdapter(list,ReappearsActivity.this);
                            recyclerView.setAdapter(reappearAdapter);
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
                Map<String, String> map = new HashMap<>();
                map.put("reappears_details", "yes");
                return map;
            }
        };
        Mysingleton.getInstance(getApplicationContext()).addToRequestQuee(stringRequest1);

    }
}