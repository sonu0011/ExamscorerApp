package sk.android.examscorer.Activity;

import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

public class SearchedResultsActivity extends AppCompatActivity {

    public static final String TAG="SearchedResultsActivity";
    RecyclerView recyclerView;
    String sub_code;
    PaperAdapter paperAdapter;
    CoordinatorLayout coordinatorLayout;
    List<CommonModel> list;
    TextView showingresultsfor;
    String keyword,selectedoption;
    ProgressDialog progressDialog;
    private CheckInternetConnection checkInternetConnection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_results);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sub_code = getIntent().getStringExtra("sub_code");
        coordinatorLayout =findViewById(R.id.searched_coord);
        getSupportActionBar().setTitle("papers from (2016-2018)");
        list = new ArrayList<>();
        recyclerView = findViewById(R.id.search_papers);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        showingresultsfor =findViewById(R.id.showresult);
         keyword = getIntent().getStringExtra("keyword");
        selectedoption =getIntent().getStringExtra("selectedoption");

         showingresultsfor.setText(keyword);
         progressDialog =new ProgressDialog(SearchedResultsActivity.this,android.R.style.Theme_DeviceDefault_Dialog_Alert);

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
            getMenuInflater().inflate(R.menu.search_menu, menu);


            MenuItem menuItem = menu.findItem(R.id.search_menu);


            SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
            searchView.setQueryHint("Enter Your Branch Name");

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    ArrayList<CommonModel> arrayList = new ArrayList<>();
                    for (CommonModel model : list) {
                        if (model.getBranch_name().toLowerCase().contains(newText.toLowerCase())) {
                            arrayList.add(model);
                        }
                    }
                    paperAdapter.FilterList(arrayList);

                    return false;
                }

            });
        return super.onCreateOptionsMenu(menu);

    }



    @Override
    public void onStart() {
        super.onStart();
        final ProgressDialog dialog = new Constants().showDialog(SearchedResultsActivity.this);
        StringRequest stringRequest1 = new StringRequest(StringRequest.Method.POST,
                Constants.Request_Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: papers " + response);
                        if (response.equals("[]")) {
                            progressDialog.dismiss();
                            Snackbar.make(coordinatorLayout,"No result found, try again !!",Snackbar.LENGTH_LONG).show();
                        } else {
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
                                    int paper_id = jsonObject.getInt("paper_id");

                                        list.add(new CommonModel(title, link, paper_id, branch_name));

                                }
                                paperAdapter = new PaperAdapter(SearchedResultsActivity.this, sub_code, list, coordinatorLayout, 2);
                                recyclerView.setAdapter(paperAdapter);
                                dialog.dismiss();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.getMessage());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("seachedresult", "yes");
                map.put("keyword",keyword);
                map.put("selectedoption",selectedoption);
                return map;
            }
        };
        Mysingleton.getInstance(getApplicationContext()).addToRequestQuee(stringRequest1);

    }
}