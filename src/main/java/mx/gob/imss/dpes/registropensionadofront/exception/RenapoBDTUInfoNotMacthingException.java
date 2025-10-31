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
public class RenapoBDTUInfoNotMacthingException extends LocalBusinessException {
    public final static String KEY = "msg346";
    
    public RenapoBDTUInfoNotMacthingException() {
        super(KEY);
    }
    
    public RenapoBDTUInfoNotMacthingException(String causa) {
        super(causa);
    }
}
