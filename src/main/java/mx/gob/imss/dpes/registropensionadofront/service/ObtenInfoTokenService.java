/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadofront.service;

import java.util.Date;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.UnknowException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.registropensionadofront.exception.InvalidTokenAlreadyUsedException;
import mx.gob.imss.dpes.registropensionadofront.exception.InvalidTokenException;
import mx.gob.imss.dpes.registropensionadofront.exception.InvalidTokenExpiredException;
import mx.gob.imss.dpes.registropensionadofront.model.ActivarRegistroRequest;
import mx.gob.imss.dpes.registropensionadofront.model.TokenRegistroUsusario;
import mx.gob.imss.dpes.registropensionadofront.restclient.RegistroPensionadoClient;
import mx.gob.imss.dpes.registropensionadofront.rule.CompararFechas;
import mx.gob.imss.dpes.support.util.ExceptionUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author eduardo.montesh
 */
@Provider
public class ObtenInfoTokenService extends ServiceDefinition<ActivarRegistroRequest, ActivarRegistroRequest> {

    @Inject
    @RestClient
    private RegistroPensionadoClient tokenInfoClient;

    @Inject
    private CompararFechas rule;

    @Override
    public Message<ActivarRegistroRequest> execute(Message<ActivarRegistroRequest> request) throws BusinessException {

        Response response = null;
        try {
            log.log(Level.INFO, ">>>>>>> ObtenInfoTokenService  request={0}", request);
            response = tokenInfoClient.obtenInfoToken(request.getPayload());
        } catch (RuntimeException e) {
            e.printStackTrace();
            log.log(Level.SEVERE, ">>>>>>> ObtenInfoTokenService  !!!--- ERROR: RuntimeException tokenInfoClient.toString()={0}", tokenInfoClient.toString());
            log.log(Level.SEVERE, ">>>>>>> ObtenInfoTokenService  !!!--- ERROR: RuntimeException message=\n{0}", e.getMessage());
            if (e.getMessage() != null) {
                if (e.getMessage().contains("Unknown error, status code 404") || e.getMessage().contains("Unknown error, status code 502")) {
                    log.log(Level.SEVERE, ">>>>>>> ObtenInfoTokenService !!!--- ERROR: Servicio no encontrado {0}", "RegistroPensionadoBack");
                    ExceptionUtils.throwServiceException("RegistroPensionadoBack");
                }
                if (e.getMessage().contains("Unknown error, status code 406")) {
                    log.log(Level.SEVERE, ">>>>>>>  !!!--- ERROR InvalidTokenException");
                    throw new InvalidTokenException();
                }
            }
            throw new UnknowException();
        }

        if (response == null) {
            throw new InvalidTokenException();
        }

        log.log(Level.INFO, ">>>>>>> ++++ObtenInfoTokenService response =\n{0}", response);

        if (response != null && response.getStatus() == 200) {
            TokenRegistroUsusario infoToken = response.readEntity(TokenRegistroUsusario.class);
            //rule.apply(infoToken);
            Date fechaActual = new Date();
            if (fechaActual.compareTo(infoToken.getVigenciaToken()) > 0) {
                log.log(Level.SEVERE, ">>>>>>>ERROR ObtenInfoTokenService Vigencia expirada= {0}", infoToken.getVigenciaToken());
                throw new InvalidTokenExpiredException();
            }
            if (infoToken.getIndActivo() == 0) {
                log.log(Level.INFO, ">>>>>>> ++++ObtenInfoTokenService infoToken.getIndActivo() == 0");
                //return response(null, ServiceStatusEnum.EXCEPCION, new BadRequestException(), null);
                throw new InvalidTokenAlreadyUsedException();
            } else {
                request.getPayload().setInfoToken(infoToken);
                log.log(Level.INFO, ">>>>>>>>>ObtenInfoTokenService FRONT Obtiene info de token {0}", request);
                return new Message<>(request.getPayload());
            }
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new InvalidTokenException(), null);
    }

}
