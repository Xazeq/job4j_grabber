package ru.job4j.grabber.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

public class SqlRuDateTimeParser implements DateTimeParser {
    private final static Map<String, Month> MONTHS = new HashMap<>();

    static {
        fillMonthsMap();
    }

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
                MONTHS.get(date[1]),
                Integer.parseInt(date[0]),
                Integer.parseInt(time[0]),
                Integer.parseInt(time[1])
        );
    }

    private static void fillMonthsMap() {
        MONTHS.put("янв", Month.JANUARY);
        MONTHS.put("фер", Month.FEBRUARY);
        MONTHS.put("мар", Month.MARCH);
        MONTHS.put("апр", Month.APRIL);
        MONTHS.put("май", Month.MAY);
        MONTHS.put("июн", Month.JUNE);
        MONTHS.put("июл", Month.JULY);
        MONTHS.put("авг", Month.AUGUST);
        MONTHS.put("сен", Month.SEPTEMBER);
        MONTHS.put("окт", Month.OCTOBER);
        MONTHS.put("ноя", Month.NOVEMBER);
        MONTHS.put("дек", Month.DECEMBER);
    }
}
