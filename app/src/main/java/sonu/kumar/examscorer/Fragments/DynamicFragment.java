package sonu.kumar.examscorer.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

import sonu.kumar.examscorer.Activity.PapersActivity;
import sonu.kumar.examscorer.Adapters.PaperAdapter;
import sonu.kumar.examscorer.Models.CommonModel;
import sonu.kumar.examscorer.R;
import sonu.kumar.examscorer.Utills.Constants;
import sonu.kumar.examscorer.Utills.Mysingleton;


public class DynamicFragment extends Fragment {
    private static final String TAG = "DynamicFragment";
    private List<CommonModel> list;
    PaperAdapter paperAdapter;
    private RecyclerView recyclerView;
    private CoordinatorLayout coordinatorLayout;


    public static DynamicFragment newInstance() {
        Log.d(TAG, "newInstance: ");
        return new DynamicFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.dynamic_fragment_layout, container, false);
        return view;
    }

//    private void initViews(View view) {
//       final String year =  getArguments().getString("year");
//       final String subcode =  getArguments().getString("subcode");
//        Log.d(TAG, "initViews: "+year+subcode);
//
//
//        list = new ArrayList<>();
//        coordinatorLayout = view.findViewById(R.id.papers_coo);
//        recyclerView = view.findViewById(R.id.sub_recycleview);
//        recyclerView.setHasFixedSize(true);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
//        recyclerView.setLayoutManager(gridLayoutManager);
//        Log.d(TAG, "initViews: " + year + "subcode" + subcode);
////        textView.setText(String.valueOf("Category :  "+getArguments().getInt("position")));
//
//        final ProgressDialog dialog = new Constants().showDialog(getContext());
//        StringRequest stringRequest1 = new StringRequest(StringRequest.Method.POST,
//                Constants.Request_Url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d(TAG, "onResponse: " + response);
//                        JSONArray jsonArray = null;
//                        try {
//                            jsonArray = new JSONArray(response);
//                            list.clear();
//                            for (int i = 0; i < jsonArray.length(); i++) {
//
//
//                                //first time
//                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                String title = jsonObject.getString("paper_title");
//                                String link = jsonObject.getString("paper_link");
//                                int paper_id = jsonObject.getInt("paper_id");
//
//
//                                list.add(new CommonModel(title, link, paper_id, ""));
//
//
//                            }
//
//
//                            paperAdapter = new PaperAdapter(getContext(), subcode, list, coordinatorLayout, 3);
//                            recyclerView.setAdapter(paperAdapter);
//                            dialog.dismiss();
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d(TAG, "onErrorResponse: " + error.toString());
//                Log.d(TAG, "onErrorResponse: " + error.getLocalizedMessage());
//
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<>();
//                map.put("papers_details_year", "yes");
//                map.put("year", getArguments().getString("year"));
//                map.put("sub_code", getArguments().getString("subcode"));
//
//                return map;
//            }
//        };
//        Mysingleton.getInstance(getContext()).addToRequestQuee(stringRequest1);
//
//
//    }

}
