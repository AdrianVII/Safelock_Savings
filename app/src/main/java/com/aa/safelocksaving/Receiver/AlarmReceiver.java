package com.aa.safelocksaving.Receiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.legacy.content.WakefulBroadcastReceiver;

import com.aa.safelocksaving.Main_Activity;
import com.aa.safelocksaving.R;

import java.util.Random;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Notify", "Si entro aqui wey");
        Bundle bundle = intent.getExtras();
        Intent i = new Intent(context, Main_Activity.class);
        i.putExtra("Id", bundle.getLong("Id"));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, bundle.getInt("RequestCode"), i, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "RemindersChannel")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(bundle.getString("Title"))
                .setContentText(bundle.getString("Text"))
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);
        NotificationManagerCompat.from(context).notify(new Random().nextInt(100), builder.build());
    }
}
