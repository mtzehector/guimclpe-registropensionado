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
public class InvalidTokenAlreadyUsedException extends LocalBusinessException {
    public final static String KEY = "msg357";
    
    public InvalidTokenAlreadyUsedException() {
        super(KEY);
    }
    
    public InvalidTokenAlreadyUsedException(List parameters) {
       super(KEY);
       super.addParameters(parameters);
               
    }
    public InvalidTokenAlreadyUsedException(String causa) {
        super(causa);
    }
}
