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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.DateUtil;

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
    @EJB
    private ConfigurationFacade configurationFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Test les regles de gestion 15 et 16
     */
    public void verifierReservationsEncours() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        List<Reservation> reservations = em.createQuery("SELECT r FROM Reservation AS r WHERE r.etatReservation=0 AND "
                + " r.dateLimiteRecuperation<'" + sdf.format(new Date()) + "'").getResultList();
        reservations.stream().forEach((Reservation r) -> {
            r.getObjet().setEtatObjet(0);
            objetFacade.edit(r.getObjet());
            remove(r);
        });

        List<Reservation> locations = em.createQuery("SELECT r FROM Reservation AS r WHERE r.etatReservation=1").getResultList();
        locations.stream().forEach((Reservation r) -> {
            int nbrDays = DateUtil.calculDureeEffectiveLocation(r.getDateRetrait(), new Date());
            if (nbrDays > 3 * r.getDureeMaxLocationAppliquee()) {
                r.setEtatReservation(-3);
                r.getObjet().setEtatObjet(-1);
                edit(r);
                objetFacade.edit(r.getObjet());
            }
        });

    }

    /**
     * Valider une réservation
     *
     * @param reservation: la réservation à valider
     */
    public void validerContrat(Reservation reservation) {
        reservation.setEtatReservation(1);
        edit(reservation);
        reservation.getObjet().setEtatObjet(2);
        objetFacade.edit(reservation.getObjet());
    }

    /**
     * Liste des réservations en cours d'un utilisateur
     *
     * @param utilisateur: l'utilisateur
     * @return: la liste des réservations
     */
    public List<Reservation> getUserReservationsEncours(Utilisateur utilisateur) {
        return getUserReservationByEtatTemplate(utilisateur, 0, null);
    }

    /**
     * Récuperer une réservation par identifiant
     *
     * @param idReservation: identifiant de la réservation
     * @return: la réservation, ou bien null: si la réservation n'est pas
     * trouvée
     */
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

    /**
     * Liste des réservations d'un utilisateur dans un point de location
     *
     * @param utilisateur: l'utilisateur
     * @param pointLocation: le point de location
     * @return: la liste des réservations
     */
    public List<Reservation> getUserReservations(Utilisateur utilisateur, PointLocation pointLocation) {
        return getUserReservationByEtatTemplate(utilisateur, 0, pointLocation);
    }

    /**
     * Liste des des locations d'un utilisateur dans un point de location
     *
     * @param utilisateur: l'utilisateur
     * @param pointLocation: le point de location
     * @return: la liste des réservations
     */
    public List<Reservation> getUserLocationsEncours(Utilisateur utilisateur, PointLocation pointLocation) {
        return getUserReservationByEtatTemplate(utilisateur, 1, pointLocation);
    }

    /**
     * Liste des locations d'un utilisateur dans un point de location
     *
     * @param utilisateur: l'utilisateur
     * @param pointLocation: le point de location
     * @return: la liste des réservations
     */
    public List<Reservation> getAllUserLocations(Utilisateur utilisateur, PointLocation pointLocation) {
        return getUserReservationByEtatTemplate(utilisateur, -1, pointLocation);
    }

    /**
     * Liste des réservations selon plusieurs critere
     *
     * @param utilisateur: l'utilisateur
     * @param etatReservation: l'etat de réservation 0: réservation, 1:
     * location, -1: location terminée, null: toutes lesréservations
     * @param pointLocation: le point de location
     * @return: liste des réservations
     */
    private List<Reservation> getUserReservationByEtatTemplate(Utilisateur utilisateur, Integer etatReservation,
            PointLocation pointLocation) {
        String req = "SELECT r FROM Reservation AS r WHERE 1=1";
        if (utilisateur != null) {
            req += " AND r.utilisateur.id=" + utilisateur.getId();
        }
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

    /**
     * Supprimer une réservation
     *
     * @param reservation: la réservation à supprimer
     */
    public void removeReservation(Reservation reservation) {
        Objet objet = objetFacade.find(reservation.getObjet().getId());
        objet.setEtatObjet(0);
        objetFacade.edit(objet);
        remove(reservation);
    }

    /**
     * Récuperer une réservation par identifiant de l
     *
     * @param idObjet: l'identifiant de l'objet
     * @return: la réservation
     */
    public Reservation getReservationByObjetId(String idObjet) {
        List<Reservation> res = em.createQuery("SELECT R FROM Reservation AS R WHERE R.etatReservation=1 AND R.objet.idObjet='" + idObjet + "'").getResultList();
        if (res.isEmpty()) {
            return null;
        }
        return res.get(0);
    }

    /**
     * Valider le retour d'un objet
     *
     * @param reservation: la réservation
     */
    public void validerRetourObjet(Reservation reservation) {
        reservation.setEtatReservation(-1);
        edit(reservation);
        reservation.getObjet().setEtatObjet(0);
        objetFacade.edit(reservation.getObjet());
    }

}
