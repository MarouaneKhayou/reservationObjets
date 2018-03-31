package controler;

import bean.Categorie;
import bean.Objet;
import bean.PointLocation;
import bean.Reservation;
import bean.Utilisateur;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import java.io.IOException;
import service.ObjetFacade;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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
import org.primefaces.context.RequestContext;
import service.ConfigurationFacade;
import service.ReservationsFacade;
import service.UtilisateurFacade;
import util.DateUtil;
import util.RandomId;
import util.SessionUtil;

@Named("objetController")
@SessionScoped
public class ObjetController implements Serializable {

    @EJB
    private ObjetFacade ejbFacade;
    @EJB
    private ConfigurationFacade configurationFacade;
    @EJB
    private ReservationsFacade reservationsFacade;
    @EJB
    private UtilisateurFacade utilisateurFacade;

    private List<Objet> items = null;
    private Objet selected;
    private String prixLocationTemplate;
    private String cautionTemplate;
    private String amendeDepassementTemplate;
    private Categorie categorie;
    private PointLocation pointLocation;
    private String libelle;
    private Reservation reservation;
    private boolean showDialgue = false;

    public ObjetController() {
    }

    public void initParams() {
        items = null;
        categorie = null;
        pointLocation = null;
        libelle = "";
    }

    public void prepareCreateReservation() {
        reservation = new Reservation();
    }

    /**
     * Réserver un objet
     */
    public void reserverObjet() {
        //Test si la valeur entrée pour la durée de location est correcte 
        if (reservation.getDureeLocation() < configurationFacade.getDureeMinLocation().getValeur()
                | reservation.getDureeLocation() > configurationFacade.getDureeMaxLocation().getValeur()) {
            JsfUtil.addErrorMessage("Durée de location incorrect");
        } else {
            try {
                Utilisateur utilisateur = utilisateurFacade.getUserByMail(SessionUtil.getConnectedUser().getEmail());
                int nombreReservationEncoure = reservationsFacade.getUserReservationsEncours(utilisateur).size();

                //On test si le nombre de réservation en cours de l'utilisateur ne depasse pas le seuil 
                if (nombreReservationEncoure >= configurationFacade.getNombreMaxObjetLoue().getValeur()) {
                    JsfUtil.addErrorMessage("Vous avez attient le nombre maximal des objets réserver en meme temps");
                } else {
                    String idReservation;
                    do {
                        idReservation = RandomId.getRandomAlphanumricId(12);
                    } while (reservationsFacade.ifReservationIdExists(idReservation));

                    reservation.setIdReservation(idReservation);
                    reservation.setDureeMaxLocationAppliquee(configurationFacade.getDureeMaxLocation().getValeur());
                    reservation.setDureeMinLocationAppliquee(configurationFacade.getDureeMinLocation().getValeur());
                    reservation.setEtatReservation(0);
                    reservation.setDateReservation(new Date());
                    reservation.setUtilisateur(SessionUtil.getConnectedUser());

                    reservation.setDateLimiteRecuperation(DateUtil.addDaysToDate(new Date(), configurationFacade.getDureeMaxReservation().getValeur()));

                    reservation.setAmendeDepassementJournaliereAppliquee(configurationFacade.getAmendeDepassementJournaliere().getValeur());
                    reservation.setDureeMaxLocationAppliquee(configurationFacade.getDureeMaxLocation().getValeur());
                    reservation.setDureeMinLocationAppliquee(configurationFacade.getDureeMinLocation().getValeur());
                    reservation.setPrixLocationAppliquee(selected.getPrixLocation());

                    reservation.setObjet(selected);
                    reservationsFacade.createReservation(reservation);
                    showDialgue = true;
                    SessionUtil.goUserReservations();
                }

            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, "Erreur systeme !");
            }
        }
    }

    /**
     * Afficher une boite de dialogue avec le récapitulatif de la réservation
     */
    public void showReservationConfirmDialogue() {
        if (showDialgue) {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('myDialogVar').show();");
            showDialgue = false;
        }
    }

    public Reservation getReservation() {
        if (reservation == null) {
            reservation = new Reservation();
        }
        return reservation;
    }

    public void goHome() {
        try {
            SessionUtil.goHome();
        } catch (IOException ex) {
            Logger.getLogger(ObjetController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public boolean ifAvailible(Objet objet) {
        return objet.getEtatObjet() == 0;
    }

    public boolean ifisReservated(Objet objet) {
        return objet.getEtatObjet() == 1;
    }

    public List<Objet> getConnectedEmployeeObjets() {
        return getFacade().getObjectsByCriteres(SessionUtil.getConnectedUser().getPointLocation(), -1, libelle, categorie);
    }

    public void seachObjets() {
        items = getFacade().getObjectsByCriteres(libelle, categorie, pointLocation);
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public PointLocation getPointLocation() {
        if (pointLocation == null) {
            pointLocation = new PointLocation();
        }
        return pointLocation;
    }

    public void setPointLocation(PointLocation pointLocation) {
        this.pointLocation = pointLocation;
    }

    public String getLibelle() {
        if (libelle == null) {
            libelle = new String();
        }
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getPrixLocationTemplate() {
        return prixLocationTemplate;
    }

    public void setPrixLocationTemplate(String prixLocationTemplate) {
        this.prixLocationTemplate = prixLocationTemplate;
    }

    public String getCautionTemplate() {
        return cautionTemplate;
    }

    public void setCautionTemplate(String cautionTemplate) {
        this.cautionTemplate = cautionTemplate;
    }

    public String getAmendeDepassementTemplate() {
        return amendeDepassementTemplate;
    }

    public void setAmendeDepassementTemplate(String amendeDepassementTemplate) {
        this.amendeDepassementTemplate = amendeDepassementTemplate;
    }

    public Objet getSelected() {
        if (selected == null) {
            selected = new Objet();
        }
        return selected;
    }

    public void setSelected(Objet selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ObjetFacade getFacade() {
        return ejbFacade;
    }

    public String prepareView(Objet objet) {
        selected = objet;
        System.out.println("prepare : " + selected.getLibelle());
        return "/public/objet/Details.xhtml";
    }

    public void prepareEdit(Objet objet) {
        selected = objet;
    }

    public Objet prepareCreate() {
        selected = new Objet();
        prixLocationTemplate = "";
        cautionTemplate = "";
        amendeDepassementTemplate = "";
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        try {
            Double prixLocation = new Double(prixLocationTemplate);
            Double caution = new Double(prixLocationTemplate);
            Double amendeDepassement = new Double(prixLocationTemplate);
            selected.setPrixLocation(prixLocation);
            selected.setCaution(caution);
            selected.setAmendeDepassement(amendeDepassement);
            selected.setPointLocation(SessionUtil.getConnectedUser().getPointLocation());
            persist(PersistAction.CREATE, "Objet créé avec success");
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Erreur de validation de la valeur de l'un des champs");
        }

        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ObjetUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ObjetDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Objet> getItems() {
        if (items == null) {
            items = getFacade().getObjectsByCriteres(null, null, null);
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction == PersistAction.CREATE) {
                    String idObjet;
                    do {
                        idObjet = RandomId.getRandomAlphanumricId(12);
                    } while (getFacade().ifObjetIdExists(idObjet));
                    selected.setIdObjet(idObjet);
                    selected.setEtatObjet(0);
                    getFacade().edit(selected);
                } else if (persistAction == PersistAction.UPDATE) {
                    getFacade().edit(selected);
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

    public Objet getObjet(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Objet> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Objet> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Objet.class)
    public static class ObjetControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ObjetController controller = (ObjetController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "objetController");
            return controller.getObjet(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
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
            if (object instanceof Objet) {
                Objet o = (Objet) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Objet.class.getName()});
                return null;
            }
        }

    }

}
