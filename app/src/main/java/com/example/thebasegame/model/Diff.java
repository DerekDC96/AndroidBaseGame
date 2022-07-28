package com.example.thebasegame.model;

public enum Diff {
    EASY(3), MED(2), HARD(1);

    // allows for int to be associated with enumeration by calling toInt();
    Diff(int value) {
        this.value = value;
    }
    private int value;

    public int toInt() {
        return value;
    }
    static public Diff getDiff(int value) {
        switch (value) {
            case 3:
                return EASY;
            case 2:
                return MED;
            default:
                return HARD;

        }
    }
}
