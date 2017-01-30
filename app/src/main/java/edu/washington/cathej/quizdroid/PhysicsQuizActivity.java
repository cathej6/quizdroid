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

public class PhysicsQuizActivity extends Activity {

    private String[] questions = new String[] {
            "What is acceleration?",
            "Why hate physics??"
    };

    private String[] answers = new String[] {
            "a^2 + b^2 = c^2",
            "a + b = c",
            "ax^2 + bx + c = 0",
            "ax + b = 0",
            "infinity",
            "I don't know",
            "a rudementry number",
            "an imaginary number"
    };

    private int[] correctAnswerIndex = new int[] {
            3, 1
    };

    private int questionNum = 0;

    private int correctAnswers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_quiz);

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
                    mAnswer.setText("My answer: " + answers[correctAnswerIndex[questionNum]]);
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
