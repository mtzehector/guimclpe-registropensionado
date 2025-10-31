/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadofront.service;

import java.util.ArrayList;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.CorreoSinAdjuntos;
import mx.gob.imss.dpes.registropensionadofront.model.ActivarRegistroRequest;
import mx.gob.imss.dpes.registropensionadofront.restclient.EnviaCorreoClient;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class EnviaCambioPasswordService extends ServiceDefinition<ActivarRegistroRequest, ActivarRegistroRequest> {
    @Inject
    @RestClient
    EnviaCorreoClient correoClient;
    
    @Inject
    Config config;
    
    @Override
    public Message<ActivarRegistroRequest> execute(Message<ActivarRegistroRequest> request) throws BusinessException {
        
        String cuerpoCorreo = config.getValue("plantillaCambioPassword", String.class);
        
        CorreoSinAdjuntos correo = new CorreoSinAdjuntos();
        correo.setAsunto("Cambio Exitoso");
        ArrayList<String> correos = new ArrayList<>();
        correos.add(request.getPayload().getInfoToken().getCorreo());
        correo.setCorreoPara(correos);
        correo.setCuerpoCorreo(cuerpoCorreo);
        
        log.log(Level.INFO, ">>>>>>>>Antes de enviar correo {0}", correo);
        Response response = correoClient.enviaCorreo(correo);
        log.log(Level.INFO, ">>>>>>>>Después de enviar correo status={0}", response.getStatus());
        
        if(response.getStatus() == 200 || response.getStatus() == 204) {
            return request;
        }
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    } 
}
