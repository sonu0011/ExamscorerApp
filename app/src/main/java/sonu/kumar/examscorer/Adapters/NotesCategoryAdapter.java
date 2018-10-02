package sonu.kumar.examscorer.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import sonu.kumar.examscorer.Activity.ImagesActivity;
import sonu.kumar.examscorer.Activity.NotesSubCategory;
import sonu.kumar.examscorer.Models.AnotherCommonModel;
import sonu.kumar.examscorer.Models.CommonModel;
import sonu.kumar.examscorer.R;

/**
 * Created by sonu on 19/9/18.
 */

public class NotesCategoryAdapter extends RecyclerView.Adapter<NotesCategoryAdapter.ViewHolder> {
    List<CommonModel> list;
    Context context;
    public static final String TAG ="NOtescategoryadaptere";
    List<AnotherCommonModel> list1;
    int i;

    public NotesCategoryAdapter(Context context, List<AnotherCommonModel> list1, int i) {
        this.context = context;
        this.list1 = list1;
        this.i = i;
        Log.d(TAG, "NotesCategoryAdapter: value of i is 1");
    }

    public NotesCategoryAdapter(List<CommonModel> list, Context context) {
        Log.d(TAG, "NotesCategoryAdapter: constructor");
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        if (i==1){
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_subcat_layout,parent,false));

        }
        else {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_cat_layout,parent,false));

        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (i==1){
            AnotherCommonModel model = list1.get(position);
            Glide.with(context).load(model.getNotes_subcat_image()).into(holder.notes_cat_image);
            holder.notes_sub_cat_title.setText(model.getNotes_sub_cat_title());
            holder.getData(position);
        }
        else {
            Log.d(TAG, "onBindViewHolder: ");
            CommonModel model = list.get(position);
            Glide.with(context).load(model.getNotes_cat_image()).into(holder.notes_cat_image);
            holder.getData(position);
        }


    }

    @Override
    public int getItemCount() {
        if (i==1){
            return  list1.size();
        }
        else {
            return list.size() ;

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView notes_cat_image;
        TextView notes_cat_count,notes_sub_cat_title;
        public ViewHolder(View itemView) {
            super(itemView);
            notes_cat_image =itemView.findViewById(R.id.notes_cat_image);
            notes_cat_count =itemView.findViewById(R.id.notes_count_text);
            if (i==1){
                notes_sub_cat_title =itemView.findViewById(R.id.notes_subcat_title);
            }
        }

        public void getData(int position) {
            if (i!=1){
                final CommonModel data = list.get(position);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent =new Intent(context, NotesSubCategory.class);
                        intent.putExtra("notes_id",data.getNotes_cat_id());
                        intent.putExtra("notes_title",data.getNotes_cat_title());
                        context.startActivity(intent);
                    }
                });
            }
            if (i ==1){

                final AnotherCommonModel model = list1.get(position);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String cat_id = model.getNotes_catid();
                        String subcat_id = model.getNotes_sub_id();
                        String notes_subcat_title = model.getNotes_sub_cat_title();
                        Intent intent = new Intent(context, ImagesActivity.class);
                        intent.putExtra("cat_id", cat_id);
                        intent.putExtra("subcat_id", subcat_id);
                        intent.putExtra("subcat_title", notes_subcat_title);
                        context.startActivity(intent);

                    }
                });
            }


        }

    }
}
