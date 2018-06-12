package ca.turbobutterfly.wachadoin.android.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ca.turbobutterfly.wachadoin.android.bootstrapper.Bootstrapper;

//  https://github.com/commonsguy/cw-advandroid/tree/master/SystemServices/Alarm/

public class AlarmReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        NotificationHelper helper = Bootstrapper.ComposeNotificationHelper(context);
        helper.SendNotification();
        helper.ScheduleNotification();
    }
}
