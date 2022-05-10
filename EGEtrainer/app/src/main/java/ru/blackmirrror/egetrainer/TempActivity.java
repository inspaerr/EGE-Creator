package ru.blackmirrror.egetrainer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class TempActivity extends AppCompatActivity {

    TextView textViewEmail, textViewPassword;
    Button logout_btn;
    SharedPreferences sharedPreferences;

    private int progress = 0;
    private ProgressBar pbHorizontal;
    private TextView tvProgressHorizontal;
    private TextView tvProgressCircle;
    Button click;

    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_EMAIL = "name";
    private static final String KEY_PASSWORD = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        pbHorizontal = (ProgressBar) findViewById(R.id.pb_horizontal);
        //tvProgressHorizontal = (TextView) findViewById(R.id.tv_progress_horizontal);
        tvProgressCircle     = (TextView)findViewById(R.id.textView4);
        click = findViewById(R.id.temp);

        textViewEmail = findViewById(R.id.textViewEmail);
        textViewPassword = findViewById(R.id.textViewPassword);

        logout_btn = findViewById(R.id.logout);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        String email = sharedPreferences.getString(KEY_EMAIL, null);
        String password = sharedPreferences.getString(KEY_PASSWORD, null);


        if (email != null || password != null){
            textViewEmail.setText("Email: "+ email);
            textViewPassword.setText("Password: "+ password);
        }

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(TempActivity.this, "Log out successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = progress + 10;
                //postProgress(progress);
            }
        });
    }

    /*private void postProgress(int progress) {
        String strProgress = String.valueOf(progress) + " %";
        pbHorizontal.setProgress(progress);
        pbHorizontal.set

        if (progress == 0) {
            pbHorizontal.setSecondaryProgress(0);
        } else {
            pbHorizontal.setSecondaryProgress(progress + 5);
        }
        //tvProgressHorizontal.setText(strProgress);
        tvProgressCircle.setText(strProgress);
    }*/
}