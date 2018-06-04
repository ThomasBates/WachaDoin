package ca.turbobutterfly.wachadoin.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ca.turbobutterfly.wachadoin.support.Bootstrapper;

public class SaveReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        NotificationHelper helper = Bootstrapper.ComposeNotificationHelper(context);

        helper.CancelNotification();
        helper.CancelScheduledNotification();
        helper.ExtendCurrentActivity();
        helper.ScheduleNotification();
    }
}
