package ru.blackmirrror.egetrainer.Models;

public class Task {
    private String subject;
    private String task;
    private String number;

    public Task() {}

    public Task(String subject, String task, String number) {
        this.subject = subject;
        this.task = task;
        this.number = number;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
