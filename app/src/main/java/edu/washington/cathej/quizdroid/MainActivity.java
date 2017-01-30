package edu.washington.cathej.quizdroid;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

    private String[] topics = new String[] {
        "Math", "Physics", "Marvel Super Heroes", "Harry Potter"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, topics);

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);


        //Intent intent = new Intent(this, OtherActivity.class);
        //startActivity(intent);
    }
}
