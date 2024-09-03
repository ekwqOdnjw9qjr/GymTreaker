package ru.fitnes.fitnestreaker.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum MembershipType {
    SMALL(30L,10L),
    BASIC(60L,20L),
    MEDIUM(90L,30L),
    LARGE(120L,45L),
    QUARTERLY(180L,50L),
    ANNUAL(360L,60L);

    private final Long duration;
    private final Long freezeDays;

}

