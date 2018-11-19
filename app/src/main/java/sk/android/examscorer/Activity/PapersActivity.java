package sk.android.examscorer.Activity;

import android.app.ProgressDialog;
import android.support.v4.view.ViewPager;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

public class PapersActivity extends AppCompatActivity {

    public static final String TAG = "PapersActivity";
    private ViewPager viewPager;
    private TabLayout mTabLayout;
    Toolbar toolbar;
    String year = "";
    RecyclerView recyclerView;
    String sub_code;
    PaperAdapter paperAdapter;
    CoordinatorLayout coordinatorLayout;
    List<CommonModel> list1;
    List<CommonModel> list;
    private CheckInternetConnection checkInternetConnection;
    private String sub_name;
    private String semester,branch_title;
    TextView nofoundquesiotnpaper,notice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_papers);
        initViews();
        list = new ArrayList<>();
        coordinatorLayout = findViewById(R.id.coordinatatelayout);
        list = new ArrayList<>();
        list1 = new ArrayList<>();
        recyclerView = findViewById(R.id.viewpagerrecycleview);
        recyclerView.setHasFixedSize(true);
        branch_title =  getIntent().getStringExtra("branch_title");
       nofoundquesiotnpaper =findViewById(R.id.quesitonPaperstitle);
       notice =findViewById(R.id.notice);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        checkInternetConnection = new CheckInternetConnection();
        registerReceiver(checkInternetConnection, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (checkInternetConnection != null) {
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void initViews() {
        sub_code = getIntent().getStringExtra("sub_code");
        sub_name = getIntent().getStringExtra("sub_name");
        semester = getIntent().getStringExtra("semester");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (sub_code != null) {
            getSupportActionBar().setTitle(sub_name);
            getSupportActionBar().setSubtitle(semester);


        }
        viewPager = findViewById(R.id.viewpager);
        mTabLayout = findViewById(R.id.tabs);
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(final TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                final ProgressDialog dialog = new Constants().showDialog(PapersActivity.this);
                StringRequest stringRequest1 = new StringRequest(StringRequest.Method.POST,
                        Constants.Request_Url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d(TAG, "onResponse: "+response);
                                if (response.equals("[]")){
                                    nofoundquesiotnpaper.setVisibility(View.VISIBLE);
                                    notice.setVisibility(View.VISIBLE);
                                    dialog.dismiss();
                                    return;
                                }
                                JSONArray jsonArray = null;
                                try {
                                    jsonArray = new JSONArray(response);
                                    list1.clear();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String title = jsonObject.getString("paper_title");
                                        String link = jsonObject.getString("paper_link");
                                        String sub_code = jsonObject.getString("subcode");
                                        int paper_id = jsonObject.getInt("paper_id");
                                        //sub code here branch_name in model


                                        list1.add(new CommonModel(title, link, paper_id, sub_code));


                                    }
                                    paperAdapter = new PaperAdapter(PapersActivity.this, sub_code, list1, coordinatorLayout, 3);
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
                        Log.d(TAG, "onErrorResponse: " + error.getLocalizedMessage());

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("papers_details_year", "yes");
                        map.put("year", (String) tab.getText());
                        map.put("sub_code", sub_code);
                        map.put("branch_name",branch_title);

                        return map;
                    }
                };
                Mysingleton.getInstance(PapersActivity.this).addToRequestQuee(stringRequest1);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        setDynamicFragmentToTabLayout();
    }

    private void setDynamicFragmentToTabLayout() {


        final ProgressDialog dialog = new Constants().showDialog(PapersActivity.this);
        StringRequest stringRequest1 = new StringRequest(StringRequest.Method.POST,
                Constants.Request_Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        if (response.equals("[]")){
                            nofoundquesiotnpaper.setVisibility(View.VISIBLE);
                            notice.setVisibility(View.VISIBLE);
                            dialog.dismiss();

                            return;
                        }
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response);
                            list.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Log.d(TAG, "onResponse: " + i);
                                if (i == 0) {
                                    //first time

                                    Log.d(TAG, "onResponse:for valude of i  +" + i);
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String title = jsonObject.getString("paper_title");
                                    String link = jsonObject.getString("paper_link");
                                    int paper_id = jsonObject.getInt("paper_id");
                                    String year_val = jsonObject.getString("year");
                                    year = year_val;
                                    Log.d(TAG, "onResponse:  1st time " + year);
                                    year = year_val;
                                    mTabLayout.addTab(mTabLayout.newTab().setText(year));

                                }
                                if (i > 0) {
                                    Log.d(TAG, "onResponse: value of i is" + i);
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String title = jsonObject.getString("paper_title");
                                    String link = jsonObject.getString("paper_link");
                                    int paper_id = jsonObject.getInt("paper_id");
                                    String year1 = jsonObject.getString("year");

                                    Log.d(TAG, "onResponse: after first value" + year);
                                    if (!year.equals(year1)) {
                                        mTabLayout.addTab(mTabLayout.newTab().setText(year1));

                                    }
                                    year = year1;

                                }
                            }
                            for (int i = 0; i < mTabLayout.getTabCount(); i++) {

                                View tab = ((ViewGroup) mTabLayout.getChildAt(0)).getChildAt(i);

                                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
                                p.setMargins(5, 0, 50, 0);

                                tab.requestLayout();
                            }

                            dialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.toString());
                Log.d(TAG, "onErrorResponse: " + error.getLocalizedMessage());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("papers_details", "yes");
                map.put("sub_code", sub_code);
                map.put("branch_title",branch_title);
                return map;
            }
        };
        Mysingleton.getInstance(getApplicationContext()).addToRequestQuee(stringRequest1);


    }
}
