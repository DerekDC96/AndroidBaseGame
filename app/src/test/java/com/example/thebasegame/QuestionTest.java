package com.example.thebasegame;

import static org.junit.Assert.assertEquals;

import com.example.thebasegame.model.Diff;
import com.example.thebasegame.model.Question;

import org.junit.Test;

public class QuestionTest {

    @Test
    public void testCalculateScore() {
        Question q1;
        q1 = new Question(2, 4,16, 10, Diff.getDiff(2));
        assertEquals( 10, q1.calculateScore("4"));

        Question q2 = new Question(3, 9,16, 10, Diff.getDiff(2));
        assertEquals( 5, q2.calculateScore("3"));

        Question q3 = new Question(2, 18,16, 10, Diff.getDiff(2));
        assertEquals( 9, q3.calculateScore("16"));

        Question q4 = new Question(2, 2048,16, 10, Diff.getDiff(2));
        assertEquals( 0, q4.calculateScore("512"));

        Question q5 = new Question(2, 2048,16, 10, Diff.getDiff(3));
        assertEquals( 3, q5.calculateScore("512"));

        Question q6 = new Question(2, 2048,16, 10, Diff.getDiff(3));
        assertEquals( 3, q6.calculateScore("512"));
    }

    @Test
    public void testBaseToTen() {
        Question q1;
        q1 = new Question(2, 4,16, 10, Diff.getDiff(2));
        assertEquals(1171, q1.baseToTen("10010010011"));

        Question q2;
        q2 = new Question(3, 4,16, 10, Diff.getDiff(2));
        assertEquals(35900, q2.baseToTen("1211020122"));

    }

    @Test
    public void testTenToBase() {
        Question q1;
        q1 = new Question(2, 1171,16, 10, Diff.getDiff(2));
        assertEquals("10010010011", q1.tenToBase());

        Question q2;
        q2 = new Question(3, 35900,16, 10, Diff.getDiff(2));
        assertEquals("1211020122", q2.tenToBase());

    }
}
