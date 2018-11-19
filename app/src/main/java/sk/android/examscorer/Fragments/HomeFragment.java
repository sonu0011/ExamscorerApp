package sk.android.examscorer.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import sk.android.examscorer.Adapters.BranchAdapter;
import sk.android.examscorer.Models.CommonModel;
import sk.android.examscorer.R;
import sk.android.examscorer.Utills.Constants;
import sk.android.examscorer.Utills.Mysingleton;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    View view;
    List<CommonModel>list;
    public static final String TAG   ="HomeFragment";
    BranchAdapter branchAdapter;
    ProgressDialog progressDialog;
    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        list =new ArrayList<>();
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.common_recycleview);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager manager =new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(manager);
        return  view;

    }

    @Override
    public void onStart() {
        super.onStart();
        final ProgressDialog dialog = new Constants().showDialog(getContext());
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
                                String id = jsonObject.getString("branch_id");
                                String title =jsonObject.getString("branch_title");
                                String image = jsonObject.getString("branch_image");
                                String headin = jsonObject.getString("branch_heading");
                                Log.d(TAG, "onResponse: "+id+image+headin);
                                list.add(new CommonModel(id,image,headin,title,7));

                            }branchAdapter =new BranchAdapter(list,getActivity());
                            recyclerView.setAdapter(branchAdapter);
                            dialog.dismiss();
                           // progressDialog.dismiss();

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
                map.put("Branch_Details","yes");

                return map;
            }
        };
        Mysingleton.getInstance(getContext()).addToRequestQuee(stringRequest);

    }
}
