package edu.washington.cathej.quizdroid;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private List<Topic> topics;
    private PendingIntent alarmIntent;
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("setup", "The application has been created.");

        QuizApp app = (QuizApp)this.getApplication();
        try {
            topics = app.getRepository().getAllTopics();
        } catch (IOException ex) {
            topics = new ArrayList<Topic>();
        }
        String[] topicTitles = new String[topics.size()];
        for (int i = 0; i < topics.size(); i++) {
            topicTitles[i] = topics.get(i).getTitle();
        }

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("url", "http://tednewardsandbox.site44.com/questions.json");
        editor.putString("updateTime","3");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, topicTitles);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                intent.putExtra("topicPosition", position);
                MainActivity.this.startActivity(intent);
            }
        });

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(MainActivity.this, UpdateReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(MainActivity.this, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 100,
                60000 * Integer.parseInt(pref.getString("updateTime", "1")), alarmIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_math_overview, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * On selecting action bar icons
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_preferences:
                // search action
                goToPreferences();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void goToPreferences() {
        Intent intent = new Intent(MainActivity.this, PreferencesActivity.class);
        MainActivity.this.startActivity(intent);
    }
}
