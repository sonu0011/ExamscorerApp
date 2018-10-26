package sonu.kumar.examscorer.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.lang.reflect.Type;
import java.util.List;

import sonu.kumar.examscorer.Activity.SubjecstActivity;
import sonu.kumar.examscorer.Models.CommonModel;
import sonu.kumar.examscorer.R;

/**
 * Created by sonu on 15/9/18.
 */

public class SemAdapter extends RecyclerView.Adapter<SemAdapter.Viewholder> {
    List<CommonModel> list;
    Context context;
    public static final String TAG ="Sem ADpter";
    String branch_id;
    private int lastPosition =-1;

    public SemAdapter(List<CommonModel> list, Context context, String branch_id) {
        this.list = list;
        this.context = context;
        this.branch_id = branch_id;
    }

    @NonNull
    @Override
    public SemAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(context).inflate(R.layout.custom_semester_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SemAdapter.Viewholder holder, int position) {
        CommonModel model = list.get(position);
        holder.imageView.setText(model.getSem_title());
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

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView imageView;
        Typeface custom_font = Typeface.createFromAsset(context.getAssets(),  "roboto.ttf");



        public Viewholder(View itemView) {
            super(itemView);
            imageView =itemView.findViewById(R.id.sem_title);
            imageView.setTypeface(custom_font, Typeface.BOLD);

        }

        public void getData(int position) {
            final CommonModel model = list.get(position);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: "+"sem id is "+model.getSem_id()+"Branch id is "+branch_id);
                    Intent intent =new Intent(context,SubjecstActivity.class);
                    intent.putExtra("branch_id",branch_id);
                    intent.putExtra("sem_id",model.getSem_id());
                    intent.putExtra("sem_title",model.getSem_title());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(intent);
                }
            });
        }
    }
}
