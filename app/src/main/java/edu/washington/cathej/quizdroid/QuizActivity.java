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

    private Topic topic;
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

        initializeState(getIntent());

        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.equals(stages[0]) || (status.equals(stages[2]) && button.getText() == "Next")) {
                    transitionToQuestion(button);
                } else if (status.equals(stages[1]) &&
                        questionFragment.options.getCheckedRadioButtonId() != -1) {
                    transitionToAnswer(button);
                } else if (status.equals(stages[2]) && button.getText().equals("Finish")) {
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    v.getContext().startActivity(intent);
                }
            }
        });
    }

    // Initaializes basic content that is needed to run the activity.
    public void initializeState(Intent i) {
        topic = (Topic) i.getSerializableExtra("topic");

        Bundle bOverview = new Bundle();
        bOverview.putSerializable("topic", topic);
        overviewFragment = new OverviewFragment();
        overviewFragment.setArguments(bOverview);

        commitTransaction(overviewFragment);
    }

    // This method transitions the display to a question fragment
    public void transitionToQuestion(Button button) {
        Bundle bQuestion = new Bundle();
        bQuestion.putSerializable("question", topic.getQuestions().get(questionNum));
        bQuestion.putInt("questionNum", questionNum);
        questionFragment = new QuestionFragment();
        questionFragment.setArguments(bQuestion);

        commitTransaction(questionFragment);

        status = stages[1];
        button.setText("Submit");
    }

    // This method transitions the display to an answer fragment
    public void transitionToAnswer(Button button) {
        if (questionNum == topic.getQuestions().size() - 1) {
            button.setText("Finish");
        } else {
            button.setText("Next");
        }
        int selectedId = questionFragment.options.getCheckedRadioButtonId();
        RadioButton theirAnswer = (RadioButton) questionFragment.options.findViewById(selectedId);
        int answerIndex = questionFragment.options.indexOfChild(theirAnswer);
        if (topic.getQuestions().get(questionNum).isCorrect(answerIndex)) {
            correctAnswers++;
        }

        questionNum++;

        Bundle bAnswer = new Bundle();
        bAnswer.putSerializable("topic", topic);
        bAnswer.putInt("correctAnswers", correctAnswers);
        bAnswer.putInt("questionNum", questionNum);
        bAnswer.putInt("answerIndex", answerIndex);

        answerFragment = new AnswerFragment();
        answerFragment.setArguments(bAnswer);

        commitTransaction(answerFragment);
        status = stages[2];
    }

    // This function commits transactions for fragments.
    public void commitTransaction(Fragment f) {
        FragmentTransaction tx = getFragmentManager().beginTransaction();
        tx.replace(R.id.content, f);
        tx.commit();
    }
}
