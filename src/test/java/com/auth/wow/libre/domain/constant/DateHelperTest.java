package com.auth.wow.libre.domain.constant;

import com.auth.wow.libre.domain.model.constant.*;
import org.junit.jupiter.api.*;

import java.time.*;
import java.util.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;

class DateHelperTest {
    @Test
    void testGetZoneIdBySite() {
        assertThat(DateHelper.getZoneIdBySite("WLUS")).isEqualTo(ZoneId.of("America/New_York"));
        assertThat(DateHelper.getZoneIdBySite("WLMX")).isEqualTo(ZoneId.of("America/Mexico_City"));
        assertThat(DateHelper.getZoneIdBySite("INVALID")).isNull();
    }

    @Test
    void testGetLocalDateTime() {
        Date now = new Date();
        LocalDateTime localDateTime = DateHelper.getLocalDateTime(now);
        assertThat(localDateTime).isNotNull();
        assertThat(localDateTime.atZone(ZoneId.systemDefault()).toInstant()).isEqualTo(now.toInstant());
    }

    @Test
    void testGetLocalDateTimeWithSiteId() {
        Date now = new Date();
        LocalDateTime localDateTime = DateHelper.getLocalDateTime(now, "WLUS");
        assertThat(localDateTime).isNotNull();
        assertThat(localDateTime.atZone(ZoneId.of("America/New_York")).toInstant()).isEqualTo(now.toInstant());
    }

    @Test
    void testFormatDateFromString() {
        String dateStr = "2025-03-13 12:30:45";
        ZoneId zoneId = ZoneId.of("America/New_York");

        Date parsedDate = DateHelper.formatDateFromString(dateStr, zoneId);
        assertThat(parsedDate).isNotNull();
        assertThat(parsedDate.toInstant().atZone(zoneId).getYear()).isEqualTo(2025);
    }

    @Test
    void testLocalDateFromDate() {
        LocalDate localDate = LocalDate.of(2025, 3, 13);
        ZoneId zoneId = ZoneId.of("America/New_York");

        Date convertedDate = DateHelper.localDateFromDate(localDate, zoneId);
        assertThat(convertedDate).isNotNull();
        assertThat(convertedDate.toInstant().atZone(zoneId).toLocalDate()).isEqualTo(localDate);
    }

    @Test
    void testDateToString() {
        Date date = new Date();
        String formattedDate = DateHelper.dateToString(date);
        assertThat(formattedDate).isNotEmpty();
    }

    @Test
    void testIncrementMinutes() {
        Date now = new Date();
        Date newDate = DateHelper.incrementMinutes(now, 10);

        assertThat(newDate).isNotNull();
        assertThat(newDate.getTime()).isEqualTo(now.getTime() + 10 * 60 * 1000);
    }
}
