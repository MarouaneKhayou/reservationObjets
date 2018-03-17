/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Marouane
 */
@Entity
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String idReservation;
    private int dureeLocation;
    private int dureeEffectiveLocation;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateEffectiveLocation;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateLimiteRetour;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateLimiteRecuperation;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateRetrait;
    private int etatReservation;
    private Double amendeDegradation;
    private int dureeMaxLocationAppliquee;
    private int dureeMinLocationAppliquee;
    private int amendeDepassementJournaliereAppliquee;
    private Double prixTotal;
    @ManyToOne
    private Objet objet;
    @ManyToOne
    private Utilisateur utilisateur;

    public Reservation() {
    }

    public String getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(String idReservation) {
        this.idReservation = idReservation;
    }

    public int getAmendeDepassementJournaliereAppliquee() {
        return amendeDepassementJournaliereAppliquee;
    }

    public void setAmendeDepassementJournaliereAppliquee(int amendeDepassementJournaliereAppliquee) {
        this.amendeDepassementJournaliereAppliquee = amendeDepassementJournaliereAppliquee;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Objet getObjet() {
        return objet;
    }

    public void setObjet(Objet objet) {
        this.objet = objet;
    }

    public int getDureeLocation() {
        return dureeLocation;
    }

    public void setDureeLocation(int dureeLocation) {
        this.dureeLocation = dureeLocation;
    }

    public int getDureeEffectiveLocation() {
        return dureeEffectiveLocation;
    }

    public void setDureeEffectiveLocation(int dureeEffectiveLocation) {
        this.dureeEffectiveLocation = dureeEffectiveLocation;
    }

    public Date getDateEffectiveLocation() {
        return dateEffectiveLocation;
    }

    public void setDateEffectiveLocation(Date dateEffectiveLocation) {
        this.dateEffectiveLocation = dateEffectiveLocation;
    }

    public Date getDateLimiteRetour() {
        return dateLimiteRetour;
    }

    public void setDateLimiteRetour(Date dateLimiteRetour) {
        this.dateLimiteRetour = dateLimiteRetour;
    }

    public Date getDateLimiteRecuperation() {
        return dateLimiteRecuperation;
    }

    public void setDateLimiteRecuperation(Date dateLimiteRecuperation) {
        this.dateLimiteRecuperation = dateLimiteRecuperation;
    }

    public Date getDateRetrait() {
        return dateRetrait;
    }

    public void setDateRetrait(Date dateRetrait) {
        this.dateRetrait = dateRetrait;
    }

    public int getEtatReservation() {
        return etatReservation;
    }

    public void setEtatReservation(int etatReservation) {
        this.etatReservation = etatReservation;
    }

    public Double getAmendeDegradation() {
        return amendeDegradation;
    }

    public void setAmendeDegradation(Double amendeDegradation) {
        this.amendeDegradation = amendeDegradation;
    }

    public int getDureeMaxLocationAppliquee() {
        return dureeMaxLocationAppliquee;
    }

    public void setDureeMaxLocationAppliquee(int dureeMaxLocationAppliquee) {
        this.dureeMaxLocationAppliquee = dureeMaxLocationAppliquee;
    }

    public int getDureeMinLocationAppliquee() {
        return dureeMinLocationAppliquee;
    }

    public void setDureeMinLocationAppliquee(int dureeMinLocationAppliquee) {
        this.dureeMinLocationAppliquee = dureeMinLocationAppliquee;
    }

    public Double getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(Double prixTotal) {
        this.prixTotal = prixTotal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Reservation other = (Reservation) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.Reservation[ id=" + id + " ]";
    }

}
