package ru.fitnes.fitnestreaker.entity;

public enum MembershipEndDate {
    BASIC(30), MEDIUM(60);


    private final int duration;

    MembershipEndDate(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }
}

