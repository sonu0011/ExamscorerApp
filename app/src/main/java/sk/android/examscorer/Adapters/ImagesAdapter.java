package sk.android.examscorer.Adapters;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import sk.android.examscorer.Models.AnotherCommonModel;
import sk.android.examscorer.R;

/**
 * Created by sonu on 1/10/18.
 */

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.Viewholder> {
    Context context;
    List<AnotherCommonModel> list;
    ProgressDialog progressDialog;
    private AlertDialog.Builder mbuilder;
    public static final String TAG = "ImagesAdapter";
    ImageView imageView1;
    CoordinatorLayout coordinatorLayout;
    Snackbar snackbar;

    public ImagesAdapter(Context context, List<AnotherCommonModel> list, CoordinatorLayout coordinatorLayout) {
        this.context = context;
        this.list = list;
        progressDialog = new ProgressDialog(context);
        this.coordinatorLayout = coordinatorLayout;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(context).inflate(R.layout.custom_images_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder holder, int position) {

        AnotherCommonModel model = list.get(position);
        Glide.with(context).
                load(model.getNotes_download_link1())
                .placeholder(R.drawable.loader)
                .dontAnimate()
//
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView share, download, cancel;

        public Viewholder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.images_imageview);
        }

        }



    private void saveImage(Bitmap image, String title) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {     // Comment 1
            if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "saveImage: permission granted");
                String savedImagePath = null;

                String imageFileName = title;
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
                    galleryAddPic(savedImagePath, title);
                    progressDialog.dismiss();
                    Toast.makeText(context, "IMAGE SAVED to" + storageDir + "/" + title, Toast.LENGTH_LONG).show();
                }

            } else {
                Log.d(TAG, "saveImage: permission denied");
                progressDialog.dismiss();
                snackbar = Snackbar.make(coordinatorLayout, "We require a write permission. Please allow it in Settings.", Snackbar.LENGTH_INDEFINITE)
                        .setAction("SETTINGS", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                                intent.setData(uri);
                                snackbar.dismiss();
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);

                            }
                        });
                View snackbarView = snackbar.getView();
                TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                textView.setMaxLines(3);    // Comment 4.
                snackbar.show();

            }

        } else {
            String savedImagePath = null;

            String imageFileName = title;
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
                galleryAddPic(savedImagePath, title);
                progressDialog.dismiss();
                Toast.makeText(context, "IMAGE SAVED to" + storageDir + "/" + title, Toast.LENGTH_LONG).show();
            }
        }


    }

    private void galleryAddPic(String imagePath, String title) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);


        File pictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/Examscorer/" + title);
        String[] listofpictures = pictures.list();
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
