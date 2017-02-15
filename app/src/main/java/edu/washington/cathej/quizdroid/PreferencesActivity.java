package edu.washington.cathej.quizdroid;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.R.attr.data;
import static java.security.AccessController.getContext;

public class PreferencesActivity extends Activity {
    private String dataUrl;
    private int updateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        dataUrl = getIntent().getStringExtra("dataUrl");
        updateTime = getIntent().getIntExtra("updateTime", 1);

        final EditText url = (EditText) findViewById(R.id.url);
        final EditText time = (EditText) findViewById(R.id.time);

        url.setText(dataUrl);
        time.setText(updateTime + "");

        final Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PreferencesActivity.this, UpdateReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(PreferencesActivity.this, 0, intent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                stopService(intent);
                alarmManager.cancel(pendingIntent);

                dataUrl = url.getText().toString();
                updateTime = Integer.parseInt(time.getText().toString());

                intent = new Intent(PreferencesActivity.this, UpdateReceiver.class);
                intent.putExtra("url", dataUrl);
                PendingIntent alarmIntent = PendingIntent.getBroadcast(PreferencesActivity.this, 0, intent, 0);

                alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() +
                        100, 100, alarmIntent);

                Log.i("debug", "Alarm canceled");

                CharSequence text = "Downloading data from: " + url.getText() +
                        " every " + time.getText() + " minutes";
                Toast toast = Toast.makeText(PreferencesActivity.this, text, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
