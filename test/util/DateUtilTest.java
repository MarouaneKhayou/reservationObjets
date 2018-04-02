/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Marouane
 */
public class DateUtilTest {

    public DateUtilTest() {
    }

    /**
     * Test of addDaysToDate method, of class DateUtil.
     */
    @Test
    public void testAddDaysToDate() {
        LocalDate date1 = LocalDate.parse("2018-03-21");
        Date res1 = DateUtil.addDaysToDate(Date.from(date1.atStartOfDay(ZoneId.systemDefault()).toInstant()), 6);
        assertTrue(LocalDate.parse("2018-03-28").equals(res1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));

        date1 = LocalDate.parse("2018-04-06");
        Date res2 = DateUtil.addDaysToDate(Date.from(date1.atStartOfDay(ZoneId.systemDefault()).toInstant()), 4);
        assertTrue(LocalDate.parse("2018-04-11").equals(res2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));

        date1 = LocalDate.parse("2018-12-21");
        Date res3 = DateUtil.addDaysToDate(Date.from(date1.atStartOfDay(ZoneId.systemDefault()).toInstant()), 5);
        assertTrue(LocalDate.parse("2018-12-28").equals(res3.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));

    }

    /**
     * Test of calculDureeEffectiveLocation method, of class DateUtil.
     */
    @Test
    public void testCalculDureeEffectiveLocation() {
        LocalDate date1 = LocalDate.parse("2018-04-02");
        LocalDate date2 = LocalDate.parse("2018-04-10");

        int res1 = DateUtil.calculDureeEffectiveLocation(Date.from(date1.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(date2.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        assertEquals(7, res1);

        date1 = LocalDate.parse("2018-01-10");
        date2 = LocalDate.parse("2018-01-17");

        int res2 = DateUtil.calculDureeEffectiveLocation(Date.from(date1.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(date2.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        assertEquals(5, res2);

        date1 = LocalDate.parse("2018-04-30");
        date2 = LocalDate.parse("2018-05-09");

        int res3 = DateUtil.calculDureeEffectiveLocation(Date.from(date1.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(date2.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        assertEquals(6, res3);
    }

    /**
     * Test of calculDureeEffectiveLocation method, of class DateUtil.
     */
    @Test
    public void testifHoliday() {
        LocalDate date1 = LocalDate.parse("2018-01-12");
        assertTrue(DateUtil.ifHoliday(date1));

        date1 = LocalDate.parse("2018-01-14");
        assertFalse(DateUtil.ifHoliday(date1));
    }
}
