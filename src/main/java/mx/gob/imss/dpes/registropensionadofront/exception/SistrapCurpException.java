/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadofront.exception;

/**
 *
 * @author juan.garfias
 */
public class SistrapCurpException extends LocalBusinessException {
     public final static String KEY = "msg359";
    
    public SistrapCurpException() {
        super(KEY);
    }
    
    public SistrapCurpException(String causa) {
        super(causa);
    }
}
