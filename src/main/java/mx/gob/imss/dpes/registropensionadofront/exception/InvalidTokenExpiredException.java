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
public class InvalidTokenExpiredException extends LocalBusinessException {
    public final static String KEY = "msg358";
    
    public InvalidTokenExpiredException() {
        super(KEY);
    }
    
    public InvalidTokenExpiredException(List parameters) {
       super(KEY);
       super.addParameters(parameters);
               
    }
    public InvalidTokenExpiredException(String causa) {
        super(causa);
    }
}
