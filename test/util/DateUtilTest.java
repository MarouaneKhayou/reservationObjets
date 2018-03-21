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
        LocalDate date = LocalDate.parse("2018-03-21");
        Date res = DateUtil.addDaysToDate(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()), 6);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        assertTrue(LocalDate.parse("2018-03-28").equals(res.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));

        fail("The test case is a prototype.");
    }

    /**
     * Test of ifHoliday method, of class DateUtil.
     */
    @Test
    public void testIfHoliday() {
        System.out.println("ifHoliday");
        LocalDate date = null;
        boolean expResult = false;
        boolean result = DateUtil.ifHoliday(date);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
