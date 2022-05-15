package ru.blackmirrror.egetrainer.Models;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ru.blackmirrror.egetrainer.Models.QuizContract.*;

public class RemDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Tasks.db";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;

    public RemDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        /*final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                RemTable.TABLE_NAME + " ( " +
                RemTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RemTable.SUBJECT + " TEXT, " +
                RemTable.TASK + " TEXT, " +
                RemTable.NUMBER + " TEXT" + ")";
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);*/

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                RemTable.TABLE_NAME + " ( " +
                RemTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RemTable.SUBJECT + " TEXT, " +
                RemTable.TASK + " TEXT, " +
                RemTable.NUMBER + " TEXT" + ")";
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + QuizContract.QuestionTable.TABLE_NAME);
        onCreate(db);
    }

    public void addTask(Task task){
        db = getWritableDatabase();
        insertTask(task);
    }

    /*public void addQuestions(List<Question> questions){
        db = getWritableDatabase();
        for (Question question : questions)
            insertQuestion(question);
    }*/

    private void insertTask(Task task){

        ContentValues contentValues = new ContentValues();

        contentValues.put(QuizContract.RemTable.SUBJECT, task.getSubject());
        contentValues.put(QuizContract.RemTable.TASK, task.getTask());
        contentValues.put(QuizContract.RemTable.NUMBER, task.getNumber());

        db.insert(QuizContract.QuestionTable.TABLE_NAME, null, contentValues);
    }

    @SuppressLint("Range")
    public ArrayList<Task> getAllTasks(String subject, String task, String number) {

        ArrayList<Task> tasks = new ArrayList<>();

        db = getReadableDatabase();

        String[] selectionArgs = new String[]{subject, task, number};
        Cursor cursor = db.rawQuery("SELECT * FROM " + QuizContract.RemTable.TABLE_NAME +
                " WHERE " + QuizContract.RemTable.SUBJECT + " = ?" +
                " AND " + QuizContract.RemTable.TASK + " = ?" +
                " AND " + QuizContract.RemTable.NUMBER + " = ?"
                , selectionArgs);

        if (cursor.moveToFirst()) {
            do {

                Task task1 = new Task();
                task1.setSubject(cursor.getString(cursor.getColumnIndex(QuizContract.RemTable.SUBJECT)));
                task1.setTask(cursor.getString(cursor.getColumnIndex(QuizContract.RemTable.TASK)));
                task1.setNumber(cursor.getString(cursor.getColumnIndex(QuizContract.RemTable.NUMBER)));

                tasks.add(task1);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return tasks;
    }
}
