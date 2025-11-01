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
public class UserOrPwdInvalidRuntimeException extends RuntimeException {
     public UserOrPwdInvalidRuntimeException(String causa) {
        super(causa);
    }
}
