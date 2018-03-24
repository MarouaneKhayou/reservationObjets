/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Objet;
import bean.PointLocation;
import bean.Reservation;
import bean.Utilisateur;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Marouane
 */
@Stateless
public class ReservationsFacade extends AbstractFacade<Reservation> {

    @PersistenceContext(unitName = "reservationObjetsPU")
    private EntityManager em;
    @EJB
    private ObjetFacade objetFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void validerContrat(Reservation reservation) {
        reservation.setEtatReservation(1);
        edit(reservation);
        reservation.getObjet().setEtatObjet(2);
        objetFacade.edit(reservation.getObjet());
    }

    public List<Reservation> getUserReservationsEncours(Utilisateur utilisateur) {
        return getUserReservationByEtatTemplate(utilisateur, 0, null);
    }

    public Reservation getReservationById(String idReservation) {
        List<Reservation> res = em.createQuery("SELECT R FROM Reservation AS R WHERE R.idReservation='" + idReservation + "'").getResultList();
        if (res.isEmpty()) {
            return null;
        }
        return res.get(0);
    }

    /**
     * Modifier la durée d'une location
     *
     * @param reservation: la réservation
     * @param dureeLocation: la nouvelle durée
     */
    public void updateReservationDureeLocation(Reservation reservation, Integer dureeLocation) {
        em.createQuery("UPDATE Reservation AS r SET r.dureeLocation=" + dureeLocation).executeUpdate();
        reservation.setDureeLocation(dureeLocation);
    }

    public List<Reservation> getUserReservations(Utilisateur utilisateur, PointLocation pointLocation) {
        return getUserReservationByEtatTemplate(utilisateur, 0, pointLocation);
    }

    public List<Reservation> getUserLocationsEncours(Utilisateur utilisateur, PointLocation pointLocation) {
        return getUserReservationByEtatTemplate(utilisateur, 1, pointLocation);
    }

    public List<Reservation> getAllUserLocations(Utilisateur utilisateur, PointLocation pointLocation) {
        return getUserReservationByEtatTemplate(utilisateur, -1, pointLocation);
    }

    private List<Reservation> getUserReservationByEtatTemplate(Utilisateur utilisateur, Integer etatReservation,
            PointLocation pointLocation) {
        String req = "SELECT r FROM Reservation AS r WHERE r.utilisateur.id=" + utilisateur.getId();
        if (etatReservation != null) {
            req += " AND r.etatReservation=" + etatReservation;
        }
        if (pointLocation != null) {
            req += " AND r.objet.pointLocation.id=" + pointLocation.getId();
        }
        return em.createQuery(req).getResultList();
    }

    public ReservationsFacade() {
        super(Reservation.class);
    }

    /**
     * Teste si l'identifiant de la réservation existe au niveau de la base de
     * données
     *
     * @param idReservation : identifiant de la réservation
     * @return true si l'identifiant existe, false sinon
     */
    public boolean ifReservationIdExists(String idReservation) {
        return em.createQuery("SELECT r FROM Reservation AS r WHERE r.idReservation='" + idReservation + "'").getResultList().size() > 0;
    }

    /**
     * Créer une reservation
     *
     * @param reservation: la réservation
     */
    public void createReservation(Reservation reservation) {
        reservation.getObjet().setEtatObjet(1);
        objetFacade.edit(reservation.getObjet());
        edit(reservation);
    }

    public void removeReservation(Reservation selected) {
        Objet objet = objetFacade.find(selected.getObjet().getId());
        objet.setEtatObjet(0);
        objetFacade.edit(objet);
        remove(selected);
    }

    public Reservation getReservationByObjetId(String idObjet) {
        List<Reservation> res = em.createQuery("SELECT R FROM Reservation AS R WHERE R.objet.idObjet='" + idObjet + "'").getResultList();
        if (res.isEmpty()) {
            return null;
        }
        return res.get(0);
    }

    public void validerRetourObjet(Reservation reservation) {
        reservation.setEtatReservation(-1);
        edit(reservation);
        reservation.getObjet().setEtatObjet(0);
        objetFacade.edit(reservation.getObjet());
    }

}
