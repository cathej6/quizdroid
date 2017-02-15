package edu.washington.cathej.quizdroid;

import java.io.Serializable;

/**
 * Created by catherinejohnson on 2/12/17.
 */

public class Question implements Serializable {
    private String text;
    private String[] answers;
    private int correctAnswer;

    public Question(String text, String[] answers, int correctAnswer) {
        this.text = text;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }

    public String getText() {
        return text;
    }

    public String[] getAnswers() {
        return answers;
    }

    public boolean isCorrect(int answerIndex) {
        return answerIndex + 1 == correctAnswer;
    }

    public String getCorrectAnswer() {
        return answers[correctAnswer];
    }
}
