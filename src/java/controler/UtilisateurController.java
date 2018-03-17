package controler;

import bean.PointLocation;
import bean.Utilisateur;
import controler.util.JsfUtil;
import controler.util.JsfUtil.PersistAction;
import java.io.IOException;
import service.UtilisateurFacade;

import java.io.Serializable;
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
import util.SessionUtil;

@Named("utilisateurController")
@SessionScoped
public class UtilisateurController implements Serializable {

    @EJB
    private service.UtilisateurFacade ejbFacade;
    private List<Utilisateur> items = null;
    private Utilisateur selected;
    private String passord2;
    private String login;
    private String password;
    private Utilisateur newUser;
    private String newPassword;
    private String repeatPassword;
    private String recentPassword;
    private PointLocation selectedPointLocation;

    public UtilisateurController() {
    }

    /**
     * Afficher la liste des employés d'un point de location
     *
     * @param pointLocation: le point de location
     * @throws IOException
     */
    public void showEmployes(PointLocation pointLocation) throws IOException {
        this.selectedPointLocation = pointLocation;
        this.items = getFacade().getPointLocationEmployes(pointLocation);
        SessionUtil.redirect("/admin/employes/List.xhtml");
    }

    /**
     * Méthode pour permettre à l'utilisateur de changer son mot de passe de
     * l'utilisateur
     */
    public void changePassword() {
        if (newPassword.equals(repeatPassword)) {
            int res = getFacade().changeUserPassword(getConnectedUser(), recentPassword, newPassword);
            if (res == -1) {
                JsfUtil.addErrorMessage("Ancien mot de passe incorrect");
            } else if (res == 1) {
                JsfUtil.addSuccessMessage("Mot de passe modifié avec success");
                newPassword = "";
                recentPassword = "";
                repeatPassword = "";
            }
        } else {
            JsfUtil.addErrorMessage("Les deux mots de passe doivent se correpondre");
        }
    }

    /**
     * Changer les information de l'utilsateur
     */
    public void updateUserInformation() {
        if (newUser.getNom().equals("") & newUser.getPrenom().equals("") & newUser.getEmail().equals("")) {
            JsfUtil.addErrorMessage("Veuillez indroduire des informations");
        } else {
            int res = getFacade().updateUserInformation(getConnectedUser(), newUser.getPrenom(), newUser.getNom(),
                    newUser.getEmail());
            if (res == -1) {
                JsfUtil.addErrorMessage("Le mail saisie déjà existant");
            } else if (res == 1) {
                JsfUtil.addSuccessMessage("Information modifiées avec success");
                newUser = new Utilisateur();
            }
        }
    }

    /**
     * Déconnecter l'utilisateur connecté et supprimer a session
     *
     * @throws IOException
     */
    public void deconnexion() throws IOException {
        JsfUtil.addSuccessMessage("Déconnexion reussi!");
        SessionUtil.deconnexion();
        SessionUtil.goHome();
    }

    /**
     * Méthode de connexion
     *
     * @throws IOException
     */
    public void connexion() throws IOException {
        int res = getFacade().connexion(login, password);
        if (res == 1) {
            Utilisateur connectedUser = getFacade().getUserByMail(login);
            JsfUtil.addSuccessMessage("Connexion reussi!");
            SessionUtil.registerUser(connectedUser);
            SessionUtil.goHome();
        } else {
            JsfUtil.addErrorMessage("Email ou mot de passe incorrect");
        }
    }

    /**
     * Test si l'utilisateur est un administrateur
     *
     * @return true: si l'utilisateur est un admin, false: sinon
     */
    public boolean isUserNormal() {
        return testUserProfilTemplate(0);
    }

    /**
     * Test si l'utilisateur est un administrateur
     *
     * @return true: si l'utilisateur est un admin, false: sinon
     */
    public boolean isUserAdmin() {
        return testUserProfilTemplate(2);
    }

    /**
     * Test si l'utilisateur est un employe au sein d'un point de location
     *
     * @return true si l'utilisateur est un employé
     */
    public boolean isUserEmploye() {
        return testUserProfilTemplate(1);
    }

    /**
     * Méthode générique qui teste le profile de l'utilisateur
     *
     * @param profile: le profile de l'utilisateur, 0: utilisateur normal, 1:
     * Empoye, 2 Administrateur
     * @return
     */
    public boolean testUserProfilTemplate(int profile) {
        if (isUserConnected()) {
            return getConnectedUser().getProfile() == profile;
        }
        return false;
    }

    /**
     * Test si l'utilisateur est connecté ou non
     *
     * @return : true: si l'utilisateur est connecté, false: sinon
     */
    public boolean isUserConnected() {
        return getConnectedUser() != null;
    }

    /**
     * Récuperer l'utilisateur connecté à partir de la session
     *
     * @return: l'utilisateur connecté, ou bien null sinon
     */
    public Utilisateur getConnectedUser() {
        return SessionUtil.getConnectedUser();
    }

    /**
     * Rediriger l'utilisateur vers la page d'acceil
     *
     * @throws IOException
     */
    public void goHome() throws IOException {
        SessionUtil.goHome();
    }

    /**
     * Rediriger l'utilisateur vers la page de login
     *
     * @throws IOException
     */
    public void goLogin() throws IOException {
        SessionUtil.goLogin();
    }

    /**
     * Rediriger l'utilsateur vers la page d'inscription
     *
     * @throws IOException
     */
    public void goRegister() throws IOException {
        SessionUtil.goRegister();
    }

    /*
     * GETTERs and SETTERS*
     */
    public Utilisateur getNewUser() {
        if (newUser == null) {
            newUser = new Utilisateur();
        }
        return newUser;
    }

    public void setNewUser(Utilisateur newUser) {
        this.newUser = newUser;
    }

    public String getPassord2() {
        return passord2;
    }

    public void setPassord2(String passord2) {
        this.passord2 = passord2;
    }

    public String getLogin() {
        return login;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String getRecentPassword() {
        return recentPassword;
    }

    public PointLocation getSelectedPointLocation() {
        return selectedPointLocation;
    }

    public void setSelectedPointLocation(PointLocation selectedPointLocation) {
        this.selectedPointLocation = selectedPointLocation;
    }

    public void setRecentPassword(String recentPassword) {
        this.recentPassword = recentPassword;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Utilisateur getSelected() {
        if (selected == null) {
            selected = new Utilisateur();
        }
        return selected;
    }

    public void setSelected(Utilisateur selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private UtilisateurFacade getFacade() {
        return ejbFacade;
    }

    /**
     * GETTERs and SETTERS*
     */
    public void prepareCreate() {
        selected = new Utilisateur();
        initializeEmbeddableKey();
    }

    public void createEmploye() {
        if (selected != null) {
            if (selected.getPassword().equals(passord2)) {
                selected.setProfile(1);
                PointLocation pointLocation = new PointLocation();
                System.out.println("zzzzzz " + pointLocation.getId());
                pointLocation.setId(selectedPointLocation.getId());
                selected.setPointLocation(selectedPointLocation);
                persist(PersistAction.CREATE, "Compte créé avec success", true, false);
                this.items = getFacade().getPointLocationEmployes(pointLocation);
            } else {
                JsfUtil.addErrorMessage("Les deux mots de passe doivent se correspondre");
            }
        }
    }

    public void create() {
        if (selected != null) {
            if (selected.getEmail() == null | selected.getPassword().equals("") | passord2.equals("")) {
                JsfUtil.addErrorMessage("Erreur de validation veuillez indiquer les champs obligatoires");
            } else {
                System.out.println("");
                if (selected.getPassword().equals(passord2)) {
                    persist(PersistAction.CREATE, "Compte créé avec success", false, true);
                } else {
                    JsfUtil.addErrorMessage("Les deux mots de passe doivent se correspondre");
                }
            }

        }
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("UtilisateurUpdated"), false, false);
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("UtilisateurDeleted"), false, false);
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Utilisateur> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    /**
     * Méthode pour la création, modification et supprission d'un utilisateur au
     * niveau de la base de donnée
     *
     * @param persistAction: PersistAction.CREATE: pour la création,
     * PersistAction.UPDATE: pour la modification, PersistAction.DELETE: pour la
     * supprission,
     * @param successMessage: le message à afficher si l'une des opération CUD
     * est effectué avec success
     */
    private void persist(PersistAction persistAction, String successMessage, boolean isEmploye, boolean redirectToLogin) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction == PersistAction.CREATE) {
                    int res;
                    if (isEmploye) {
                        res = getFacade().createEmploye(selected);
                    } else {
                        res = getFacade().createNormalUser(selected);
                    }
                    if (res == -1) {
                        JsfUtil.addErrorMessage("Email saisie déja existant!");
                    } else if (res == 1) {
                        selected = new Utilisateur();
                        JsfUtil.addSuccessMessage(successMessage);
                        if (redirectToLogin) {
                            util.SessionUtil.goLogin();
                        }
                    }
                } else if (persistAction == PersistAction.UPDATE) {
                    getFacade().edit(selected);
                    JsfUtil.addSuccessMessage(successMessage);
                } else if (persistAction == PersistAction.DELETE) {
                    getFacade().remove(selected);
                    JsfUtil.addSuccessMessage(successMessage);
                }
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

    public Utilisateur getUtilisateur(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Utilisateur> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Utilisateur> getItemsAvailableSelectOne() {
        return getFacade().findAll();

    }

    @FacesConverter(forClass = Utilisateur.class)
    public static class UtilisateurControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UtilisateurController controller = (UtilisateurController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "utilisateurController");
            return controller.getUtilisateur(getKey(value));
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
            if (object instanceof Utilisateur) {
                Utilisateur o = (Utilisateur) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Utilisateur.class.getName()});
                return null;
            }
        }

    }

}
