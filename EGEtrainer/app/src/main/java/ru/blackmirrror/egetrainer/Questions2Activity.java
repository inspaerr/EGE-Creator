package ru.blackmirrror.egetrainer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import ru.blackmirrror.egetrainer.Models.DatabaseHelper;
import ru.blackmirrror.egetrainer.Models.Question;
import ru.blackmirrror.egetrainer.Models.QuizContract;
import ru.blackmirrror.egetrainer.Models.QuizDbHelper;

public class Questions2Activity extends AppCompatActivity implements View.OnClickListener{

    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;

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

    ImageButton imageButton;
    boolean flag = true;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions2);

        mDBHelper = new DatabaseHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

        showTimer();
        setUpUI();
        startTest();
    }

    @SuppressLint("Range")
    public ArrayList<Question> getAllQuestions(String subj) {

        ArrayList<Question> questionList = new ArrayList<>();

        String[] selectionArgs = new String[]{subj};
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + QuizContract.QuestionTable.TABLE_NAME +
                " WHERE " + QuizContract.QuestionTable.SUBJECT + " = ?", selectionArgs);

        if (cursor.moveToFirst()) {
            do {

                Question question = new Question();
                question.setTextQuestion(cursor.getString(cursor.getColumnIndex(QuizContract.QuestionTable.COLUMN_QUESTION)));
                question.setAnswer(cursor.getString(cursor.getColumnIndex(QuizContract.QuestionTable.ANSWER)));
                question.setSubject(cursor.getString(cursor.getColumnIndex(QuizContract.QuestionTable.SUBJECT)));
                question.setNumberQuestion(cursor.getInt(cursor.getColumnIndex(QuizContract.QuestionTable.NUMBER_QUESTION)));
                question.setNumberNumberQuestion(cursor.getInt(cursor.getColumnIndex(QuizContract.QuestionTable.NUMBER_NUMBER_QUESTION)));

                questionList.add(question);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return questionList;
    }

    private void setUpUI() {
        imageButton = findViewById(R.id.imButton);
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
        //QuizDbHelper quizDbHelper = new QuizDbHelper(this);
        Bundle arguments = getIntent().getExtras();
        String sub = arguments.getString("sub");
        //questionArrayList = quizDbHelper.getAllQuestions(sub);
        questionArrayList = getAllQuestions(sub);
    }

    private void startTest(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        if (questionCounter == questionTotalCount-1)
            next.setText("Закончить");
        else
            next.setText("Далее");
        if (questionTotalCount == 0) return;
        currentQuestion = questionArrayList.get(questionCounter);
        question.setText(currentQuestion.getTextQuestion());
        answer.setText(answers.get(questionCounter));
        questionNumber.setText("Задача "+(questionCounter+1));

        flag = true;
        imageButton.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        imageButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // меняем изображение на кнопке
                if (flag) {
                    String query = "INSERT INTO " + QuizContract.FavouriteTable.TABLE_NAME_2 + "(" + QuizContract.FavouriteTable.USER_UID + ", " + QuizContract.FavouriteTable.QUESTION_ID  + ") "
                            + "VALUES " + "(" + user.getUid() + ", " + QuizContract.QuestionTable._ID + ")";
                    imageButton.setImageResource(R.drawable.ic_baseline_favorite_24);
                    mDb.rawQuery(query, null);
                }
                else
                    // возвращаем первую картинку
                    imageButton.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                flag = !flag;
            }
        });

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
                onResActivity();
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
                onResActivity();
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

    private void onResActivity(){
        //TODO активность
        Intent intent = new Intent(Questions2Activity.this, ResultActivity.class);
        intent.putExtra("questionTotalCount", questionTotalCount);
        for (int i = 0; i < questionTotalCount; i++) {
            intent.putExtra("a" + i, questionArrayList.get(i).getAnswer());
            intent.putExtra("ay" + i, answers.get(i));
        }
        startActivity(intent);
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