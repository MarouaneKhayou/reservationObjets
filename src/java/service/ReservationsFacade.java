/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Objet;
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
    
    public List<Reservation> getUserReservationsEncours(Utilisateur utilisateur) {
        return getUserReservationByEtatTemplate(utilisateur, 0);
    }
    
    public void updateReservationDureeLocation(Reservation reservation, Integer dureeLocation) {
        em.createQuery("UPDATE Reservation AS r SET r.dureeLocation=" + dureeLocation).executeUpdate();
        reservation.setDureeLocation(dureeLocation);
    }
    
    public List<Reservation> getUserReservations(Utilisateur utilisateur) {
        return getUserReservationByEtatTemplate(utilisateur, 0);
    }
    
    private List<Reservation> getUserReservationByEtatTemplate(Utilisateur utilisateur, Integer etatReservation) {
        String req = "SELECT r FROM Reservation AS r WHERE r.utilisateur.id=" + utilisateur.getId();
        if (etatReservation != null) {
            req += " AND r.etatReservation=" + etatReservation;
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
    
}
