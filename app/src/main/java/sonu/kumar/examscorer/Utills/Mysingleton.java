package sonu.kumar.examscorer.Utills;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by sonu on 14/9/18.
 */
public class Mysingleton {
    private static Mysingleton mySingleton;
    private RequestQueue requestQueue;
    private  static Context ctx;
    private Mysingleton(Context context){
        ctx =context;
        requestQueue = getRequestQueue();

    }
    public RequestQueue getRequestQueue(){
        if (requestQueue  == null){
            requestQueue = Volley.newRequestQueue(ctx);
        }
        return  requestQueue;
    }
    public static  synchronized Mysingleton getInstance(Context context){
        if (mySingleton == null){
            mySingleton =new Mysingleton(context);
        }
        return mySingleton;
    }
    public<T>  void  addToRequestQuee(Request<T> request){
        requestQueue.add(request);
    }
}
