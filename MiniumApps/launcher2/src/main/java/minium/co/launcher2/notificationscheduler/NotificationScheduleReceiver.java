package minium.co.launcher2.notificationscheduler;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.orm.query.Condition;
import com.orm.query.Select;

import org.androidannotations.annotations.EReceiver;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import minium.co.core.app.DroidPrefs_;
import minium.co.core.log.Tracer;
import minium.co.launcher2.model.MissedCallItem;
import minium.co.launcher2.model.ReceivedSMSItem;
import minium.co.launcher2.utils.DisplayAlertActivity_;

@EReceiver
public class NotificationScheduleReceiver extends BroadcastReceiver {

    @Pref
    DroidPrefs_ prefs;

    @SystemService
    NotificationManager notificationManager;

    public NotificationScheduleReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Tracer.d("NotificationScheduleReceiver onReceive: " + new SimpleDateFormat("hh:mm:ss.SSS a", Locale.US).format(new Date()));

        if (prefs.isFlowRunning().get()) {
            prefs.isNotificationSupressed().put(true);
        } else if (prefs.isNotificationSupressed().get()) {
            showNotifications(context);
            prefs.isNotificationSupressed().put(false);
        } else {
            showNotifications(context);
        }
    }

    void showNotifications(Context context) {
        long count =
                Select.from(MissedCallItem.class)
                .where(Condition.prop("has_displayed").eq(0))
                .count() +

                Select.from(ReceivedSMSItem.class)
                        .where(Condition.prop("has_displayed").eq(0))
                        .count();

        if (count > 0) {
            DisplayAlertActivity_.intent(context).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
        }

        /*List<MissedCallItem> missedCalls = Select.from(MissedCallItem.class)
                .where(Condition.prop("has_displayed").eq(0))
                .list();

        List<ReceivedSMSItem> smsItems = Select.from(ReceivedSMSItem.class)
                .where(Condition.prop("has_displayed").eq(0))
                .list();

        Tracer.d("Generating missed call notifications: " + missedCalls.size() + " and SMS: " + smsItems.size());

        if (missedCalls.size() > 0 || smsItems.size() > 0) {
            new AudioUtils().playNotificationSound(context);
        }

        if (missedCalls.size() > 0) {
            for (MissedCallItem item : missedCalls)
                showCallNotifications(context, item.getNumber(), missedCalls.size());

            MissedCallItem.deleteAll(MissedCallItem.class);
        }

        if (smsItems.size() > 0) {
            for (ReceivedSMSItem item : smsItems) {
                showSMSNotifications(context, item.getNumber(), item.getBody(), smsItems.size());
            }

            ReceivedSMSItem.deleteAll(ReceivedSMSItem.class);
        }*/
    }

    private void showSMSNotifications(Context context, String number, String body, int size) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("minium.co.messages", "com.moez.QKSMS.ui.MainActivity_"));
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent,  PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification  = new Notification.Builder(context)
                .setContentTitle(number)
                .setContentText(body)
                .setContentInfo(size == 1 ? "" : String.valueOf(size))
                .setSmallIcon(android.R.drawable.sym_action_email)
                .setContentIntent(pIntent)
                .setAutoCancel(true).build();
        notificationManager.notify(1, notification);
    }

    private void showCallNotifications(Context context, String number, int size) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + number));
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent,  PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification  = new Notification.Builder(context)
                .setContentTitle("Missed Call!")
                .setContentText("Missed call from " + number)
                .setContentInfo(size == 1 ? "" : String.valueOf(size))
                .setSmallIcon(android.R.drawable.sym_call_missed)
                .setContentIntent(pIntent)
                .setAutoCancel(true).build();
        notificationManager.notify(0, notification);
    }
}
