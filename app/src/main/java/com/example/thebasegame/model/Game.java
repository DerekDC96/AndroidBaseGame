package com.example.thebasegame.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {
    final int NUMQUESTIONS = 5;

    private Diff diff;
    private int bits;
    private int base;
    private ArrayList<Question> questionList;

    private int score;
    private int maxScore = 100;
    private int curQuestionIndex;

    // EFFECTS: Constructs a game with given base number
    public Game(int base, Diff diff) {
        this.diff = diff;
        this.base = base;
        this.bits = 4;
        questionList = new ArrayList<>(NUMQUESTIONS);
        curQuestionIndex = 0;
        generateQuestions();
    }

    // REQUIRES: curQuestion is valid index
    // MODIFIES:this
    // EFFECTS: returns the curr question
    public Question getCurrQuestion() {
        return questionList.get(curQuestionIndex);

    }

    // MODIFIES: this
    // EFFECTS: adds calculated score from question to score
    public void processAnswer(String answer) {
        int calc = getCurrQuestion().calculateScore(answer);

        score += calc;
    }

    public void nextQuestion() {
        curQuestionIndex++;
    }


    private void generateQuestions() {
        for (int i = 0; i < NUMQUESTIONS; i++) {
            questionList.add(new Question(base, (int) (Math.random() * (Math.pow(base, bits) - 1)),bits, maxScore/NUMQUESTIONS, diff));
        }
    }

    public int getScore() {
        return score;
    }

    public int getNUMQUESTIONS() {
        return NUMQUESTIONS;
    }

    public int getBase() {
        return base;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public int getCurrQuestionIndex() {
        return curQuestionIndex;
    }

    public Diff getDiff() {
        return diff;
    }
}
