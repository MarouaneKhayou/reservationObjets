/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import service.ReservationsFacade;

/**
 *
 * @author Marouane
 */
@Singleton
@Startup
public class BackgroundJobManager {

    @EJB
    private ReservationsFacade reservationsFacade;

    @Schedule(hour = "0", minute = "0", second = "0", persistent = false)
    /**
     * Lancer le job qui va se lancer chaque minuit pour tester les deux regles
     * de gestion 16 et 15
     */
    public void someMinuteJob() {
        reservationsFacade.verifierReservationsEncours();
    }
}
