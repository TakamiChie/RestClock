package jp.androidapp.takamichie.RestClock;

import jp.androidapp.takamichie.RestClock.util.Util;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;
import android.util.Log;

public class RestClockService extends Service {

  @Override
  public IBinder onBind(Intent arg0) {
    return null;
  }

  @Override
  public void onStart(Intent intent, int startId) {
    IntentFilter filter = new IntentFilter();
    filter.addAction(Intent.ACTION_BATTERY_CHANGED);
    registerReceiver(new Recieved_ActionBatteryChanged(), filter);
    super.onStart(intent, startId);
  }

  @Override
  public boolean onUnbind(Intent intent) {
    return super.onUnbind(intent);
  }

  private final class Recieved_ActionBatteryChanged extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
      Log.d("DockClock", "Broadcast Recieve");
      // ACかUSBに接続されたか？
      int plugged = intent.getIntExtra("plugged", 0);
      if (plugged == BatteryManager.BATTERY_PLUGGED_AC
          || plugged == BatteryManager.BATTERY_PLUGGED_USB) {
        Log.d("DockClock", "Actions");

        Util.setNotification(context);
        /*
         * // ダイアログの生成 AlertDialog.Builder dialog = new
         * AlertDialog.Builder(context.getApplicationContext());
         * dialog.setTitle(R.string.wording_title_connecter_join);
         * dialog.setMessage(String.format(context
         * .getString(R.string.wording_message_connecter_join), context
         * .getString(R.string.app_name)));
         * dialog.setPositiveButton(R.string.yes, new OnClickListener() { public
         * void onClick(DialogInterface dialog, int which) { Intent intent = new
         * Intent(Intent.ACTION_MAIN); intent.setClassName(context,
         * Main.class.getName()); context.startActivity(intent);
         * dialog.dismiss(); } }); dialog.setNegativeButton(android.R.string.no,
         * new OnClickListener() { public void onClick(DialogInterface dialog,
         * int which) { dialog.cancel(); } });
         * 
         * dialog.create().show();
         */
      }else{
        Util.removeNotification(context);
      }
    }
  }

}
