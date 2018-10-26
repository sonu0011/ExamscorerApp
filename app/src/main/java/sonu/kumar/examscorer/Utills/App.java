package sonu.kumar.examscorer.Utills;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.google.android.gms.ads.MobileAds;

import sonu.kumar.examscorer.Activity.SemesterActivity;

/**
 * Created by sonu on 17/10/18.
 */


public class App extends Application {
    public static final String CHANNLE_ID ="channel1";

    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(App.this,"ca-app-pub-9104180069881340~6525361897");

        createNotificaitonChannel();
    }

    private void createNotificaitonChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel =new NotificationChannel(
                    CHANNLE_ID,
                    "channel1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("This is channel 1");
            NotificationManager manager =getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }
    }
}
