/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import bean.Utilisateur;
import exception.AppException;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Marouane
 */
@Path("/utilisateur")
public class UtilisateurRest {

    @EJB
    private service.UtilisateurFacade utilisateurFacade;

    @POST
    @Path("/create")
    public String create(
            @FormParam("nom") String nom, @FormParam("prenom") String prenom,
            @FormParam("password") String password, @FormParam("email") String email,
            @FormParam("adresse") String adresse) throws AppException {
        Utilisateur utilisateur = new Utilisateur();

        utilisateur.setEmail(email);
        utilisateur.setNom(nom);
        utilisateur.setPrenom(prenom);
        utilisateur.setPassword(password);
        utilisateur.setAddresse(adresse);

        int res = utilisateurFacade.createNormalUser(utilisateur);
        if (res == -1) {
            throw new AppException(Response.Status.CONFLICT.getStatusCode(), 409, "Email saisé déjà existant");
        }
        return "Utilisateur crée avec success";
    }
}
