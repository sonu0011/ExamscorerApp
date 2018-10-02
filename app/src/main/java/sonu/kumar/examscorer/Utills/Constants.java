package sonu.kumar.examscorer.Utills;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;

import sonu.kumar.examscorer.Activity.LoginActivity;
import sonu.kumar.examscorer.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sonu on 14/9/18.
 */

public class Constants {
    SharedPreferences sharedPreferences;

    public static final String FIRST_TIME_LAUNCH ="first_time_launch";
    public static final String SHARED_KEY ="KEY";
    public static final String LOGIN_SHARED_PREF ="login_details";
    public static final String LOGIN_USER_Name ="user_name";
    public static final String LOGIN_USER_EMAIL ="user_email";
    public static final String LOGIN_USER_ID = "user_id";
    public static final String REMEMBER_ME ="REMEMBER_ME";
    public static final String LOGIN_USER_IMAGE="user_image";
    public static final String Request_Url ="http://192.168.44.178/ExamscorerApp/API/allrequests.php";
    public static final String NOTES_IMG ="http://192.168.44.178/ExamscorerApp/images/notes_img.jpg";
    public static final String NOTES_PDF ="http://192.168.44.178/ExamscorerApp/images/pdf_img.jpg";
    public static final String NOTES_PPT ="http://192.168.44.178/ExamscorerApp/images/ppt_img.jpg";
    public void ShowLogoutDialog(final Context context){
        sharedPreferences =context.getSharedPreferences(Constants.SHARED_KEY,MODE_PRIVATE);

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Dialog);
        builder
                .setIcon(context.getResources().getDrawable(R.drawable.logout))
                .setTitle("Log out").setMessage("Are you sure you want to Log out?")
                .setCancelable(false)

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sharedPreferences.edit().clear().apply();
                        Intent i = new Intent(context, LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(i);

                        ((Activity)context).finish();


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
