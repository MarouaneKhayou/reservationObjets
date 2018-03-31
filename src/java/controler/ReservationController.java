package controler;

import bean.Objet;
import bean.Reservation;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import service.ReservationsFacade;
import util.DateUtil;
import util.SessionUtil;

@Named("reservationController")
@SessionScoped
public class ReservationController implements Serializable {

    @EJB
    private ReservationsFacade ejbFacade;
    @EJB
    private service.ConfigurationFacade configurationFacade;
    private List<Reservation> items = null;
    private Reservation selected;
    private boolean isValidReservation = false;
    private Objet objet;
    private Integer dureeLocation;
    private String idReservation;
    private String idObjet;
    private List<Reservation> reservations;
    private Date dateRetour;

    public ReservationController() {
    }

    /**
     * Afficher le détail d'un objet
     */
    public void afficherObjet() {
        if (idObjet.equals("")) {
            JsfUtil.addErrorMessage("Veuillez introduire l'identifiant de l'objet");
        } else {
            selected = getFacade().getReservationByObjetId(idObjet);
            if (selected == null) {
                JsfUtil.addErrorMessage("Objet inexistante !");
                initParams();
            } else if (selected.getEtatReservation() == 0) {
                JsfUtil.addErrorMessage("Objet en cours de réservation");
            } else if (selected.getEtatReservation() == 1) {
                isValidReservation = true;
            } else if (selected.getEtatReservation() == -1) {
                JsfUtil.addErrorMessage("Réservation de l'objet terminée");
            }
        }
    }

    /**
     * Afficher le détail d'une réservation
     */
    public void afficherReservation() {
        if (idReservation.equals("")) {
            JsfUtil.addErrorMessage("Veuillez introduire une valeur");
        } else {
            selected = getFacade().getReservationById(idReservation);
            if (selected == null) {
                JsfUtil.addErrorMessage("Réservation inexistante !");
                initParams();
            } else if (selected.getEtatReservation() == 0) {
                isValidReservation = true;
            } else if (selected.getEtatReservation() == 1) {
                JsfUtil.addErrorMessage("Réservation en cours de location");
            } else if (selected.getEtatReservation() == -1) {
                JsfUtil.addErrorMessage("Réservation terminée");
            }
        }
    }

    /**
     * Préparer les objets à retrouné au stock du point de location
     */
    public void prepareObjetRetourValidation() {
        if (dateRetour == null) {
            JsfUtil.addErrorMessage("Veuillez introduire une date de retour");
        } else {
            reservations.stream().forEach((Reservation r) -> {
                r.setDateRetour(dateRetour);
                r.setDureeEffectiveLocation(DateUtil.calculDureeEffectiveLocation(r.getDateRetrait(), dateRetour));
                Double prixTotal;
                if (r.getDureeEffectiveLocation() <= r.getDureeMinLocationAppliquee()) {
                    prixTotal = (r.getDureeMinLocationAppliquee() * r.getObjet().getPrixLocation())
                            + r.getAmendeDegradation();
                } else if (r.getDureeEffectiveLocation() <= r.getDureeMaxLocationAppliquee()) {
                    prixTotal = (r.getDureeMaxLocationAppliquee() * r.getObjet().getPrixLocation())
                            + r.getAmendeDegradation();
                } else {
                    prixTotal = (r.getDureeMaxLocationAppliquee() * r.getObjet().getPrixLocation())
                            + (r.getDureeEffectiveLocation() - r.getDureeMaxLocationAppliquee())
                            * r.getAmendeDepassementJournaliereAppliquee() + r.getAmendeDegradation();
                }
                r.setPrixTotal(prixTotal);
            });
        }
    }

    /**
     * Préparer la validation de la réservation
     */
    public void prepareValidation() {
        reservations.stream().forEach((r) -> {
            r.setDateRetrait(new Date());
            r.setDateLimiteRetour(DateUtil.addDaysToDate(new Date(), r.getDureeLocation()));
        });
    }

    /**
     * Valider le retour des objets
     */
    public void validerRetourObjet() {
        reservations.stream().forEach((item) -> {
            getFacade().validerRetourObjet(item);
        });
        initParams();
        JsfUtil.addSuccessMessage("Retour effecté avec success");
    }

    /**
     * Valider le location
     */
    public void validerContrat() {
        int nbrLocationEncours = getFacade().getUserLocationsEncours(reservations.get(0).getUtilisateur(), null).size();
        nbrLocationEncours += reservations.size();
        if (nbrLocationEncours < configurationFacade.getNombreMaxObjetLoue().getValeur()) {
            reservations.stream().forEach((item) -> {
                getFacade().validerContrat(item);
            });
            initParams();
            JsfUtil.addSuccessMessage("Contrat validé avec success");
        } else {
            JsfUtil.addErrorMessage("Nombre maximum d'objets en location atteint !");
        }

    }

    /**
     * Supprimer une réservation de la liste des réservations selectionnées
     *
     * @param reservation: la réservation
     */
    public void deleteReservation(Reservation reservation) {
        reservations.remove(reservation);
        JsfUtil.addSuccessMessage("Réservation supprimée!");
    }

    /**
     * Ajouter une réservation à la liste des réservations selectionnées
     */
    public void ajouterReservation() {
        if (ifResrvationAlreadySelected()) {
            JsfUtil.addErrorMessage("Réservation déjà ajoutée");
        } else {
            selected.setAmendeDegradation(new Double(0));
            reservations.add(selected);
            idReservation = "";
            idObjet = "";
            isValidReservation = false;
            selected = new Reservation();
            JsfUtil.addSuccessMessage("Réservation ajoutée avec success");
        }
    }

    /**
     * Vérefier si une réservation n'est pas déjà selectionnée
     *
     * @return: true ou bien false
     */
    public boolean ifResrvationAlreadySelected() {
        for (Reservation r : reservations) {
            if (r.equals(selected)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Initialiser les parametres
     */
    public void initParams() {
        idReservation = "";
        idObjet = "";
        dateRetour = null;
        isValidReservation = false;
        selected = new Reservation();
        reservations = new ArrayList<>();
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public boolean isIsValidReservation() {
        return isValidReservation;
    }

    public void setIsValidReservation(boolean isValidReservation) {
        this.isValidReservation = isValidReservation;
    }

    public void getUserReservations(Objet objet) {
        items = getFacade().getUserReservations(SessionUtil.getConnectedUser(), null);
    }

    public void prepareReservation(Objet objet) {
        this.objet = objet;
        selected.setDureeLocation(configurationFacade.getDureeMinLocation().getValeur());
        System.out.println(selected.getDureeLocation());
    }

    public Integer getDureeLocation() {
        return dureeLocation;
    }

    public void setDureeLocation(Integer dureeLocation) {
        this.dureeLocation = dureeLocation;
    }

    public Objet getObjet() {
        return objet;
    }

    public void setObjet(Objet objet) {
        this.objet = objet;
    }

    public Reservation getSelected() {
        return selected;
    }

    public void setSelected(Reservation selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ReservationsFacade getFacade() {
        return ejbFacade;
    }

    public String getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(String idReservation) {
        this.idReservation = idReservation;
    }

    public Date getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(Date dateRetour) {
        this.dateRetour = dateRetour;
    }

    public String getIdObjet() {
        return idObjet;
    }

    public void setIdObjet(String idObjet) {
        this.idObjet = idObjet;
    }

    public Reservation prepareCreate() {
        selected = new Reservation();
        initializeEmbeddableKey();
        return selected;
    }

    public void prepareModification() {
        dureeLocation = selected.getDureeLocation();
    }

    public void create() {
        if (selected.getDureeLocation() < configurationFacade.getDureeMinLocation().getValeur()
                | selected.getDureeLocation() > configurationFacade.getDureeMinLocation().getValeur()) {
            JsfUtil.addErrorMessage("Durée de location incorrect");
        } else {
            persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ReservationCreated"));
        }
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void updateDureeLocation() {
        try {
            getFacade().updateReservationDureeLocation(selected, dureeLocation);
            JsfUtil.addSuccessMessage("Réservation modifiée avec sucess");
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, "Réservation modifiée avec sucess");
    }

    public void destroy() {
        persist(PersistAction.DELETE, "Réservation supprimée avec sucess");
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Reservation> getItems() {
        if (items == null) {
            items = getFacade().getUserReservations(SessionUtil.getConnectedUser(), null);
            selected = null;
        }
        return items;
    }

    public void setItems(List<Reservation> items) {
        this.items = items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().removeReservation(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Reservation getReservation(java.lang.String id) {
        return getFacade().find(id);
    }

    public List<Reservation> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Reservation> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Reservation.class)
    public static class ReservationControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ReservationController controller = (ReservationController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "reservationController");
            return controller.getReservation(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Reservation) {
                Reservation o = (Reservation) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Reservation.class.getName()});
                return null;
            }
        }

    }

}
