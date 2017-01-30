package edu.washington.cathej.quizdroid;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;
import android.util.Log;

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
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = (String) parent.getItemAtPosition(position);
                Log.i("clicktests", "ItemValue is " + itemValue);
                Intent intent;
                if (itemValue == topics[0]) {
                    intent = new Intent(view.getContext(), MathOverActivity.class);
                } else if (itemValue == topics[1]) {
                    intent = new Intent(view.getContext(), PhysicsOverActivity.class);
                } else if (itemValue == topics[2]) {
                    intent = new Intent(view.getContext(), MarvelOverActivity.class);
                } else {
                    intent = new Intent(view.getContext(), PotterOverActivity.class);
                }
                view.getContext().startActivity(intent);
            }
        });
    }
}
