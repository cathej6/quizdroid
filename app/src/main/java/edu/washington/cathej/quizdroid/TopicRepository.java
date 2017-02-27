package edu.washington.cathej.quizdroid;

import android.os.Environment;
import android.util.JsonReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by catherinejohnson on 2/9/17.
 */

public class TopicRepository {
    private List<Topic> topics = new ArrayList<Topic>();
    private boolean initialized = false;


    public void updateTopics() throws IOException {
        topics.clear();
        File questions = new File(Environment.getExternalStorageDirectory(), "questions.json");
        InputStream is = new FileInputStream(questions);
        buildTopics(new JsonReader(new InputStreamReader(is)));
    }

    public List<Topic> getAllTopics() throws IOException {
        if (!initialized) {
            updateTopics();
            initialized = true;
        }
        return topics;
    }

    private void buildTopics(JsonReader reader) throws IOException {
        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();
            String title = "";
            String description = "";
            List<Question> questions = null;

            while (reader.hasNext()) {
                String key = reader.nextName();
                if (key.equals("title")) {
                    title = reader.nextString();
                } else if (key.equals("description")) {
                    description = reader.nextString();
                } else if (key.equals("questions")) {
                    questions = addQuestions(reader);
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            topics.add(new Topic(title, description, questions));
        }
        reader.endArray();
    }

    private List<Question> addQuestions(JsonReader reader) throws IOException {
        List<Question> questions = new ArrayList<Question>();
        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();
            String text = "";
            int answer = -1;
            String[] answers = new String[4];

            while (reader.hasNext()) {
                String key = reader.nextName();
                if (key.equals("text")) {
                    text = reader.nextString();
                } else if (key.equals("answer")) {
                    answer = Integer.parseInt(reader.nextString());
                } else if (key.equals("answers")) {
                    reader.beginArray();
                    int index = 0;
                    while (reader.hasNext()) {
                        answers[index] = reader.nextString();
                        index++;
                    }
                    reader.endArray();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            questions.add(new Question(text, answers, answer));
        }
        reader.endArray();
        return questions;
    }

}
