/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadofront.exception;

/**
 *
 * @author eduardo.montesh
 */
public class RenapoCurpException extends LocalBusinessException {
    public final static String KEY = "msg344";
    
    public RenapoCurpException() {
        super(KEY);
    }
    
    public RenapoCurpException(String causa) {
        super(causa);
    }
}
