/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadofront.service;

import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BadRequestException;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.registropensionadofront.model.LogginRequest;
import mx.gob.imss.dpes.registropensionadofront.model.PerfilPersonaModel;
import mx.gob.imss.dpes.registropensionadofront.restclient.PerfilClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class PerfilService extends ServiceDefinition<LogginRequest, LogginRequest>{
    
    @Inject
    @RestClient
    private PerfilClient client;
    
    @Override
    public Message<LogginRequest> execute(Message<LogginRequest> request) throws BusinessException {
         log.log(Level.INFO, ">>>>>>>----FRONT Perfil : {0}", request.getPayload());
         PerfilPersonaModel perfil = new PerfilPersonaModel();
         perfil.setIdUsuario(request.getPayload().getUsuario().getPersona().getCveUsuario());
         Response response = client.getPerfil(perfil);
         
        if (response != null && response.getStatus() == 200) {
            PerfilPersonaModel perfilRes = response.readEntity(PerfilPersonaModel.class);
            request.getPayload().getUsuario().setPerfilPersona(perfilRes);
            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new BadRequestException(), null);   
    }
    
}
