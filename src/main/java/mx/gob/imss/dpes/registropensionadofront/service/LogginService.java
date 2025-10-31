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
import mx.gob.imss.dpes.common.exception.UnknowException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.registropensionadofront.exception.UserOrPwdInvalidException;
import mx.gob.imss.dpes.registropensionadofront.model.LogginRequest;
import mx.gob.imss.dpes.registropensionadofront.model.UsuarioResponse;
import mx.gob.imss.dpes.registropensionadofront.restclient.RegistroPensionadoClient;
import mx.gob.imss.dpes.support.util.ExceptionUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class LogginService extends ServiceDefinition<LogginRequest, LogginRequest> {

    @Inject
    @RestClient
    private RegistroPensionadoClient client;

    @Override
    public Message<LogginRequest> execute(Message<LogginRequest> request) throws BusinessException {

        log.log(Level.INFO, ">>>>>>>----FRONT loggeo : {0}", request.getPayload());
        
        request.getPayload().setPassword(
            DigestUtils.sha256Hex(request.getPayload().getPassword())
        );
        
        Response response = null;
        try {
            response = client.loggeo(request.getPayload());
        } 
        catch (RuntimeException e) {
            e.printStackTrace();
            log.log(Level.SEVERE, ">>>>>>>  !!!--- ERROR: RuntimeException message=\n{0}", e.getMessage());
            if (e.getMessage().contains("Unknown error, status code 404") || e.getMessage().contains("Unknown error, status code 502")) {
                log.log(Level.SEVERE, ">>>>>>>  !!!--- ERROR: Servicio no encontrado {0}", "RegistroPensionadoBack");
                ExceptionUtils.throwServiceException("RegistroPensionadoBack");
            }
            if (e.getMessage().contains("Unknown error, status code 406")) {
                log.log(Level.SEVERE, ">>>>>>>  !!!--- ERROR UserOrPwdInvalidException");
                throw new UserOrPwdInvalidException();
            }

            throw new UnknowException();
        }

        log.log(Level.INFO, ">>>>>>>----FRONT after execute response.getStatus()= {0}", response.getStatus());

        if (response != null && response.getStatus() == 200) {
            UsuarioResponse user = response.readEntity(UsuarioResponse.class);
            request.getPayload().setUsuario(user);
            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new BadRequestException(), null);   
    }
}
