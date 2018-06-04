package ca.turbobutterfly.wachadoin.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ca.turbobutterfly.wachadoin.support.Bootstrapper;

//  https://github.com/commonsguy/cw-advandroid/tree/master/SystemServices/Alarm/

public class BootReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        NotificationHelper helper = Bootstrapper.ComposeNotificationHelper(context);
        helper.ScheduleNotification();
    }
}
