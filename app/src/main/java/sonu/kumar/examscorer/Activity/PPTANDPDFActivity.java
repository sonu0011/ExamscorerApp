package sonu.kumar.examscorer.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import sonu.kumar.examscorer.Adapters.PPT_AND_PDF_ADAPTER;
import sonu.kumar.examscorer.Adapters.PaperAdapter;
import sonu.kumar.examscorer.Models.AnotherCommonModel;
import sonu.kumar.examscorer.Models.CommonModel;
import sonu.kumar.examscorer.R;
import sonu.kumar.examscorer.Utills.Constants;
import sonu.kumar.examscorer.Utills.Mysingleton;

public class PPTANDPDFActivity extends AppCompatActivity {
    String cat_id,subcat_id,subcat_title;
    public static final String TAG="PPTANDPDFACTIVITY";
    PPT_AND_PDF_ADAPTER adapter;
    RecyclerView recyclerView;
    List<AnotherCommonModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pptandpdf);
        cat_id =getIntent().getStringExtra("cat_id");
        subcat_id =getIntent().getStringExtra("subcat_id");
        subcat_title =getIntent().getStringExtra("subcat_title");

        getSupportActionBar().setTitle(subcat_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        list =new ArrayList<>();
        recyclerView =findViewById(R.id.ppt_pdf_recycleview);
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
                                String notes_pdf_link = jsonObject.getString("notes_pdf_link");
                                String notes_ppt_title = jsonObject.getString("notes_ppt_title");


                                list.add(new AnotherCommonModel( notes_pdf_link,notes_ppt_title));


                            }
                            adapter = new PPT_AND_PDF_ADAPTER(getApplicationContext(),list);
                            recyclerView.setAdapter(adapter);

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
                map.put("pdf_pdf_details", "yes");
                map.put("cat_id",cat_id);
                map.put("subcat_id",subcat_id);
                return map;
            }
        };
        Mysingleton.getInstance(getApplicationContext()).addToRequestQuee(stringRequest1);

    }
}
