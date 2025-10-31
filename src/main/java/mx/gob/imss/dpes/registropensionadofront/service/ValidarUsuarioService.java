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
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.registropensionadofront.exception.EMailExistException;
import mx.gob.imss.dpes.registropensionadofront.model.RegistroRequest;
import mx.gob.imss.dpes.registropensionadofront.model.TokenRegistroUsusario;
import mx.gob.imss.dpes.registropensionadofront.restclient.RegistroPensionadoClient;
import mx.gob.imss.dpes.registropensionadofront.rule.CalculoVigenciaToken;
import mx.gob.imss.dpes.registropensionadofront.rule.GenerarToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class ValidarUsuarioService extends ServiceDefinition<RegistroRequest, RegistroRequest> {
    
  @Inject
  @RestClient
  private RegistroPensionadoClient client;
  
  @Inject
  private CalculoVigenciaToken rule1;
  
  @Inject
  private GenerarToken rule2;
    
    @Override
    public Message<RegistroRequest> execute(Message<RegistroRequest> request) throws BusinessException {

        //log.log(Level.INFO, ">>>>>>>FRONT Valida Usuario : {0}", request.getPayload());
        Integer validaUsuario = 0;
        boolean tieneToken = false;
        try {
            validaUsuario = client.validarUsuario(request.getPayload());

            if (validaUsuario == 0) {
                //Valida la existencia de un token de creacion por el email
                Response responseTokenCreacion = client.obtenerTokenCreacionPorCorreo(request.getPayload().getCorreo());
                if (responseTokenCreacion.getStatus() == 200) {
                    TokenRegistroUsusario tk = responseTokenCreacion.readEntity(TokenRegistroUsusario.class);
                    log.log(Level.SEVERE, "ERROR CorreoTokenVencido tk: {0}", tk);
                    throw new EMailExistException();
                }
            }

            if (validaUsuario == 0) {
                rule1.apply(request.getPayload());
                rule2.apply(request.getPayload());
                return new Message<>(request.getPayload());
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ValidarUsuarioService.execute - request = [" + request + "]", e);
        }

        return response(null, ServiceStatusEnum.EXCEPCION, new EMailExistException(), null);
    }
    

  
}
