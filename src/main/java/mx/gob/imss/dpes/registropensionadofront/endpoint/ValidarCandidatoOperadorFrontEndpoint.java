/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadofront.endpoint;

import java.util.LinkedList;
import java.util.List;
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
import mx.gob.imss.dpes.registropensionadofront.model.RegistroRequest;
import mx.gob.imss.dpes.registropensionadofront.service.ObtenDatosRenapo;
import mx.gob.imss.dpes.registropensionadofront.service.ComparaBDTU;
import mx.gob.imss.dpes.registropensionadofront.service.RelacionLaboralService;
import mx.gob.imss.dpes.support.util.ExceptionUtils;

/**
 *
 * @author juan.garfias
 */
@Path("validar-candidato-operador")
@RequestScoped
public class ValidarCandidatoOperadorFrontEndpoint extends BaseGUIEndPoint<BaseModel, BaseModel, BaseModel> {

    @Inject
    ObtenDatosRenapo renapo;

    @Inject
    ComparaBDTU bdtu;

    @Inject
    RelacionLaboralService relacionLaboralService;


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response validaCandidatoOperador(RegistroRequest request) throws BusinessException {

        log.log(Level.INFO, "Recibe test RQ : " + request.getCurp() + " - " + request.getNss());

        validateInput(request);
        ServiceDefinition[] steps = {renapo, bdtu, relacionLaboralService};
        Message<RegistroRequest> response = renapo.executeSteps(steps, new Message<>(request));

        return toResponse(response);
    }

    private void validateInput(RegistroRequest registroReq) throws BusinessException {
        checkMandatoryFields(registroReq);
    }

    private void checkMandatoryFields(RegistroRequest registroReq) throws BusinessException {
        List<String> mandatoryFields = new LinkedList();
        mandatoryFields.add(registroReq.getCurp());
        mandatoryFields.add(registroReq.getNss());
        ExceptionUtils.checkMandatoryFields(mandatoryFields);
    }

}
