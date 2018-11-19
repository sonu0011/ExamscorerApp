package sk.android.examscorer.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import sk.android.examscorer.R;
import sk.android.examscorer.Utills.Constants;
import sk.android.examscorer.Utills.Mysingleton;

/**
 * Created by sonu on 14/9/18.
 */

public class CustomPagerAdapter extends android.support.v4.view.PagerAdapter {
    Context context;
    public static final String TAG ="PagerAdapter";
    LayoutInflater layoutInflater;
    public  String[] images ;
    public  int[] imag  ={R.drawable.intro_papers,R.drawable.intro_signup,R.drawable.intro_signin};
    public  String[] heading;
    //={"Question Papers","Create Your Account","Sign In To Your Account"};
    public  String[] description;
    //description ={"Download B-Tech PTU Main Campus Question Papers","Visit examscorer.co.in/signup and create your account in free",
           // "After creating your account you can signin to app as well as to website"};

    public CustomPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view  =LayoutInflater.from(context).inflate(R.layout.intro_layout,container,false);
        final ImageView intro_image =view.findViewById(R.id.intro_image);
        final TextView intro_heading =view.findViewById(R.id.intro_heading);
        final TextView intro_desc =view.findViewById(R.id.intro_desc);
        intro_image.setImageResource(imag[position]);
        StringRequest sr = new StringRequest(
                StringRequest.Method.POST,
                Constants.Request_Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: "+response);
                        try {
                            JSONArray jsonArray =new JSONArray(response);
                            images =new String[jsonArray.length()];
                            heading =new String[jsonArray.length()];
                            description =new String[jsonArray.length()];
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject =jsonArray.getJSONObject(i);
                                String image =  jsonObject.getString("intro_image");
                                String  heading1 =jsonObject.getString("intro_heading");
                                String  desc =jsonObject.getString("intro_desc");
                                images[i] =image;
                                heading[i] =heading1;
                                description[i] =desc;


                            }
                            if (position ==0){
                                intro_heading.setText(heading[position]);
                                intro_desc.setText(description[position]);
                            }
                            else {
                                intro_heading.setText(heading[position]);
                                intro_desc.setText(description[position]);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: "+error.toString());

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map =new HashMap<>();
                map.put("intro_details","yes");
                return  map;
            }
        };
        Mysingleton.getInstance(context).addToRequestQuee(sr);



        container.addView(view);

        return  view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
