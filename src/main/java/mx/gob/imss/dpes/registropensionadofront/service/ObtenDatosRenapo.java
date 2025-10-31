/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadofront.service;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.renapo.model.RenapoCurpIn;
import mx.gob.imss.dpes.interfaces.renapo.model.RenapoCurpRequest;
import mx.gob.imss.dpes.registropensionadofront.exception.CurpInvalidStatusException;
import mx.gob.imss.dpes.registropensionadofront.exception.RenapoCurpException;
import mx.gob.imss.dpes.registropensionadofront.model.RegistroRequest;
import mx.gob.imss.dpes.renapo.service.RenapoCurpService;

/**
 *
 * @author eduardo.montesh
 */
@Provider
public class ObtenDatosRenapo extends ServiceDefinition<RegistroRequest, RegistroRequest> {

    @Inject
    RenapoCurpService renapo;

    @Override
    public Message<RegistroRequest> execute(Message<RegistroRequest> request) throws BusinessException {
        log.log(Level.INFO, ">>>>ObtenDatosRenapo Request: {0}", request.getPayload());
        RenapoCurpRequest renapoRequest = new RenapoCurpRequest();
        RenapoCurpIn in = new RenapoCurpIn();
        in.setCurp(request.getPayload().getCurp());
        renapoRequest.setRenapoCurpIn(in);
        Message<RenapoCurpRequest> response = new Message<>();

        try {
            response = renapo.execute(new Message<>(renapoRequest));
        } catch (BusinessException e) {
            throw new CurpInvalidStatusException("msg362");
        }
        log.log(Level.INFO, ">>>>  ObtenDatosRenapo response= {0}", response);

        if (!(response.getPayload().getRenapoCurpOut().getEstatusCURP().equals("AN")
                || response.getPayload().getRenapoCurpOut().getEstatusCURP().equals("AH")
                || response.getPayload().getRenapoCurpOut().getEstatusCURP().equals("CRA")
                || response.getPayload().getRenapoCurpOut().getEstatusCURP().equals("RCN")
                || response.getPayload().getRenapoCurpOut().getEstatusCURP().equals("RCC"))) {

            throw new CurpInvalidStatusException("msg359");
        }

        if (Message.isExito(response)) {
            request.getPayload().setRenapoRequest(response.getPayload());
            return request;
        }
        log.log(Level.SEVERE, "!!!   ERROR >>>>ObtenDatosRenapo Request: {0}", request.getPayload());
        return response(null, ServiceStatusEnum.EXCEPCION, new RenapoCurpException(), null);
    }

}
