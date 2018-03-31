/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import bean.Categorie;
import bean.Objet;
import dto.ObjetDTO;
import exception.AppException;
import java.rmi.server.ObjID;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Marouane
 */
@Path("/objets")
public class ObjetRest {

    @EJB
    private service.ObjetFacade objetFacade;
    @EJB
    private service.CategorieFacade categorieFacade;

    @GET
    @Path("/libelle/{libelle}")
    @Produces(MediaType.APPLICATION_XML)
    public List<ObjetDTO> getObjetsByLibelle(@PathParam("libelle") String libelle) throws NotFoundException, AppException {
        return getObjetDTOsByCriteres(libelle, null);
    }

    @GET
    @Path("/categorie/{categorie}")
    @Produces(MediaType.APPLICATION_XML)
    public List<ObjetDTO> getObjetsByLibelleAndCategorie(@PathParam("categorie") String categorie) throws NotFoundException, AppException {
        return getObjetDTOsByCriteres(null, categorie);
    }

    @GET
    @Path("/libelle/{libelle}/categorie/{categorie}")
    @Produces(MediaType.APPLICATION_XML)
    public List<ObjetDTO> getObjetsByLibelleAndCategorie(@PathParam("libelle") String libelle,
            @PathParam("categorie") String categorie) throws NotFoundException, AppException {
        return getObjetDTOsByCriteres(libelle, categorie);
    }

    /**
     * Récuperer des objets DTO par libelle et catégorie
     *
     * @param libelle
     * @param categorie
     * @return: la liste des objets DTO
     * @throws AppException: exception dans le cas d'un objet non trouvé
     */
    private List<ObjetDTO> getObjetDTOsByCriteres(String libelle, String categorie) throws AppException {
        Categorie c = null;

        if (categorie != null) {
            c = categorieFacade.getCategorieByName(categorie);
            if (c == null) {
                throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),
                        404, "Catégorie non trouvée non trouvé");
            }
        }

        List<Objet> objets = objetFacade.getObjectsByCriteres(libelle, c);
        List<ObjetDTO> res = new ArrayList<>();

        if (objets.isEmpty()) {
            throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),
                    400, "Objet non trouvé");
        }

        objets.stream().forEach((Objet o) -> {
            String etatObjet = "Disponible";
            if (o.getEtatObjet() == 1) {
                etatObjet = "Réservé";
            } else if (o.getEtatObjet() == 2) {
                etatObjet = "En location";
            }

            res.add(new ObjetDTO(o.getLibelle(), o.getPrixLocation(), o.getCaution(), o.getAmendeDepassement(),
                    etatObjet, o.getDefauts(), o.getDescription(), o.getCategorie().getNom(),
                    o.getPointLocation().getNom()));
        });
        return res;
    }
}
