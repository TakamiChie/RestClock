package jp.androidapp.takamichie.RestClock;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

public class Main extends Activity {
  Timer tickTimer;

  static int holidayColor = Color.RED;
  static int subHolidayColor = Color.BLUE;
  static int reloadCountMAX = 10;
  SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd EEE");
  Date lastDay;
  int reloadCount;
  String webSite;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    Log.d("DockClock", "Prepare");

    // カレンダーテーブルの作成
    TableRow.LayoutParams params = new TableRow.LayoutParams();
    params.weight = 1;
    params.width = LayoutParams.FILL_PARENT;
    params.height = LayoutParams.FILL_PARENT;

    TableLayout table = (TableLayout) findViewById(R.id.table_calender);
    for (int r = 0; r < table.getChildCount(); r++) {
      TableRow row = (TableRow) table.getChildAt(r);
      row.removeAllViews();
      for (int c = 0; c < 7; c++) {
        TextView label = new TextView(this);
        label.setHeight(500);
        label.setGravity(r == 0 ? Gravity.CENTER : Gravity.LEFT);
        switch (c) {
          case 0:
            label.setTextColor(holidayColor);
            break;
          case 6:
            label.setTextColor(subHolidayColor);
            break;
        }

        row.addView(label, params);
      }
    }
    // 曜日列の作成
    TableRow weekrow = (TableRow) findViewById(R.id.tr_weeks);
    String[] weekdays = getResources().getStringArray(R.array.weekdays);
    for (int i = 0; i < weekrow.getChildCount(); i++) {
      ((TextView) weekrow.getChildAt(i)).setText(weekdays[i]);
    }
    Log.d("DockClock", "Finish");
  }

  @Override
  protected void onResume() {
    super.onResume();
    WebView webView = (WebView) findViewById(R.id.webView);
    webView.loadUrl("http://favstar.fm/users/TakamiChie/recent");
    reloadCount = reloadCountMAX;
    tickTimer = new Timer(false);
    tickTimer.schedule(new TimerTask() {
      @Override
      public void run() {
        Handler h = new Handler(Main.this.getMainLooper());
        h.post(new Runnable() {
          @Override
          public void run() {
            updateTime();
            reloadCount--;
            if (reloadCount <= 0) {
              WebView webView = (WebView) findViewById(R.id.webView);
              webView.reload();
              reloadCount = reloadCountMAX;
            }
          }
        });
      }
    }, 0, 1000);
  }

  @Override
  protected void onPause() {
    super.onPause();
    tickTimer.cancel();
    tickTimer.purge();
  }

  private void updateTime() {
    TextView label_time = (TextView) findViewById(R.id.label_time);
    TextView label_day = (TextView) findViewById(R.id.label_day);
    Date now = new Date();
    label_time.setText(timeFormat.format(now));
    if (lastDay == null || lastDay.getDate() != now.getDate()) {
      Log.d("DockClock", "RefreshDate");
      label_day.setText(dateFormat.format(now));
      createCalender();
      Log.d("DockClock", "Updated");
    }
    lastDay = now;
  }

  private void createCalender() {
    int day = 1;
    Date now = new Date();
    Calendar cal = Calendar.getInstance();
    cal.set(1900 + now.getYear(), now.getMonth(), 1);
    int firstDay = cal.get(Calendar.DAY_OF_WEEK);
    int dayCount = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    day -= (firstDay - 1);
    TableLayout table = (TableLayout) findViewById(R.id.table_calender);
    for (int r = 1; r < table.getChildCount(); r++) {
      TableRow row = (TableRow) table.getChildAt(r);
      for (int c = 0; c < row.getChildCount(); c++) {
        if (day > dayCount)
          break;
        if (day > 0) {
          TextView label = (TextView) row.getChildAt(c);
          label.setText(String.valueOf(day));
        }
        day++;
      }
    }
  }
}