package sk.android.examscorer.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import sk.android.examscorer.Activity.PapersActivity;
import sk.android.examscorer.Models.CommonModel;
import sk.android.examscorer.R;
import sk.android.examscorer.Utills.RecyclerViewClickListener;

/**
 * Created by sonu on 15/9/18.
 */

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.Viewholder> {
    Context context;
    List<CommonModel> list;
    private int lastPosition =-1;
    String semester;
    String branch_title;

    public SubjectAdapter(Context context, List<CommonModel> list,String semester,String branch_title) {
        this.context = context;
        this.list = list;
        this.semester =semester;
        this.branch_title =branch_title;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return new Viewholder(LayoutInflater.from(context).inflate(R.layout.custom_subjects_layout,parent,false));

       return new Viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.new_subjects_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
       CommonModel model =  list.get(position);

       holder.heading.setText(model.getSub_heading());
       holder.name.setText(model.getSub_name());
        holder.code.setText(model.getSub_code());
       //holder.getData(position);
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

    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView heading,name,code;
        ImageView newiamge;
        private RecyclerViewClickListener mListener;

        Typeface custom_font = Typeface.createFromAsset(context.getAssets(),  "dark.ttf");
        private static final String TAG = "Viewholder";
        public Viewholder(View itemView) {
            super(itemView);
            newiamge =itemView.findViewById(R.id.new_imge);
            heading =itemView.findViewById(R.id.new_sub_title);
            name =itemView.findViewById(R.id.new_sub_small);
            code =itemView.findViewById(R.id.new_sub_code);
            heading.setTypeface(custom_font,Typeface.BOLD);
            newiamge.setOnClickListener(this);
            heading.setOnClickListener(this);
            name.setOnClickListener(this);
            code.setOnClickListener(this);
            int[] androidColors = context.getResources().getIntArray(R.array.mdcolor_400);
            int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
            newiamge.setColorFilter(randomAndroidColor);
        }

        @Override
        public void onClick(View view) {
            Intent intent =new Intent(context, PapersActivity.class);
            intent.putExtra("sub_code",list.get(getAdapterPosition()).getSub_code());
            intent.putExtra("sub_name",list.get(getAdapterPosition()).getSub_heading());
            intent.putExtra("semester",semester);
            intent.putExtra("branch_title",branch_title);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);



        }
    }
}
