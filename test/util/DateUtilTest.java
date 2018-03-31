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

        LocalDate date2 = LocalDate.parse("2018-04-06");
        Date res2 = DateUtil.addDaysToDate(Date.from(date2.atStartOfDay(ZoneId.systemDefault()).toInstant()), 4);
        assertTrue(LocalDate.parse("2018-04-11").equals(res2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));

    }
}
