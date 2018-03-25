/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Marouane
 */
@Entity
@XmlRootElement
public class Objet implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String idObjet;
    private String libelle;
    private Double prixLocation;
    private Double caution;
    private Double amendeDepassement;
    private int etatObjet;
    private String defauts;
    private String description;
    @ManyToOne
    private Categorie categorie;
    @ManyToOne
    private PointLocation pointLocation;

    public Objet() {
    }

    public String getLibelle() {
        return libelle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdObjet() {
        return idObjet;
    }

    public void setIdObjet(String idObjet) {
        this.idObjet = idObjet;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Double getPrixLocation() {
        return prixLocation;
    }

    public void setPrixLocation(Double prixLocation) {
        this.prixLocation = prixLocation;
    }

    public Double getCaution() {
        return caution;
    }

    public void setCaution(Double caution) {
        this.caution = caution;
    }

    public Double getAmendeDepassement() {
        return amendeDepassement;
    }

    public void setAmendeDepassement(Double amendeDepassement) {
        this.amendeDepassement = amendeDepassement;
    }

    public int getEtatObjet() {
        return etatObjet;
    }

    public void setEtatObjet(int etatObjet) {
        this.etatObjet = etatObjet;
    }

    public String getDefauts() {
        return defauts;
    }

    public void setDefauts(String defauts) {
        this.defauts = defauts;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public PointLocation getPointLocation() {
        return pointLocation;
    }

    public void setPointLocation(PointLocation pointLocation) {
        this.pointLocation = pointLocation;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.id);
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
        final Objet other = (Objet) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.Objet[ id=" + id + " ]";
    }

}
