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
public class ResourceNotFoundRuntimeException extends RuntimeException {
     public ResourceNotFoundRuntimeException(String causa) {
        super(causa);
    }
}
