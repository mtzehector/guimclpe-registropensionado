/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.registropensionadofront.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
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
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.personaef.model.PersonaEF;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.registropensionadofront.exception.InvalidTokenException;
import mx.gob.imss.dpes.registropensionadofront.exception.RegistroPensionadoClienteException;
import mx.gob.imss.dpes.registropensionadofront.model.ActivarRegistroRequest;
import mx.gob.imss.dpes.registropensionadofront.restclient.DocumentoBackClient;
import mx.gob.imss.dpes.registropensionadofront.restclient.PersonaBackClient;
import mx.gob.imss.dpes.registropensionadofront.service.ActivaRegistroService;
import mx.gob.imss.dpes.registropensionadofront.service.EnviaConfirmacionRegistroUsuarioService;
import mx.gob.imss.dpes.registropensionadofront.service.ObtenDatosBDTUService;
import mx.gob.imss.dpes.registropensionadofront.service.ObtenInfoTokenService;
import mx.gob.imss.dpes.registropensionadofront.service.RenapoService;
import mx.gob.imss.dpes.registropensionadofront.service.ValidarDocumentoService;
import static org.springframework.http.converter.json.Jackson2ObjectMapperBuilder.json;

/**
 *
 * @author eduardo.montesh
 */
@Path("activar")
@RequestScoped
public class ActivarRegistroEndPoint extends BaseGUIEndPoint<BaseModel, BaseModel, BaseModel> {

    @Inject
    ObtenInfoTokenService infoTokenService;

    @Inject
    ObtenDatosBDTUService bdtuService;

    @Inject
    ActivaRegistroService persisteUsuario;

    @Inject
    EnviaConfirmacionRegistroUsuarioService enviaCorreoService;

    @Inject
    RenapoService renapo;

    @Inject
    PersonaBackClient personaBackClient;

    @Inject
    DocumentoBackClient docBackClient;
    
    @Inject
    ValidarDocumentoService validarDocumento;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response activaRegistroPensionado(ActivarRegistroRequest request) {
        try {

            //log.log(Level.INFO, ">>>>>>registroPensionadoFront ActivarRegistroEndPoint activaRegistroPensionado FRONT comienza activacion de password");

            Message<ActivarRegistroRequest> responseAux = infoTokenService.execute(new Message<>(request));

            //log.log(Level.INFO, ">>>>>>registroPensionadoFront ActivarRegistroEndPoint activaRegistroPensionado Datos del Token : {0}", responseAux);

            if (Message.isException(responseAux)) {
                throw new InvalidTokenException();
            }

            int cvePerfil = responseAux.getPayload().getInfoToken().getCvePerfil().intValue();

            switch (cvePerfil) {
                case 6:
                case 4:
                case 3: {
                    ServiceDefinition[] steps = {infoTokenService, renapo, persisteUsuario, enviaCorreoService};

                    Message<ActivarRegistroRequest> response = infoTokenService.executeSteps(steps, new Message<>(request));
                    //log.log(Level.INFO, ">>>>>>registroPensionadoFront ActivarRegistroEndPoint activaRegistroPensionado FRONT Termina activacion de password {0}", response.getPayload());

                    if (cvePerfil == 4) {
                        PersonaEF personaEF = new PersonaEF();
                        personaEF.setCurp(response.getPayload().getRenapoRequest().getRenapoCurpIn().getCurp());
                        personaEF.setCveCurp(response.getPayload().getRenapoRequest().getRenapoCurpIn().getCurp());
                        personaEF.setCveEntidadFinanciera(
                                response.getPayload().getInfoToken().getCveEntidadFinanciera().longValue()
                        );
                        personaEF.setNumEmpleado(
                                response.getPayload().getInfoToken().getNumEmpleado()
                        );
                        personaEF.setNss(
                                response.getPayload().getInfoToken().getNss()
                        );
                        personaEF.setCveTipoPersonaEf(2L);
                        personaEF.setIndRegistrado(1);
                        log.log(Level.INFO,
                                "personaBackClient.load()   FRONT Operador EF registro personal_EF Back {0}", personaEF);
                        personaBackClient.load(personaEF);

                        validarDocumento.execute(responseAux);

                    }

                    enviaCorreoService.execute(response);

                    return toResponse(response);

                }
                default: {
                    ServiceDefinition[] steps = {
                            infoTokenService,
                            renapo,
                            persisteUsuario,
                            enviaCorreoService};

                    Message<ActivarRegistroRequest> response = infoTokenService.executeSteps(steps, new Message<>(request));
                    //log.log(Level.INFO, ">>>>>>registroPensionadoFront ActivarRegistroEndPoint activaRegistroPensionado FRONT Termina activacion de password {0}", response.getPayload());

                    return toResponse(response);
                }
            }
        } catch (BusinessException be) {
            log.log(Level.SEVERE, "ActivarRegistroEndPoint.activaRegistroPensionado - request = [" + request + "]", be);
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, be, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, "ActivarRegistroEndPoint.activaRegistroPensionado - request = [" + request + "]", e);
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION,
                    new RegistroPensionadoClienteException(RegistroPensionadoClienteException.ERROR_DESCONOCIDO_EN_EL_SERVICIO),
                    null));
        }
    }
}
