package ru.blackmirrror.egetrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import ru.blackmirrror.egetrainer.Models.Question;
import ru.blackmirrror.egetrainer.Models.QuizDbHelper;

public class QuestionsActivity extends AppCompatActivity {

    TextView totalQuestions, score, wrong, correct, timer, question;
    EditText answer;
    Button next;

    ArrayList<Question> questionArrayList;
    int questionCounter;
    int questionTotalCount;
    Question currentQuestion;
    boolean answered;

    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        setUpUI();
        fetchDbHelper();
    }

    private void setUpUI() {
        next = findViewById(R.id.btnNext);
        answer = findViewById(R.id.editTxtAnswer);
        totalQuestions = findViewById(R.id.txtTotalQuestion);
        score = findViewById(R.id.txtScore);
        wrong = findViewById(R.id.txtWrong);
        correct = findViewById(R.id.txtCorrect);
        timer = findViewById(R.id.txtTimer);
        question = findViewById(R.id.txtQuestion);
    }

    private void fetchDbHelper(){
        QuizDbHelper quizDbHelper = new QuizDbHelper(this);
        questionArrayList = quizDbHelper.getAllQuestions("math");
        
        startQuestions();
    }

    private void startQuestions() {
        questionTotalCount = questionArrayList.size();
        Collections.shuffle(questionArrayList);
        showQuestions();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered){
                    if (answer.getText().toString() != null){
                        QuestionOperations();

                    }
                    else {
                        Toast.makeText(QuestionsActivity.this, "Please answer",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void QuestionOperations(){
        answered = true;
        String ansNumber = answer.getText().toString();
        checkSolution(ansNumber);
    }

    private void checkSolution(String ansNumber) {
        if (currentQuestion.getAnswer().equals(ansNumber)){
            Toast.makeText(QuestionsActivity.this, "Correct", Toast.LENGTH_SHORT).show();
            showQuestions();
        }
        else {
            Toast.makeText(QuestionsActivity.this, "Uncorrect", Toast.LENGTH_SHORT).show();
            showQuestions();
        }
        if (questionCounter < questionTotalCount){
            next.setText("Finish");
        }
    }

    private void showQuestions(){
        if (questionCounter < questionTotalCount){
            currentQuestion = questionArrayList.get(questionCounter);
            question.setText(currentQuestion.getTextQuestion());
            answer.getText().toString();

            questionCounter++;
            answered = false;

            next.setText("Confirm");
            totalQuestions.setText("Questions:"+questionCounter+"/"+questionTotalCount);
        }
        else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent intent = new Intent(getApplicationContext(), QuestionsActivity.class);
                    startActivity(intent);
                }
            }, 500);
        }
    }
}
