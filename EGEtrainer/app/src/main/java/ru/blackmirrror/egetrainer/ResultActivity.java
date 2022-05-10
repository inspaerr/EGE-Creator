package ru.blackmirrror.egetrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    TextView out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        out = findViewById(R.id.textout);

        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, SearchNewActivity.class);
                startActivity(intent);
            }
        });

        createTable();
    }

    private void createTable() {

        ArrayList<String> answers = new ArrayList<String>();
        ArrayList<String> answersYour = new ArrayList<String>();

        Bundle arguments = getIntent().getExtras();
        int questionTotalCount = arguments.getInt("questionTotalCount");
        for (int i = 0; i < questionTotalCount; i++){
            answers.add(arguments.getString("a" + i));
            answersYour.add(arguments.getString("ay" + i));
        }

        int ROWS = questionTotalCount;
        int COLUMNS = 3;

        TableLayout tableLayout = (TableLayout) findViewById(R.id.table);

        for (int i = 0; i < ROWS+1; i++) {

            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
            tableRow.setGravity(Gravity.CENTER);
            tableRow.setPadding(0,16,0,0);

            for (int j = 0; j < COLUMNS; j++) {
                TextView textView = new TextView(this);
                if (j == 0) {
                    if (i == 0) textView.setText("№");
                    else textView.setText("" + i);
                } else if (j == 1) {
                    if (i == 0) textView.setText("Ваш ответ");
                    else {
                        textView.setText(answersYour.get(i-1));
                        if (answers.get(i-1).equals(answersYour.get(i-1))) {
                            textView.setBackgroundResource(R.drawable.correct_answer);
                        } else {
                            textView.setBackgroundResource(R.drawable.correct_no_answer);
                        }
                    }
                }
                else {
                    if (i == 0) textView.setText("Верный ответ");
                    else textView.setText(answers.get(i-1));
                }
                textView.setTextColor(Color.BLACK);
                textView.setTextSize(16);
                textView.setGravity(Gravity.CENTER);
                textView.setPadding(16, 0, 16, 0);

                tableRow.addView(textView, j);
            }

            tableLayout.addView(tableRow, i);
        }
    }
}