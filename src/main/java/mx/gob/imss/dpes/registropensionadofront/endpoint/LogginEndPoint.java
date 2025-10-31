/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadofront.endpoint;

import java.util.logging.Level;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.registropensionadofront.model.LogginRequest;
import mx.gob.imss.dpes.registropensionadofront.service.LogginService;
import mx.gob.imss.dpes.registropensionadofront.service.PerfilService;

/**
 *
 * @author edgar.arenas
 */
@Path("loggin")
@RequestScoped
public class LogginEndPoint extends BaseGUIEndPoint<BaseModel, BaseModel, BaseModel>{
    
    @Inject
    LogginService loggin;
    
    @Inject
    PerfilService perfil;
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response creaRegistroPensionado(LogginRequest request) throws BusinessException {
        log.log(Level.INFO, ">>>>----FRONT comienza validacion de credenciales");
        ServiceDefinition[] steps = {loggin, perfil};
        
        Message<LogginRequest> response = loggin.executeSteps(steps, new Message<>(request));
        log.log(Level.INFO, ">>>>FRONT Termina validacion de credenciales");
        
        return toResponse(response);
    }
}
