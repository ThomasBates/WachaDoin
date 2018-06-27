package ca.turbobutterfly.wachadoin.android.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ca.turbobutterfly.wachadoin.android.bootstrapper.Bootstrapper;

public class BootReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        NotificationHelper helper = Bootstrapper.ComposeNotificationHelper(context);
        helper.ScheduleNotification();
    }
}
