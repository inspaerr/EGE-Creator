package ru.blackmirrror.egetrainer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import ru.blackmirrror.egetrainer.Models.Question;
import ru.blackmirrror.egetrainer.Models.QuizDbHelper;

public class Questions2Activity extends AppCompatActivity implements View.OnClickListener{

    private static final long COUNTDOWN_IN_MILLIS = 14400000;
    private CountDownTimer countDownTimer;
    private long timeLeftMillis;

    TextView timer, question, questionNumber, tf;
    EditText answer;
    Button next, before;

    ArrayList<TextView> textViewArrayList;

    ArrayList<Question> questionArrayList;
    ArrayList<String> answers;
    int questionCounter = 0;
    int questionTotalCount;
    Question currentQuestion;
    String an;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions2);
        showTimer();
        setUpUI();
        startTest();
    }

    private void setUpUI() {
        next = findViewById(R.id.btnAfter);
        before = findViewById(R.id.btnBefore);
        answer = findViewById(R.id.etAnswer);
        timer = findViewById(R.id.tvTime);
        question = findViewById(R.id.tvQuestion);
        questionNumber = findViewById(R.id.tvNumberQuestion);

        tf = findViewById(R.id.tvFinish);

        fetchDbHelper();

        questionTotalCount = questionArrayList.size();
        showMenu(questionTotalCount);

        answers = new ArrayList<String>();
        for (int i = 0; i < questionTotalCount; i++)
            answers.add("");
    }

    private void fetchDbHelper(){
        QuizDbHelper quizDbHelper = new QuizDbHelper(this);
        Bundle arguments = getIntent().getExtras();
        String sub = arguments.getString("sub");
        questionArrayList = quizDbHelper.getAllQuestions(sub);
    }

    private void startTest(){
        if (questionCounter == questionTotalCount-1)
            next.setText("Закончить");
        else
            next.setText("Далее");
        currentQuestion = questionArrayList.get(questionCounter);
        question.setText(currentQuestion.getTextQuestion());
        answer.setText(answers.get(questionCounter));
        questionNumber.setText("Задача "+(questionCounter+1));

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (next.getText().equals("Далее")) {
                    an = answer.getText().toString();
                    answers.set(questionCounter, an);
                    questionCounter++;
                    startTest();
                }
                else {
                    showResult();
                }
            }
        });

        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (questionCounter != 0) {
                    an = answer.getText().toString();
                    answers.set(questionCounter, an);
                    questionCounter--;
                    startTest();
                }
            }
        });

        for (int i = 0; i < questionTotalCount; i++){
            textViewArrayList.get(i).setOnClickListener(this);
        }

        tf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showResult();}
        });


    }

    @Override
    public void onClick(View v) {
        an = answer.getText().toString();
        answers.set(questionCounter, an);
        TextView temp = (TextView) v;
        questionCounter = Integer.parseInt(temp.getText().toString())-1;
        startTest();
    }

    private void showTimer() {

        timeLeftMillis = COUNTDOWN_IN_MILLIS;

        countDownTimer = new CountDownTimer(timeLeftMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftMillis = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                timeLeftMillis = 0;
                updateTimer();
                countDownTimer.cancel();
                showResult();
            }
        }.start();
    }

    private void updateTimer() {
        int hours = (int) (((timeLeftMillis) / 1000) / 60) / 60;
        int minutes = (int) ((timeLeftMillis / 1000) / 60) % 60;
        int seconds = (int) (timeLeftMillis / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);

        timer.setText(timeFormatted);

        /*if (timeLeftMillis < 10000) {
            timer.setTextColor(Color.RED);
        }
        else {
            timer.setTextColor(colorStateList);
        }*/
    }

    private void showResult() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Вы уверены, что хотите закончить тест?");

        LayoutInflater inflater = LayoutInflater.from(this);
        View resultWindow = inflater.inflate(R.layout.before_result_window, null);
        dialog.setView(resultWindow);

        dialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO активность
                Intent intent = new Intent(Questions2Activity.this, ResultActivity.class);
                intent.putExtra("questionTotalCount", questionTotalCount);
                for (int i = 0; i < questionTotalCount; i++) {
                    intent.putExtra("a" + i, questionArrayList.get(i).getAnswer());
                    intent.putExtra("ay" + i, answers.get(i));
                }
                startActivity(intent);
            }
        });

        dialog.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();

    }

    private void showMenu(int count){
        LinearLayout linearLayout= (LinearLayout) findViewById(R.id.layout_menu);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(4,4,4, 0);

        textViewArrayList = new ArrayList<TextView>();

        for (int i = 0; i < count; i++){
            TextView textView = new TextView(this);
            textView.setText(""+(i+1));
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            textView.setBackgroundResource(R.drawable.number_question);
            textView.setLayoutParams(layoutParams);
            textViewArrayList.add(textView);
            linearLayout.addView(textView, i);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null)
            countDownTimer.cancel();
    }
}