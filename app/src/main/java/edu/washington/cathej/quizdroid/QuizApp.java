package edu.washington.cathej.quizdroid;

import android.app.Application;
import android.util.Log;

/**
 * Created by catherinejohnson on 2/9/17.
 */

public class QuizApp extends Application {

    private TopicRepository instance = new TopicRepository();

    public QuizApp() {
        Log.d("setup", "QuizApp has been created.");
    }

    public TopicRepository getRepository() {
        return instance;
    }

}
