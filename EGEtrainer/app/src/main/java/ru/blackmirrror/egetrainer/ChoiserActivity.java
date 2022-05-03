package ru.blackmirrror.egetrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class ChoiserActivity extends AppCompatActivity {

    TextView variants, tasks, before;
    TableLayout tableLayout;

    String subject;
    int questionTotalCount;
    String[] subjects = {"mathBase", "mathProf", "rus", "eng", "inf", "phis", "soc", "his", "bio", "chem", "geog", "leat"};
    int[] questionTotalCounts = {21, 18, 27, 44, 27, 30, 25, 19, 28, 34, 31, 12};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choiser);

        variants = findViewById(R.id.textVariants);
        tasks = findViewById(R.id.textTasks);
        before = findViewById(R.id.textPrev);
        tableLayout = (TableLayout) findViewById(R.id.choiseTable);

        Bundle arguments = getIntent().getExtras();
        subject = arguments.getString("sub");
        questionTotalCount =questionTotalCounts[Arrays.asList(subjects).indexOf(subject)];

        variants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLayout("variant");
            }
        });

        tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLayout("task");
            }
        });

        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoiserActivity.this, SearchNewActivity.class);
                startActivity(intent);
            }
        });

    }

    private void showLayout(String choice) {

        tableLayout.removeAllViews();

        int ROWS;
        if (choice == "variant")
            ROWS = 20;
        else
            ROWS = questionTotalCount;
        int COLUMNS = 2;
        int temp = ROWS;

        while (temp > 0) {

            for (int i = 0; i < ROWS/2+ROWS%2; i++) {

                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
                //tableRow.setBackgroundResource(R.drawable.shelf);

                for (int j = 0; j < COLUMNS; j++) {
                    if (temp > 0) {
                        TextView textView = new TextView(this);
                        if (choice == "variant")
                            textView.setText("Вариант "+(ROWS-temp+1));
                        else
                            textView.setText("Задание "+(ROWS-temp+1));

                        tableRow.addView(textView, j);

                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ChoiserActivity.this, Questions2Activity.class);
                                intent.putExtra("sub", subject);
                                startActivity(intent);
                            }
                        });
                    }
                    temp--;
                }

                tableLayout.addView(tableRow, i);
            }
        }
    }
}