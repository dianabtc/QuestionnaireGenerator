package com.example.project;

public class Answer  {
    private String text, value;
    private static int idInc = 0;
    private int id;

    public Answer(String text, String value) {
        this.text = text;
        this.value = value;
        idInc++;
    }

    public static void setIdInc(int idInc) {
        Answer.idInc = idInc;
    }

    public static int getIdInc() {
        return idInc;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return text + " " + value + " " + id + " ";
    }
}
