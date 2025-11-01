/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadofront.rule;

import java.util.UUID;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.rule.BaseRule;
import mx.gob.imss.dpes.registropensionadofront.model.RegistroRequest;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class GenerarToken extends BaseRule<RegistroRequest, RegistroRequest>{
    
    public RegistroRequest apply(RegistroRequest request){
       UUID codPart = UUID.randomUUID();
       if(request.getCurp() == null || request.getCurp().isEmpty()){
           String flat = codPart.toString();
           flat = flat + "-ac";
           request.setToken(flat);
           return request;
       }
        request.setToken(codPart.toString());

        return request;
    }
}
