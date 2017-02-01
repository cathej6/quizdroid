package edu.washington.cathej.quizdroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import static edu.washington.cathej.quizdroid.R.id.options;
import static edu.washington.cathej.quizdroid.R.id.question;

public class MathQuizActivity extends Activity {

    private String[] questions;
    private String[] answers;
    private int[] correctAnswerIndex;

    private int questionNum = 0;
    private int correctAnswers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_quiz);

        // To be able to grab information from last activity
        Intent i = getIntent();
        String quizName = i.getStringExtra("quizName");
        questions = i.getStringArrayExtra("questions");
        answers = i.getStringArrayExtra("answers");
        correctAnswerIndex = i.getIntArrayExtra("correctAnswers");


        TextView name = (TextView) findViewById(R.id.name);
        name.setText(quizName);
        TextView description = (TextView) findViewById(R.id.description);
        description.setText("This topic centers around the magical world of " + quizName
                + " and the wonderous things you can learn from it");
        TextView count = (TextView) findViewById(R.id.count);
        count.setText("Number of questions: " + questions.length);


        final LinearLayout questionPage = (LinearLayout) findViewById(R.id.questionpage);
        final LinearLayout answerPage = (LinearLayout) findViewById(R.id.answerpage);

        final TextView yAnswer = (TextView) findViewById(R.id.yanswer);
        final TextView mAnswer = (TextView) findViewById(R.id.manswer);
        final TextView total = (TextView) findViewById(R.id.total);
        final TextView question = (TextView) findViewById(R.id.question);
        question.setText(questions[0]);


        final RadioGroup options = (RadioGroup) findViewById(R.id.options);
        createRadioButtons(options);

        final Button submit = (Button) findViewById(R.id.submit);
        final Button next = (Button) findViewById(R.id.next);
        final Button finish = (Button) findViewById(R.id.finish);

        Button begin = (Button) findViewById(R.id.begin);
        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout overview = (LinearLayout) findViewById(R.id.overview);
                overview.setVisibility(View.GONE);
                submit.setVisibility(View.VISIBLE);
                questionPage.setVisibility(View.VISIBLE);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (options.getCheckedRadioButtonId() != -1) {
                    questionPage.setVisibility(View.GONE);
                    submit.setVisibility(View.GONE);
                    answerPage.setVisibility(View.VISIBLE);
                    if (questionNum == questions.length - 1) {
                        finish.setVisibility(View.VISIBLE);
                    } else {
                        next.setVisibility(View.VISIBLE);
                    }
                    int selectedId = options.getCheckedRadioButtonId();
                    RadioButton theirAnswer = (RadioButton) options.findViewById(selectedId);
                    int answerIndex = options.indexOfChild(theirAnswer);
                    if (answerIndex == correctAnswerIndex[questionNum]) {
                        correctAnswers++;
                    }

                    yAnswer.setText("Your answer: " + answers[questionNum * 4 + answerIndex]);
                    mAnswer.setText("My answer: " + answers[questionNum * 4 + correctAnswerIndex[questionNum]]);
                    questionNum++;
                    total.setText("You have " + correctAnswers + " out of " + questionNum
                            + " correct");
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (options.getCheckedRadioButtonId() != -1) {
                    options.removeAllViews();
                    createRadioButtons(options);
                    question.setText(questions[questionNum]);
                    questionPage.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    answerPage.setVisibility(View.GONE);
                    next.setVisibility(View.GONE);
                }
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    public void createRadioButtons(RadioGroup options) {
        for (int i = 0; i < 4; i++) {
            RadioButton option = new RadioButton(this);
            option.setText(answers[questionNum * 4 + i]);
            options.addView(option);
        }
    }
}
