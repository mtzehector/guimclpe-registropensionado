/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadofront.rule;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Date;
import java.util.logging.Level;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.rule.BaseRule;
import mx.gob.imss.dpes.registropensionadofront.model.TokenRegistroUsusario;
import mx.gob.imss.dpes.support.config.CustomDateDeserializer;
import mx.gob.imss.dpes.support.config.CustomDateSerializer;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class CompararFechas extends BaseRule<TokenRegistroUsusario, TokenRegistroUsusario>{
    
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonSerialize(using = CustomDateSerializer.class)
    Date fechaActual = new Date();
    Integer tokenVigente;
    
    @Override
     public TokenRegistroUsusario apply(TokenRegistroUsusario request){
        log.log(Level.INFO,">>>>Fecha de vigenciaTOKEN : {0}", request.getVigenciaToken());
        log.log(Level.INFO,">>>>Fecha de actual : {0}", fechaActual);
        tokenVigente = fechaActual.compareTo(request.getVigenciaToken());
        switch(tokenVigente){
            case 0:
            case -1:
                break;
            case 1:
                request.setIndActivo(0);
                break;
            default:
                break;
        }
        log.log(Level.INFO,">>>>TOKENVALIDO?? : {0}", request.getIndActivo());
        return request;
     }
   
    
}
