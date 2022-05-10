package ru.blackmirrror.egetrainer;

import androidx.annotation.NonNull;
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
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import ru.blackmirrror.egetrainer.Models.DatabaseHelper;
import ru.blackmirrror.egetrainer.Models.Question;
import ru.blackmirrror.egetrainer.Models.QuizContract;
import ru.blackmirrror.egetrainer.Models.QuizDbHelper;

public class Questions2Activity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MY_APP";

    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    FirebaseStorage storage;
    StorageReference storageRef;

    private static final long COUNTDOWN_IN_MILLIS = 14400000;
    private CountDownTimer countDownTimer;
    private long timeLeftMillis;

    TextView timer, question, questionNumber;
    ImageView tf;
    EditText answer;
    Button next, before;
    ImageView imageQuestion;

    ArrayList<TextView> textViewArrayList;

    ArrayList<Question> questionArrayList;
    ArrayList<String> answers;
    int questionCounter = 0;
    int questionTotalCount;
    Question currentQuestion;
    String an;
    String subjectt, choicee, numberr;

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
    public ArrayList<Question> getAllQuestions(String subj, String choi, String numb) {

        ArrayList<Question> questionList = new ArrayList<>();
        Cursor cursor;

        String[] selectionArgs = new String[]{subj,numb};

        if (choi.equals("variant")) {
            cursor = mDb.rawQuery("SELECT * FROM " + QuizContract.QuestionTable.TABLE_NAME +
                    " WHERE " + QuizContract.QuestionTable.SUBJECT + " = ?" +
                    " AND " + QuizContract.QuestionTable.NUMBER_NUMBER_QUESTION + " = ?", selectionArgs);
        }
        else {
            cursor = mDb.rawQuery("SELECT * FROM " + QuizContract.QuestionTable.TABLE_NAME +
                    " WHERE " + QuizContract.QuestionTable.SUBJECT + " = ?" +
                    " AND " + QuizContract.QuestionTable.NUMBER_QUESTION + " = ?", selectionArgs);
        }
        /*cursor = mDb.rawQuery("SELECT * FROM " + QuizContract.QuestionTable.TABLE_NAME +
                " WHERE " + QuizContract.QuestionTable.SUBJECT + " = ?", selectionArgs);*/

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
        next = findViewById(R.id.btnAfter);
        before = findViewById(R.id.btnBefore);
        answer = findViewById(R.id.etAnswer);
        timer = findViewById(R.id.tvTime);
        question = findViewById(R.id.tvQuestion);
        questionNumber = findViewById(R.id.tvNumberQuestion);
        imageQuestion = findViewById(R.id.imvQuection);

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
        String del;
        Bundle arguments = getIntent().getExtras();
        subjectt = arguments.getString("sub");
        choicee = arguments.getString("choice");
        numberr = arguments.getString("number");
        numberr = numberr.substring(8, numberr.length());
        //questionArrayList = quizDbHelper.getAllQuestions(sub);
        questionArrayList = getAllQuestions(subjectt, choicee, numberr);
    }

    private void showPictureQuestion(int num) {
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://ege-trainer.appspot.com");
        String select;

        if (choicee.equals("variant"))
            select = subjectt + "/" + num + "/" + numberr + ".png";
        else
            select = subjectt + "/" + numberr + "/" + num + ".png";
        storageRef.child(select).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.with(Questions2Activity.this).load(uri)
                        .into(imageQuestion);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "Ошибка!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startTest(){
        if (questionCounter == questionTotalCount-1)
            next.setText("Закончить");
        else
            next.setText("Далее");
        if (questionTotalCount == 0) return;
        currentQuestion = questionArrayList.get(questionCounter);
        question.setText(currentQuestion.getTextQuestion());
        imageQuestion.setImageResource(0);
        showPictureQuestion(questionCounter+1);
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
                    an = answer.getText().toString();
                    answers.set(questionCounter, an);
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
        layoutParams.setMargins(4,4,4, 8);

        textViewArrayList = new ArrayList<TextView>();

        for (int i = 0; i < count; i++){
            TextView textView = new TextView(this);
            textView.setText(""+(i+1));
            textView.setTextColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER);
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