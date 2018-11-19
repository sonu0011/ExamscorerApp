package sk.android.examscorer.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sk.android.examscorer.Models.CommonModel;
import sk.android.examscorer.R;

/**
 * Created by sonu on 19/9/18.
 */

public class ReappearAdapter extends RecyclerView.Adapter<ReappearAdapter.ViewHolder> {
    List<CommonModel>list;
    Context context;
    private int lastPosition =-1;

    public ReappearAdapter(List<CommonModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.custom_reappears_paper,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       CommonModel model =  list.get(position);
       holder.reappear_paper_code.setText(model.getSp_code());
       holder.reappear_title.setText(model.getSp_title());
       holder.getData(position);
        if(position >lastPosition) {

            Animation animation = AnimationUtils.loadAnimation(context,
                    R.anim.animation_while_scrolling);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void FilterList(ArrayList<CommonModel> arrayList) {
        list =arrayList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView reappear_paper_code,reappear_title;
        ImageView download,share;
        Typeface custom_font = Typeface.createFromAsset(context.getAssets(),  "roboto.ttf");

        public ViewHolder(View itemView) {
            super(itemView);
            reappear_paper_code =itemView.findViewById(R.id.reappear_sub_code);
            reappear_title =itemView.findViewById(R.id.reappear_sub_title);
            reappear_title.setTypeface(custom_font, Typeface.BOLD);
            reappear_paper_code.setTypeface(custom_font);
            download =itemView.findViewById(R.id.reappear_download);
            share =itemView.findViewById(R.id.reappear_share);
        }

        public void getData(int position) {

           final CommonModel model =  list.get(position);
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");

                    intent.putExtra(Intent.EXTRA_TEXT, model.getSp_link());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(Intent.createChooser(intent, "Share Via"));

                }
            });
            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(model.getSp_link()));
                    browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(Intent.createChooser(browserIntent, "Download"));


                }
            });

        }
    }
}
