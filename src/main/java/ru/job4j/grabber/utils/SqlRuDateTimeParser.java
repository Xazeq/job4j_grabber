package ru.job4j.grabber.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

public class SqlRuDateTimeParser implements DateTimeParser {
    @Override
    public LocalDateTime parse(String parse) {
        LocalDateTime result;
        String[] dateTime = parse.split(", ");
        String[] date = dateTime[0].split(" ");
        String[] time = dateTime[1].split(":");
        if (dateTime[0].equals("сегодня")) {
            result = getToday(time);
        } else if (dateTime[0].equals("вчера")) {
            result = getToday(time).minusDays(1);
        } else {
            result = getAnotherDay(date, time);
        }
        return result;
    }

    private LocalDateTime getToday(String[] time) {
        return LocalDateTime.of(
                LocalDate.now(),
                LocalTime.of(
                        Integer.parseInt(time[0]),
                        Integer.parseInt(time[1]))
                );
    }

    private LocalDateTime getAnotherDay(String[] date, String[] time) {
        return LocalDateTime.of(
                Integer.parseInt("20" + date[2]),
                getMonth(date[1]),
                Integer.parseInt(date[0]),
                Integer.parseInt(time[0]),
                Integer.parseInt(time[1])
        );
    }

    private Month getMonth(String month) {
        return switch (month) {
            case ("янв") -> Month.JANUARY;
            case ("фер") -> Month.FEBRUARY;
            case ("мар") -> Month.MARCH;
            case ("апр") -> Month.APRIL;
            case ("май") -> Month.MAY;
            case ("июн") -> Month.JUNE;
            case ("июл") -> Month.JULY;
            case ("авг") -> Month.AUGUST;
            case ("сен") -> Month.SEPTEMBER;
            case ("окт") -> Month.OCTOBER;
            case ("ноя") -> Month.NOVEMBER;
            case ("дек") -> Month.DECEMBER;
            default -> throw new IllegalStateException("Unexpected value: " + month);
        };
    }
}
