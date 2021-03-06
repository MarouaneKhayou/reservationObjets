/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 *
 * @author Marouane
 */
public class AppExceptionMapper implements ExceptionMapper<AppException> {

    @Override
    public Response toResponse(AppException ex) {
        return Response.status(ex.getStatus())
                .entity(new ErrorMessage(ex))
                .build();
    }
}
