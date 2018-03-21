package util;

import bean.Utilisateur;
import java.io.IOException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

public class SessionUtil {

    private static final SessionUtil instance = new SessionUtil();

    public static void goUserReservations() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.getFlash().setKeepMessages(true);
        ec.redirect(ec.getRequestContextPath() + "/faces/utilisateur/reservation/List.xhtml");
    }

    public static void goUserProfile() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.getFlash().setKeepMessages(true);
        ec.redirect(ec.getRequestContextPath() + "/faces/profilClient/profil.xhtml");
    }

    private SessionUtil() {
    }

    public static void goHome() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/");
    }

    public static void goLogin() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/faces/utilisateur/login.xhtml");
    }

    public static void goRegister() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/faces/utilisateur/register.xhtml");
    }

    public static void goPanier() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/faces/reservation/userPanier.xhtml");
    }

    /**
     * Ajouter un utilisateur à la session
     *
     * @param user : l'utilisateur qui vient de se connecter
     */
    public static void registerUser(Utilisateur utilisateur) {
        setAttribute("Utilisateur", utilisateur);
    }

    /**
     * Récuperer l'utilisateur connecté
     *
     * @return : l'utilisateur connecté
     */
    public static Utilisateur getConnectedUser() {
        return (Utilisateur) getAttribute("Utilisateur");
    }

    /**
     * Déconnecter l'utilisateur et supprimer le supprimer de la session
     */
    public static void deconnexion() {
        FacesContext fc = FacesContext.getCurrentInstance();
        getSession(fc).removeAttribute("Utilisateur");
        getSession(fc).invalidate();

    }

    public static SessionUtil getInstance() {
        return instance;
    }

    public static HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    }

    /**
     * Rediriger vers une URL
     *
     * @param pagePath
     * @throws IOException
     */
    public static void redirect(String pagePath) throws IOException {
        if (!pagePath.endsWith(".xhtml")) {
            pagePath += ".xhtml";
        }
        //FacesContext.getCurrentInstance().getExternalContext().redirect(pagePath);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/faces/" + pagePath);
    }

    private static boolean isContextOk(FacesContext fc) {
        boolean res = (fc != null
                && fc.getExternalContext() != null
                && fc.getExternalContext().getSession(false) != null);
        return res;
    }

    private static HttpSession getSession(FacesContext fc) {
        return (HttpSession) fc.getExternalContext().getSession(false);
    }

    public static Object getAttribute(String cle) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Object res = null;
        if (isContextOk(fc)) {
            res = getSession(fc).getAttribute(cle);
        }
        return res;
    }

    public static void setAttribute(String cle, Object valeur) {
        FacesContext fc = FacesContext.getCurrentInstance();
        if (fc != null && fc.getExternalContext() != null) {
            getSession(fc).setAttribute(cle, valeur);
        }
    }
}
