/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadofront.service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Correo;
import mx.gob.imss.dpes.registropensionadofront.model.RegistroRequest;
import mx.gob.imss.dpes.registropensionadofront.restclient.EnviaCorreoClient;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class EnviaRecuperacionPasswordService extends ServiceDefinition<RegistroRequest, RegistroRequest>  {
    @Inject
    @RestClient
    EnviaCorreoClient correoClient;
    
    @Inject
    private Config config;
    
    @Override
    public Message<RegistroRequest> execute(Message<RegistroRequest> request) throws BusinessException {
        Correo correo = new Correo();
        List<String> correoList = new ArrayList<>();
        correoList.add(request.getPayload().getCorreo());
        
        correo.setCorreoPara(correoList);
        
        
        log.log(Level.INFO, "EnviaRecuperacionPasswordService: \n\n {0} \n\n", request);

        //String curp = request.getPayload().getCurp();
        //String curpCod = Base64.getEncoder().encodeToString(curp.getBytes());
        //log.log(Level.INFO, ">>>>>>>>CURPcod {0}", curpCod);
        String liga = config.getValue("ligaCambioContra", String.class);
        String plantilla = String.format(config.getValue("plantillaRecuperaPassword", String.class),
                            liga, request.getPayload().getToken());
        
        correo.setAsunto("Recuperacion contraseña");
        
        try {
            correo.setCuerpoCorreo(new String(plantilla.getBytes(), StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(EnviaTokenPorCorreoService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Response response = correoClient.enviaCorreo(correo);
        log.log(Level.INFO, ">>>>>>>>Después de enviar correo status={0}", response.getStatus());
        if (response.getStatus() == 200 || response.getStatus() == 204) {
            return request;
        }
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
