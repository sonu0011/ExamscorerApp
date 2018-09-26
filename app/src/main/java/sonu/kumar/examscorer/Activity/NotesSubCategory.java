package sonu.kumar.examscorer.Activity;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sonu.kumar.examscorer.Adapters.NotesCategoryAdapter;
import sonu.kumar.examscorer.Models.AnotherCommonModel;
import sonu.kumar.examscorer.Models.CommonModel;
import sonu.kumar.examscorer.R;
import sonu.kumar.examscorer.Utills.Constants;
import sonu.kumar.examscorer.Utills.Mysingleton;

public class NotesSubCategory extends AppCompatActivity {
    ImageView imageView;
    RecyclerView recyclerView;
    String notes_id, notes_title;
    private NotesCategoryAdapter notesCategoryAdapter;
    public static final String TAG ="NotesSubcategory";
    private List<AnotherCommonModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_sub_category);
        imageView = findViewById(R.id.notes_sub_cat_images);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.sub_cat_recycleview);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
list =new ArrayList<>();
        notes_id = getIntent().getStringExtra("notes_id");
        notes_title = getIntent().getStringExtra("notes_title");
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        collapsingToolbar.setTitle(notes_title);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        if (notes_id.equals("1")) {
            Glide.with(getApplicationContext()).load(Constants.NOTES_IMG).into(imageView);

        }
        if (notes_id.equals("2")) {
            Glide.with(getApplicationContext()).load(Constants.NOTES_PDF).into(imageView);

        }
        if (notes_id.equals("3")) {
            Glide.with(getApplicationContext()).load(Constants.NOTES_PPT).into(imageView);

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
                                String notes_sub_id = jsonObject.getString("notes_sub_id");
                                String notes_catid = jsonObject.getString("notes_catid");
                                String notes_subcat_image = jsonObject.getString("notes_subcat_image");
                                String notes_subcat_title = jsonObject.getString("notes_subcat_title");


                                list.add(new AnotherCommonModel( notes_sub_id, notes_catid,notes_subcat_image, notes_subcat_title));


                            }
                            Log.d(TAG, "onResponse: list size" + list.size());
                            notesCategoryAdapter = new NotesCategoryAdapter(getApplicationContext(),list,1);
                            recyclerView.setAdapter(notesCategoryAdapter);

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
                map.put("notes_subcat_details", "yes");
                map.put("notes_id",notes_id);
                return map;
            }
        };
        Mysingleton.getInstance(getApplicationContext()).addToRequestQuee(stringRequest1);

    }
}