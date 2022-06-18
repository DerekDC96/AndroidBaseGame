package com.example.thebasegame.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {
    final int NUMQUESTIONS = 5;

    private int diff;
    private int bits;
    private int base;
    private ArrayList<Question> questionList;

    private int score;
    private int maxScore = 100;
    private int curQuestion;

    // EFFECTS: Constructs a game with given base number
    public Game(int base, int diff) {
        this.diff = diff;
        this.base = base;
        this.bits = 8;
        questionList = new ArrayList<>(NUMQUESTIONS);
        curQuestion = 0;
        generateQuestions();
    }

    // REQUIRES: curQuestion is valid index
    // MODIFIES:this
    // EFFECTS: returns the next question
    public Question getNextQuestion() {
        Question ret = questionList.get(curQuestion);
        curQuestion++;
        return ret;
    }

    // MODIFIES: this
    // EFFECTS: increases curQuestion by 1 and adds calculated score from question to score
    public void processAnswer(String answer) {
        score += getNextQuestion().calculateScore(answer);
    }


    private void generateQuestions() {
        for (int i = 0; i < NUMQUESTIONS; i++) {
            questionList.add(new Question(base, (int) (Math.random() * (Math.pow(base, bits) - 1)),bits, maxScore/NUMQUESTIONS, diff));
        }
    }

    public int getScore() {
        return score;
    }


}
