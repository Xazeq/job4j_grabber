package ru.job4j.grabber.utils;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;

public class SqlRuDateTimeParserTest {

    @Test
    public void whenTodayDate() {
        SqlRuDateTimeParser parser = new SqlRuDateTimeParser();
        LocalDateTime now = LocalDateTime.now();
        assertThat(parser.parse("сегодня, 12:49"),
                is(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 12, 49)));
    }

    @Test
    public void whenYesterdayDate() {
        SqlRuDateTimeParser parser = new SqlRuDateTimeParser();
        LocalDateTime now = LocalDateTime.now();
        assertThat(parser.parse("вчера, 11:19"),
                is(LocalDateTime.of(now.getYear(), now.getMonth(), now.minusDays(1).getDayOfMonth(), 11, 19)));
    }

    @Test
    public void whenAnotherDate() {
        SqlRuDateTimeParser parser = new SqlRuDateTimeParser();
        assertThat(parser.parse("12 май 21, 02:09"),
                is(LocalDateTime.of(2021, 5, 12, 2, 9)));
    }
}