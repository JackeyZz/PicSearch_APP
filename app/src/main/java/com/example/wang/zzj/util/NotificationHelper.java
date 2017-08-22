package com.example.wang.zzj.util;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import com.github.johnpersano.supertoasts.SuperToast;

/**
 * Created by wang on 4/8/15.提醒帮助类
 */
public class NotificationHelper {

//    通知id
    public static final int NOTIFICATION_ID = 1000;

//    震动
    public static void vibrate(Context context) {
        ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(250);
    }

//    提示音
    public static void sound(Context context) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    提示音和震动
    public static void soundAndVibrate(Context context) {
        sound(context);
        vibrate(context);
    }

//    蓝色弹出框
    public static void toast(Context context, String message) {
        toast(context, message, SuperToast.Background.BLUE);
    }

//    红色弹出框
    public static void toastDanger(Context context, String message) {
        toast(context, message, SuperToast.Background.RED);
    }

//    绿色弹出框
    public static void toastSuccess(Context context, String message) {
        toast(context, message, SuperToast.Background.GREEN);
    }

//    橙色弹出框
    public static void toastWarn(Context context, String message) {
        toast(context, message, SuperToast.Background.ORANGE);
    }

    private static void toast(Context context, String message, int background) {
        if (context != null) {
            SuperToast superToast = new SuperToast(context);

            superToast.setAnimations(SuperToast.Animations.FLYIN);
            superToast.setDuration(SuperToast.Duration.SHORT);
            superToast.setBackground(background);
            superToast.setTextSize(SuperToast.TextSize.SMALL);
            superToast.setText(message);

            superToast.show();
        }
    }


//    通知
    public static void notify(Context context, int id, int resId, String title, String message, Intent[] intents) {
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            pendingIntent = PendingIntent.getActivities(context, 0, intents, PendingIntent.FLAG_ONE_SHOT);
        } else {
            pendingIntent = PendingIntent.getActivity(context, 0, intents[intents.length - 1], PendingIntent.FLAG_ONE_SHOT);
        }

        notify(context, id, resId, title, message, pendingIntent);
    }

    public static void notify(Context context, int id, int resId, String title, String message, PendingIntent pendingIntent) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setTicker(message).setContentTitle(title).setContentText(message).setSmallIcon(resId);
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        manager.notify(id, notification);
    }

}
