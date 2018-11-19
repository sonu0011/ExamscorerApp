package sk.android.examscorer.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sk.android.examscorer.Models.CommonModel;
import sk.android.examscorer.R;
import sk.android.examscorer.Utills.Constants;
import sk.android.examscorer.Utills.Mysingleton;

/**
 * Created by sonu on 16/9/18.
 */

public class PaperAdapter extends RecyclerView.Adapter<PaperAdapter.ViewHolder> {
    int i;
    Context context;
    String sub_code;
    List<CommonModel> list;
    SharedPreferences sharedPreferences;
    CoordinatorLayout coordinatorLayout;
    ImageView noimage;
    TextView notext;

    public PaperAdapter(int i, Context context, List<CommonModel> list, ImageView noimage, TextView notext) {
        this.i = i;
        this.context = context;
        this.list = list;
        this.noimage = noimage;
        this.notext = notext;
    }

    public static final String TAG = "PapersAdapter";
    private int lastPosition = -1;

    public PaperAdapter(int i, Context context, List<CommonModel> list) {
        this.i = i;
        this.context = context;
        this.list = list;

    }

    public PaperAdapter(Context context, String sub_code, List<CommonModel> list, CoordinatorLayout coordinatorLayout, int i) {
        this.i = i;
        this.context = context;
        this.sub_code = sub_code;
        this.list = list;
        this.coordinatorLayout = coordinatorLayout;
        //sharedPreferences = context.getSharedPreferences(Constants.SHARED_KEY, Context.MODE_PRIVATE);


    }

    public PaperAdapter(Context context, String sub_code, List<CommonModel> list, CoordinatorLayout coordinatorLayout) {
        this.context = context;
        this.sub_code = sub_code;
        this.list = list;
        this.coordinatorLayout = coordinatorLayout;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.custom_papers_layout, parent, false));


    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final CommonModel model = list.get(position);
        String result = model.getPaper_title().substring(model.getPaper_title().indexOf("(") + 1, model.getPaper_title().indexOf(")"));
        if (i == 1) {
            // for favorites
            holder.sub_name.setText(model.getPaper_title());
            int color = Color.parseColor("#FF0000");
            holder.paper_fav.setColorFilter(color);
            holder.branch_name.setText(model.getBranch_name());

        } else if (i == 2) {
            //for searched result
            holder.sub_name.setText(model.getPaper_title());
            holder.branch_name.setText(model.getBranch_name());

        } else {
            holder.sub_name.setText(result);
            holder.branch_name.setText(model.getBranch_name());
            StringRequest stringRequest11 = new StringRequest(
                    StringRequest.Method.POST,
                    Constants.Request_Url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "onResponse: bind viewholder " + response);
                            if (response.equals("1")) {
                                int color = Color.parseColor("#FF0000");
                                holder.paper_fav.setColorFilter(color);
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "onErrorResponse: " + error.toString());

                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    sharedPreferences = context.getSharedPreferences(Constants.SHARED_KEY, Context.MODE_PRIVATE);

                    int user_id = sharedPreferences.getInt(Constants.LOGIN_USER_ID, 0);
                    Map<String, String> map = new HashMap<>();
                    map.put("run_time_fav", "yes");
                    map.put("user_id", String.valueOf(user_id));
                    map.put("paper_id", String.valueOf(model.getPaper_id()));
                    return map;
                }
            };

            Mysingleton.getInstance(context).addToRequestQuee(stringRequest11);

        }


        holder.getData(position);
        if (position > lastPosition) {

            Animation animation = AnimationUtils.loadAnimation(context,
                    R.anim.animation_while_scrolling);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }

    }

    @Override
    public int getItemCount() {
        if (list.size() ==0){
            if (noimage!=null && notext !=null) {
                noimage.setVisibility(View.VISIBLE);
                notext.setVisibility(View.VISIBLE);
            }
        }
        return list.size();
    }

    public void FilterList(ArrayList<CommonModel> arrayList) {
        this.list =arrayList;
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sub_name, branch_name;
        ImageView paper_fav, paper_download, paper_share, remove;
        LinearLayout linearLayout;
        Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "roboto.ttf");

        public ViewHolder(View itemView) {
            super(itemView);
            sub_name = itemView.findViewById(R.id.papers_mst);
            paper_fav = itemView.findViewById(R.id.papers_fav);
            paper_download = itemView.findViewById(R.id.papers_download);
            paper_share = itemView.findViewById(R.id.papers_share);
            branch_name = itemView.findViewById(R.id.branch_name);
            remove = itemView.findViewById(R.id.remove_from_fav);
            linearLayout = itemView.findViewById(R.id.brnch_name_layout);
            sub_name.setTypeface(custom_font, Typeface.BOLD);
            branch_name.setTypeface(custom_font, Typeface.BOLD);

            if (i == 1) {
                // for  favourites
                remove.setVisibility(View.VISIBLE);
            }
        }

        public void getData(final int position) {
            final CommonModel model = list.get(position);
            paper_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");

                    intent.putExtra(Intent.EXTRA_TEXT, model.getPaper_link());
//                   intent.putExtra(Intent.EXTRA_CC,"fghsdja");
//                   intent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id=com.agbe.jaquar");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(Intent.createChooser(intent, "Share Via"));

                }
            });
            paper_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(model.getPaper_link()));
                    browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(Intent.createChooser(browserIntent, "Download"));

                }
            });
            if (i != 1) {
                paper_fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        StringRequest stringRequest = new StringRequest(
                                StringRequest.Method.POST,
                                Constants.Request_Url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d(TAG, "onResponse: fac clicke" + response);
                                        Log.d(TAG, "onResponse: " + response);
                                        if (response.equals("1")) {

                                            StringRequest stringRequest1 = new StringRequest(
                                                    StringRequest.Method.POST,
                                                    Constants.Request_Url,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            Log.d(TAG, "onResponse: delete equals 1 " + response);
                                                            if (response.equals("0")) {
                                                                int color = Color.parseColor("#39000000");
                                                                paper_fav.setColorFilter(color);
                                                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Removed from favourite", Snackbar.LENGTH_SHORT)
                                                                        ;
                                                                View snackbarView = snackbar.getView();
                                                                TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                                                                textView.setTextColor(Color.WHITE);
                                                                snackbar.show();

                                                            }

                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {

                                                        }
                                                    }
                                            ) {
                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    sharedPreferences = context.getSharedPreferences(Constants.SHARED_KEY, Context.MODE_PRIVATE);

                                                    int user_id = sharedPreferences.getInt(Constants.LOGIN_USER_ID, 0);
                                                    Map<String, String> map = new HashMap<>();
                                                    map.put("removed_fav", "yes");
                                                    map.put("user_id", String.valueOf(user_id));
                                                    map.put("paper_id", String.valueOf(model.getPaper_id()));
                                                    return map;
                                                }
                                            };

                                            Mysingleton.getInstance(context).addToRequestQuee(stringRequest1);


                                        }
                                        if (response.equals("0")) {
                                            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Added to favourite", Snackbar.LENGTH_SHORT);
                                            View snackbarView = snackbar.getView();
                                            TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                                            textView.setTextColor(Color.WHITE);
                                            snackbar.show();
                                            int color = Color.parseColor("#FF0000");
                                            paper_fav.setColorFilter(color);


                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }
                        ) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                sharedPreferences = context.getSharedPreferences(Constants.SHARED_KEY, Context.MODE_PRIVATE);

                                int user_id = sharedPreferences.getInt(Constants.LOGIN_USER_ID, 0);
                                Map<String, String> map = new HashMap<>();
                                map.put("set_fav", "yes");
                                map.put("user_id", String.valueOf(user_id));
                                map.put("paper_id", String.valueOf(model.getPaper_id()));

                                return map;
                            }
                        };
                        Mysingleton.getInstance(context).addToRequestQuee(stringRequest);
                    }
                });
            }
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Remove");
                    builder.setMessage("Do you want to Remove ? ");
                    builder.setIcon(R.drawable.ic_delete_black_24dp);

                    builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            StringRequest stringRequest1 = new StringRequest(
                                    StringRequest.Method.POST,
                                    Constants.Request_Url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.d(TAG, "onResponse: delete button clicked 1 " + response);
                                            list.remove(position);

                                            notifyItemRemoved(position);
                                            notifyItemRangeChanged(position, list.size());

                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    }
                            ) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    sharedPreferences = context.getSharedPreferences(Constants.SHARED_KEY, Context.MODE_PRIVATE);

                                    int user_id = sharedPreferences.getInt(Constants.LOGIN_USER_ID, 0);
                                    Map<String, String> map = new HashMap<>();
                                    map.put("removed_fav", "yes");
                                    map.put("user_id", String.valueOf(user_id));
                                    map.put("paper_id", String.valueOf(model.getPaper_id()));
                                    return map;
                                }
                            };

                            Mysingleton.getInstance(context).addToRequestQuee(stringRequest1);


                        }
                    });
                    builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();

                        }
                    });
                    builder.create();
                    builder.show();


                }
            });
        }
    }
}
