package ru.blackmirrror.egetrainer.Models;

public class Question {
    private String textQuestion;
    private String answer;
    private String subject;
    private int numberQuestion;
    private int numberNumberQuestion;

    public Question(){}
    public Question(String question, String answer) {
        this.textQuestion = question;
        this.answer = answer;
    }

    public Question(String question, String answer, String subject, int numberQuestion, int numberNumberQuestion) {
        this.textQuestion = question;
        this.answer = answer;
        this.subject = subject;
        this.numberQuestion = numberQuestion;
        this.numberNumberQuestion = numberNumberQuestion;
    }

    public String getTextQuestion() {
        return textQuestion;
    }

    public void setTextQuestion(String textQuestion) {
        this.textQuestion = textQuestion;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getNumberQuestion() {
        return numberQuestion;
    }

    public void setNumberQuestion(int numberQuestion) {
        this.numberQuestion = numberQuestion;
    }

    public int getNumberNumberQuestion() {
        return numberNumberQuestion;
    }

    public void setNumberNumberQuestion(int numberNumberQuestion) {
        this.numberNumberQuestion = numberNumberQuestion;
    }
}
