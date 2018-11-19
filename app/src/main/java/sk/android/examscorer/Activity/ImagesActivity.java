package sk.android.examscorer.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentTransaction;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sk.android.examscorer.Adapters.GalleryAdapter;
import sk.android.examscorer.Adapters.ImagesAdapter;
import sk.android.examscorer.BroadcastReceivers.CheckInternetConnection;
import sk.android.examscorer.Fragments.SlideshowDialogFragment;
import sk.android.examscorer.Models.AnotherCommonModel;
import sk.android.examscorer.R;
import sk.android.examscorer.Utills.Constants;
import sk.android.examscorer.Utills.Mysingleton;

public class ImagesActivity extends AppCompatActivity {
String notes_cat_id,notes_subcat_id,notes_subcat_title;
    private CheckInternetConnection checkInternetConnection;
    RecyclerView recyclerView;
    ImagesAdapter adapter;
    ArrayList<AnotherCommonModel> list;
    public static final String TAG ="ImagesActivity";
    ProgressDialog progressDialog;
    CoordinatorLayout coordinatorLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        progressDialog =new ProgressDialog(ImagesActivity.this);
        list =new ArrayList<>();
        notes_cat_id =getIntent().getStringExtra("cat_id");
        notes_subcat_id =getIntent().getStringExtra("subcat_id");
        notes_subcat_title =getIntent().getStringExtra("subcat_title");
coordinatorLayout =findViewById(R.id.imagecoor);
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
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(ImagesActivity.this, R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(gridLayoutManager);

    }

    public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

        private int mItemOffset;

        public ItemOffsetDecoration(int itemOffset) {
            mItemOffset = itemOffset;
        }

        public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
            this(context.getResources().getDimensionPixelSize(itemOffsetId));
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
        }
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

        final ProgressDialog dialog = new Constants().showDialog(ImagesActivity.this);
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
                            adapter = new ImagesAdapter(ImagesActivity.this, list,coordinatorLayout);
                            recyclerView.setAdapter(adapter);
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
                map.put("images_details", "yes");
                map.put("cat_id", notes_cat_id);
                map.put("subcat_id",notes_subcat_id);
                return map;
            }
        };
        Mysingleton.getInstance(getApplicationContext()).addToRequestQuee(stringRequest1);


    }

    @Override
    protected void onResume() {
        super.onResume();

        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", list);
                bundle.putInt("position", position);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }
}
