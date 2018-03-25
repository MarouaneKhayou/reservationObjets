package dto;

import javax.xml.bind.annotation.XmlRootElement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Marouane
 */
@XmlRootElement
public class ObjetDTO {

    private String libelle;
    private Double prixLocation;
    private Double caution;
    private Double amendeDepassement;
    private String etatObjet;
    private String defauts;
    private String description;
    private String categorie;
    private String pointLocation;

    public ObjetDTO() {
    }

    public ObjetDTO(String libelle, Double prixLocation, Double caution, Double amendeDepassement, String etatObjet, String defauts, 
            String description, String categorie, String pointLocation) {
        this.libelle = libelle;
        this.prixLocation = prixLocation;
        this.caution = caution;
        this.amendeDepassement = amendeDepassement;
        this.etatObjet = etatObjet;
        this.defauts = defauts;
        this.description = description;
        this.categorie = categorie;
        this.pointLocation = pointLocation;
    }

    public String getLibelle() {
        return libelle;
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

    public String getEtatObjet() {
        return etatObjet;
    }

    public void setEtatObjet(String etatObjet) {
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

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getPointLocation() {
        return pointLocation;
    }

    public void setPointLocation(String pointLocation) {
        this.pointLocation = pointLocation;
    }

    @Override
    public String toString() {
        return "ObjetDTO{" + "libelle=" + libelle + ", prixLocation=" + prixLocation + ", caution=" + caution + ", amendeDepassement=" + amendeDepassement + ", etatObjet=" + etatObjet + ", defauts=" + defauts + ", description=" + description + ", categorie=" + categorie + ", pointLocation=" + pointLocation + '}';
    }

}
