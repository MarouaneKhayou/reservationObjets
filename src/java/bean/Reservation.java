/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.util.Date;
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
    private String id;
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
    private Double prixTotal;
    @ManyToOne
    private Objet objet;
    @ManyToOne
    private Utilisateur utilisateur;

    public Reservation() {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reservation)) {
            return false;
        }
        Reservation other = (Reservation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.Reservation[ id=" + id + " ]";
    }

}
