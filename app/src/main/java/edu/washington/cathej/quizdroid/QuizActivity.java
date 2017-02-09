package edu.washington.cathej.quizdroid;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class QuizActivity extends Activity {

    private String[] questions;
    private String[] answers;
    private int[] correctAnswerIndex;
    private String[] stages = new String[] {"overview", "question", "answer"};

    private String status = stages[0];

    private int questionNum = 0;
    private int correctAnswers = 0;

    private OverviewFragment overviewFragment;
    private QuestionFragment questionFragment;
    private AnswerFragment answerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        // To be able to grab information from last activity
        Intent i = getIntent();
        String quizName = i.getStringExtra("quizName");
        questions = i.getStringArrayExtra("questions");
        answers = i.getStringArrayExtra("answers");
        correctAnswerIndex = i.getIntArrayExtra("correctAnswers");

        Bundle bOverview = new Bundle();
        bOverview.putString("quizName", quizName);
        bOverview.putInt("count", questions.length);
        overviewFragment = new OverviewFragment();
        overviewFragment.setArguments(bOverview);

        commitTransaction(overviewFragment);

        final Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.equals(stages[0]) || (status.equals(stages[2]) && button.getText() == "Next")) {
                    Bundle bQuestion = new Bundle();
                    bQuestion.putStringArray("questions", questions);
                    bQuestion.putStringArray("answers", answers);
                    bQuestion.putInt("questionNum", questionNum);

                    questionFragment = new QuestionFragment();
                    questionFragment.setArguments(bQuestion);

                    commitTransaction(questionFragment);

                    status = stages[1];
                    button.setText("Submit");
                } else if (status.equals(stages[1]) &&
                        questionFragment.options.getCheckedRadioButtonId() != -1) {
                    if (questionNum == questions.length - 1) {
                        button.setText("Finish");
                    } else {
                        button.setText("Next");
                    }
                    int selectedId = questionFragment.options.getCheckedRadioButtonId();
                    RadioButton theirAnswer = (RadioButton) questionFragment.options.findViewById(selectedId);
                    int answerIndex = questionFragment.options.indexOfChild(theirAnswer);
                    if (answerIndex == correctAnswerIndex[questionNum]) {
                        correctAnswers++;
                    }

                    String yAnswer = "Your answer: " + answers[questionNum * 4 + answerIndex];
                    String mAnswer = "My answer: " + answers[questionNum * 4 +
                            correctAnswerIndex[questionNum]];
                    questionNum++;
                    String total = "You have " + correctAnswers + " out of " + questionNum
                            + " correct";

                    Bundle bAnswer = new Bundle();
                    bAnswer.putString("yAnswer", yAnswer);
                    bAnswer.putString("mAnswer", mAnswer);
                    bAnswer.putString("total", total);

                    answerFragment = new AnswerFragment();
                    answerFragment.setArguments(bAnswer);

                    commitTransaction(answerFragment);

                    status = stages[2];
                } else if (status.equals(stages[2]) && button.getText().equals("Finish")) {
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    v.getContext().startActivity(intent);
                }
            }
        });
    }

    public void commitTransaction(Fragment f) {
        FragmentTransaction tx = getFragmentManager().beginTransaction();
        tx.replace(R.id.content, f);
        tx.commit();
    }
}
