/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadofront.rule;

import java.util.Calendar;
import java.util.logging.Level;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.enums.PerfilUsuarioEnum;
import mx.gob.imss.dpes.common.rule.BaseRule;
import mx.gob.imss.dpes.registropensionadofront.model.RegistroRequest;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class CalculoVigenciaToken extends BaseRule<RegistroRequest, RegistroRequest> {
   
    final Integer VIGENCIA = 3;
    final Integer MINUTES = 0;
    
    @Override
    public RegistroRequest apply(RegistroRequest request){
        Calendar c = Calendar.getInstance();
        //if(request.getCvePerfil() == PerfilUsuarioEnum.PENSIONADO.toValue()){
        //    c.add(Calendar.HOUR_OF_DAY, VIGENCIA);
        //    c.add(Calendar.MINUTE, MINUTES); 
        //    log.log(Level.INFO,">>>>VIGENCIA TOKEN HORAS = {0}", request.getVigenciaToken());
        //}else{
             c.add(Calendar.DAY_OF_WEEK, VIGENCIA);
             log.log(Level.INFO,">>>>VIGENCIA TOKEN DIAS = {0}", request.getVigenciaToken());
        //}
       
        request.setVigenciaToken(c.getTime());
        
        log.log(Level.INFO,">>>>registroPensionadoFront calculoVigenciaToken VIGENCIA TOKEN = {0}", request.getVigenciaToken());
        
        return request;
    }
}
