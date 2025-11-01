package mx.gob.imss.dpes.registropensionadofront.service;

import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.PersonaByCurpExistException;
import mx.gob.imss.dpes.common.exception.PersonaByEmailExistException;
import mx.gob.imss.dpes.common.exception.PersonaEFByCurpExistException;
import mx.gob.imss.dpes.common.exception.RegistroPatronalNoExisteException;
import mx.gob.imss.dpes.common.exception.UnknowException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.registropensionadofront.model.RegistroRequest;
import mx.gob.imss.dpes.registropensionadofront.restclient.PersonaBackClient;
import mx.gob.imss.dpes.registropensionadofront.restclient.RegistroUserPersonaClient;
import mx.gob.imss.dpes.registropensionadofront.rule.CalculoVigenciaToken;
import mx.gob.imss.dpes.registropensionadofront.rule.GenerarToken;
import mx.gob.imss.dpes.support.util.ExceptionUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class ValidarUsuarioPersonaService extends ServiceDefinition<RegistroRequest, RegistroRequest> {
    
  @Inject
  @RestClient
  private RegistroUserPersonaClient client;
  
  @Inject
  @RestClient
  private PersonaBackClient personaClient;
  
  @Inject
  private CalculoVigenciaToken rule1;
  
  @Inject
  private GenerarToken rule2;
    
    @Override
    public Message<RegistroRequest> execute(Message<RegistroRequest> request) throws BusinessException {

        log.log(Level.INFO, ">>>>>>> registroPensionadoFRONT ValidarUsuarioPersonaService Valida UsuarioPersona : {0}", request.getPayload());
        Integer validaUsuario = 0;
        int personaRP = 0;
        int personaCurp = 0;
        try {
            validaUsuario = client.validarUsuarioPersona(request.getPayload());
            personaCurp = personaClient.validateByCurp(request.getPayload().getCurp());
            if(request.getPayload().getRegistroPatronal() != null && !request.getPayload().getRegistroPatronal().equals(""))
            personaRP = personaClient.validateByRP(request.getPayload().getRegistroPatronal(),request.getPayload().getCveEntidadFinanciera().toString());
        } 
        
        catch (RuntimeException e) {
            e.printStackTrace();
            log.log(Level.SEVERE, ">>>>>>>  !!!--- ERROR: RuntimeException message=\n{0}", e.getMessage());
            if (e.getMessage().contains("Unknown error, status code 404") || e.getMessage().contains("Unknown error, status code 502")) {
                log.log(Level.SEVERE, ">>>>>>>  !!!--- ERROR: Servicio no encontrado {0}", "RegistroPensionadoBack");
                ExceptionUtils.throwServiceException("RegistroPensionadoBack");
            }
            if (e.getMessage().contains("Unknown error, status code 406")) {
                log.log(Level.SEVERE, ">>>>>>>  !!!--- ERROR: Servicio no encontrado {0}", "RegistroPensionadoBack");
                throw new PersonaByEmailExistException();
            }
            
            throw new UnknowException();
        }
        log.log(Level.INFO, ">>>>>>>registroPensionadoFRONT ValidarUsuarioPersonaService Usuario= {0}", validaUsuario);

        if (validaUsuario == 1) {
           throw new PersonaByEmailExistException();
        }
        if (personaCurp == 1) {
            throw new PersonaByCurpExistException();
        }
        if (personaCurp == 2) {
            throw new PersonaEFByCurpExistException();
        }
        if (personaRP == 3){
            throw new RegistroPatronalNoExisteException();
        }
        
        if(personaCurp != 0){
            request.getPayload().setCvePersona(Long.parseLong(personaCurp+""));
        }

        return new Message<>(request.getPayload());
    }
    

  
}
