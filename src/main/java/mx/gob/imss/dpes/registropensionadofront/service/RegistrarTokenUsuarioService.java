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
import mx.gob.imss.dpes.common.exception.UnknowException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.registropensionadofront.model.RegistroRequest;
import mx.gob.imss.dpes.registropensionadofront.restclient.RegistroPensionadoClient;
import mx.gob.imss.dpes.support.util.ExceptionUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class RegistrarTokenUsuarioService extends ServiceDefinition<RegistroRequest, RegistroRequest> {

    @Inject
    @RestClient
    private RegistroPensionadoClient client;

    @Override
    public Message<RegistroRequest> execute(Message<RegistroRequest> request) throws BusinessException {

        log.log(Level.INFO, ">>>>>>>registroPensionadoFront RegistrarTokenUsuarioService request.getPayload()= {0}", request.getPayload());
        log.log(Level.INFO, ">>>>>>>registroPensionadoFront RegistrarTokenUsuarioService request.getPayload().getVigenciaToken = ''{0}''", request.getPayload().getVigenciaToken());
        Response event = null;
        try {
            event = client.create(request.getPayload());
        } catch (RuntimeException e) {
            e.printStackTrace();
            log.log(Level.SEVERE, ">>>>>>>  !!!--- ERROR: RuntimeException message=\n{0}", e.getMessage());
            if (e.getMessage().contains("Unknown error, status code 404") || e.getMessage().contains("Unknown error, status code 502")) {
                log.log(Level.SEVERE, ">>>>>>>  !!!--- ERROR: Servicio no encontrado {0}", "RegistroPensionadoBack");
                ExceptionUtils.throwServiceException("RegistroPensionadoBack");
            }
            throw new UnknowException();
        }

        if (event.getStatus() == 200) {
            RegistroRequest rr = event.readEntity(RegistroRequest.class);
            request.setPayload(rr);
            return request;
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new UnknowException(), null);
    }
}
