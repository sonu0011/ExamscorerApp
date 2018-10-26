package sonu.kumar.examscorer.Adapters;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import sonu.kumar.examscorer.Models.AnotherCommonModel;
import sonu.kumar.examscorer.R;

/**
 * Created by sonu on 26/9/18.
 */

public class PPT_AND_PDF_ADAPTER extends RecyclerView.Adapter<PPT_AND_PDF_ADAPTER.Vieholder> {
    String dest_file_path = "test.pdf";
    int downloadedSize = 0, totalsize;
    public static final String TAG = "PPtand pdf adapter";
    Context context;
    List<AnotherCommonModel> list;
    private float per=0;
    private int lastPosition =-1;

    public PPT_AND_PDF_ADAPTER(Context context, List<AnotherCommonModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Vieholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vieholder(LayoutInflater.from(context).inflate(R.layout.download_pdf_ppt_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Vieholder holder, int position) {
        AnotherCommonModel model = list.get(position);
        holder.titel.setText(model.getNotes_download_title());
        holder.setData(position);
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

    public void FilterList(ArrayList<AnotherCommonModel> arrayList) {
        list =arrayList;
        notifyDataSetChanged();
    }

    public class Vieholder extends RecyclerView.ViewHolder {
        TextView titel;
        ImageView share, download;
        Typeface custom_font = Typeface.createFromAsset(context.getAssets(),  "roboto.ttf");


        public Vieholder(View itemView) {
            super(itemView);
            titel = itemView.findViewById(R.id.pdf_ppt_download_title);
            share = itemView.findViewById(R.id.pdf_ppt_share_icon);
            download = itemView.findViewById(R.id.pdf_ppt_download_icon);
            titel.setTypeface(custom_font,Typeface.BOLD);
        }

        public void setData(final int position) {
            final AnotherCommonModel model = list.get(position);

            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: "+model.getNotes_download_link());
                    Intent intent =new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(model.getNotes_download_link()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(intent);


                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");

                    intent.putExtra(Intent.EXTRA_TEXT, model.getNotes_download_link());
//                   intent.putExtra(Intent.EXTRA_CC,"fghsdja");
//                   intent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id=com.agbe.jaquar");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(Intent.createChooser(intent, "Share Using.."));

                }
            });
        }
    }

}
    

