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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private Quiz[] quizes = new Quiz[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("setup", "The application has been created.");

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

        QuizApp app = (QuizApp)this.getApplication();
        final List<Topic> topics = app.getRepository().getAllTopics();

        if (topics.size() < 4) {
            buildTopics(topics, quizes);
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
    }

    public void buildTopics(List<Topic> topics, Quiz[] data) {
        for (int i = 0; i < data.length; i++) {
            Quiz thisQuiz = data[i];
            List<Question> questionList = new ArrayList<Question>();

            for (int j = 0; j < thisQuiz.questions.length; j++) {
                String[] thisQuestionsAnswers = new String[4];
                for (int k = 0; k < 4; k++) {
                    thisQuestionsAnswers[k] = thisQuiz.answers[k + (j * 4)];
                }
                Question newQuestion = new Question(thisQuiz.questions[j], thisQuestionsAnswers,
                        thisQuiz.correctAnswers[j]);
                questionList.add(newQuestion);
            }
            Topic newTopic = new Topic(thisQuiz.name, thisQuiz.description, questionList);
            topics.add(newTopic);
        }
    }

    public class Quiz implements Serializable{
        // specify tip amount
        public String name;
        public String description;

        public String[] questions;
        public String[] answers;
        public int[] correctAnswers;


        // constructor to set tip
        public Quiz(String name, String[] questions, String[] answers, int[] correctAnswers){
            this.name = name;
            this.description = "This topic centers around the magical world of " + name +
                    " and the wonderous things you can learn from it";
            this.questions = questions;
            this.answers = answers;
            this.correctAnswers = correctAnswers;
        }
    }
}
