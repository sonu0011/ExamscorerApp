package sonu.kumar.examscorer.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import javax.microedition.khronos.opengles.GL;

import sonu.kumar.examscorer.Models.AnotherCommonModel;
import sonu.kumar.examscorer.R;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by sonu on 1/10/18.
 */

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.Viewholder> {
    Context context;
    List<AnotherCommonModel> list;
    ProgressDialog progressDialog;
    private boolean isImageFitToScreen;
    private AlertDialog.Builder mbuilder;
    AlertDialog alertDialog;
    public static final String TAG="ImagesAdapter";

    ImageView imageView1;
    ProgressBar progressBar;
    public ImagesAdapter(Context context, List<AnotherCommonModel> list, ProgressBar bar) {
        this.context = context;
        this.list = list;
        progressDialog =new ProgressDialog(context);
        progressBar =bar;
        Log.d(TAG, "ImagesAdapter: "+bar);
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(context).inflate(R.layout.custom_images_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder holder, int position) {
        //final ProgressDialog dialog =new ProgressDialog(context);
       // dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
       // dialog.show();

        AnotherCommonModel model =  list.get(position);
        Glide.with(context).
                load(model.getNotes_download_link1())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        //progressBar.setVisibility(View.GONE);
                       // dialog.dismiss();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        //progressBar.setVisibility(View.GONE);
                        //dialog.dismiss();
                        return false;
                    }
                })

                .placeholder(R.drawable.loader)
//                .into(new SimpleTarget<GlideDrawable>() {
//                    @Override
//                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                   holder.imageView.setImageDrawable(resource);
//                    }
//                });
        .into(holder.imageView);
        holder.getData(position);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView share, download,cancel;

        public Viewholder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.images_imageview);



        }

        public void getData(int position) {
            final AnotherCommonModel model = list.get(position);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    mbuilder = new AlertDialog.Builder(context);
                    View view1 = LayoutInflater.from(context).inflate(R.layout.custom_full_image_layout, null);
                    mbuilder.setView(view1);
                     mbuilder.create();
                   final AlertDialog dialog = mbuilder.show();
                    imageView1 = view1.findViewById(R.id.downlaod_imge);
                    share = view1.findViewById(R.id.download_share);
                    download =view1.findViewById(R.id.download_download);
                    cancel =view1.findViewById(R.id.download_cancel);
                    share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("text/plain");

                            intent.putExtra(Intent.EXTRA_TEXT, model.getNotes_download_link1());

                            context.startActivity(Intent.createChooser(intent, "Share Via"));
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();


                        }
                    });
                    download.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.setMessage("Downloading image..");
                            progressDialog.show();
                            Glide.with(context)
                                    .load(model.getNotes_download_link1())
                                    .asBitmap()
                                    .into(new SimpleTarget<Bitmap>(400, 400) {
                                        @Override
                                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {

                                            saveImage(resource, model.getDownload_imges_title());
                                            dialog.dismiss();
                                        }
                                    });


                        }
                    });


                    PhotoViewAttacher pAttacher = new PhotoViewAttacher(imageView1);


                    pAttacher.update();
                    Glide.with(context).load(model.getNotes_download_link1()).placeholder(R.drawable.loader).into(imageView1);
//                    mbuilder.setPositiveButton("Download", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            progressDialog.setCanceledOnTouchOutside(false);
//                            progressDialog.setMessage("Downloading image..");
//                            progressDialog.show();
//                            Glide.with(context)
//                                    .load(model.getNotes_download_link1())
//                                    .asBitmap()
//                                    .into(new SimpleTarget<Bitmap>(400, 400) {
//                                        @Override
//                                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
//
//                                            saveImage(resource, model.getDownload_imges_title());
//                                        }
//                                    });
//
//                        }
//                    })
//                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    dialogInterface.cancel();
//                                }
//                            });



                }
            });




//            download.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    progressDialog.setCanceledOnTouchOutside(false);
//                    progressDialog.setMessage("Downloading image..");
//                    progressDialog.show();
//                    Glide.with(context)
//                            .load(model.getNotes_download_link1())
//                            .asBitmap()
//                            .into(new SimpleTarget<Bitmap>(400,400) {
//                                @Override
//                                public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation)  {
//
//                                    saveImage(resource,model.getDownload_imges_title());
//                                }
//                            });
//
//
//
//                }
//            });
//            share.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(Intent.ACTION_SEND);
//                    intent.setType("text/plain");
//
//                    intent.putExtra(Intent.EXTRA_TEXT, model.getNotes_download_link1());
//
//                    context.startActivity(Intent.createChooser(intent, "Share Via"));
//                }
//            });
        }

    }
    private String saveImage(Bitmap image,String title) {
        String savedImagePath = null;


        String imageFileName =title;
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/Examscorer");
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the image to the system gallery
            galleryAddPic(savedImagePath,title);
            progressDialog.dismiss();
            Toast.makeText(context, "IMAGE SAVED to"+storageDir+"/"+title, Toast.LENGTH_LONG).show();
        }
        return savedImagePath;
    }
    private void galleryAddPic(String imagePath,String title) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);


        File pictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/Examscorer/"+title);
        String[] listofpictures =pictures.list();
//        for (String picture:listofpictures){
//
//        }
//        Uri uri =Uri.parse(String.valueOf(pictures));
//        Log.d(TAG, "galleryAddPic: "+uri);
//        Intent intent  =new Intent(Intent.ACTION_VIEW,uri);
//     intent.setType("image/*");
//        context.startActivity(Intent.createChooser(intent,"View Downloaded Image"));




    }
}
