/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Marouane
 */
public class AppException extends Exception {

    private static final long serialVersionUID = -8999932578270387947L;

    Integer status;
    int code;

    public AppException(int status, int code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public AppException() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
