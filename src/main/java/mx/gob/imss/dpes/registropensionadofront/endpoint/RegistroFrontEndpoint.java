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
import mx.gob.imss.dpes.registropensionadofront.exception.EMailFieldsNotMatchsException;
import mx.gob.imss.dpes.registropensionadofront.model.RegistroRequest;
import mx.gob.imss.dpes.registropensionadofront.service.ComparaBDTU;
import mx.gob.imss.dpes.registropensionadofront.service.EnviaTokenPorCorreoService;
import mx.gob.imss.dpes.registropensionadofront.service.ObtenDatosRenapo;
import mx.gob.imss.dpes.registropensionadofront.service.PersistePersonaService;
import mx.gob.imss.dpes.registropensionadofront.service.RegistrarTokenUsuarioService;
import mx.gob.imss.dpes.registropensionadofront.service.SistrapOsbService;
import mx.gob.imss.dpes.registropensionadofront.service.ValidarUsuarioPersonaService;
import mx.gob.imss.dpes.registropensionadofront.service.ValidarUsuarioService;
import mx.gob.imss.dpes.support.util.ExceptionUtils;

/**
 *
 * @author eduardo.montesh
 */
@Path("registro")
@RequestScoped
public class RegistroFrontEndpoint extends BaseGUIEndPoint<BaseModel, BaseModel, BaseModel> {

    @Inject
    ObtenDatosRenapo renapo;

    @Inject
    ComparaBDTU bdtu;

    @Inject
    ValidarUsuarioService valUsuarioBD;
    
    @Inject
    ValidarUsuarioPersonaService valUsuarioPersona;
    
    @Inject
    RegistrarTokenUsuarioService registroUsuario;

    @Inject
    EnviaTokenPorCorreoService enviaToken;
    
    @Inject
    SistrapOsbService sistrapOsbService;
    
    @Inject
    PersistePersonaService persistePersona;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response creaRegistroPensionado(RegistroRequest registroReq) throws BusinessException {

        log.log(Level.INFO, "Ejecutando Servicio registroFront: {0} ", registroReq);

        switch (registroReq.getCvePerfil().intValue()) {

            case 1://Pensionado
                validateInput(registroReq);
                
                ServiceDefinition[] steps = {
                    renapo,
                    sistrapOsbService,
                    valUsuarioPersona,
                    valUsuarioBD,
                    registroUsuario,
                    enviaToken
                };
                
                Message<RegistroRequest> response = renapo.executeSteps(steps, new Message<>(registroReq));
                log.log(Level.INFO, "Termina Servicio registroFront.");
                
                return toResponse(response);
            case 2://Admin EF
            case 6://Operador IMSS

                ServiceDefinition[] stepsAdminEF = {valUsuarioBD, registroUsuario, enviaToken};

                Message<RegistroRequest> responseAdminEF = valUsuarioBD.executeSteps(stepsAdminEF, new Message<>(registroReq));
                log.log(Level.INFO, ">>>>registroPensionadoFront RegistroFrontEndpoint creaRegistroPensionado Después de llamar a renapo");
                return toResponse(responseAdminEF);
            case 3://Promotor EF
                
                ServiceDefinition[] stepsPromotor = {
                    renapo, 
                    bdtu, // ComparaBDTU
                    valUsuarioBD, 
                    registroUsuario, // RegistrarTokenUsuarioService
                    enviaToken};
                
                Message<RegistroRequest> responsePromotor = renapo.executeSteps(stepsPromotor, new Message<>(registroReq));
                
                log.log(Level.INFO, ">>>>registroPensionadoFront RegistroFrontEndpoint creaRegistroPromotor después de llamar a renapo");
                
                return toResponse(responsePromotor);
            
            case 4://Operador EF
                ServiceDefinition[] stepsOpEf = {renapo, bdtu, valUsuarioPersona, 
                    persistePersona, valUsuarioBD, registroUsuario, enviaToken};

                Message<RegistroRequest> responseOpEf = renapo.executeSteps(stepsOpEf, new Message<>(registroReq));
                log.log(Level.INFO, ">>>>registroPensionadoFront RegistroFrontEndpoint creaRegistroPensionado Después de llamar a renapo");
                return toResponse(responseOpEf);
            case 5://Admin IMSS

            default:
                break;

        }

        return toResponse(null);
    }

    private void validateInput(RegistroRequest registroReq) throws BusinessException {
        validateEMailFields(registroReq.getCorreo(), registroReq.getCorreoConfirmar());
        checkMandatoryFields(registroReq);
    }

    private void validateEMailFields(String email, String confirmEMail) throws BusinessException {
        if (email.compareTo(confirmEMail) != 0) {
            throw new EMailFieldsNotMatchsException();
        }
    }

    private void checkMandatoryFields(RegistroRequest registroReq) throws BusinessException {
        List<String> mandatoryFields = new LinkedList();
        mandatoryFields.add(registroReq.getCorreo());
        mandatoryFields.add(registroReq.getCorreoConfirmar());
        mandatoryFields.add(registroReq.getCurp());
        mandatoryFields.add(registroReq.getNss());
        mandatoryFields.add((registroReq.getNumTelefono() != null) ? registroReq.getNumTelefono().toString() : "");
        ExceptionUtils.checkMandatoryFields(mandatoryFields);

    }

    @POST
    @Path("/validate-usuario")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateUsuario(RegistroRequest registroReq) throws BusinessException {

        log.log(Level.INFO, ">>>>>>>>registroPensionadoFront RegistroFrontEndpoint  validateRegistroPensionado registroReq= {O} ", registroReq);

        ServiceDefinition[] steps = {valUsuarioBD};
        Message<RegistroRequest> response = valUsuarioBD.executeSteps(steps, new Message<>(registroReq));
        log.log(Level.INFO, ">>>>registroPensionadoFront RegistroFrontEndpoint validateRegistroPensionado response= {O} ", response);
        return toResponse(response);
    }

}
