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
public class EMailFieldsNotMatchsException extends LocalBusinessException {

    public final static String KEY = "msg350";

    public EMailFieldsNotMatchsException() {
        super(KEY);

    }



public EMailFieldsNotMatchsException(List parameters) {
        super(KEY);
        super.addParameters(parameters);

    }

    public EMailFieldsNotMatchsException(String causa) {
        super(causa);
    }
}
