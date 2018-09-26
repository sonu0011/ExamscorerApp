package sonu.kumar.examscorer.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import sonu.kumar.examscorer.Models.AnotherCommonModel;
import sonu.kumar.examscorer.R;

/**
 * Created by sonu on 26/9/18.
 */

public class PPT_AND_PDF_ADAPTER extends RecyclerView.Adapter<PPT_AND_PDF_ADAPTER.Vieholder>{
    Context context;
    List<AnotherCommonModel> list;

    public PPT_AND_PDF_ADAPTER(Context context, List<AnotherCommonModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Vieholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vieholder(LayoutInflater.from(context).inflate(R.layout.download_pdf_ppt_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Vieholder holder, int position) {
        AnotherCommonModel model = list.get(position);
        holder.titel.setText(model.getNotes_pdf_title());
        holder.setData(position);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Vieholder extends RecyclerView.ViewHolder {
        TextView titel;
        ImageView share,download;
        public Vieholder(View itemView) {
            super(itemView);
            titel =itemView.findViewById(R.id.pdf_ppt_download_title);
            share =itemView.findViewById(R.id.pdf_ppt_share_icon);
            download =itemView.findViewById(R.id.pdf_ppt_download_icon);
        }

        public void setData(int position) {

        }
    }
}
