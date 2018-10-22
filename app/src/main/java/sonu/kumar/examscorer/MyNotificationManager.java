package sonu.kumar.examscorer;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import sonu.kumar.examscorer.Utills.App;

/**
 * Created by sonu on 17/10/18.
 */

public class MyNotificationManager {

    public static final int ID_BIG_NOTIFICATION = 234;
    public static final int ID_SMALL_NOTIFICATION = 235;

    private Context mCtx;

    public MyNotificationManager(Context mCtx) {
        this.mCtx = mCtx;
    }

    //the method will show a big notification with an image
    //parameters are title for message title, message for message text, url of the big image and an intent that will open
    //when you will tap on the notification
    public void showBigNotification(String title, String message, String url, Intent intent) {
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mCtx,
                        ID_BIG_NOTIFICATION,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationManagerCompat compat =NotificationManagerCompat.from(mCtx);
        Notification notification =new NotificationCompat.Builder(mCtx, App.CHANNLE_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .setBigContentTitle(title)
                        .bigPicture(getBitmapFromURL(url))
                )

                .setColor(Color.GREEN)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        compat.notify(1,notification);



//        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
//        bigPictureStyle.setBigContentTitle(title);
//        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
//        bigPictureStyle.bigPicture(getBitmapFromURL(url));
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx);
//        Notification notification;
//        notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher).setTicker(title).setWhen(0)
//                .setAutoCancel(true)
//                .setContentIntent(resultPendingIntent)
//                .setContentTitle(title)
//                .setStyle(bigPictureStyle)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.mipmap.ic_launcher))
//                .setContentText(message)
//                .build();
//
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//
//        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(ID_BIG_NOTIFICATION, notification);
    }

    //the method will show a small notification
    //parameters are title for message title, message for message text and an intent that will open
    //when you will tap on the notification
//    public void showSmallNotification(String title, String message, Intent intent) {
//        PendingIntent resultPendingIntent =
//                PendingIntent.getActivity(
//                        mCtx,
//                        ID_SMALL_NOTIFICATION,
//                        intent,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );
//
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx);
//        Notification notification;
//        notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher).setTicker(title).setWhen(0)
//                .setAutoCancel(true)
//                .setContentIntent(resultPendingIntent)
//                .setContentTitle(title)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.mipmap.ic_launcher))
//                .setContentText(message)
//                .build();
//
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//
//        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(ID_SMALL_NOTIFICATION, notification);
//    }
    public void showSmallNotification(String title, String message, Intent intent) {
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mCtx,
                        ID_SMALL_NOTIFICATION,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        NotificationManagerCompat compat =NotificationManagerCompat.from(mCtx);
        Notification notification =new NotificationCompat.Builder(mCtx, App.CHANNLE_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .setColor(Color.GREEN)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        compat.notify(1,notification);
    }

    //The method will return Bitmap from an image URL
    private Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
