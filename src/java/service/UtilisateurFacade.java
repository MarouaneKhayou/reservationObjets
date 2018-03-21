/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.PointLocation;
import bean.Utilisateur;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.PasswordUtil;

/**
 *
 * @author Marouane
 */
@Stateless
public class UtilisateurFacade extends AbstractFacade<Utilisateur> {

    @PersistenceContext(unitName = "reservationObjetsPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UtilisateurFacade() {
        super(Utilisateur.class);
    }

    /**
     * Récuperer la liste des employés d'un point de location
     *
     * @param pointLocation: le point de location
     * @return La liste des employés
     */
    public List<Utilisateur> getPointLocationEmployes(PointLocation pointLocation) {
        return em.createQuery("SELECT u FROM Utilisateur AS u WHERE u.profile=1 AND u.pointLocation.id=" + pointLocation.getId()).getResultList();
    }

    /**
     * Changer le mot de passe de l'utilisateur
     *
     * @param user: l'utilisateur
     * @param recentPassword: l'ancien mot de passe
     * @param newPassword: le nouveau mot de passe
     * @return -1: si l'ancien mot de passe est incorrect, 1: sinon
     */
    public int changeUserPassword(Utilisateur user, String recentPassword, String newPassword) {
        if (PasswordUtil.getHashedPassword(recentPassword).equals(user.getPassword())) {
            user.setPassword(PasswordUtil.getHashedPassword(newPassword));
            edit(user);
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * Modifier les information de l'utilisateur
     *
     * @param user: l'utilisateur
     * @param firstName: le nouveau prénom
     * @param lastName: le nouveau nom
     * @param mail: le nouveau email
     * @return -1: si le nouveau email est déjà exisrant, 1: si la modification
     * a bien été faite
     */
    public int updateUserInformation(Utilisateur user, String firstName, String lastName, String mail, String addresse) {
        if (!mail.equals("")) {
            if (ifMailExists(mail)) {
                return -1;
            }
            user.setEmail(mail);
        }
        if (!firstName.equals("")) {
            user.setPrenom(firstName);
        }
        if (!lastName.equals("")) {
            user.setNom(lastName);
        }
        if (!addresse.equals("")) {
            user.setAddresse(addresse);
        }
        edit(user);
        return 1;
    }

    /**
     * Connecter un utilisateur avec son email et mot de passe
     *
     * @param mail: l'email de l'utilisateur
     * @param password: Password
     * @return 1: si les dnnées saisié sont corrects, -1: si l'email ou bien le
     * mot de passe est incorrect
     */
    public int connexion(String mail, String password) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(mail);
        utilisateur.setPassword(password);
        Utilisateur loadedUser = getUserByMail(mail);
        if (loadedUser != null) {
            if (PasswordUtil.getHashedPassword(password).equals(loadedUser.getPassword())) {
                return 1;
            }
        }
        return -1;
    }

    /**
     * Créer un compte utilisateur normal
     *
     * @param utilisateur: l'utilisateur a créer
     * @return -1: si l'email saisie est déjà existant, 1: si le compte de
     * l'utilisateur est créer avec success
     */
    public int createNormalUser(Utilisateur utilisateur) {
        return createUserTemplate(utilisateur, 0);
    }

    /**
     * Créer un compte d'un employé
     *
     * @param utilisateur: l'utilisateur a créer
     * @return -1: si l'email saisie est déjà existant, 1: si le compte de
     * l'employé est créer avec success
     */
    public int createEmploye(Utilisateur utilisateur) {
        return createUserTemplate(utilisateur, 1);
    }

    /**
     * Methode privée qui permet la création d'un utilisateur utilisateur normal
     * ou bine employé
     *
     * @param utilisateur: l'utilisateur
     * @param profile: le profile 0: utilisateur normal, 1: employé
     * @return -1: si l'email saisie est déjà existant, 1: si le compte de
     * l'utilisateur est créer avec success
     */
    private int createUserTemplate(Utilisateur utilisateur, int profile) {
        if (ifMailExists(utilisateur.getEmail())) {
            return -1;
        }
        utilisateur.setEtatProfile(0);
        utilisateur.setPassword(PasswordUtil.getHashedPassword(utilisateur.getPassword()));
        utilisateur.setProfile(profile);
        create(utilisateur);
        return 1;
    }

    /**
     * Tester si un email correspond à un utilisateur dans la base de donnée
     *
     * @param email: l'email
     * @return true si un utilisateur qui a cet email, false sinon
     */
    public boolean ifMailExists(String email) {
        return getUserByMail(email) != null;
    }

    /**
     * Récupérer un utilisateur par son email
     *
     * @param email: l'email de l'utilisateur
     * @return l'utilisateur si l'email extsite, ou bien null
     */
    public Utilisateur getUserByMail(String email) {
        List<Utilisateur> res = em.createQuery("SELECT u FROM Utilisateur u WHERE u.etatProfile=0 AND u.email='" + email + "'").getResultList();
        if (res.isEmpty()) {
            return null;
        } else {
            return res.get(0);
        }
    }
}
