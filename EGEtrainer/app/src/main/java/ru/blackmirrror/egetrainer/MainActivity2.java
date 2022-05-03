package ru.blackmirrror.egetrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;



public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        TextView textView1 = (TextView) findViewById(R.id.first);
        TextView textView2 = (TextView) findViewById(R.id.last);

        //String FirstName = getIntent().getStringExtra("FirstName");
        //String LastName = getIntent().getStringExtra("LastName");
        //textView1.setText(FirstName);
        //textView2.setText(LastName);
    }

}