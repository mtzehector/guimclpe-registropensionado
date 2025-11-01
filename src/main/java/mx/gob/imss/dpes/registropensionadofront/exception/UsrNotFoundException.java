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
public class UsrNotFoundException extends LocalBusinessException {
    public final static String KEY = "msg353";
    public final static String ERROR_AL_REGISTRAR_PENSIONADO_POR_EMAIL = "msg367";
    
    public UsrNotFoundException() {
        super(KEY);
    }
    
    public UsrNotFoundException(List parameters) {
       super(KEY);
       super.addParameters(parameters);
               
    }
    public UsrNotFoundException(String causa) {
        super(causa);
    }
}
