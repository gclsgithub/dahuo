package com.hytc.webmanage.common.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public enum FwDateTimeUtils {
    me;

    final public LocalDateTime toLocalDateTime(final String year, final String month, final String day) {
        if (year == null || month == null || day == null) {
            return null;
        }
        try {
            LocalDate d = LocalDate.of(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day));
            return LocalDateTime.of(d, LocalTime.MIN);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     *  日付変換 String→LocalDateTime
     *
     * @param date
     * @param format
     * @return
     */
    final public LocalDateTime toLocalDateTime(final String date, FwDateTimeFormat format) {
        if (date == null || format == null) {
            return null;
        }
        DateTimeFormatter f = DateTimeFormatter.ofPattern(format.getValue());
        try {
            LocalDateTime dt = LocalDateTime.parse(date, f);
            return dt;
        } catch (Exception e) {
            try {
                LocalDate d = LocalDate.parse(date, f);
                return LocalDateTime.of(d, LocalTime.MIN);
            } catch (Exception ex) {
            }
            return null;
        }
    }

    /**
     * 日付変換 String→LocalDate
     *
     * @param date
     * @param format
     * @return
     */
    final public LocalDate toLocalDate(final String date, FwDateTimeFormat format) {
        if (date == null || format == null) {
            return null;
        }
        DateTimeFormatter f = DateTimeFormatter.ofPattern(format.getValue());
        try {
            return LocalDate.parse(date, f);
        } catch (Exception e) {
            try {
                LocalDateTime dt = LocalDateTime.parse(date, f);
                return dt.toLocalDate();
            } catch (Exception ex) {
            }
            return null;
        }
    }

    final public boolean isXMonthsPasted(LocalDateTime time, long monthsToSubtract) {
        if (time == null) {
            return false;
        }
        return this.isXMonthsPasted(LocalDate.now() //
            , LocalDate.of(time.getYear(), time.getMonth(), time.getDayOfMonth()) //
            , monthsToSubtract //
        );
    }

    /**
     * Xか月が過ぎているかどうかとチェック<br>
     *
     * @param past
     * @param monthsToSubtract
     * @return
     */
    final public boolean isXMonthsPasted(LocalDate past, long monthsToSubtract) {
        return this.isXMonthsPasted(LocalDate.now(), past, monthsToSubtract);
    }

    /**
     * Xか月が過ぎているかどうかとチェック<br>
     * today:2019/10/08 , past:2019/07/07,3か月：true<br>
     * today:2019/10/08 , past:2019/07/08,3か月：false<br>
     * today:2019/10/08 , past:2019/07/09,3か月：false<br>
     *
     * @param today
     * @param past
     * @param monthsToSubtract
     * @return
     */
    final public boolean isXMonthsPasted(LocalDate today, LocalDate past, long monthsToSubtract) {
        if (today.minusMonths(monthsToSubtract).isAfter(past)) {
            return true;
        }
        return false;
    }

    /**
     * 日付変換 LocalDate→String
     *
     * @param inDate
     * @param format
     * @return
     */
    final public String localDateToString(LocalDate inDate, final FwDateTimeFormat format) {
        if (inDate == null || format == null) {
            return null;
        }
        try {
            return inDate.format(DateTimeFormatter.ofPattern(format.getValue()));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 日付変換 LocalDateTime→String
     *
     * @param inDateTime
     * @param format
     * @return
     */
    final public String localDateTimeToString(LocalDateTime inDateTime, final FwDateTimeFormat format) {
        if (inDateTime == null || format == null) {
            return null;
        }
        try {
            return inDateTime.format(DateTimeFormatter.ofPattern(format.getValue()));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 月の月初日を取得する 2019/08/07 00:00:00 ⇒ 2019/08/01 00:00:00 2019/09/07 00:00:00 ⇒ 2019/09/01 00:00:00
     *
     * @param localDate
     * @return the first day of the month
     */
    final public LocalDateTime toLocalFirstDate(LocalDateTime localDate) {
        LocalDate firstDayOfMonth = LocalDate.of(localDate.getYear(), localDate.getMonth(), 1);
        return LocalDateTime.of(firstDayOfMonth, LocalTime.MIN);
    }

    /**
     * 月の月末日を取得する 2019/08/07 00:00:00 ⇒ 2019/08/31 00:00:00 2019/09/07 00:00:00 ⇒ 2019/09/30 00:00:00
     *
     * @param localDate
     * @return the last day of the month
     */
    final public LocalDateTime toLocalLastDate(LocalDateTime localDate) {
        LocalDate lastDayOfMonth = localDate.with(TemporalAdjusters.lastDayOfMonth()).toLocalDate();
        return LocalDateTime.of(lastDayOfMonth, LocalTime.MIN);
    }


    /**
     * 先月末日を取得する 2019/10/07 00:00:00 ⇒ 2019/09/30 00:00:00
     *
     * @param localDate
     * @return the last day of the month
     */
    final public LocalDateTime toPreviousMonthLastDate(LocalDateTime localDate) {
        LocalDateTime ldt = LocalDateTime.parse(localDate.toString());
        LocalDateTime previousMonth = ldt.minusMonths(1L);
        return toLocalLastDate(previousMonth);
    }

    /**
     * 週の週初日を取得する
     *
     * @param localDate
     * @return the last day of the week
     */
    final public LocalDateTime toBeginDayOfWeek(LocalDateTime localDate) {
        LocalDate baseDate = localDate.toLocalDate();
        LocalDate firstDayForWeek = baseDate.with(DayOfWeek.MONDAY);
        return LocalDateTime.of(firstDayForWeek, LocalTime.MIN);
    }

    /**
     * 週の週末日を取得する
     *
     * @param localDate
     * @return the last day of the week
     */
    final public LocalDateTime toEndDayOfWeek(LocalDateTime localDate) {
        LocalDate baseDate = localDate.toLocalDate();
        LocalDate endDayForWeek = baseDate.with(DayOfWeek.SUNDAY);
        return LocalDateTime.of(endDayForWeek, LocalTime.MIN);
    }
}
