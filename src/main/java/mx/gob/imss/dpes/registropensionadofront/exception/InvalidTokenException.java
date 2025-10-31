/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadofront.exception;

import java.util.List;

/**
 *
 * @author eduardo.montesh
 */
public class InvalidTokenException extends LocalBusinessException {
    public final static String KEY = "msg354";
    public final static String ERROR_EN_REGISTRO_USUARIO = "msg369";
    
    public InvalidTokenException() {
        super(KEY);
    }
    
    public InvalidTokenException(List parameters) {
       super(KEY);
       super.addParameters(parameters);
               
    }
    public InvalidTokenException(String causa) {
        super(causa);
    }
}
