package com.example.thebasegame.model;

import android.util.Log;

import java.io.Serializable;

public class Question implements Serializable {

    // diff is the maximum log distance from correct answer to receive any points, 0 is hardest
    private Diff diff;

    private int base;
    private int number;
    private int bits;
    private double value;


    public Question(int base, int number, int bits, double value, Diff diff) {
        this.base = base;
        this.bits = bits;
        this.value = value;
        this.number = number;
        this.diff  = diff;
    }

    // REQUIRES: base <=9
    // EFFECTS: returns a string of the decimal number in base
    public String tenToBase() {
        StringBuilder sb = new StringBuilder();
        int k = (int)Math.floor(Math.log(number)/Math.log(base));

        int remainder = number;
        for (int i = k; i >= 0; i--) {
            int c = remainder / (int)Math.pow(base, i);
            sb.append(c);
            remainder = remainder % (int)Math.pow(base, i);
        }

        return sb.toString();
    }

    public int baseToTen(String s) {
        char[] chrs = s.toCharArray();
        int n = chrs.length;

        int ret = 0;
        for (int i = 0; i < n; i++) {
            int k = Integer.parseInt(String.valueOf(chrs[i]));
            // converts char to int, then multiply by exponentiated base and add to ret
            ret += (Math.pow(base, n-i-1) * k);
        }
        return ret;
    }



    // EFFECTS: returns the scored points for a given answer
    //          which depends on the logarithmic distances
    public int calculateScore(String answer) {

        double dist = logDist(Integer.valueOf(answer));

        if (dist >= diff.toInt()) {
            return 0;
        } else {
            return (int)(value - (dist* value/diff.toInt()));
        }
    }

    // EFFECTS: returns logarithmic distance in base
    // e.g. logdist(4,8) in base 2  = 1, logdist(4,2) in base 2 = 1
    private double logDist(int ans) {
        return Math.abs(Math.log(ans)/Math.log(base) - Math.log(number)/Math.log(base));
    }

    @Override
    public String toString() {
        return "What is " + tenToBase() + "?";
    }
}
