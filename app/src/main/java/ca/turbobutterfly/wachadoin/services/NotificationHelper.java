package ca.turbobutterfly.wachadoin.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import java.io.IOException;
import java.util.Date;

import ca.turbobutterfly.wachadoin.R;
import ca.turbobutterfly.wachadoin.activities.MainActivity;
import ca.turbobutterfly.wachadoin.data.IDataProvider;

public class NotificationHelper
{
    //  For Notification.
    private static final int _notificationID = 1;
    private static final String _channelID = "ca.turbobutterfly.wachadoin.reminders.01";
    private static final String _channelName = "WachaDoin Reminders";
    private static final String _channelDescription = "Notification channel for WachaDoin Reminders";

    private Context _context;
    private IDataProvider _dataProvider;
    private SharedPreferences _preferences;
    private NotificationManager _notificationManager;
    private AlarmManager _alarmManager;

    private Date _startTime;
    private long _startTime_ms;
    private String _lastLogText;

    private boolean _doNotifications;
    private boolean _doPopupApp;
    private String _ringtone;
    private boolean _doVibrate;
    private long _waitTime;
    private long _snoozeTime;


    public NotificationHelper(Context context, IDataProvider dataProvider)
    {
        _context = context;
        _dataProvider = dataProvider;

        _preferences = PreferenceManager.getDefaultSharedPreferences(_context);
        _notificationManager = (NotificationManager)_context.getSystemService(Context.NOTIFICATION_SERVICE);
        _alarmManager = (AlarmManager)_context.getSystemService(Context.ALARM_SERVICE);
    }

    private void GetSettings()
    {
        _startTime = _dataProvider.GetLastEndTime();
        _startTime_ms = _startTime.getTime();
        _lastLogText = _dataProvider.GetLastLogText();

        //  Preferences
        _doNotifications = _preferences.getBoolean("pref_notifications_enabled", true);
        _doPopupApp = _preferences.getBoolean("pref_notifications_popup_app", false);
        _ringtone = _preferences.getString("pref_notifications_ringtone", "");
        _doVibrate = _preferences.getBoolean("pref_notifications_vibrate", true);
        String temp = _preferences.getString("pref_notifications_delay", "15");
        _waitTime = Integer.parseInt(temp) * 60000;
        temp = _preferences.getString("pref_notifications_snooze", "1");
        _snoozeTime = Integer.parseInt(temp) * 60000;
    }

    private void CreateNotificationChannel()
    {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O)
        {
            return;
        }

        //_notificationManager.deleteNotificationChannel(_channelID);

        NotificationChannel notificationChannel = new NotificationChannel(
                _channelID,
                _channelName,
                NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.setDescription(_channelDescription);

        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);

        notificationChannel.enableVibration(true);
        //notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

        //notificationChannel.setShowBadge(false);
//            notificationChannel.setSound(ringtoneUri, new AudioAttributes.Builder()
//                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT).build());

        _notificationManager.createNotificationChannel(notificationChannel);
    }

    public void SendNotification()
    {
        if (_notificationManager == null)
        {
            return;
        }

        GetSettings();

        if (!_doNotifications)
        {
            return;
        }

        CreateNotificationChannel();

        //  This will be ignored for Oreo+
        Uri ringtoneUri = Uri.parse(_ringtone);

        //  Create Intent
        Intent activityIntent = new Intent(_context, MainActivity.class);

        if (_doPopupApp)
        {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
            {
                NotificationChannel channel = _notificationManager.getNotificationChannel(_channelID);
                if (channel != null)
                {
                    ringtoneUri = channel.getSound();
                }
            }

            playSound(_context, ringtoneUri);

            _context.startActivity(activityIntent);
            return;
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(
                _context,
                0,
                activityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        //  Create Save Action intent
        Intent saveIntent = new Intent(_context, SaveReceiver.class);
        PendingIntent savePendingIntent =
                PendingIntent.getBroadcast(_context, 0, saveIntent, 0);



        //  Create Notification
        NotificationCompat.Builder builder;

        builder = new NotificationCompat.Builder(
                _context,
                _channelID)
                //.setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setContentTitle("Wacha Doin?")
                //.setContentText("Tap to record your activity")
                .setContentText(_lastLogText)
                .setSmallIcon(R.mipmap.ic_launcher)
                //.setStyle(inboxStyle)
                .addAction(R.mipmap.ic_launcher, "Save", savePendingIntent)
                .setSound(ringtoneUri);

        //  Create "big view" style.
        long minutes = (new Date().getTime() - _startTime_ms) / (60000);
        String plural = ((minutes == 1) ? "" : "s");

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle(builder);
        inboxStyle.setBigContentTitle("Wacha Doin?");
        inboxStyle.addLine(_lastLogText);
        inboxStyle.addLine(_context.getString(R.string.notification_prompt, minutes, plural));

        Notification notification = builder.build();

        // notificationID allows you to update or cancel the notification later on.
        _notificationManager.notify(_notificationID, notification);
    }

    public void ScheduleNotification()
    {
        if (_alarmManager == null)
        {
            return;
        }

        GetSettings();

        long endTime = new Date().getTime();
        long delayTime = _waitTime - (endTime - _startTime_ms);
        if (delayTime <= 0)
        {
            delayTime = _snoozeTime;
        }
        long scheduleTime = endTime + delayTime;

        PendingIntent pendingIntent = CreatePendingIntent();

        _alarmManager.set(AlarmManager.RTC_WAKEUP,
                scheduleTime,
                pendingIntent);
    }

    public void CancelNotification()
    {
        _notificationManager.cancel(_notificationID);
    }

    public void CancelScheduledNotification()
    {
        if (_alarmManager == null)
        {
            return;
        }

        PendingIntent pendingIntent = CreatePendingIntent();
        _alarmManager.cancel(pendingIntent);
    }

    public void ExtendCurrentActivity()
    {
        if (_dataProvider == null)
        {
            return;
        }

        GetSettings();

        _dataProvider.SaveLogEntry(_startTime, new Date(), _lastLogText);
    }

    private PendingIntent CreatePendingIntent()
    {
        Intent receiverIntent = new Intent(_context, AlarmReceiver.class);

        return PendingIntent.getBroadcast(
                _context,
                0,
                receiverIntent,
                PendingIntent.FLAG_ONE_SHOT);
    }




    private void playSound(Context context, Uri soundUri)
    {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try
        {
            mediaPlayer.setDataSource(context, soundUri);
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0)
            {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mediaPlayer.prepare();
                mediaPlayer.start();
            }
        }
        catch (IOException e)
        {
            System.out.println("OOPS");
        }
    }
}