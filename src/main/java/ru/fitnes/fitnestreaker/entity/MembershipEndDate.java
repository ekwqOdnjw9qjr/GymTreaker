package ru.fitnes.fitnestreaker.entity;

import lombok.Getter;


@Getter
public enum MembershipEndDate {
    SMALL(30L),
    BASIC(60L),
    MEDIUM(90L),
    LARGE(120L),
    QUARTERLY(180L),
    ANNUAL(360L);


    private final Long duration;

    MembershipEndDate(Long duration) {
        this.duration = duration;
    }

}

