package jp.androidapp.takamichie.RestClock.util;

import jp.androidapp.takamichie.RestClock.Main;
import jp.androidapp.takamichie.RestClock.R;
import jp.androidapp.takamichie.RestClock.RestClockService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class Util {
  public static final String FROM_NOTIFICATION = "FromNotification";

  private Util() {
  }

  public static void runningService(Context context){
    Intent intent = new Intent(context, RestClockService.class);
    context.startService(intent);
  }

  public static void setNotification(Context context) {
    NotificationManager man = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
    Notification notify = new Notification(R.drawable.icon,
        context.getString(R.string.wording_connecter_join),
        System.currentTimeMillis());

    Intent sendIntent = new Intent(Intent.ACTION_MAIN);
    sendIntent.setClassName(context, Main.class.getName());
    sendIntent.putExtra(FROM_NOTIFICATION, true);
    PendingIntent pending = PendingIntent.getActivity(context, 0,
        sendIntent, 0);
    
    notify.setLatestEventInfo(context.getApplicationContext(), context
        .getString(R.string.wording_connecter_join), String.format(
        context.getString(R.string.wording_connecter_join_details),
        context.getString(R.string.app_name)), pending);
    
    man.notify(R.string.app_name, notify);
  }

  public static void removeNotification(Context context) {
    NotificationManager man = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
    man.cancelAll();
  }
}
