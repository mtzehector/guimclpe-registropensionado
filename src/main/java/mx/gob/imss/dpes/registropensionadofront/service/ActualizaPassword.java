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
import mx.gob.imss.dpes.registropensionadofront.model.ActivarRegistroRequest;
import mx.gob.imss.dpes.registropensionadofront.model.RegistroRequest;
import mx.gob.imss.dpes.registropensionadofront.restclient.RegistroPensionadoClient;
import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class ActualizaPassword extends ServiceDefinition<ActivarRegistroRequest, ActivarRegistroRequest> {

    @Inject
    @RestClient
    private RegistroPensionadoClient client;

    @Override
    public Message<ActivarRegistroRequest> execute(Message<ActivarRegistroRequest> request) throws BusinessException {

        request.getPayload().setPassword(
                DigestUtils.sha256Hex(request.getPayload().getPassword())
        );
        
        log.log(Level.INFO, ">>>>>>>FRONT actualiza Password : {0}", request.getPayload());

        Response response = client.actualizarPassword(request.getPayload());

        if (response.getStatus() == 200) {
            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new BadRequestException(), null);
    }
}
