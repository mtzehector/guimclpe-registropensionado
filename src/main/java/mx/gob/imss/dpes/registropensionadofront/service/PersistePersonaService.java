package mx.gob.imss.dpes.registropensionadofront.service;

import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.UnknowException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.registropensionadofront.model.EstadoPersonaEf;
import mx.gob.imss.dpes.registropensionadofront.model.PersonaUpdateResponse;
import mx.gob.imss.dpes.registropensionadofront.model.PersonalUpdateResponse;
import mx.gob.imss.dpes.registropensionadofront.model.RegistroRequest;
import mx.gob.imss.dpes.registropensionadofront.restclient.PersonaBackClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author luisr.rodriguez
 */
@Provider
public class PersistePersonaService extends ServiceDefinition<RegistroRequest, RegistroRequest>  {
    @Inject
    @RestClient
    private PersonaBackClient personaClient;

    @Override
    public Message<RegistroRequest> execute(Message<RegistroRequest> request) throws BusinessException {
        log.log(Level.INFO, ">>>>>>> registroPensionadoFRONT PersistePersonaService : {0}", request.getPayload());
        if(request.getPayload().getCvePersona() != null && request.getPayload().getCvePersona() != 0L){
            try{
                PersonaUpdateResponse persona = new PersonaUpdateResponse();
                persona.setId(request.getPayload().getCvePersona());
                persona.setNumEmpleado(request.getPayload().getNumEmpleado());
                persona.setRegistroPatronal(request.getPayload().getRegistroPatronal());
                persona.setTelCelular(request.getPayload().getNumTelefono());
                persona.setCorreoElectronico(request.getPayload().getCorreo());
                persona.setBaja(0L);
                persona.setCveEstadoPersonaEf(new EstadoPersonaEf(1L, "Activo"));
                persona.setCveEntidadFinanciera(Long.parseLong(request.getPayload()
                        .getCveEntidadFinanciera() + ""));
                persona.setRfc(request.getPayload().getRfc());
                personaClient.update(persona);
                
                PersonalUpdateResponse personaEF = new PersonalUpdateResponse();
                personaEF.setCveCurp(request.getPayload().getCurp());
                personaEF.setNumEmpleado(request.getPayload().getNumEmpleado());
                personaEF.setCveEntidadFinanciera(Long.parseLong(request.getPayload()
                        .getCveEntidadFinanciera() + ""));
                personaClient.updatePersonaEF(personaEF);
                
                return request;
            }catch(RuntimeException ex){
                log.log(Level.SEVERE,ex.getMessage());
                throw new UnknowException();
            }
        }
        return request;
    }
    
}
