package com.example.fiona.moviecatalogue5;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import cz.msebera.android.httpclient.Header;

public class AlarmReceiver extends BroadcastReceiver  {
    public static final String DAILY_REMINDER = "Daily Reminder Alarm";
    public static final String DAILY_RELEASE = "Release Reminder Alarm";

    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TYPE = "type";
    // Siapkan 2 id untuk 2 macam alarm, onetime dna repeating
    private final int ID_DAILY = 100;
    private final int ID_RELEASE = 101;

    //stack notification
    private int idNotification = 0;
    private final List<NotificationItem> stackNotif = new ArrayList<>();
    private static final CharSequence CHANNEL_NAME = "movie channel";
    private final static String GROUP_KEY_EMAILS = "group_key_movies";
    private static final int MAX_NOTIFICATION = 4;


    public AlarmReceiver() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {

        if(intent.getStringExtra(EXTRA_TYPE).equals(DAILY_RELEASE)){
            String tanggalSekarang = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            String API_KEY = "ac43d02969415550317c8131600d2c8d";
            String url = "https://api.themoviedb.org/3/discover/movie?api_key="+ API_KEY+ "&primary_release_date.qte=" + tanggalSekarang + "&primary_release_date.lte=" + tanggalSekarang;

            AsyncHttpClient client = new AsyncHttpClient();
            client.get(url, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                     ArrayList<Favorite> listItems = new ArrayList<>();
                    try {
                        String result = new String(responseBody);
                        JSONObject responseObject = new JSONObject(result);
                        JSONArray list = responseObject.getJSONArray("results");

                        String output ="";
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject tv = list.getJSONObject(i);
                            Favorite tvShowItems = new Favorite();
                            tvShowItems.setNama(tv.getString("title"));
                            listItems.add(tvShowItems);
                            output = tvShowItems.getNama()+" ,";
                            stackNotif.add(new NotificationItem(idNotification, output, "messagee"));
                            sendNotif(context,ID_RELEASE,listItems.size());
                            idNotification++;

                        }

                    } catch (Exception e) {
                        Log.d("Exception", e.getMessage());
                    }

                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("onFailure", error.getMessage());
                }
            });
        }else{
            // TODO: This method is called when the BroadcastReceiver is receiving
            // an Intent broadcast.
            String type = intent.getStringExtra(EXTRA_TYPE);
            String message = intent.getStringExtra(EXTRA_MESSAGE);
            String title = type.equalsIgnoreCase(DAILY_REMINDER) ? DAILY_REMINDER : DAILY_RELEASE;
            int notifId = type.equalsIgnoreCase(DAILY_REMINDER) ? ID_DAILY : ID_RELEASE;
            showToast(context, title, message);
            showAlarmNotification(context, title, message, notifId);
        }
    }
    private void showToast(Context context, String title, String message) {
        Toast.makeText(context, title + " : " + message, Toast.LENGTH_LONG).show();
    }
    public boolean cekAlarm(Context context, String type){
        Intent intent = new Intent(context, AlarmReceiver.class);
        int notifId = type.equalsIgnoreCase(DAILY_REMINDER) ? ID_DAILY : ID_RELEASE;
        return PendingIntent.getBroadcast(context,notifId, intent, PendingIntent.FLAG_NO_CREATE) != null;
    }

    private void sendNotif(Context context, int id, int title) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_notifications_white_48px);
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder;
        //Melakukan pengecekan jika idNotification lebih kecil dari Max Notif
        String CHANNEL_ID = "channel_01";
        if (idNotification < MAX_NOTIFICATION) {
            mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle("Movie Baru " + stackNotif.get(idNotification).getSender())
                    .setContentText(stackNotif.get(idNotification).getMessage())
                    .setSmallIcon(R.drawable.ic_access_time_black)
                    .setLargeIcon(largeIcon)
                    .setGroup(GROUP_KEY_EMAILS)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
        } else {
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle()
                    .addLine("Film Baru " + stackNotif.get(idNotification).getSender())
                    .addLine("Film Baru " + stackNotif.get(idNotification - 1).getSender())
                    .addLine("Film Baru " + stackNotif.get(idNotification - 2).getSender())
                    .addLine("Film Baru " + stackNotif.get(idNotification - 3).getSender())
                    .addLine("Film Baru " + stackNotif.get(idNotification - 4).getSender())
                    .setBigContentTitle(title + " Film Baru!!")
                    .setSummaryText("Movie Catalogue");
            mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle(title + " Film Baru !!")
                    .setContentText("Movie Catalogue")
                    .setSmallIcon(R.drawable.ic_notifications_white_48px)
                    .setGroup(GROUP_KEY_EMAILS)
                    .setGroupSummary(true)
                    .setContentIntent(pendingIntent)
                    .setStyle(inboxStyle)
                    .setAutoCancel(true);
        }
     /*
    Untuk android Oreo ke atas perlu menambahkan notification channel
    Materi ini akan dibahas lebih lanjut di modul extended
     */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /* Create or update. */
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            mBuilder.setChannelId(CHANNEL_ID);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
            }
        }
        Notification notification = mBuilder.build();
        if (mNotificationManager != null) {
            mNotificationManager.notify(id, notification);
        }
    }
    private void showAlarmNotification(Context context, String title, String message, int notifId) {
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "AlarmManager channel";
        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,notifId,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_access_time_black)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound)
                .setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setChannelId(CHANNEL_ID);
            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }
        Notification notification = builder.build();
        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notifId, notification);
        }
    }

    public void setRepeatingAlarm2(Context context, String type, String message) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_DAILY, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        Toast.makeText(context, "Daily Reminder alarm set up", Toast.LENGTH_SHORT).show();
    }

    public void setReleaseAlarm2(Context context, String type,String message) {
        String DATE_FORMAT = "yyyy-MM-dd";
        String TIME_FORMAT = "HH:mm";
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_RELEASE, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        Toast.makeText(context, "Release Movie alarm set up", Toast.LENGTH_SHORT).show();
    }

    public void cancelAlarm(Context context, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        int requestCode = type.equalsIgnoreCase(DAILY_REMINDER) ? ID_DAILY: ID_RELEASE;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        pendingIntent.cancel();
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
        Toast.makeText(context, "Alarm dibatalkan", Toast.LENGTH_SHORT).show();
    }

}
