package ru.blackmirrror.egetrainer.Models;

import android.provider.BaseColumns;

public final class QuizContract {

    public QuizContract(){}

    public static class QuestionTable implements BaseColumns {

        public static final String TABLE_NAME = "quiz_questions";
        public static final String COLUMN_QUESTION = "question";
        public static final String ANSWER = "answer";
        public static final String SUBJECT = "subject";
        public static final String NUMBER_QUESTION = "number_question";
        public static final String NUMBER_NUMBER_QUESTION = "number_number_question";

    }

}
