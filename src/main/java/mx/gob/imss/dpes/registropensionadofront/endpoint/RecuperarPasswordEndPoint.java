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
import mx.gob.imss.dpes.registropensionadofront.model.ActivarRegistroRequest;
import mx.gob.imss.dpes.registropensionadofront.model.RegistroRequest;
import mx.gob.imss.dpes.registropensionadofront.service.ActualizaPassword;
import mx.gob.imss.dpes.registropensionadofront.service.EnviaCambioPasswordService;
import mx.gob.imss.dpes.registropensionadofront.service.EnviaConfirmacionRegistroUsuarioService;
import mx.gob.imss.dpes.registropensionadofront.service.EnviaRecuperacionPasswordService;
import mx.gob.imss.dpes.registropensionadofront.service.EnviaTokenPorCorreoService;
import mx.gob.imss.dpes.registropensionadofront.service.ObtenInfoTokenService;
import mx.gob.imss.dpes.registropensionadofront.service.RecuperarPasswordService;
import mx.gob.imss.dpes.registropensionadofront.service.RegistrarTokenUsuarioService;

/**
 *
 * @author edgar.arenas
 */
@Path("actualizar")
@RequestScoped
public class RecuperarPasswordEndPoint extends BaseGUIEndPoint<BaseModel, BaseModel, BaseModel>{
    
    @Inject
    RecuperarPasswordService validarUsuario;
    
    @Inject
    ObtenInfoTokenService infoTokenService;
    
    @Inject
    RegistrarTokenUsuarioService persisteToken;
    
    @Inject
    EnviaRecuperacionPasswordService enviarToken;
    
    @Inject
    EnviaCambioPasswordService enviarConfirmacion;
    
     @Inject
    ActualizaPassword actualizarPassword;
    
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response recuperaPasswordPensionado(RegistroRequest registroReq) throws BusinessException {
        log.log(Level.INFO, ">>>FRONT Comienza recuperaPasswordPensionado {0}", registroReq.getCorreo());
        ServiceDefinition[] steps = {validarUsuario, persisteToken, enviarToken};
        
        Message<RegistroRequest> response = validarUsuario.executeSteps(steps, new Message<>(registroReq));
        log.log(Level.INFO, ">>>FRONT Termina recuperaPasswordPensionado");
        
        return Response.ok().build();
    }
    
    @POST
    @Path("/password")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response actualizarPassword(ActivarRegistroRequest actualizacion) throws BusinessException {
        log.log(Level.INFO, ">>>FRONT actualizarPassword {0}", actualizacion);
        ServiceDefinition[] steps = {infoTokenService, actualizarPassword, enviarConfirmacion};
        
        Message<ActivarRegistroRequest> response = infoTokenService.executeSteps(steps, new Message<>(actualizacion));
        log.log(Level.INFO, ">>>FRONT Termina actualizarPassword");
        
        return toResponse(response);
    } 
    
    @POST
    @Path("/actualizaDatos")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response actualizaPasswordPensionado(RegistroRequest registroReq) throws BusinessException {
        log.log(Level.INFO, ">>>FRONT Comienza recuperaPasswordPensionado {0}", registroReq.getCorreo());
        ServiceDefinition[] steps = {validarUsuario, persisteToken};
        
        Message<RegistroRequest> response = validarUsuario.executeSteps(steps, new Message<>(registroReq));
        log.log(Level.INFO, ">>>FRONT Termina recuperaPasswordPensionado");
        
        return toResponse(response);
    }
}
