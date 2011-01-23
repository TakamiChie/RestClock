package jp.androidapp.takamichie.RestClock;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.BatteryManager;
import android.util.Log;

public class DockReciever extends BroadcastReceiver {

  @Override
  public void onReceive(final Context context, Intent intent) {

    Log.d("DockClock", "Broadcast Recieve");
    int plugged = intent.getIntExtra("plugged", 0);
    if (plugged == BatteryManager.BATTERY_PLUGGED_AC
        || plugged == BatteryManager.BATTERY_PLUGGED_USB) {
      Log.d("DockClock", "Actions");
      AlertDialog.Builder dialog = new AlertDialog.Builder(context);
      dialog.setTitle(R.string.wording_title_connecter_join);
      dialog.setMessage(String.format(context
          .getString(R.string.wording_message_connecter_join), context
          .getString(R.string.app_name)));
      dialog.setPositiveButton("�͂�", new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          Intent intent = new Intent(Intent.ACTION_MAIN);
          intent.setClassName(context, Main.class.getName());
          context.startActivity(intent);
          dialog.dismiss();
        }
      });
      dialog.setNegativeButton(android.R.string.no, new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          dialog.cancel();
        }
      });
    }
  }
}
