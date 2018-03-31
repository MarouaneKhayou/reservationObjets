package rest;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author Marouane
 */
@ApplicationPath("/api")
public class RestApiConfiguration extends Application {

    /**
     * Initialiser l'API Rest
     *
     * @return: la liste des classes Rest
     */
    @Override
    public Set<Class<?>> getClasses() {
        HashSet h = new HashSet<Class<?>>();
        h.add(ObjetRest.class);
        h.add(UtilisateurRest.class);
        return h;
    }
}
