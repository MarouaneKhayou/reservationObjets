/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Categorie;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Marouane
 */
@Stateless
public class CategorieFacade extends AbstractFacade<Categorie> {

    @PersistenceContext(unitName = "reservationObjetsPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CategorieFacade() {
        super(Categorie.class);
    }

    public Categorie getCategorieByName(String nom) {
        List<Categorie> res = em.createQuery("SELECT c FROM Categorie AS c WHERE UPPER(c.nom) like  UPPER('%" + nom + "%')").getResultList();
        if (res.isEmpty()) {
            return null;
        }
        return res.get(0);
    }

}
