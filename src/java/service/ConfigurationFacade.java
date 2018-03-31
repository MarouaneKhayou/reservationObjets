/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Configuration;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Marouane
 */
@Stateless
public class ConfigurationFacade extends AbstractFacade<Configuration> {

    @PersistenceContext(unitName = "reservationObjetsPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConfigurationFacade() {
        super(Configuration.class);
    }

    /**
     * Récuperer une variable configuration par nom
     *
     * @param nom: le nom
     * @return: objet configuration
     */
    private Configuration getConfigurationByNameTemplate(String nom) {
        List<Configuration> res = em.createQuery("SELECT c FROM Configuration AS c WHERE c.nom='" + nom + "'").getResultList();
        if (res.isEmpty()) {
            return new Configuration();
        }
        return res.get(0);

    }

    /**
     * Récuperer la durée minimale de location
     *
     * @return: configuration
     */
    public Configuration getDureeMinLocation() {
        return getConfigurationByNameTemplate("NDmL");
    }

    /**
     * Récuperer l'amende de depassement journaliere
     *
     * @return: configuration
     */
    public Configuration getAmendeDepassementJournaliere() {
        return getConfigurationByNameTemplate("ADJ");
    }

    /**
     * Récuperer le nombre maximum d'objets loués en meme temps
     *
     * @return: configuration
     */
    public Configuration getNombreMaxObjetLoue() {
        return getConfigurationByNameTemplate("NMOL");
    }

    /**
     * Récuperer la durée maximale de location
     *
     * @return: configuration
     */
    public Configuration getDureeMaxLocation() {
        return getConfigurationByNameTemplate("NDML");
    }

    /**
     * Récuperer la durée maximale de réservation
     *
     * @return: configuration
     */
    public Configuration getDureeMaxReservation() {
        return getConfigurationByNameTemplate("NDMR");
    }
}
