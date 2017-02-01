package edu.washington.cathej.quizdroid;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;
import android.util.Log;

import java.io.Serializable;

import static android.R.id.input;

public class MainActivity extends Activity {

    private String[] topics = new String[] {
        "Math", "Physics", "Marvel Super Heroes", "Harry Potter"
    };

    private Quiz[] quizes = new Quiz[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quizes[0]= new Quiz("Math",
                new String[] {
                        "Which one is the quadratic equation?",
                        "How what does i standfor?"
                },
                new String[] {
                        "a^2 + b^2 = c^2",
                        "a + b = c",
                        "ax^2 + bx + c = 0",
                        "ax + b = 0",
                        "infinity",
                        "an indefinate number",
                        "a rudementry number",
                        "an imaginary number"
                },
                new int[] {2, 3});
        quizes[1]= new Quiz("Physics",
                new String[] {
                        "What is acceleration?",
                        "Why hate physics??"
                },
                new String[] {
                        "a^2 + b^2 = c^2",
                        "a + b = c",
                        "ax^2 + bx + c = 0",
                        "ax + b = 0",
                        "infinity",
                        "I don't know",
                        "a rudementry number",
                        "an imaginary number"
                },
                new int[] {3, 1});
        quizes[2]= new Quiz("Marvel Super Heros",
                new String[] {
                        "Who is Iron Man?",
                        "What's the best marvel movie?"
                },
                new String[] {
                        "Ted Neward",
                        "Tony Stark",
                        "The homeless guy",
                        "I don't know",
                        "The Avengers",
                        "Captain America",
                        "Thor",
                        "X-Men: United"
                },
                new int[] {1, 0});
        quizes[3]= new Quiz("Harry Potter",
                new String[] {
                        "Who is YouKnowWho?",
                        "Is Harry a Horrorcrux?"
                },
                new String[] {
                        "Tom Riddle",
                        "HeWhoMustNotBeNamed",
                        "Voldemort",
                        "All of the above",
                        "Yes",
                        "No",
                        "Maybe",
                        "I don't know"
                },
                new int[] {3, 0});

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, topics);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = (String) parent.getItemAtPosition(position);
                Log.i("clicktests", "ItemValue is " + itemValue);
                Intent intent = new Intent(MainActivity.this, MathQuizActivity.class);
                //intent.putExtra("quiz", quizes[position]);

                intent.putExtra("quizName", quizes[position].name);
                intent.putExtra("questions", quizes[position].questions);
                intent.putExtra("answers", quizes[position].answers);
                intent.putExtra("correctAnswers", quizes[position].correctAnswers);

                MainActivity.this.startActivity(intent);

            }
        });
    }

    public class Quiz implements Serializable{
        // specify tip amount
        public String name;

        public String[] questions;
        public String[] answers;
        public int[] correctAnswers;


        // constructor to set tip
        public Quiz(String name, String[] questions, String[] answers, int[] correctAnswers){
            this.name = name;
            this.questions = questions;
            this.answers = answers;
            this.correctAnswers = correctAnswers;
        }

        private Quiz(Parcel in) {

        }
    }
}
