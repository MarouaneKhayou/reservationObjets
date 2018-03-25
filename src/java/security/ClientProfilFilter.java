/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import bean.Utilisateur;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Marouane
 */
public class ClientProfilFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession s = ((HttpServletRequest) request).getSession();

        if (s.getAttribute("Utilisateur") != null) {
            if (((Utilisateur) s.getAttribute("Utilisateur")).getProfile() != 0) {
                ((HttpServletResponse) response).sendRedirect(request.getServletContext().getContextPath() + "/faces/index.xhtml");
            }
        } else {
            ((HttpServletResponse) response).sendRedirect(request.getServletContext().getContextPath() + "/faces/index.xhtml");
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

}
