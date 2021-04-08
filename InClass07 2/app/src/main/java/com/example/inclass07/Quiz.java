package com.example.inclass07;

import java.io.Serializable;
import java.util.ArrayList;

public class Quiz implements Serializable {

    String image;
    ArrayList<String> choice;
    int answer;
    int id;
    String text;


    @Override
    public String toString() {
        return "Quiz{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", image='" + image + '\'' +
                ", choice=" + choice +
                ", answer=" + answer +
                '}';
    }
}
