package com.aa.safelocksaving.Operation;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.aa.safelocksaving.DAO.DAOAlarm;
import com.aa.safelocksaving.R;
import com.aa.safelocksaving.Receiver.AlarmReceiver;
import com.aa.safelocksaving.data.DateBasic;

import java.util.Calendar;
import java.util.Map;

public class AlarmOp {
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    private Context context;
    private DAOAlarm daoAlarm;

    public AlarmOp(Context context) {
        this.context = context;
        daoAlarm = new DAOAlarm(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "RemindersChannel",
                    context.getString(R.string.remindersText),
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(context.getString(R.string.channelForRemindersText));
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    public void setAlarm(long id, String title, String text, DateBasic date, int importance) {
        if (!daoAlarm.existAlarm(id)) {
            int requestCode = daoAlarm.getRequestCode(id);
            daoAlarm.setAlarm(id, requestCode);
            //Log.i("alarm", "alarm not exist");
            Calendar calendar = Calendar.getInstance();
            calendar.set(date.getYear(), date.getMonth(), date.getDay());
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            intent.putExtra("Id", id);
            intent.putExtra("RequestCode", requestCode);
            intent.putExtra("Title", title);
            intent.putExtra("Text", text);
            pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60, pendingIntent);
            Toast.makeText(context, "Alarm " + requestCode + " added", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    public void cancelAlarm(long id) {
        if (daoAlarm.existAlarm(id)) {
            int requestCode = daoAlarm.getAlarm(id);
            daoAlarm.removeAlarm(id);
            Intent intent = new Intent(context, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            if (alarmManager == null)
                alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            Toast.makeText(context, "Canceled" + requestCode, Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelAllAlarms() {
        Map<String, ?> alarms = daoAlarm.getAll();
        for (Map.Entry<String, ?> alarm : alarms.entrySet())
            cancelAlarm(Long.parseLong(alarm.getKey()));
    }
}