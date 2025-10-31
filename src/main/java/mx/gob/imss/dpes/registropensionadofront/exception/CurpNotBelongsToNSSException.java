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
public class CurpNotBelongsToNSSException extends LocalBusinessException {
    public final static String KEY = "msg345";
    
    public CurpNotBelongsToNSSException() {
        super(KEY);
    }
    
    public CurpNotBelongsToNSSException(List parameters) {
       super(KEY);
       super.addParameters(parameters);
               
    }
    
    public CurpNotBelongsToNSSException(String causa) {
        super(causa);
    }
}
