package sonu.kumar.examscorer.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import sonu.kumar.examscorer.BroadcastReceivers.CheckInternetConnection;
import sonu.kumar.examscorer.R;
import sonu.kumar.examscorer.Utills.Constants;
import sonu.kumar.examscorer.Utills.Mysingleton;

public class ProfileActivity extends AppCompatActivity {
    CircleImageView circularImageView;
    Button save_changes;
    EditText update_user_name;
    SharedPreferences sharedPreferences;
    int user_id;
    Uri ImageUri;
    String imageurl,profile_image_from_shared,user_name_from_shaared;
    private ProgressDialog progressDialog;
    private Bitmap bitmap;
    public static final String TAG ="ProfileActivity";
    private CheckInternetConnection checkInternetConnection;
    Snackbar snackbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        circularImageView =findViewById(R.id.update_profile_image);
        save_changes =findViewById(R.id.update_save_changes_btn);
        update_user_name =findViewById(R.id.update_profile_user_name);
        progressDialog =new ProgressDialog(ProfileActivity.this);

        sharedPreferences =getSharedPreferences(Constants.SHARED_KEY,MODE_PRIVATE);
        user_id =sharedPreferences.getInt(Constants.LOGIN_USER_ID,0);
        profile_image_from_shared =sharedPreferences.getString(Constants.LOGIN_USER_IMAGE,null);
        user_name_from_shaared =sharedPreferences.getString(Constants.LOGIN_USER_Name,null);



        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        checkInternetConnection = new CheckInternetConnection();
        registerReceiver(checkInternetConnection, intentFilter);

        Log.d(TAG, "onCreate: image is"+profile_image_from_shared+" user name is"+user_name_from_shaared);

        if (profile_image_from_shared !=null) {
            if (profile_image_from_shared.length() > 0) {
                Log.d(TAG, "onCreate: imnage is  " + profile_image_from_shared);
                Glide.with(ProfileActivity.this).load(profile_image_from_shared).into(circularImageView);
            }
        }
        if (user_name_from_shaared !=null) {
            if (user_name_from_shaared.length() > 0) {
                Log.d(TAG, "onCreate: name " + user_name_from_shaared);
                update_user_name.setText(user_name_from_shaared);
            }
        }

//        StringRequest stringRequest1 = new StringRequest(StringRequest.Method.POST,
//                Constants.Request_Url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d(TAG, "onResponse: "+response);
//                        if (response!=null){
//                            try {
//                                JSONArray jsonArray =new JSONArray(response);
//                                for (int i=0;i<jsonArray.length();i++){
//                                    JSONObject jsonObject =jsonArray.getJSONObject(i);
//                                    String name =jsonObject.getString("user_name");
//                                    if (name !=null){
//                                        update_user_name.setText(name);
//                                    }
//                                    String image =jsonObject.getString("user_profile_pic");
//                                    if (image!=null){
//                                        imageurl =image;
//                                        Log.d(TAG, "onResponse: "+image);
//                                        Glide.with(ProfileActivity.this).load(image).into(circularImageView);
//                                    }
//                                    if (image ==null){
//                                        circularImageView.setImageResource(R.drawable.profile_log);
//                                    }
//
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<>();
//                map.put("fetchprofiledetails", "set");
//                map.put("user_id", String.valueOf(user_id));
//                return map;
//            }
//        };
//        Mysingleton.getInstance(ProfileActivity.this).addToRequestQuee(stringRequest1);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (checkInternetConnection !=null){
            unregisterReceiver(checkInternetConnection);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        circularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: circularimageview");
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                {
                    if(ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
                    }
                    else
                    {
                        if (snackbar!=null){
                            if (snackbar.isShown()){
                                snackbar.dismiss();
                            }
                        }
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(1,1)
                                .start(ProfileActivity.this);
                    }
                }
                else
                {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1,1)
                            .start(ProfileActivity.this);
                }
            }
        });
        save_changes.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                Log.d(TAG, "onClick: ");

                if (profile_image_from_shared != null) {
                    if (profile_image_from_shared.equals("http://192.168.43.126/ExamscorerApp/API/Uploads/")) {
                        if (ImageUri == null) {
                            Toast.makeText(ProfileActivity.this, "Please select a profile image", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (update_user_name.getText().toString().trim().isEmpty()) {
                            Toast.makeText(ProfileActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (ImageUri != null && !update_user_name.getText().toString().isEmpty()) {
                            progressDialog.setMessage("Updating profile...");
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.create();
                            progressDialog.show();
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), ImageUri);
                                UploadImage();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                if (profile_image_from_shared == null && user_name_from_shaared == null) {
                    if (ImageUri == null) {
                        Toast.makeText(ProfileActivity.this, "Please select a profile image", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (update_user_name.getText().toString().trim().isEmpty()) {
                        Toast.makeText(ProfileActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (ImageUri != null && !update_user_name.getText().toString().isEmpty()) {
                        progressDialog.setMessage("Updating profile...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.create();
                        progressDialog.show();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), ImageUri);
                            UploadImage();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
                if (profile_image_from_shared != null) {
                    if (!profile_image_from_shared.equals("http://192.168.43.126/ExamscorerApp/API/Uploads/") && ImageUri == null) {
                        if (update_user_name.getText().toString().trim().isEmpty()) {
                            Toast.makeText(ProfileActivity.this, "Enter your name", Toast.LENGTH_SHORT).show();
                        } else {
                            //update name only
                            final ProgressDialog pd = new ProgressDialog(ProfileActivity.this);
                            pd.setMessage("Updating profile...");
                            pd.setCanceledOnTouchOutside(false);
                            pd.show();

                            StringRequest stringRequest = new StringRequest(StringRequest.Method.POST,
                                    Constants.Request_Url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.d(TAG, "onResponse update name only: " + response);
                                            if (response.equals("1")) {
                                                Log.d(TAG, "onResponse: " + response);
                                                pd.dismiss();
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString(Constants.LOGIN_USER_Name, update_user_name.getText().toString());
                                                editor.apply();

                                                Toast.makeText(ProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(ProfileActivity.this, BranchActivity.class));
                                                finish();
                                            }


                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.d(TAG, "onErrorResponse: " + error.getMessage());

                                        }
                                    }
                            ) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> map = new HashMap<>();
                                    map.put("UpdateName", "set");
                                    map.put("user_id", String.valueOf(user_id));
                                    map.put("user_name", update_user_name.getText().toString());
                                    return map;
                                }
                            };
                            progressDialog.dismiss();
                            Mysingleton.getInstance(ProfileActivity.this).addToRequestQuee(stringRequest);


                        }
                    }

                }
                if (profile_image_from_shared != null)
                {
                    if (ImageUri != null && !profile_image_from_shared.equals("http://192.168.43.126/ExamscorerApp/API/Uploads/")) {
                        if (update_user_name.getText().toString().trim().isEmpty()) {
                            Toast.makeText(ProfileActivity.this, "Enter your name", Toast.LENGTH_SHORT).show();
                        } else {
                            //update n ame and  profile
                            //update shared pref value
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), ImageUri);
                                UploadImage();


                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    }
            }
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: ");
        switch (requestCode) {
            case 2: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (snackbar!=null){
                        if (snackbar.isShown()){
                            snackbar.dismiss();
                        }
                    }// Comment 1.

                    //... we got the permission allowed, now we can try to write the file into storage again ...
                    Log.d(TAG, "onRequestPermissionsResult:  request granted");

                } else {
                    Log.d(TAG, "onRequestPermissionsResult:  permission denied");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        Log.d(TAG, "onRequestPermissionsResult: request denied");
                      snackbar = Snackbar.make(findViewById(android.R.id.content), "We require a write permission. Please allow it in Settings.", Snackbar.LENGTH_INDEFINITE)
                                .setAction("SETTINGS", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                                        intent.setData(uri);
                                        startActivityForResult(intent, 1000);
                                    }
                                });
                        View snackbarView = snackbar.getView();
                        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        textView.setMaxLines(3);    // Comment 4.
                        snackbar.show();
                    }

                }
            }
        }
    }
    private void UploadImage() {
        final ProgressDialog pd1 =new ProgressDialog(ProfileActivity.this);
        pd1.setMessage("Updating profile...");
        pd1.setCanceledOnTouchOutside(false);
        pd1.show();
        StringRequest stringRequest = new StringRequest(StringRequest.Method.POST,
                Constants.Request_Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: "+response);
                        if (response.equals("1")){
                            Log.d(TAG, "onResponse: "+response);
                            if (pd1.isShowing()){
                                pd1.dismiss();

                            }
                           String username =  update_user_name.getText().toString();
                           String withoutspace = username.replaceAll("\\s","");
                            Toast.makeText(ProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onResponse: withoutspace username is "+withoutspace);


                            SharedPreferences.Editor editor =sharedPreferences.edit();
                            editor.putString(Constants.LOGIN_USER_IMAGE,"http://192.168.43.126/ExamscorerApp/API/Uploads/"+withoutspace+user_id+"."+"jpg");
                            editor.putString(Constants.LOGIN_USER_Name,update_user_name.getText().toString());
                            editor.apply();
                            startActivity(new Intent(ProfileActivity.this,BranchActivity.class));
                            finish();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: "+error.getMessage());

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("UpdateProfile", "set");
                map.put("user_id", String.valueOf(user_id));
                map.put("user_name",update_user_name.getText().toString());
                map.put("profile_pic",imageToString(bitmap));
                return map;
            }
        };
        progressDialog.dismiss();
        Mysingleton.getInstance(ProfileActivity.this).addToRequestQuee(stringRequest);
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream =new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imagebytes =byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imagebytes,Base64.DEFAULT);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: ");
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult: "+result.getUri());
                ImageUri = result.getUri();
                circularImageView.setImageURI(result.getUri());

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }    }


}
