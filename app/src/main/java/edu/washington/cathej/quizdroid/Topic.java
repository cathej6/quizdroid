package edu.washington.cathej.quizdroid;

import java.io.Serializable;
import java.util.List;

/**
 * Created by catherinejohnson on 2/12/17.
 */

public class Topic implements Serializable {
    private String title;
    private String description;
    private List<Question> questions;

    public Topic(String title, String description, List<Question> questions) {
        this.title = title;
        this.description = description;
        this.questions = questions;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Question> getQuestions() {
        return questions;
    }
}
