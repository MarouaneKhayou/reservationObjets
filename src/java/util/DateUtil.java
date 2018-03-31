/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Marouane
 */
public class DateUtil {

    public static List<LocalDate> feriesDates = new ArrayList<>();

    /**
     * Initialiser la liste des jours fériés, le jour et le mois d'un jour férié
     * est fixe alors que l'année correspond à l'année en cours
     */
    static {
        feriesDates.add(Year.now().atMonth(Month.JANUARY).atDay(12));
        feriesDates.add(Year.now().atMonth(Month.APRIL).atDay(2));
        feriesDates.add(Year.now().atMonth(Month.MAY).atDay(1));
        feriesDates.add(Year.now().atMonth(Month.MAY).atDay(8));
        feriesDates.add(Year.now().atMonth(Month.MAY).atDay(10));
        feriesDates.add(Year.now().atMonth(Month.MAY).atDay(21));
        feriesDates.add(Year.now().atMonth(Month.JULY).atDay(14));
        feriesDates.add(Year.now().atMonth(Month.AUGUST).atDay(15));
        feriesDates.add(Year.now().atMonth(Month.NOVEMBER).atDay(1));
        feriesDates.add(Year.now().atMonth(Month.NOVEMBER).atDay(11));
        feriesDates.add(Year.now().atMonth(Month.DECEMBER).atDay(25));
    }

    /**
     * Calculer la la durée effective de location à partir de la date de retrait
     * et la date de retour de l'objet
     *
     * @param dateRetrait
     * @param dateRetour
     * @return: le nombre de jours de location
     */
    public static int calculDureeEffectiveLocation(Date dateRetrait, Date dateRetour) {
        int nbr = 0;
        LocalDate dr = dateRetrait.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate dR = dateRetour.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (dr.isBefore(dR)) {
            for (LocalDate date = dr; date.isBefore(dR); date = date.plusDays(1)) {
                if (date.getDayOfWeek().getValue() != 7 & !ifHoliday(date)) {
                    nbr++;
                }
            }
            return nbr++;
        } else {
            return -1;
        }
    }

    /**
     * Ajouter un nombre de jours à une date
     *
     * @param date: la date
     * @param nbrDays: le nombre de jours à ajouter
     * @return
     */
    public static Date addDaysToDate(Date date, int nbrDays) {
        if (nbrDays < 0) {
            return null;
        } else {
            LocalDate d = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            for (int i = 0; i < nbrDays; i++) {
                d = d.plusDays(1);

                while (d.getDayOfWeek().getValue() == 7 | ifHoliday(d)) {
                    d = d.plusDays(1);
                }
            }

            return Date.from(d.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
    }

    /**
     * Teste si un jour est un jour ferié
     *
     * @param date: la date
     * @return: true si la date corresond à un jour férié, false sinon
     */
    public static boolean ifHoliday(LocalDate date) {
        for (LocalDate d : feriesDates) {
            if (d.equals(date)) {
                return true;
            }
        }
        return false;
    }
}
