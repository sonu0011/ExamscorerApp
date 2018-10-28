package sonu.kumar.examscorer.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import sonu.kumar.examscorer.Activity.SemesterActivity;
import sonu.kumar.examscorer.Models.CommonModel;
import sonu.kumar.examscorer.R;

/**
 * Created by sonu on 15/9/18.
 */

public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.ViewHolder> {
    List<CommonModel> list;
    Context context;
    private int lastPosition = -1;

    public BranchAdapter(List<CommonModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.branch_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommonModel model = list.get(position);
        Glide.with(context).
                load(model.getBrancg_image())
                .into(holder.branch_image);
        holder.branch_title.setText(model.getBranch_heading());
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView branch_image;
        TextView branch_title;
        public ViewHolder(View itemView) {
            super(itemView);
            branch_image =itemView.findViewById(R.id.branch_image);
            //branch_image.setBackgroundColor(Color.BLACK);
           // branch_image.setAlpha((float) 0.7);
            branch_title =itemView.findViewById(R.id.branch_title);
        }

        public void getData(int position) {
            final CommonModel model = list.get(position);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent =new Intent(context, SemesterActivity.class);
                    intent.putExtra("branch_id",model.getBranch_id());
                    intent.putExtra("branch_title",model.getBranch_heading());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }
            });

        }
    }
}
