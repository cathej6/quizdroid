package edu.washington.cathej.quizdroid;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
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
    private String dataUrl = "www.tedneward.com";
    private int updateTime = 1;

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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, topicTitles);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                intent.putExtra("topic", topics.get(position));
                MainActivity.this.startActivity(intent);
            }
        });

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(MainActivity.this, UpdateReceiver.class);
        intent.putExtra("url", dataUrl);
        alarmIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() +
                        100, 100, alarmIntent);
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
        intent.putExtra("dataUrl", dataUrl);
        intent.putExtra("updateTime", updateTime);
        MainActivity.this.startActivity(intent);
    }
}
