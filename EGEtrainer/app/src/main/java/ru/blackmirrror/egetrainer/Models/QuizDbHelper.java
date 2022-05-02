package ru.blackmirrror.egetrainer.Models;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.blackmirrror.egetrainer.Models.QuizContract.*;

import java.util.ArrayList;

public class QuizDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "GoQuiz2";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;

    public QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionTable.TABLE_NAME + " ( " +
                QuestionTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionTable.COLUMN_QUESTION + " TEXT, " +
                QuestionTable.ANSWER + " TEXT, " +
                QuestionTable.SUBJECT + " TEXT, " +
                QuestionTable.NUMBER_QUESTION + " INTEGER, " +
                QuestionTable.NUMBER_NUMBER_QUESTION + " INTEGER" + ")";
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);

        fillQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + QuestionTable.TABLE_NAME);
        onCreate(db);
    }

    private void addQuestions(Question question){

        ContentValues contentValues = new ContentValues();

        contentValues.put(QuestionTable.COLUMN_QUESTION, question.getTextQuestion());
        contentValues.put(QuestionTable.ANSWER, question.getAnswer());
        contentValues.put(QuestionTable.SUBJECT, question.getSubject());
        contentValues.put(QuestionTable.NUMBER_QUESTION, question.getNumberQuestion());
        contentValues.put(QuestionTable.NUMBER_NUMBER_QUESTION, question.getNumberNumberQuestion());

        db.insert(QuestionTable.TABLE_NAME, null, contentValues);
    }

    private void fillQuestionsTable(){

        Question q1 = new Question("Матеша 1.1 теееееееееееееееееееекст текк ыадопыл ф лалп вмвдамлы вжчадп впж ыжкп  ыыва п ып ыпкжы п" +
                "ыка мыдцтдыуавдл дыува дув аыжп цжцуаожфз узшфжу оауоэыпофэ  фэуокопэыкпыфд эув фэуваофэу " +
                "уаыдшпызжкпжыувопжыувоафжрыжувтамлытвыдлп деы" +
                " ыпды пыд лпш тфжяувпжеыфувпфжув жкапожвукп  дфук ардфркгфаж фжукпшвурпфу  поужщы п" +
                "ф ыурдыдкшгпершдф  фшгупр гфпдф рдфш рдкрпфг укрдгрп фдпгкпррп ф фш рукшрфдкд фд у " +
                " фукпрфдукрдфф ддфкрпдфупшрдп фупрркпфцдадфдупдк   дукшпдукпдф ф оекжу пеф жп  фжу ?", "1", "mathBase", 1, 1);
        addQuestions(q1);

        Question q2 = new Question("Матеша 2.1?", "1", "mathBase", 2, 1);
        addQuestions(q2);

        Question q3 = new Question("Матеша 3.1?", "1", "mathBase", 3, 1);
        addQuestions(q3);

        Question q4 = new Question("Матеша 4.1", "1", "mathBase", 4, 1);
        addQuestions(q4);

        Question q5 = new Question("Матеша 5.1", "1", "mathBase", 5, 1);
        addQuestions(q5);

        Question q6 = new Question("Матеша 5.1", "1", "mathBase", 6, 1);
        addQuestions(q6);

        Question q7 = new Question("Матеша 5.1", "1", "mathBase", 7, 1);
        addQuestions(q7);

        Question q8 = new Question("Матеша 5.1", "1", "mathBase", 8, 1);
        addQuestions(q8);
    }

    @SuppressLint("Range")
    public ArrayList<Question> getAllQuestions(String subj) {

        ArrayList<Question> questionList = new ArrayList<>();

        db = getReadableDatabase();

        /*String Projection[] = {
                QuestionTable._ID,
                QuestionTable.COLUMN_QUESTION,
                QuestionTable.ANSWER,
                QuestionTable.SUBJECT,
                QuestionTable.NUMBER_QUESTION,
                QuestionTable.NUMBER_NUMBER_QUESTION
        };*/

        //Cursor cursor = db.query(QuestionTable.TABLE_NAME, Projection, null,
                //null, null, null, null);

        String[] selectionArgs = new String[]{subj};
        Cursor cursor = db.rawQuery("SELECT * FROM " + QuestionTable.TABLE_NAME +
                " WHERE " + QuestionTable.SUBJECT + " = ?", selectionArgs);

        if (cursor.moveToFirst()) {
            do {

                Question question = new Question();
                question.setTextQuestion(cursor.getString(cursor.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                question.setAnswer(cursor.getString(cursor.getColumnIndex(QuestionTable.ANSWER)));
                question.setSubject(cursor.getString(cursor.getColumnIndex(QuestionTable.SUBJECT)));
                question.setNumberQuestion(cursor.getInt(cursor.getColumnIndex(QuestionTable.NUMBER_QUESTION)));
                question.setNumberNumberQuestion(cursor.getInt(cursor.getColumnIndex(QuestionTable.NUMBER_NUMBER_QUESTION)));

                questionList.add(question);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return questionList;
    }
}
