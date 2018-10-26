package sonu.kumar.examscorer.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.List;

import sonu.kumar.examscorer.Activity.PapersActivity;
import sonu.kumar.examscorer.Models.CommonModel;
import sonu.kumar.examscorer.R;

/**
 * Created by sonu on 15/9/18.
 */

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.Viewholder> {
    Context context;
    List<CommonModel> list;
    private int lastPosition =-1;

    public SubjectAdapter(Context context, List<CommonModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(context).inflate(R.layout.custom_subjects_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
       CommonModel model =  list.get(position);

       holder.heading.setText(model.getSub_heading());
       holder.name.setText(model.getSub_name());
        holder.code.setText(model.getSub_code());
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
        TextView heading,name,code;
        Typeface custom_font = Typeface.createFromAsset(context.getAssets(),  "roboto.ttf");

        public Viewholder(View itemView) {
            super(itemView);
            heading =itemView.findViewById(R.id.sub_heading);
            name =itemView.findViewById(R.id.sub_small);
            code =itemView.findViewById(R.id.sub_code);
            heading.setTypeface(custom_font,Typeface.BOLD);
            code.setTypeface(custom_font,Typeface.BOLD);
            name.setTypeface(custom_font);
            code.setTextColor(Color.BLACK);
        }

        public void getData(int position) {
            final CommonModel model = list.get(position);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent =new Intent(context, PapersActivity.class);
                    intent.putExtra("sub_code",model.getSub_code());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(intent);
                }
            });
        }
    }
}
