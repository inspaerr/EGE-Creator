package ru.blackmirrror.egetrainer;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import ru.blackmirrror.egetrainer.Models.DatabaseHelper;
import ru.blackmirrror.egetrainer.Models.Question;
import ru.blackmirrror.egetrainer.Models.QuizContract;

public class Favourite2Activity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    TextView question, questionNumber, tf;
    EditText answer;

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
        setContentView(R.layout.activity_favourite2);

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
        setUpUI();
        startTest();
    }

    @SuppressLint("Range")
    public void getQuestion() {
        Bundle arguments = getIntent().getExtras();
        String question_id = String.valueOf(arguments.getInt("question_id"));

        String[] selectionArgs = new String[]{question_id};
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + QuizContract.QuestionTable.TABLE_NAME +
                " WHERE " + QuizContract.QuestionTable._ID + " = ?", selectionArgs);

        if (cursor.moveToFirst()) {
            do {
                currentQuestion = new Question();
                currentQuestion.setTextQuestion(cursor.getString(cursor.getColumnIndex(QuizContract.QuestionTable.COLUMN_QUESTION)));
                currentQuestion.setAnswer(cursor.getString(cursor.getColumnIndex(QuizContract.QuestionTable.ANSWER)));
                currentQuestion.setSubject(cursor.getString(cursor.getColumnIndex(QuizContract.QuestionTable.SUBJECT)));
                currentQuestion.setNumberQuestion(cursor.getInt(cursor.getColumnIndex(QuizContract.QuestionTable.NUMBER_QUESTION)));
                currentQuestion.setNumberNumberQuestion(cursor.getInt(cursor.getColumnIndex(QuizContract.QuestionTable.NUMBER_NUMBER_QUESTION)));
                currentQuestion.setQuestionId(cursor.getInt(cursor.getColumnIndex(QuizContract.QuestionTable._ID)));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void setUpUI() {
        answer = findViewById(R.id.etAnswer);
        question = findViewById(R.id.tvQuestion);
        questionNumber = findViewById(R.id.tvNumberQuestion);
        tf = findViewById(R.id.tvFinish);
        imageButton = findViewById(R.id.imButton);
        fetchDbHelper();

        questionTotalCount = 1;
    }

    private void fetchDbHelper() {
        getQuestion();
    }

    private void startTest() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        question.setText(currentQuestion.getTextQuestion());
        questionNumber.setText("Задача " + currentQuestion.getQuestionId());
        tf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResult();
            }
        });


    }

    @Override
    public void onClick(View v) {
        an = answer.getText().toString();
        TextView temp = (TextView) v;
        questionCounter = Integer.parseInt(temp.getText().toString()) - 1;
        startTest();
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

    private void onResActivity() {
        Intent intent = new Intent(Favourite2Activity.this, ResultActivity.class);
        intent.putExtra("questionTotalCount", questionTotalCount);
        String answer_text = answer.getText().toString();
        for (int i = 0; i < questionTotalCount; i++) {
            intent.putExtra("a" + i, currentQuestion.getAnswer());
            intent.putExtra("ay" + i, answer_text);
        }
        startActivity(intent);
    }
}