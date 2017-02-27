package edu.washington.cathej.quizdroid;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.R.attr.data;
import static java.security.AccessController.getContext;

public class PreferencesActivity extends Activity {
    private String dataUrl;
    private String updateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        dataUrl = pref.getString("url", "http://tednewardsandbox.site44.com/questions.json");
        updateTime = pref.getString("updateTime", "1");

        final EditText url = (EditText) findViewById(R.id.url);
        final EditText time = (EditText) findViewById(R.id.time);

        url.setText(dataUrl);
        time.setText(updateTime + "");

        final Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PreferencesActivity.this, UpdateReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(PreferencesActivity.this,
                        0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                stopService(intent);
                alarmManager.cancel(pendingIntent);

                dataUrl = url.getText().toString();
                updateTime = time.getText().toString();

                intent = new Intent(PreferencesActivity.this, UpdateReceiver.class);
                intent.putExtra("url", dataUrl);
                PendingIntent alarmIntent = PendingIntent.getBroadcast(PreferencesActivity.this,
                        0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime() + 100,
                        60000 * Integer.parseInt(pref.getString("updateTime", "1")), alarmIntent);

                Log.i("debug", "Alarm canceled");

                CharSequence text = "Downloading data from: " + url.getText() +
                        " every " + time.getText() + " minutes";
                Toast toast = Toast.makeText(PreferencesActivity.this, text, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
