/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Categorie;
import bean.Objet;
import bean.PointLocation;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Marouane
 */
@Stateless
public class ObjetFacade extends AbstractFacade<Objet> {

    @PersistenceContext(unitName = "reservationObjetsPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ObjetFacade() {
        super(Objet.class);
    }

    /**
     * Méthode pour chercher la liste des objets selon plusieurs critres
     *
     * @param pointLocation: le point de location
     * @param etatObjet: 0: non reservé, 1: reservé, 2: loué
     * @param libelle: llibelle de l'objet
     * @param categorie: catégorie
     * @return
     */
    public List<Objet> getObjectsByCriteres(PointLocation pointLocation, int etatObjet, String libelle, Categorie categorie) {
        String req = "SELECT o FROM Objet o WHERE 1=1";
        if (pointLocation != null) {
            req += " And o.pointLocation.id=" + pointLocation.getId();
        }
        if (etatObjet != -1) {
            req += " And o.etatObjet = " + etatObjet;
        }
        if (categorie != null) {
            req += " And o.categorie.id= " + categorie.getId();
        }
        if (libelle != null) {
            if (!libelle.equals("")) {
                req += " And UPPER(o.libelle) like  UPPER('%" + libelle + "%') ";
            }
        }
        return em.createQuery(req).getResultList();
    }

    /**
     * Méthode pour chercher la liste des objets selon plusieurs critres
     *
     * @param libelle: llibelle de l'objet
     * @param categorie: catégorie
     * @return
     */
    public List<Objet> getObjectsByCriteres(String libelle, Categorie categorie) {
        String req = "SELECT o FROM Objet o WHERE 1=1";
        if (categorie != null) {
            req += " And o.categorie.id= " + categorie.getId();
        }
        if (libelle != null) {
            if (!libelle.equals("")) {
                req += " And UPPER(o.libelle) like  UPPER('%" + libelle + "%') ";
            }
        }
        return em.createQuery(req).getResultList();
    }

    /**
     * Méthode pour chercher la liste des objets selon plusieurs critres
     *
     * @param libelle: libelle de l'objet
     * @param categorie: la catégorie
     * @param pointLocation: le point de location
     * @return: la liste des objets
     */
    public List<Objet> getObjectsByCriteres(String libelle, Categorie categorie, PointLocation pointLocation) {
        String req = "SELECT o FROM Objet o WHERE o.etatObjet=0";
        if (pointLocation != null) {
            req += " And o.pointLocation.id = " + pointLocation.getId();
        }
        if (categorie != null) {
            req += " And o.categorie.id= " + categorie.getId();
        }
        if (libelle != null) {
            if (!libelle.equals("")) {
                req += " And UPPER(o.libelle) like  UPPER('%" + libelle + "%') ";
            }
        }
        return em.createQuery(req).getResultList();
    }

    /**
     * Teste si l'identifiant de l'objet existe au niveau de la base de données
     *
     * @param id: identifiant de l'objet
     * @return true si l'identifiant existe, false sinon
     */
    public boolean ifObjetIdExists(String id) {
        return em.createQuery("SELECT o FROM Objet AS o WHERE o.idObjet='" + id + "'").getResultList().size() > 0;
    }

}
